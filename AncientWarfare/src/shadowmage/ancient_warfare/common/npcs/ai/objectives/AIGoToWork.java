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
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.targeting.TargetPosition;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AIGoToWork extends NpcAIObjective
{
TECivic workSite = null;
boolean working = false;

/**
 * @param npc
 * @param maxPriority
 */
public AIGoToWork(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);
  this.maxCooldownticks = 40;
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
//    Config.logDebug("inventory full");
    work = false;
    }
  else if(npc.wayNav.getWorkSite()==null)
    {
    work = false;
//    Config.logDebug("has no work site, checking targetHelper");
    while(npc.targetHelper.hasTargetsOfType(TargetType.WORK))
      {
      ITargetEntry entry = npc.targetHelper.getHighestAggroTarget(TargetType.WORK);
      if(entry.isTileEntry())
        {
        if(entry.getTileEntity(npc.worldObj) instanceof TECivic)
          {
          TECivic tec = (TECivic) entry.getTileEntity(npc.worldObj);
          if(tec.hasWork() && tec.canHaveMoreWorkers() && npc.npcType.getWorkTypes(npc.rank).contains(tec.getCivic().getWorkType()))
            {
//            Config.logDebug("assigning te from aggro list!!");
            npc.wayNav.setWorkSite(new WayPoint(entry.floorX(), entry.floorY(), entry.floorZ(), entry.getTargetType()));
            workSite = tec;
            work = true;
            break;
            }
          else
            {
            npc.targetHelper.removeTarget(entry);
            }
          }
        }
      }    
    }
  else if(!isWorkSiteWorkable())
    {
//    Config.logDebug("site not workable -- clearing work site");
    npc.wayNav.setWorkSite(null);
    work = false;
    }  
  if(work)
    {
    this.currentPriority = this.maxPriority;
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
  ITargetEntry p = npc.wayNav.getWorkSite();
  if(workSite!=null && p!=null)//make sure current work reference lines up with wayNav work-site
    {
    if(p.floorX()!=workSite.xCoord || p.floorY()!= workSite.yCoord || p.floorZ()!=workSite.zCoord)
      {         
      workSite = null;
      }
    }
  if(workSite!=null)//check to make sure te is still valid
    {
    if(npc.worldObj.getBlockTileEntity(workSite.xCoord, workSite.yCoord, workSite.zCoord)!=workSite)
      {
//      Config.logDebug("world te did not match workSite");
      workSite = null;      
      }
    }
  if(workSite==null && p!=null)
    {
    TileEntity te = npc.worldObj.getBlockTileEntity(p.floorX(), p.floorY(), p.floorZ());
    if(te instanceof TECivic)
      {
      workSite = (TECivic)te;
      }
    else
      {
      workSite = null;
      }
    }
  if(workSite==null)
    {
//        Config.logDebug("not workable--no site");
    return false;
    }
  else
    {
    if(workSite.canHaveMoreWorkers() && workSite.hasWork())
      {
      return true;
      }
    else
      {
//            Config.logDebug("no workers or no work rejection");
      return false;
      }
    }
  }

@Override
public void onRunningTick()
  {
  if(workSite==null || !workSite.hasWork())
    {
    this.setFinished();
    return;
    }
  if(npc.getDistance(workSite.xCoord+0.d, workSite.yCoord, workSite.zCoord+0.5d)>2.4)
    {
    working = false;
    //wait for ai to move to target
    }
  else
    {      
    if(!working)
      {
      working = true;
      npc.setActionTicksToMax();
      }
    this.doWork();
    }   
  }

@Override
public void onObjectiveStart()
  {
  if(workSite!=null)
    {
    workSite.addWorker(npc);
    this.setMoveToWork();
    }
  else
    {
    this.setFinished();
    }
  working = false;
  }

protected void doWork()
  {
  npc.swingItem();
  if(npc.actionTick<=0)
    {  
    workSite.doWork(npc);
    npc.setActionTicksToMax();    
    }
  }

protected void setMoveToWork()
  {
  npc.setTargetAW(npc.wayNav.getWorkSite());
  }

@Override
public void stopObjective()
  {
  if(workSite!=null)
    {    
    workSite.removeWorker(npc);
    }
  }

}
