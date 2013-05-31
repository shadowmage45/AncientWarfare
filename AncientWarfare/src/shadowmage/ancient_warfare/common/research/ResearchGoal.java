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

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.research.ammo.ResearchBallistics;
import shadowmage.ancient_warfare.common.research.ammo.ResearchExplosives;
import shadowmage.ancient_warfare.common.research.ammo.ResearchFlammables;
import shadowmage.ancient_warfare.common.research.ammo.ResearchRockets;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchCounterweights;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchGunpowderVehicles;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchMaterialLevel;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchMobility;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTorsion;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTurrets;
import shadowmage.ancient_warfare.common.vehicles.materials.VehicleMaterial;

public class ResearchGoal implements IResearchGoal
{
public static ResearchGoal[] researchGoals = new ResearchGoal[256];

public static IResearchGoal vehicleMobility1 = new ResearchMobility(0,0);
public static IResearchGoal vehicleMobility2 = new ResearchMobility(1,1).addDependencies(vehicleMobility1);
public static IResearchGoal vehicleMobility3 = new ResearchMobility(2,2).addDependencies(vehicleMobility2);
public static IResearchGoal vehicleMobility4 = new ResearchMobility(3,3).addDependencies(vehicleMobility3);
public static IResearchGoal vehicleMobility5 = new ResearchMobility(4,4).addDependencies(vehicleMobility4);
public static IResearchGoal vehicleTurrets1 = new ResearchTurrets(5, 0);
public static IResearchGoal vehicleTurrets2 = new ResearchTurrets(6, 1).addDependencies(vehicleTurrets1);
public static IResearchGoal vehicleTurrets3 = new ResearchTurrets(7, 2).addDependencies(vehicleTurrets2);
public static IResearchGoal vehicleTurrets4 = new ResearchTurrets(8, 3).addDependencies(vehicleTurrets3);
public static IResearchGoal vehicleTurrets5 = new ResearchTurrets(9, 4).addDependencies(vehicleTurrets4);
public static IResearchGoal vehicleTorsion1 = new ResearchTorsion(10, 0);
public static IResearchGoal vehicleTorsion2 = new ResearchTorsion(11, 1).addDependencies(vehicleTorsion1);
public static IResearchGoal vehicleTorsion3 = new ResearchTorsion(12, 2).addDependencies(vehicleTorsion2);
public static IResearchGoal vehicleTorsion4 = new ResearchTorsion(13, 3).addDependencies(vehicleTorsion3);
public static IResearchGoal vehicleTorsion5 = new ResearchTorsion(14, 4).addDependencies(vehicleTorsion4);
public static IResearchGoal vehicleCounterweights1 = new ResearchCounterweights(15,0);
public static IResearchGoal vehicleCounterweights2 = new ResearchCounterweights(16,1).addDependencies(vehicleCounterweights1);
public static IResearchGoal vehicleCounterweights3 = new ResearchCounterweights(17,2).addDependencies(vehicleCounterweights2);
public static IResearchGoal vehicleCounterweights4 = new ResearchCounterweights(18,3).addDependencies(vehicleCounterweights3);
public static IResearchGoal vehicleCounterweights5 = new ResearchCounterweights(19,4).addDependencies(vehicleCounterweights4);
public static IResearchGoal vehicleGunpowderWeapons1 = new ResearchGunpowderVehicles(20, 0).addDependencies(50);//50 == explosives1
public static IResearchGoal vehicleGunpowderWeapons2 = new ResearchGunpowderVehicles(21, 1).addDependencies(vehicleGunpowderWeapons1);
public static IResearchGoal vehicleGunpowderWeapons3 = new ResearchGunpowderVehicles(22, 2).addDependencies(vehicleGunpowderWeapons2);
public static IResearchGoal vehicleGunpowderWeapons4 = new ResearchGunpowderVehicles(23, 3).addDependencies(vehicleGunpowderWeapons3);
public static IResearchGoal vehicleGunpowderWeapons5 = new ResearchGunpowderVehicles(24, 4).addDependencies(vehicleGunpowderWeapons4);

public static IResearchGoal ammoExplosives1 = new ResearchExplosives(50, 0).addDependencies(vehicleGunpowderWeapons1).addDependencies(57);//57==flammables2
public static IResearchGoal ammoExplosives2 = new ResearchExplosives(51, 1).addDependencies(ammoExplosives1);
public static IResearchGoal ammoExplosives3 = new ResearchExplosives(52, 2).addDependencies(ammoExplosives2);
public static IResearchGoal ammoRockets1 = new ResearchRockets(53, 0).addDependencies(ammoExplosives2);
public static IResearchGoal ammoRockets2 = new ResearchRockets(54, 1).addDependencies(ammoRockets1);
public static IResearchGoal ammoRockets3 = new ResearchRockets(55, 2).addDependencies(ammoRockets2);
public static IResearchGoal ammoFlammables1 = new ResearchFlammables(56, 0).addDependencies(59);//59==ballistics1
public static IResearchGoal ammoFlammables2 = new ResearchFlammables(57, 0).addDependencies(ammoFlammables1);
public static IResearchGoal ammoFlammables3 = new ResearchFlammables(58, 0).addDependencies(ammoFlammables2);
public static IResearchGoal ammoBallistics1 = new ResearchBallistics(59, 0);
public static IResearchGoal ammoBallistics2 = new ResearchBallistics(60, 0).addDependencies(ammoBallistics1);
public static IResearchGoal ammoBallistics3 = new ResearchBallistics(61, 0).addDependencies(ammoBallistics2);

/**
 * upgrade/armor research? 
 */

/**
 * civic research?
 */

/**
 * efficiency research? (decrease materials costs for crafting vehicles/structures?)
 */

public static IResearchGoal materialWood1 = new ResearchMaterialLevel(200, 0, VehicleMaterial.materialWood);
public static IResearchGoal materialWood2 = new ResearchMaterialLevel(201, 1, VehicleMaterial.materialWood).addDependencies(materialWood1);
public static IResearchGoal materialWood3 = new ResearchMaterialLevel(202, 2, VehicleMaterial.materialWood).addDependencies(materialWood2);
public static IResearchGoal materialWood4 = new ResearchMaterialLevel(203, 3, VehicleMaterial.materialWood).addDependencies(materialWood3);
public static IResearchGoal materialWood5 = new ResearchMaterialLevel(204, 4, VehicleMaterial.materialWood).addDependencies(materialWood4);

public static IResearchGoal materialIron1 = new ResearchMaterialLevel(205, 0, VehicleMaterial.materialIron);
public static IResearchGoal materialIron2 = new ResearchMaterialLevel(206, 1, VehicleMaterial.materialIron).addDependencies(materialIron1);
public static IResearchGoal materialIron3 = new ResearchMaterialLevel(207, 2, VehicleMaterial.materialIron).addDependencies(materialIron2);
public static IResearchGoal materialIron4 = new ResearchMaterialLevel(208, 3, VehicleMaterial.materialIron).addDependencies(materialIron3);
public static IResearchGoal materialIron5 = new ResearchMaterialLevel(209, 4, VehicleMaterial.materialIron).addDependencies(materialIron4);

protected int researchGoalNumber = 0;
protected String displayName = "";
protected String displayTooltip = "";
protected HashSet<Integer> dependencies = new HashSet<Integer>();
protected HashSet<IResearchGoal> dependencyCache = new HashSet<IResearchGoal>();
protected List<String> detailedDescription = new ArrayList<String>();
protected List<ItemStack> resources = new ArrayList<ItemStack>();
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

public ResearchGoal addResource(ItemStack stack)
  {
  this.resources.add(stack);
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
  for(Integer i : this.dependencies)
    {
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
public List<ItemStack> getResearchResources() 
  {
  return resources;
  }

}
