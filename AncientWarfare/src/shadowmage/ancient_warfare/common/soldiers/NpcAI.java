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
package shadowmage.ancient_warfare.common.soldiers;

import java.util.Random;

public abstract class NpcAI implements INpcAI
{

/**
 * mutex bits...add them together to create hybrids..
 */
public static final int NONE = 0;
public static final int WANDER = 1;
public static final int FOLLOW = 2;
public static final int ATTACK = 4;
public static final int HEAL = 8;
public static final int REPAIR = 16;
public static final int HARVEST = 32;
public static final int MOVE_TO = 64;
public static final int MOUNT_VEHICLE = 128;

protected NpcBase npc;

protected static Random rng = new Random();

protected String taskName = "";
protected String tooltip = "";
protected int successTicks = 20;
protected int failureTicks = 20;

protected int currentTick = 0;
protected int cooldownTicks = 0;
protected boolean finished = false;
protected boolean success = false;
protected boolean hasStarted = false;

/**
 * actual classes should provide a static AI type num internally
 * @param typeNum
 * @param npc
 */
public NpcAI(NpcBase npc)
  {
  this.npc = npc;
  }

@Override
public String getTaskName()
  {
  return taskName;
  }

@Override
public int getSuccessTicks()
  {
  return successTicks;
  }

@Override
public int getFailureTicks()
  {
  return failureTicks;
  }

@Override
public boolean shouldExecute(NpcBase npc)
  {
  return this.cooldownTicks<=0 && !this.isFinished();
  }

@Override
public void incrementTickCounts()
  {  
  if(this.cooldownTicks>0)
    {
    this.cooldownTicks--;  
    }
  this.currentTick++;
  }

@Override
public boolean isFinished()
  {
  if(this.finished)
    {
    this.finished = false;
    this.hasStarted = false;
    if(this.wasSuccess())
      {
      this.cooldownTicks = this.getSuccessTicks();
      }
    else
      {
      this.cooldownTicks = this.getFailureTicks();
      }
    this.currentTick = 0;
    return true;
    }
  return false;
  }

@Override
public void startAI()
  {
  this.hasStarted = true;
  this.onAiStarted();
  }

@Override
public boolean hasStarted()
  {
  return this.hasStarted;
  }

@Override
public boolean wasSuccess()
  {
  if(this.success)
    {
    this.success = false;
    return true;
    }
  return false;
  }

}
