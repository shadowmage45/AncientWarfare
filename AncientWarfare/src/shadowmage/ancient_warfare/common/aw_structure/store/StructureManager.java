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

import shadowmage.ancient_warfare.client.aw_structure.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_structure.build.Builder;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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

public List<ProcessedStructure> getStructureList()
  {
  List<ProcessedStructure> list = new ArrayList<ProcessedStructure>();
  list.addAll(this.structures);  
  return list;
  }

public NBTTagCompound getClientInitData()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList list = new NBTTagList();
  Iterator<ProcessedStructure> it = structures.iterator();
  ProcessedStructure structure;
  NBTTagCompound structTag;
  while(it.hasNext())
    {    
    structure = it.next();
    structTag = new NBTTagCompound();
    structTag.setString("name", String.valueOf(structure.name));
    structTag.setShort("x", (short)structure.xSize);
    structTag.setShort("y", (short)structure.ySize);
    structTag.setShort("z", (short)structure.zSize);
    list.appendTag(structTag);    
    }  
  tag.setTag("structData", list);
  return tag;
  }

private void handleInitClient(NBTTagCompound tag)
  {
  if(tag.hasKey("structData"))
    {
    this.addClientStructuresFromNBT(tag.getTagList("structData"));
    }
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

private void addClientStructuresFromNBT(NBTTagList list)
  {
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

}
