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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
    this.motionY -= this.ammoType.getGravityFactor();
    if(this.motionX != 0 || this.motionY != 0 || this.motionZ != 0)
      {     
      
      Vec3 pos = Vec3.createVectorHelper(posX, posY, posZ);
      Vec3 move = Vec3.createVectorHelper(posX+motionX, posY+motionY, posZ+motionZ);
      
//      MovingObjectPosition blockHit = worldObj.rayTraceBlocks(pos, move);
      MovingObjectPosition hit = this.worldObj.rayTraceBlocks_do_do(pos, move, false, true);   
      if(hit!=null)
        { 
        
        Config.logDebug("setting in ground");
        Config.logDebug("hitVec: "+hit.hitVec.toString());
        this.inGround = true;
//        this.posX = hit.hitVec.xCoord;
//        this.posY = hit.hitVec.yCoord;
//        this.posZ = hit.hitVec.zCoord;
//       
//        this.motionX = 0;
//        this.motionY = 0;
//        this.motionZ = 0;
        if(!this.ammoType.isPersistent() && !this.worldObj.isRemote)
          {
          this.setDead();
          }
          /**
           * TESTING
           */
        this.motionX = (double)((float)(hit.hitVec.xCoord - this.posX));
        this.motionY = (double)((float)(hit.hitVec.yCoord - this.posY));
        this.motionZ = (double)((float)(hit.hitVec.zCoord - this.posZ));
        float motionSpeed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.posX -= this.motionX / (double)motionSpeed * 0.05000000074505806D;
        this.posY -= this.motionY / (double)motionSpeed * 0.05000000074505806D;
        this.posZ -= this.motionZ / (double)motionSpeed * 0.05000000074505806D;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        } 
      else
        {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        this.onUpdateArrowRotation();
        }
      }
    }
  }

public void onUpdateArrowRotation()
  {
  double motionSpeed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
  this.rotationYaw =Trig.toDegrees((float) Math.atan2(this.motionX, this.motionZ)) - 90 ;
  this.rotationPitch = Trig.toDegrees((float)Math.atan2(this.motionY, (double)motionSpeed)) - 90;
  //Config.logDebug("setting rotPitch to: "+this.rotationPitch);
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
  this.currentGrav = tag.getFloat("grav");
  }

@Override
protected void writeEntityToNBT(NBTTagCompound tag)
  {
  tag.setInteger("type", missileType);
  tag.setFloat("grav", this.currentGrav);
  }

@Override
protected void entityInit()
  {
  }

@Override
public void writeSpawnData(ByteArrayDataOutput data)
  {
  data.writeInt(missileType);
  data.writeFloat(this.currentGrav);
  }

@Override
public void readSpawnData(ByteArrayDataInput data)
  {
  this.missileType =data.readInt();
  this.ammoType = AmmoRegistry.instance().getAmmoEntry(missileType);
  this.currentGrav = data.readFloat();
  }
}
