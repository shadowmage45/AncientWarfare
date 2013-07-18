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
package shadowmage.ancient_warfare.common.npcs;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;


public class NpcAIObjectiveManager
{

/**OBJECTIVE MANAGER
 * start:
 * 
 * if current==null || current.isFinished() || changeTicks<=0 
 *    updatePriorities() 
 *    new = selectHighest()
 *    setObjective(new)
 * if(current!=null)
 *  {
 *  current.tickObjective()
 *  }  
 */

NpcBase npc;

NpcAIObjective currentObjective;
int currentObjectiveTicks = 0;


public boolean wasMoving = false;
List<NpcAIObjective> allObjectives = new ArrayList<NpcAIObjective>();

public NpcAIObjectiveManager(NpcBase npc)
  {
  this.npc = npc;
  }

public void addObjectives(List<NpcAIObjective> objectives)
  {
  this.allObjectives.clear();
  this.currentObjective = null;
  this.currentObjectiveTicks = 0;
  this.allObjectives.addAll(objectives);
  }

/**
 * called from NpcBase updateAITick every X ticks(Config.aiTicks)
 */
public void updateObjectives()
  {  
  if(this.currentObjective==null || this.currentObjective.isFinished() || currentObjectiveTicks<=0)
    {    
    this.updatePriorities();
    this.selectObjective();
    }
  if(this.currentObjective!=null)
    {
    this.currentObjective.onTick();
    }
  for(NpcAIObjective obj : this.allObjectives)
    {
    obj.updateCooldownTicks();
    }
  if(currentObjectiveTicks>0)
    {
    currentObjectiveTicks--;
    }
  }

private void updatePriorities()
  {
  for(NpcAIObjective obj : this.allObjectives)
    {
    if(obj!=null && obj.cooldownTicks<=0)
      {
      obj.updatePriority();
      }
    }
  }

private void selectObjective()
  {
  NpcAIObjective highestObj = null;
  int bestPriority = 0;
  for(NpcAIObjective obj : this.allObjectives)
    {
    if(obj.currentPriority > bestPriority && obj.cooldownTicks<=0)
      {
      bestPriority = obj.currentPriority;
      highestObj = obj;
      }
    }
  this.setObjective(highestObj);
  }

private void setObjective(NpcAIObjective objective)
  {
  if(objective==null)
    {
    npc.setObjectiveID((byte)-1);
    npc.setTaskID((byte)-1);
    this.currentObjectiveTicks = 2;
    if(this.currentObjective!=null)
      {
      this.currentObjective.stopObjective();
      }
    this.currentObjective = null;    
    }
  else
    {
    npc.setObjectiveID((byte)objective.getObjectiveNum());
    if(this.currentObjective==null || this.currentObjective != objective)
      {
      if(this.currentObjective!=null)
        {
        this.currentObjective.stopObjective();
        }
      objective.startObjective();
      } 
    this.currentObjective = objective;
    this.currentObjectiveTicks = this.currentObjective.otherInterruptTicks;
    }
  }

private void tickObjective()
  {
  if(this.currentObjective!=null)
    {
    this.currentObjective.onTick();
    this.currentObjective.onRunningTick();
    }
  }

}
