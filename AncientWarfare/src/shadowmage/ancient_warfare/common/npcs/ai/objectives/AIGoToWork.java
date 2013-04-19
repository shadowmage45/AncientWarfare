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
package shadowmage.ancient_warfare.common.npcs.ai.objectives;

import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.npcs.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.pathfinding.PathUtils;
import shadowmage.ancient_warfare.common.pathfinding.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.TargetType;

public class AIGoToWork extends NpcAIObjective
{

TECivic workSite;
WorkPoint workPoint;
int currentWorkTicks = 0;

/**
 * @param npc
 * @param maxPriority
 */
public AIGoToWork(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 1, false));
  }

@Override
public void updatePriority()
  {
  if(!npc.wayNav.hasWorkPoint())
    {
    //TODO check targetHelper for workSite targets that have broadcast, take highest priority/aggro target
    this.currentPriority = 0;
    return;
    }
  else if(this.workSite==null)
    {
    WayPoint workPoint = npc.wayNav.getWorkPoint();
    TileEntity te = npc.worldObj.getBlockTileEntity(workPoint.x, workPoint.y, workPoint.z);
    if(te instanceof TECivic)
      {
      this.workSite = (TECivic)te;
      }
    else
      {
      this.workSite = null;
      }
    }
  else
    {
    WayPoint workPoint = npc.wayNav.getWorkPoint();
    if(workPoint.x!=workSite.xCoord || workPoint.y!=workSite.yCoord || workPoint.z!=workSite.zCoord)
      {
      TileEntity te = npc.worldObj.getBlockTileEntity(workPoint.x, workPoint.y, workPoint.z);
      if(te instanceof TECivic)
        {
        this.workSite = (TECivic)te;
        }
      else
        {
        this.workSite = null;
        }
      }
    }
  if(this.workSite==null)
    {
    this.isFinished = true;
    this.npc.wayNav.clearWorkPoint();
    this.cooldownTicks = this.maxCooldownticks;
    this.currentPriority = 0;
    return;
    }
  if(this.workSite.canHaveMoreWorkers(npc))
    {    
    this.currentPriority = this.maxPriority;
    }
  else
    {
    this.currentPriority = 0;
    }  
  }

@Override
public void onRunningTick()
  {
  if(this.workSite!=null)
    {
    if(workPoint!=null)
      {      
      //check distance to work point
      //move to work point or 'work' on point
      //TODO check 'stuck' ticks to see if need to turn in work early/noPath
      this.currentWorkTicks++;
      if(npc.getDistance(workPoint.posX()+0.5f, workPoint.posY(), workPoint.posZ()+0.5f) > 3)
        {
        this.setMoveToWork(workPoint);
        }
      else
        {
        this.workOnPoint(workPoint);
        }
      }
    else
      {
      int ex = npc.floorX();
      int ey = npc.floorY();
      int ez = npc.floorZ();
      if(!workSite.isInsideOrNearWorkBounds(ex, ey, ez, 5))
        {
        this.setMoveToWorkSite();
        }
      else
        {
        this.claimWorkPoint();
        }
      }    
    }
  else
    {
    this.workPoint = null;
    this.workSite = null;
    this.currentPriority = 0;
    this.cooldownTicks = this.maxCooldownticks;    
    this.isFinished = true;
    this.npc.wayNav.clearWorkPoint();
    }
  }

@Override
public void onObjectiveStart()
  {
  if(this.workSite!=null)
    {
    int ex = npc.floorX();
    int ey = npc.floorY();
    int ez = npc.floorZ();
    if(!workSite.isInsideOrNearWorkBounds(ex, ey, ez, 5))
      {
      this.setMoveToWorkSite();
      }
    else
      {
      this.claimWorkPoint();
      }
    }
  else
    {
    this.workPoint = null;
    this.workSite = null;
    this.currentPriority = 0;
    this.cooldownTicks = this.maxCooldownticks;    
    this.isFinished = true;
    this.npc.wayNav.clearWorkPoint();
    }
  }

protected void workOnPoint(WorkPoint p)
  {
  if(p!=null)
    {
    if(npc.actionTick<=0)
      {
      npc.actionTick = 35;//TODO add action delay to npctype - by rank
      p.incrementHarvestHits();
      if(p.shouldFinish())
        {
        p.setFinished();
        this.setWorkFinished(p);
        }
      }
    }
  }

protected void setWorkFinished(WorkPoint p)
  {
  if(workSite!=null && p!=null)
    {
    workSite.onWorkFinished(npc, p);
    this.workPoint = null;
    }
  }

protected void setMoveToWorkSite()
  {
  //TODO set move-to target to work site block and/or inside of bounds of work area
  AIAggroEntry target = npc.getTarget();
  if(target!=null)
    {
    //TODO
    //check to see if current target is within bounds, else ..do the same as below    
    }
  else
    {
    BlockPosition pos = workSite.getPositionInBounds(2);
    pos.y = PathUtils.findClosestYTo(npc.getWorldAccess(), pos.x, pos.y, pos.z);
    setMoveToPoint(pos.x, pos.y, pos.z);
    }
  }

protected void setMoveToPoint(int x, int y, int z)
  {
  npc.setTargetAW(npc.targetHelper.getTargetFor(x, y, z, TargetType.MOVE));
  }

protected void setMoveToWork(WorkPoint p)
  {
  setMoveToPoint(p.floorX(), p.floorY(), p.floorZ());
  }

protected void claimWorkPoint()
  {
  this.currentWorkTicks = 0;
  if(this.workSite!=null)
    {
    WorkPoint p = this.workSite.getWorkPoint(npc);
    this.setWorkPoint(p);
    }
  }

protected void setWorkPoint(WorkPoint p)
  {
  this.workPoint = p;
  if(p!=null)
    {
    AIAggroEntry target = npc.getTarget();
    if(target!=null)
      {
      if(target.isEntityEntry && p.isEntityEntry())
        {
        if(target.getEntity()!=p.getEntityTarget())
          {
          this.setMoveToWork(p);
          }
        }
      else if(!target.isEntityEntry && !p.isEntityEntry())
        {
        if(target.floorX()!=p.floorX()|| target.floorY()!=p.floorY() || target.floorZ()!= p.floorZ())
          {
          this.setMoveToWork(p);
          }
        }
      else
        {
        this.setMoveToWork(p);
        }
      }
    else
      {
      this.setMoveToWork(p);
      }
    }
  }

@Override
public void stopObjective()
  {
  if(this.workSite!=null)
    {
    if(this.workPoint!=null)
      {
      this.workSite.onWorkFailed(npc, workPoint);
      }
    }  
  this.workPoint = null;  
  }

}
