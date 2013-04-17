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
package shadowmage.ancient_warfare.common.npcs.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shadowmage.ancient_warfare.common.npcs.NpcBase;

public abstract class NpcAIObjective
{

protected NpcBase npc;
protected int maxPriority;
public int currentPriority;
public int cooldownTicks = 0;
public int maxCooldownticks = 10;
public int selfInterruptTicks = 10;
public int otherInterruptTicks = 10;

protected boolean hadWork = true;
protected boolean isFinished = false;

protected static Random rng;// = new Random();

/**
 * the tasks, in order, necessary to complete this objective
 * e.g. find target, move to target, attack target.
 */
protected List<NpcAITask> aiTasks = new ArrayList<NpcAITask>();

public abstract void addTasks();
public abstract void updatePriority();
public abstract void onRunningTick();
public abstract void onObjectiveStart();
public abstract void stopObjective();

public NpcAIObjective(NpcBase npc, int maxPriority)
  {
  this.npc = npc;
  rng = npc.getRNG();
  this.maxPriority = maxPriority;
  this.addTasks();
  }

public void onTick()
  { 
  hadWork = false;
  int mutex = 0;
  for(NpcAITask task : this.aiTasks)
    {
    if(task.canExecute(mutex) && task.shouldExecute())
      {
      hadWork = true;
      task.onTick();
      mutex += task.taskType;
      }
    }
  this.onRunningTick();
  }

public void startObjective()
  {
  this.isFinished = false;
  this.hadWork = false;
  this.onObjectiveStart();
  }

public boolean isFinished()
  {
  return !this.hadWork || this.isFinished;
  }

public void updateCooldownTicks()
  {
  if(this.cooldownTicks>0)
    {
    this.cooldownTicks--;
    }
  }

}
