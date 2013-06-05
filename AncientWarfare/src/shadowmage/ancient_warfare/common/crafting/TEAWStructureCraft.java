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
package shadowmage.ancient_warfare.common.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public class TEAWStructureCraft extends TEAWCrafting implements IInventory, ISidedInventory
{

AWInventoryBasic inventory = new AWInventoryBasic(10);
int[] resultSlot = new int[]{9};
int[] craftMatrix = new int[]{0,1,2,3,4,5,6,7,8};
ResourceListRecipe recipe = null;
public boolean isStarted = false;
public int displayProgress;
public int displayProgressMax;
short compileTime = 0;
short compileTimeMax = 5;

/**
 * 
 */
public TEAWStructureCraft()
  {
  this.modelID = 2;
  }

@Override
public void onBlockClicked(EntityPlayer player)
  {
  GUIHandler.instance().openGUI(GUIHandler.CIVIL_ENGINEERING, player, worldObj, xCoord, yCoord, zCoord);
  }

@Override
public void updateEntity()
  {  
  if(this.isStarted)
    {
    if(this.recipe==null)
      {
      this.setFinished();
      return;
      }
    if(this.compileTime>=this.compileTimeMax)
      {
      if(this.tryRemoveItem())
        {
        if(this.isRecipeFinished())
          {        
          this.produceItem();
          this.setFinished();        
          }
        else
          {
          this.compileTime = 0;
          }
        }
      }
    if(this.compileTime<this.compileTimeMax)
      {
      this.compileTime++;
      this.displayProgress++;
      }
    }
  }

protected int calcTotalTime()
  {
  int time = 0;
  for(ItemStackWrapperCrafting item : recipe.resources)
    {
    time += item.getQuantity() * 5;
    }
  return time;
  }

protected boolean tryRemoveItem()
  {
  for(ItemStackWrapperCrafting item : recipe.resources)
    {
    if(item.getRemainingNeeded() > 0 && InventoryTools.getCountOf(inventory, item.getFilter(), craftMatrix)>0)
      {
      Config.logDebug("found matching filter");
      InventoryTools.tryRemoveItems(inventory, item.getFilter(), 1, 0, 8);
      item.setRemainingNeeded(item.getRemainingNeeded()-1);
      Config.logDebug("decremented item : "+item.getFilter().toString() + " remaining count : "+item.getRemainingNeeded());    
      return true;
      }
    }
  return false;
  }

protected boolean isRecipeFinished()
  {
  Config.logDebug("checking if recipe finished");
  for(ItemStackWrapperCrafting item : recipe.resources)
    {
    if(item.getRemainingNeeded()>0)
      {
      Config.logDebug("sensing recipe not finished...");
      return false;
      }
    }
  return true;
  }

protected boolean canSetFinished()
  {
  return InventoryTools.canHoldItem(inventory, recipe.getResult(), recipe.getResult().stackSize, 9, 9);
  }

protected void setFinished()
  {
  this.isStarted = false;
  this.compileTimeMax = 0;
  this.compileTime = 0;
  this.recipe = null;
  this.displayProgress = 0;
  this.displayProgressMax = 0;
  }

protected void produceItem()
  {
  InventoryTools.tryMergeStack(inventory, recipe.getResult().copy(), resultSlot);
  }

@Override
public boolean canUpdate()
  {
  return true;
  }

public void validateAndSetRecipe(ResourceListRecipe recipe)
  {
  if(this.isStarted || this.recipe!=null || recipe == null){return;}
  this.recipe = recipe.copy();
  this.isStarted = true;
  this.displayProgressMax = this.calcTotalTime();
  Config.logDebug("setting recipe and working....recipe: "+recipe);
  }

public void clearWork()
  {
  this.setFinished();
  }

@Override
public void readDescriptionPacket(NBTTagCompound tag)
  {
  
  }

@Override
public void writeDescriptionData(NBTTagCompound tag)
  {
  
  }

@Override
public void writeExtraNBT(NBTTagCompound tag)
  {
  if(this.recipe!=null)
    {
    tag.setCompoundTag("rec", this.recipe.getNBTTag());
    }
  tag.setBoolean("work", this.isStarted);
  tag.setShort("time", this.compileTime);
  tag.setCompoundTag("inv", this.inventory.getNBTTag());
  tag.setInteger("dtime", displayProgress);
  tag.setInteger("dmax", displayProgressMax);  
  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  {
  if(tag.hasKey("rec"))
    {
    this.recipe = new ResourceListRecipe(tag.getCompoundTag("rec"));
    }
  if(tag.hasKey("inv"))
    {
    this.inventory.readFromNBT(tag.getCompoundTag("inv"));
    }
  this.isStarted = tag.getBoolean("work");
  this.compileTime = tag.getShort("time");
  this.displayProgress = tag.getInteger("dtime");
  this.displayProgressMax = tag.getInteger("dmax");
  }

@Override
public int[] getAccessibleSlotsFromSide(int var1)
  {
  return (var1==1 || var1==0) ? resultSlot : craftMatrix;
  }

@Override
public boolean canInsertItem(int slotNum, ItemStack itemstack, int side)
  {
  return slotNum==9? false : (side ==1 || side==0) ? false : true;
  }

@Override
public boolean canExtractItem(int slotNum, ItemStack itemstack, int side)
  {
  return true;
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
  return "AW.EngineeringStation";
  }

@Override
public boolean isInvNameLocalized()
  {  
  return false;
  }

@Override
public int getInventoryStackLimit()
  {  
  return 64;
  }

@Override
public boolean isUseableByPlayer(EntityPlayer entityplayer)
  {  
  return true;
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
  return i == 9 ? false : true;
  }

/**
 * @return
 */
public ResourceListRecipe getRecipe()
  {
  return this.recipe;
  }

/**
 * 
 */
public void stopWorkAndClearRecipe()
  {
  this.setFinished();
  }

}
