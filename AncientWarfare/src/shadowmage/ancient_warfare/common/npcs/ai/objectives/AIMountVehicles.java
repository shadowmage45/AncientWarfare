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
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMountVehicle;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.targeting.TargetType;

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
public byte getObjectiveNum()
  {
  return mount;
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 1.f, true));
  this.aiTasks.add(new AIMountVehicle(npc));
  }

@Override
public void updatePriority()
  {
  if(npc.targetHelper.areTargetsInRange(TargetType.MOUNT, maxRange))
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
  if(npc.getTarget()==null)
    {
    if(npc.targetHelper.areTargetsInRange(TargetType.MOUNT, maxRange))
      {
      setMountTarget();      
      }
    else
      {
      this.currentPriority = 0;
      this.isFinished = true;
      }
    }
  }

@Override
public void onObjectiveStart()
  {
  setMountTarget();
  }

@Override
public void stopObjective()
  {
  npc.setTargetAW(null);  
  }

protected void setMountTarget()
  {
  npc.setTargetAW(npc.targetHelper.getHighestAggroTargetInRange(TargetType.MOUNT, maxRange));
  }

}
