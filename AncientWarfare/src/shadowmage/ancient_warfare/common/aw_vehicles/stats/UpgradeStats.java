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
package shadowmage.ancient_warfare.common.aw_vehicles.stats;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.aw_core.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.aw_core.registry.entry.VehicleUpgrade;
import shadowmage.ancient_warfare.common.aw_vehicles.VehicleBase;

public class UpgradeStats
{

/**
 * currently installed upgrades, will be iterated through linearly to call upgrade.applyEffects, multiple upgrades may have cumulative effects
 */
private List upgrades = new ArrayList<VehicleUpgrade>();

/**
 * list of all upgrades that are valid for this vehicle, used by inventoryChecking to see whether it can be installed or not
 */
private List validUpgrades = new ArrayList<VehicleUpgrade>();
private VehicleBase vehicle;

public UpgradeStats(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

public void updateUpgrades()
  {
  //set all alterable values back to base values
  //apply effects from upgrades linearly
  }

public void addValidUpgrade(int type)
  {
  VehicleUpgrade upgrade = VehicleUpgradeRegistry.instance().getUpgrade(type);
  if(upgrade!=null && !this.validUpgrades.contains(upgrade))
    {
    this.validUpgrades.add(upgrade);
    }
  }

public void addValidUpgrade(String name)
  {
  VehicleUpgrade upgrade = VehicleUpgradeRegistry.instance().getUpgrade(name);
  if(upgrade!=null && !this.validUpgrades.contains(upgrade))
    {
    this.validUpgrades.add(upgrade);
    }
  }

}
