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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.armors.IVehicleArmorType;
import shadowmage.ancient_warfare.common.vehicles.materials.IVehicleMaterial;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;
import shadowmage.ancient_warfare.common.vehicles.upgrades.IVehicleUpgradeType;

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
public abstract class VehicleType implements IVehicleType
{



/**
 * SELF-REGISTRY STUFF.....
 */
public static final IVehicleType[] vehicleTypes = new IVehicleType[1024];
//private static HashMap<Integer, IVehicleType> vehicleTypes = new HashMap<Integer, IVehicleType>();


/**
 * INSTANCE VARIABLES......
 */
public float width = 2;
public float height = 2;
public float weight = 1000;//kg

private final int vehicleType;
public IVehicleMaterial vehicleMaterial = null;
public boolean isMountable = false;
public boolean isDrivable = false;
public boolean isCombatEngine = false;
public boolean canAdjustYaw = false;
public boolean canAdjustPitch = false;
public boolean canAdjustPower = false;

public float turretForwardsOffset = 0.f;
public float turretHorizontalOffset = 0.f;
public float turretVerticalOffset = 0.f;
public float missileForwardsOffset = 0.f;
public float missileHorizontalOffset= 0.f;
public float missileVerticalOffset= 0.f;
public float riderForwardsOffset= 0.f;
public float riderHorizontalOffset= 0.f;
public float riderVerticalOffset= 0.f;
public boolean shouldRiderSit = true;
public boolean moveRiderWithTurret = false;

public float baseForwardSpeed;
public float baseStrafeSpeed;

public float basePitchMin;
public float basePitchMax;

public float turretRotationMax;
public float baseMissileVelocityMax;
public float baseHealth = 100;

public float maxMissileWeight = 10;

public float accuracy = 1.f;

public String displayName = "AWVehicleBase";
public String displayTooltip = "AWVehicleBase";

public List<IAmmoType> validAmmoTypes = new ArrayList<IAmmoType>();
public List<IVehicleUpgradeType> validUpgrades = new ArrayList<IVehicleUpgradeType>();
public List<IVehicleArmorType> validArmors = new ArrayList<IVehicleArmorType>();

int storageBaySize = 0;
int ammoBaySize = 6;
int upgradeBaySize = 3;
int armorBaySize = 3;

public int materialCount = 1;
public List<ItemStack> additionalMaterials = new ArrayList<ItemStack>();

public VehicleType(int typeNum)
  {
  this.vehicleType = typeNum; 
  vehicleTypes[typeNum] = this;
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
public boolean isUpgradeValid(IVehicleUpgradeType upgrade)
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
public List<IVehicleUpgradeType> getValidUpgrades()
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

@Override
public boolean isArmorValid(IVehicleArmorType armor)
  {
  return this.validArmors.contains(armor);
  }

@Override
public List<IVehicleArmorType> getValidArmors()
  {
  return this.validArmors;
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
public ItemStack getStackForLevel(int level)
  {
  ItemStack stack = new ItemStack(ItemLoader.vehicleSpawner,1,this.getGlobalVehicleType());
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("lev", level);
  stack.setTagInfo("AWVehSpawner", tag);
  return stack;
  }

@Override
public int getStorageBaySize()
  {
  return this.storageBaySize;
  }

@Override
public int getAmmoBaySize()
  {
  return this.ammoBaySize;
  }

@Override
public int getArmorBaySize()
  {
  return this.armorBaySize;
  }

@Override
public int getUpgradeBaySize()
  {
  return this.upgradeBaySize;
  }

@Override
public float getMaxMissileWeight()
  {
  return this.maxMissileWeight;
  }

@Override
public boolean shouldRiderSit()
  {
  return this.shouldRiderSit;
  }

/********************************REGISTRY METHODS********************************/

public static IVehicleType getVehicleType(int num)
  {
  if(num>=0 && num < vehicleTypes.length)
    {
    return vehicleTypes[num];
    }
  return null;
  }

public static VehicleBase getVehicleForType(World world, int type, int level)
  {
  if(vehicleTypes[type]!=null)
    {
    IVehicleType vehType = getVehicleType(type);
    VehicleBase vehicle = new VehicleBase(world);
    vehicle.setVehicleType(vehType, level);
    vehicle.setInitialHealth();
    return vehicle;
    }  
  return null;
  }

private static List<ItemStack> displayItemCache = null;

public static List getCreativeDisplayItems()
  {
  if(displayItemCache!=null)
    {
    return displayItemCache;
    }
  List<ItemStack> stacks = new ArrayList<ItemStack>();
  ItemStack stack = null;
  for(IVehicleType type : vehicleTypes)
    {
    if(type==null || type.getMaterialType()==null)
      {
      continue;
      }
    for(int i = 0; i < type.getMaterialType().getNumOfLevels(); i++)
      {
      stack = new ItemStack(ItemLoader.vehicleSpawner,1,type.getGlobalVehicleType());
      NBTTagCompound tag = new NBTTagCompound();
      tag.setInteger("lev", i);
      stack.setTagInfo("AWVehSpawner", tag);
      stacks.add(stack);
      }
    } 
  displayItemCache = stacks;
  return stacks;
  }

@Override
public boolean moveRiderWithTurret()
  {
  return this.moveRiderWithTurret;
  }

@Override
public float getTurretPosX()
  {
  return turretHorizontalOffset;
  }

@Override
public float getTurretPosY()
  {
  return turretVerticalOffset;
  }

@Override
public float getTurretPosZ()
  {
  return turretForwardsOffset;
  }

}
