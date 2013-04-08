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
package shadowmage.ancient_warfare.common.structures.data.rules;

import java.util.Iterator;
import java.util.List;

import shadowmage.ancient_warfare.common.utils.StringTools;

public class VehicleRule
{
public short ruleNumber;
public short vehicleType;
public byte[] armorFrontTypes;
public byte[] upgradeTypes;
public byte[] ammoTypes;

private VehicleRule()
  {  
  }

public static VehicleRule parseRule(List<String> ruleLines)
  {
  String line;
  VehicleRule rule = new VehicleRule();
  Iterator<String> it = ruleLines.iterator();
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("number"))
      {
      rule.ruleNumber = StringTools.safeParseShort("=", line);      
      }
    if(line.toLowerCase().startsWith("type"))
      {
      rule.vehicleType = StringTools.safeParseShort("=", line);     
      }
    if(line.toLowerCase().startsWith("armorfront"))
      {
      rule.armorFrontTypes = StringTools.safeParseByteArray("=", line);      
      }    
    if(line.toLowerCase().startsWith("upgrades"))
      {
      rule.upgradeTypes = StringTools.safeParseByteArray("=", line);
      }   
    if(line.toLowerCase().startsWith("ammos"))
      {
      rule.ammoTypes = StringTools.safeParseByteArray("=", line);      
      }    
    }  
  if(rule.ruleNumber>=0)
    {
    return rule;
    }
  return null;
  }
}
