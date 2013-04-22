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

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.npcs.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.pathfinding.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.utils.TargetType;

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
public void updatePriority()
  {
  if(npc.wayNav.getPatrolSize()<=0)
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
    this.isFinished = true;
    }
  else//check to see if we just completed a node, if so, pause.
    {
    AIAggroEntry entry = npc.getTarget();
    if(entry==null)
      {
//      Config.logDebug("entity has no target, setting patrol to finished");

      this.cooldownTicks = this.maxCooldownticks;
      this.isFinished = true;
      this.patrolPoint = null;
      }
    else
      {
      if(entry.targetType.getTypeName() == TargetType.PATROL)
        {
        if(npc.getDistanceFromTarget(entry) < 3)
          {
//          Config.logDebug("sensing completed patrol point, setting finished");
          this.cooldownTicks = this.maxCooldownticks;
          this.isFinished = true;
          this.patrolPoint = null;
          }
        }
      else
        {
//        Config.logDebug("inconsistent target, not patrol target, setting finished");
        //what? somehow a diff target was set, force-finished
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
//  Config.logDebug("starting patrol ai, choosing next patrol point, setting target");
  patrolPoint = npc.wayNav.getNextPatrolPoint();
  if(patrolPoint!=null)
    {
    npc.setTargetAW(npc.targetHelper.getTargetFor(patrolPoint.floorX(), patrolPoint.floorY(), patrolPoint.floorZ(), TargetType.PATROL));
    }
  else
    {
    this.isFinished = true;
    this.cooldownTicks = maxCooldownticks;
    }
  }

@Override
public void stopObjective()
  {
//  Config.logDebug("stopping patrol ai, clearing target");
  npc.setTargetAW(null);  
  npc.clearPath();
  }

}
