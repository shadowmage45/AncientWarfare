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
package shadowmage.ancient_warfare.client.gui.info;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.materials.IVehicleMaterial;

public class GuiVehicleDetails extends GuiInfoBase
{

IVehicleType vehicle;
int level;



/**
 * @param parent
 * @param recipe
 */
public GuiVehicleDetails(GuiContainerAdvanced parent,  ResourceListRecipe recipe, IVehicleType vehicle, int level)
  {
  super(parent, recipe);
  this.level = level;
  this.vehicle = vehicle;
  IVehicleMaterial material = vehicle.getMaterialType();
  this.detailText.add("Max Hit Points     : "+vehicle.getBaseHealth() * material.getHPFactor(level));
  this.detailText.add("Max Forward Speed  : " +vehicle.getBaseForwardSpeed() * material.getSpeedForwardFactor(level));
  this.detailText.add("Max Strafe Speed   : " +vehicle.getBaseStrafeSpeed() * material.getSpeedStrafeFactor(level));
  this.detailText.add("Vehicle Weight     : "+vehicle.getBaseWeight() * material.getWeightFactor(level));
  this.detailText.add("Has turret         : "+vehicle.canAdjustYaw());
  this.detailText.add("Can aim pitch      : "+vehicle.canAdjustPitch());
  this.detailText.add("Can adjust power   : "+vehicle.canAdjustPower());
  this.detailText.add("Ammo Bay Size      : " +  vehicle.getAmmoBaySize());
  this.detailText.add("Armor Bay Size     : "+ vehicle.getArmorBaySize());
  this.detailText.add("Upgrade Bay Size   : "+vehicle.getUpgradeBaySize());
  this.detailText.add("Storage Bay Size   : "+vehicle.getStorageBaySize());
  this.detailText.add("Is mountable       : "+vehicle.isMountable());
  this.detailText.add("Is driveable       : "+vehicle.isDrivable());
  this.detailText.add("Is Combat Engine   : "+vehicle.isCombatEngine());
  
  
  
  }

}
