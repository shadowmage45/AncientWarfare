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
package shadowmage.ancient_warfare.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemBuilderBase;
import shadowmage.ancient_warfare.common.item.ItemLoader;

public class ContainerCSB extends ContainerBase
{

//public final ItemStack builderItem;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerCSB(EntityPlayer openingPlayer, IEntityContainerSynch synch) 
  {
  super(openingPlayer, synch);
  if(player.inventory.getCurrentItem() == null && !(player.inventory.getCurrentItem().getItem() instanceof ItemBuilderBase))
    {
    Config.logError("Severe error initializing Creative Structure Builder Container, improper ItemStack detected.");
    }
  }


@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(player.worldObj.isRemote)
    {
    Config.logError("Server packet recieved on client side!");
    return;
    }
  ItemStack builderItem = player.inventory.getCurrentItem();
  if(builderItem==null || builderItem.getItem()==null)
    {
    return;
    }
  int id = builderItem.itemID;
  if(id != ItemLoader.instance().structureCreativeBuilder.itemID && id != ItemLoader.instance().structureCreativeBuilderTicked.itemID)
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
  if(tag.hasKey("name"))
    {
    stackTag.setString("name", tag.getString("name"));
    }  
  if(tag.hasKey("team"))
    {
    stackTag.setInteger("team", tag.getInteger("team"));
    }
  if(tag.hasKey("veh"))
    {
    stackTag.setInteger("veh", tag.getInteger("veh"));
    }
  if(tag.hasKey("npc"))
    {
    stackTag.setInteger("npc", tag.getInteger("npc"));
    }
  if(tag.hasKey("gate"))
    {
    stackTag.setInteger("gate", tag.getInteger("gate"));
    }  
  builderItem.setTagInfo("structData", stackTag);
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  
  }


@Override
public NBTTagCompound getInitData()
  {
  // TODO Auto-generated method stub
  return null;
  }

}
