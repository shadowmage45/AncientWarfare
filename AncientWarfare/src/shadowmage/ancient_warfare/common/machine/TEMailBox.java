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
package shadowmage.ancient_warfare.common.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.inventory.AWInventoryMailbox;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.tracker.MailboxData;
import shadowmage.ancient_warfare.common.tracker.entry.BoxData;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.Trig;

public class TEMailBox extends TEMachine implements IInventory, ISidedInventory
{

AWInventoryMailbox inventory;
int[][] sideSlotIndices = new int[4][4];
int[] topIndices = new int[4];
int[] bottomIndices = new int[18];
int mailTicks = 0;
BoxData boxData = null;

public int mailBoxSize = 38;

/**
 * 
 */
public TEMailBox()
  {
  this.machineNumber = 1;
  this.canUpdate = true;
  this.shouldWriteInventory = false;
  this.guiNumber = GUIHandler.MAILBOX;
  this.inventory = new AWInventoryMailbox(mailBoxSize, null);
  int index = 0;  
  for(int i = 0; i < 18; i++, index++)
    {
    bottomIndices[i]=index;
    }
  for(int i = 0; i < 4; i++, index++)
    {
    topIndices[i] = index;
    }  
  for(int i = 0; i < 4; i++)
    {
    for(int k = 0; k <4; k++, index++)
      {
      sideSlotIndices[i][k]=index;
      }
    }
  }

/************************************************UPDATE METHODS*************************************************/

@Override
public void updateEntity()
  {
  if(this.worldObj==null || this.worldObj.isRemote || this.boxData==null){return;}
  this.mailTicks++;
  if(this.mailTicks>=Config.mailSendTicks)
    {
    this.mailTicks = 0;
    /**
     * check each side destination in data, if valid destination, send packages
     */
    String destination = null;
    int[] slots;
    int slot;
    ItemStack stack;
    BoxData data;
    for(int i = 1; i <6; i++)
      {
      destination = this.boxData.getSideName(i);      
      if(destination==null){continue;}      
      data=MailboxData.instance().getBoxDataFor(destination, mailBoxSize);
      if(data==null || !data.isAssigned()){continue;}
      slots = i==1? this.topIndices : this.sideSlotIndices[i-2];
      for(int k = 0; k < slots.length; k++)
        {
        slot = slots[k];
        stack = this.getStackInSlot(slot);
        if(stack==null){continue;}
        
        float dist = Trig.getDistance(xCoord, yCoord, zCoord, data.posX(), data.posY(), data.posZ());
        data.addIncomingItem(stack, (int)dist*5);
        this.setInventorySlotContents(slot, null);
        Config.logDebug("adding stack to mail route for: "+destination + " time: "+((int)dist*5));
        }
      }    
    /**
     * check mailbox for any incoming packages and attempt merge into inventory
     */
    this.boxData.tryDeliverMail(this, bottomIndices);
    }
  }

/************************************************BOX INTERACTION METHODS*************************************************/

public BoxData getBoxData()
  {
  return this.boxData;
  }

public void setBoxData(BoxData data)
  {
  this.boxData = data;
  this.mailTicks = 0;
  this.inventory.setBoxData(data);
  }

public void onBlockBreak()
  {  
  if(this.worldObj!=null && !this.worldObj.isRemote && this.boxData!=null)
    {
    this.boxData.clearAssignment();
    }
  }

/************************************************DATA SYNCH METHODS*************************************************/

@Override
public void readFromNBT(NBTTagCompound tag)
  {  
  super.readFromNBT(tag);
  if(tag.hasKey("boxName"))
    {
    String boxName = tag.getString("boxName");
    this.setBoxData(MailboxData.instance().getBoxDataFor(boxName, mailBoxSize));
    }
  this.mailTicks = tag.getInteger("mailTick");
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  if(this.boxData!=null)
    {
    tag.setString("boxName", this.boxData.getBoxName());
    }
  }

/************************************************SIDED INVENTORY METHODS*************************************************/

@Override
public int[] getAccessibleSlotsFromSide(int side)
  {
  if(this.boxData==null)
    {
    return new int[0];
    }
  if(side==0)
    {
    return bottomIndices;
    }
  else if(side==1)
    {
    return topIndices;
    }
  side = BlockTools.getCardinalFromSide(side);
  side = (side + this.rotation)%4;
  return sideSlotIndices[side];
  }

@Override
public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {  
  return this.boxData==null? false : true;
  }

@Override
public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
  if(this.boxData==null){return false;}
  if(j==1)
    {
    return true;
    }
  return false;
  }

/************************************************INVENTORY METHODS*************************************************/

@Override
public void onInventoryChanged()
  {
  super.onInventoryChanged();
  }

@Override
public int getSizeInventory()
  {
  return inventory.getSizeInventory();
  }

@Override
public ItemStack getStackInSlot(int i)
  {
  return inventory.getStackInSlot(i);
  }

@Override
public ItemStack decrStackSize(int i, int j)
  {
  return inventory.decrStackSize(i, j);
  }

@Override
public ItemStack getStackInSlotOnClosing(int i)
  {
  return inventory.getStackInSlotOnClosing(i);
  }

@Override
public void setInventorySlotContents(int i, ItemStack itemstack)
  {
  inventory.setInventorySlotContents(i, itemstack);
  }

@Override
public String getInvName()
  {
  return "AWInventory.mail";
  }

@Override
public boolean isInvNameLocalized()
  {
  return false;
  }

@Override
public int getInventoryStackLimit()
  {
  return inventory.getInventoryStackLimit();
  }

@Override
public boolean isUseableByPlayer(EntityPlayer entityplayer)
  {
  return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
  }

@Override
public void openChest()
  {
  }

@Override
public void closeChest()
  {
  }

@Override
public boolean isStackValidForSlot(int i, ItemStack itemstack)
  {
  return true;
  }
}
