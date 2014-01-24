/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
package shadowmage.ancient_vehicles.common.vehicle;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.minecraft.world.World;

public class VehicleType
{

public static HashMap<String, VehicleType> vehicleTypes = new HashMap<String, VehicleType>();

String name;//translation key/unique registered name for this vehicle type
Object movementType;//enum / flag for movement type for this vehicle
Class firingHelper;//reference to the firing helper type for this vehicle, for constructing new vehicle entities
int maxHealth;//max health for this vehicle type
float mass;//un-adjusted mass for this vehicle
float thrust;//used with mass to determine acceleration/handling for this vehicle
float firePower;//used with ammo mass to determine final projectile velocity

String renderId;//used by rendering to..well..render
String modelTexture;//used by rendering to bind the proper texture
String modelId;//used by rendering to select the proper model

public VehicleType(String name)
  {
  this.name = name;
  vehicleTypes.put(name, this);
  }

public static final VehicleType getVehicleType(String name)
  {
  return vehicleTypes.get(name);
  }

public String getName(){return name;}

public Object getNewFiringHelper(EntityVehicle vehicle)
  {
  try
    {
    return firingHelper.getDeclaredConstructor(EntityVehicle.class).newInstance(vehicle);
    } 
  catch (InstantiationException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } 
  catch (IllegalAccessException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } 
  catch (IllegalArgumentException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } 
  catch (InvocationTargetException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } 
  catch (NoSuchMethodException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } 
  catch (SecurityException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    }
  return null;
  }

public static EntityVehicle createVehicle(World world, String typeName)
  {
  VehicleType type = VehicleType.getVehicleType(typeName);  
  EntityVehicle entity = new EntityVehicle(world).setVehicleType(type);
  entity.setFiringHelper(type.getNewFiringHelper(entity));
  return entity;
  }

public static VehicleType parseFromCSV(String[] csv)
  {
  if(csv==null || csv.length<6)
    {
    throw new IllegalArgumentException("Could not parse vehicle type from:\n+"+csv+"\ndid not contain enough elements to parse");
    }
  VehicleType type = null;
  String name = csv[0];
  String model = csv[1];
  String render = csv[2];
  String texture = csv[3];
  String fireHelper = csv[4];
  String moveType = csv[5];
  
  type = new VehicleType(name);
  type.firingHelper = VehicleRegistry.getFiringHelperClass(fireHelper);
  type.modelId = model;
  type.renderId = render;
  type.modelTexture = texture;
  type.movementType = VehicleRegistry.getMoveType(moveType);
  /**
   * TODO read the rest of vehicle-type stats (need to determine what the stats will be)
   */
  return type;
  }
}
