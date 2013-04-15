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
package shadowmage.ancient_warfare.common.utils;

import java.util.HashSet;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RayTraceUtils
{

public static MovingObjectPosition tracePathWithYawPitch(World world, float x, float y, float z, float yaw, float pitch, float range, float borderSize, HashSet<Entity> excluded)
  {
  float tx = Trig.sinDegrees(yaw)*range*Trig.cosDegrees(pitch) + x;
  float ty = Trig.sinDegrees(pitch)*range+y;
  float tz = Trig.cosDegrees(yaw)*range*Trig.cosDegrees(pitch) + z;
  return tracePath(world, x, y, z, tx, ty, tz, borderSize, excluded);
  }

public static MovingObjectPosition tracePath(World world, float x, float y, float z, float tx, float ty, float tz, float borderSize, HashSet<Entity> excluded)
  {
  Vec3 startVec = Vec3.vec3dPool.getVecFromPool(x, y, z);
  Vec3 lookVec = Vec3.vec3dPool.getVecFromPool(tx-x, ty-y, tz-z);
  Vec3 endVec = Vec3.vec3dPool.getVecFromPool(tx, ty, tz);
  float minX = x < tx ? x : tx;
  float minY = y < ty ? y : ty;
  float minZ = z < tz ? z : tz;
  float maxX = x > tx ? x : tx;
  float maxY = y > ty ? y : ty; 
  float maxZ = z > tz ? z : tz;
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(minX, minY, minZ, maxX, maxY, maxZ).expand(borderSize, borderSize, borderSize);
  List<Entity> allEntities = world.getEntitiesWithinAABBExcludingEntity(null, bb);  
  MovingObjectPosition blockHit = world.rayTraceBlocks(startVec, endVec);
  startVec = Vec3.vec3dPool.getVecFromPool(x, y, z);
  endVec = Vec3.vec3dPool.getVecFromPool(tx, ty, tz);
  float maxDistance = (float) endVec.distanceTo(startVec);
  if(blockHit!=null)
    {
    maxDistance = (float) blockHit.hitVec.distanceTo(startVec);
    }  
  Entity closestHitEntity = null;
  float closestHit = Float.POSITIVE_INFINITY;
  float currentHit = 0.f;
  AxisAlignedBB entityBb;// = ent.getBoundingBox();
  MovingObjectPosition intercept;
  for(Entity ent : allEntities)
    {    
    if(ent.canBeCollidedWith() && !excluded.contains(ent))
      {
      float entBorder =  ent.getCollisionBorderSize();
      entityBb = ent.boundingBox;
      if(entityBb!=null)
        {
        entityBb = entityBb.expand(entBorder, entBorder, entBorder);
        intercept = entityBb.calculateIntercept(startVec, endVec);
        if(intercept!=null)
          {
          currentHit = (float) intercept.hitVec.distanceTo(startVec);
          if(currentHit < closestHit || currentHit==0)
            {            
            closestHit = currentHit;
            closestHitEntity = ent;
            }
          } 
        }
      }
    }  
  if(closestHitEntity!=null)
    {
    blockHit = new MovingObjectPosition(closestHitEntity);
    }
  return blockHit;
  }


}
