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
package shadowmage.ancient_warfare.common.crafting;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.ITEWorkSite;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public abstract class TEAWCrafting extends TileEntity implements IInventory, ISidedInventory
{


public int teamNum;
public int orientation;
protected int modelID;
protected AWInventoryBasic inventory;
public int[] craftMatrix;
public int[] resultSlot;
public int[] bookSlot;
protected ResourceListRecipe recipe;
protected boolean isWorking = false;
protected boolean isStarted = false;
protected String workingPlayerName = null;
protected PlayerEntry workingPlayerEntry = null;
protected int workProgress = 0;
protected int workProgressMax = 0;

int recipeStartCheckDelayTicks = 0;

protected boolean canUpdate = false;

public TEAWCrafting()
  {
  }

public abstract void readDescriptionPacket(NBTTagCompound tag);
public abstract void writeDescriptionData(NBTTagCompound tag);
public abstract void writeExtraNBT(NBTTagCompound tag);
public abstract void readExtraNBT(NBTTagCompound tag);
public abstract void onBlockClicked(EntityPlayer player);

public int getOrientation()
  {
  return orientation;
  }

public int getModelID()
  {
  return this.modelID;
  }

public void setOrientation(int face)
  {
  this.orientation = face;
  }

@Override
public boolean canUpdate()
  {
  return true;
  }

@Override
public void updateEntity()
  {
  super.updateEntity();
  if(this.worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  this.updateCrafting();
  }

public boolean isHostile(int sourceTeam)
  {
  if(this.worldObj==null)
    {
    return false;
    }
  return TeamTracker.instance().isHostileTowards(worldObj, sourceTeam, teamNum);
  }

/************************************************PLAYER INTERACTION METHODS*************************************************/
public void setStarted(boolean value)
  {
  this.isStarted = value;
  }

public boolean isStarted()
  {
  return isStarted;
  }

/************************************************CRAFTING METHODS*************************************************/

protected void updateCrafting()
  {
  if(isStarted)
    {
    if(this.recipe!=null && this.workingPlayerName!=null)
      {
      if(this.isWorking)
        {
        if(this.workProgress<this.workProgressMax)
          {
          if(this.canUpdate || !Config.useNpcWorkForCrafting)
            {
            this.workProgress++;
            this.canUpdate = false;
            }
          }
        if(this.workProgress>=this.workProgressMax)
          {
          if(tryFinishCrafting())
            {   
            tryStartCrafting();
            }
          }
        }
      else
        {
        if(this.recipeStartCheckDelayTicks>0)
          {
          this.recipeStartCheckDelayTicks--;
          }
        else
          {
          this.recipeStartCheckDelayTicks = Config.npcAITicks*10;
          this.tryStartCrafting();        
          }
        }
      }
    else if(this.workingPlayerName==null)
      {
      this.isWorking = false;
      this.isStarted = false;
      this.workProgress = 0;
      this.workProgressMax = 0;
      this.recipe = null;
      }
    else if(this.recipe==null)
      {
      this.isWorking = false;
      this.isStarted = false;
      this.workProgress = 0;
      this.workProgressMax = 0;
      }
    else
      {
      this.isWorking = false;
      this.workProgress = 0;
      this.workProgressMax = 0;
      }
    }  
  }

protected boolean tryStartCrafting()
  {  
  if(this.recipe!=null && this.recipe.doesInventoryContainResources(inventory, craftMatrix))
    {
    this.recipe.removeResourcesFrom(inventory, craftMatrix, this);
    this.workProgress =0;
    this.isWorking = true;
    return true;
    }
  this.isWorking = false;
  return false;
  }

protected boolean tryFinishCrafting()
  {
  if(canFinishCrafting())
    {
    InventoryTools.tryMergeStack(inventory, recipe.result.copy(), resultSlot);
    this.workProgress = 0;
    this.recipeStartCheckDelayTicks = 0;
    return true;
    }
  return false;
  }

protected boolean canFinishCrafting()
  {
  return recipe!=null && InventoryTools.canHoldItem(inventory, recipe.result, recipe.result.stackSize, resultSlot[0], resultSlot[0]);
  }

public void setRecipe(ResourceListRecipe recipe)
  {
  if(!this.isStarted && !this.isWorking && recipe!=null)
    {
    this.recipe = recipe;
    this.workProgressMax = recipe.getResourceItemCount() * 20;
    this.workProgress = 0;
    this.isWorking = false;
    this.recipeStartCheckDelayTicks = 0;
    }
  }

public void stopAndClear()
  {
  this.setStarted(false);
  this.recipe = null;
  this.workProgress = 0;
  this.workProgressMax = 0;
  this.isWorking = false;
  }

public ResourceListRecipe getRecipe()
  {
  return this.recipe;
  }

public PlayerEntry getWorkingPlayerEntry()
  {
  return this.workingPlayerEntry;
  } 

public void setCanUpdate()
  {
  this.canUpdate = true;
  }

public boolean isWorking()
  {
  return isWorking;
  }

public int getWorkProgress()
  {
  return this.workProgress;
  }

public int getWorkProgressMax()
  {
  return this.workProgressMax;
  }

/************************************************DATA METHODS*************************************************/
@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.orientation = tag.getByte("face");
  this.workProgress = tag.getInteger("time");
  this.workProgressMax = tag.getInteger("timeMax");
  this.isWorking = tag.getBoolean("work");
  this.isStarted = tag.getBoolean("started");
  this.teamNum = tag.getInteger("team");
  if(tag.hasKey("name"))
    {
    this.workingPlayerName = tag.getString("name");
    this.workingPlayerEntry = PlayerTracker.instance().getEntryFor(workingPlayerName);
    }
  if(tag.hasKey("inv") && this.inventory!=null)
    {
    this.inventory.readFromNBT(tag.getCompoundTag("inv"));
    }
  if(tag.hasKey("recipe"))
    {
    this.recipe = new ResourceListRecipe(tag.getCompoundTag("recipe"));
    }
  this.readExtraNBT(tag);
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setByte("face", (byte)this.orientation);
  tag.setInteger("time", this.workProgress);
  tag.setInteger("timeMax", this.workProgressMax);
  tag.setBoolean("work", this.isWorking);
  tag.setBoolean("started", this.isStarted);
  tag.setInteger("team", this.teamNum);
  if(this.workingPlayerName!=null)
    {
    tag.setString("name", this.workingPlayerName);
    }
  if(this.inventory!=null)
    {
    tag.setCompoundTag("inv", this.inventory.getNBTTag());
    }
  if(this.recipe!=null)
    {
    tag.setCompoundTag("recipe", this.recipe.getNBTTag());
    }
  this.writeExtraNBT(tag);
  }

@Override
public Packet getDescriptionPacket()
  {
  NBTTagCompound tag = new NBTTagCompound();
  this.writeDescriptionData(tag);
  tag.setByte("face", (byte) this.orientation);  
  Packet132TileEntityData pkt = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);  
  return pkt;
  }

