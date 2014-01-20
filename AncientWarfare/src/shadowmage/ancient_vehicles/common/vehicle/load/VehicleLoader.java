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
package shadowmage.ancient_vehicles.common.vehicle.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shadowmage.ancient_framework.AWFramework;
import shadowmage.ancient_vehicles.common.vehicle.VehicleType;

public class VehicleLoader
{

public VehicleLoader()
  {
  // TODO Auto-generated constructor stub
  }

public List<VehicleType> loadFromDefinition()
  {
  InputStream is = AWFramework.instance.getClass().getResourceAsStream("assets/ancientwarfare/definitions/vehicles.def");
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
