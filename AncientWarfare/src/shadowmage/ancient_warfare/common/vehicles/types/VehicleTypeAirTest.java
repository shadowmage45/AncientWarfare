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

import net.minecraft.item.Item;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleMovementType;
import shadowmage.ancient_warfare.common.vehicles.VehicleVarHelpers.BallistaVarHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleFiringVarsHelper;
import shadowmage.ancient_warfare.common.vehicles.materials.VehicleMaterial;
import shadowmage.ancient_warfare.common.vehicles.missiles.Ammo;

public class VehicleTypeAirTest extends VehicleType
{

/**
 * @param typeNum
 */
public VehicleTypeAirTest(int typeNum)
  {
  super(typeNum);
  this.vehicleMaterial = VehicleMaterial.materialWood;  
  this.materialCount = 5;
  this.movementType = VehicleMovementType.AIR1;
  this.maxMissileWeight = 20.f;
  
  this.validAmmoTypes.add(Ammo.ammoStoneShot10);
  this.validAmmoTypes.add(Ammo.ammoStoneShot15); 
  this.validAmmoTypes.add(Ammo.ammoFireShot10);
  this.validAmmoTypes.add(Ammo.ammoFireShot15);
  this.validAmmoTypes.add(Ammo.ammoPebbleShot10);
  this.validAmmoTypes.add(Ammo.ammoPebbleShot15);
  this.validAmmoTypes.add(Ammo.ammoClusterShot10);
  this.validAmmoTypes.add(Ammo.ammoClusterShot15);
  this.validAmmoTypes.add(Ammo.ammoExplosive10);
  this.validAmmoTypes.add(Ammo.ammoExplosive15);
  this.validAmmoTypes.add(Ammo.ammoHE10);
  this.validAmmoTypes.add(Ammo.ammoHE15);
  this.validAmmoTypes.add(Ammo.ammoNapalm10);
  this.validAmmoTypes.add(Ammo.ammoNapalm15);
  
  
  this.validAmmoTypes.add(Ammo.ammoArrow);
  this.validAmmoTypes.add(Ammo.ammoArrowFlame);
  this.validAmmoTypes.add(Ammo.ammoArrowIron);
  this.validAmmoTypes.add(Ammo.ammoArrowIronFlame);
  
  if(Config.addOversizeAmmo)
    {
    this.validAmmoTypes.add(Ammo.ammoStoneShot30);
    this.validAmmoTypes.add(Ammo.ammoStoneShot45);
    this.validAmmoTypes.add(Ammo.ammoFireShot30);
    this.validAmmoTypes.add(Ammo.ammoFireShot45);
    this.validAmmoTypes.add(Ammo.ammoPebbleShot30);
    this.validAmmoTypes.add(Ammo.ammoPebbleShot45);
    this.validAmmoTypes.add(Ammo.ammoClusterShot30);
    this.validAmmoTypes.add(Ammo.ammoClusterShot45);
    this.validAmmoTypes.add(Ammo.ammoExplosive30);
    this.validAmmoTypes.add(Ammo.ammoExplosive45);
    this.validAmmoTypes.add(Ammo.ammoHE30);
    this.validAmmoTypes.add(Ammo.ammoHE45);
    
    }
  
  this.ammoBySoldierRank.put(0, Ammo.ammoStoneShot10);
  this.ammoBySoldierRank.put(1, Ammo.ammoStoneShot10);
  this.ammoBySoldierRank.put(2, Ammo.ammoStoneShot10);
  
  this.validArmors.add(ArmorRegistry.armorStone);
  this.validArmors.add(ArmorRegistry.armorObsidian);
  this.validArmors.add(ArmorRegistry.armorIron);
    
  this.validUpgrades.add(VehicleUpgradeRegistry.speedUpgrade);
  this.validUpgrades.add(VehicleUpgradeRegistry.reloadUpgrade);
      
  this.armorBaySize = 1;
  this.upgradeBaySize = 3;
  this.ammoBaySize = 2;  
  this.storageBaySize = 0;    
  
  this.addNeededResearchForMaterials();
  
  this.addNeededResearch(0, ResearchGoal.vehicleMobility1);
  this.addNeededResearch(1, ResearchGoal.vehicleMobility2);
  this.addNeededResearch(2, ResearchGoal.vehicleMobility3);
  this.addNeededResearch(3, ResearchGoal.vehicleMobility4);
  this.addNeededResearch(4, ResearchGoal.vehicleMobility5);
  
  this.additionalMaterials.add(new ItemStackWrapperCrafting(Item.silk, 8, false, false));
  this.additionalMaterials.add(new ItemStackWrapperCrafting(ItemLoader.torsionUnit, 2, false, false));
  this.additionalMaterials.add(new ItemStackWrapperCrafting(ItemLoader.equipmentBay, 1, false, false));
  this.additionalMaterials.add(new ItemStackWrapperCrafting(ItemLoader.mobilityUnit, 1, false, false));
  
  this.width = 2.7f;
  this.height = 1.4f;  
          
  this.baseStrafeSpeed = 2.f;
  this.baseForwardSpeed = 25f*0.05f;
  
  this.turretForwardsOffset = 0.f;
  this.turretVerticalOffset = 0.f;
  this.accuracy = 0.98f;
  this.basePitchMax = -90;
  this.basePitchMin = -90; 
  this.baseMissileVelocityMax = 2.f;   
  
  this.riderForwardsOffset = -0.0625f * 7;
  this.riderVerticalOffset = 0.0625f * 12;  
  this.shouldRiderSit = true;
  
  this.isMountable = true;
  this.isDrivable = true;//adjust based on isMobile or not
  this.isCombatEngine = true;
  
  this.canAdjustPitch = true;
  this.canAdjustPower = false;
  this.canAdjustYaw = false;
  this.turretRotationMax=0.f;//adjust based on mobile/stand fixed (0), stand fixed(90'), or mobile or stand turret (360) 
  this.displayName = "Aircraft test";
  this.displayTooltip.add("No Firing Mechanism");
  this.displayTooltip.add("Airplane Hull");
  this.displayTooltip.add("No Turret");
  }

@Override
public String getTextureForMaterialLevel(int level)
  {
  switch(level)
    {
    case 0:
    return Config.texturePath + "models/airplane1.png";
    case 1:
    return Config.texturePath + "models/airplane2.png";
    case 2:
    return Config.texturePath + "models/airplane3.png";
    case 3:
    return Config.texturePath + "models/airplane4.png";
    case 4:
    return Config.texturePath + "models/airplane5.png";
    default:
    return Config.texturePath + "models/airplane1.png";
    }
  }

@Override
public VehicleFiringVarsHelper getFiringVarsHelper(VehicleBase veh)
  {
  return new BallistaVarHelper(veh);
  }

}
