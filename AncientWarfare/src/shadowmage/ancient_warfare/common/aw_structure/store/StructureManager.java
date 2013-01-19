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
package shadowmage.ancient_warfare.common.aw_structure.store;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.client.aw_structure.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.aw_core.AWCore;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.network.Packet01ModData;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class StructureManager
{


private static List<ProcessedStructure> structures = new ArrayList<ProcessedStructure>();
private static List<StructureClientInfo> clientStructures = new ArrayList<StructureClientInfo>();


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

public ProcessedStructure getStructure(String name)
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

public void addStructure(ProcessedStructure struct)
  {
  structures.add(struct);
  }

public void addStructures(List<ProcessedStructure> structs)
  {
  structures.addAll(structs);
  if(Config.DEBUG)
    {
    System.out.println("loaded: "+structures.size()+" structures!");
    }
  }

public List<StructureClientInfo> getClientStructures()
  { 
  return clientStructures;
  }

public List<ProcessedStructure> getStructureList()
  {
  return structures;
  }

public void handlePlayerLogin(EntityPlayer player)
  {
  if(!player.worldObj.isRemote)
    {
    Packet01ModData init = new Packet01ModData();
    init.packetData.setBoolean("struct", true);
    init.packetData.setCompoundTag("structInit", getClientInitData());
    AWCore.proxy.sendPacketToPlayer(player, init);
    }
  }

private NBTTagCompound getClientInitData()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList list = new NBTTagList();
  Iterator<ProcessedStructure> it = structures.iterator();
  ProcessedStructure structure;  
  while(it.hasNext())
    {    
    structure = it.next();    
    list.appendTag(StructureClientInfo.getClientTag(structure));    
    }  
  tag.setTag("initList", list);
  System.out.println("setting data to send to client. size: "+list.tagCount());
  return tag;
  }

public void handlePacketData(NBTTagCompound tag, World world)
  {
  if(world.isRemote)
    {
    this.handleUpdateClient(tag);
    }
  else
    {
    this.handleUpdateServer(tag);
    }
  }

public void handleInitClient(NBTTagCompound tag)
  {
  System.out.println("Handling client structure init data.  Current size: "+clientStructures.size());
  if(tag.hasKey("initList"))
    {
    this.addClientStructuresFromNBT(tag.getTagList("initList"));
    }
  System.out.println("Client structure init data loaded.  Loaded size: "+clientStructures.size());
  }

public void handleUpdateClient(NBTTagCompound tag)
  {
  if(tag.hasKey("structInit"))
    {
    handleInitClient(tag.getCompoundTag("structInit"));
    }
  if(tag.hasKey("add"))
    {
    addClientStructureFromNBT(tag.getCompoundTag("add"));
    }
  if(tag.hasKey("remove"))
    {
    removeClientStructure(tag.getString("remove"));
    }
  }

public void handleUpdateServer(NBTTagCompound tag)
  {
  //TODO
  //handle add structure, relay to clients
  //handle remove structure, relay to clients
  if(tag.hasKey("add"))
    {
    
    }
  if(tag.hasKey("remove"))
    {
    
    }
  }

public boolean isValidStructureClient(String name)
  {
  for(ProcessedStructure struct : this.structures)
    {
    if(struct.name.equals(name))
      {
      return true;
      }
    }
  return false;
  }

/**
 * INIT method, clears structure list before adding from nbt list * 
 * @param list
 */
private void addClientStructuresFromNBT(NBTTagList list)
  {
  this.clientStructures.clear();
  NBTTagCompound tag;
  for(int i = 0; i < list.tagCount(); i++)
    {
    tag = (NBTTagCompound) list.tagAt(i);
    this.addClientStructureFromNBT(tag);    
    }
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

}
