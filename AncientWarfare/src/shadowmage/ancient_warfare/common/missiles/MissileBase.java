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

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;

public abstract class MissileBase extends Entity
{

/**
 * Must be set after missile is constructed, but before spawned server side.  Client-side this will be set by the readSpawnData method.  This ammo type is responsible for many onTick qualities,
 * effects of impact, and model/render instance used.
 */
IAmmoType ammoType = null;

float gravity = 0.006f;

/**
 * @param par1World
 */
public MissileBase(World par1World)
  {
  super(par1World);
  }

public void setAmmoType(IAmmoType type)
  {
  this.ammoType = type;
  if(ammoType!=null)
    {
    this.gravity = ammoType.getGravityFactor();    
    }
  }

public void setMissileParams(float x, float y, float z, float mx, float my, float mz)
  {
  float weightFactor = 1.f;
  if(this.ammoType!=null)
    {
    weightFactor = this.ammoType.getWeightFactor();
    }
  this.prevPosX = this.posX = x;
  this.prevPosY = this.posY = y;
  this.prevPosZ = this.posZ = z;
  this.motionX = mx * weightFactor;
  this.motionY = my * weightFactor;
  this.motionZ = mz * weightFactor;
  }

public void onImpactEntity(){}
public void onImpactWorld(){}


@Override
public void onUpdate()
  {
  super.onUpdate();
  this.onMovementTick();
  }

public void onMovementTick()
  {
  
  }

@Override
protected void readEntityFromNBT(NBTTagCompound var1)
  {
  // TODO Auto-generated method stub  
  }

@Override
protected void writeEntityToNBT(NBTTagCompound var1)
  {
  // TODO Auto-generated method stub  
  }

@Override
protected void entityInit()
  {
  }
}
