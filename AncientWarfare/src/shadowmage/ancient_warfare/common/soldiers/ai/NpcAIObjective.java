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
package shadowmage.ancient_warfare.common.soldiers.ai;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;

public abstract class NpcAIObjective
{

protected NpcBase npc;
protected int maxPriority;
public int currentPriority;
public int minObjectiveTicks = 40/Config.npcAITicks;
public boolean isFinished = false;

protected AIAggroEntry objectiveTarget;



/**
 * the tasks, in order, necessary to complete this objective
 * e.g. find target, move to target, attack target.
 */
protected List<NpcAITask> aiTasks = new ArrayList<NpcAITask>();

public abstract void updateObjectivePriority();
public abstract void addTasks();

public NpcAIObjective(NpcBase npc, int maxPriority)
  {
  this.npc = npc;
  this.maxPriority = maxPriority;
  this.addTasks();
  }

public void onTick()
  {
  boolean hasWork = false;
  int mutex = 0;
  for(NpcAITask task : this.aiTasks)
    {
    if(task.canExecute(mutex) && task.shouldExecute())
      {
      hasWork = true;
      task.onTick();
      mutex += task.taskType;
      }
    }
  if(!hasWork)
    {
    this.isFinished = true;
    }
  }

public void updateTaskTimers()
  {
  for(NpcAITask task : this.aiTasks)
    {
    task.updateTimers();
    }
  }

public void startObjective()
  {
  this.isFinished = false;
  npc.setTargetAW(objectiveTarget);
  }

}
