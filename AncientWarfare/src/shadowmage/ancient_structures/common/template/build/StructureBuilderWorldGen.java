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

import net.minecraft.world.World;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class StructureBuilderWorldGen extends StructureBuilder
{

public StructureBuilderWorldGen(World world, StructureTemplate template, int face, int x, int y, int z)
  {
  super(world, template, face, x, y, z);
  }

@Override
public void instantConstruction()
  {
  /**
   * TODO clearing / fill
   */
  super.instantConstruction();
  }

protected void placeRule(TemplateRule rule)
  {  
  try
    {
    if(rule.shouldPlaceOnBuildPass(world, turns, destination.x, destination.y, destination.z, currentPriority))
      {
      rule.handlePlacement(world, turns, destination.x, destination.y, destination.z);    
      }
    }
  catch(Exception e)
    {
    
    }
  }
}
