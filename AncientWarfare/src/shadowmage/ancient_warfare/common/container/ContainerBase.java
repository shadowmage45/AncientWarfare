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

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IContainerGuiCallback;
import shadowmage.ancient_warfare.common.interfaces.IEntityContainerSynch;
import shadowmage.ancient_warfare.common.interfaces.IHandlePacketData;
import shadowmage.ancient_warfare.common.network.Packet03GuiComs;
import shadowmage.ancient_warfare.common.utils.InventoryTools;



/**
 * client-server synching container
 * @author Shadowmage
 *
 */
public abstract class ContainerBase extends Container implements IHandlePacketData
{

/**
 * the player who opened this container
 */
public final EntityPlayer player;

/**
 * the TE or Entity responsible for server-side base data, and multi-crafter support
 */
public final IEntityContainerSynch entity;
public IContainerGuiCallback gui;

public ContainerBase(EntityPlayer openingPlayer, IEntityContainerSynch synch)
  {
  this.player = openingPlayer;
  this.entity = synch;
  if(entity!=null)
    {
    entity.addPlayer(player);
    }
  }

public void setGui(IContainerGuiCallback gui)
  {
  this.gui = gui;
  }

public void sendDataToGUI(NBTTagCompound tag)
  {
  NBTTagCompound baseTag = new NBTTagCompound();
  baseTag.setTag("guiData", tag);
  this.sendDataToPlayer(baseTag);
  }

public void handleRawPacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("guiData"))
    {
    if(this.gui!=null)
      {    
      this.gui.handleDataFromContainer(tag.getCompoundTag("guiData"));
      }
    }
  else
    {
    this.handlePacketData(tag);
    }
  }

@Override
public boolean canInteractWith(EntityPlayer var1)
  {  
  if(entity!=null)
    {
    return entity.canInteract(var1);
    }
  return true;
  }

public void sendDataToServer(NBTTagCompound tag)
  {
  if(!player.worldObj.isRemote)
    {
    Config.logError("Attempt to send data to server FROM server");
    Exception e = new IllegalAccessException();
    e.printStackTrace();
    return;
    }
  Packet03GuiComs pkt = new Packet03GuiComs();
  pkt.setData(tag);
  AWCore.proxy.sendPacketToServer(pkt);
  }

/**
 * send data from server to populate client-side container
 * @param tag
 */
public void sendDataToPlayer(NBTTagCompound tag)
  {
  if(player.worldObj.isRemote)
    {
    Config.logError("Attempt to send data to client FROM client");
    Exception e = new IllegalAccessException();
    e.printStackTrace();
    return;
    }
  Packet03GuiComs pkt = new Packet03GuiComs();
  pkt.setData(tag);
  AWCore.proxy.sendPacketToPlayer(player, pkt);
  }

public abstract List<NBTTagCompound> getInitData();

@Override
protected boolean mergeItemStack(ItemStack inputStack, int startSlot, int stopSlot, boolean iterateBackwards)
  {
  boolean returnFlag = false;
  int k = startSlot;
  if(stopSlot<startSlot)//if some nubtard tried reversing indices//because who the fuck iterates backwards...
    {
    startSlot = stopSlot;
    stopSlot = k;
    }
  Slot slot;
  ItemStack stackFromSlot;
  if (inputStack.isStackable())
    {
    int numToMerge;
    for(int i =startSlot; i < stopSlot && inputStack.stackSize > 0 ; i++)
      {
      slot = (Slot)this.inventorySlots.get(k);
      if(slot==null)
        {
        continue;
        }      
      stackFromSlot = slot.getStack();
      if(InventoryTools.doItemsMatch(inputStack, stackFromSlot))
        {
        numToMerge = slot.getSlotStackLimit() - stackFromSlot.stackSize;
        numToMerge = numToMerge > inputStack.stackSize ? inputStack.stackSize : numToMerge;
        numToMerge = numToMerge + stackFromSlot.stackSize > stackFromSlot.getMaxStackSize() ? stackFromSlot.getMaxStackSize() - stackFromSlot.stackSize : numToMerge;       
        
        if(numToMerge>0)
          {
          inputStack.stackSize -= numToMerge;
          stackFromSlot.stackSize += numToMerge;
          slot.onSlotChanged();
          returnFlag = true;
          }
        }
      }
    }
  if (inputStack.stackSize > 0)
    {
    int numToMerge;
    for(int i =startSlot; i < stopSlot && inputStack.stackSize > 0 ; i++)
      {
      slot = (Slot)this.inventorySlots.get(k);
      stackFromSlot = slot.getStack();
      if(stackFromSlot==null)
        {
        slot.putStack(inputStack.copy());
        slot.onSlotChanged();
        inputStack.stackSize = 0;
        returnFlag = true;
        break;       
        }
      }
    }
  return returnFlag;
  }

}
