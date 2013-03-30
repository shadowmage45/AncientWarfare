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
package shadowmage.ancient_warfare.common.soldiers.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcAI;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AIAttackTarget extends NpcAI
{

int maxAttackDelayTicks = 35;//should set this from soldier somewhere..

int attackDelayTicks = 0;
int blockAttackHits = 0;

/**
 * @param npc
 */
public AIAttackTarget(NpcBase npc)
  {
  super(npc);
  this.taskType = ATTACK;
  this.successTicks = 1;
  this.failureTicks = 1;
  this.taskName = "AttackTarget";
  }

@Override
public int exclusiveTasks()
  {
  return MOVE_TO + HARVEST + REPAIR + HEAL;
  }

@Override
public void onAiStarted()
  {
  attackDelayTicks = 0;
  blockAttackHits = 0;
  }

@Override
public void onTick()
  {
  AIAggroEntry target = npc.getTarget();
  if(target!=null && npc.getTargetType().equals("attack"))
    {
    if(target.getDistanceFrom() < npc.targetHelper.getAttackDistance(target))
      {      
      if(attackDelayTicks>0)
        {
        attackDelayTicks--;
        return;
        }      
      Config.logDebug("Attacking target");
      this.attackTarget(target);
      if(this.checkIfTargetDead(target))
        {
        this.success= true;
        this.finished = true;
        }          
      }
    else
      {
      this.success = false;
      this.finished = true;
      Config.logDebug("not at target yet, moving closer");
      } 
    }  
  else
    {
    Config.logDebug("not attacking target... (no target)");
    this.success = false;
    this.finished = true;
    }
  }

protected void attackTarget(AIAggroEntry target)
  {
  if(npc.isRidingVehicle())
    {
    attackTargetMounted(target);
    return;
    }
  
  attackDelayTicks =  maxAttackDelayTicks;
  
  if(!target.isEntityEntry)
    {
    Config.logDebug("doing block attack");
    blockAttackHits++;    
    int id = npc.worldObj.getBlockId((int)target.posX(), (int)target.posY(),(int)target.posZ());
    Block block = Block.blocksList[id];
    if(id!=0 && block!=null)
      {
      if(blockAttackHits>=(int)block.getBlockHardness(npc.worldObj, (int)target.posX(), (int)target.posY(),(int)target.posZ()))
        {
        BlockTools.breakBlockAndDrop(npc.worldObj, (int)target.posX(), (int)target.posY(),(int)target.posZ());
        }
      }
    }
  else
    {
    Config.logDebug("doing entity attack");
    Entity ent = target.getEntity();
    if(ent!=null)
      {
      npc.attackEntityAsMob(ent);
      }
    }
  
  }

protected void attackTargetMounted(AIAggroEntry target)
  {
  VehicleBase vehicle = (VehicleBase) npc.ridingEntity;
  this.attackDelayTicks = vehicle.currentReloadTicks + 5;
  }

protected boolean checkIfTargetDead(AIAggroEntry target)
  {
  if(target.getEntity()!=null && target.getEntity().isDead)
    {
    return true;
    }
  else if(!target.isEntityEntry)
    {
    if(npc.worldObj.getBlockId((int)target.posX(), (int)target.posY(),(int)target.posZ())==0)
      {
      return true;
      }
    }
  else if(target.isEntityEntry && target.getEntity()==null)
    {
    npc.setTargetAW(null);
    return true;
    }  
  return false;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  
  }

@Override
public NBTTagCompound getNBTTag()
  {
  return null;
  }

}
