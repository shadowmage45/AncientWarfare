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
package shadowmage.ancient_warfare.common.vehicles.types;

import shadowmage.ancient_warfare.common.missiles.Ammo;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.materials.VehicleMaterial;

public class VehicleTypeCatapult extends VehicleTypeBase
{

/**
 * @param typeNum
 */
public VehicleTypeCatapult(int typeNum)
  {
  super(typeNum);
  this.width = 2;
  this.height = 2;
  this.missileVerticalOffset = 2.70f* Trig.sinDegrees(45) + 0.4f;
  this.missileForwardsOffset = -2.70f* Trig.cosDegrees(45);
  this.isMountable = true;
  this.isDrivable = true;
  this.isCombatEngine = true;
  this.canAdjustPower = true;
  this.canAdjustPitch = false;
  this.canAdjustYaw = false;
  this.baseStrafeSpeed = 2.f;
  this.baseForwardSpeed = 0.8f;  
  this.basePitchMax = 45;
  this.basePitchMin = 45;
  this.baseMissileVelocityMax = 32.f;  
  this.vehicleMaterial = VehicleMaterial.materialWood;
  this.riderForwardsOffset = 1.2f;
  this.riderVerticalOffset = 0.7f;
  
  this.validAmmoTypes.add(Ammo.ammoArrow);
  this.validAmmoTypes.add(Ammo.ammoStoneShot);
  
  this.validUpgrades.add(VehicleUpgradeRegistry.speedUpgrade);
  }

}
