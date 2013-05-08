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
import net.minecraft.entity.EntityLiving;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAITask;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AIHealTarget extends NpcAITask
{

/**
 * @param npc
 */
public AIHealTarget(NpcBase npc)
  {
  super(npc);
  this.exclusiveTasks = MOVE_TO + ATTACK + FOLLOW + HARVEST;
  }

@Override
public void onTick()
  {
  if(npc.actionTick<=0)
    {
    Entity ent = npc.getTarget().getEntity(npc.worldObj);
    if(ent instanceof EntityLiving)
      {
      EntityLiving liv = (EntityLiving)ent;
      if(liv.getHealth()< liv.getMaxHealth())
        {
        liv.heal(npc.rank + 2);
        npc.setActionTicksToMax();
        }
      else
        {
        npc.targetHelper.removeTarget(npc.getTarget());
        npc.setTargetAW(null);
        }
      }
    else
      {
      npc.targetHelper.removeTarget(npc.getTarget());
      npc.setTargetAW(null);
      }
    }
  }

@Override
public boolean shouldExecute()
  {
  return npc.getTargetType()==TargetType.HEAL && npc.getDistanceFromTarget(npc.getTarget())< npc.targetHelper.getAttackDistance(npc.getTarget());
  }

@Override
public byte getTaskType()
  {
  return task_heal;
  }

}
