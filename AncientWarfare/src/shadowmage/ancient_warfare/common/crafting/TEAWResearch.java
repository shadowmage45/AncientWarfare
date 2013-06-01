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
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class TEAWResearch extends TEAWCrafting implements IInventory, ISidedInventory
{

protected AWInventoryBasic inventory = new AWInventoryBasic(10);
int[] bookSlot = new int[]{0};
int[] researchSlots = new int[]{1,2,3,4,5,6,7,8,9};

/**
 * server side value..client side is kept in the container...
 */
public int researchProgress = 0;

public IResearchGoal currentResearch = null;

public String researchingPlayer = null;

/**
 * 
 */
public TEAWResearch()
  {
  this.modelID = 0;
  }

@Override
public void updateEntity()
  {
  if(this.worldObj.isRemote){return;}
  if(this.currentResearch!=null)
    {
    if(this.researchingPlayer==null || this.researchingPlayer.equals(""))
      {
      this.researchingPlayer = null;
      this.currentResearch = null;
      this.resetProgress(); 
      return;
      }
    this.researchProgress++;
    if(this.researchProgress>=this.currentResearch.getResearchTime())
      {
      this.setResearchFinished();
      }
    }
  }

public void startResearch(IResearchGoal goal)
  {
  if(this.researchingPlayer!=null)
    {
    this.currentResearch = goal;
    this.researchProgress = 0;
    }
  else
    {
    this.currentResearch = null;
    this.researchProgress = 0;
    }
  }

protected void setResearchFinished()
  {
  PlayerTracker.instance().addResearchToPlayer(worldObj, researchingPlayer, this.currentResearch.getGlobalResearchNum());
  this.currentResearch = null;
  this.resetProgress();
  }

@Override
public boolean canUpdate()
  {
  return true;
  }

@Override
public void onBlockClicked(EntityPlayer player)
  {
  GUIHandler.instance().openGUI(GUIHandler.RESEARCH, player, worldObj, xCoord, yCoord, zCoord);
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
  tag.setCompoundTag("inv", this.inventory.getNBTTag());
  if(this.currentResearch!=null){tag.setInteger("goal", currentResearch.getGlobalResearchNum());}
  tag.setInteger("prog", this.researchProgress);
  if(this.researchingPlayer!=null){tag.setString("name", this.researchingPlayer);}  
  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  {
  if(tag.hasKey("inv"))
    {
    this.inventory.readFromNBT(tag.getCompoundTag("inv"));
    }
  if(tag.hasKey("name")){this.researchingPlayer = tag.getString("name");}
  if(tag.hasKey("prog")){this.researchProgress = tag.getInteger("prog");}
  if(tag.hasKey("goal")){this.currentResearch = ResearchGoal.getGoalByID(tag.getInteger("goal"));}
  }

@Override
public int[] getAccessibleSlotsFromSide(int var1)
  {
  if(var1==0)
    {
    return bookSlot;
    }
  return researchSlots;
  }

@Override
public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {
  return isStackValidForSlot(i, itemstack);
  }

@Override
public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
  return isStackValidForSlot(i, itemstack);
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
  return "AW.ResearchTable";
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
  if(i==0)
    {
    return itemstack==null || itemstack.itemID==ItemLoader.researchBook.itemID;
    }
  return true;
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
  ItemStack stack = this.getStackInSlot(0);
  if(stack!=null && stack.itemID==ItemLoader.researchBook.itemID)
    {
    if(stack.hasTagCompound() && stack.getTagCompound().getCompoundTag("AWResInfo").hasKey("name"))
      {
      name = stack.getTagCompound().getCompoundTag("AWResInfo").getString("name");
      if(this.researchingPlayer==null || !this.researchingPlayer.equals(name))
        {
        Config.logDebug("setting researching player to: "+name);
        this.researchingPlayer = name;
        this.resetProgress();
        this.currentResearch = null;
        }
      }    
    }
  if(name==null && this.researchingPlayer!=null)
    {   
    Config.logDebug("clearing researching player");
    this.currentResearch = null;
    this.researchingPlayer = null;
    this.resetProgress();    
    }
  }

public void resetProgress()
  {
  this.researchProgress = 0;  
  }


}
