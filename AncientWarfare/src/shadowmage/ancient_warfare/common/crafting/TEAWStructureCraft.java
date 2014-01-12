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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public class TEAWStructureCraft extends TEAWCrafting
{
	
public boolean isStructureStarted = false;
short compileTime = 0;
short compileTimeMax = 5;

/**
 * 
 */
public TEAWStructureCraft()
  {
  this.modelID = 2;
  craftMatrix = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
  resultSlot = new int[]{18};
  inventory = new AWInventoryBasic(19);
  }

@Override
public void onBlockClicked(EntityPlayer player)
  {
  GUIHandler.instance().openGUI(GUIHandler.CIVIL_ENGINEERING, player, worldObj, xCoord, yCoord, zCoord);
  }

@Override
public void updateEntity()
  {  
  if(this.isStructureStarted)
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
        this.compileTime = 0;
        if(this.isRecipeFinished())
          {        
          this.produceItem();
          this.setFinished();        
          }
        }
      }
    if(this.compileTime<this.compileTimeMax)
      {
      this.compileTime++;
      this.workProgress++;
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
      InventoryTools.tryRemoveItems(inventory, item.getFilter(), 1, 0, 17);
      item.setRemainingNeeded(item.getRemainingNeeded()-1);
      return true;
      }
    }
  return false;
  }

protected boolean isRecipeFinished()
  {
  for(ItemStackWrapperCrafting item : recipe.resources)
    {
    if(item.getRemainingNeeded()>0)
      {
      return false;
      }
    }
  return true;
  }

protected boolean canSetFinished()
  {
  return recipe!=null && InventoryTools.canHoldItem(inventory, recipe.getResult(), recipe.getResult().stackSize, 18, 18);
  }

protected void setFinished()
  {
  this.isStructureStarted = false;
  this.compileTime = 0;
  this.recipe = null;
  this.workProgress = 0;
  this.workProgressMax = 0;
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
  if(this.isStructureStarted || this.recipe!=null || recipe == null){return;}
  this.recipe = recipe.copy();
  this.isStructureStarted = true;
  this.workProgressMax = this.calcTotalTime();
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
  tag.setBoolean("structStarted", this.isStructureStarted);
  tag.setShort("cTime", this.compileTime);
  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  { 
  this.isStructureStarted = tag.getBoolean("structStarted");
  this.compileTime = tag.getShort("cTime");
  }

/**
 * 
 */
public void stopWorkAndClearRecipe()
  {
  this.setFinished();
  }


}
