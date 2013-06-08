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

public class TEAWEngineering extends TEAWCrafting implements IInventory, ISidedInventory
{

AWInventoryBasic inventory = new AWInventoryBasic(10);
int[] resultSlot = new int[]{9};
int[] craftMatrix = new int[]{0,1,2,3,4,5,6,7,8};
ResourceListRecipe currentRecipe = null;
public int displayProgress = 0;
public int displayProgressMax = 0;
public boolean isWorking = false;
public boolean shouldUpdate = false;

/**
 * 
 */
public TEAWEngineering()
  {
  this.modelID = 1;
  }

public void validateAndSetRecipe(ResourceListRecipe recipe)
  {
  if(this.isWorking){return;}
  recipe = AWCraftingManager.instance().validateRecipe(recipe);
  if(recipe!=null)
    {
    this.setRecipe(recipe);    
    }
  }

public ResourceListRecipe getRecipe()
  {
  return this.currentRecipe;
  }

public void stopWork()
  {
  this.isWorking = false;
  this.displayProgress = 0;
  this.displayProgressMax = 0;
  }

public void clearRecipe()
  {
  if(this.isWorking)
    {
    /**
     * TODO refund used items
     */
    }
  this.currentRecipe = null;
  }

@Override
public void updateEntity()
  {  
  super.updateEntity();
  if(this.worldObj==null || this.worldObj.isRemote){return;}
  if(this.currentRecipe!=null && !this.isWorking)
    {    
    this.displayProgress = 0;
    if(this.tryStartRecipe())
      {
      this.isWorking = true;
      }
    }
  if(this.isWorking && this.shouldUpdate)
    {
    this.displayProgress++;
    }
  if(this.isWorking && this.currentRecipe!=null && this.displayProgress>=this.displayProgressMax)
    {
    if(!this.tryFinishRecipe())
      {
      this.displayProgress = this.displayProgressMax;
      }
    }
  this.shouldUpdate = false;
  }

protected boolean tryStartRecipe()
  {
  boolean start = this.currentRecipe.doesInventoryContainResources(inventory, craftMatrix);
  if(start)
    {
    int count = this.currentRecipe.getResourceItemCount();
    this.currentRecipe.removeResourcesFrom(inventory, craftMatrix);
    this.displayProgress =0;
    this.displayProgressMax = count * 5;
    }
  return start;
  }

protected boolean tryFinishRecipe()
  {
  if(InventoryTools.canHoldItem(inventory, currentRecipe.result, currentRecipe.result.stackSize, 9, 9))
    {
    InventoryTools.tryMergeStack(inventory, currentRecipe.result.copy(), resultSlot);
    this.isWorking = false;
    this.displayProgress = 0;
    return true;
    }
  return false;
  }

@Override
public boolean canUpdate()
  {
  return true;
  }

@Override
public void onBlockClicked(EntityPlayer player)
  {
  if(player.worldObj.isRemote){return;}
  GUIHandler.instance().openGUI(GUIHandler.ENGINEERING, player, player.worldObj, xCoord, yCoord, zCoord);
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
  tag.setInteger("prog", displayProgress);
  tag.setInteger("progMax", displayProgressMax);
  tag.setBoolean("work", this.isWorking);
  tag.setCompoundTag("inv", this.inventory.getNBTTag());
  if(this.currentRecipe!=null)
    {
    tag.setCompoundTag("rec", this.currentRecipe.getNBTTag());
    }
  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  {
  this.displayProgress = tag.getInteger("prog");
  this.displayProgressMax = tag.getInteger("progMax");
  this.isWorking = tag.getBoolean("work");
  if(tag.hasKey("inv"))
    {
    this.inventory.readFromNBT(tag.getCompoundTag("inv"));
    }
  if(tag.hasKey("rec"))
    {
    this.currentRecipe = new ResourceListRecipe(tag.getCompoundTag("rec"));
    }
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

}
