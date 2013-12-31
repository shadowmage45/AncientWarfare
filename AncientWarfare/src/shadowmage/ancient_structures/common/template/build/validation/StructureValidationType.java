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
package shadowmage.ancient_structures.common.template.build.validation;

public enum StructureValidationType
{
GROUND("ground", StructureValidatorGround.class),
UNDERGROUND("underground", StructureValidatorUnderground.class),
SKY("sky", StructureValidatorSky.class),
WATER("water", StructureValidatorWater.class),
UNDERWATER("underwater", StructureValidatorUnderwater.class),
HARBOR("harbor", zStructureValidatorHarbor.class), 
ISLAND("island", StructureValidatorIsland.class);

private String name;
private Class<? extends StructureValidator> validatorClass;

StructureValidationType(String name, Class<? extends StructureValidator> validatorClass)
  {
  this.name = name;
  this.validatorClass = validatorClass;
  }

public String getName()
  {
  return name;
  }

public StructureValidator getValidator()
  {
  try
    {
    return validatorClass.newInstance();
    } 
  catch (InstantiationException e)
    {
    e.printStackTrace();
    } 
  catch (IllegalAccessException e)
    {
    e.printStackTrace();
    }
  return null;
  }

public static StructureValidationType getTypeFromName(String name)
  {
  if(name==null){return null;}
  name = name.toLowerCase();
  if(name.equals(GROUND.name)){return GROUND;}
  else if(name.equals(UNDERGROUND.name)){return UNDERGROUND;}
  else if(name.equals(SKY.name)){return SKY;}
  else if(name.equals(WATER.name)){return WATER;}
  else if(name.equals(UNDERWATER.name)){return UNDERWATER;}
  else if(name.equals(HARBOR.name)){return HARBOR;}
  else if(name.equals(ISLAND.name)){return ISLAND;}
  return null;
  }

/**
 * validation types:
 * ground:
 *    validate border edge blocks for depth and leveling
 *    validate border target blocks
 * 
 * underground:
 *    validate min/max overfill height is met
 *    validate border target blocks
 *    
 * water:
 *    validate water depth along edges
 *    
 * underwater:
 *    validate min/max water depth at placement x/z
 *    validate border edge blocks for depth and leveling
 *    
 * sky:
 *    validate min flying height along edges
 * 
 * harbor:
 *    validate edges--front all land, sides land/water, back all water. validate edge-depth and leveling *    
 * 
 * island:
 *    validate min/max water depth at placement x/z
 *    validate border edge blocks for depth and leveling
 *   
 */

}
