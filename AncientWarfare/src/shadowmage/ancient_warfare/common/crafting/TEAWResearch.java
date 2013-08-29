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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class TEAWResearch extends TEAWCraftingWorkSite
{

/**
 * 
 */
public TEAWResearch()
  {
  this.modelID = 0;
  inventory = new AWInventoryBasic(11);
  bookSlot = new int[]{10};
  craftMatrix = new int[]{0,1,2,3,4,5,6,7,8};
  this.workType = CivicWorkType.RESEARCH;
  this.shouldBroadcast = true;  
  }

@Override
public void setRecipe(ResourceListRecipe recipe)
  {
  if(this.recipe==null && recipe!=null)
    {
    this.recipe = recipe;
    this.workProgressMax = ResearchGoal.getGoalByID(recipe.getResult().getItemDamage()).getResearchTime();
    this.workProgress = 0;
    this.isWorking = false;
    } 
  }

@Override
protected boolean tryFinishCrafting()
  {
  IResearchGoal goal = ResearchGoal.getGoalByID(recipe.getResult().getItemDamage());
  PlayerTracker.instance().addResearchToPlayer(worldObj, workingPlayerName, goal.getGlobalResearchNum());
  this.recipe = null;
  this.isWorking = false;
  this.workProgress = 0;
  this.workProgressMax = 0;
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
  
  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  {
  
  }

}
