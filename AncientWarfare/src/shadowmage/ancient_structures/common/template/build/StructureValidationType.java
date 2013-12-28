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
package shadowmage.ancient_structures.common.template.build;

public enum StructureValidationType
{
GROUND("ground", StructureValidatorGround.class),
UNDERGROUND("underground", null),
SKY("sky", null),
WATER("water", null),
UNDERWATER("underwater", null),
ISLAND("island", null),
HARBOR("harbor", null);

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
  else if(name.equals("underwater")){return UNDERWATER;}
  else if(name.equals("island")){return ISLAND;}
  else if(name.equals("harbor")){return HARBOR;}
  return null;
  }
}
