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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.gates.types.Gate;
import shadowmage.ancient_warfare.common.interfaces.IScannerItem;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class ItemGateSpawner extends AWItemClickable implements IScannerItem
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemGateSpawner(int itemID)
  {
  super(itemID, true);
  this.hasLeftClick = true;
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  return false;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  return false;
  }

@Override
public BlockPosition getScanPos1(ItemStack stack)
  {
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWGateInfo") && stack.getTagCompound().getCompoundTag("AWGateInfo").hasKey("pos1"))
    {
    return new BlockPosition(stack.getTagCompound().getCompoundTag("AWGateInfo").getCompoundTag("pos1"));
    }
  return null;
  }

@Override
public BlockPosition getScanPos2(ItemStack stack)
  {
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWGateInfo") && stack.getTagCompound().getCompoundTag("AWGateInfo").hasKey("pos2"))
    {
    return new BlockPosition(stack.getTagCompound().getCompoundTag("AWGateInfo").getCompoundTag("pos2"));
    }
  return null;
  }

}
