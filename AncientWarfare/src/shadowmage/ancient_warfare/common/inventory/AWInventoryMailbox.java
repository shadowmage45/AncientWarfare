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
package shadowmage.ancient_warfare.common.inventory;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_framework.common.inventory.AWInventoryBase;
import shadowmage.ancient_warfare.common.tracker.entry.BoxData;

public class AWInventoryMailbox extends AWInventoryBase
{

protected BoxData data;
ItemStack[] stacks;
/**
 * @param size
 */
public AWInventoryMailbox(int size, BoxData data)
  {
  super(size);
  this.data = data;
  this.stacks = new ItemStack[size];
  }

public void setBoxData(BoxData data)
  {
  this.data = data;
  }

@Override
public ItemStack getStackInSlot(int i)
  {
  return data==null? stacks[i] : data.getStackInSlot(i);
  }

@Override
public void setInventorySlotContents(int i, ItemStack itemstack)
  {
  if(data!=null)
    {
    data.setInventorySlotContents(i, itemstack);
    }
  else
    {
    stacks[i] = itemstack;
    }
  }

}
