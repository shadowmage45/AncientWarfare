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
package shadowmage.ancient_warfare.common.container;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.item.ItemBackpack;
import shadowmage.ancient_warfare.common.item.ItemLoader;

public class ContainerBackpack extends ContainerBase
{

AWInventoryBasic inventory = null;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerBackpack(EntityPlayer openingPlayer)
  {
  super(openingPlayer, null);int y;
  int x;
  int slotNum;
  int xPos; 
  int yPos;
  ItemStack filter = new ItemStack(ItemLoader.backpack);
  for (x = 0; x < 9; ++x)//add player hotbar slots
    {
    slotNum = x;
    xPos = 8 + x * 18;
    yPos = 162 + 3*18; 
    if(x==player.inventory.currentItem)
      {
      this.addSlotToContainer(new SlotNoPull(openingPlayer.inventory, x, xPos, yPos));
      }
    else
      {
      this.addSlotToContainer(new SlotExcludeOnly(openingPlayer.inventory, x, xPos, yPos, filter));      
      }
    }
  for (y = 0; y < 3; ++y)
    {
    for (x = 0; x < 9; ++x)
      {
      slotNum = y*9 + x + 9;// +9 is to increment past hotbar slots
      xPos = 8 + x * 18;
      yPos = 158 + y * 18;
      this.addSlotToContainer(new SlotExcludeOnly(openingPlayer.inventory, slotNum, xPos, yPos, filter));
      }
    }  

  AWInventoryBasic te = ItemBackpack.getInventoryFor(player.inventory.getCurrentItem());
  inventory = te;
  for(y = 0; y < te.getSizeInventory()/9; y++)
    {
    for(x = 0; x < 9; x++)
      {
      slotNum = y*9 + x;
      if(slotNum<te.getSizeInventory())
        {
        xPos = 8 + x * 18;
        yPos = y * 18 + 15;       
        Slot slot = new Slot(te, slotNum, xPos, yPos);
        this.addSlotToContainer(slot);        
        }
      }
    } 
  }

@Override
public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotClickedIndex)
  {
  ItemStack slotStackCopy = null;
  Slot theSlot = (Slot)this.inventorySlots.get(slotClickedIndex);
  if (theSlot != null && theSlot.getHasStack())
    {
    ItemStack slotStack = theSlot.getStack();
    slotStackCopy = slotStack.copy();
    int invIndex = theSlot.getSlotIndex();    
    int storageSlots = inventory.getSizeInventory();    
    if (slotClickedIndex < 36)//player slots...
      {      
      if (!this.mergeItemStack(slotStack, 36, 36+storageSlots, false))//merge into storage inventory
        {
        return null;
        }
      }
    else if(slotClickedIndex >=36 &&slotClickedIndex < 36+storageSlots)//storage slots, merge to player inventory
      {
      if (!this.mergeItemStack(slotStack, 0, 36, true))//merge into player inventory
        {
        return null;
        }
      }       
    if (slotStack.stackSize == 0)
      {
      theSlot.putStack((ItemStack)null);
      }
    else
      {
      theSlot.onSlotChanged();
      }
    if (slotStack.stackSize == slotStackCopy.stackSize)
      {
      return null;
      }
    theSlot.onPickupFromSlot(par1EntityPlayer, slotStack);
    }
  return slotStackCopy;
  }



@Override
public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
  {
  super.onCraftGuiClosed(par1EntityPlayer);  
  if(!par1EntityPlayer.worldObj.isRemote)
    {
    ItemStack stack = par1EntityPlayer.inventory.getCurrentItem();
    ItemBackpack.writeInventoryToItem(stack, inventory);
    }
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub    
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub    
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

}
