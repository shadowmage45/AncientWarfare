/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.npcs.helpers.targeting;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AITargetEntry
{

/**
 * kind of sloppy, but allows for both TileEntity and Entity targets to share the same underlying targeting system
 */
private Class targetClass = null;
public int priority = 0;
boolean isEntityTarget = false;
TargetType typeName;
public float maxTargetRange = Config.npcAISearchRange;
protected NpcBase npc;

public AITargetEntry(NpcBase owner, TargetType typeName, Class clz, int priority, boolean isEntityTarget, float maxTargetRange)
  {
  this.typeName = typeName;
  this.targetClass = clz;
  this.priority = priority;
  this.isEntityTarget = isEntityTarget;
  this.maxTargetRange = maxTargetRange;
  this.npc = owner;
  }  

public TargetType getTypeName()
  {
  return this.typeName;
  }

public boolean isTarget(Entity ent)
  {
  return ent!=null && targetClass!=null && targetClass.isAssignableFrom(ent.getClass());
  }

public boolean isTarget(TileEntity te)
  {
  return te!=null && targetClass!=null && targetClass.isAssignableFrom(te.getClass());
  }
/**
 * TODO
 * @param world
 * @param x
 * @param y
 * @param z
 * @return
 */
public boolean isTarget(World world, int x, int y, int z)
  {
  return !isEntityTarget;
  }

public boolean isEntityTarget()
  {
  return this.isEntityTarget;
  }

public boolean isTileTarget()
  {
  return !this.isEntityTarget;
  }

public int getAggroAdjustment(AIAggroTargetWrapper target)
  {
  return Config.npcAITicks;
  }

}
