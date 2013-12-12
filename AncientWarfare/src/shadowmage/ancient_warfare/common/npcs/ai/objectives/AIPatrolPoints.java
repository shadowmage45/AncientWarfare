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
package shadowmage.ancient_warfare.common.npcs.ai.objectives;

import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AIPatrolPoints extends NpcAIObjective
{

WayPoint patrolPoint;
int enemyRange = 20;

/**
 * @param npc
 * @param maxPriority
 */
public AIPatrolPoints(NpcBase npc, int maxPriority, int enemyRange)
  {
  super(npc, maxPriority);
  this.enemyRange = enemyRange;
  }

@Override
public byte getObjectiveNum()
  {
  return patrol;
  }

@Override
public void updatePriority()
  {
  if(npc.wayNav.getPatrolSize()<=0 || npc.targetHelper.areTargetsInRange(TargetType.ATTACK, enemyRange))
    {
    this.currentPriority = 0;
    }
  else
    {
    this.currentPriority = this.maxPriority;     
    }
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 2, false));
  }

@Override
public void onRunningTick()
  {
  //check to see if enemies are within range
  if(npc.targetHelper.areTargetsInRange(TargetType.ATTACK, enemyRange))
    {
    this.setFinished();
    }
  else//check to see if we just completed a node, if so, pause.
    {
    ITargetEntry entry = npc.getTarget();
    if(entry==null)
      {
      this.cooldownTicks = this.maxCooldownticks;
      this.isFinished = true;
      this.patrolPoint = null;
      }
    else
      {      
      if(entry.getTargetType() == TargetType.PATROL)
        {
        if(npc.getDistanceFromTarget(entry) < 3)
          {
          this.cooldownTicks = this.maxCooldownticks;
          this.isFinished = true;
          this.patrolPoint = null;
          }
        }
      else
        {
        this.cooldownTicks = this.maxCooldownticks;
        this.isFinished = true;
        this.patrolPoint = null;
        }
      }
    }
  }

@Override
public void onObjectiveStart()
  {
  if(patrolPoint==null)
    {
    for(int i = 0; i < npc.wayNav.getPatrolSize(); i++)
      {
      patrolPoint = npc.wayNav.getNextPatrolPoint();
      if(patrolPoint!=null && patrolPoint.isTargetLoaded(npc.worldObj))
        {
        break;
        }
      }
    }  
  if(patrolPoint!=null && patrolPoint.isTargetLoaded(npc.worldObj))
    {    
    npc.setTargetAW(patrolPoint);
    }
  else
    {
    npc.setTargetAW(null);
    this.isFinished = true;
    this.cooldownTicks = maxCooldownticks;
    }
  }

@Override
public void stopObjective()
  {
  npc.setTargetAW(null);  
  npc.clearPath();
  }

}
