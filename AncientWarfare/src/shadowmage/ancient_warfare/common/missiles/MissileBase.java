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
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.utils.Trig;

public class MissileBase extends Entity implements IEntityAdditionalSpawnData
{

/**
 * Must be set after missile is constructed, but before spawned server side.  Client-side this will be set by the readSpawnData method.  This ammo type is responsible for many onTick qualities,
 * effects of impact, and model/render instance used.
 */
IAmmoType ammoType = null;
public int missileType = 0;
public float currentGrav = 0.f;

/**
 * @param par1World
 */
public MissileBase(World par1World)
  {
  super(par1World);
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
  this.setMissileParams(ammo, x, y, z, vX, vY, vZ);
  }

public void onImpactEntity(Entity ent, float x, float y, float z)
  {
  this.ammoType.onImpactEntity(worldObj, ent, x, y, z);
  }

public void onImpactWorld()
  {
  this.ammoType.onImpactWorld(worldObj, (float)posX,(float)posY, (float)posZ);  
  }

@Override
public void onUpdate()
  {
  super.onUpdate();
  this.onMovementTick();
  }


int tickNum = 0;

public void onMovementTick()
  {
  
  //this.currentGrav += ;
  this.motionY -= this.ammoType.getGravityFactor();
  
  if(!this.worldObj.isRemote)
    {
    //Config.logDebug("tickNum: "+tickNum+" :: "+motionY+" :: "+currentGrav);
    tickNum++;
    }
  
  if(this.motionX != 0 || this.motionY != 0 || this.motionZ != 0)
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
//    float radAng = (float) Math.atan2(motionZ, motionX);
//    this.rotationYaw = Trig.toDegrees(radAng) - 90;
//    
//    float velH = (float) Math.sqrt(motionX*motionX + motionZ*motionZ);//the X in the pitch setting..
//    this.rotationPitch = Trig.toDegrees((float) Math.atan2(motionY, velH));     
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
