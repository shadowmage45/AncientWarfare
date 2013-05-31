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
package shadowmage.ancient_warfare.common.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackWrapperCrafting extends ItemStackWrapper
{

int remainingNeeded = 0;

public ItemStackWrapperCrafting(ItemStackWrapper input)
  {
  super(input.getFilter(), input.getQuantity());
  this.remainingNeeded = getQuantity();
  }

public ItemStackWrapperCrafting(ItemStack stack, int qty)
  {
  super(stack, qty);
  this.remainingNeeded = qty;
  }

public ItemStackWrapperCrafting(ItemStack stack)
  {
  super(stack);
  this.remainingNeeded = stack.stackSize;
  }

public ItemStackWrapperCrafting(NBTTagCompound tag)
  {
  super(tag);
  this.remainingNeeded = tag.getInteger("rem");
  }

public NBTTagCompound writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setInteger("rem", remainingNeeded);
  return tag;
  }
}
