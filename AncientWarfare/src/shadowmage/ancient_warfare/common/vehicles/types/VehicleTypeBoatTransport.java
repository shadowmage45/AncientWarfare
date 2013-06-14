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

import net.minecraft.block.Block;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleMovementType;
import shadowmage.ancient_warfare.common.vehicles.VehicleVarHelpers.DummyVehicleHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleFiringVarsHelper;
import shadowmage.ancient_warfare.common.vehicles.materials.VehicleMaterial;

public class VehicleTypeBoatTransport extends VehicleType
{

/**
 * @param typeNum
 */
public VehicleTypeBoatTransport(int typeNum)
  {
  super(typeNum);
  this.vehicleMaterial = VehicleMaterial.materialWood;
  this.movementType = VehicleMovementType.WATER;
  this.materialCount = 3;
  this.validArmors.add(ArmorRegistry.armorStone);
  this.validArmors.add(ArmorRegistry.armorObsidian);
  this.validArmors.add(ArmorRegistry.armorIron);  
  this.width = 2.7f;
  this.height = 1.4f;  
  this.isMountable = true;
  this.isDrivable = true;
  this.isCombatEngine = false;
  this.shouldRiderSit = true;
  this.riderForwardsOffset = 1.4f;
  this.riderVerticalOffset = 0.7f;
  this.baseForwardSpeed = 3.7f*0.05f;
  this.baseStrafeSpeed = 1.75f;
  this.ammoBaySize = 0;
  this.upgradeBaySize = 6;
  this.armorBaySize = 6;
  this.storageBaySize = 54*4;
  this.displayName = "Transport Ship" ;
  this.displayTooltip.add("No Firing Mechanism");
  this.displayTooltip.add("Boat Hull");
  this.displayTooltip.add("No Turret");
  this.displayTooltip.add("Special: Storage Area");
  this.addNeededResearchForMaterials();
  this.additionalMaterials.add(new ItemStackWrapperCrafting(Block.chest, 8, false, false));
  this.additionalMaterials.add(new ItemStackWrapperCrafting(ItemLoader.equipmentBay, 1, false, false));
  }

@Override
public String getTextureForMaterialLevel(int level)
  {
  switch(level)
    {
    case 0:
    return Config.texturePath + "models/boatTransport1.png";
    case 1:
    return Config.texturePath + "models/boatTransport2.png";
    case 2:
    return Config.texturePath + "models/boatTransport3.png";
    case 3:
    return Config.texturePath + "models/boatTransport4.png";
    case 4:
    return Config.texturePath + "models/boatTransport5.png";
    default:
    return Config.texturePath + "models/boatTransport1.png";
    }
  }


@Override
public VehicleFiringVarsHelper getFiringVarsHelper(VehicleBase veh)
  {
  return new DummyVehicleHelper(veh);
  }

}
