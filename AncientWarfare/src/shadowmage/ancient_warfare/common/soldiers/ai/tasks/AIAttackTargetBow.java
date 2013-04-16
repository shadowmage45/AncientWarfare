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

import java.util.HashSet;
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
import shadowmage.ancient_warfare.common.utils.RayTraceUtils;
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
  float accuracy = npc.npcType.getAccuracy(npc.rank);
  IAmmoType ammo = npc.npcType.getAmmoType(npc.rank);
  if(ammo!=null)
    {
    if(Config.adjustMissilesForAccuracy)
      {       
      yaw   += (float)rng.nextGaussian() * (1.f - accuracy)*10.f;
      angle += (float)rng.nextGaussian() * (1.f - accuracy)*10.f;    
      }  
    MissileBase missile = getMissile(ammo, x, y, z, yaw, angle, 20.f);    
    if(missile!=null)
      {
      npc.playSound("random.bow", 1.0F, 1.0F / (rng.nextFloat() * 0.4F + 0.8F));
      npc.swingItem();
      npc.worldObj.spawnEntityInWorld(missile);
      }
    }
  }

protected boolean isLineOfSightClear(AIAggroEntry target)
  {
  HashSet<Entity> excluded = new HashSet<Entity>();
  excluded.add(npc);
  MovingObjectPosition hit = RayTraceUtils.tracePath(npc.worldObj, (float)npc.posX, (float)npc.posY+npc.getEyeHeight(), (float)npc.posZ, target.posX(), target.posY(), target.posZ(), 0.3f, excluded);
  if(hit!=null && hit.entityHit!=null)
    {
    if(hit.entityHit!=target.getEntity())
      {
      return false;
      }
    }
  return true;
  }

}
