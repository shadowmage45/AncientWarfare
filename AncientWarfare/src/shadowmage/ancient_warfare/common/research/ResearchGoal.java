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
package shadowmage.ancient_warfare.common.research;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.research.ammo.ResearchBallistics;
import shadowmage.ancient_warfare.common.research.ammo.ResearchExplosives;
import shadowmage.ancient_warfare.common.research.ammo.ResearchFlammables;
import shadowmage.ancient_warfare.common.research.ammo.ResearchRockets;
import shadowmage.ancient_warfare.common.research.civic.ResearchCivics;
import shadowmage.ancient_warfare.common.research.civic.ResearchLogistics;
import shadowmage.ancient_warfare.common.research.general.ResearchEfficiencyIron;
import shadowmage.ancient_warfare.common.research.general.ResearchEfficiencyWood;
import shadowmage.ancient_warfare.common.research.general.ResearchMechanics;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchCounterweights;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchGunpowderVehicles;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchMaterialLevel;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchMobility;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTorsion;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTurrets;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.vehicles.materials.VehicleMaterial;

public class ResearchGoal implements IResearchGoal
{
public static ResearchGoal[] researchGoals = new ResearchGoal[256];

public static IResearchGoal vehicleMobility1 = new ResearchMobility(ResearchGoalNumbers.mobility1,0).addDependencies(ResearchGoalNumbers.mechanics1);
public static IResearchGoal vehicleMobility2 = new ResearchMobility(ResearchGoalNumbers.mobility2,1).addDependencies(vehicleMobility1);
public static IResearchGoal vehicleMobility3 = new ResearchMobility(ResearchGoalNumbers.mobility3,2).addDependencies(vehicleMobility2);
public static IResearchGoal vehicleMobility4 = new ResearchMobility(ResearchGoalNumbers.mobility4,3).addDependencies(vehicleMobility3);
public static IResearchGoal vehicleMobility5 = new ResearchMobility(ResearchGoalNumbers.mobility5,4).addDependencies(vehicleMobility4);
public static IResearchGoal vehicleTurrets1 = new ResearchTurrets(ResearchGoalNumbers.turrets1, 0).addDependencies(ResearchGoalNumbers.mechanics1);
public static IResearchGoal vehicleTurrets2 = new ResearchTurrets(ResearchGoalNumbers.turrets2, 1).addDependencies(vehicleTurrets1);
public static IResearchGoal vehicleTurrets3 = new ResearchTurrets(ResearchGoalNumbers.turrets3, 2).addDependencies(vehicleTurrets2);
public static IResearchGoal vehicleTurrets4 = new ResearchTurrets(ResearchGoalNumbers.turrets4, 3).addDependencies(vehicleTurrets3);
public static IResearchGoal vehicleTurrets5 = new ResearchTurrets(ResearchGoalNumbers.turrets5, 4).addDependencies(vehicleTurrets4);
public static IResearchGoal vehicleTorsion1 = new ResearchTorsion(ResearchGoalNumbers.torsion1, 0);
public static IResearchGoal vehicleTorsion2 = new ResearchTorsion(ResearchGoalNumbers.torsion2, 1).addDependencies(vehicleTorsion1);
public static IResearchGoal vehicleTorsion3 = new ResearchTorsion(ResearchGoalNumbers.torsion3, 2).addDependencies(vehicleTorsion2);
public static IResearchGoal vehicleTorsion4 = new ResearchTorsion(ResearchGoalNumbers.torsion4, 3).addDependencies(vehicleTorsion3);
public static IResearchGoal vehicleTorsion5 = new ResearchTorsion(ResearchGoalNumbers.torsion5, 4).addDependencies(vehicleTorsion4);
public static IResearchGoal vehicleCounterweights1 = new ResearchCounterweights(ResearchGoalNumbers.counterweights1,0).addDependencies(ResearchGoalNumbers.mechanics1);
public static IResearchGoal vehicleCounterweights2 = new ResearchCounterweights(ResearchGoalNumbers.counterweights2,1).addDependencies(vehicleCounterweights1);
public static IResearchGoal vehicleCounterweights3 = new ResearchCounterweights(ResearchGoalNumbers.counterweights3,2).addDependencies(vehicleCounterweights2);
public static IResearchGoal vehicleCounterweights4 = new ResearchCounterweights(ResearchGoalNumbers.counterweights4,3).addDependencies(vehicleCounterweights3);
public static IResearchGoal vehicleCounterweights5 = new ResearchCounterweights(ResearchGoalNumbers.counterweights5,4).addDependencies(vehicleCounterweights4);
public static IResearchGoal vehicleGunpowderWeapons1 = new ResearchGunpowderVehicles(ResearchGoalNumbers.gunpowder1, 0).addDependencies(ResearchGoalNumbers.explosives1);
public static IResearchGoal vehicleGunpowderWeapons2 = new ResearchGunpowderVehicles(ResearchGoalNumbers.gunpowder2, 1).addDependencies(vehicleGunpowderWeapons1);
public static IResearchGoal vehicleGunpowderWeapons3 = new ResearchGunpowderVehicles(ResearchGoalNumbers.gunpowder3, 2).addDependencies(vehicleGunpowderWeapons2);
public static IResearchGoal vehicleGunpowderWeapons4 = new ResearchGunpowderVehicles(ResearchGoalNumbers.gunpowder4, 3).addDependencies(vehicleGunpowderWeapons3);
public static IResearchGoal vehicleGunpowderWeapons5 = new ResearchGunpowderVehicles(ResearchGoalNumbers.gunpowder5, 4).addDependencies(vehicleGunpowderWeapons4);

public static IResearchGoal ammoExplosives1 = new ResearchExplosives(ResearchGoalNumbers.explosives1, 0).addDependencies(vehicleGunpowderWeapons1).addDependencies(ResearchGoalNumbers.flammables2);
public static IResearchGoal ammoExplosives2 = new ResearchExplosives(ResearchGoalNumbers.explosives2, 1).addDependencies(ammoExplosives1);
public static IResearchGoal ammoExplosives3 = new ResearchExplosives(ResearchGoalNumbers.explosives3, 2).addDependencies(ammoExplosives2);
public static IResearchGoal ammoRockets1 = new ResearchRockets(ResearchGoalNumbers.rockets1, 0).addDependencies(ammoExplosives2);
public static IResearchGoal ammoRockets2 = new ResearchRockets(ResearchGoalNumbers.rockets2, 1).addDependencies(ammoRockets1);
public static IResearchGoal ammoRockets3 = new ResearchRockets(ResearchGoalNumbers.rockets3, 2).addDependencies(ammoRockets2);
public static IResearchGoal ammoFlammables1 = new ResearchFlammables(ResearchGoalNumbers.flammables1, 0).addDependencies(ResearchGoalNumbers.ballistics1);
public static IResearchGoal ammoFlammables2 = new ResearchFlammables(ResearchGoalNumbers.flammables2, 1).addDependencies(ammoFlammables1);
public static IResearchGoal ammoFlammables3 = new ResearchFlammables(ResearchGoalNumbers.flammables3, 2).addDependencies(ammoFlammables2);
public static IResearchGoal ammoBallistics1 = new ResearchBallistics(ResearchGoalNumbers.ballistics1, 0);
public static IResearchGoal ammoBallistics2 = new ResearchBallistics(ResearchGoalNumbers.ballistics2, 1).addDependencies(ammoBallistics1);
public static IResearchGoal ammoBallistics3 = new ResearchBallistics(ResearchGoalNumbers.ballistics3, 2).addDependencies(ammoBallistics2);

/**
 * upgrade/armor research? 
 */
public static IResearchGoal upgradeMechanics1 = new ResearchMechanics(ResearchGoalNumbers.mechanics1, 0);
public static IResearchGoal upgradeMechanics2 = new ResearchMechanics(ResearchGoalNumbers.mechanics2, 1).addDependencies(upgradeMechanics1);
public static IResearchGoal upgradeMechanics3 = new ResearchMechanics(ResearchGoalNumbers.mechanics3, 2).addDependencies(upgradeMechanics2);
public static IResearchGoal upgradeMechanics4 = new ResearchMechanics(ResearchGoalNumbers.mechanics4, 3).addDependencies(upgradeMechanics3);
public static IResearchGoal upgradeMechanics5 = new ResearchMechanics(ResearchGoalNumbers.mechanics5, 4).addDependencies(upgradeMechanics4);

/**
 * civic research
 */
public static IResearchGoal civicEngineering1 = new ResearchCivics(ResearchGoalNumbers.civics1, 0);
public static IResearchGoal civicEngineering2 = new ResearchCivics(ResearchGoalNumbers.civics2, 1).addDependencies(civicEngineering1);
public static IResearchGoal civicEngineering3 = new ResearchCivics(ResearchGoalNumbers.civics3, 2).addDependencies(civicEngineering2);
public static IResearchGoal civicEngineering4 = new ResearchCivics(ResearchGoalNumbers.civics4, 3).addDependencies(civicEngineering3);
public static IResearchGoal civicEngineering5 = new ResearchCivics(ResearchGoalNumbers.civics5, 4).addDependencies(civicEngineering4);
public static IResearchGoal logistics1 = new ResearchLogistics(ResearchGoalNumbers.logistics1,0).addDependencies(civicEngineering1);
public static IResearchGoal logistics2 = new ResearchLogistics(ResearchGoalNumbers.logistics2,1).addDependencies(logistics1);
public static IResearchGoal logistics3 = new ResearchLogistics(ResearchGoalNumbers.logistics3,2).addDependencies(logistics2);
public static IResearchGoal logistics4 = new ResearchLogistics(ResearchGoalNumbers.logistics4,3).addDependencies(logistics3);
public static IResearchGoal logistics5 = new ResearchLogistics(ResearchGoalNumbers.logistics5,4).addDependencies(logistics4);

/**
 * efficiency research? (decrease materials costs for crafting vehicles/structures/ammo?)
 */
public static IResearchGoal efficiencyWood1 = new ResearchEfficiencyWood(ResearchGoalNumbers.efficiencyWood1, 0);
public static IResearchGoal efficiencyWood2 = new ResearchEfficiencyWood(ResearchGoalNumbers.efficiencyWood2, 1).addDependencies(efficiencyWood1);
public static IResearchGoal efficiencyWood3 = new ResearchEfficiencyWood(ResearchGoalNumbers.efficiencyWood3, 2).addDependencies(efficiencyWood2);
public static IResearchGoal efficiencyIron1 = new ResearchEfficiencyIron(ResearchGoalNumbers.efficiencyIron1, 0);
public static IResearchGoal efficiencyIron2 = new ResearchEfficiencyIron(ResearchGoalNumbers.efficiencyIron2, 1).addDependencies(efficiencyIron1);
public static IResearchGoal efficiencyIron3 = new ResearchEfficiencyIron(ResearchGoalNumbers.efficiencyIron3, 2).addDependencies(efficiencyIron2);

/**
 * vehicle materials
 */
public static IResearchGoal materialWood1 = new ResearchMaterialLevel(200, 0, VehicleMaterial.materialWood).addResource(new ItemStack(Block.planks,10), true, false);
public static IResearchGoal materialWood2 = new ResearchMaterialLevel(201, 1, VehicleMaterial.materialWood).addDependencies(materialWood1).addResource(new ItemStack(Block.planks,20), true, false);
public static IResearchGoal materialWood3 = new ResearchMaterialLevel(202, 2, VehicleMaterial.materialWood).addDependencies(materialWood2).addResource(new ItemStack(Block.planks,30), true, false);
public static IResearchGoal materialWood4 = new ResearchMaterialLevel(203, 3, VehicleMaterial.materialWood).addDependencies(materialWood3).addResource(new ItemStack(Block.planks,40), true, false);
public static IResearchGoal materialWood5 = new ResearchMaterialLevel(204, 4, VehicleMaterial.materialWood).addDependencies(materialWood4).addResource(new ItemStack(Block.planks,50), true, false);

public static IResearchGoal materialIron1 = new ResearchMaterialLevel(205, 0, VehicleMaterial.materialIron).addResource(new ItemStack(Item.ingotIron, 10), false, true);
public static IResearchGoal materialIron2 = new ResearchMaterialLevel(206, 1, VehicleMaterial.materialIron).addDependencies(materialIron1).addResource(new ItemStack(Item.ingotIron, 20), false, true);
public static IResearchGoal materialIron3 = new ResearchMaterialLevel(207, 2, VehicleMaterial.materialIron).addDependencies(materialIron2).addResource(new ItemStack(Item.ingotIron, 30), false, true);
public static IResearchGoal materialIron4 = new ResearchMaterialLevel(208, 3, VehicleMaterial.materialIron).addDependencies(materialIron3).addResource(new ItemStack(Item.ingotIron, 40), false, true);
public static IResearchGoal materialIron5 = new ResearchMaterialLevel(209, 4, VehicleMaterial.materialIron).addDependencies(materialIron4).addResource(new ItemStack(Item.ingotIron, 50), false, true);

/**
 * npcs/command?
 */



protected int researchGoalNumber = 0;
protected String displayName = "";
protected String displayTooltip = "";
protected HashSet<Integer> dependencies = new HashSet<Integer>();
protected HashSet<IResearchGoal> dependencyCache = new HashSet<IResearchGoal>();
protected List<String> detailedDescription = new ArrayList<String>();
protected List<ItemStackWrapperCrafting> resources = new ArrayList<ItemStackWrapperCrafting>();
protected int researchTime = 100;

public ResearchGoal(int num)
  {
  this.researchGoalNumber = num;
  if(num<0 || num >= researchGoals.length)
    {
    Config.logError("Research goal number out of range: "+num);
    return;
    }
  if(researchGoals[num]!=null)
    {
    Config.logError("Research goal being overwritten");
    }  
  this.researchGoals[num] = this;
  }

@Override
public ResearchGoal addResource(ItemStack stack, boolean dmg, boolean tag)
  {
  this.resources.add(new ItemStackWrapperCrafting(stack, dmg, tag));
  return this;
  }

public ResearchGoal setDisplayName(String name)
  {
  this.displayName = name;
  return this;
  }

public ResearchGoal setTooltip(String tip)
  {
  this.displayTooltip = tip;
  return this;
  }

@Override
public int getGlobalResearchNum()
  {
  return researchGoalNumber;
  }

@Override
public String getDisplayName()
  {
  return displayName;
  }

@Override
public String getDisplayTooltip()
  {  
  return displayTooltip;
  }

@Override
public HashSet<IResearchGoal> getDependencies()
  {
  if(this.dependencyCache.size() == this.dependencies.size())
    {
    return this.dependencyCache;
    }
  this.dependencyCache.clear();
  Config.logDebug("setting up dependency cache for goal: "+this.displayName );
  for(Integer i : this.dependencies)
    {
    Config.logDebug("adding dependency : "+researchGoals[i].displayName);
    this.dependencyCache.add(researchGoals[i]);
    }
  return dependencyCache;
  }

@Override
public List<String> getDetailedDescription()
  {
  return this.detailedDescription;
  }

@Override
public IResearchGoal getGoalByNumber(int num)
  {
  if(num>=0 && num < this.researchGoals.length)
    {
    return this.researchGoals[num];
    }
  return null;
  }

@Override
public IResearchGoal addDependencies(IResearchGoal... deps)
  {
  for(int i = 0; i < deps.length; i++)
    {
    if(deps[i]!=null)
      {
      this.dependencies.add(deps[i].getGlobalResearchNum());
      }
    }
  return this;
  }

@Override
public IResearchGoal addDependencies(Integer... deps)
  {
  for(int i = 0; i < deps.length; i++)
    {
    if(deps[i]!=null)
      {
      this.dependencies.add(deps[i]);
      }
    }
  return this;
  }

public static IResearchGoal getGoalByID(int id)
  {
  if(id>=0 && id< researchGoals.length)
    {
    return researchGoals[id];    
    }
  return null;
  }

public static IResearchGoal[] getDefaultKnownResearch()
  {
  return new IResearchGoal[]{vehicleTorsion1, materialWood1};
  }

public static Collection<IResearchGoal> getUnlocks(IResearchGoal goal)
  {
  HashSet<IResearchGoal> goals = new HashSet<IResearchGoal>();
  for(IResearchGoal g : researchGoals)
    {
    if(g==null){continue;}
    if(g.getDependencies().contains(goal))
      {
      goals.add(g);
      }    
    }
  return goals;
  }

@Override
public int getResearchTime() 
  {
  return researchTime;
  }

@Override
public List<ItemStackWrapperCrafting> getResearchResources() 
  {
  return resources;
  }

@Override
public ResourceListRecipe constructRecipe()
  {
  ResourceListRecipe recipe = new ResourceListRecipe(new ItemStack(ItemLoader.researchNotes,1,this.getGlobalResearchNum()), RecipeType.NONE);
  recipe.setDisplayName(getDisplayName());
  recipe.addNeededResearch(dependencies);
  recipe.addResources(getResearchResources());
  return recipe;
  }

}
