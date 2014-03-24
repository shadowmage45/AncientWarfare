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

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.template.rule.TemplateRuleEntity;
import shadowmage.ancient_structures.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.gates.types.Gate;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class TemplateRuleGates extends TemplateRuleEntity
{

String gateType;
BlockPosition pos1 = new BlockPosition();
BlockPosition pos2 = new BlockPosition();

/**
 * scanner-constructor.  called when scanning an entity.
 * @param world the world containing the scanned area
 * @param entity the entity being scanned
 * @param turns how many 90' turns to rotate entity for storage in template
 * @param x world x-coord of the enitty (floor(posX)
 * @param y world y-coord of the enitty (floor(posY)
 * @param z world z-coord of the enitty (floor(posZ)
 */
public TemplateRuleGates(World world, Entity entity, int turns, int x, int y, int z)
  {
  super(world, entity, turns, x, y, z);
  EntityGate gate = (EntityGate)entity;
  BlockPosition pos1 = gate.pos1.copy();
  pos1.offset(-x, -y, -z);
  BlockPosition pos2 = gate.pos2.copy();
  pos2.offset(-x, -y, -z);
  
  BlockTools.rotateAroundOrigin(pos1, turns);
  BlockTools.rotateAroundOrigin(pos2, turns);
  this.pos1 = pos1;
  this.pos2 = pos2;
  
  this.gateType = Gate.getGateNameFor(gate);
  }

public TemplateRuleGates()
  {
  
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  BlockPosition p1 = pos1.copy();
  BlockPosition p2 = pos2.copy();
    
  BlockTools.rotateAroundOrigin(p1, turns);
  BlockTools.rotateAroundOrigin(p2, turns);
  
  p1.offset(x, y, z);
  p2.offset(x, y, z);
  
  int face = (2 + turns)%4;
  
  BlockPosition min = BlockTools.getMin(pos1, pos2);
  BlockPosition max = BlockTools.getMax(pos1, pos2);
  for(int x1 = min.x; x1 <=max.x ;x1++)
    {
    for(int y1 = min.y; y1 <=max.y ;y1++)
      {
      for(int z1 = min.z; z1 <=max.z ;z1++)
        {
        world.setBlockToAir(x1, y1, z1);
        }
      }
    }
    
  EntityGate gate = Gate.constructGate(world, p1, p2, Gate.getGateByName(gateType), (byte)face);
  
  if(gate!=null)
    {
    world.spawnEntityInWorld(gate);
    }
  else
    {
    AWLog.logDebug("returned null gate for construction from construct gate...");
    }  
  }

@Override
public void parseRuleData(NBTTagCompound tag)
  {
  gateType = tag.getString("gateType");
  NBTTagCompound pTag = tag.getCompoundTag("pos1");
  pos1.read(pTag);
  pTag = tag.getCompoundTag("pos2");
  pos2.read(pTag);
  }

@Override
public void writeRuleData(NBTTagCompound tag)
  {
  tag.setString("gateType", gateType);
  NBTTagCompound pTag = new NBTTagCompound();
  pos1.writeToNBT(pTag);
  tag.setCompoundTag("pos1", pTag);
  pTag = new NBTTagCompound();
  pos2.writeToNBT(pTag);
  tag.setCompoundTag("pos2", pTag);
  }

@Override
public void addResources(List<ItemStack> resources)
  {

  }

@Override
public boolean shouldPlaceOnBuildPass(World world, int turns, int x, int y, int z, int buildPass)
  {
  return buildPass==3;
  }

}
