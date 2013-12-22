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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.rule.TemplateRuleEntity;

public class TemplateRuleVanillaEntity extends TemplateRuleEntity
{

String mobID;
float xOffset;
float zOffset;
float rotation;

public TemplateRuleVanillaEntity(World world, Entity entity, int turns, int x, int y, int z)
  {
  this.mobID = EntityList.getStringFromID(EntityList.getEntityID(entity));
  rotation = (entity.rotationYaw + 90.f*turns)%360.f;
  float x1, z1;
  x1 = (float) (entity.posX%1.d);
  z1 = (float) (entity.posZ%1.d);
  xOffset = BlockTools.rotateFloatX(x1, z1, turns);
  zOffset = BlockTools.rotateFloatZ(x1, z1, turns);
  xOffset = x1;
  zOffset = z1;
  }

public TemplateRuleVanillaEntity()
  {
  
  }

@Override
public boolean shouldReuseRule(World world, Entity entity, int x, int y, int z)
  {
  return mobID.equals(EntityList.getEntityString(entity));
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  Entity e = EntityList.createEntityByName(mobID, world);
  float x1 = BlockTools.rotateFloatX(xOffset, zOffset, turns);
  float z1 = BlockTools.rotateFloatZ(xOffset, zOffset, turns);
  float yaw = (rotation + 90.f * turns)%360.f;
  e.setPosition(x+x1, y, z+z1);
  e.rotationYaw = yaw;
  world.spawnEntityInWorld(e);
  }

@Override
public void parseRuleData(List<String> ruleData)
  {
  for(String line : ruleData)
    {
    if(line.toLowerCase().startsWith("mobid="))
      {
      mobID = StringTools.safeParseString("=", line);
      }
    else if(line.toLowerCase().startsWith("offset="))
      {
      float[] offsets = StringTools.safeParseFloatArray("=", line); 
      xOffset = offsets[0];
      zOffset = offsets[1];
      }
    else if(line.toLowerCase().startsWith("rotation="))
      {
      rotation = StringTools.safeParseFloat("=", line);
      }
    }
  }

@Override
public void writeRuleData(BufferedWriter out) throws IOException
  {
  out.write("mobID="+mobID);
  out.newLine();
  out.write("offset="+StringTools.getCSVStringForArray(xOffset, zOffset));
  out.newLine();
  out.write("rotation="+rotation);
  out.newLine();
  }

@Override
public boolean shouldPlaceOnBuildPass(World world, int turns, int x, int y, int z, int buildPass)
  {
  return buildPass==3;
  }

@Override
public void addResources(List<ItemStack> resources)
  {
  
  }

}
