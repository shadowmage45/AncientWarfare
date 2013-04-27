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

import net.minecraft.util.Icon;
import shadowmage.ancient_warfare.common.config.Config;

public class VehicleTypeBallistaMobile extends VehicleTypeBallista
{

  /**
   * @param typeNum
   */
  public VehicleTypeBallistaMobile(int typeNum)
    {
    super(typeNum);
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
    this.displayName = "Ballista Mobile Fixed";
    this.displayTooltip = "A ballista mounted on a wheeled frame.";
    }
  
@Override
public String getTextureForMaterialLevel(int level)
  {
  switch(level)
    {
    case 0:
    return Config.texturePath + "models/ballistaMobile1.png";
    case 1:
    return Config.texturePath + "models/ballistaMobile2.png";
    case 2:
    return Config.texturePath + "models/ballistaMobile3.png";
    case 3:
    return Config.texturePath + "models/ballistaMobile4.png";
    case 4:
    return Config.texturePath + "models/ballistaMobile5.png";
    default:
    return Config.texturePath + "models/ballistaMobile1.png";
    }
  }


}
