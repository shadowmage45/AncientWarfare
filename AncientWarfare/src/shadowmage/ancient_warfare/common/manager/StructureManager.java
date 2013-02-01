/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
   Please see COPYING for precise license information.

   This file is part of Ancient Warfare.

   Ancient Warfare is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Ancient Warfare is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Ancient Warfare.  If not, see <http://www.gnu.org/licenses/>.
 */
package shadowmage.ancient_warfare.common.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.Packet01ModData;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;

public class StructureManager
{

//TODO change these to hashmap by name
private static List<ProcessedStructure> structures = new ArrayList<ProcessedStructure>();
private static List<StructureClientInfo> clientStructures = new ArrayList<StructureClientInfo>();

private static HashMap<String, ProcessedStructure> tempBuilderStructures = new HashMap<String, ProcessedStructure>();
private static StructureClientInfo tempBuilderClientInfo;


private StructureGeneratorSelector structureSelector = new StructureGeneratorSelector();

private StructureManager(){}
private static StructureManager INSTANCE;
public static StructureManager instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE=new StructureManager();
    }
  return INSTANCE;
  }

private class StructureGeneratorSelector
{

private List<ProcessedStructure> structures = new ArrayList<ProcessedStructure>();

private int totalWeight;


public ProcessedStructure getRandomWeightedStructure(Random random)
  {
  if(this.structures.size()>0 && totalWeight >0)
    {
    int check = random.nextInt(totalWeight);
    for(ProcessedStructure struct : this.structures)
      {
      if(check>=struct.structureWeight)
        {
        check-=struct.structureWeight;
        }
      else
        {
        return struct;
        }
      }
    }
  return null;
  }

public ProcessedStructure getRandomBelow(Random rand, int val)
  {
  ArrayList<ProcessedStructure> structs = new ArrayList<ProcessedStructure>();
  int foundWeight = 0;
  for(ProcessedStructure struct : this.structures)
    {
    if(struct!=null && struct.structureValue<=val)
      {
      structs.add(struct);
      foundWeight += struct.structureWeight;
      }
    }
  if(foundWeight<=0)
    {
    return null;
    }
  int check = rand.nextInt(foundWeight);
  for(ProcessedStructure struct : structs)
    {
    if(check>=struct.structureWeight)
      {
      check-=struct.structureWeight;
      }
    else
      {
      return struct;
      }
    }
  return null;
  }

public void addStructure(ProcessedStructure struct)
  {
  if(struct!=null)
    {
    this.structures.add(struct);
    this.totalWeight += struct.structureWeight;
    }
  }


}

private class StructureDistanceList
{
public int chunkDistance;
/**
 * this could totally be a list of strings...and then pull the actual struct from struct manager...
 * ....or not....
 */
private List<ProcessedStructure> structures = new ArrayList<ProcessedStructure>();
private int totalBinWeight;

public StructureDistanceList(int dist)
  {
  this.chunkDistance = dist;
  }

public ProcessedStructure getRandomSelection(Random rnd)
  { 
  int sel = rnd.nextInt(totalBinWeight+1);
  for(ProcessedStructure struct : this.structures)
    {
    if(sel>struct.structureWeight)
      {
      sel -= struct.structureWeight;
      }
    else
      {
      return struct;
      }
    }
  return null;
  }

public void addStructure(ProcessedStructure struct)
  {
  this.structures.add(struct);
  this.totalBinWeight += struct.structureWeight;
  }

public int getEntrySize()
  {
  return this.structures.size();
  }

public int getBinWeight()
  {  
  return totalBinWeight * (this.chunkDistance+1)^8 ;
  }
}//////////////********** END STRUCTUREWEIGHTLIST ************///////////////

public ProcessedStructure getRandomWeightedStructure(Random rand)
  {
  return this.structureSelector.getRandomWeightedStructure(rand);
  }

public ProcessedStructure getRandomWeightedStructureBelowValue(Random rand, int val)
  {
  return this.structureSelector.getRandomBelow(rand, val);
  }

private Packet01ModData constructPacket(NBTTagCompound tag)
  {
  Packet01ModData pkt = new Packet01ModData();
  pkt.setStructData(tag);
  return pkt;
  }

/************************************* SERVER METHODS ************************************/
public void handlePacketDataServer(NBTTagCompound tag)
  {
  //TODO??
  }

public ProcessedStructure getStructureServer(String name)
  {
  Iterator<ProcessedStructure> it = structures.iterator();
  ProcessedStructure struct;
  while(it.hasNext())
    {
    struct = it.next();
    if(struct.name.equals(name))
      {
      return struct;
      }
    }
  return null;
  }

/**
 * server side method to add a temp structure and relay to ITS client
 * @param tag
 */
