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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class ItemDebugSpawner extends AWItemClickable
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemDebugSpawner(int itemID)
  {
  super(itemID, false);
  this.setIconIndex(5);
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack,  BlockPosition hit, int side)
  {
  if(hit==null || world.isRemote || stack == null)
    {
    return false;
    }
  hit = BlockTools.offsetForSide(hit, side);
    
  spawnVehicle(world, player, hit);
    
  if(!player.capabilities.isCreativeMode)
    {
    stack.stackSize--;
    if(stack.stackSize<=0)
      {
      player.inventory.setInventorySlotContents(player.inventory.currentItem, null);    
      }
    }
  return true;
  }

public void spawnVehicle(World world, EntityPlayer player, BlockPosition hit)
  {
  
  }

}
