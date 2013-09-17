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
package shadowmage.ancient_warfare.common.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.civics.TECivicWarehouse;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.inventory.AWInventoryMapped;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapper;
import shadowmage.ancient_warfare.common.utils.StackWrapperComparatorAlphaAZ;

public class ContainerCivicWarehouse extends ContainerBase
{

TECivicWarehouse te;
public List<ItemStackWrapper> warehouseItems = new ArrayList<ItemStackWrapper>();
protected StackWrapperComparatorAlphaAZ azSorter = new StackWrapperComparatorAlphaAZ();


protected AWInventoryMapped cacheInventory = new AWInventoryMapped(0);

public int filledSlotCount = 0;

public boolean receivedDatas = false;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerCivicWarehouse(EntityPlayer openingPlayer, TECivicWarehouse te)
  {
  super(openingPlayer, te);
  this.te = te;  
  if(!te.worldObj.isRemote)
    {
    this.warehouseItems = InventoryTools.getCompactedInventory(te.inventory, azSorter);    
    this.filledSlotCount = te.getSizeInventory() - te.inventory.getEmptySlotCount();
    this.cacheInventory.setInventorySize(te.getSizeInventory());    
    ItemStack stack = null;
    for(int i = 0; i < te.getSizeInventory(); i++)
      {
      stack = te.getStackInSlot(i);
      if(stack!=null)
        {
        this.cacheInventory.setInventorySlotContents(i, stack.copy());
        }      
      }    
    }
    
  int y;
  int x;
  int slotNum;  
  int xPos; 
  int yPos;
  for (x = 0; x < 9; ++x)//add player hotbar slots
    {
    slotNum = x;
    xPos = 8 + x * 18;
    yPos = 162 + 3*18;
    this.addSlotToContainer(new Slot(openingPlayer.inventory, x, xPos, yPos));
    }
  for (y = 0; y < 3; ++y)
    {
    for (x = 0; x < 9; ++x)
      {
      slotNum = y*9 + x + 9;// +9 is to increment past hotbar slots
      xPos = 8 + x * 18;
      yPos = 158 + y * 18;
      this.addSlotToContainer(new Slot(openingPlayer.inventory, slotNum, xPos, yPos));
      }
    }
  
  for(y = 0; y < 3; y++)
    {
    for(x = 0; x < 3; x++)
      {
      slotNum = y*3 + x;
      xPos = 8 + x * 18;
      yPos = 98 + y * 18;
      Slot slot = new Slot(te.inputSlots, slotNum, xPos, yPos);
      this.addSlotToContainer(slot);      
      }
    }
  
  for(y = 0; y < 3; y++)
    {
    for(x = 0; x < 3; x++)
      {
      slotNum = y*3 + x;
      xPos = 8 + x * 18 + 6*18;
      yPos = 98 + y * 18;       
      Slot slot = new Slot(te.withdrawSlots, slotNum, xPos, yPos);
      this.addSlotToContainer(slot);      
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
    int storageSlots = te.inputSlots.getSizeInventory();
    int storageSlots2 = te.withdrawSlots.getSizeInventory();
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
    else if(slotClickedIndex >=36+storageSlots && slotClickedIndex < 36+storageSlots+storageSlots2)//storage slots, merge to player inventory
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
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("req"))
    {
    this.handleRequest(tag);
    }
  if(tag.hasKey("init"))
    {
    this.receivedDatas = true;
    this.handleInitData(tag);
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("reqInit") && !player.worldObj.isRemote)
    {
    Config.logDebug("sending inventory to client from request");
    this.updateAndSendInventory(true);
    }
  }

protected void handleRequest(NBTTagCompound tag)
  {
  ItemStack filter = ItemStack.loadItemStackFromNBT(tag);
  for(int i = 0; i< this.warehouseItems.size(); i++)
    {
    ItemStack item = this.warehouseItems.get(i).getFilter();
    if(InventoryTools.doItemsMatch(filter, item))
      {
      if(te.withdrawSlots.canHoldItem(item, item.getMaxStackSize()))
        {
        ItemStack removed = te.inventory.getItems(item, 64);
        removed = te.withdrawSlots.tryMergeItem(removed);
        if(item!=null)
          {
          te.overFlow.add(item);
          }
        }
      break;
      }
    } 
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  this.warehouseItems = InventoryTools.getCompactInventoryFromTag(tag);
  this.filledSlotCount = tag.getInteger("filledSlotCount");  
  this.receivedDatas = true;
  if(this.gui!=null){this.gui.refreshGui();}
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  List<NBTTagCompound> tags = new ArrayList<NBTTagCompound>();
  NBTTagCompound tag = InventoryTools.getTagForCompactInventory(warehouseItems);
  tag.setInteger("filledSlotCount", this.filledSlotCount);
  tags.add(tag);
  return tags;
  }

@Override
public void onContainerClosed(EntityPlayer par1EntityPlayer)
  {
  te.removePlayer(par1EntityPlayer);
  super.onContainerClosed(par1EntityPlayer);
  }

@Override
public void detectAndSendChanges()
  {
  this.updateAndSendInventory(false); 
  super.detectAndSendChanges();
  }

protected void updateAndSendInventory(boolean defaultVal)
  {
  boolean sendPacket = defaultVal;
  if(te.getSizeInventory()!=cacheInventory.getSizeInventory())
    {
    sendPacket = true;
    }
  else
    {
    ItemStack cacheStack = null;
    ItemStack teStack = null;
    for(int i = 0; i < this.cacheInventory.getSizeInventory(); i++)
      {
      cacheStack = cacheInventory.getStackInSlot(i);
      teStack = te.inventory.getStackInSlot(i);
      if(!ItemStack.areItemStacksEqual(cacheStack, teStack))        
        {
        sendPacket = true;      
        if(teStack!=null)
          {
          cacheInventory.setInventorySlotContents(i, teStack.copy());
          }
        else
          {
          cacheInventory.setInventorySlotContents(i, null);
          }  
        }
      }
    }
  if(sendPacket)
    {
    this.filledSlotCount = te.getSizeInventory() - te.inventory.getEmptySlotCount();
    this.warehouseItems = InventoryTools.getCompactedInventory(te.inventory, azSorter);
    NBTTagCompound tag = InventoryTools.getTagForCompactInventory(warehouseItems);
    tag.setInteger("filledSlotCount", this.filledSlotCount);
    tag.setBoolean("init", true);
    this.sendDataToPlayer(tag);
    }  
  }

@Override
public boolean canInteractWith(EntityPlayer var1)
  {
  return super.canInteractWith(var1) && var1!=null && var1.getDistance(te.xCoord+0.5d, te.yCoord, te.zCoord+0.5d) < 5.d;
  }

}
