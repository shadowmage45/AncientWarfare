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
package shadowmage.ancient_structures.common.template.plugin.default_plugins.entity_rules;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.NBTTools;
import shadowmage.ancient_framework.common.utils.StringTools;

public class TemplateRuleEntityHanging extends TemplateRuleVanillaEntity
{

NBTTagCompound tag = new NBTTagCompound();
int direction;

BlockPosition hangTarget = new BlockPosition();//cached location for use during placement

public TemplateRuleEntityHanging(World world, Entity entity, int turns, int x, int y, int z)
  {
  super(world, entity, turns, x, y, z);
  EntityHanging hanging = (EntityHanging)entity;  
  entity.writeToNBT(tag);
  this.direction = (hanging.hangingDirection + turns)%4;
  tag.removeTag("UUIDMost");
  tag.removeTag("UUIDLeast");
  }

public TemplateRuleEntityHanging()
  {
  
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  int direction = (this.direction+turns)%4;
  hangTarget.reassign(x, y, z);
  hangTarget.offsetForHorizontalDirection((direction+2)%4);
  Entity e = EntityList.createEntityByName(mobID, world);
  tag.setByte("Direction", (byte)direction);
  tag.setInteger("TileX", hangTarget.x);
  tag.setInteger("TileY", hangTarget.y);
  tag.setInteger("TileZ", hangTarget.z);
  e.readFromNBT(tag);  
  world.spawnEntityInWorld(e);
  }

@Override
public void parseRuleData(List<String> ruleData)
  {
  super.parseRuleData(ruleData);
  tag = readTag(ruleData);
  for(String line : ruleData)
    {
    if(line.toLowerCase().startsWith("direction="))
      {
      this.direction = StringTools.safeParseInt("=", line);
      }
    }  
  }

@Override
public void writeRuleData(BufferedWriter out) throws IOException
  {
  super.writeRuleData(out);
  out.write("direction="+direction);
  out.newLine();
  writeTag(out, tag);
  }

}
