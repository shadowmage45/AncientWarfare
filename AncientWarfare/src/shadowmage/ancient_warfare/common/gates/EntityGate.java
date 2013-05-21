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
package shadowmage.ancient_warfare.common.gates;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.gates.types.Gate;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * an class to represent ALL gate types
 * @author Shadowmage
 *
 */
public class EntityGate extends Entity implements IEntityAdditionalSpawnData
{

public BlockPosition pos1;
public BlockPosition pos2;

public float edgePosition;//the bottom/opening edge of the gate (closed should correspond to pos1)

float openingSpeed = 0.f;//calculated speed of the opening gate -- used during animation

Gate gateType = null;

int teamNum = 0;

/**
 * @param par1World
 */
public EntityGate(World par1World)
  {
  super(par1World);
  }

public Gate getGateType()
  {
  return this.gateType;
  }

public void setGateType(Gate type)
  {
  this.gateType = type;
  Config.logDebug("setting entity gate type to: "+type);
  }

@Override
protected void entityInit()
  {
  this.dataWatcher.addObject(31, Integer.valueOf(40));
  this.dataWatcher.addObject(30, Byte.valueOf((byte)0));
  }

public void setOpeningStatus(byte op)
  {
  this.dataWatcher.updateObject(30, Byte.valueOf(op));
  }

public byte getOpeningStatus()
  {
  return this.dataWatcher.getWatchableObjectByte(30);
  }

public int getHealth()
  {
  return (int)this.dataWatcher.getWatchableObjectInt(31);
  }

public void setHealth(int val)
  {
  if(val<0)
    {
    val = 0;
    }
  this.dataWatcher.updateObject(31, Integer.valueOf(val));
  }

@Override
public void setPosition(double par1, double par3, double par5)
  {
  super.setPosition(par1, par3, par5);
  if(this.gateType!=null)
  {
	  this.gateType.setCollisionBoundingBox(this);  
  }
  }

@Override
public void onUpdate()
  {
  super.onUpdate();
  this.gateType.onUpdate(this);
  }

@Override
public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
  {
  int health = this.getHealth();
  health -= par2;
  this.setHealth(health);
  if(health<=0)
    {
    this.setDead();
    }
  return super.attackEntityFrom(par1DamageSource, par2);
  }

@Override
public boolean canBeCollidedWith()
  {
  return true;
  }

@Override
public boolean canBePushed()
  {
  return false;
  }

@Override
public String getTexture()
  {
  return gateType.getTexture();
  }

@Override
public float getShadowSize()
  {
  return 0.f;
  }

@Override
protected void readEntityFromNBT(NBTTagCompound tag)
  {
  this.pos1 = new BlockPosition(tag.getCompoundTag("pos1"));
  this.pos2 = new BlockPosition(tag.getCompoundTag("pos2"));
  this.setGateType(Gate.getGateByID(tag.getInteger("type")));
  this.teamNum = tag.getInteger("team");
  this.edgePosition = tag.getFloat("edge");
  this.setHealth(tag.getInteger("health"));
  }

@Override
protected void writeEntityToNBT(NBTTagCompound tag)
  {
  tag.setCompoundTag("pos1", pos1.writeToNBT(new NBTTagCompound()));
  tag.setCompoundTag("pos2", pos2.writeToNBT(new NBTTagCompound()));
  tag.setInteger("type", this.gateType.getGlobalID());
  tag.setInteger("team", teamNum);
  tag.setFloat("edge", this.edgePosition);
  tag.setInteger("health", this.getHealth());
  }

@Override
public void writeSpawnData(ByteArrayDataOutput data)
  {
  data.writeInt(pos1.x);
  data.writeInt(pos1.y);
  data.writeInt(pos1.z);
  data.writeInt(pos2.x);
  data.writeInt(pos2.y);
  data.writeInt(pos2.z);
  data.writeInt(this.gateType.getGlobalID());  
  data.writeInt(this.teamNum);
  data.writeFloat(this.edgePosition);
  }

@Override
public void readSpawnData(ByteArrayDataInput data)
  {
  this.pos1 = new BlockPosition(data.readInt(), data.readInt(), data.readInt());
  this.pos2 = new BlockPosition(data.readInt(), data.readInt(), data.readInt());
  this.gateType = Gate.getGateByID(data.readInt());
  this.teamNum = data.readInt();
  this.edgePosition = data.readFloat();
  }

}
