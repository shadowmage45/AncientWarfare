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
package shadowmage.ancient_warfare.common.vehicles;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import net.minecraft.world.World;

public class VehicleCatapult extends VehicleBase
{
static boolean staticBlockCalled = false;

/**
 * @param par1World
 */
public VehicleCatapult(World par1World)
  {
  super(par1World);
  this.vehicleType = CATAPULT;
  }

/**
 * load ammo types for this vehicle...
 */
static
  {
  if(staticBlockCalled==true)
    {
    Config.logDebug("static block being called MORE THAN ONCE");
    }
  staticBlockCalled = true;  
  }

}
