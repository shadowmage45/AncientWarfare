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

import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.pathfinding.PathUtils;
import shadowmage.ancient_warfare.common.targeting.TargetPosition;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class AIGoToWork extends NpcAIObjective
{

boolean working = false;
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
  this.aiTasks.add(new AIMoveToTarget(npc, 1.8f, false));
  }

@Override
public void updatePriority()
  {
  /**
   *  if no space, no site, no room for workers, or no work
   *    set priority to 0
   *    set cooldown to max
   *  else if has space, has site, has room for workers and has work
   *    set priority to max     
   */  
  boolean work = true;
  if(npc.inventory.getEmptySlotCount()<=1)
    {
    Config.logDebug("inventory full");
    work = false;
    }
  else if(!npc.wayNav.hasWorkSitePoint())
    {
    Config.logDebug("has no work site point");
    //TODO check target-lists for broadcasting work-sites
    work = false;
    }
  else if(!npc.wayNav.hasWorkSite())
    {
    Config.logDebug("has no work site");
    work = false;
    }
  else if(!isWorkSiteWorkable())
    {
    Config.logDebug("site not workable");
    work = false;
    }  
  if(work)
    {
//    Config.logDebug("setting work priority to max!! "+npc.wayNav.getWorkPoint());
    
    this.currentPriority = this.maxPriority;
//    this.cooldownTicks = this.maxCooldownticks;
    }
  else
    {
    this.currentPriority = 0;
    this.cooldownTicks = this.maxCooldownticks;
    }
  }

/**
 * checks work site from wayNav to see if it has work
 * and can take on more workers at the moment
 * @return
 */
protected boolean isWorkSiteWorkable()
  {
  TECivic work = npc.wayNav.getWorkSite();
  if(work==null)
    {
    return false;
    }
  else
    {
    if(work.canHaveMoreWorkers(npc) && work.hasWork(npc))
      {
      return true;
      }
    }
  return false;
  }

@Override
public void onRunningTick()
  {
  /**
   * if has work point
   *  if in range of work point
   *    work on point
   *    if point finished
   *      turn in
   *  else
   *    try to move towards work point
   * else
   *  try to claim work point---
   */  
  TECivic workSite = npc.wayNav.getWorkSite();
  if(workSite==null)
    {
    this.setFinished();
    return;
    }
  WorkPoint workPoint = npc.wayNav.getWorkPoint();
  
  if(workPoint==null)//try to claim a work point
    {
    WorkPoint p = workSite.getWorkPoint(npc);
    this.setWorkPoint(p);
    if(p==null)
      {
//      Config.logDebug("work site returned null point, setting finished");
      this.setFinished();
      return;
      }
    working = false;
    }
  else
    {
    if(npc.getDistance(workPoint.posX(), workPoint.posY(), workPoint.posZ())>2.4)
      {
      working = false;
      //wait for ai to move to target
      //move to point
      }
    else
      {      
      if(!working)
        {
        working = true;
        npc.actionTick = 35;
        }
      this.workOnPoint(workPoint);
      }
    }
  }

@Override
public void onObjectiveStart()
  {
  if(npc.wayNav.hasWorkSite())
    {
    TECivic work = npc.wayNav.getWorkSite();
    work.addWorker(npc);
    this.setWorkPoint(work.getWorkPoint(npc));
    }
  else
    {
    this.setFinished();
    }
  working = false;
  }

protected void workOnPoint(WorkPoint p)
  {
  if(p!=null)
    {
    if(npc.wayNav.hasWorkSite())
      {
      npc.swingItem();
      if(npc.actionTick<=0)
        {        
        npc.actionTick = 35;//TODO add action delay to npctype - by rank  
        WorkPoint returned = npc.wayNav.getWorkSite().doWork(npc, p);
        if(p!=returned)
          {
          this.setWorkPoint(returned);
          } 
        }
      }    
    }
  }

protected void setMoveToPoint(int x, int y, int z)
  {
  npc.setTargetAW(TargetPosition.getNewTarget(x, y, z, TargetType.WORK));
  }

protected void setMoveToWork(WorkPoint p)
  {
  setMoveToPoint(p.floorX(), p.floorY(), p.floorZ());
//  if(npc.getWorldAccess().isWalkable(p.floorX(), p.floorY(), p.floorZ()))
//    {
//    setMoveToPoint(p.floorX(), p.floorY(), p.floorZ());
//    }
//  else
//    {
//    int x = p.floorX();
//    int z = p.floorZ();
//    for(int y = p.floorY()+1; y>=p.floorY()-2; y--)
//      {
//      if(npc.getWorldAccess().isWalkable(x, y, z))
//        {
//        setMoveToPoint(x, y, z);
//        }
//      else if(npc.getWorldAccess().isWalkable(x-1, y, z))
//        {
//        setMoveToPoint(x-1, y, z);
//        }
//      else if(npc.getWorldAccess().isWalkable(x+1, y, z))
//        {
//        setMoveToPoint(x+1, y, z);
//        }
//      else if(npc.getWorldAccess().isWalkable(x, y, z-1))
//        {
//        setMoveToPoint(x, y, z-1);
//        }
//      else if(npc.getWorldAccess().isWalkable(x, y, z+1))
//        {
//        setMoveToPoint(x, y, z+1);
//        }
//      }
//    } 
  }

protected void setWorkPoint(WorkPoint p)
  {
  npc.actionTick = 35;
  if(p!=null)
    {   
    npc.wayNav.setWorkPoint(p);
    this.setMoveToWork(p);
    }
  else
    {
    npc.wayNav.setWorkPoint(null);  
    npc.setTargetAW(null);
    }
  }

@Override
public void stopObjective()
  {
  if(npc.wayNav.hasWorkSite())
    {
    if(npc.wayNav.getWorkPoint()!=null)
      {
      npc.wayNav.getWorkSite().onWorkFailed(npc, npc.wayNav.getWorkPoint());
      }
    npc.wayNav.getWorkSite().removeWorker(npc);
    }
  npc.wayNav.setWorkPoint(null);
  working = false;
  }

}
