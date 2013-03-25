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

public class VehicletypeCatapultStandTurret extends VehicleTypeCatapult
{

/**
 * @param typeNum
 */
public VehicletypeCatapultStandTurret(int typeNum)
  {
  super(typeNum);
  
  this.width = 2.7f;
  this.height = 2.f; 
  this.baseMissileVelocityMax = 32.f;  
  this.turretVerticalOffset = 13 * 0.0625f;
  this.turretVerticalOffset = 0.4f;
  this.riderForwardsOffset = 1.2f;
  this.riderVerticalOffset = 0.7f;
  this.displayName = "Catapult Stand Turret";
  this.displayTooltip = "An immobile catapult mounted on a swiveling turret.";
  this.storageBaySize = 0;
  this.armorBaySize = 4;
  this.upgradeBaySize = 4;
  this.canAdjustYaw = true;
  this.baseForwardSpeed = 0.f;
  this.baseStrafeSpeed = .5f;
  this.turretRotationMax=45.f;
  this.isDrivable = true;
  this.shouldRiderSit = true;
  this.moveRiderWithTurret = true;
  }

@Override
public String getTextureForMaterialLevel(int level)
  {
  switch(level)
    {
    case 0:
    return Config.texturePath + "models/catapultStandTurret1.png";
    case 1:
    return Config.texturePath + "models/catapultStandTurret2.png";
    case 2:
    return Config.texturePath + "models/catapultStandTurret3.png";
    case 3:
    return Config.texturePath + "models/catapultStandTurret4.png";
    case 4:
    return Config.texturePath + "models/catapultStandTurret5.png";
    default:
    return Config.texturePath + "models/catapultStandTurret1.png";
    }
  }
}
