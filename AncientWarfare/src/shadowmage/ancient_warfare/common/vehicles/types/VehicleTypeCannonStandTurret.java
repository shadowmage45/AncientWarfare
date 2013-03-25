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

public class VehicleTypeCannonStandTurret extends VehicleTypeCannon
{

/**
 * @param typeNum
 */
public VehicleTypeCannonStandTurret(int typeNum)
  {
  super(typeNum);
  this.displayName = "Cannon Stand Turret";
  this.displayTooltip = "A turret mounted cannon mounted on a simple base.";
  this.width = 1.2f;
  this.height = 1.6f;
  this.turretRotationMax=45;
  this.moveRiderWithTurret = true;
  this.canAdjustYaw = true;
  this.shouldRiderSit = true;
  this.baseStrafeSpeed = 0.5f;
  this.baseForwardSpeed = 0.f;
  this.baseMissileVelocityMax = 40.f;   
  this.turretVerticalOffset = 13.5f*0.0625f;
  this.riderVerticalOffset = 0.55f;
  this.riderForwardsOffset = -1.25f;
  }

}
