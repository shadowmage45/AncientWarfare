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
package shadowmage.ancient_warfare.common.npcs.ai.objectives;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIAttackTarget;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AIAttackTargets extends NpcAIObjective
{

int minRange = 20;
int maxRange = 20;
/**
 * @param npc
 * @param maxPriority
 */
public AIAttackTargets(NpcBase npc, int maxPriority, int minRange, int maxRange)
  {
  super(npc, maxPriority);
  this.minRange = minRange;
  this.maxRange = maxRange;
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 1.f, true));  
  this.aiTasks.add(new AIAttackTarget(npc));
  }

@Override
public void updatePriority()
  {  
//  Config.logDebug("checking if targets are in range: ");
  if(npc.targetHelper.areTargetsInRange(TargetType.ATTACK, maxRange))
    {
//    Config.logDebug("attack targets in range: setting attack priority to max: "+this.maxPriority);
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
  if(checkIfTargetDead())
    {
    if(!findTarget())
      {
      this.setFinished();
      }
    } 
  }

protected boolean checkIfTargetDead()
  {
  ITargetEntry target = npc.getTarget();
  if(target!=null)
    {
    if(target.isEntityEntry())
      {
      Entity ent = target.getEntity(npc.worldObj);
      if(ent!=null)
        {
        if(ent.isDead)
          {
          return true;
          }
        if(ent instanceof EntityLiving)
          {
          EntityLiving liv = (EntityLiving)ent;
          if(liv.getHealth()<=0)
            {
            return true;
            }
          }
        return false;
        }
      else
        {
        return true;
        }
      }
    else if(target.isTargetLoaded(npc.worldObj) && npc.worldObj.getBlockId(target.floorX(), target.floorY(), target.floorZ())!=0)
      {
      return false;
      }
    }
  return true;
  }

protected boolean findTarget()
  {
  ITargetEntry target = npc.targetHelper.getHighestAggroTargetInRange(TargetType.ATTACK, maxRange);
  if(target!=null)
    {
    npc.setTargetAW(target);
    return true;
    }
  return false;
  }

@Override
public void onObjectiveStart()
  {
  npc.setTargetAW(npc.targetHelper.getHighestAggroTargetInRange(TargetType.ATTACK, maxRange));
  }

@Override
public void stopObjective()
  {
  npc.setTargetAW(null);
  if(npc.isRidingVehicle())
    {
    VehicleBase vehicle = (VehicleBase) npc.ridingEntity;
    vehicle.moveHelper.clearInputFromDismount();
    }
  }

@Override
public byte getObjectiveNum()
  {
  return attack;
  }

}