@Override
public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
  super.onDataPacket(net, pkt);
  readDescriptionPacket(pkt.customParam1);
  this.orientation = pkt.customParam1.getByte("face");
  }

/************************************************INVENTORY METHODS*************************************************/
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
  return "AW.Crafting";
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
  if(this.bookSlot!=null && this.bookSlot.length>0)
    {
    String name = null;
    ItemStack stack = this.getStackInSlot(10);
    if(stack!=null && stack.itemID==ItemLoader.researchBook.itemID)
      {
      if(stack.hasTagCompound() && stack.getTagCompound().getCompoundTag("AWResInfo").hasKey("name"))
        {
        name = stack.getTagCompound().getCompoundTag("AWResInfo").getString("name");
        if(this.workingPlayerName==null || !this.workingPlayerName.equals(name))
          {
          this.workingPlayerName = name;
          this.workingPlayerEntry = PlayerTracker.instance().getEntryFor(workingPlayerName); 
          this.stopAndClear();
          }
        }    
      }
    if(name==null && this.workingPlayerName!=null)
      {   
      this.workingPlayerName = null;
      this.workingPlayerEntry = null;
      this.stopAndClear();
      }    
    }
  }

/************************************************SIDED INVENTORY METHODS*************************************************/
@Override
public int[] getAccessibleSlotsFromSide(int side)
  {
  if(side==0 || side==1)
    {
    if(this.resultSlot!=null)
      {
      return this.resultSlot;
      }
    }
  return craftMatrix;
  }

@Override
public boolean canInsertItem(int slot, ItemStack stack, int side)
  {
  if(side==0 || side==1)
    {
    return false;
    }
  else if(this.craftMatrix!=null)
    {
    for(int i = 0; i < this.craftMatrix.length; i++)
      {
      if(slot==this.craftMatrix[i])
        {
        return true;
        }
      }
    }
  return false;
  }

@Override
public boolean canExtractItem(int slot, ItemStack stack, int side)
  {
  if(this.bookSlot!=null && this.bookSlot.length>0 && slot== this.bookSlot[0])
    {
    return false;
    }
  return true;
  }

@Override
public boolean isStackValidForSlot(int i, ItemStack itemstack)
  {
  if(bookSlot!=null && bookSlot.length>0 && i == bookSlot[0])
    {
    return itemstack.itemID == ItemLoader.researchBook.itemID;
    }
  if(resultSlot!=null && resultSlot.length>0 && i== resultSlot[0])
    {
    return false;
    }
  return true;
  }

}
