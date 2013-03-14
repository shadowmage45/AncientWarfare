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
package shadowmage.ancient_warfare.common.vehicles.upgrades;

import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleUpgradePitchDown extends VehicleUpgradeBase
{

/**
 * @param num
 */
public VehicleUpgradePitchDown(int num)
  {
  super(num);
  this.displayName = "Turret Wedge: -3";
  this.tooltip = "Decreases turret pitch by 3 degrees.";
  }

@Override
public void applyVehicleEffects(VehicleBase vehicle)
  {
  vehicle.currentTurretPitchMax-=3;
  vehicle.currentTurretPitchMin-=3;
  if(vehicle.localTurretPitch<vehicle.currentTurretPitchMin)
    {
    vehicle.localTurretPitch = vehicle.currentTurretPitchMin;
    }
  if(vehicle.localTurretPitch>vehicle.currentTurretPitchMax)
    {
    vehicle.localTurretPitch = vehicle.currentTurretPitchMax;
    }
  }

}
