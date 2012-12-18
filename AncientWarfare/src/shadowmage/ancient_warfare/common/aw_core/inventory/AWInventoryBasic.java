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
package shadowmage.ancient_warfare.common.aw_core.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.aw_core.utils.IInventoryCallback;

public class AWInventoryBasic implements IInventory
{

List<IInventoryCallback> callBacks = new ArrayList<IInventoryCallback>();
ItemStack[] inventorySlots;
public AWInventoryBasic(int size)
  {
  this.inventorySlots = new ItemStack[size];
  }

public AWInventoryBasic(int size, IInventoryCallback caller)
  {
  this(size);
  this.addCallback(caller);
  }

public void addCallback(IInventoryCallback ent)
  {
  if(!this.callBacks.contains(ent))
    {
    this.callBacks.add(ent);
    }
  }

@Override
public int getSizeInventory()
  {
  return this.inventorySlots.length;
  }

@Override
public ItemStack getStackInSlot(int var1)
  {
  if(var1 < this.inventorySlots.length && var1>=0)
    {
    return this.inventorySlots[var1];
    }
  return null;
  }

@Override
public ItemStack decrStackSize(int slotNum, int decreaseBy)
  {
  if (this.inventorySlots[slotNum] != null)
    {
    ItemStack tempStack;
    if (this.inventorySlots[slotNum].stackSize <= decreaseBy)
      {
      tempStack = this.inventorySlots[slotNum];
      this.inventorySlots[slotNum] = null;
      this.onInventoryChanged();
      return tempStack;
      }
    else
      {
      tempStack = this.inventorySlots[slotNum].splitStack(decreaseBy);
      if (this.inventorySlots[slotNum].stackSize == 0)
        {
        this.inventorySlots[slotNum] = null;
        }
      this.onInventoryChanged();
      return tempStack;
      }
    }
  else
    {
    return null;
    }
  }

@Override
public ItemStack getStackInSlotOnClosing(int var1)
  {
  if (this.inventorySlots[var1] != null)
    {
    ItemStack var2 = this.inventorySlots[var1];
    this.inventorySlots[var1] = null;
    return var2;
    }
  else
    {
    return null;
    }
  }

@Override
public void setInventorySlotContents(int stackIndex, ItemStack newContents)
  {
  this.inventorySlots[stackIndex] = newContents;
  if (newContents != null && newContents.stackSize > this.getInventoryStackLimit())
    {
    newContents.stackSize = this.getInventoryStackLimit();
    }
  this.onInventoryChanged();
  }

@Override
public String getInvName()
  {  
  return "DefaultInventory";
  }

@Override
public int getInventoryStackLimit()
  {  
  return 64;
  }

@Override
public void onInventoryChanged()
  {
  for(IInventoryCallback cb : this.callBacks)
    {
    if(cb!=null){cb.onInventoryChanged(this);}
    }
  }

@Override
public boolean isUseableByPlayer(EntityPlayer var1)
  {
  return true;
  }

@Override
public void openChest()
  {
  
  }

@Override
public void closeChest()
  {
  
  }

/**
 * return {@link NBTTagCompound} describing this inventory
 * @return
 */
public NBTTagCompound getNBTTag()
  {  
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList itemList = new NBTTagList();
  for (int slotIndex = 0; slotIndex < this.getSizeInventory(); ++slotIndex)
    {
    if (this.getStackInSlot(slotIndex) != null)
      {
      NBTTagCompound itemEntryTag = new NBTTagCompound();
      itemEntryTag.setByte("Slot", (byte)slotIndex);
      this.getStackInSlot(slotIndex).writeToNBT(itemEntryTag);
      itemList.appendTag(itemEntryTag);
      }
    }
  tag.setTag("Items", itemList);
  return tag;
  }

/**
 * read the inventory from an NBT tag
 * @param tag
 */
public void readFromNBT(NBTTagCompound tag)
  {
  NBTTagList itemList = tag.getTagList("Items");  
  for (int tagIndex = 0; tagIndex < itemList.tagCount(); ++tagIndex)
    {
    NBTTagCompound itemStackTag = (NBTTagCompound)itemList.tagAt(tagIndex);
    int slotForItem = itemStackTag.getByte("Slot") & 255;
    if (slotForItem >= 0 && slotForItem < this.getSizeInventory())
      {
      this.setInventorySlotContents(slotForItem, ItemStack.loadItemStackFromNBT(itemStackTag));
      }
    }
  }

}
