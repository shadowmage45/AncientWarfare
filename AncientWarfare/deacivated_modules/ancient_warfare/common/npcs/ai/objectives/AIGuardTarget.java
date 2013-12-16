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

import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;

public class AIGuardTarget extends NpcAIObjective
{

/**
 * @param npc
 * @param maxPriority
 */
public AIGuardTarget(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 5, false));
  }

@Override
public void updatePriority()
  {
  this.currentPriority = 0;
  if(npc.wayNav.getGuardTarget()!=null && npc.wayNav.getGuardTarget().getEntity(npc.worldObj)!=null)
    {
    this.currentPriority = this.maxPriority;
    }
  }

@Override
public void onRunningTick()
  {
  if(npc.wayNav.getGuardTarget()==null || npc.wayNav.getGuardTarget().getEntity(npc.worldObj)==null)
    {
    this.setFinished();
    return;
    }
  
  /**
   * uhh..really...nothing?
   */
  }

@Override
public void onObjectiveStart()
  {
  npc.setTargetAW(npc.wayNav.getGuardTarget());
  }

@Override
public void stopObjective()
  {
  npc.setTargetAW(null);
  npc.clearPath();
  }

@Override
public byte getObjectiveNum()
  {
  return guard;
  }

}
