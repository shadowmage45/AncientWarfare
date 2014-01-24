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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import shadowmage.ancient_framework.AWFramework;
import shadowmage.ancient_framework.common.item.AWItemBase;
import shadowmage.ancient_framework.common.registry.ObjectRegistry;
import shadowmage.ancient_vehicles.AWVehicles;
import shadowmage.ancient_vehicles.common.config.AWVehicleStatics;
import shadowmage.ancient_vehicles.common.item.AWVehiclesItemLoader;

public class VehicleRegistry
{

private static HashMap<String, Class> firingHelpers = new HashMap<String, Class>();
private static HashMap<String, Object> moveTypes = new HashMap<String, Object>();

public static void loadVehicles()
  {
  List<VehicleType> types = loadFromDefinition(AWVehicleStatics.vehicleDefinitionsFile);
  AWVehicles.instance.logDebug("loaded: "+types.size() + " vehicle definitions");
  for(VehicleType t : types)
    {
    if(t.isSuvivalEnabled())
      {
      //reg recipes and items
      AWItemBase item = AWVehiclesItemLoader.vehicleSpawner;
      ObjectRegistry reg = AWFramework.instance.objectRegistry;
      //reg.addDescription(item, t.name, itemDamage, tooltipKey, itemIcon);
      /**
       * TODO -- I don't think I need to really reg-the extra types, merely add the display stacks for creative (if reg for creative)
       * and in the item, I need to override getName() to return the name from the nbt-tag
       * 
       * should probably add a helper method into the vehicleType to return a properly formatted itemStack for that vehicle type
       * to use in display stacks/recipe results/etc
       */
      }
    else if(t.isSuvivalEnabled())
      {
      //reg only items
      }
    }
  }

public static Class getFiringHelperClass(String name)
  {
  return firingHelpers.get(name);
  }

public static Object getMoveType(String name)
  {
  return moveTypes.get(name);
  }

private static List<VehicleType> loadFromDefinition(String path)
  {
  InputStream is = AWVehicles.instance.getClass().getResourceAsStream(path);
  if(is==null){return Collections.emptyList();}  
  List<VehicleType> types = new ArrayList<VehicleType>();
  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
  String line;
  String[] lineBits;
  VehicleType type;
  //#name, model, render, texture, firingHelper, moveType, <other stats--movement, inventory size, etc>
  try
    {
    while((line = reader.readLine())!=null)
      {
      if(line.startsWith("#")){continue;}
      lineBits = line.split(",", -1);
      type = VehicleType.parseFromCSV(lineBits);
      if(type!=null)
        {
        types.add(type);
        }
      }
    } 
  catch (IOException e1)
    {
    e1.printStackTrace();
    }  
  try
    {
    reader.close();
    } 
  catch (IOException e)
    {
    e.printStackTrace();
    }
  return types;
  }

}
