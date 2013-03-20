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

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.missiles.Ammo;
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleFiringVarsHelper;
import shadowmage.ancient_warfare.common.vehicles.materials.VehicleMaterial;


public class VehicleTypeHwacha extends VehicleType
{

/**
 * @param typeNum
 */
public VehicleTypeHwacha(int typeNum)
  {
  super(typeNum);
  this.vehicleMaterial = VehicleMaterial.materialWood;  
  this.validAmmoTypes.add(Ammo.ammoRocket);
  this.validArmors.add(ArmorRegistry.armorStone);
  this.validArmors.add(ArmorRegistry.armorIron);
  this.validArmors.add(ArmorRegistry.armorObsidian);

  this.isMountable = true;
  this.isDrivable = true;
  this.shouldRiderSit = false;
  this.moveRiderWithTurret = false;
  this.isCombatEngine = true;
  this.canAdjustPower = true;
  this.canAdjustPitch = false;
  this.canAdjustYaw = false;
  this.accuracy = 0.75f;
  this.baseStrafeSpeed = 1.f;
  this.baseForwardSpeed = 4.f*0.05f;  
  this.basePitchMax = 20;
  this.basePitchMin = 20;

  this.validUpgrades.add(VehicleUpgradeRegistry.aimUpgrade);
  this.validUpgrades.add(VehicleUpgradeRegistry.pitchDownUpgrade);
  this.validUpgrades.add(VehicleUpgradeRegistry.pitchUpUpgrade);
  this.validUpgrades.add(VehicleUpgradeRegistry.powerUpgrade);  
  this.validUpgrades.add(VehicleUpgradeRegistry.reloadUpgrade);
  this.validUpgrades.add(VehicleUpgradeRegistry.speedUpgrade);

  this.width = 2;
  this.height = 2; 
  this.baseMissileVelocityMax = 42.f;  
  this.missileVerticalOffset = 1.6f;
  this.missileForwardsOffset = 0.8f;
  this.riderForwardsOffset = -1.2f;
  this.riderVerticalOffset = 0.5f;
  this.displayName = "Hwacha";
  this.displayTooltip = "OMGZ Rockets!";
  this.storageBaySize = 0;
  this.armorBaySize = 3;
  this.upgradeBaySize = 3;
  }

@Override
public VehicleFiringVarsHelper getFiringVarsHelper(VehicleBase veh)
  {
  return new HwachaFiringVarsHelper(veh);
  }

public class HwachaFiringVarsHelper extends VehicleFiringVarsHelper
{
/**
 * @param vehicle
 */
public HwachaFiringVarsHelper(VehicleBase vehicle)
  {
  super(vehicle);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  return new NBTTagCompound();
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {

  }

@Override
public void onFiringUpdate()
  {

  }

@Override
public void onReloadUpdate()
  {
  
  }

@Override
public void onLaunchingUpdate()
  {

  }

@Override
public void onReloadingFinished()
  {

  }

@Override
public float getVar1()
  {
  return 0;
  }

@Override
public float getVar2()
  {
  return 0;
  }

@Override
public float getVar3()
  {
  return 0;
  }

@Override
public float getVar4()
  {
  return 0;
  }

@Override
public float getVar5()
  {
  return 0;
  }

@Override
public float getVar6()
  {
  return 0;
  }

@Override
public float getVar7()
  {
  return 0;
  }

@Override
public float getVar8()
  {
  return 0;
  }
}
}
