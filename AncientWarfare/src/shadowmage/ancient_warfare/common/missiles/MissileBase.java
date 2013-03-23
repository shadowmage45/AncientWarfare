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

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.interfaces.IMissileHitCallback;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.utils.Trig;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class MissileBase extends Entity implements IEntityAdditionalSpawnData
{

/**
 * Must be set after missile is constructed, but before spawned server side.  Client-side this will be set by the readSpawnData method.  This ammo type is responsible for many onTick qualities,
 * effects of impact, and model/render instance used.
 */
IAmmoType ammoType = null;
IMissileHitCallback shooter = null;
public int missileType = 0;
int rocketBurnTime = 0;

public boolean inGround = false;
int blockX;
int blockY;
int blockZ;
int blockID;
int blockMeta;

/**
 * initial velocities, used by rocket for acceleration factor
 */
float mX;
float mY;
float mZ;

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
  this.mX = mx;
  this.mY = my;
  this.mZ = mz;
  if(this.ammoType.updateAsArrow())
    {
    this.onUpdateArrowRotation();
    }
  this.prevRotationPitch = this.rotationPitch;
  this.prevRotationYaw = this.rotationYaw;
  if(this.ammoType.isRocket())//use launch power to determine rocket burn time...
    {
    float temp = MathHelper.sqrt_float(mx*mx+my*my+mz*mz);
    this.rocketBurnTime = (int) (temp*20.f*AmmoRocket.burnTimeFactor);
    
    this.mX = (float) (motionX/temp) *AmmoRocket.accelerationFactor;
    this.mY = (float) (motionY/temp) *AmmoRocket.accelerationFactor;
    this.mZ = (float) (motionZ/temp) *AmmoRocket.accelerationFactor;
    this.motionX = mX;
    this.motionY = mY;
    this.motionZ = mZ;    
    }
  }

