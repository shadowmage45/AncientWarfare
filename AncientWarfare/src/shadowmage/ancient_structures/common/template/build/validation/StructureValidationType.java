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
SUBMERGED("underwater", StructureValidatorSubmerged.class),
HARBOR("harbor", StructureValidatorHarbor.class);

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
  if(name.equals("ground")){return GROUND;}
  else if(name.equals("underground")){return UNDERGROUND;}
  else if(name.equals("sky")){return SKY;}
  else if(name.equals("water")){return WATER;}
  else if(name.equals("underwater")){return SUBMERGED;}
  else if(name.equals("harbor")){return HARBOR;}
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
 * sky:
 *    validate min flying height along edges
 *    template should have no ground/land in it (unless desired)
 *    
 * water:
 *    validate water depth along edges
 *    
 * submerged (previously underwater/island):
 *    validate min/max water depth at placement x/z
 *    validate border edge blocks for depth and leveling
 * 
 * harbor:
 *    validate edges--front all land, sides land/water, back all water. validate edge-depth and leveling
 *    template should have water along back edge, land on front edge, structure/indetermined on sides
 *   
 */

}
