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
package shadowmage.ancient_warfare.common.civics.worksite;

import java.lang.ref.WeakReference;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.WorkType;
import shadowmage.ancient_warfare.common.npcs.NpcBase;

public class WorkPoint
{

protected WorkType type;
protected Entity ent;//target entity
protected int x;
protected int y; 
protected int z;
private WeakReference<NpcBase> worker = new WeakReference<NpcBase>(null);
protected int totalHarvestHits = 1;
protected int currentHarvestHits = 0;
protected boolean singleUse = false;

public WorkPoint(int x, int y, int z, WorkType type)
  {
  this.x = x;
  this.y = y;
  this.z = z;
  this.type = type;
  }

public WorkPoint(Entity ent, WorkType type)
  {
  this.ent = ent;
  this.type = type;
  }

public WorkType getWorkType()
  {
  return this.type;
  }

/**
 * overridable method to determine if a point has work
 * resets internal canStart flag dependant upon if work is available at this point
 * @return
 */
public boolean hasWork(World world)
  {
  return true;
  }

public boolean canStart()
  {
  return this.worker==null;
  }

public void incrementHarvestHits()
  {
  this.currentHarvestHits++;
  }

/**
 * pretty much a hack to exit early from working a node
 */
public void setHarvestHitToMax()
  {
  this.currentHarvestHits = this.totalHarvestHits;
  }

/**
 * return if this goal is done (by action/hit count)
 * @return
 */
public boolean shouldFinish()
  {
  return this.currentHarvestHits>=this.totalHarvestHits;
  }

/**
 * used for single-use stuff, such as clearing/placing blocks (building/mining mostly)
 * @return
 */
public boolean isSingleUse()
  {
  return this.singleUse;
  }

/**
 * to be defined by subtypes, basic implimenataion only checks entity
 * @param world
 * @return
 */
public boolean isValidEntry(World world)
  {
  if(this.isEntityEntry())
    {
    return this.ent!=null;
    }
  return true;
  }

/**
 * clears worker * 
 */
public void setFinished()
  {
  this.setWorked(null);
  }

public void setWorked(NpcBase npc)
  {
  this.worker = new WeakReference<NpcBase>(npc);
  }

public boolean hasWorker()
  {
  return this.worker.get()!=null;
  }

public NpcBase getWorker()
  {
  return this.worker.get();
  }

public float posX()
  {
  return (float) (this.ent!=null ? ent.posX : x);
  }

public float posY()
  {
  return (float) (this.ent!=null ? ent.posY : y);
  }

public float posZ()
  {
  return (float) (this.ent!=null ? ent.posZ : z);
  }

public int floorX()
  {
  return this.ent==null? x : MathHelper.floor_double(posX());
  }

public int floorY()
  {
  return this.ent==null? y : MathHelper.floor_double(posY());
  }

public int floorZ()
  {
  return this.ent==null? z : MathHelper.floor_double(posZ());
  }

public Entity getEntityTarget()
  {
  return this.ent;
  }

public boolean isEntityEntry()
  {
  return this.ent!=null;
  }

@Override
public int hashCode()
  {
  final int prime = 31;
  int result = 1;
  result = prime * result + ((type == null) ? 0 : type.hashCode());
  result = prime * result + x;
  result = prime * result + y;
  result = prime * result + z;
  return result;
  }

@Override
public boolean equals(Object obj)
  {
  if (this == obj)
    return true;
  if (obj == null)
    return false;
  if (!(obj instanceof WorkPoint))
    return false;
  WorkPoint other = (WorkPoint) obj;
  if (type != other.type)
    return false;
  if (x != other.x)
    return false;
  if (y != other.y)
    return false;
  if (z != other.z)
    return false;
  return true;
  }

}
