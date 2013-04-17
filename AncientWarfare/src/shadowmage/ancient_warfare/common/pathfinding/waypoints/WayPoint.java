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
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.utils.TargetType;

public class WayPoint implements INBTTaggable
{

TargetType type;
String name = "";
public int x;
public int y;
public int z;

public WayPoint(int x, int y, int z, TargetType type)
  {
  this.x = x;
  this.y = y;
  this.z = z;
  this.type = type;
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

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("t", this.type.ordinal());
  tag.setString("n", this.name);
  tag.setIntArray("pos", new int[]{x,y,z});
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.name = tag.getString("n");
  this.type = TargetType.values()[tag.getInteger("t")];
  int[] pos = tag.getIntArray("pos");
  this.x = pos[0];
  this.y = pos[1];
  this.z = pos[2];
  }




}
