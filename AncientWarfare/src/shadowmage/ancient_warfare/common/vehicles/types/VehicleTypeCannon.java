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
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleVarHelpers.CannonVarHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleFiringVarsHelper;
import shadowmage.ancient_warfare.common.vehicles.materials.VehicleMaterial;

public class VehicleTypeCannon extends VehicleType
{

/**
 * @param typeNum
 */
public VehicleTypeCannon(int typeNum)
  {
  super(typeNum);
  this.vehicleMaterial = VehicleMaterial.materialIron;
  
  this.validAmmoTypes.add(Ammo.ammoStoneShot);  
  
  this.validUpgrades.add(VehicleUpgradeRegistry.pitchDownUpgrade);
  this.validUpgrades.add(VehicleUpgradeRegistry.pitchUpUpgrade);
  this.validUpgrades.add(VehicleUpgradeRegistry.pitchExtUpgrade);
  this.validUpgrades.add(VehicleUpgradeRegistry.powerUpgrade);
  this.validUpgrades.add(VehicleUpgradeRegistry.reloadUpgrade);
  this.validUpgrades.add(VehicleUpgradeRegistry.aimUpgrade);

  this.validArmors.add(ArmorRegistry.armorStone);
  this.validArmors.add(ArmorRegistry.armorIron);
  this.validArmors.add(ArmorRegistry.armorObsidian);

  this.storageBaySize = 0; 
  this.accuracy = 0.98f;
  this.baseStrafeSpeed = 2.f;
  this.baseForwardSpeed = 6.f*0.05f; 
  this.basePitchMax = 15;
  this.basePitchMin = -15; 
  this.isMountable = true;
  this.isCombatEngine = true;
  this.canAdjustPitch = true;
  this.canAdjustPower = false;


  /**
   * default values that should be overriden by ballista types...
   */
  this.baseMissileVelocityMax = 42.f;//stand versions should have higher velocity, as should fixed version--i.e. mobile turret should have the worst of all versions   
  this.width = 2;
  this.height = 2;  

  this.armorBaySize = 3;
  this.upgradeBaySize = 3;
  this.ammoBaySize = 6;
  }

@Override
public VehicleFiringVarsHelper getFiringVarsHelper(VehicleBase veh)
  {
  return new CannonVarHelper(veh);
  }

}
