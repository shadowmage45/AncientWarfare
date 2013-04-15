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

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.ai.NpcAIObjective;


public class NpcAIObjectiveManager
{

NpcBase npc;

NpcAIObjective currentObjective;
int currentObjectiveTicks = 0;

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

public void updateObjectives()
  {
  if(this.currentObjectiveTicks>0)
    {
    this.currentObjectiveTicks--;
    }  
//  Config.logDebug("updating priorities for objectives. : "+this.allObjectives.size());
  for(NpcAIObjective objective : this.allObjectives)
    {
    objective.updateObjectivePriority();
    objective.updateTaskTimers();
    }
  this.selectObjective();
  this.tickObjective();
  }

private void selectObjective()
  {
  if(this.currentObjective==null || this.currentObjectiveTicks<=0 || this.currentObjective.isFinished )
    {    
    NpcAIObjective highestObj = null;
    int bestPriority = 0;
    for(NpcAIObjective obj : this.allObjectives)
      {
//      Config.logDebug(obj+ " p: "+obj.currentPriority);
      if(obj.currentPriority>bestPriority)
        {
        bestPriority = obj.currentPriority;
        highestObj = obj;
        }
      }
    this.setObjective(highestObj);
    }
  }

private void setObjective(NpcAIObjective objective)
  {
  if(objective==null)
    {
    this.currentObjectiveTicks = 0;
    this.currentObjective = null;
    }
  else
    {
    if(objective != this.currentObjective)
      {
      this.currentObjectiveTicks = objective.minObjectiveTicks;
      objective.startObjective();
      }
    this.currentObjective = objective;    
    }
  }

private void tickObjective()
  {
  if(this.currentObjective!=null)
    {
    this.currentObjective.onTick();
    }
  }

}
