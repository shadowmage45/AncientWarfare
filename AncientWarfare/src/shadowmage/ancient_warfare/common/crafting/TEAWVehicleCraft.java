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
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.types.VehicleType;

public class TEAWVehicleCraft extends TEAWCrafting implements IInventory, ISidedInventory
{

public ResourceListRecipe recipe = null;
public int displayProgress = 0;
public int displayProgressMax = 0;
int progressPerWork = 20;
public boolean shouldUpdate = false;
public boolean isWorking = false;

AWInventoryBasic inventory = new AWInventoryBasic(11);

int[] craftMatrix= new int[]{0,1,2,3,4,5,6,7,8};
int[] outputSlot= new int[]{9};
int[] bookSlot = new int[]{10};
public String workingPlayer;

int vehicleType = -1;
int vehicleLevel = -1;

/**
 * 
 */
public TEAWVehicleCraft()
  {
  this.modelID = 3;
  }

@Override
public void updateEntity()
  {  
  super.updateEntity();
  if(this.recipe!=null)
    {
    if(isWorking)
      {
      if(this.displayProgress>=this.displayProgressMax)
        {
        this.displayProgress = this.displayProgressMax;
        if(this.tryFinish())
          {
          this.displayProgress = 0;
          this.displayProgressMax = 0;
          this.isWorking = false;
          }
        }
      if(this.shouldUpdate)
        {
        this.displayProgress++;
        shouldUpdate = false;
        }
      }
    else
      {
      if(this.tryStart())
        {
        this.isWorking = true;
        }
      }
    }       
  }

public void validateAndSetRecipe(ResourceListRecipe recipe)
  {
  if(recipe!=null)
    {
    Config.logDebug("setting recipe to : "+recipe);    
    this.recipe = recipe;
    ItemStack result = recipe.getResult();    
    if(result!=null)
      {
      int type = result.getItemDamage();
      int level = 0;
      IVehicleType t = VehicleType.getVehicleType(type);
      if(result.hasTagCompound() && result.getTagCompound().hasKey("AWVehSpawner"))
        {
        level = result.getTagCompound().getCompoundTag("AWVehSpawner").getInteger("lev");
        }
      this.vehicleType = type;
      this.vehicleLevel = level;
      Config.logDebug("set working vehicle to "+t);
      }
    }
  else
    {
    this.stopAndClearRecipe();
    }
  this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
  }

public void stopAndClearRecipe()
  {
  this.isWorking = false;
  this.recipe = null;
  this.displayProgress = 0;
  this.displayProgressMax = 0;
  this.vehicleLevel = -1;
  this.vehicleType = -1;  
  }

/**
 * try starting and removing items
 * @return
 */
protected boolean tryStart()
  {
  if(this.recipe!=null)
    {
    boolean start = this.recipe.doesInventoryContainResources(inventory, craftMatrix);
    if(start)
      {
      Config.logDebug("starting working");
      int count = this.recipe.getResourceItemCount();
      this.recipe.removeResourcesFrom(inventory, craftMatrix);
      this.displayProgress =0;
      this.displayProgressMax = count * 20;
      }
    return start;
    }
  return false;
  }

/**
 * try finishing (place items into output slot)
 * @return
 */
protected boolean tryFinish()
  {
  if(InventoryTools.canHoldItem(inventory, recipe.result, recipe.result.stackSize, 9, 9))
    {
    Config.logDebug("setting finished and producing item");
    InventoryTools.tryMergeStack(inventory, recipe.result.copy(), outputSlot);
    this.isWorking = false;
    this.displayProgress = 0;
    return true;
    }
  return false;
  }

@Override
public int[] getAccessibleSlotsFromSide(int var1)
  {
  if(var1==0 || var1==1)
    {
    return outputSlot;    
    }
  else
    {
    return craftMatrix;
    }
  }

@Override
public boolean isStackValidForSlot(int i, ItemStack itemstack)
  {
  if(i==10)
    {
    Config.logDebug("trying place into slot 10:");
    return itemstack.itemID == ItemLoader.researchBook.itemID;
    }
  else if(i==9)
    {Config.logDebug("trying place into slot 9:");
    return false;
    }
  return true;
  }

@Override
public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {
  if(j==0 || j==1)
    {
    return false;
    }
  if(i==10)
    {
    Config.logDebug("trying place into slot 10:");
    return itemstack.itemID == ItemLoader.researchBook.itemID;
    }
  else if(i==9)
    {
    Config.logDebug("trying place into slot 9:");
    return false;
    }
  return true;
  }

@Override
public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
  return i== 10 ? false : true;
  }

@Override
public void readDescriptionPacket(NBTTagCompound tag)
  {
  if(tag.hasKey("type"))
    {
    this.vehicleType = tag.getInteger("type");
    this.vehicleLevel = tag.getInteger("lev");
    }
  }

@Override
public void writeDescriptionData(NBTTagCompound tag)
  {
  tag.setInteger("type", this.vehicleType);
  tag.setInteger("lev", this.vehicleLevel);
  }

@Override
public void writeExtraNBT(NBTTagCompound tag)
  {
  tag.setCompoundTag("inv", this.inventory.getNBTTag());
  tag.setBoolean("work", this.isWorking);
  tag.setBoolean("up", this.shouldUpdate);
  tag.setInteger("time", displayProgress);
  tag.setInteger("tMax", this.displayProgressMax);
  if(this.workingPlayer!=null)
    {
    tag.setString("name", this.workingPlayer);    
    }
  if(this.recipe!=null)
    {
    tag.setCompoundTag("rec", this.recipe.getNBTTag());
    }
  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  {
  this.inventory.readFromNBT(tag.getCompoundTag("inv"));
  this.isWorking = tag.getBoolean("work");
  this.shouldUpdate = tag.getBoolean("up");
  this.displayProgress = tag.getInteger("time");
  this.displayProgressMax = tag.getInteger("tMax");
  if(tag.hasKey("name"))
    {
    this.workingPlayer = tag.getString("name");
    }
  if(tag.hasKey("rec"))
    {
    this.recipe = new ResourceListRecipe(tag.getCompoundTag("rec"));
    }
  }

@Override
public void onBlockClicked(EntityPlayer player)
  {
  if(!player.worldObj.isRemote)
    {
    GUIHandler.instance().openGUI(GUIHandler.VEHICLE_CRAFT, player, player.worldObj, xCoord, yCoord, zCoord);
    }
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
  return "AW.VehicleCrafting";
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
public void onInventoryChanged()
  {
  super.onInventoryChanged();
  if(worldObj==null || worldObj.isRemote)
    {
    return;
    }
  String name = null;
  ItemStack stack = this.getStackInSlot(10);
  if(stack!=null && stack.itemID==ItemLoader.researchBook.itemID)
    {
    if(stack.hasTagCompound() && stack.getTagCompound().getCompoundTag("AWResInfo").hasKey("name"))
      {
      name = stack.getTagCompound().getCompoundTag("AWResInfo").getString("name");
      if(this.workingPlayer==null || !this.workingPlayer.equals(name))
        {
        Config.logDebug("setting researching player to: "+name);
        this.workingPlayer = name;
        this.isWorking = false;
        this.recipe = null;
        this.displayProgress = 0;
        this.displayProgressMax = 0;
        }
      }    
    }
  if(name==null && this.workingPlayer!=null)
    {   
    Config.logDebug("clearing researching player");
    this.workingPlayer = null;
    this.isWorking = false;
    this.recipe = null;
    this.displayProgress = 0;
    this.displayProgressMax = 0;
    }
  }
}
