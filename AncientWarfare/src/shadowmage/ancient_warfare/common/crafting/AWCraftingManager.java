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

import net.minecraft.entity.player.EntityPlayer;
import shadowmage.ancient_warfare.common.AWStructureModule;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.civics.types.ICivicType;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.gates.IGateType;
import shadowmage.ancient_warfare.common.gates.types.Gate;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.npcs.NpcTypeBase;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.structures.data.AWStructure;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.missiles.Ammo;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;
import shadowmage.ancient_warfare.common.vehicles.types.VehicleType;

public class AWCraftingManager
{


List<ResourceListRecipe> vehicleRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> ammoRecipes = new ArrayList<ResourceListRecipe>();

private AWCraftingManager(){}
private static AWCraftingManager INSTANCE = new AWCraftingManager();
public static AWCraftingManager instance()
  {
  return INSTANCE;
  }

/**
 * should be called from AWCore during final INIT stages
 */
public void loadRecipes()
  {
  ResourceListRecipe recipe;
  for(IVehicleType vehicle : VehicleType.vehicleTypes)
    {
    if(vehicle==null){continue;}
    for(int i = 0; i < vehicle.getMaterialType().getNumOfLevels(); i++)
      {
      recipe = vehicle.constructRecipe(i);
      if(recipe!=null)
        {
        this.addVehicleRecipe(vehicle.constructRecipe(i));        
        }
      }
    } 
  
  for(IAmmoType ammo : Ammo.ammoTypes)
    {
    if(ammo==null){continue;}
      {
      recipe = ammo.constructRecipe();
      if(recipe!=null)
        {
        recipe.setDisplayName(ammo.getDisplayName());
        ammoRecipes.add(recipe);        
        }
      }
    }
  
  for(NpcTypeBase npc : NpcTypeBase.npcTypes)
    {
    if(npc==null){continue;}      
    //TODO add npc recipes      
    }
  
  for(ICivicType civic : Civic.civicList)
    {
    if(civic==null){continue;}
    //TODO add civic recipes
    }
  
  for(IGateType gate : Gate.gateTypes)
    {
    if(gate==null){continue;}
    //TODO add gate recipes
    }
  
  for(AWStructure structure : StructureManager.instance().getSurvivalModeStructures())
    {
    if(structure==null){continue;}
    //TODO add structure recipes
    }
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
    if(recipe.neededResearch==null || PlayerTracker.instance().getEntryFor(player).hasDoneResearchByNumbers(recipe.neededResearch))
      {
      found.add(recipe);
      }
    }  
  return found;  
  }

public List<ResourceListRecipe> getRecipesDependantOn(IResearchGoal goal)
  {
  List<ResourceListRecipe> recipes = new ArrayList<ResourceListRecipe>();
  for(ResourceListRecipe recipe : this.vehicleRecipes)
    {
    if(recipe.neededResearch.contains(goal))
      {
      recipes.add(recipe);
      }
    }
  return recipes;
  }

}
