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

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.utils.Trig;

public class VehicleTypeCatapultMobileFixed extends VehicleTypeCatapult
{

/**
 * @param typeNum
 */
public VehicleTypeCatapultMobileFixed(int typeNum)
  {
  super(typeNum);
  this.width = 2;
  this.height = 2; 
  this.baseStrafeSpeed = 1.7f;
  this.baseForwardSpeed = 4.2f*0.05f;
  this.baseMissileVelocityMax = 32.f;  
  this.turretVerticalOffset = 15 * 0.0625f;
  this.missileVerticalOffset = 2.0f* Trig.sinDegrees(70);
  this.missileForwardsOffset = -2.0f* Trig.cosDegrees(70);
  this.riderForwardsOffset = 1.2f;
  this.riderVerticalOffset = 0.7f;
  this.displayName = "Catapult Mobile Fixed";
  this.displayTooltip = "A catpult mounted on a wheeled frame.";
  this.storageBaySize = 0;
  this.armorBaySize = 3;
  this.upgradeBaySize = 3;
  this.canAdjustYaw = false;
  this.isDrivable = true;
  this.shouldRiderSit = true;
  this.moveRiderWithTurret = false;
  }

@Override
public String getTextureForMaterialLevel(int level)
  {
  switch(level)
    {
    case 0:
    return Config.texturePath + "models/catapultMobileFixed1.png";
    case 1:
    return Config.texturePath + "models/catapultMobileFixed2.png";
    case 2:
    return Config.texturePath + "models/catapultMobileFixed3.png";
    case 3:
    return Config.texturePath + "models/catapultMobileFixed4.png";
    case 4:
    return Config.texturePath + "models/catapultMobileFixed5.png";
    default:
    return Config.texturePath + "models/catapultMobileFixed1.png";
    }
  }
}
