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

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryTools
{

public static int getEmptySlots(IInventory inv)
  {
  int count = 0;
  for(int i = 0; i < inv.getSizeInventory(); i++)
    {
    if(inv.getStackInSlot(i)==null)
      {
      count++;
      }
    }
  return count;
  }

public static ItemStack putInFirstOpenSlot(IInventory inv, ItemStack in)
  {
  return putInFirstOpenSlotBetween(inv, in, 0, inv.getSizeInventory());
  }

/**
 * puts between start/end indexes (0-18 is slots 0-17)
 * @param in
 * @param start
 * @param stopBefore
 * @return
 */
public static ItemStack putInFirstOpenSlotBetween(IInventory inv, ItemStack in, int start, int stopBefore)
  {
  return mergeItemStack(inv, in, start, stopBefore);
  }

/**
 * merges provided ItemStack with the first avaliable one in the container/player inventory, returns extra, if any;
 */
private static ItemStack mergeItemStack(IInventory inv, ItemStack in, int startIndex, int stopIndex)
  {
  if(in == null)
    {
    return in;
    }
  int iteratorIndex = startIndex;
  if(startIndex<0)
    {
    startIndex = 0;
    } 
  if(stopIndex>inv.getSizeInventory())
    {
    stopIndex = inv.getSizeInventory();
    }
  ItemStack tempStack;  
  if (in.isStackable())
    {
    while (in.stackSize > 0 && iteratorIndex < stopIndex)
      {      
      tempStack = inv.getStackInSlot(iteratorIndex);
      if (tempStack != null && tempStack.itemID == in.itemID && (!in.getHasSubtypes() || in.getItemDamage() == tempStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(in, tempStack))
        {
        int tempTotal = tempStack.stackSize + in.stackSize;
        if (tempTotal <= in.getMaxStackSize())
          {          
          tempStack.stackSize = tempTotal;
          inv.onInventoryChanged();
          return null;
          }
        else if (tempStack.stackSize < in.getMaxStackSize())
          {
          in.stackSize -= in.getMaxStackSize() - tempStack.stackSize;
          inv.onInventoryChanged();
          tempStack.stackSize = in.getMaxStackSize();
          }
        }
      ++iteratorIndex;
      }
    }
  if (in.stackSize > 0)
    {  
    iteratorIndex = startIndex;
    while (iteratorIndex < stopIndex)
      {      
      tempStack = inv.getStackInSlot(iteratorIndex);
      if (tempStack == null)
        {
        inv.setInventorySlotContents(iteratorIndex, in);
        inv.onInventoryChanged();
        return null;
        }     
      ++iteratorIndex;        
      }
    }
  return in;
  }

public static ItemStack getStackOf(IInventory inv, int id, int dmg, int minQty, int maxQty)
  {  
  ItemStack stack = null;
  for(int i = 0; i < inv.getSizeInventory(); i++)
    {
    stack = inv.getStackInSlot(i);
    if(stack!=null && stack.itemID==id && stack.getItemDamage()==dmg && stack.stackSize>=minQty)
      {
      ItemStack temp = stack.copy();
      if(stack.stackSize>maxQty)
        {
        temp.stackSize=maxQty;
        stack.stackSize-=temp.stackSize;
        inv.setInventorySlotContents(i, stack);
        if(stack.stackSize<=0)
          {
          inv.setInventorySlotContents(i, null);
          }
        }
      else
        {
        inv.setInventorySlotContents(i, null);
        }      
      return temp;
      }
    }
  return null;
  }

public static boolean hasStackOf(IInventory inv, int id, int dmg, int minQty)
  {
  ItemStack stack;
  for(int i = 0; i < inv.getSizeInventory(); i ++)
    {
    stack = inv.getStackInSlot(i);
    if(stack!=null && stack.itemID==id && stack.getItemDamage()==dmg && stack.stackSize>=minQty)
      {
      return true;
      }
    }
  return false;
  }


}
