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
package shadowmage.ancient_warfare.common.soldiers.ai.tasks;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.pathfinding.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.ai.NpcAITask;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.utils.TargetType;

public class AIFollowPatrolPoints extends NpcAITask
{

public WayPoint currentPoint;
public AIAggroEntry currentTarget;

/**
 * @param npc
 */
public AIFollowPatrolPoints(NpcBase npc)
  {
  super(npc);
  }

@Override
public void onTick()
  {
  Config.logDebug("choosing patrol point");
  this.currentPoint = npc.wayNav.getNextPatrolPoint();
  this.currentTarget = npc.targetHelper.getTargetFor(currentPoint.x, currentPoint.y, currentPoint.z, TargetType.MOVE);
  npc.setTargetAW(currentTarget);  
  }

@Override
public boolean shouldExecute()
  {
  return npc.wayNav.getPatrolSize()>0 && (this.currentPoint==null || npc.getDistance(currentPoint.x, currentPoint.y, currentPoint.z)<3);
  }


}
