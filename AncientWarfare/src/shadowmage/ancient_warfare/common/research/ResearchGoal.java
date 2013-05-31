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
import java.util.HashSet;
import java.util.List;

import net.minecraft.item.ItemStack;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchCounterweights;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchGunpowderVehicles;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchMaterialLevel;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchMobility;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTorsion;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTurrets;

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
public static IResearchGoal vehicleGunpowderWeapons1 = new ResearchGunpowderVehicles(20, 0);
public static IResearchGoal vehicleGunpowderWeapons2 = new ResearchGunpowderVehicles(21, 1).addDependencies(vehicleGunpowderWeapons1);
public static IResearchGoal vehicleGunpowderWeapons3 = new ResearchGunpowderVehicles(22, 2).addDependencies(vehicleGunpowderWeapons2);
public static IResearchGoal vehicleGunpowderWeapons4 = new ResearchGunpowderVehicles(23, 3).addDependencies(vehicleGunpowderWeapons3);
public static IResearchGoal vehicleGunpowderWeapons5 = new ResearchGunpowderVehicles(24, 4).addDependencies(vehicleGunpowderWeapons4);

/**
 * upgrade/armor research?
 * 
 */

/**
 * ammo research?
 */

/**
 * civic research?
 */

/**
 * efficiency research? (decrease materials costs for crafting vehicles/structures?)
 */

public static IResearchGoal materialWood1 = new ResearchMaterialLevel(200, 0, "Rough Wood");
public static IResearchGoal materialWood2 = new ResearchMaterialLevel(201, 1, "Treated Wood").addDependencies(materialWood1);
public static IResearchGoal materialWood3 = new ResearchMaterialLevel(202, 2, "Ironshod Wood").addDependencies(materialWood2);
public static IResearchGoal materialWood4 = new ResearchMaterialLevel(203, 3, "Iron Core Wood").addDependencies(materialWood3);
public static IResearchGoal materialWood5 = new ResearchMaterialLevel(204, 4, "Iron Substitute").addDependencies(materialWood4);

public static IResearchGoal materialIron1 = new ResearchMaterialLevel(205, 0, "Rough Iron");
public static IResearchGoal materialIron2 = new ResearchMaterialLevel(206, 1, "Fine Iron").addDependencies(materialIron1);
public static IResearchGoal materialIron3 = new ResearchMaterialLevel(207, 2, "Tempered Iron").addDependencies(materialIron2);
public static IResearchGoal materialIron4 = new ResearchMaterialLevel(208, 3, "Minor Alloy").addDependencies(materialIron3);
public static IResearchGoal materialIron5 = new ResearchMaterialLevel(209, 4, "Major Alloy").addDependencies(materialIron4);

protected int researchGoalNumber = 0;
protected String displayName = "";
protected String displayTooltip = "";
protected HashSet<IResearchGoal> dependencies = new HashSet<IResearchGoal>();
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
  return this.dependencies;
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

@Override
public int getResearchTime() 
  {
  // TODO Auto-generated method stub
  return researchTime;
  }

@Override
public List<ItemStack> getResearchResources() 
  {
  return resources;
  }

}
