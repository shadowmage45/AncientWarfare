/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
package shadowmage.ancient_vehicles.common.vehicles.types;

import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_vehicles.common.registry.VehicleUpgradeRegistry;

public class VehicleTypeBallistaMobile extends VehicleTypeBallista
{

  /**
   * @param typeNum
   */
  public VehicleTypeBallistaMobile(int typeNum)
    {
    super(typeNum);
    this.configName = "ballista_mobile";
    this.baseMissileVelocityMax = 42.f;//stand versions should have higher velocity, as should fixed version--i.e. mobile turret should have the worst of all versions   
    this.width = 2.7f;
    this.height = 1.4f;  
    
    this.armorBaySize = 3;
    this.upgradeBaySize = 3;
      
    this.baseStrafeSpeed = 1.7f;
    this.baseForwardSpeed = 4.2f*0.05f;
    this.turretForwardsOffset = 1.f;
    this.turretVerticalOffset = 1.2f;
    this.riderForwardsOffset = -1.2f;
    this.riderVerticalOffset = 0.7f;
    this.shouldRiderSit = true;
    
    this.isDrivable = true;//adjust based on isMobile or not
    this.canAdjustYaw = false;//adjust based on hasTurret or not
    this.turretRotationMax=0.f;//adjust based on mobile/stand fixed (0), stand fixed(90'), or mobile or stand turret (360) 
    this.displayName = "item.vehicleSpawner.6";
    this.displayTooltip.add("item.vehicleSpawner.tooltip.torsion");
    this.displayTooltip.add("item.vehicleSpawner.tooltip.mobile");
    this.displayTooltip.add("item.vehicleSpawner.tooltip.noturret");

    this.validUpgrades.add(VehicleUpgradeRegistry.speedUpgrade);
    this.addNeededResearch(0, ResearchGoal.vehicleMobility1);
    this.addNeededResearch(1, ResearchGoal.vehicleMobility2);
    this.addNeededResearch(2, ResearchGoal.vehicleMobility3);
    this.addNeededResearch(3, ResearchGoal.vehicleMobility4);
    this.addNeededResearch(4, ResearchGoal.vehicleMobility5);
    this.additionalMaterials.add(new ItemStackWrapperCrafting(ItemLoaderCore.mobilityUnit, 1, false, false));
    }
  
@Override
public String getTextureForMaterialLevel(int level)
  {
  switch(level)
    {
    case 0:
    return Statics.TEXTURE_PATH + "models/ballistaMobile1.png";
    case 1:
    return Statics.TEXTURE_PATH + "models/ballistaMobile2.png";
    case 2:
    return Statics.TEXTURE_PATH + "models/ballistaMobile3.png";
    case 3:
    return Statics.TEXTURE_PATH + "models/ballistaMobile4.png";
    case 4:
    return Statics.TEXTURE_PATH + "models/ballistaMobile5.png";
    default:
    return Statics.TEXTURE_PATH + "models/ballistaMobile1.png";
    }
  }

}
