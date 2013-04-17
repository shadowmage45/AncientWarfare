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

import java.util.List;

import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathUtils;
import shadowmage.ancient_warfare.common.utils.TargetType;

public class AIWander extends NpcAIObjective
{

int tps = 20/Config.npcAITicks;
int wanderTick = 0;
int nextWander = tps;

/**
 * @param npc
 * @param maxPriority
 */
public AIWander(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);  
  this.currentPriority = this.maxPriority;
  }

@Override
public void addTasks()
  {
//  this.aiTasks.add(new AIMoveToTarget(npc, 1, false));
  }

@Override
public void updatePriority()
  {
  
  }

@Override
public void onRunningTick()
  {
  this.wanderTick++;
  if(npc.getTarget()==null || npc.getDistanceFromTarget(npc.getTarget())<2 || wanderTick > 20 || npc.targetHelper.areTargetsInRange(TargetType.ATTACK, 20))
    {
    this.isFinished = true;
    this.cooldownTicks = 10;
    }
  }

@Override
public void onObjectiveStart()
  {
  //get random target
  int range = 10;
  int x = MathHelper.floor_double(npc.posX);
  int y = MathHelper.floor_double(npc.posY);
  int z = MathHelper.floor_double(npc.posZ);  
  int tx = x + rng.nextInt(range*2)-range;
  int tz = z + rng.nextInt(range*2)-range;
  int ty = PathUtils.findClosestYTo(npc.nav.worldAccess, tx, y, tz);
  List<Node> path = PathUtils.randomCrawl(npc.nav.worldAccess, x, y, z, tx, ty, tz, 12, rng);
  npc.setPath(path);
  if(path.size()>0)
    {
    Node end = path.get(path.size()-1);
    npc.setTargetAW(npc.targetHelper.getTargetFor(end.x, end.y, end.z, TargetType.WANDER));
    }
  else
    {
    npc.setTargetAW(npc.targetHelper.getTargetFor(tx, ty, tz, TargetType.WANDER));
    }
  }

@Override
public void stopObjective()
  {
  this.wanderTick = 0;
  }


}
