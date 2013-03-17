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

public class VehicleTypeCatapultMobileTurret extends VehicleTypeCatapult
{

  /**
   * @param typeNum
   */
  public VehicleTypeCatapultMobileTurret(int typeNum)
    {
    super(typeNum);
    this.width = 2;
    this.height = 2; 
    this.baseMissileVelocityMax = 30.f;  
    this.turretVerticalOffset = 15 * 0.0625f;
    this.missileVerticalOffset = 2.0f* Trig.sinDegrees(70);
    this.missileForwardsOffset = -2.0f* Trig.cosDegrees(70);
    this.riderForwardsOffset = 1.2f;
    this.riderVerticalOffset = 0.8f;
    this.displayName = "Catapult Mobile Turret";
    this.displayTooltip = "A turret-mounted catapult on a wheeled frame.";
    this.storageBaySize = 0;
    this.armorBaySize = 3;
    this.upgradeBaySize = 3;
    this.canAdjustYaw = true;
    this.isDrivable = true;
    this.shouldRiderSit = true;
    this.moveRiderWithTurret = true;
    this.turretRotationMax=180.f;
    }

@Override
public String getTextureForMaterialLevel(int level)
  {
  switch(level)
    {
    case 0:
    return Config.texturePath + "models/catapultMobileTurret1.png";
    case 1:
    return Config.texturePath + "models/catapultMobileTurret2.png";
    case 2:
    return Config.texturePath + "models/catapultMobileTurret3.png";
    case 3:
    return Config.texturePath + "models/catapultMobileTurret4.png";
    case 4:
    return Config.texturePath + "models/catapultMobileTurret5.png";
    default:
    return Config.texturePath + "models/catapultMobileTurret1.png";
    }
  }
}
