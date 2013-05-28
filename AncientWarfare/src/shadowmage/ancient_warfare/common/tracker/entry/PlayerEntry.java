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
package shadowmage.ancient_warfare.common.tracker.entry;

import java.util.Collection;
import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;

public class PlayerEntry implements INBTTaggable
{

public String playerName = "";
EntityPlayer player = null;
HashSet<IResearchGoal> doneResearch = new HashSet<IResearchGoal>();

public void addCompletedResearch(int num)
  {
  this.doneResearch.add(ResearchGoal.getGoalByID(num));
  }

public boolean hasDoneResearch(IResearchGoal goal)
  {
  return this.doneResearch.contains(goal);
  }

public boolean hasDoneResearch(Collection<IResearchGoal> goals)
  {  
  for(IResearchGoal goal : goals)
    {
    if(goal==null){continue;}
    if(!this.doneResearch.contains(goal))
      {
      return false;
      }
    }
  return true;
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", playerName);
  int[] research = new int[doneResearch.size()];
  int it = 0;
  for(IResearchGoal goal : doneResearch)
    {
    research[it] = goal.getGlobalResearchNum();
    it++;
    }
  tag.setIntArray("res", research);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.playerName = tag.getString("name");
  this.doneResearch.clear();
  int[] research = tag.getIntArray("res");
  for(int i = 0; i< research.length; i++)
    {
    doneResearch.add(ResearchGoal.getGoalByID(research[i]));
    }
  }

}
