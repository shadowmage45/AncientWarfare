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
package shadowmage.ancient_warfare.common.npcs.waypoints;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.targeting.TargetPosition;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class WayPoint extends TargetPosition implements INBTTaggable
{

protected String name = "";

/**
 * @param type
 */
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

public WayPoint(int x, int y, int z, TargetType type, String name)
  {
  super(x,y,z,type);
  this.name = name;
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
public int hashCode()
  {
  final int prime = 31;
  int result = 1;
  result = prime * result + ((type == null) ? 0 : type.hashCode());
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
  if (type != other.type)
    return false;
  if (x != other.x)
    return false;
  if (y != other.y)
    return false;
  if (z != other.z)
    return false;
  return true;
  }


}