public void setMissileParams2(IAmmoType ammo, float x, float y, float z, float yaw, float angle, float velocity)
  {
  float vX = -Trig.sinDegrees(yaw)* Trig.cosDegrees(angle) *velocity * 0.05f;
  float vY = Trig.sinDegrees(angle) * velocity  * 0.05f;
  float vZ = -Trig.cosDegrees(yaw)* Trig.cosDegrees(angle) *velocity  * 0.05f;
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
public void applyEntityCollision(Entity par1Entity)
  {
  //NOOP..they can't freaking collide in this manner...
  }

@Override
public void onUpdate()
  {
  this.ticksExisted++;
  super.onUpdate();
  this.onMovementTick();
  if(this.ticksExisted>6000 && !this.worldObj.isRemote)//5 min timer max for missiles...
    {
    this.setDead();
    }
  }

public void onMovementTick()
  {
  if(this.inGround)
    {
    int id = this.worldObj.getBlockId(blockX, blockY, blockZ);
    int meta = this.worldObj.getBlockMetadata(blockX, blockY, blockZ);
    if(id!=blockID || meta != blockMeta)
      {
      this.motionX = 0;
      this.motionY = 0;
      this.motionZ = 0;
      this.inGround = false;
      }
    }  
  if(!this.inGround)
    {    
    Vec3 positionVector = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
    Vec3 moveVector = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
    MovingObjectPosition hitPosition = this.worldObj.rayTraceBlocks_do_do(positionVector, moveVector, false, true);
    positionVector = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
    moveVector = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ); 
    Entity hitEntity = null;
    
    if(this.ticksExisted>10)//TODO set a firingEntity...or two..
      {
      List nearbyEntities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
      double closestHit = 0.0D;
      float borderSize;
      
      
      for (int i = 0; i < nearbyEntities.size(); ++i)
        {
        Entity curEnt = (Entity)nearbyEntities.get(i);
        if (curEnt.canBeCollidedWith())
          {
          borderSize = 0.3F;
          AxisAlignedBB var12 = curEnt.boundingBox.expand((double)borderSize, (double)borderSize, (double)borderSize);
          MovingObjectPosition checkHit = var12.calculateIntercept(positionVector, moveVector);
          if (checkHit != null)
            {
            double hitDistance = positionVector.distanceTo(checkHit.hitVec);
            if (hitDistance < closestHit || closestHit == 0.0D)
              {
              hitEntity = curEnt;
              closestHit = hitDistance;
              }
            }
          }
        }
      }    
    if (hitEntity != null)
      {
      hitPosition = new MovingObjectPosition(hitEntity);
      }
    if (hitPosition != null)
      {
      if (hitPosition.entityHit != null)
        {
        this.onImpactEntity(hitPosition.entityHit, (float)posX ,(float)posY, (float)posZ);
        if(!this.ammoType.isPersistent() && !this.worldObj.isRemote)
          {
          this.setDead();
          }
        else if(this.ammoType.isPersistent())
          {
          this.motionX *= - 0.25f;
          this.motionY *= - 0.25f;
          this.motionZ *= - 0.25f;
          }  
        }
      else
        {
        this.onImpactWorld();
        this.motionX = (double)((float)(hitPosition.hitVec.xCoord - this.posX));
        this.motionY = (double)((float)(hitPosition.hitVec.yCoord - this.posY));
        this.motionZ = (double)((float)(hitPosition.hitVec.zCoord - this.posZ));
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
        else if(this.ammoType.isPersistent())
          {
          this.blockX = hitPosition.blockX;
          this.blockY = hitPosition.blockY;
          this.blockZ = hitPosition.blockZ;
          this.blockID = this.worldObj.getBlockId(blockX, blockY, blockZ);
          this.blockMeta = this.worldObj.getBlockMetadata(blockX, blockY, blockZ);
          }          
        }
      }
    
    this.posX += this.motionX;
    this.posY += this.motionY;
    this.posZ += this.motionZ; 
    
    if(this.ammoType.isRocket() && this.rocketBurnTime>0)//if it is a rocket, accellerate if still burning
      {
      this.rocketBurnTime--;
      this.motionX += mX;
      this.motionY += mY;
      this.motionZ += mZ;
      if(this.worldObj.isRemote)
        {
        //TODO spawn particles...smoke..fire...wtf ever
        this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }
      }
    else
      {      
      this.motionY -= (double)this.ammoType.getGravityFactor();      
      }
    this.setPosition(this.posX, this.posY, this.posZ);
    if(this.ammoType.updateAsArrow())
      {
      this.onUpdateArrowRotation();
      }
    }  
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
  this.blockX = tag.getInteger("bX");
  this.blockY = tag.getInteger("bY");
  this.blockZ = tag.getInteger("bZ");
  this.blockID = tag.getInteger("bID");
  this.blockMeta = tag.getInteger("bMd");
  this.ticksExisted = tag.getInteger("ticks");
  this.mX = tag.getFloat("mX");
  this.mY = tag.getFloat("mY");
  this.mZ = tag.getFloat("mZ");
  }

@Override
protected void writeEntityToNBT(NBTTagCompound tag)
  {
  tag.setInteger("type", missileType);
  tag.setBoolean("inGround", this.inGround);
  tag.setInteger("bX", this.blockX);
  tag.setInteger("bY", this.blockY);
  tag.setInteger("bZ", this.blockZ);
  tag.setInteger("bID", this.blockID);
  tag.setInteger("bMd", this.blockMeta);
  tag.setInteger("ticks", this.ticksExisted);
  tag.setFloat("mX", this.mX);
  tag.setFloat("mY", this.mY);
  tag.setFloat("mZ", this.mZ);
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
  data.writeInt(blockX);
  data.writeInt(blockY);
  data.writeInt(blockZ);
  data.writeInt(blockID);
  data.writeInt(blockMeta);
  data.writeInt(rocketBurnTime);
  }

@Override
public void readSpawnData(ByteArrayDataInput data)
  {
  this.missileType =data.readInt();
  this.ammoType = AmmoRegistry.instance().getAmmoEntry(missileType);
  this.prevRotationYaw = this.rotationYaw = data.readFloat();
  this.prevRotationPitch = this.rotationPitch = data.readFloat();
  this.inGround = data.readBoolean();
  this.blockX = data.readInt();
  this.blockY = data.readInt();
  this.blockZ = data.readInt();
  this.blockID = data.readInt();
  this.blockMeta = data.readInt();
  this.rocketBurnTime = data.readInt();
  }
}
