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
short vehicleType;
byte armorFrontTypes[];
byte armorMidTypes[];
byte armorRearTypes[];
byte upgradeFrontTypes[];
byte upgradeMidTypes[];
byte upgradeRearTypes[];
byte[] ammoTypes = new byte[]{-1,-1,-1,-1,-1,-1};

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
      rule.ruleNumber = Short.parseShort(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("type"))
      {
      rule.ruleNumber = Short.parseShort(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("armorfront"))
      {
      rule.armorFrontTypes = StringTools.parseByteArray(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("armormid"))
      {
      rule.armorMidTypes = StringTools.parseByteArray(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("armorrear"))
      {
      rule.armorRearTypes = StringTools.parseByteArray(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("upgrade1"))
      {
      rule.upgradeFrontTypes = StringTools.parseByteArray(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("upgrade2"))
      {
      rule.upgradeMidTypes = StringTools.parseByteArray(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("upgrade3"))
      {
      rule.upgradeRearTypes = StringTools.parseByteArray(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("ammo1"))
      {
      rule.ammoTypes[0]=Byte.parseByte(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("ammo2"))
      {
      rule.ammoTypes[1]=Byte.parseByte(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("ammo3"))
      {
      rule.ammoTypes[2]=Byte.parseByte(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("ammo4"))
      {
      rule.ammoTypes[3]=Byte.parseByte(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("ammo5"))
      {
      rule.ammoTypes[4]=Byte.parseByte(line.split("=")[1]);      
      }
    if(line.toLowerCase().startsWith("ammo6"))
      {
      rule.ammoTypes[5]=Byte.parseByte(line.split("=")[1]);      
      }
    }  
  if(rule.ruleNumber>=0)
    {
    return rule;
    }
  return null;
  }
}
