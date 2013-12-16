/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
package shadowmage.ancient_structures.common.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_structures.common.item.ItemBuilderBase;
import shadowmage.ancient_structures.common.structures.data.StructureBuildSettings;

public class ContainerCSB extends ContainerBase
{

public StructureBuildSettings clientSettings;
public StructureBuildSettings serverSettings;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerCSB(EntityPlayer openingPlayer) 
  {
  super(openingPlayer, null);

  if(player.worldObj.isRemote)
    {
    return;
    }
  ItemStack builderItem = player.inventory.getCurrentItem();
  if(builderItem==null || builderItem.getItem()==null)
    {
    return;
    } 
  NBTTagCompound stackTag;
  if(builderItem.hasTagCompound() && builderItem.getTagCompound().hasKey("structData"))
    {
    stackTag = builderItem.getTagCompound().getCompoundTag("structData");
    }
  else
    {
    stackTag = new NBTTagCompound();
    }  
  this.serverSettings = StructureBuildSettings.constructFromNBT(stackTag); 
  }

/**
 * relay the client-side info back to parent
 */
public void updateServerContainer()
  {
  if(clientSettings!=null)
    {
    NBTTagCompound baseTag = new NBTTagCompound();
    NBTTagCompound tag = clientSettings.getNBTTag();
    baseTag.setCompoundTag("structData", tag);
    this.sendDataToServer(baseTag);
    }
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(player.worldObj.isRemote)
    {
    AWLog.logError("Server packet recieved on client side!");
    return;
    }  
  if(tag.hasKey("structData") && this.serverSettings!=null)
    {
    this.serverSettings.readFromNBT(tag.getCompoundTag("structData"));
    }
  if(tag.hasKey("name"))
    {
    this.serverSettings.name = tag.getString("name");
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  if(tag.hasKey("structData"))
    {
    this.clientSettings = StructureBuildSettings.constructFromNBT(tag.getCompoundTag("structData"));
    }
  }

@Override
public List<NBTTagCompound> getInitData()
  {  
  if(this.serverSettings!=null)
    {
    List<NBTTagCompound> initList = new ArrayList<NBTTagCompound>();  
    NBTTagCompound baseTag = new NBTTagCompound();
    NBTTagCompound tag = serverSettings.getNBTTag();    
    baseTag.setCompoundTag("structData", tag);
    initList.add(baseTag);
    return initList;    
    }
  return null;
  }

@Override
public void onContainerClosed(EntityPlayer par1EntityPlayer)
  {
  super.onContainerClosed(par1EntityPlayer);
  if(par1EntityPlayer.worldObj.isRemote)
    {
    return;
    }
  ItemStack builderItem = player.inventory.getCurrentItem();  
  if(builderItem==null || builderItem.getItem()==null)
    {
    return;
    }
  /**
   * TODO fix validation of item prior to setting nbt data
   */
  builderItem.setTagInfo("structData", serverSettings.getNBTTag());  
  }

}
