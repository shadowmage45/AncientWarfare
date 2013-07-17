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
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.inventory.AWInventoryMailbox;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.tracker.MailboxData;
import shadowmage.ancient_warfare.common.tracker.entry.BoxData;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.Trig;

public abstract class TEMailBoxBase extends TEMachine implements IInventory, ISidedInventory
{

protected AWInventoryMailbox inventory;
protected int mailTicks = 0;
protected BoxData boxData = null;
public int mailBoxSize = 38;

/**
 * 
 */
public TEMailBoxBase()
  {
  this.canUpdate = true;
  this.shouldWriteInventory = false;  
  this.facesOpposite = true;
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
  tag.setInteger("mailTick", this.mailTicks);
  }

/************************************************SIDED INVENTORY METHODS*************************************************/

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
