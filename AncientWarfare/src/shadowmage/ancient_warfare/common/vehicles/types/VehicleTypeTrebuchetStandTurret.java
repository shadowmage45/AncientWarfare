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

public class VehicleTypeTrebuchetStandTurret extends VehicleTypeTrebuchet
{

/**
 * @param typeNum
 */
public VehicleTypeTrebuchetStandTurret(int typeNum)
  {
  super(typeNum);
  this.displayName = "Trebuchet Stand Turret";
  this.displayTooltip.add("Trebuchet Firing Mechanism");
  this.displayTooltip.add("Fixed-Emplacement");
  this.displayTooltip.add("45' Turret");
  this.width = 2.7f;
  this.height = 2.7f; 
  this.canAdjustYaw = true;
  this.moveRiderWithTurret = true;
  this.shouldRiderSit = true;
  this.turretRotationMax = 45;
  this.riderForwardsOffset = 1.275f;
  this.riderVerticalOffset = 0.7f;
  this.turretVerticalOffset = (34.f + 67.5f + 24.0f+9.5f)*0.0625f;
  }

@Override
public String getTextureForMaterialLevel(int level)
  {
  switch(level)
    {
    case 0:
    return Config.texturePath + "models/trebuchetMobile1.png";
    case 1:
    return Config.texturePath + "models/trebuchetMobile2.png";
    case 2:
    return Config.texturePath + "models/trebuchetMobile3.png";
    case 3:
    return Config.texturePath + "models/trebuchetMobile4.png";
    case 4:
    return Config.texturePath + "models/trebuchetMobile5.png";
    default:
    return Config.texturePath + "models/trebuchetMobile1.png";
    }
  }
}
