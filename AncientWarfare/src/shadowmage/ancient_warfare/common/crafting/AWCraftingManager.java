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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.civics.types.ICivicType;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.gates.IGateType;
import shadowmage.ancient_warfare.common.gates.types.Gate;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.npcs.NpcTypeBase;
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.armors.IVehicleArmorType;
import shadowmage.ancient_warfare.common.vehicles.missiles.Ammo;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;
import shadowmage.ancient_warfare.common.vehicles.types.VehicleType;
import shadowmage.ancient_warfare.common.vehicles.upgrades.IVehicleUpgradeType;

import com.google.common.collect.Lists;

public class AWCraftingManager
{

List<ResourceListRecipe> vehicleRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> ammoRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> civicRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> gateRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> upgradeRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> armorRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> npcRecipes = new ArrayList<ResourceListRecipe>();

List<ResourceListRecipe> structureRecipesServer = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> structureRecipesClient = new ArrayList<ResourceListRecipe>();

private Map<RecipeType, List<ResourceListRecipe>> recipesByType = new HashMap<RecipeType, List<ResourceListRecipe>>();

private AWCraftingManager()
  {
  recipesByType.put(RecipeType.VEHICLE, vehicleRecipes);
  recipesByType.put(RecipeType.AMMO, ammoRecipes);
  recipesByType.put(RecipeType.CIVIC, civicRecipes);
  recipesByType.put(RecipeType.GATE, gateRecipes);
  recipesByType.put(RecipeType.UPGRADE, upgradeRecipes);
  recipesByType.put(RecipeType.ARMOR, armorRecipes);
  recipesByType.put(RecipeType.NPC, npcRecipes);
  }

private static AWCraftingManager INSTANCE = new AWCraftingManager();

public static AWCraftingManager instance()
  {
  return INSTANCE;
  }

public ResourceListRecipe getRecipeByResult(ItemStack result)
  {
  for(List<ResourceListRecipe> list : this.recipesByType.values())
    {
    for(ResourceListRecipe valid : list)
      {
      if(InventoryTools.doItemsMatch(valid.result, result))
        {
        return valid;
        }      
      }
    } 
  return null;
  }

public ResourceListRecipe validateRecipe(ResourceListRecipe recipe)
  {
  if(recipe==null){return null;}
  for(List<ResourceListRecipe> list : this.recipesByType.values())
    {
    for(ResourceListRecipe valid : list)
      {
      if(InventoryTools.doItemsMatch(valid.result, recipe.result))
        {
        if(areResourceListsIdentical(valid, recipe))
          {
          return valid;
          }
        }      
      }
    }
  Config.logDebug("returning invalid recipe from recipe validate");
  return null;
  }

public ResourceListRecipe getStructureRecipeFor(String name)
  {
  for(ResourceListRecipe recipe : this.structureRecipesServer)
    {
    if(recipe.getDisplayName().toLowerCase().equals(name.toLowerCase()))
      {
      return recipe;
      }
    }
  return null;
  }

public List<ResourceListRecipe> getRecipesContaining(PlayerEntry entry, String text, EnumSet<RecipeType> types, boolean creative)
  {
  String name;
  List<ResourceListRecipe> recipes = new ArrayList<ResourceListRecipe>();
  for(List<ResourceListRecipe> list : this.recipesByType.values())
    {
    for(ResourceListRecipe recipe : list)
      {
      if(types.isEmpty() || types.contains(recipe.type))
        {
        name = recipe.displayName;
        if(name.toLowerCase().contains(text.toLowerCase()) && ((!Config.DEBUG && creative) || Config.disableResearch || recipe.canBeCraftedBy(entry)))
          {
          recipes.add(recipe);
          }
        }
      }
    }  
  return recipes;  
  }

protected boolean areResourceListsIdentical(ResourceListRecipe a, ResourceListRecipe b)
  {
  boolean found = false;
  for(ItemStackWrapperCrafting stack : a.resources)
    {
    found = false;
    for(ItemStackWrapperCrafting bstack : b.resources)
      {
      if(stack.matches(bstack))
        {
        found = true;
        break;
        }
      }
    if(!found)
      {
      Config.logDebug("returning could not find stack on validate recipe");
      return false;
      }
    }  
  return a.resources.size() == b.resources.size();
  }

/**
 * should be called from AWCore during final INIT stages
 */
public void loadRecipes()
  {
  this.addGateRecipes();//done
  this.addArmorRecipes();//done
  this.addCivicRecipes();//done
  this.addUpgradeRecipes();//done
  this.addAmmoRecipes();
  this.addNpcRecipes();
  this.addVehicleRecipes();
  this.addStructureRecipes();
  }

protected void addCivicRecipes()
  {
  ResourceListRecipe recipe;
  for(ICivicType civic : Civic.civicList)
    {
    if(civic==null){continue;}
    recipe = civic.constructRecipe();
    if(recipe!=null)
      {
      this.civicRecipes.add(recipe);
      Config.logDebug("adding civic recipe: "+recipe);
      }    
    }  
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.warehouseStorage.blockID,1,0), RecipeType.CIVIC);
  recipe.neededResearch.add(ResearchGoalNumbers.civics1);
  recipe.neededResearch.add(ResearchGoalNumbers.logistics3);
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.chest, 1), false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.planks, 4), true, false));
  this.civicRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.warehouseStorage.blockID,1,1), RecipeType.CIVIC);
  recipe.neededResearch.add(ResearchGoalNumbers.civics2);
  recipe.neededResearch.add(ResearchGoalNumbers.logistics4);
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.chest, 2), false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.planks, 4), true, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.paper, 2), true, false));
  this.civicRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.warehouseStorage.blockID,1,2), RecipeType.CIVIC);
  recipe.neededResearch.add(ResearchGoalNumbers.civics3);
  recipe.neededResearch.add(ResearchGoalNumbers.logistics5);
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.chest, 2), false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.ingotIron, 8), true, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.paper, 2), true, false));
  this.civicRecipes.add(recipe);
  }

