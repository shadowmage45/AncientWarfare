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
import shadowmage.ancient_warfare.common.config.Config;
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
  this.aiTasks.add(new AIMoveToTarget(npc, 3, false));
  }

@Override
public void updatePriority()
  {
  if(!npc.wayNav.hasWorkSitePoint())
    {
//    Config.logDebug("update work priority, has no work point, setting finished");
    //TODO check targetHelper for workSite targets that have broadcast, take highest priority/aggro target
    this.currentPriority = 0;
    return;
    }
  else if(this.workSite==null)
    {
    WayPoint workPoint = npc.wayNav.getWorkSitePoint();
    TileEntity te = npc.worldObj.getBlockTileEntity(workPoint.x, workPoint.y, workPoint.z);
    if(te instanceof TECivic)
      {
//      Config.logDebug("had no work site, but had work point and valid, setting work site");
      this.workSite = (TECivic)te;
      }
    else
      {
//      Config.logDebug("had no work site, but had work point, it was invalid--setting finished");
      this.workSite = null;
      }
    }
  else
    {
//    Config.logDebug("had work site, validating");
    WayPoint workPoint = npc.wayNav.getWorkSitePoint();
    TileEntity te = npc.worldObj.getBlockTileEntity(workPoint.x, workPoint.y, workPoint.z);
    if(te instanceof TECivic)
      {
//      Config.logDebug("work site is valid");
      this.workSite = (TECivic)te;
      }
    else
      {
//      Config.logDebug("work site was not valid!");
      this.workSite = null;
      }
    }
  if(this.workSite==null)
    {
//    Config.logDebug("setting min priority and exit, work will not run");
    this.isFinished = true;
    this.npc.wayNav.clearWorkSitePoint();
    this.cooldownTicks = this.maxCooldownticks;
    this.currentPriority = 0;
    return;
    }
  if(this.workSite.canHaveMoreWorkers(npc))
    { 
    this.workSite.addWorker(npc);
//    Config.logDebug("work site has room for more workers, adding and doing work");
    if(npc.wayNav.getWorkPoint()==null)
      {
      this.npc.wayNav.setWorkPoint(this.workSite.getWorkPoint(npc));
      }
    if(npc.wayNav.getWorkPoint()!=null)
      {
      this.currentPriority = this.maxPriority;
      }
    else
      {
      this.isFinished = true;
      this.cooldownTicks = this.maxCooldownticks;
      this.currentPriority = 0;
      }
    }
  else
    {
    Config.logDebug("no room for more workers, setting min priority and exit, work will not run");
    this.isFinished = true;
    this.npc.wayNav.clearWorkSitePoint();
    this.cooldownTicks = this.maxCooldownticks;
    this.currentPriority = 0;
    }  
  }

@Override
public void onRunningTick()
  {
  WorkPoint workPoint = npc.wayNav.getWorkPoint();
  if(this.workSite!=null)
    {
    if(workPoint!=null)
      { 
      
//      Config.logDebug("has work point, attemping to work");
      //check distance to work point
      //move to work point or 'work' on point
      //TODO check 'stuck' ticks to see if need to turn in work early/noPath
//      Config.logDebug("incrementing work ticks");
      
      
      int y = PathUtils.findClosestYTo(npc.getWorldAccess(), workPoint.floorX(), workPoint.floorY(), workPoint.floorZ());
      if(npc.getDistance(workPoint.posX()+0.5f, y, workPoint.posZ()+0.5f) > 4)
        {
//        Config.logDebug("outside work range, heading towards point");
        this.setMoveToWork(workPoint);
        npc.actionTick = 35;
        }
      else
        {
        this.currentWorkTicks++;
//        Config.logDebug("within work range, working on point");
        npc.swingItem();
        this.workOnPoint(workPoint);
        }
      }
    else
      {
//      Config.logDebug("has no work point, moving to work or claiming point");
      int ex = npc.floorX();
      int ey = npc.floorY();
      int ez = npc.floorZ();
      if(!workSite.isInsideOrNearWorkBounds(ex, ey, ez, 5))
        {
//        Config.logDebug("outside work boundaries, heading to work");
        this.setMoveToWorkSite();
        }
      else
        {
//        Config.logDebug("inside work boundaries, claiming point");
        this.claimWorkPoint();
        npc.actionTick = 35;
        }
      }    
    }
  else
    {
//    Config.logDebug("no work site in run tick, setting finished");
    npc.wayNav.setWorkPoint(null);
    this.workSite = null;
    this.currentPriority = 0;
    this.cooldownTicks = this.maxCooldownticks;    
    this.isFinished = true;
    this.npc.wayNav.clearWorkSitePoint();
    }
  }

@Override
public void onObjectiveStart()
  {
//  Config.logDebug("starting aiWork");
  if(this.workSite!=null)
    {
//    Config.logDebug("validating current work site");
    int ex = npc.floorX();
    int ey = npc.floorY();
    int ez = npc.floorZ();
    if(!workSite.isInsideOrNearWorkBounds(ex, ey, ez, 5))
      {
//      Config.logDebug("outside of work bounds, heading to work");
      this.setMoveToWorkSite();
      }
    else
      {
//      Config.logDebug("inside of work bounds getting work point");
      this.claimWorkPoint();
      }
    }
  else
    {
//    Config.logDebug("could not start, work site was null--setting finished and exiting");
    this.npc.wayNav.setWorkPoint(null);
    this.workSite = null;
    this.currentPriority = 0;
    this.cooldownTicks = this.maxCooldownticks;    
    this.isFinished = true;
    this.npc.wayNav.clearWorkSitePoint();
    }
  }

protected void workOnPoint(WorkPoint p)
  {
  if(p!=null)
    {
    if(npc.actionTick<=0)
      {
      npc.actionTick = 35;//TODO add action delay to npctype - by rank
      this.workSite.doWork(npc, p);      
      if(p.shouldFinish())
        {
        this.setWorkFinished(p);
//        Config.logDebug("work was finished, setting work finished, new point: "+this.workPoint);
        }
      }
    }
  }

protected void setWorkFinished(WorkPoint p)
  {
  if(workSite!=null && p!=null)
    {
    workSite.onWorkFinished(npc, p);
    }
  this.npc.wayNav.setWorkPoint(null);
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
    Config.logDebug("returned position to move to: "+pos);
    setMoveToPoint(pos.x, pos.y, pos.z);
    }
  }

protected void setMoveToPoint(int x, int y, int z)
  {
  npc.setTargetAW(npc.targetHelper.getTargetFor(x, y, z, TargetType.MOVE));
  }

protected void setMoveToWork(WorkPoint p)
  {
  int y = PathUtils.findClosestYTo(npc.getWorldAccess(), p.floorX(), p.floorY(), p.floorZ());
  setMoveToPoint(p.floorX(), y, p.floorZ());
  }

protected void claimWorkPoint()
  {
  this.currentWorkTicks = 0;
  if(this.workSite!=null)
    {
    WorkPoint p = this.workSite.getWorkPoint(npc);
    this.setWorkPoint(p);
    Config.logDebug("claimed work point: "+p);
    }
  }

protected void setWorkPoint(WorkPoint p)
  {
  npc.wayNav.setWorkPoint(p);
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
    if(npc.wayNav.getWorkPoint()!=null)
      {
      this.workSite.onWorkFailed(npc, npc.wayNav.getWorkPoint());
      }
    }  
  this.npc.wayNav.setWorkPoint(null);
  }

}
