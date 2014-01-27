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

import shadowmage.ancient_framework.common.utils.StringTools;
import net.minecraft.world.World;

public class VehicleType
{

public static final HashMap<String, VehicleType> vehicleTypes = new HashMap<String, VehicleType>();

String name;//translation key/unique registered name for this vehicle type
boolean survival;//available in survival through crafting (forces creative=true)
boolean creative;//available in creative in menu for ops/admins/creative play (loaded, but no recipe)
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
public boolean isSuvivalEnabled(){return survival;}
public boolean isCreativeEnabled(){return creative;}
public String getTextureName(){return modelTexture;}
public String getModelId(){return modelId;}
public String getRenderId(){return renderId;}

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
  if(csv==null || csv.length<8)
    {
    throw new IllegalArgumentException("Could not parse vehicle type from:\n+"+csv+"\ndid not contain enough elements to parse");
    }
  VehicleType type = null;
  type = new VehicleType(csv[0]);
  type.modelId = csv[1];
  type.renderId = csv[2];
  type.modelTexture = csv[3];
  type.firingHelper = VehicleRegistry.getFiringHelperClass(csv[4]);
  type.movementType = VehicleRegistry.getMoveType(csv[5]);
  type.survival = StringTools.safeParseBoolean(csv[6]);
  type.creative = StringTools.safeParseBoolean(csv[7]);
   
  /**
   * TODO read the rest of vehicle-type stats (need to determine what the stats will be)
   */
  return type;
  }
}