protected void addGateRecipes()
  {
  ResourceListRecipe recipe;
  for(IGateType gate : Gate.gateTypes)
    {
    if(gate==null){continue;}
    recipe = gate.constructRecipe();
    if(recipe!=null)
      {
      this.gateRecipes.add(recipe);      
      Config.logDebug("adding gate recipe for: "+recipe);
      }
    }
  }

protected void addStructureRecipes()
  {
  ResourceListRecipe recipe;
  for(ProcessedStructure structure : StructureManager.instance().getSurvivalModeStructures())
    {
    if(structure==null){continue;}
    recipe = structure.constructRecipe();
    if(recipe!=null)
      {
      this.structureRecipesServer.add(recipe);
      Config.logDebug("adding recipe :"+recipe);
      }
    }
  }

protected void addNpcRecipes()
  {
  ResourceListRecipe recipe;
  for(NpcTypeBase npc : NpcTypeBase.npcTypes)
    {
    if(npc==null){continue;}    
    for(int i = 0; i < npc.getNumOfLevels(); i++)
      {
      recipe = npc.constructRecipe(i);
      if(recipe!=null)
        {
        this.npcRecipes.add(recipe);
        Config.logDebug("adding npc recipe for : "+recipe);
        }
      }      
    }
  }

protected void addVehicleRecipes()
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
  }

protected void addUpgradeRecipes()
  {
  ResourceListRecipe recipe;
  for(IVehicleUpgradeType upgrade : VehicleUpgradeRegistry.instance().getUpgradeList())
    {
    if(upgrade==null){continue;}
    recipe = upgrade.constructRecipe();
    if(recipe!=null)
      {
      this.upgradeRecipes.add(recipe);
      Config.logDebug("adding upgrade recipe: "+recipe);
      }
    }  
  }

protected void addAmmoRecipes()
  {
  ResourceListRecipe recipe;
  for(IAmmoType ammo : Ammo.ammoTypes)
    {
    if(ammo==null){continue;}
      {
      recipe = ammo.constructRecipe();
      if(recipe!=null)
        {
        ammoRecipes.add(recipe);
        Config.logDebug("adding ammo recipe for: "+recipe);
        }
      }
    }  
  }

protected void addArmorRecipes()
  {
  ResourceListRecipe recipe;
  for(IVehicleArmorType t : ArmorRegistry.instance().getArmorTypes())
    {
    recipe = t.constructRecipe();
    if(t!=null)
      {
      this.armorRecipes.add(recipe);
      Config.logDebug("adding armor recipe: "+recipe);
      }
    }
  }

public void addStructureRecipe(ProcessedStructure struct)
  {
  if(struct==null || !struct.survival){return;}
  Config.logDebug("adding server structure recipe for : "+struct.name);
  ResourceListRecipe recipe = null;
  for(ResourceListRecipe test : this.structureRecipesServer)
    {
    if(test.getDisplayName().equals(struct.name))
      {
      recipe = test;
      break;
      }
    }
  if(recipe!=null)
    {
    this.structureRecipesServer.remove(recipe);
    }
  recipe = struct.constructRecipe();
  if(recipe!=null)
    {
    this.structureRecipesServer.add(recipe);
    }
  }

public void addStructureRecipe(StructureClientInfo info)
  {
  if(info==null || !info.survival){return;}
  Config.logDebug("adding client structure recipe for : "+info.name);
  ResourceListRecipe recipe = null;
  for(ResourceListRecipe test : this.structureRecipesClient)
    {
    if(test.getDisplayName().equals(info.name))
      {
      recipe = test;
      break;
      }
    }
  if(recipe!=null)
    {
    this.structureRecipesClient.remove(recipe);
    }
  recipe = info.constructRecipe();
  if(recipe!=null)
    {
    this.structureRecipesClient.add(recipe);
    }
  }

public void resetClientData()
  {
  this.structureRecipesClient.clear();
  }

public void addVehicleRecipe(ResourceListRecipe recipe)
  {
  Config.logDebug("Adding Vehicle Recipe for: "+recipe);
  this.vehicleRecipes.add(recipe);
  }

public List<ResourceListRecipe> getRecipesOfTypesFor(PlayerEntry entry, boolean creative, RecipeType...recipeTypes)
  {
  List<ResourceListRecipe> found = new ArrayList<ResourceListRecipe>();
  for(RecipeType type : recipeTypes)
    {
    List<ResourceListRecipe> recipes = this.recipesByType.get(type);
    for(ResourceListRecipe recipe : recipes)
      {
      if(recipe.canBeCraftedBy(entry) || (!Config.DEBUG && creative))
        {
        found.add(recipe);
        }
      }
    }
  return found; 
  }

public List<ResourceListRecipe> getStructureRecipesClient()
  {
  return Lists.newArrayList(this.structureRecipesClient);
  }

public List<ResourceListRecipe> getStructureRecipesServer()
  {
  return Lists.newArrayList(this.structureRecipesServer);
  }

public List<ResourceListRecipe> getRecipesDependantOn(IResearchGoal goal)
  {
  List<ResourceListRecipe> recipes = new ArrayList<ResourceListRecipe>();
  for(ResourceListRecipe recipe : this.vehicleRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.ammoRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.civicRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.gateRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.upgradeRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.armorRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  return recipes;
  }

}
