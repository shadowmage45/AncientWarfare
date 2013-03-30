/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
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
package shadowmage.ancient_warfare.common.soldiers.helpers.targeting;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;

public class AITargetEntry
{

/**
 * kind of sloppy, but allows for both TileEntity and Entity targets to share the same underlying targeting system
 */
Class targetClass = null;
public int priority = 0;
boolean isEntityTarget = false;
String typeName = "";
public float maxTargetRange = Config.npcAISearchRange;
protected NpcBase npc;

public AITargetEntry(NpcBase owner, String typeName, Class clz, int priority, boolean isEntityTarget, float maxTargetRange)
  {
  this.typeName = typeName;
  this.targetClass = clz;
  this.priority = priority;
  this.isEntityTarget = isEntityTarget;
  this.maxTargetRange = maxTargetRange;
  this.npc = owner;
  }  

public String getTypeName()
  {
  return this.typeName;
  }

public boolean isTarget(Entity ent)
  {
  return targetClass.isAssignableFrom(ent.getClass());//ent.getClass().isAssignableFrom(entityClass);
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

//public boolean isTarget(Class clz)
//  {
//  return targetClass.isAssignableFrom(clz);//clz.isAssignableFrom(entityClass);
//  }

public boolean isEntityTarget()
  {
  return this.isEntityTarget;
  }

public boolean isTileTarget()
  {
  return !this.isEntityTarget;
  }

public int getAggroAdjustment(AIAggroEntry target)
  {
  return Config.npcAITicks;
  }

}
