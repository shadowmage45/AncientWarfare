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
package shadowmage.ancient_warfare.common.civics.worksite;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class WorkPoint
{

public final Entity target;
public final int x;
public final int y;
public final int z;
public final byte special;
public final TargetType work;

public WorkPoint(int x, int y, int z, TargetType type)
  {
  this.x = x;
  this.y = y;
  this.z = z;
  this.work = type;
  target = null;
  special = 0;
  }

public WorkPoint(int x, int y, int z, byte special, TargetType t)
  {
  this.x = x;
  this.y = y;
  this.z = z;
  this.work = t;
  target = null;
  this.special = special;
  }

public WorkPoint(Entity ent, TargetType type)
  {
  x = 0;
  y = 0;
  z = 0;
  this.target = ent;
  this.work = type;
  special = 0;
  }

@Override
public String toString()
  {
  return "Work Point: "+x+","+y+","+z+"::"+work;
  }

/**
 * currently only writes out x,y,z,meta,workType
 * @param tag
 * @return
 */
public NBTTagCompound writeToNBT(NBTTagCompound tag)
  {
  tag.setInteger("x", x);
  tag.setInteger("y", y);
  tag.setInteger("z", z);
  tag.setByte("s", special);
  tag.setInteger("t", work.ordinal());
//  if(target!=null)
//    {
//    tag.setLong("idmsb", target.getPersistentID().getMostSignificantBits());
//    tag.setLong("idlsb", target.getPersistentID().getLeastSignificantBits());
//    }
  return tag;
  }

/**
 * does not maintain/load any entity target of the work point..no reference to worldObj
 * @param tag
 * @return
 */
public static WorkPoint constructFromNBT(NBTTagCompound tag)
  {
  int x = tag.getInteger("x");
  int y = tag.getInteger("y");
  int z = tag.getInteger("z");
  byte s = tag.getByte("s");
  TargetType work = TargetType.values()[tag.getInteger("t")];  
  WorkPoint p = new WorkPoint(x,y,z, s, work);
  return p;
  }

}
