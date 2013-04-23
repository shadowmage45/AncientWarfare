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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class WorkPoint extends WayPoint
{

protected Entity ent;//target entity
private WeakReference<NpcBase> worker = new WeakReference<NpcBase>(null);
protected int totalHarvestHits = 1;
protected int currentHarvestHits = 0;
protected boolean singleUse = false;

public WorkPoint(int x, int y, int z, TargetType type)
  {
  super(x,y,z, type);
  }

public WorkPoint(int x, int y, int z, int side, TargetType type)
  {
  super(x,y,z,side, type);
  }

public WorkPoint(Entity ent, TargetType type)
  {
  super(type);
  this.ent = ent;
  this.type = type;
  }

/**
 * @param compoundTag
 */
public WorkPoint(NBTTagCompound compoundTag)
  {
  super(compoundTag);
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

public void resetHarvestTicks()
  {
  this.currentHarvestHits = 0;
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

}
