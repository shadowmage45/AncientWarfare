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
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;

public class StackWrapper implements INBTTaggable
{

public ItemStack stack;

private StackWrapper()
  {
  
  }

public StackWrapper(ItemStack stack)
  {
  if(stack==null)
    {
    throw new NullPointerException();
    }
  this.stack = stack.copy();
  }

public boolean equals(ItemStack stack)
  {
  return InventoryTools.doItemsMatch(this.stack, stack);
  }

public static StackWrapper loadFromNBT(NBTTagCompound tag)
  {
  StackWrapper wrap = new StackWrapper();
  wrap.readFromNBT(tag);
  return wrap;
  }

@Override
public NBTTagCompound getNBTTag()
  {
  return stack.writeToNBT(new NBTTagCompound());
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.stack = ItemStack.loadItemStackFromNBT(tag);
  }

}
