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
import shadowmage.ancient_warfare.common.pathfinding.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.soldiers.ai.tasks.AIMoveToTarget;

public class AIStayNearHome extends NpcAIObjective
{

int leashRange;
int chokeRange;

/**
 * @param npc
 * @param maxPriority
 */
public AIStayNearHome(NpcBase npc, int maxPriority, int range, int chokeRange)
  {
  super(npc, maxPriority);
  this.leashRange = range;
  this.chokeRange = chokeRange;
  }

@Override
public void updateObjectivePriority()
  {
  if(!npc.wayNav.hasHomePoint())
    {
    this.currentPriority = 0;
    }
  else
    {    
    WayPoint home = npc.wayNav.getHomePoint();
    float range = (float) npc.getDistance(home.x+0.5d, home.y, home.z+0.5d);
    if(range>leashRange)
      {
      if(this.currentPriority<this.maxPriority)
        {
        this.currentPriority++;
        }
      if(this.objectiveTarget==null)
        {
        Config.logDebug("setting home point");
        this.objectiveTarget = npc.targetHelper.getTargetFor(home.x, home.y, home.z, npc.targetHelper.TARGET_MOVE);
        }
      }
    else
      {
      if(this.currentPriority>0)
        {
        this.currentPriority--;
        }
      }
    }
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, chokeRange, false));
  }

}
