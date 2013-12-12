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
package shadowmage.ancient_warfare.common.crafting;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.interfaces.ITEWorkSite;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public abstract class TEAWCraftingWorkSite extends TEAWCrafting implements ITEWorkSite
{

int maxWorkers = 1;
int broadcastDelayTicks = 0;
int workerValidationDelayTicks = 0;
protected boolean shouldBroadcast = false;
protected Set<IWorker> workers = Collections.newSetFromMap(new WeakHashMap<IWorker, Boolean>());
CivicWorkType workType = CivicWorkType.NONE;

/**
 * 
 */
public TEAWCraftingWorkSite()
  {
  }

@Override
public void updateEntity()
  {
  super.updateEntity();
  if(this.worldObj!=null && !this.worldObj.isRemote)
    {
    this.validateWorkers();
    this.broadcastWork(Config.npcAISearchRange);    
    }
  }

/************************************************WORKSITE METHODS*************************************************/
@Override
public void doWork(IWorker worker)
  {
  this.workProgress+=20;
  if(this.workProgress>=this.workProgressMax)
    {
    this.workProgress = this.workProgressMax;
    if(this.tryFinishCrafting())
      {
      this.tryStartCrafting();
      }
    }
  }

@Override
public boolean hasWork()
  {
  return this.isWorking && this.workProgress<this.workProgressMax;
  }

@Override
public boolean canHaveMoreWorkers(IWorker worker)
  {
  if(this.workers.contains(worker) && this.workers.size() <= maxWorkers)
    {
    return true;
    }
  else if(this.workers.size()+1 <= maxWorkers)
    {
    return true;
    }
  return false;
  }

@Override
public void addWorker(IWorker worker)
  {
  this.workers.add(worker);
  }

@Override
public void removeWorker(IWorker worker)
  {
  this.workers.remove(worker);  
  }

protected void validateWorkers()
  {
  if(this.workerValidationDelayTicks>0)
    {
    this.workerValidationDelayTicks--;
    return;
    }
  this.workerValidationDelayTicks = Config.npcAITicks * 10;
  Iterator<IWorker> workIt = this.workers.iterator();
  IWorker npc = null;
  while(workIt.hasNext())
    {
    npc = workIt.next();
    if(npc==null || npc.isDead() || npc.getDistanceFrom(xCoord, yCoord, zCoord)>Config.npcAISearchRange)
      {      
      workIt.remove();
      continue;
      }
    WayPoint p = npc.getWorkPoint();
    if(p==null || p.floorX()!= xCoord || p.floorY()!=yCoord || p.floorZ()!=zCoord)
      {
      workIt.remove();
      continue;
      }
    ITEWorkSite  te = npc.getWorkSite();
    if(te!=this)
      {
      workIt.remove();
      continue;
      }
    }
  }

@Override
public void broadcastWork(int maxRange)
  {
  if(!this.shouldBroadcast)
    {
    return;
    }
  if(this.broadcastDelayTicks>0)
    {
    this.broadcastDelayTicks--;
    return;
    }
  this.broadcastDelayTicks = Config.npcAITicks * 10;
  if(!this.isWorking)
    {
    return;
    }
  if(this.worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1).expand(maxRange, maxRange/2, maxRange);
  List<NpcBase> npcList = worldObj.getEntitiesWithinAABB(NpcBase.class, bb);
  for(NpcBase npc : npcList)
    {
    if(isHostile(npc.teamNum))      
      {
      if(npc.npcType.isCombatUnit())
        {
        npc.handleTileEntityTargetBroadcast(this, TargetType.ATTACK_TILE, Config.npcAITicks*11);
        }      
      }
    else
      {
      if(hasWork() && canHaveMoreWorkers(npc) && npc.npcType.getWorkTypes(npc.rank).contains(workType) && npc.teamNum==this.teamNum)
        {
        npc.handleTileEntityTargetBroadcast(this, TargetType.WORK, Config.npcAITicks*11);
        }
      }
    }
  }

@Override
public CivicWorkType getWorkType()
  {
  return this.workType;
  }

@Override
public boolean isWorkSite()
  {
  return true;
  }
}
