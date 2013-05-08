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

import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.targeting.TargetPosition;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AIStayNearCommander extends NpcAIObjective
{

int leashRange;
int chokeRange;

/**
 * @param npc
 * @param maxPriority
 */
public AIStayNearCommander(NpcBase npc, int maxPriority, int range, int chokeRange)
  {
  super(npc, maxPriority);
  this.leashRange = range;
  this.chokeRange = chokeRange;
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, chokeRange, false));
  }

@Override
public void updatePriority()
  {
  if(npc.wayNav.getCommander()==null)
    {
    this.currentPriority = 0;
    }
  else if(npc.wayNav.getCommander().isDead)
    {
    npc.wayNav.setCommander(null);
    this.currentPriority = 0;    
    }
  else if(npc.wayNav.getCommander().getDistanceToEntity(npc) > leashRange)
    {
    this.currentPriority = this.maxPriority;
    }
  }

@Override
public void onRunningTick()
  {
  if(npc.wayNav.getCommander()==null || npc.wayNav.getCommander().getDistanceToEntity(npc) < chokeRange)
    {
    this.setFinished();
    }
  else
    {
    if(npc.getTargetType()!=TargetType.COMMANDER)
      {
      setTarget();
      }
    }  
  }

protected void setTarget()
  {
  npc.setTargetAW(TargetPosition.getNewTarget(npc.wayNav.getCommander(), TargetType.COMMANDER));
  }

@Override
public void onObjectiveStart()
  {
  setTarget();
  }

@Override
public void stopObjective()
  {
  this.setFinished();
  }

@Override
public byte getObjectiveNum()
  {
  return commander_follow;
  }

}
