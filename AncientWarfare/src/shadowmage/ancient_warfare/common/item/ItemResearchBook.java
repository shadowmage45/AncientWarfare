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
package shadowmage.ancient_warfare.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class ItemResearchBook extends AWItemClickable
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemResearchBook(int itemID)
  {
  super(itemID, false);
  this.hasLeftClick = false;
  this.maxStackSize = 1;
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  super.addInformation(stack, player, list, par4); NBTTagCompound tag = null;
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWResInfo"))
    {
    tag = stack.getTagCompound().getCompoundTag("AWResInfo");
    list.add("Belongs to: "+tag.getString("name"));
    if(player.getEntityName().equals(tag.getString("name")))
      {
      list.add("Right click to view your research notes.");
      }
    }
  else
    {
    list.add("Unclaimed Research Book");
    list.add("Right click to claim");
    }  
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote)
    {
    return true;
    }
  NBTTagCompound tag = null;
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWResInfo"))
    {
    tag = stack.getTagCompound().getCompoundTag("AWResInfo");
    if(tag.hasKey("name"))
      {
      String name = tag.getString("name");
      if(name.equals(player.getEntityName()))
        {
        GUIHandler.instance().openGUI(GUIHandler.INFO, player, world, 0, 0, 0);        
        }
      else        
        {
        player.addChatMessage("This is not your research book!");
        }
      }
    }
  else
    {
    tag = new NBTTagCompound();
    tag.setString("name", player.getEntityName());
    player.addChatMessage("Binding Research Book to You!");    
    }  
  stack.setTagInfo("AWResInfo", tag);
  return true;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  return false;
  }

public PlayerEntry getEntryForOwner(World world, ItemStack stack)
  {
  if(world.isRemote){return null;}
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWResInfo") && stack.getTagCompound().getCompoundTag("AWResInfo").hasKey("name"))
    {
    String name = stack.getTagCompound().getCompoundTag("AWResInfo").getString("name");
    return PlayerTracker.instance().getEntryFor(name);
    }
  return null;
  }
}
