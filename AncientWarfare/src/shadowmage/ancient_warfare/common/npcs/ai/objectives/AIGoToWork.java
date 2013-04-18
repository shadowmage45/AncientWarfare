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

public class AIGoToWork extends NpcAIObjective
{

boolean isHarvestTask;

/**
 * @param npc
 * @param maxPriority
 */
public AIGoToWork(NpcBase npc, int maxPriority, boolean isHarvestTask)
  {
  super(npc, maxPriority);
  this.isHarvestTask = isHarvestTask;
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
    this.currentPriority = 0;
    return;
    }
  float percent = 1.f;
  float maxF = (float)this.maxPriority;
  float priorF = 0.f;
  if(isHarvestTask)//dependant upon inventory being empty
    {
    percent = 1.f - npc.inventory.getPercentFull();    
    }
  else//is a processing task, needs 'full' inventory to 'work'
    {
    percent = npc.inventory.getPercentFull();    
    }
  priorF = percent * maxF;
  this.currentPriority = (int) priorF;
  }

@Override
public void onRunningTick()
  {
  // TODO Auto-generated method stub

  }

@Override
public void onObjectiveStart()
  {
  // TODO Auto-generated method stub

  }

@Override
public void stopObjective()
  {
  // TODO Auto-generated method stub

  }

}
