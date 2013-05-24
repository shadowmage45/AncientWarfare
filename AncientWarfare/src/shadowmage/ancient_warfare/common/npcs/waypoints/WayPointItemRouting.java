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
package shadowmage.ancient_warfare.common.npcs.waypoints;

import java.util.ArrayList;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class WayPointItemRouting extends WayPoint
{

boolean deposit = false;
RoutingType routingType = RoutingType.NONE;
ItemStack[] filters = new ItemStack[8];

public WayPointItemRouting()
  {
  super(TargetType.DELIVER);
  }

public WayPointItemRouting(NBTTagCompound tag)
  {
  super(TargetType.DELIVER);
  this.readFromNBT(tag);
  }

public WayPointItemRouting(int x, int y, int z)
  {
  super(x, y, z, TargetType.DELIVER);
  }

public WayPointItemRouting(int x, int y, int z, int side)
  {
  super(x, y, z, side, TargetType.DELIVER);
  }

public int getFilterLength()
  {
  return this.filters.length;
  }

public boolean getDeliver()
  {
  return deposit;
  }

public void setDeliver(boolean val)
  {
  this.deposit = val;
  }

public RoutingType getRoutingType()
  {
  return this.routingType;
  }

public void setRoutingType(RoutingType t)
  {
  this.routingType = t;
  }

public void nextRoutingType()
  {
  int ordinal = this.routingType.ordinal();
  ordinal++;
  if(ordinal>=RoutingType.values().length)
    {
    ordinal = 1;
    if(ordinal>=RoutingType.values().length)
      {
      ordinal = 0;
      }
    }
  this.routingType = RoutingType.values()[ordinal];
  }

public ItemStack getFilterStack(int index)
  {
  if(index>=0 && index < filters.length)
    {
    return filters[index];
    }
  return null;
  }

public void setFilterStack(int index, ItemStack stack)
  {
  if(index>=0 && index<this.filters.length)    
    {
    this.filters[index] = stack;
    }
  }

public boolean doWork(NpcBase npc)
  {
  TileEntity te = getTileEntity(npc.worldObj);
  IInventory other = null;
  if(te instanceof IInventory)
    {
    other = (IInventory)te;
    }
  if(other==null)
    {
    return false;
    }
  IInventory target = deposit ? other : npc.inventory;
  IInventory source = deposit ? npc.inventory : other;
  boolean found = false;
  switch(routingType)
  {
  case ALL_BUT:
  found = doAllExcept(source, target);
  break;
  case ALL_BUT_EXACT:
  found = doAllExceptExact(source, target);
  break;
  case ANY_OF:
  found = doAllOf(source, target);
  break;
  case EXACT:
  found = doAllExact(source, target);
  break;
  case FILL_TO:
  found = doFillTo(source, target);
  break;
  }
  return found;
  }

/**
 * return the stack size of the filter, if any. -1 for not found
 * @param stack
 * @return
 */
protected int filterContains(ItemStack stack)
  {
  if(stack==null){return -1;}
  for(int i = 0; i < filters.length; i++)
    {
    ItemStack f = filters[i];
    if(f==null)continue;
    if(InventoryTools.doItemsMatch(f, stack))
      {
      return f.stackSize;
      }
    }
  return -1;
  }

protected boolean doAllOf(IInventory source, IInventory target)
  { 
  return doTransaction(source, target, false, false);
  }

protected boolean doAllExact(IInventory source, IInventory target)
  {  
  return doTransaction(source, target, true, false);
  }

protected boolean doFillTo(IInventory source, IInventory target)
  { 
  boolean shouldContinue = false;
  ItemStack stack;
  int[] sourceSlots = null;
  int[] targetSlots = null;
  if(source instanceof ISidedInventory)
    {
    sourceSlots = ((ISidedInventory)source).getSizeInventorySide(getSide());
    }
  if(target instanceof ISidedInventory)
    {
    targetSlots = ((ISidedInventory)target).getSizeInventorySide(getSide());
    }
  int sourceSize = sourceSlots !=null ? sourceSlots.length : source.getSizeInventory();
  int targetSize = targetSlots !=null ? targetSlots.length : target.getSizeInventory();
  int sourceIndex;
  int targetIndex;
  for(int i = 0; i < sourceSize; i++)
    {
    sourceIndex = sourceSlots !=null ? sourceSlots[i] : i;
    stack = source.getStackInSlot(sourceIndex);
    if(stack==null){continue;}
    int num = filterContains(stack);    
    if(num >=0)
      {
      int found = targetSlots != null ? InventoryTools.getCountOf(target, stack, targetSlots) : InventoryTools.getCountOf(target, stack, 0, target.getSizeInventory()-1);;
      int needed = num-found;
      if(needed>0)
        {
        if(stack.stackSize <= needed)
          {
          //merge entire stack
          shouldContinue = true;
          stack = InventoryTools.tryMergeStack(target, stack, getSide());
          if(stack!=null)
            {
            shouldContinue = false;
            }
          source.setInventorySlotContents(sourceIndex, stack);
          }
        else
          {
          shouldContinue = true;
          ItemStack split = stack.splitStack(needed);
          source.setInventorySlotContents(sourceIndex, stack);
          split = InventoryTools.tryMergeStack(target, split, getSide());
          if(split!=null)
            {
            InventoryTools.tryMergeStack(source, split, sourceSlots);
            shouldContinue = false;
            }
          if(stack.stackSize==0)
            {
            source.setInventorySlotContents(sourceIndex, null);
            }
          }
        }
      }
    }  
  return shouldContinue;
  }

protected boolean doAllExcept(IInventory source, IInventory target)
  {
  return doTransaction(source, target, false, true);
  }

protected boolean doTransaction(IInventory source, IInventory target, boolean exact, boolean except)
  {
  boolean shouldContinue = false;
  ItemStack stack;
  int[] sourceSlots = null;
  int[] targetSlots = null;
  if(source instanceof ISidedInventory)
    {
    sourceSlots = ((ISidedInventory)source).getSizeInventorySide(getSide());
    }
  if(target instanceof ISidedInventory)
    {
    targetSlots = ((ISidedInventory)target).getSizeInventorySide(getSide());
    }
  int sourceSize = sourceSlots !=null ? sourceSlots.length : source.getSizeInventory();
  int sourceIndex;
  int found;
  for(int i = 0; i < sourceSize; i++)
    {
    sourceIndex = sourceSlots !=null ? sourceSlots[i] : i;    
    stack = source.getStackInSlot(sourceIndex);
    if(stack==null){continue;}
    found = filterContains(stack);
    if( shouldTransact(found, stack.stackSize, exact, except) )
      {
      shouldContinue = true;
      stack = InventoryTools.tryMergeStack(target, stack, getSide());      
      if(stack!=null)
        {
        shouldContinue = false;        
        }
      source.setInventorySlotContents(i, stack);
      break;      
      }
    }  
  return shouldContinue;
  }

protected boolean shouldTransact(int found, int stackSize, boolean exact, boolean except)
  {
  if(except && !exact && found==-1)
    {
    return true;
    }
  else if(except && exact && found!=stackSize)
    {
    return true;
    }
  else if(!exact && !except)
    {
    return found>0;
    }
  else if(exact && !except)
    {
    return found==stackSize;
    }
  return false;
  }

protected boolean doAllExceptExact(IInventory source, IInventory target)
  {
  return doTransaction(source, target, true, true);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = super.getNBTTag();
  tag.setString("rt", this.routingType.name());
  tag.setBoolean("d", this.deposit);
  NBTTagList itemList = new NBTTagList();
  NBTTagCompound itemTag;
  ItemStack filter;
  for(int i = 0; i < this.filters.length; i++)
    {
    filter = this.filters[i];
    if(filter!=null)
      {
      itemTag = new NBTTagCompound();
      itemTag.setByte("s", (byte)i);
      filter.writeToNBT(itemTag);
      itemList.appendTag(itemTag);
      }
    }  
  tag.setTag("fl", itemList);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  if(tag.hasKey("rt"))
    {
    this.routingType = RoutingType.valueOf(tag.getString("rt"));
    }    
  this.deposit = tag.getBoolean("d");
  byte slot;
  NBTTagCompound itemTag;
  NBTTagList itemList = tag.getTagList("fl");
  for(int i = 0; i < itemList.tagCount(); i++)
    {
    itemTag = (NBTTagCompound) itemList.tagAt(i);
    slot = itemTag.getByte("s");
    if(slot>=0 && slot< this.filters.length)
      {    	  
      this.setFilterStack(slot, ItemStack.loadItemStackFromNBT(itemTag));
      }
    }    
    
  }

}
