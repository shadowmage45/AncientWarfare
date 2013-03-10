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
package shadowmage.ancient_warfare.common.vehicles;

import java.util.List;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.vehicles.armors.IVehicleArmorType;
import shadowmage.ancient_warfare.common.vehicles.materials.IVehicleMaterial;
import shadowmage.ancient_warfare.common.vehicles.upgrades.IVehicleUpgradeType;

public interface IVehicleType
{

public abstract float getWidth();
public abstract float getHeight();
public abstract float getBaseWeight();
public abstract float getBaseHealth();//base max health, before any materials or upgrades adjustments

public abstract String getTextureForMaterialLevel(int level);//get the texture for the input material quality level
public abstract String getDisplayName();
public abstract String getDisplayTooltip();

public abstract int getGlobalVehicleType();//by number, registry num...
public abstract IVehicleMaterial getMaterialType();//wood, iron...?? material type will apply adjustments to base stats, before upgrades/etc are applied

public abstract boolean isMountable();//should allow mounting
public abstract boolean isDrivable();//should check movement input params?
public abstract boolean isCombatEngine();//should check non-movement input params?
public abstract boolean canAdjustYaw();//can aim yaw be adjusted independently of vehicle yaw?
public abstract boolean canAdjustPitch();//can aim pitch be adjusted? (should be EITHER pitch OR power)
public abstract boolean canAdjustPower();//can shot velocity be adjusted? (should be EITHER pitch OR power)

public abstract float getMissileForwardsOffset();
public abstract float getMissileHorizontalOffset();
public abstract float getMissileVerticalOffset();

public abstract float getRiderForwardsOffset();
public abstract float getRiderHorizontalOffset();
public abstract float getRiderVerticalOffest();

public abstract float getBaseForwardSpeed();
public abstract float getBaseStrafeSpeed();

public abstract float getBasePitchMin();
public abstract float getBasePitchMax();
public abstract float getBaseTurretRotationAmount();//max rotation from a center point. >=180 means the turret can spin around completely
public abstract float getBaseMissileVelocityMax();//base missile velocity, before materials or upgrades

public abstract float getBaseAccuracy();

public abstract boolean isAmmoValidForInventory(IAmmoType ammo);//does not determine if it can be fired, only if it can be placed into inventory
public abstract boolean isUpgradeValid(IVehicleUpgradeType upgrade);
public abstract boolean isArmorValid(IVehicleArmorType armor);

public abstract List<IAmmoType> getValidAmmoTypes();
public abstract List<IVehicleUpgradeType> getValidUpgrades();
public abstract List<IVehicleArmorType> getValidArmors();

public abstract int getMaterialQuantity();//get the quantity of the main material to construct this vehicle
public abstract List<ItemStack> getAdditionalMaterials();//get a list of additional materials needed to construct this vehicle

public abstract ItemStack getStackForLevel(int level);

}
