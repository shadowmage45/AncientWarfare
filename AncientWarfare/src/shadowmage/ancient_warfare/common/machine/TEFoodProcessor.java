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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.item.ItemLoader;

public class TEFoodProcessor extends TEMachine implements IInventory, ISidedInventory
{

int[] bottomIndices = new int[1];
int[] otherIndices = new int[9];
int storedFoodValue = 0;
AWInventoryBasic inventory = new AWInventoryBasic(10);

public TEFoodProcessor()
  {
  this.canUpdate = true;
  this.bottomIndices[0]=0;
  for(int i =0 ; i < 9; i++)
    {
    otherIndices[i] = i+1;
    }
  this.guiNumber = GUIHandler.FOOD_PROCESSOR;
  this.facesOpposite = true;
  }

int delayTicks = 0;

@Override
public void updateEntity()
  {
  super.updateEntity();
  if(this.worldObj==null || this.worldObj.isRemote || !this.worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)){return;}
  delayTicks--;
  if(delayTicks>0){return;}
  delayTicks = 5;
  ItemStack fromSlot;
  for(int b : otherIndices)
    {
    fromSlot = this.getStackInSlot(b);
    if(fromSlot==null || !(fromSlot.getItem() instanceof ItemFood)|| fromSlot.itemID == Item.rottenFlesh.itemID || fromSlot.itemID==ItemLoader.rations.itemID){continue;}
    ItemFood item = (ItemFood)fromSlot.getItem();
    this.storedFoodValue += item.getHealAmount();
    fromSlot.stackSize--;
    if(fromSlot.stackSize<=0)
      {
      this.setInventorySlotContents(b, null);
      }
    break;
    } 
  if(this.storedFoodValue>=2)
    {
    fromSlot = this.getStackInSlot(0);
    if(fromSlot==null || (fromSlot.itemID==ItemLoader.rations.itemID && fromSlot.stackSize<fromSlot.getMaxStackSize()))
      {
      if(fromSlot==null)
        {
        fromSlot = new ItemStack(ItemLoader.rations);
        this.setInventorySlotContents(0, fromSlot);
        }
      else
        {
        fromSlot.stackSize++;
        }
      this.storedFoodValue-=2;
      }
    }
//  Config.logDebug("food value available: "+this.storedFoodValue);
  }

/********************************SIDED INVENTORY METHODS*******************************************/
@Override
public int[] getAccessibleSlotsFromSide(int var1)
  {
  return var1==0? bottomIndices : otherIndices;
  }

@Override
public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {
  return j != 0 && isItemValidForSlot(i, itemstack);
  }

@Override
public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
  return true;
  }

/********************************INVENTORY METHODS*******************************************/
 
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
  return "AWInventory.food";
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
  return i==0 ? false : (itemstack.getItem() instanceof ItemFood);
  }


/********************************DATA SYNCH METHODS*******************************************/
@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.storedFoodValue = tag.getInteger("food");
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setInteger("food", this.storedFoodValue);
  }


}
