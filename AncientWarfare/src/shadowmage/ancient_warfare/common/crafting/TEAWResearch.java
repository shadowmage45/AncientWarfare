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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;

public class TEAWResearch extends TEAWCraftingWorkSite
{
protected List<Integer> researchQueue = new ArrayList<Integer>();

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

public int getQueueLength()
  {
  return this.researchQueue.size();
  }

public List<Integer> getResearchQueue()
  {
  return researchQueue;
  }

public void addResearchToQueue(int num)
  {
  IResearchGoal goal = ResearchGoal.getGoalByID(num);
  List<IResearchGoal> known = workingPlayerEntry.getKnownResearch();
  List<Integer> deps = new ArrayList<Integer>();
  for(IResearchGoal g : known)
    {
    deps.add(g.getGlobalResearchNum());
    }
  deps.addAll(researchQueue);
  if(this.recipe!=null)
    {
    deps.add(this.recipe.getResult().getItemDamage());
    }
  if(goal.isResearchMet(deps) && !this.researchQueue.contains(Integer.valueOf(num)) && !this.workingPlayerEntry.hasDoneResearch(ResearchGoal.getGoalByID(num)))
    {
    this.researchQueue.add(Integer.valueOf(num));
    }
  }

public void removeResearchFromQueue(int index)
  {
  while(this.researchQueue.contains(Integer.valueOf(index)))
    {
    this.researchQueue.remove(Integer.valueOf(index));
    }
  }

@Override
public void setRecipe(ResourceListRecipe recipe)
  {
  if(!this.isStarted && !this.isWorking && recipe!=null)
    {
    this.recipe = recipe;
    this.workProgressMax = ResearchGoal.getGoalByID(recipe.getResult().getItemDamage()).getResearchTime();
    this.workProgress = 0;
    this.isWorking = false;
    this.isStarted = false;
    } 
  }

@Override
protected boolean tryFinishCrafting()
  {
  IResearchGoal goal = ResearchGoal.getGoalByID(recipe.getResult().getItemDamage());
  if(goal==null || workingPlayerName==null)
  {
	  this.recipe = null;
	  this.isStarted = false;
	  this.isWorking = false;
	  this.workProgress = 0;
	  this.workProgressMax = 0;
	  return false;
  }
  PlayerTracker.instance().addResearchToPlayer(worldObj, workingPlayerName, goal.getGlobalResearchNum());
  if(!this.researchQueue.isEmpty())
    {
    this.recipe = null;
    while(!this.researchQueue.isEmpty())
      {
      int num = this.researchQueue.remove(0);
      IResearchGoal g = ResearchGoal.getGoalByID(num);
      if(!this.workingPlayerEntry.hasDoneResearch(g) && this.workingPlayerEntry.hasDoneResearch(g.getDependencies()))
        {
        this.recipe = AWCraftingManager.instance().getRecipeByResult(new ItemStack(ItemLoader.researchNotes,1, g.getGlobalResearchNum()));
        this.workProgressMax = g.getResearchTime();
        break;
        }
      }
    if(this.recipe==null)
      {
      this.isStarted = false;
      }
    }
  else
    {
    this.recipe = null;
    }
  this.workProgress = 0;
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
  int[] array = new int[this.researchQueue.size()];
  for(int i = 0;i < this.researchQueue.size(); i++)
    {
    array[i] = this.researchQueue.get(i);
    }
  tag.setIntArray("queue", array);
  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  {
  this.researchQueue.clear();
  int[] queue = tag.getIntArray("queue");
  for(int i =0;i < queue.length; i++)    
    {
    this.researchQueue.add(queue[i]);
    }
  }

}
