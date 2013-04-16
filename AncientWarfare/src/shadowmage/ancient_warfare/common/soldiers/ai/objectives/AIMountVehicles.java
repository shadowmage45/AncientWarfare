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
package shadowmage.ancient_warfare.common.soldiers.ai.objectives;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.soldiers.ai.tasks.AIMountVehicle;
import shadowmage.ancient_warfare.common.soldiers.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;

public class AIMountVehicles extends NpcAIObjective
{

int maxRange;
/**
 * @param npc
 * @param maxPriority
 */
public AIMountVehicles(NpcBase npc, int maxPriority, int maxRange)
  {
  super(npc, maxPriority);
  this.maxRange = maxRange;
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 1.f, true));
  this.aiTasks.add(new AIMountVehicle(npc));
  }

@Override
public void updatePriorityTick()
  {
  if(npc.targetHelper.areTargetsInRange(NpcTargetHelper.TARGET_MOUNT, maxRange))
    {
    if(this.currentPriority<this.maxPriority)
      {
      this.currentPriority++;
      }
    }
  else
    {
    this.currentPriority = 0;
    }  
  }

@Override
public void onRunningTick()
  {
  
  }

@Override
public void onObjectiveStart()
  {
  npc.setTargetAW(npc.targetHelper.getHighestAggroTargetInRange(NpcTargetHelper.TARGET_MOUNT, maxRange)); 
  }

}
