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
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.Packet01ModData;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;

/**
 * Manages server side processed structures, and their client-side data equivalents
 * @author Shadowmage
 *
 */
public class StructureManager
{

//TODO change these to hashmap by name
private static Map<String, ProcessedStructure> structures = new HashMap<String, ProcessedStructure>();
private static Map<String, StructureClientInfo> clientStructures = new HashMap<String, StructureClientInfo>();

/**
 * playerName, scannedStructure
 */
private static HashMap<String, ProcessedStructure> tempBuilderStructures = new HashMap<String, ProcessedStructure>();

/**
 * current client-side scannedStructure (used by ItemBuilderDirect)
 */
private static StructureClientInfo tempBuilderClientInfo;


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
  return this.structures.get(name);
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
 * their structure map. does not add struct to world gen selection, needs manual adding..
 * @param struct
 */
public void addStructure(ProcessedStructure struct, boolean sendPacket)
  {
  Config.logDebug("adding structure(server): "+struct.name);
  this.structures.put(struct.name, struct);
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
  for(String name : this.structures.keySet())
    {
    ProcessedStructure structure = this.structures.get(name);
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
  if(this.clientStructures.containsKey(name))
    {
    return true;
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
    StructureClientInfo info = new StructureClientInfo(tag);
    this.clientStructures.put(info.name, info);
    }
  Config.logDebug("Added "+this.clientStructures.size()+" structures to client map");
  }

private void addClientStructureFromNBT(NBTTagCompound tag)
  {
  StructureClientInfo info = new StructureClientInfo(tag);
  this.clientStructures.put(info.name, info);
  }

private void removeClientStructure(String name)
  {
  if(this.clientStructures.containsKey(name))
    {
    this.clientStructures.remove(name);
    }  
  }

public void clearClientData()
  {
  this.clientStructures.clear();
  this.tempBuilderClientInfo = null;  
  }

public StructureClientInfo getClientStructure(String name)
  {
  return this.clientStructures.get(name);
  }

public StructureClientInfo getClientTempStructure()
  {
  return this.tempBuilderClientInfo;
  }

public void clearClientTempInfo()
  {
  this.tempBuilderClientInfo = null;
  }

public List<StructureClientInfo> getClientStructures()
  {
  ArrayList<StructureClientInfo> clientStructures = new ArrayList<StructureClientInfo>();
  for(String name : this.clientStructures.keySet())
    {
    StructureClientInfo struct = this.clientStructures.get(name);
    if(struct!=null)
      {
      clientStructures.add(struct);
      }
    }
  return clientStructures;
  }

}
