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

import org.bouncycastle.asn1.x509.Target;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.soldiers.NpcAI;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.config.Config;

public class AIMoveToTarget extends NpcAI
{

float prevDistance;
float distance;

int delayTicksMax = 10;
int delayTicks = 0;
int stuckTicks = 0;

/**
 * @param npc
 */
public AIMoveToTarget(NpcBase npc)
  {
  super(npc);  
  this.successTicks = 20;
  this.failureTicks = 20;
  this.taskType = MOVE_TO;
  this.taskName = "MoveToTarget";
  }

@Override
public int exclusiveTasks()
  {  
  return ATTACK +  REPAIR + HEAL + HARVEST; //action tasks
  }

@Override
public void onAiStarted()
  {
  stuckTicks = 0;
  delayTicks = 0;
  }

@Override
public void onTick()
  {
  if(npc.getTarget()==null)
    {
    this.finished = true;
    this.success = true;
    return;
    }
  float bX = npc.getTarget().posX();
  float bY = npc.getTarget().posY();
  float bZ = npc.getTarget().posZ();
  if(npc.getTarget().isEntityEntry)
    {
    Entity ent = npc.getTarget().getEntity();
    if(ent!=null && !ent.onGround)
      {
      //Config.logDebug("setting new target height for flying target");
      int x = MathHelper.floor_float(bX);
      int y = MathHelper.floor_float(bY);
      int z = MathHelper.floor_float(bZ);
      if(npc.worldObj.getBlockId(x, y, z)==Block.ladder.blockID)
        {
        //Config.logDebug("target on ladder, not adjusting");
        }
      else
        {
        while(y>1)
          {
          if(npc.worldObj.isBlockNormalCube(x, y, z))
            {
            break;
            }
          y--;
          }
        bY = y+1;
        }      
      }
    }
  //Config.logDebug("targetPos: "+bX+","+bY+","+bZ);
  this.prevDistance = this.distance;
  this.distance = (float) npc.getDistance(bX, bY, bZ);  
  float attackDistance = npc.targetHelper.getAttackDistance(npc.getTarget());
//  Config.logDebug("calc targetDist: "+npc.targetHelper.getAttackDistance(npc.getTarget()));
  if(distance < attackDistance)
    {
    this.finished = true;
    this.success = true;
    if(npc.getTargetType().equals(TARGET_WANDER))
      {
      npc.setTargetAW(null);
      }
//    Config.logDebug("MoveToTarget finished");
    return;
    }  
  delayTicks--;
  if(delayTicks>0)
    {
    return;
    }
  delayTicks = delayTicksMax;
  if(Trig.getAbsDiff(distance, prevDistance)<0.05f)
    {
    stuckTicks++;
    if(stuckTicks>10)
      {
      npc.setTargetAW(null);
      stuckTicks = 0;
      }
    }
//  if(npc.getTargetType().equals(NpcAI.TARGET_ATTACK))
//    {
//    float xAO = (float) (npc.posX - bX);  
//    float zAO = (float) (npc.posZ - bZ);
//    float yaw = Trig.toDegrees((float) Math.atan2(xAO, zAO));
//    float newLen = distance - (attackDistance * 1.2f);//move slightly inside min effective range attack distance
//    bX = (float)npc.posX + Trig.sinDegrees(yaw)*newLen;
//    bZ = (float)npc.posZ + Trig.cosDegrees(yaw)*newLen;
//    }
  //find new coordinate at x (attack distance) from target
  
  if(npc.isRidingVehicle())
    {
    ((VehicleBase)npc.ridingEntity).nav.setMoveTo(MathHelper.floor_float(bX), MathHelper.floor_float(bY), MathHelper.floor_float(bZ));
    }
  else
    {
    npc.nav.setMoveTo(MathHelper.floor_float(bX), MathHelper.floor_float(bY), MathHelper.floor_float(bZ));
    }
  }


}
