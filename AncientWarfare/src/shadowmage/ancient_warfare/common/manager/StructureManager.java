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


private static List<ProcessedStructure> structures = new ArrayList<ProcessedStructure>();
private static List<StructureClientInfo> clientStructures = new ArrayList<StructureClientInfo>();

private static  HashMap<String, ProcessedStructure> tempBuilderStructures = new HashMap<String, ProcessedStructure>();
private static  StructureClientInfo tempBuilderClientInfo;


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
public void addStructure(ProcessedStructure struct)
  {
  structures.add(struct);
  
  NBTTagCompound structData = new NBTTagCompound();
  structData.setCompoundTag("add", StructureClientInfo.getClientTag(struct));   
  AWCore.proxy.sendPacketToAllPlayers(constructPacket(structData));
  }

/**
 * clear structure data, and add an entire list of structures to it
 * DOES NOT RELAY CHANGES TO CLIENTS, THIS IS A LOAD/INIT METHOD ONLY
 * @param structs
 */
public void addStructures(List<ProcessedStructure> structs)
  {
  structures.clear();
  structures.addAll(structs);
  if(Config.DEBUG)
    {
    System.out.println("Sucessfully loaded: "+structures.size()+" structures!");
    }
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
