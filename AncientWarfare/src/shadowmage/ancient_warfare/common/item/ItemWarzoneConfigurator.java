/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.warzone.WarzoneManager;

public class ItemWarzoneConfigurator extends AWItemClickable
{

public ItemWarzoneConfigurator(int itemID)
  {
  super(itemID, false);
  }

private WarzoneItemData serverData = new WarzoneItemData();

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world==null || world.isRemote || stack==null || stack.getItem()!=this){return true;}
  if(!AWCore.instance.proxy.isPlayerOp(player))
    {
    player.addChatMessage("You must have Op status to use this item.");
    return true;
    }
  serverData.loadFromItem(stack);
  if(player.isSneaking())
    {
    serverData.clearData();
    serverData.saveToItem(stack);
    return true;
    }
  if(serverData.hasPos1 && serverData.hasPos2)
    {
    player.addChatMessage("Configured a new warzone between: "+serverData.getPos1() + " and "+serverData.getPos2());
    WarzoneManager.instance().addWarzone(world, serverData.getPos1(), serverData.getPos2());
    //create warzone, etc
    serverData.clearData();
    }
  else if(serverData.hasPos1)
    {
    //set pos 2
    serverData.setPos2(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
    }
  else
    {
    //set pos1
    serverData.setPos1(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
    }
  serverData.saveToItem(stack);
  return true;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  return false;
  }

private class WarzoneItemData
{
boolean hasPos1 = false;
boolean hasPos2 = false;
BlockPosition pos1;
BlockPosition pos2;

public WarzoneItemData()
  {
  pos1 = new BlockPosition();
  pos2 = new BlockPosition();
  }

public void loadFromItem(ItemStack item)
  {
  hasPos1 = false;
  hasPos2 = false;
  if(item.hasTagCompound() && item.getTagCompound().hasKey("WZConfig"))
    {
    NBTTagCompound data = item.getTagCompound().getCompoundTag("WZConfig");
    if(data.hasKey("pos1"))
      {
      hasPos1 = true;
      pos1.readFromNBT(data.getCompoundTag("pos1"));
      }
    if(data.hasKey("pos2"))
      {
      hasPos2 = true;
      pos2.readFromNBT(data.getCompoundTag("pos2"));
      }
    }
  }

public void clearItem(ItemStack item)
  {
  this.loadFromItem(item);
  this.clearData();
  this.saveToItem(item);
  }

public void clearData()
  {
  this.hasPos1 = false;
  this.hasPos2 = false;
  }

public void saveToItem(ItemStack item)
  {
  NBTTagCompound data = new NBTTagCompound();
  NBTTagCompound posTag;
  if(hasPos1)
    {
    posTag = new NBTTagCompound();
    pos1.writeToNBT(posTag);
    data.setTag("pos1", posTag);
    }  
  if(hasPos2)
    {
    posTag = new NBTTagCompound();
    pos2.writeToNBT(posTag);
    data.setTag("pos2", posTag);
    }
  item.setTagInfo("WZConfig", data);
  }

public void setPos1(int x, int y, int z)
  {
  pos1.x = x;
  pos1.y = y;
  pos1.z = z;
  hasPos1 = true;
  }

public void setPos2(int x, int y, int z)
  {
  pos2.x = x;
  pos2.y = y;
  pos2.z = z;
  hasPos2 = true;
  }

public boolean hasPos1(){return hasPos1;}
public boolean hasPos2(){return hasPos2;}
public BlockPosition getPos1(){return pos1;}
public BlockPosition getPos2(){return pos2;}

}

}
