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

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAITask;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AIRepairTarget extends NpcAITask
{

/**
 * @param npc
 */
public AIRepairTarget(NpcBase npc)
  {
  super(npc);
  }

@Override
public void onTick()
  {
  if(npc.actionTick>0)
    {
    return;
    }
  npc.setActionTicksToMax();
  ITargetEntry entry = npc.getTarget();
  if(entry!=null)
    {
    Entity ent = entry.getEntity(npc.worldObj);
    if(ent instanceof VehicleBase)
      {
      VehicleBase vehicle = (VehicleBase)ent;
      int health = (int) vehicle.getHealth();
      if(health < vehicle.baseHealth)
        {
        int healAmt = (npc.rank*2) + 4;
        if(health + healAmt > vehicle.baseHealth)
          {
          healAmt = (int) (vehicle.baseHealth - vehicle.getHealth());
          }
        vehicle.setHealth(vehicle.getHealth() + healAmt);
        }      
      }
    }
  }

@Override
public boolean shouldExecute()
  {
  return npc.getTargetType()==TargetType.REPAIR && npc.getTarget().getEntity(npc.worldObj)!=null && npc.getDistanceFromTarget(npc.getTarget())< npc.targetHelper.getAttackDistance(npc.getTarget());
  }

@Override
public byte getTaskType()
  {
  return task_repair;
  }

}
