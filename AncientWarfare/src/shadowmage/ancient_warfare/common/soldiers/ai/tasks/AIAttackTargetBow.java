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

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;
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
  attackDelayTicks =  maxAttackDelayTicks / Config.npcAITicks;  
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
  float x = (float) npc.posX+npc.getEyeHeight();
  float y = (float) npc.posY;
  float z = (float) npc.posZ;
  float tx = (float) (target.posX - x);
  float ty = (float) (target.posY - y)+0.5f;
  float tz = (float) (target.posZ - z);
  float angle = Trig.getLaunchAngleToHit(tx, ty, tz, 20.f).value();
  float accuracy = 0.85f;
  IAmmoType ammo = Ammo.ammoSoldierArrowWood;
  if(Config.adjustMissilesForAccuracy)
    {       
    yaw   += (float)rng.nextGaussian() * (1.f - accuracy)*10.f;
    angle += (float)rng.nextGaussian() * (1.f - accuracy)*10.f;    
    }
  
  MissileBase missile = getMissile(ammo, (float)npc.posX, (float)npc.posY+npc.getEyeHeight(), (float)npc.posZ, yaw, angle, 20.f);

//  EntityArrow var2 = new EntityArrow(npc.worldObj, npc, 1.0F);  
//  var2.posY = npc.posY + (double)npc.getEyeHeight() - 0.10000000149011612D;
//  double var6 = target.posX - npc.posX;
//  double var8 = target.posY + (double)target.height - 0.699999988079071D - var2.posY;
//  double var10 = target.posZ - npc.posZ;
//  double var12 = (double)MathHelper.sqrt_double(var6 * var6 + var10 * var10);
//
//  if (var12 >= 1.0E-7D)
//    {
//    float var14 = (float)(Math.atan2(var10, var6) * 180.0D / Math.PI) - 90.0F;
//    float var15 = (float)(-(Math.atan2(var8, var12) * 180.0D / Math.PI));
//    double var16 = var6 / var12;
//    double var18 = var10 / var12;
//    var2.setLocationAndAngles(npc.posX + var16, var2.posY, npc.posZ + var18, var14, var15);
//    var2.yOffset = 0.0F;
//    float var20 = (float)var12 * 0.2F;
//    var2.setThrowableHeading(var6, var8 + (double)var20, var10, 1.6f, 12.f);
//    }
//  
//  int var3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, npc.getHeldItem());
//  int var4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, npc.getHeldItem());
//
//  if (var3 > 0)
//    {
//    var2.setDamage(var2.getDamage() + (double)var3 * 0.5D + 0.5D);
//    }
//
//  if (var4 > 0)
//    {
//    var2.setKnockbackStrength(var4);
//    }
//
//  if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, npc.getHeldItem()) > 0)
//    {
//    var2.setFire(100);
//    }
//  npc.worldObj.spawnEntityInWorld(var2);
  npc.playSound("random.bow", 1.0F, 1.0F / (rng.nextFloat() * 0.4F + 0.8F));
  if(missile!=null)
    {
    npc.worldObj.spawnEntityInWorld(missile);
    }
  }

}
