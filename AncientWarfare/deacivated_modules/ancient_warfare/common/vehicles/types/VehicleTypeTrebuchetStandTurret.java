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
package shadowmage.ancient_warfare.common.vehicles.types;

import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.item.ItemLoaderCore;
import shadowmage.ancient_warfare.common.research.ResearchGoal;

public class VehicleTypeTrebuchetStandTurret extends VehicleTypeTrebuchet
{

/**
 * @param typeNum
 */
public VehicleTypeTrebuchetStandTurret(int typeNum)
  {
  super(typeNum);
  this.configName = "trebuchet_stand_turret";
  this.displayName = "item.vehicleSpawner.14";
  this.displayTooltip.add("item.vehicleSpawner.tooltip.weight");
  this.displayTooltip.add("item.vehicleSpawner.tooltip.fixed");
  this.displayTooltip.add("item.vehicleSpawner.tooltip.midturret");
  this.width = 2.7f;
  this.height = 2.7f; 
  this.canAdjustYaw = true;
  this.moveRiderWithTurret = true;
  this.shouldRiderSit = true;
  this.turretRotationMax = 45;
  this.riderForwardsOffset = 1.275f;
  this.riderVerticalOffset = 0.7f;
  this.turretVerticalOffset = (34.f + 67.5f + 24.0f+9.5f)*0.0625f;
  this.addNeededResearch(0, ResearchGoal.vehicleTurrets1);
  this.addNeededResearch(1, ResearchGoal.vehicleTurrets2);
  this.addNeededResearch(2, ResearchGoal.vehicleTurrets3);
  this.addNeededResearch(3, ResearchGoal.vehicleTurrets4);
  this.addNeededResearch(4, ResearchGoal.vehicleTurrets5);
  this.additionalMaterials.add(new ItemStackWrapperCrafting(ItemLoaderCore.turretComponents, 1, false, false));
  }

@Override
public String getTextureForMaterialLevel(int level)
  {
  switch(level)
    {
    case 0:
    return Statics.TEXTURE_PATH + "models/trebuchetMobile1.png";
    case 1:
    return Statics.TEXTURE_PATH + "models/trebuchetMobile2.png";
    case 2:
    return Statics.TEXTURE_PATH + "models/trebuchetMobile3.png";
    case 3:
    return Statics.TEXTURE_PATH + "models/trebuchetMobile4.png";
    case 4:
    return Statics.TEXTURE_PATH + "models/trebuchetMobile5.png";
    default:
    return Statics.TEXTURE_PATH + "models/trebuchetMobile1.png";
    }
  }
}
