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
package shadowmage.ancient_warfare.common.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RayTraceUtils
{

public static MovingObjectPosition tracePathWithYawPitch(World world, float x, float y, float z, float yaw, float pitch, float range, float borderSize, HashSet<Entity> excluded)
  {
  float tx = x + (Trig.sinDegrees(yaw+180) * range * Trig.cosDegrees(pitch));
  float ty = (-Trig.sinDegrees(pitch) * range) + y;
  float tz = z + (Trig.cosDegrees(yaw) * range * Trig.cosDegrees(pitch));
  return tracePath(world, x, y, z, tx, ty, tz, borderSize, excluded);
  }

public static MovingObjectPosition getPlayerTarget(EntityPlayer player, float range, float border)
  {
  HashSet<Entity> excluded = new HashSet<Entity>();
  excluded.add(player);
  if(player.ridingEntity!=null)
    {
    excluded.add(player.ridingEntity);
    }
  return tracePathWithYawPitch(player.worldObj, (float)player.posX, (float)player.posY + (player.worldObj.isRemote ? 0.f : 1.62f), (float)player.posZ, player.rotationYaw, player.rotationPitch, range, border, excluded);  
  }

/**
 * 
 * @param world
 * @param x startX
 * @param y startY
 * @param z startZ
 * @param tx endX
 * @param ty endY
 * @param tz endZ
 * @param borderSize extra area to examine around line for entities
 * @param excluded any excluded entities (the player, etc)
 * @return a MovingObjectPosition of either the block hit (no entity hit), the entity hit (hit an entity), or null for nothing hit
 */
public static MovingObjectPosition tracePath(World world, float x, float y, float z, float tx, float ty, float tz, float borderSize, HashSet<Entity> excluded)
  {
  Vec3 startVec = Vec3.fakePool.getVecFromPool(x, y, z);
  Vec3 lookVec = Vec3.fakePool.getVecFromPool(tx-x, ty-y, tz-z);
  Vec3 endVec = Vec3.fakePool.getVecFromPool(tx, ty, tz);
  float minX = x < tx ? x : tx;
  float minY = y < ty ? y : ty;
  float minZ = z < tz ? z : tz;
  float maxX = x > tx ? x : tx;
  float maxY = y > ty ? y : ty; 
  float maxZ = z > tz ? z : tz;
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().getAABB(minX, minY, minZ, maxX, maxY, maxZ).expand(borderSize, borderSize, borderSize);
  List<Entity> allEntities = world.getEntitiesWithinAABBExcludingEntity(null, bb);  
  MovingObjectPosition blockHit = world.rayTraceBlocks(startVec, endVec);
  startVec = Vec3.fakePool.getVecFromPool(x, y, z);
  endVec = Vec3.fakePool.getVecFromPool(tx, ty, tz);
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


/**
 * wewt...only took like....8 tries and a whole day of thinking...but custom written from the ground up.
 * returns all hits (sometimes+1) between vectors 0 and 1 (x0, x1, etc...).  finds exact position hit on the block side as it is crossing into that block (does not maintain side information)
 * (only it doesn't work if any _1 coord is < the corresponding _0 coord (e.g. x1 < x0))
 * @param x0
 * @param y0
 * @param z0
 * @param x1
 * @param y1
 * @param z1
 * @return
 */
public static List<Pos3f> traceRay2(float x0, float y0, float z0, float x1, float y1, float z1)
  {
  List<Pos3f> hits = new ArrayList<Pos3f>();
  
  float travel = 0;
  float distance = Trig.getDistance(x0, y0, z0, x1, y1, z1);  
  float mx = x1-x0;
  float my = y1-y0;
  float mz = z1-z0;  
  boolean invertX = x0<x1;
  boolean invertY = y0<y1;
  boolean invertZ = z0<z1;
  float dx;
  float dy;
  float dz;
  float px;
  float py;
  float pz;
  
  float pUse;
  
  float x = x0;
  float y = y0;
  float z = z0;
  hits.add(new Pos3f(x,y,z));
  while(travel<distance)
    {    
    dx = 1 - (x % 1.f);
    dy = 1 - (y % 1.f);
    dz = 1 - (z % 1.f);
//    if(invertX){dx = 1-dx;}
//    if(invertY){dy = 1-dy;}
//    if(invertZ){dz = 1-dz;}
    px = Math.abs(mx== 0 ? 1.f : dx / mx);
    py = Math.abs(my== 0 ? 1.f : dy / my);
    pz = Math.abs(mz== 0 ? 1.f : dz / mz);
    pUse = px < py ? px : py;
    pUse = pUse < pz ? pUse : pz;
    x += mx*pUse;
    y += my*pUse;
    z += mz*pUse;
    hits.add(new Pos3f(x,y,z));
//    Config.logDebug("hit: "+hits.get(hits.size()-1).toString());
    travel += distance * pUse;
    }  
  return hits;
  }

}
