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
package shadowmage.ancient_warfare.common.aw_structure.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.client.aw_structure.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.aw_core.container.ContainerBase;
import shadowmage.ancient_warfare.common.aw_core.utils.IContainerGUICallback;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;

public class ContainerChooseStructure extends ContainerBase
{

String currentSelection = "";
List<StructureClientInfo> structureInfo = new ArrayList<StructureClientInfo>();

/**
 * @param player
 * @param gui
 * @param crafters
 */
public ContainerChooseStructure(EntityPlayer player, IContainerGUICallback gui)
  {
  super(player, gui, null);
  }

@Override
public void handleUpdateClient(NBTTagCompound tag)
  {
  if(tag.hasKey("structData"))
    {
    this.readStructureData(tag.getTagList("structData"));
    }
  }

private void readStructureData(NBTTagList list)
  {
  NBTTagCompound tag;
  StructureClientInfo info;
  for(int i = 0; i < list.tagCount(); i++)
    {
    tag = (NBTTagCompound) list.tagAt(i);
    this.structureInfo.add(new StructureClientInfo(tag));    
    }
  }

/**
 * handle client selection of structure
 * @param name
 */
private void handleSelection(String name)
  {
  if(this.player.worldObj.isRemote)
    {
    this.currentSelection = name;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("selection", name);
    this.sendDataToServer(tag);
    }
  else
    {
    this.currentSelection = name;
    //TODO add itemStack updating code, add structureName into stack NBTTag.
    }
  }

@Override
public void handleUpdateServer(NBTTagCompound tag)
  {
  if(tag.hasKey("selection"))
    {
    this.handleSelection(tag.getString("selection"));
    }
  }

@Override
public NBTTagCompound getInitData()
  {
  
  return null;
  }


}
