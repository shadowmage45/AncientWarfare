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

public static final int attack = 0;
public static final int attack_ranged = 1;
public static final int deposit_goods = 2;
public static final int dismount = 3;
public static final int follow_player = 4;
public static final int work = 5;
public static final int mount = 6;
public static final int upkeep = 7;
public static final int patrol = 8;
public static final int shelter = 9;
public static final int home = 10;
public static final int wander = 11;

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
public abstract byte getObjectiveNum();

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
  NpcAITask last = null;
  for(NpcAITask task : this.aiTasks)
    {
    if(task.canExecute(mutex) && task.shouldExecute())
      {
      hadWork = true;
      task.onTick();
      mutex += task.taskType;
      last = task;      
      }
    }
  if(last!=null)
    {
    npc.setTaskID(last.getTaskType());
    }
  else
    {
    npc.setTaskID((byte)-1);
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
  return this.isFinished;
  }

public void updateCooldownTicks()
  {
  if(this.cooldownTicks>0)
    {
    this.cooldownTicks--;
    }
  }

public void setFinished()
  {
  this.npc.setTargetAW(null);
  this.npc.clearPath();
  this.isFinished = true;
  this.currentPriority = 0;
  this.cooldownTicks = this.maxCooldownticks;
  }

}
