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
package shadowmage.ancient_structures.api;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class TemplateRuleEntity extends TemplateRule
{

/**
 * set directly from the scanner after the rule has been initialized
 */
public int x, y, z;

/**
 * scanner-constructor.  called when scanning an entity.
 * @param world the world containing the scanned area
 * @param entity the entity being scanned
 * @param turns how many 90' turns to rotate entity for storage in template
 * @param x world x-coord of the enitty (floor(posX)
 * @param y world y-coord of the enitty (floor(posY)
 * @param z world z-coord of the enitty (floor(posZ)
 */
public TemplateRuleEntity(World world, Entity entity, int turns, int x, int y, int z)
  {

  }

/**
 * load-from disc constructor
 */
public TemplateRuleEntity()
  {
  
  }

public final void writeRule(BufferedWriter out) throws IOException
  {  
  out.write("position="+StringTools.getCSVStringForArray(new int[]{x,y,z}));
  out.newLine();
  super.writeRule(out);
  }

public final void parseRule(int ruleNumber, List<String> lines)
  {
  this.ruleNumber = ruleNumber;
  for(String line : lines)
    {
    if(line.toLowerCase().startsWith("position="))
      {
      int[] pos = StringTools.safeParseIntArray("=", line);
      x = pos[0];
      y = pos[1];
      z = pos[2];
      break;
      }
    }
  NBTTagCompound tag = readTag(lines);
  parseRuleData(tag);
  }

}
