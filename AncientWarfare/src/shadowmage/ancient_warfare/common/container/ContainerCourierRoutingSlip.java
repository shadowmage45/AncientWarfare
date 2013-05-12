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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.npcs.waypoints.CourierRoutingInfo;

public class ContainerCourierRoutingSlip extends ContainerBase
{

public CourierRoutingInfo info;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerCourierRoutingSlip(EntityPlayer openingPlayer, CourierRoutingInfo info)
  {
  super(openingPlayer, null);
  this.info = info;  
  int y;
  int x;
  int slotNum;
  int xPos; 
  int yPos;
  for (x = 0; x < 9; ++x)//add player hotbar slots
    {
    slotNum = x;
    xPos = 8 + x * 18;
    yPos = 142+28;
    if(x==player.inventory.currentItem)
      {
      this.addSlotToContainer(new SlotNoPull(openingPlayer.inventory, x, xPos, yPos));
      }
    else
      {
      this.addSlotToContainer(new Slot(openingPlayer.inventory, x, xPos, yPos));
      }
    }
  for (y = 0; y < 3; ++y)
    {
    for (x = 0; x < 9; ++x)
      {
      slotNum = y*9 + x + 9;// +9 is to increment past hotbar slots
      xPos = 8 + x * 18;
      yPos = 84 + y * 18+28;
      this.addSlotToContainer(new Slot(openingPlayer.inventory, slotNum, xPos, yPos));
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
//    int storageSlots = npc.npcType.getInventorySize(npc.rank);    
//    if (slotClickedIndex < 36)//player slots...
//      {      
//      if (!this.mergeItemStack(slotStack, 36, 36+storageSlots, false))//merge into storage inventory
//        {
//        return null;
//        }
//      }
//    else if(slotClickedIndex >=36 &&slotClickedIndex < 36+storageSlots)//vehicle slots, merge to player inventory
//      {
//      if (!this.mergeItemStack(slotStack, 0, 36, true))//merge into player inventory
//        {
//        return null;
//        }
//      }
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
  if(tag.hasKey("slip"))
    {
    info = new CourierRoutingInfo(tag.getCompoundTag("slip"));
    }
  if(tag.hasKey("filter"))
    {
    byte filter = tag.getByte("f");
    byte slot = tag.getByte("s");
    ItemStack filterStack = null;
    if(tag.hasKey("fs"))
      {
      filterStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("fs"));
      }  
    info.getPoint(filter).setFilterStack(slot, filterStack);
    }
  if(tag.hasKey("set"))
    {
    byte filter = tag.getByte("f");
    if(tag.hasKey("d"))
      {
      info.getPoint(filter).setDeliver(tag.getBoolean("d"));
      }
    if(tag.hasKey("p"))
      {
      info.getPoint(filter).setPartial(tag.getBoolean("p"));
      }
    if(tag.hasKey("i"))
      {
      info.getPoint(filter).setInclude(tag.getBoolean("i"));
      }
    }
  if(tag.hasKey("move"))
    {
    byte filter = tag.getByte("f");
    if(tag.hasKey("rem"))
      {
      info.removeRoutePoint(filter);
      }
    else if(tag.hasKey("u"))
      {
      info.movePointUp(filter);
      }
    else if(tag.hasKey("d"))
      {
      info.movePointDown(filter);
      }
    }
  }

@Override
public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
  {
  super.onCraftGuiClosed(par1EntityPlayer);  
  if(!par1EntityPlayer.worldObj.isRemote)
    {
    ItemStack stack = par1EntityPlayer.inventory.getCurrentItem();
    if(stack!=null && stack.itemID == ItemLoader.courierRouteSlip.itemID)
      {
      info.writeToItem(stack);
      }
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  info = new CourierRoutingInfo(tag.getCompoundTag("slip"));
  if(this.gui!=null)
    {
    this.gui.refreshGui();
    }
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  List<NBTTagCompound> tagList = new ArrayList<NBTTagCompound>();
  NBTTagCompound tag = new NBTTagCompound();
  tag.setCompoundTag("slip", info.getNBTTag());
  tagList.add(tag);
  return tagList;
  }

}
