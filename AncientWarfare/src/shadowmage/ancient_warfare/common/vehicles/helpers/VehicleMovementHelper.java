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
package shadowmage.ancient_warfare.common.vehicles.helpers;

import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleMovementHelper
{
private VehicleBase vehicle;
private byte forwardInput = 0;
private byte strafeInput = 0;
private float forwardMotion = 0;
private float strafeMotion = 0;

boolean hasInput = false;

public VehicleMovementHelper (VehicleBase veh)
  {
  this.vehicle = veh;
  }

public void setForwardInput(byte in)
  {
  this.hasInput = true;
  this.forwardInput = in;
  }

public void setStrafeInput(byte in)
  {
  this.hasInput = true;
  this.strafeInput = in;
  }


/**
 * called every tick from vehicle onUpdate
 */
public void onMovementTick()
  {
  if(this.hasInput)    
    {
    //update forwardMotion and strafeMotion from input...
    }
  
  this.hasInput = false;//reset input flag at end of every move tick
  }

}
