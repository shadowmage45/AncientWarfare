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
package shadowmage.ancient_warfare.common.npcs.waypoints;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.targeting.TargetPosition;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.EntityTools;

public class WayPoint extends TargetPosition
{

protected Entity entTarget = null;
UUID entityID;

public WayPoint(WayPoint p)
  {
  super(p.getTargetType());
  this.x = p.x;
  this.y = p.y;
  this.z = p.z;
  this.setSide(p.getSide());
  this.entTarget = p.entTarget;
  this.entityID = p.entityID;
  }

public WayPoint(TileEntity te, int side, TargetType t)
  {
  super(t);
  this.x = te.xCoord;
  this.y = te.yCoord;
  this.z = te.zCoord;
  this.setSide(side);  
  }

protected WayPoint(TargetType type)
  {
  super(type);
  }

public WayPoint(NBTTagCompound tag)
  {
  super(tag);
  }

public WayPoint(int x, int y, int z, TargetType type)
  {
  super(x,y,z, type);
  }

public WayPoint(int x, int y, int z, int side, TargetType type)
  {
  super(x,y,z, side, type);
  }

public WayPoint(Entity ent, TargetType type)
  { 
  super(type);
  this.x =MathHelper.floor_double(ent.posX);
  this.y =MathHelper.floor_double(ent.posY);
  this.z =MathHelper.floor_double(ent.posZ);
  this.entTarget = ent;
  this.entityID = ent.getPersistentID();
  }

public TileEntity getTileEntity(World world)
  {
  return world.getBlockTileEntity(x, y, z);
  }

@Override
public boolean isEntityEntry()
  {
  return this.entityID!=null || this.entTarget!=null;
  }

@Override
public int floorX()
  {
  if(entTarget!=null)
    {
    return MathHelper.floor_double(entTarget.posX);
    }
  return super.floorX();
  }

@Override
public int floorY()
  {
  if(entTarget!=null)
    {
    return MathHelper.floor_double(entTarget.posY);
    }
  return super.floorY();
  }

@Override
public int floorZ()
  {
  if(entTarget!=null)
    {
    return MathHelper.floor_double(entTarget.posZ);
    }
  return super.floorZ();
  }

@Override
public float posX()
  {
  if(entTarget!=null)
    {
    return (float) entTarget.posX;
    }
  return super.posX();
  }

@Override
public float posY()
  {
  if(entTarget!=null)
    {
    return (float) entTarget.posY;
    }
  return super.posY();
  }

@Override
public float posZ()
  {
  if(entTarget!=null)
    {
    return (float) entTarget.posZ;
    }
  return super.posZ();
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = super.getNBTTag();
  if(entTarget!=null)
    {
    tag.setLong("idmsb", entTarget.getPersistentID().getMostSignificantBits());
    tag.setLong("idlsb", entTarget.getPersistentID().getLeastSignificantBits());
    }
  else if(entityID!=null)
    {
    tag.setLong("idmsb", entityID.getMostSignificantBits());
    tag.setLong("idlsb", entityID.getLeastSignificantBits());
    }
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {  
  super.readFromNBT(tag);
  if(tag.hasKey("idmsb") && tag.hasKey("idlsb"))
    {
    entityID = new UUID(tag.getLong("idmsb"), tag.getLong("idlsb"));
    } 
  }

@Override
public Entity getEntity(World world)
  {
  if(this.entTarget==null && this.entityID!=null)
    {
    this.entTarget = EntityTools.getEntityByUUID(world, entityID.getMostSignificantBits(), entityID.getLeastSignificantBits());
    }
  return this.entTarget;
  }
}
