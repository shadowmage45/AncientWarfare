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
package shadowmage.ancient_warfare.common.pathfinding.waypoints;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.utils.TargetType;

public class WayPoint implements INBTTaggable
{

protected TargetType targetType;
protected String name = "";
protected int x;
protected int y;
protected int z;
protected int side = 0;

public WayPoint(int x, int y, int z, TargetType type)
  {
  this.x = x;
  this.y = y;
  this.z = z;
  this.targetType = type;
  }

public WayPoint(int x, int y, int z, int side, TargetType type)
  {
  this(x,y,z, type);
  this.side = side;
  }

public WayPoint(int x, int y, int z, TargetType type, String name)
  {
  this(x,y,z,type);
  this.name = name;
  }

public WayPoint(NBTTagCompound tag)
  {
  this.readFromNBT(tag);
  }

public boolean isValidWayPoint(World world)
  {
  return true;
  }

public boolean areWayPointsEqual(WayPoint point)
  {
  return point.x==this.x && point.y==this.y && point.z==this.z;
  }

public int floorX()
  {
  return x;
  }

public int floorY()
  {
  return y;
  }

public int floorZ()
  {
  return z;
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("t", this.targetType.ordinal());
  tag.setString("n", this.name);
  tag.setIntArray("pos", new int[]{x,y,z});
  tag.setInteger("s", side);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.name = tag.getString("n");
  this.targetType = TargetType.values()[tag.getInteger("t")];
  int[] pos = tag.getIntArray("pos");
  if(tag.hasKey("s"))
    {
    this.side = tag.getInteger("s");
    }
  this.x = pos[0];
  this.y = pos[1];
  this.z = pos[2];
  }

@Override
public int hashCode()
  {
  final int prime = 31;
  int result = 1;
  result = prime * result + ((targetType == null) ? 0 : targetType.hashCode());
  result = prime * result + x;
  result = prime * result + y;
  result = prime * result + z;
  return result;
  }

@Override
public boolean equals(Object obj)
  {
  if (this == obj)
    return true;
  if (obj == null)
    return false;
  if (!(obj instanceof WayPoint))
    return false;
  WayPoint other = (WayPoint) obj;
  if (targetType != other.targetType)
    return false;
  if (x != other.x)
    return false;
  if (y != other.y)
    return false;
  if (z != other.z)
    return false;
  return true;
  }

@Override
public String toString()
  {
  return String.format("WayPoint: %d, %d, %d :: %s", x,y,z,targetType);
  }


}
