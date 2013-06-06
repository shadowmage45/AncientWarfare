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

import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TEAWVehicleCraft extends TEAWCraftingWorkSite
{

ResourceListRecipe recipe = null;
int displayProgress = 0;
int displayProgressMax = 0;
int progressPerWork = 20;
protected boolean isWorkSiteSet = false;
public boolean shouldUpdate = false;
public boolean isWorking = false;

int[] bookSlot = new int[10];

/**
 * 
 */
public TEAWVehicleCraft()
  {
  this.isWorkSite = true;
  
  /**
   * used as craft matrix
   */
  this.resourceSlotIndices = new int[]{0,1,2,3,4,5,6,7,8};
  
  /**
   * used as OUTPUT
   */
  this.otherSlotIndices = new int[]{9};
  }

@Override
public void updateEntity()
  {  
  super.updateEntity();
  if(this.isWorkSiteSet)
    {
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
  }

public void validateAndSetRecipe(ResourceListRecipe recipe)
  {
  recipe = AWCraftingManager.instance().validateRecipe(recipe);
  if(recipe!=null)
    {
    this.recipe = recipe;
    }
  }

/**
 * try starting and removing items
 * @return
 */
protected boolean tryStart()
  {
  return false;
  }

/**
 * try finishing (place items into output slot)
 * @return
 */
protected boolean tryFinish()
  {
  return false;
  }

protected void validateStructure()
  {
  boolean valid = true;
  outerLoop:
  for(int y = this.minY; y <=this.maxY; y++)
    {
    for(int x = this.minX ; x<=this.maxX; x++)
      {
      for(int z = this.minZ; z<=this.maxZ; z++)
        {
        if(worldObj.getBlockId(x, y, z) !=0 )
          {
          valid = false;
          break outerLoop;
          }
        if(y==this.minY)
          {
          if(worldObj.getBlockId(x, y-1, z) != Block.stoneBrick.blockID)
            {
            valid = false;
            break outerLoop;
            }
          }
        }
      }
    }
  if(!valid)
    {
    this.recipe = null;
    this.isWorking = false;
    this.shouldUpdate = false;
    }
  else
    {
    this.isWorkSiteSet = true;
    }
  }

@Override
public void readDescriptionPacket(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
  }

@Override
public void writeDescriptionData(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
  }

@Override
public void writeExtraNBT(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub

  }

@Override
public void onBlockClicked(EntityPlayer player)
  {
  Config.logDebug("TAWVehicleCraft block clicked!!");
  this.validateStructure();
  if(this.isWorkSiteSet)
    {
    Config.logDebug("work site is setup and can work");
    return;
    }  
  Config.logDebug("work site is not set, not doing work");
  }

@Override
public boolean hasWork()
  {
  this.hasWork = this.recipe!=null && this.isWorking;
  return hasWork;
  }

@Override
public void doWork(NpcBase npc)
  {
  if(this.recipe!=null && this.isWorking)
    {
    this.displayProgress += this.progressPerWork;
    if(this.displayProgress>this.displayProgressMax)
      {
      this.displayProgress = this.displayProgressMax;
      }
    }
  this.updateHasWork();
  }

@Override
protected void updateHasWork()
  {
  this.setHasWork(this.recipe!=null && this.isWorking);
  }

@Override
protected void onCivicUpdate()
  { 
  this.validateStructure();
  }

@Override
protected void scan()
  {
 
  }

@Override
protected void doWork(NpcBase npc, WorkPoint p)
  {
  Config.logError("SEVERE ERROR, HIT NOOP DOWORK BLOCK IN TEAWVEHICLECRAFT");
  }

@Override
protected TargetType validateWorkPoint(WorkPoint p)
  {
  Config.logError("SEVERE ERROR, HIT NOOP VALIDATEWORKPOINT BLOCK IN TEAWVEHICLECRAFT");
  if(this.recipe==null)
    {
    return TargetType.NONE;
    }
  return p.work;
  }

@Override
public void setupSidedInventoryIndices(Civic civ)
  {
  //NOOP set in constructor
  }

@Override
public boolean isStackValidForSlot(int i, ItemStack itemstack)
  {
  return i==10 ? itemstack.itemID== ItemLoader.researchBook.itemID: i== 9? false : true;
  }

@Override
public int[] getAccessibleSlotsFromSide(int var1)
  {
  switch(var1)
  {  
  case 0://bottom -- output
  return otherSlotIndices;
  case 1://top -- book slot
  return bookSlot;
  
  case 2://fallthrough for resourceslots
  case 3:
  case 4:
  case 5:
  return resourceSlotIndices;
  }
  return new int[]{};
  }

@Override
public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {
  return i == 9 ? false : i==10 ? itemstack.itemID==ItemLoader.researchBook.itemID : true;
  }

@Override
public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
  return true;
  }

}
