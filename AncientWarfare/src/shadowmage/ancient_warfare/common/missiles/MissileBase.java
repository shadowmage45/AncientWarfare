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
package shadowmage.ancient_warfare.common.missiles;

import java.util.List;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet70GameEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.interfaces.IMissileHitCallback;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.utils.Trig;

public class MissileBase extends Entity implements IEntityAdditionalSpawnData
{

/**
 * Must be set after missile is constructed, but before spawned server side.  Client-side this will be set by the readSpawnData method.  This ammo type is responsible for many onTick qualities,
 * effects of impact, and model/render instance used.
 */
IAmmoType ammoType = null;
IMissileHitCallback shooter = null;
public int missileType = 0;
public float currentGrav = 0.f;

public boolean inGround = false;

/**
 * @param par1World
 */
public MissileBase(World par1World)
  {
  super(par1World);
  this.entityCollisionReduction = 1.f;
  }


/**
 * called server side after creating but before spawning. ammoType is set client-side by the readSpawnData method, as should all other movement (rotation/motion) params.
 * @param type
 * @param x
 * @param y
 * @param z
 * @param mx
 * @param my
 * @param mz
 */
public void setMissileParams(IAmmoType type, float x, float y, float z, float mx, float my, float mz)
  {
  this.ammoType = type;
  if(ammoType!=null)
    {
    this.missileType = ammoType.getAmmoType();
    }  
  this.setPosition(x, y, z);
  this.prevPosX = this.posX;
  this.prevPosY = this.posY;
  this.prevPosZ = this.posZ;
  this.motionX = mx;
  this.motionY = my;
  this.motionZ = mz;

  this.onUpdateArrowRotation();
  this.prevRotationPitch = this.rotationPitch;
  this.prevRotationYaw = this.rotationYaw;
  }

public void setMissileParams2(IAmmoType ammo, float x, float y, float z, float yaw, float angle, float velocity)
  {
  float vX = -Trig.sinDegrees(yaw)* Trig.cosDegrees(angle) *velocity * 0.05f;
  float vY = Trig.sinDegrees(angle) * velocity  * 0.05f;
  float vZ = -Trig.cosDegrees(yaw)* Trig.cosDegrees(angle) *velocity  * 0.05f;
  Config.logDebug("computed velocity: "+vX+","+vY+","+vZ);
  Config.logDebug("linear velocity: "+ (MathHelper.sqrt_float(vX*vX+vY*vY+vZ*vZ)*20f));
  this.setMissileParams(ammo, x, y, z, vX, vY, vZ);
  }

public void setMissileCallback(IMissileHitCallback shooter)
  {
  this.shooter = shooter;
  }

public void onImpactEntity(Entity ent, float x, float y, float z)
  {
  this.ammoType.onImpactEntity(worldObj, ent, x, y, z);
  if(this.shooter!=null)
    {
    this.shooter.onMissileImpactEntity(worldObj, ent);
    }
  }

public void onImpactWorld()
  {
  this.ammoType.onImpactWorld(worldObj, (float)posX,(float)posY, (float)posZ);
  if(this.shooter!=null)
    {
    this.shooter.onMissileImpact(worldObj, (float)posX,(float)posY, (float)posZ);
    }
  }

@Override
public void onUpdate()
  {
  super.onUpdate();
  this.onMovementTick();
  }

public void onMovementTick()
  {
  if(!this.inGround)
    {
    this.onUpdateArrowRotation();
    this.onUpdateArrowMotion();
    }
  
  if(!this.inGround)
    {
    Vec3 var17 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
    Vec3 var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
    MovingObjectPosition var4 = this.worldObj.rayTraceBlocks_do_do(var17, var3, false, true);
    if (var4 != null)
      {
      if (var4.entityHit != null)
        {

        }
      else
        {
        this.motionX = (double)((float)(var4.hitVec.xCoord - this.posX));
        this.motionY = (double)((float)(var4.hitVec.yCoord - this.posY));
        this.motionZ = (double)((float)(var4.hitVec.zCoord - this.posZ));
        float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.posX -= this.motionX / (double)var20 * 0.05000000074505806D;
        this.posY -= this.motionY / (double)var20 * 0.05000000074505806D;
        this.posZ -= this.motionZ / (double)var20 * 0.05000000074505806D;
        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        if(!this.ammoType.isPersistent() && !this.worldObj.isRemote)
          {
          this.setDead();
          }
        }
      }
    this.posX += this.motionX;
    this.posY += this.motionY;
    this.posZ += this.motionZ; 
    this.motionY -= (double)this.ammoType.getGravityFactor();
    this.setPosition(this.posX, this.posY, this.posZ);
    }  
  }

public void onUpdateArrowMotion()
  {
  
  }

public void onUpdateArrowRotation()
  {
  double motionSpeed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
  this.rotationYaw =Trig.toDegrees((float) Math.atan2(this.motionX, this.motionZ)) - 90 ;
  this.rotationPitch = Trig.toDegrees((float)Math.atan2(this.motionY, (double)motionSpeed)) - 90;
  while(this.rotationPitch - this.prevRotationPitch < -180.0F)
    {
    this.prevRotationPitch -= 360.0F;
    }

  while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
    {
    this.prevRotationPitch += 360.0F;
    }

  while (this.rotationYaw - this.prevRotationYaw < -180.0F)
    {
    this.prevRotationYaw -= 360.0F;
    }

  while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
    {
    this.prevRotationYaw += 360.0F;
    }
  //Config.logDebug("setting rotPitch to: "+this.rotationPitch);
  }

@Override
public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
  {
  this.setPosition(par1, par3, par5); 
  }

@Override
public String getTexture()
  {
  return ammoType.getModelTexture();
  }

@Override
protected void readEntityFromNBT(NBTTagCompound tag)
  {
  this.missileType = tag.getInteger("type");
  this.ammoType = AmmoRegistry.instance().getAmmoEntry(missileType);
  this.inGround = tag.getBoolean("inGround");
  }

@Override
protected void writeEntityToNBT(NBTTagCompound tag)
  {
  tag.setInteger("type", missileType);
  tag.setBoolean("inGround", this.inGround);
  }

@Override
protected void entityInit()
  {
  }

@Override
public void writeSpawnData(ByteArrayDataOutput data)
  {
  data.writeInt(missileType);
  data.writeFloat(rotationYaw);
  data.writeFloat(rotationPitch);
  data.writeBoolean(inGround);
  }

@Override
public void readSpawnData(ByteArrayDataInput data)
  {
  this.missileType =data.readInt();
  this.ammoType = AmmoRegistry.instance().getAmmoEntry(missileType);
  this.rotationYaw = data.readFloat();
  this.rotationPitch = data.readFloat();
  this.inGround = data.readBoolean();
  }
}
