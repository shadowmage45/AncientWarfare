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
package shadowmage.ancient_warfare.common.soldiers.ai.tasks;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.missiles.Ammo;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;
import shadowmage.ancient_warfare.common.vehicles.missiles.MissileBase;

public class AIAttackTargetBow extends AIAttackTarget
{

/**
 * @param npc
 */
public AIAttackTargetBow(NpcBase npc)
  {
  super(npc);
  }

@Override
protected void attackTarget(AIAggroEntry target)
  { 
  if(!isLineOfSightClear(target))
    {
    attackDelayTicks = 2;
    return;
    }
  else
    {
    attackDelayTicks =  maxAttackDelayTicks / Config.npcAITicks;
    }
  if(!target.isEntityEntry)
    {
    Config.logDebug("doing block attack");
    blockAttackHits++;    
    int id = npc.worldObj.getBlockId((int)target.posX(), (int)target.posY(),(int)target.posZ());
    Block block = Block.blocksList[id];
    if(id!=0 && block!=null)
      {
      if(blockAttackHits>=(int) block.getBlockHardness(npc.worldObj, (int)target.posX(), (int)target.posY(),(int)target.posZ()) * 2)
        {
        BlockTools.breakBlockAndDrop(npc.worldObj, (int)target.posX(), (int)target.posY(),(int)target.posZ());
        }
      }
    }
  else
    {
    Config.logDebug("doing entity attack with bow");
    Entity ent = target.getEntity();
    if(ent!=null)
      {
      doBowAttack(ent);
      }
    }  
  }

public MissileBase getMissile(IAmmoType ammo, float x, float y, float z, float yaw, float pitch, float velocity)
  {
  if(ammo!=null)
    {
    MissileBase missile = new MissileBase(npc.worldObj); 
    if(ammo.hasSecondaryAmmo())
      {
      ammo = ammo.getSecondaryAmmoType();
      }
    missile.setMissileParams2(ammo, x, y, z, yaw, pitch, velocity);
    missile.setLaunchingEntity(npc);
    return missile;
    }
  return null;
  }


protected void doBowAttack(Entity target)
  {
  float xAO = (float) (npc.posX  - target.posX);  
  float zAO = (float) (npc.posZ - target.posZ);
  float yaw = Trig.toDegrees((float) Math.atan2(xAO, zAO));
  float x = (float) npc.posX;
  float y = (float) npc.posY+npc.getEyeHeight();
  float z = (float) npc.posZ;
  float tx = (float) (target.posX - x);
  float ty = (float) (target.posY+target.height*0.5f - y);
  float tz = (float) (target.posZ - z);
  float angle = Trig.getLaunchAngleToHit(tx, ty, tz, 20.f).value();
  float accuracy = 0.85f;
  IAmmoType ammo = Ammo.ammoSoldierArrowWood;
  if(Config.adjustMissilesForAccuracy)
    {       
    yaw   += (float)rng.nextGaussian() * (1.f - accuracy)*10.f;
    angle += (float)rng.nextGaussian() * (1.f - accuracy)*10.f;    
    }  
  MissileBase missile = getMissile(ammo, x, y, z, yaw, angle, 20.f);
  npc.playSound("random.bow", 1.0F, 1.0F / (rng.nextFloat() * 0.4F + 0.8F));
  if(missile!=null)
    {
    npc.worldObj.spawnEntityInWorld(missile);
    }
  }

protected boolean isLineOfSightClear(AIAggroEntry target)
  {
//  Vec3 sourcePos = Vec3.vec3dPool.getVecFromPool(npc.posX, npc.posY+npc.getEyeHeight(), npc.posZ);
//  float rx = (float) (target.posX()-npc.posX);
//  float ry = (float) (target.posY()-npc.posY);
//  float rz = (float) (target.posZ()-npc.posZ);
//  float len = MathHelper.sqrt_float(rx*rx+ry*ry+rz*rz);
//  Vec3 lookVector = Vec3.vec3dPool.getVecFromPool(rx/len, ry/len, rz/len);
//  MovingObjectPosition hit = getEntityHit(npc.worldObj, sourcePos, lookVector, npc, 20, npc, 2.f);
//  Config.logDebug("target: "+target.posX()+","+target.posY()+","+target.posZ());
//  if(hit!=null && hit.entityHit!=null)
//    {
//    if(hit.entityHit!=target.getEntity())
//      {
//      Config.logDebug("hit entity!! "+hit.entityHit);      
//      return false;
//      }
//    }
  return true;
  }

public MovingObjectPosition getEntityHit(World world, Vec3 sourcePos, Vec3 lookVector, Entity sourceEntity, float range, Entity excludedEntity, float borderSize)
  {  
  Vec3 originalSource = Vec3.vec3dPool.getVecFromPool(sourcePos.xCoord, sourcePos.yCoord, sourcePos.zCoord);
  Vec3 originalLook = Vec3.vec3dPool.getVecFromPool(lookVector.xCoord, lookVector.yCoord, lookVector.zCoord);  
  Vec3 endVector = sourcePos.addVector(lookVector.xCoord * range, lookVector.yCoord * range, lookVector.zCoord * range);
  Config.logDebug("source: "+sourcePos);
  Config.logDebug("look: "+lookVector);
  Config.logDebug("end: "+endVector);
  MovingObjectPosition blockHit = world.rayTraceBlocks(sourcePos, endVector);
  
  /**
   * reseat vectors, as they get fucked with in the rayTrace...
   */
  sourcePos = originalSource;;
  lookVector = originalLook;
  
  float var9 = 1.f;
  
  float closestFound = 0.f;
  if(blockHit!=null)
    {
    closestFound = (float) blockHit.hitVec.distanceTo(sourcePos);
    }
  List possibleHitEntities = world.getEntitiesWithinAABBExcludingEntity(excludedEntity, sourceEntity.boundingBox.addCoord(lookVector.xCoord * range, lookVector.yCoord * range, lookVector.zCoord * range).expand((double)var9, (double)var9, (double)var9));
  Iterator<Entity> it = possibleHitEntities.iterator();
  Entity hitEntity = null;
  Entity currentExaminingEntity = null;
  while(it.hasNext())
    {
    currentExaminingEntity = it.next();
    if(currentExaminingEntity == excludedEntity)
      {
      continue;
      }
    if(currentExaminingEntity.canBeCollidedWith())
      {
      borderSize = currentExaminingEntity.getCollisionBorderSize();
      AxisAlignedBB entBB = currentExaminingEntity.boundingBox.expand((double)borderSize, (double)borderSize, (double)borderSize);
      MovingObjectPosition var17 = entBB.calculateIntercept(sourcePos, endVector);
      if (entBB.isVecInside(sourcePos))
        {
        if (0.0D < closestFound || closestFound == 0.0D)
          {
          hitEntity = currentExaminingEntity;
          closestFound = 0.0f;
          }
        }
      else if (var17 != null)
        {
        double var18 = sourcePos.distanceTo(var17.hitVec);
        if (var18 < closestFound || closestFound == 0.0D)
          {
          hitEntity = currentExaminingEntity;
          closestFound = (float) var18;
          }
        }
      }   
    }
  if(hitEntity!=null)
    {
//    Config.logDebug("entity hit!!");
    blockHit = new MovingObjectPosition(hitEntity);
    }
  return blockHit;
  }

}
