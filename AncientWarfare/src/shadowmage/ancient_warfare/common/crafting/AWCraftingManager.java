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

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;

import net.minecraft.entity.player.EntityPlayer;

public class AWCraftingManager
{


List<ResourceListRecipe> vehicleRecipes = new ArrayList<ResourceListRecipe>();

private static AWCraftingManager INSTANCE = new AWCraftingManager();
public static AWCraftingManager instance()
  {
  return INSTANCE;
  }

public void addVehicleRecipe(ResourceListRecipe recipe)
  {
  Config.logDebug("Adding Vehicle Recipe for: "+recipe.getResult().getDisplayName());
  this.vehicleRecipes.add(recipe);
  }

public List<ResourceListRecipe> getVehicleRecipesFor(EntityPlayer player)
  {
  List<ResourceListRecipe> found = new ArrayList<ResourceListRecipe>();
  for(ResourceListRecipe recipe : this.vehicleRecipes)
    {
    if(recipe.neededResearch==null || PlayerTracker.instance().getEntryFor(player).hasDoneResearch(recipe.neededResearch))
      {
      found.add(recipe);
      }
    }  
  return found;  
  }

}
