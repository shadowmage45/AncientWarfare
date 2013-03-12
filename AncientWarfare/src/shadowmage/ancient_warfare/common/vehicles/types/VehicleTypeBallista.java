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
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.materials.VehicleMaterial;

public class VehicleTypeBallista extends VehicleTypeBase
{

/**
 * @param typeNum
 */
public VehicleTypeBallista(int typeNum)
  {
  super(typeNum);  
  this.width = 2;
  this.height = 2;
  this.missileVerticalOffset = 1.2f;
  this.missileForwardsOffset = 1.f;
  this.isMountable = true;
  this.isDrivable = true;
  this.isCombatEngine = true;
  this.canAdjustPower = false;
  this.canAdjustPitch = true;
  this.canAdjustYaw = true;
  this.turretRotationMax=360.f;
  this.accuracy = 0.98f;
  this.baseStrafeSpeed = 2.f;
  this.baseForwardSpeed = 6.f*0.05f;  
  this.basePitchMax = 15;
  this.basePitchMin = -15;
  this.baseMissileVelocityMax = 42.f;  
  this.vehicleMaterial = VehicleMaterial.materialWood;
  this.riderForwardsOffset = -1.2f;
  this.riderVerticalOffset = 0.7f;
  
  this.displayName = "Ballista";
  this.displayTooltip = "The original, classic, ballista.";
  
  this.validAmmoTypes.add(Ammo.ammoArrow);
  this.validAmmoTypes.add(Ammo.ammoStoneShot);
  
  this.validUpgrades.add(VehicleUpgradeRegistry.speedUpgrade);
  this.validArmors.add(ArmorRegistry.armorStone);
  
  this.storageBaySize = 0;
  this.armorBaySize = 3;
  }



}
