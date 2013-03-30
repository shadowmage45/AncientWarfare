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
package shadowmage.ancient_warfare.common.soldiers.helpers.targeting;

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AITargetEntryMountableVehicle extends AITargetEntry
{

int vehicleType = -1;

public AITargetEntryMountableVehicle(NpcBase npc, int vehicleType, int range)
  {
  super(npc, "mount", VehicleBase.class, 0, true, range);
  this.vehicleType = vehicleType;
  }

@Override
public boolean isTarget(Entity ent)
  {
  if(ent instanceof VehicleBase)
    {
    VehicleBase vehicle = (VehicleBase)ent;
    if(vehicle.isMountable() && vehicle.riddenByEntity==null)
      {
      if(this.vehicleType==-1)
        {
        return true;
        }
      else if(this.vehicleType == vehicle.vehicleType.getGlobalVehicleType())
        {
        return true;
        }
      }
    }
  return false;
  }


}
