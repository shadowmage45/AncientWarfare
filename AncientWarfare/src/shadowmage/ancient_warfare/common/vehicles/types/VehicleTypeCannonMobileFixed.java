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

import shadowmage.ancient_warfare.common.research.ResearchGoal;

public class VehicleTypeCannonMobileFixed extends VehicleTypeCannon
{

/**
 * @param typeNum
 */
public VehicleTypeCannonMobileFixed(int typeNum)
  {
  super(typeNum);
  this.displayName = "Cannon Mobile Fixed";
  this.displayTooltip.add("Cannon Firing Mechanism");
  this.displayTooltip.add("Wheeled");
  this.displayTooltip.add("No Turret");
  this.width = 1.9f;
  this.height = 1.5f;
  this.baseForwardSpeed = 3.5f*0.05f;
  this.baseStrafeSpeed = 1.5f;
  this.turretVerticalOffset = 14.5f*0.0625f;
  this.shouldRiderSit = false;
  this.riderVerticalOffset = 0.5f;
  this.riderForwardsOffset = -2.45f;
  this.baseMissileVelocityMax = 38.f;  
  this.addNeededResearch(0, ResearchGoal.vehicleMobility1);
  this.addNeededResearch(1, ResearchGoal.vehicleMobility1);
  this.addNeededResearch(2, ResearchGoal.vehicleMobility1);
  this.addNeededResearch(3, ResearchGoal.vehicleMobility2);
  this.addNeededResearch(4, ResearchGoal.vehicleMobility2); 
  }

}
