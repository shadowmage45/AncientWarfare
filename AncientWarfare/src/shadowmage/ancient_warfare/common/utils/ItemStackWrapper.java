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

public class ItemStackWrapper
{

ItemStack filter;
private int quantity;

public ItemStackWrapper(ItemStack stack, int qty)
  {
  this.filter = stack;
  this.setQuantity(qty);
  }

public ItemStackWrapper(ItemStack stack)
  {
  this.filter = stack;
  this.setQuantity(stack.stackSize);
  }

public ItemStackWrapper(NBTTagCompound tag)
  {
  NBTTagCompound itemTag = null;
  int id = tag.getInteger("id");
  int meta = tag.getInteger("dmg");
  if(tag.hasKey("tag"))
    {
    itemTag = tag.getCompoundTag("tag");
    } 
  ItemStack stack = new ItemStack(id,1,meta);
  if(tag!=null)
    {
    stack.setTagCompound(itemTag);
    }
  filter = stack;
  setQuantity(tag.getInteger("count"));
  }

public boolean matches(ItemStack stack)
  {
  return InventoryTools.doItemsMatch(this.filter, stack);
  }

public ItemStack getFilter()
  {
  filter.stackSize = this.getQuantity();
  return filter;
  }

public NBTTagCompound writeToNBT(NBTTagCompound tag)
  {
  tag.setInteger("id", filter.itemID);
  tag.setInteger("dmg", filter.getItemDamage());
  tag.setInteger("count", getQuantity());
  if(filter.hasTagCompound())
    {
    tag.setTag("tag", filter.getTagCompound());
    }
  return tag;
  }

/**
 * @return the quantity
 */
public int getQuantity()
  {
    return quantity;
  }

/**
 * @param quantity the quantity to set
 */
public void setQuantity(int quantity)
  {
    this.quantity = quantity;
  }

}
