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
package shadowmage.ancient_warfare.common.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.interfaces.IInventoryCallback;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class AWInventoryBasic implements IInventory
{

/**
 * a list of concerned entities (viewers/synch objects) for this inventory.
 * when inventory is changed, they will all receive notice, being passed the inventory
 * 
 */
List<IInventoryCallback> callBacks = new ArrayList<IInventoryCallback>();
ItemStack[] inventorySlots;
ItemStack[] prevContents;

public AWInventoryBasic(int size)
  {
  this.inventorySlots = new ItemStack[size];
  this.prevContents = new ItemStack[size];
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

/**
 * return qty left that could not be removed from inventory
 * does not call onInventoryChanged...only used by vehicle ammo helper
 * to decrease ammo counts (only known by itemID/damage) should deprecate
 * and create itemStack filters for ammo (might be available from registry registered stack cache)
 * @param id
 * @param dmg
 * @param qty
 * @return
 */
public int decreaseCountOf(int id, int dmg, int qty)
  {  
  for(int i = 0; i < this.getSizeInventory(); i++)
    {
    ItemStack stack = inventorySlots[i];
    if(stack!=null && stack.itemID==id && stack.getItemDamage()==dmg)
      {
      if(stack.stackSize>=qty)
        {
        stack.stackSize-=qty;
        qty = 0;
        }
      else
        {
        qty-=stack.stackSize;
        stack.stackSize = 0;
        }
      if(stack.stackSize==0)
        {
        this.inventorySlots[i]=null;        
        }
      if(qty<=0)
        {
        return 0;
        }
      }
    } 
  return qty;
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
      return tempStack;
      }
    else
      {
      tempStack = this.inventorySlots[slotNum].splitStack(decreaseBy);
      if (this.inventorySlots[slotNum].stackSize == 0)
        {
        this.inventorySlots[slotNum] = null;
        }
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
  }

public int tryRemoveItems(ItemStack filter, int qty)
  {
  return InventoryTools.tryRemoveItems(this, filter, qty, 0, this.getSizeInventory()-1);
  }

public ItemStack getItems(ItemStack filter, int max)
  {
  return InventoryTools.getItems(this, filter, max, 0, this.getSizeInventory()-1);
  }

public boolean canHoldItem(ItemStack filter, int qty)
  {
  return InventoryTools.canHoldItem(this, filter, qty, 0, this.getSizeInventory()-1);
  }

/**
 * returns the remainder of the items not merged, or null if completely successful 
 * @param toMerge
 * @return
 */
public ItemStack tryMergeItem(ItemStack toMerge)
  {
  return InventoryTools.tryMergeStack(this, toMerge, 0, this.getSizeInventory()-1);
  }

public boolean containsAtLeast(ItemStack filter, int qty)
  {
  return InventoryTools.containsAtLeast(this, filter, qty, 0, this.getSizeInventory()-1);
  }

public int getEmptySlotCount()
  {
  int emptySlots = 0;
  for(int i = 0; i < this.getSizeInventory(); i ++)
    {
    if(this.getStackInSlot(i)==null)
      {
      emptySlots++;
      }
    }
  return emptySlots;
  }

public int canHoldMore(ItemStack item)
  {
  return InventoryTools.canHoldMore(this, item, 0, this.getSizeInventory()-1);
  }

/**
 * percentage full by slot count
 * @return
 */
public float getPercentEmpty()
  {
  if(this.getSizeInventory()==0)
    {
    return 1.f;
    }
  return (float)((float)this.getEmptySlotCount()/(float)this.getSizeInventory());
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
  if(!this.callBacks.isEmpty())
    {
    boolean changed = false;  
    ArrayList<Integer> changedSlots = null;
    for(int i = 0; i < this.prevContents.length; i++)
      {
      if(!ItemStack.areItemStacksEqual(prevContents[i], inventorySlots[i]))
        {      
        if(changedSlots==null)
          {
          changedSlots = new ArrayList<Integer>();
          }
        changedSlots.add(i);
        changed = true;
        if(inventorySlots[i]!=null)
          {
          prevContents[i] = inventorySlots[i].copy();
          }
        else
          {
          prevContents[i] = null;
          }
        }
      }
    if(changed)
      {
      for(IInventoryCallback cb : this.callBacks)
        {
        if(cb!=null)
          {
          cb.onInventoryChanged(this, changedSlots);
          }
        }
      }
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
