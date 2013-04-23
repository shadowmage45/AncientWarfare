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

import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAITask;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AIMoveToTarget extends NpcAITask
{

boolean useAttackDistance = false;
float stopDistance = 1.f;

int stuckTicks = 0;

int x;
int z;

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
public void onTick()
  { 
  float bX = npc.getTarget().posX();
  float bY = npc.getTarget().posY();
  float bZ = npc.getTarget().posZ();
  if(npc.getTarget().getEntity()!=null)
    {
    bY = (float) npc.getTarget().getEntity().posY;
    }
  int ex = MathHelper.floor_double(npc.posX);
  int ey = MathHelper.floor_double(npc.posY);
  int ez = MathHelper.floor_double(npc.posZ);  
  if(npc.isRidingVehicle())
    {
    ((VehicleBase)npc.ridingEntity).nav.setMoveTo(MathHelper.floor_float(bX), MathHelper.floor_float(bY), MathHelper.floor_float(bZ));
    }
  else
    {
    npc.nav.setMoveToTarget(MathHelper.floor_float(bX), MathHelper.floor_float(bY), MathHelper.floor_float(bZ));
    }
  }

@Override
public boolean shouldExecute()
  {
  if(npc.getTarget()==null)
    {
    return false;
    }
  float minDist = useAttackDistance ? npc.targetHelper.getAttackDistance(npc.getTarget()) : stopDistance;
  float dist = npc.getDistanceFromTarget(npc.getTarget());
  if(dist>minDist)
    {
    return true;
    }
  else
    {
    if(npc.isRidingVehicle())
      {
      ((VehicleBase)npc.ridingEntity).nav.clearPath();
      }
    else
      {    
      npc.nav.clearPath();      
      }
    return false;
    }
  }


  
}
