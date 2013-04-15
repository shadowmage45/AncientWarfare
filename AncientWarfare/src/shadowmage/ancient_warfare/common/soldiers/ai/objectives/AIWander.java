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

import java.util.List;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathUtils;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.soldiers.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;

public class AIWander extends NpcAIObjective
{

int tps = 20/Config.npcAITicks;
int wanderTick = 0;


/**
 * @param npc
 * @param maxPriority
 */
public AIWander(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);  
  this.currentPriority = this.maxPriority;
  }

int nextWander = tps;
@Override
public void updateObjectivePriority()
  {  
  
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 1, false));
  }

@Override
public void startObjective()
  {  
  wanderTick = 0;
  nextWander = 0;
  this.setWanderTarget();
  nextWander = (rng.nextInt(8)*tps) + 1;  
  Config.logDebug("clearing timers");
  super.startObjective();
  }

@Override
public void onTick()
  {
  super.onTick();
  wanderTick++;
  if(wanderTick >= nextWander)
    {      
    this.setWanderTarget();
    wanderTick = 0;
    nextWander = (rng.nextInt(8)*tps) + 1;  
    }  
  }

private void setWanderTarget()
  {
  int x = MathHelper.floor_double(npc.posX);
  int y = MathHelper.floor_double(npc.posY);
  int z = MathHelper.floor_double(npc.posZ);
  npc.nav.setPath(PathUtils.findRandomPath(npc.nav.worldAccess, x, y, z, 10, 4, rng.nextInt(10), rng));
  }

}
