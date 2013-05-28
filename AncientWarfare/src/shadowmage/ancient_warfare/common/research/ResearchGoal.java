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

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchCounterweights1;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchCounterweights2;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchCounterweights3;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchGunpowderVehicles1;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchGunpowderVehicles2;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchMaterialLevel;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchMobility1;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchMobility2;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTorsion1;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTorsion2;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTorsion3;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTurrets1;
import shadowmage.ancient_warfare.common.research.vehicle.ResearchTurrets2;

public class ResearchGoal implements IResearchGoal
{
private static ResearchGoal[] researchGoals = new ResearchGoal[4096];

public static IResearchGoal vehicleMobility1 = new ResearchMobility1(0);
public static IResearchGoal vehicleMobility2 = new ResearchMobility2(1).addDependencies(vehicleMobility1);
public static IResearchGoal vehicleTurrets1 = new ResearchTurrets1(2);
public static IResearchGoal vehicleTurrets2 = new ResearchTurrets2(3).addDependencies(vehicleTurrets1);
public static IResearchGoal vehicleTorsion1 = new ResearchTorsion1(4);
public static IResearchGoal vehicleTorsion2 = new ResearchTorsion2(5).addDependencies(vehicleTorsion1);
public static IResearchGoal vehicleTorsion3 = new ResearchTorsion3(6).addDependencies(vehicleTorsion2);
public static IResearchGoal vehicleCounterweights1 = new ResearchCounterweights1(7);
public static IResearchGoal vehicleCounterweights2 = new ResearchCounterweights2(8).addDependencies(vehicleCounterweights1);
public static IResearchGoal vehicleCounterweights3 = new ResearchCounterweights3(9).addDependencies(vehicleCounterweights2);
public static IResearchGoal vehicleGunpowderWeapons1 = new ResearchGunpowderVehicles1(10);
public static IResearchGoal vehicleGunpowderWeapons2 = new ResearchGunpowderVehicles2(11).addDependencies(vehicleGunpowderWeapons1);

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

public static IResearchGoal materialWood1 = new ResearchMaterialLevel(200, 0, "Rough Wood Materials");
public static IResearchGoal materialWood2 = new ResearchMaterialLevel(201, 1, "Treated Wood Materials");
public static IResearchGoal materialWood3 = new ResearchMaterialLevel(202, 2, "Ironshod Wood Materials");
public static IResearchGoal materialWood4 = new ResearchMaterialLevel(203, 3, "Iron Core Wood Materials");
public static IResearchGoal materialWood5 = new ResearchMaterialLevel(204, 4, "Iron Substitute Materials");

public static IResearchGoal materialIron1 = new ResearchMaterialLevel(205, 0, "Rough Iron Materials");
public static IResearchGoal materialIron2 = new ResearchMaterialLevel(206, 1, "Fine Iron Materials");
public static IResearchGoal materialIron3 = new ResearchMaterialLevel(207, 2, "Tempered Iron Materials");
public static IResearchGoal materialIron4 = new ResearchMaterialLevel(208, 3, "Minor Alloy Materials");
public static IResearchGoal materialIron5 = new ResearchMaterialLevel(209, 4, "Alloy Materials");

protected int researchGoalNumber = 0;
protected String displayName = "";
protected String displayTooltip = "";
protected HashSet<IResearchGoal> dependencies = new HashSet<IResearchGoal>();
protected List<String> detailedDescription = new ArrayList<String>();

public ResearchGoal(int num)
  {
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

}
