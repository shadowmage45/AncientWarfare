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
package shadowmage.ancient_warfare.common.npcs.ai.tasks;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAITask;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;

public class AIFollowPatrolPoints extends NpcAITask
{

public WayPoint currentPoint;

/**
 * @param npc
 */
public AIFollowPatrolPoints(NpcBase npc)
  {
  super(npc);
  }

@Override
public byte getTaskType()
  {
  return task_patrol;
  }

@Override
public void onTick()
  {
  this.currentPoint = npc.wayNav.getNextPatrolPoint();
  npc.setTargetAW(currentPoint);  
  }

@Override
public boolean shouldExecute()
  {
  return npc.wayNav.getPatrolSize()>0 && (this.currentPoint==null || npc.getDistance(currentPoint.floorX(), currentPoint.floorY(), currentPoint.floorZ())<3);
  }


}
