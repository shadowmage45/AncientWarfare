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
package shadowmage.ancient_warfare.common.machine;

import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TETrashcan extends TEMachine implements IInventory
{

AWInventoryBasic inventory = new AWInventoryBasic(9);
int tickDivider = 0;

/**
 * 
 */
public TETrashcan()
  {
  this.guiNumber = GUIHandler.TRASHCAN;
  this.canUpdate = true;
  this.facesOpposite = true;
  }

@Override
public void updateEntity()
  {
  if(tickDivider>=4 && this.worldObj!=null && !this.worldObj.isRemote && this.worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
    {
    ItemStack fromSlot = null;
    for(int i = 0; i < this.getSizeInventory(); i++)
      {
      fromSlot = this.getStackInSlot(i);
      if(fromSlot!=null)
        {
        fromSlot.stackSize--;
        if(fromSlot.stackSize<=0)
          {
          this.setInventorySlotContents(i, null);
          }
        break;
        }
      }
    tickDivider = 0;
    }
  tickDivider++;
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
  return "AWInventory.trash";
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
public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
  return true;
  }

}
