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
package shadowmage.ancient_warfare.client.registry;

import java.util.HashMap;

import shadowmage.ancient_warfare.client.render.RenderCatapult;
import shadowmage.ancient_warfare.client.render.RenderVehicleBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

/**
 * handle render information
 * @author Shadowmage
 *
 */
public class RenderRegistry
{

private RenderRegistry(){}
private static RenderRegistry INSTANCE;
public static RenderRegistry instance()
  {
  if(INSTANCE==null){INSTANCE = new RenderRegistry();}
  return INSTANCE;
  }

private HashMap<Integer, RenderVehicleBase> vehicleRenders = new HashMap<Integer, RenderVehicleBase>();

public void loadRenders()
  {
  this.addVehicleRender(0, new RenderCatapult());
  }

/**
 * API call for adding vehicles render types
 * @param vehType
 * @param render
 */
public void addVehicleRender(int vehType, RenderVehicleBase render)
  {
  this.vehicleRenders.put(vehType, render);
  }

public RenderVehicleBase getRenderForVehicle(int vehicle)
  {
  return this.vehicleRenders.get(vehicle);
  }

public void doVehicleRender(int vehicleType, VehicleBase veh, double x, double y, double z, float yaw, float tick)
  {
  if(this.vehicleRenders.containsKey(vehicleType))
    {
    this.vehicleRenders.get(vehicleType).renderVehicle(veh, x, y, z, yaw, tick);
    }
  }

}
