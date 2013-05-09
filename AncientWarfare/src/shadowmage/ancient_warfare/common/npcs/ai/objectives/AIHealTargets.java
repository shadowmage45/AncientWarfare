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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIHealTarget;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AIHealTargets extends NpcAIObjective
{

/**
 * @param npc
 * @param maxPriority
 */
public AIHealTargets(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 3, true));
  this.aiTasks.add(new AIHealTarget(npc));  
  }

@Override
public void updatePriority()
  {
  if(npc.targetHelper.areTargetsInRange(TargetType.HEAL, 20))
    {
    this.currentPriority = this.maxPriority;
    }
  else
    {
    this.currentPriority = 0;
    }
  }

@Override
public void onRunningTick()
  {
  if(!isTargetValid())
    {
    if(!setTarget())
      {
      this.setFinished();
      }
    } 
  }

protected boolean isTargetValid()
  {
  ITargetEntry target = npc.getTarget();
  if(target!=null && target.getTargetType()==TargetType.HEAL)
    {
    Entity ent = target.getEntity(npc.worldObj);
    if(ent instanceof EntityLiving && !ent.isDead)
      {
      EntityLiving liv = (EntityLiving)ent;
      if(liv.getHealth()<liv.getMaxHealth())
        {
        return true;
        }
      }
    }
  npc.targetHelper.removeTarget(target);
  return false;  
  }

protected boolean setTarget()
  {
  ITargetEntry target = npc.targetHelper.getHighestAggroTargetInRange(TargetType.HEAL, 20);
  npc.setTargetAW(target);
  if(target==null)
    {
    return false;
    }
  return true;
  }

@Override
public void onObjectiveStart()
  {
  this.setTarget();
  }

@Override
public void stopObjective()
  {
  this.setFinished();
  }

@Override
public byte getObjectiveNum()
  {
  return heal;
  }

}
