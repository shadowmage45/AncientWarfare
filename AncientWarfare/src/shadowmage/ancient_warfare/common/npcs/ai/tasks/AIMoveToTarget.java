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
package shadowmage.ancient_warfare.common.npcs.ai.tasks;

import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAITask;
import shadowmage.ancient_warfare.common.pathfinding.PathUtils;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AIMoveToTarget extends NpcAITask
{

boolean useAttackDistance = false;
float stopDistance = 1.f;

/**
 * @param npc
 */
public AIMoveToTarget(NpcBase npc, float stopDistance, boolean useAttackDistance)
  {
  super(npc);  
  this.useAttackDistance = useAttackDistance;
  this.stopDistance = stopDistance;
  this.taskType = MOVE_TO;
  this.exclusiveTasks = ATTACK + FOLLOW + MOUNT_VEHICLE + REPAIR + HARVEST + HEAL;
  }

@Override
public byte getTaskType()
  {
  return task_move;
  }

@Override
public void onTick()
  {   
  float bX = npc.getTarget().posX();
  float bY = npc.getTarget().posY();
  float bZ = npc.getTarget().posZ();
  if(npc.getTarget().getEntity(npc.worldObj)!=null)
    {
    bY = PathUtils.findClosestYTo(npc.getWorldAccess(), npc.getTarget().floorX(), npc.getTarget().floorY(), npc.getTarget().floorZ());
    }  
  npc.setMoveToTarget(MathHelper.floor_float(bX), MathHelper.floor_float(bY), MathHelper.floor_float(bZ));   
  npc.aiManager.wasMoving = true;
  }

@Override
public boolean shouldExecute()
  {
  if(npc.getTarget()==null)
    {
    npc.clearPath();
    return false;
    }
  float minDist = useAttackDistance ? npc.targetHelper.getAttackDistance(npc.getTarget()) : stopDistance;
  float dist = npc.getDistanceFromTarget(npc.getTarget());
  if(useAttackDistance)
    {
    if(!npc.canEntityBeSeen(npc.getTarget().getEntity(npc.worldObj)))
      {
      return true;
      }
    }  
  if(dist>minDist)
    {
    return true;
    }
  else
    {
    npc.clearPath();
    return false; 
    }
  }
  
}
