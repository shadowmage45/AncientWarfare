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
package shadowmage.ancient_warfare.common.research;

import java.util.HashSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public interface IResearchGoal
{


public abstract int getGlobalResearchNum();
public abstract String getDisplayName();
public abstract String getLocalizedName();
public abstract String getDisplayTooltip();
public abstract String getLocalizedTooltip();
public abstract HashSet<IResearchGoal> getDependencies();
public abstract List<String> getDetailedDescription();
public abstract List<String> getLocalizedDescription();
public abstract IResearchGoal getGoalByNumber(int num);
public abstract IResearchGoal addDependencies(IResearchGoal... deps);
public abstract IResearchGoal addDependencies(Integer... deps);
public abstract int getResearchTime();
public abstract List<ItemStackWrapperCrafting> getResearchResources();
public abstract IResearchGoal addResource(ItemStack stack, boolean dmg, boolean tag);
public abstract ResourceListRecipe constructRecipe();

}
