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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.registry.entry.VehicleUpgrade;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.materials.IVehicleMaterial;

/**
 * basically, a first-tier data construct class describing a vehicle.  Each vehicle will
 * reference one of these, where it will get its base stats from.  Mostly implemented to
 * reduce the amount of info sent to client for each vehicle created, and also to help
 * organize the manner and order in which vehicles will be described
 * first-tier--the vehicle itself
 * second-tier--the vehicle materials
 * third-tier--on-board vehicle upgrades
 * @author Shadowmage
 *
 */
public class VehicleTypeBase implements IVehicleType
{

public float width = 2;
public float height = 2;
public float weight = 1000;//kg

public int vehicleType = 0;
public IVehicleMaterial vehicleMaterial = null;
public boolean isMountable = false;
public boolean isDrivable = false;
public boolean isCombatEngine = false;
public boolean canAdjustYaw = false;
public boolean canAdjustPitch = false;
public boolean canAdjustPower = false;

public float missileForwardsOffset;
public float missileHorizontalOffset;
public float missileVerticalOffset;
public float riderForwardsOffset;
public float riderHorizontalOffset;
public float riderVerticalOffset;

public float baseForwardSpeed;
public float baseStrafeSpeed;

public float basePitchMin;
public float basePitchMax;

public float turretRotationMax;
public float baseMissileVelocityMax;
public float baseHealth = 100;

public float accuracy = 1.f;

public List<IAmmoType> validAmmoTypes = new ArrayList<IAmmoType>();
public List<VehicleUpgrade> validUpgrades = new ArrayList<VehicleUpgrade>();

public int materialCount = 1;
public List<ItemStack> additionalMaterials = new ArrayList<ItemStack>();

public VehicleTypeBase(int typeNum)
  {
  this.vehicleType = typeNum;
  }

@Override
public int getGlobalVehicleType()
  {
  return this.vehicleType;
  }

@Override
public IVehicleMaterial getMaterialType()
  {
  return this.vehicleMaterial;
  }

@Override
public boolean isMountable()
  {
  return this.isMountable;
  }

@Override
public boolean isDrivable()
  {
  return this.isDrivable;
  }

@Override
public boolean isCombatEngine()
  {
  return this.isCombatEngine;
  }

@Override
public boolean canAdjustYaw()
  {
  return this.canAdjustYaw;
  }

@Override
public boolean canAdjustPitch()
  {
  return canAdjustPitch;
  }

@Override
public boolean canAdjustPower()
  {
  return canAdjustPower;
  }

@Override
public float getRiderForwardsOffset()
  {
  return this.riderForwardsOffset;
  }

@Override
public float getRiderHorizontalOffset()
  {
  return this.riderHorizontalOffset;
  }

@Override
public float getRiderVerticalOffest()
  {
  return this.riderVerticalOffset;
  }

@Override
public float getBaseForwardSpeed()
  {
  return this.baseForwardSpeed;
  }

@Override
public float getBaseStrafeSpeed()
  {
  return this.baseStrafeSpeed;
  }

@Override
public float getBasePitchMin()
  {
  return this.basePitchMin;
  }

@Override
public float getBasePitchMax()
  {
  return this.basePitchMax;
  }

@Override
public float getBaseHealth()
  {
  return this.baseHealth;
  }

@Override
public float getWidth()
  {
  return this.width;
  }

@Override
public float getHeight()
  {
  return this.height;
  }

@Override
public String getTextureForMaterialLevel(int level)
  {
  return "foo.png";
  }

@Override
public float getMissileForwardsOffset()
  {  
  return this.missileForwardsOffset;
  }

@Override
public float getMissileHorizontalOffset()
  {
  return this.missileHorizontalOffset;
  }

@Override
public float getMissileVerticalOffset()
  {
  return this.missileVerticalOffset;
  }

@Override
public float getBaseWeight()
  {
  return this.weight;
  }

@Override
public float getBaseTurretRotationAmount()
  {
  return this.turretRotationMax;
  }

@Override
public float getBaseMissileVelocityMax()
  {
  return this.baseMissileVelocityMax;
  }

@Override
public boolean isAmmoValidForInventory(IAmmoType ammo)
  {
  return this.validAmmoTypes.contains(ammo);
  }

@Override
public boolean isUpgradeValid(VehicleUpgrade upgrade)
  {
  return this.validUpgrades.contains(upgrade);
  }

@Override
public float getBaseAccuracy()
  {
  return this.accuracy;
  }

@Override
public List<IAmmoType> getValidAmmoTypes()
  {
  return this.validAmmoTypes;
  }

@Override
public List<VehicleUpgrade> getValidUpgrades()
  {
  return this.validUpgrades;
  }

@Override
public int getMaterialQuantity()
  {
  return materialCount;
  }

@Override
public List<ItemStack> getAdditionalMaterials()
  {
  return additionalMaterials;
  }

}
