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

import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleTypeBallistaStand extends VehicleTypeBallista
{
/**
 * @param typeNum
 */
public VehicleTypeBallistaStand(int typeNum, Class <? extends VehicleBase> vehicleClass)
  {
  super(typeNum, vehicleClass);
  this.baseMissileVelocityMax = 45.f;//stand versions should have higher velocity, as should fixed version--i.e. mobile turret should have the worst of all versions   
  this.width = 2;
  this.height = 2;  
  
  this.armorBaySize = 4;
  this.upgradeBaySize = 4;
    
  this.missileVerticalOffset = 1.2f;
  this.missileForwardsOffset = 1.f;
  this.riderForwardsOffset = -1.2f;
  this.riderVerticalOffset = 0.7f;
  this.shouldRiderSit = false;
  
  this.isDrivable = false;//adjust based on isMobile or not
  this.canAdjustYaw = true;//adjust based on hasTurret or not
  this.turretRotationMax=90.f;//adjust based on mobile/stand fixed (0), stand fixed(90'), or mobile or stand turret (360) 
  
  }

}