public void addTempStructure(EntityPlayer player, ProcessedStructure struct)
  {
  this.tempBuilderStructures.put(String.valueOf(player.getEntityName()), struct);
  
  NBTTagCompound structData = new NBTTagCompound();
  structData.setCompoundTag("addTemp", StructureClientInfo.getClientTag(struct));
  AWCore.proxy.sendPacketToPlayer(player, constructPacket(structData));
  }

/**
 * server side method to add a structure, relays client data to all logged in clients to update
 * their structure map
 * @param struct
 */
public void addStructure(ProcessedStructure struct, boolean sendPacket)
  {
  structures.add(struct);
  
  if(struct.worldGen)
    {
    this.structureSelector.addStructure(struct);    
    }
  if(sendPacket)
    {
    NBTTagCompound structData = new NBTTagCompound();
    structData.setCompoundTag("add", StructureClientInfo.getClientTag(struct));   
    AWCore.proxy.sendPacketToAllPlayers(constructPacket(structData));
    }
  }

/**
 * clear structure data, and add an entire list of structures to it
 * DOES NOT RELAY CHANGES TO CLIENTS, THIS IS A LOAD/INIT METHOD ONLY
 * @param structs
 */
public void addStructures(List<ProcessedStructure> structs)
  {
  structures.clear();
  this.structureSelector.structures.clear();
  for(ProcessedStructure struct : structs)
    {
    this.addStructure(struct, false);
    } 
  Config.logDebug("Sucessfully loaded: "+structures.size()+" structures!");  
  }

public void handlePlayerLogin(EntityPlayer player)
  {
  if(!player.worldObj.isRemote)
    {
    NBTTagCompound structData = new NBTTagCompound();
    structData.setTag("structInit", this.getClientInitData());
    AWCore.proxy.sendPacketToPlayer(player, constructPacket(structData));
    }
  }

private NBTTagList getClientInitData()
  {
  NBTTagList list = new NBTTagList();
  Iterator<ProcessedStructure> it = structures.iterator();
  ProcessedStructure structure;  
  while(it.hasNext())
    {    
    structure = it.next();    
    list.appendTag(StructureClientInfo.getClientTag(structure));    
    }  
  return list;
  }

/**
 * SERVER SIDE
 * @param playerName
 * @return
 */
public ProcessedStructure getTempStructure(String playerName)
  {
  return this.tempBuilderStructures.get(String.valueOf(playerName));
  }

/********************************** CLIENT METHODS *********************************/

public void handlePacketDataClient(NBTTagCompound tag)
  {
  if(tag.hasKey("structInit"))
    {
    Config.logDebug("Receiving client struct init list");
    this.handleInitClient(tag.getTagList("structInit"));
    }
  if(tag.hasKey("add"))
    {
    addClientStructureFromNBT(tag.getCompoundTag("add"));
    }
  if(tag.hasKey("remove"))
    {
    removeClientStructure(tag.getString("remove"));
    }
  if(tag.hasKey("addTemp"))
    {
    addTempClientInfo(tag.getCompoundTag("addTemp"));
    }
  }

public void addTempClientInfo(NBTTagCompound tag)
  {
  Config.logDebug("Setting client side temp structure");
  this.tempBuilderClientInfo = new StructureClientInfo(tag);
  }

public boolean isValidStructureClient(String name)
  {
  for(StructureClientInfo struct : this.clientStructures)
    {
    if(struct.name.equals(name))
      {
      return true;
      }
    }
  return false;
  }

private void handleInitClient(NBTTagList list)
  {
  this.clientStructures.clear();
  NBTTagCompound tag;
  for(int i = 0; i < list.tagCount(); i++)
    {
    tag = (NBTTagCompound) list.tagAt(i);
    this.clientStructures.add(new StructureClientInfo(tag));
    }
  Config.logDebug("Added "+this.clientStructures.size()+" structures to client map");
  }

private void addClientStructureFromNBT(NBTTagCompound tag)
  {
  this.clientStructures.add(new StructureClientInfo(tag));
  }

private void removeClientStructure(String name)
  {
  Iterator<StructureClientInfo> it = clientStructures.iterator();
  StructureClientInfo info;
  while(it.hasNext())
    {
    info = it.next();
    if(info.name.equals(name))
      {
      it.remove();
      break;
      }
    }  
  }

public void clearClientData()
  {
  this.clientStructures.clear();
  }

public StructureClientInfo getClientStructure(String name)
  {
  for(StructureClientInfo info : this.clientStructures)
    {
    if(info.name.equals(name))
      {
      return info;
      }
    }
  return null;
  }

public StructureClientInfo getClientTempStructure()
  {
  return this.tempBuilderClientInfo;
  }

public List<StructureClientInfo> getClientStructures()
  {
  return this.clientStructures;
  }

}
