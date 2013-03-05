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
package shadowmage.ancient_warfare.common.registry;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleCatapult;
import shadowmage.ancient_warfare.common.vehicles.types.VehicleTypeBase;
import shadowmage.ancient_warfare.common.vehicles.types.VehicleTypeCatapult;

public class VehicleRegistry
{

private HashMap<Integer, IVehicleType> vehicleTypes = new HashMap<Integer, IVehicleType>();
private HashMap<IVehicleType, Class <? extends VehicleBase>> vehicleClasses = new HashMap<IVehicleType, Class<? extends VehicleBase>>();

public static final IVehicleType DUMMY_VEHICLE = new VehicleTypeBase(-1);
public static IVehicleType CATAPULT = new VehicleTypeCatapult(0);

private VehicleRegistry(){}
private static VehicleRegistry INSTANCE;
public static VehicleRegistry instance()
  {
  if(INSTANCE==null){INSTANCE = new VehicleRegistry();}
  return INSTANCE;
  }

/**
 * register method called from AWCore to register all included vehicles
 */
public void registerVehicles()
  {
  this.registerVehicle(VehicleCatapult.class, "AW_Catapult", CATAPULT);
  }

public void registerVehicle(Class <? extends VehicleBase> clz, String entName, IVehicleType type)
  {
  AWEntityRegistry.registerEntity(clz, entName, 130, 3, false);
  this.vehicleTypes.put(type.getGlobalVehicleType(), type);
  this.vehicleClasses.put(type, clz);
  }

public VehicleBase getVehicleForType(World world, int type)
  {
  if(this.vehicleTypes.containsKey(type))
    {
    try
      {
      return this.vehicleClasses.get(this.vehicleTypes.get(type)).getDeclaredConstructor(World.class).newInstance(world);
      } 
    catch (InstantiationException e)
      {
      e.printStackTrace();
      } 
    catch (IllegalAccessException e)
      {
      e.printStackTrace();
      } 
    catch (IllegalArgumentException e)
      {
      e.printStackTrace();
      } 
    catch (InvocationTargetException e)
      {
      e.printStackTrace();
      } 
    catch (NoSuchMethodException e)
      {
      e.printStackTrace();
      } 
    catch (SecurityException e)
      {
      e.printStackTrace();
      }
    }
  return null;
  }


}
