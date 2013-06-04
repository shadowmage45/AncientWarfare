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
package shadowmage.ancient_warfare.common.registry.entry;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;

public class EncyclopediaEntry
{

String displayName;
String tooltip;
ItemStack item;
ResourceListRecipe recipe;
Set<Integer> researchGoalNumbers = new HashSet<Integer>();
Set<IResearchGoal> researchGoalCache  = new HashSet<IResearchGoal>();
List<String> detailedDescription;

public EncyclopediaEntry(String displayName, String tooltip)
  {
  this.displayName = displayName;
  this.tooltip = tooltip;
  }

public EncyclopediaEntry setItem(ItemStack item)
  {
  this.item = item;
  return this;
  }

public EncyclopediaEntry setRecipe(ResourceListRecipe recipe)
  {
  this.recipe = recipe;
  return this;
  }

public EncyclopediaEntry addNeededResearch(Integer... nums)
  {
  for(Integer i : nums)
    {
    this.researchGoalNumbers.add(i);
    }
  return this;
  }

public EncyclopediaEntry addNeededResearch(Collection<Integer> nums)
  {
  this.researchGoalNumbers.addAll(nums);
  return this;
  }

public EncyclopediaEntry setDetailedDescription(List<String> lines)
  {
  this.detailedDescription.clear();
  this.detailedDescription.addAll(lines);
  return this;
  }

public EncyclopediaEntry setDetailedDescription(String line)  
  {
  this.detailedDescription.clear();
  this.detailedDescription.add(line);
  return this;
  }

public List<String> getDetailedDescription(int width)
  {
  return this.detailedDescription;  
  }

public ItemStack getDisplayStack()
  {
  return this.item;  
  }

public ResourceListRecipe getRecipe()
  {
  return this.recipe;
  }

public Set<Integer> getResearchGoalNumbers()
  {
  return this.researchGoalNumbers;
  }

public Set<IResearchGoal> getResearchGoals()
  {
  if(this.researchGoalCache.size()!=this.researchGoalNumbers.size())
    {
    this.researchGoalCache.clear();
    for(Integer i : this.researchGoalNumbers)
      {
      this.researchGoalCache.add(ResearchGoal.getGoalByID(i));
      }
    }
  return this.researchGoalCache;
  }

}
