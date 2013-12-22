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
import java.util.List;

import shadowmage.ancient_framework.common.utils.BlockTools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class TemplateRuleEntityLogic extends TemplateRuleVanillaEntity
{

NBTTagCompound tag = new NBTTagCompound();

public TemplateRuleEntityLogic(){}

public TemplateRuleEntityLogic(World world, Entity entity, int turns, int x, int y, int z)
  {
  super(world, entity, turns, x, y, z);
  entity.writeToNBT(tag);
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  Entity e = EntityList.createEntityByName(mobID, world);
  float xo = BlockTools.rotateFloatX(xOffset, zOffset, turns);
  float zo = BlockTools.rotateFloatZ(xOffset, zOffset, turns);
  NBTTagList list = tag.getTagList("Pos");
  if(list.tagCount()>=3)
    {
    ((NBTTagDouble)list.tagAt(0)).data = x + xo;
    ((NBTTagDouble)list.tagAt(1)).data = y;
    ((NBTTagDouble)list.tagAt(2)).data = z + zo;
    e.readFromNBT(tag);
    }
  else
    {
    e.setPosition(x+0.5d, y, z+0.5d);
    }
  world.spawnEntityInWorld(e);
  }

@Override
public void parseRuleData(List<String> ruleData)
  {
  super.parseRuleData(ruleData);
  tag = readTag(ruleData);
  }

@Override
public void writeRuleData(BufferedWriter out) throws IOException
  {
  super.writeRuleData(out);
  writeTag(out, tag);
  }



}
