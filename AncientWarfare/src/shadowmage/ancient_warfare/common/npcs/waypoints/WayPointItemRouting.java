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
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class WayPointItemRouting extends WayPoint
{

/**
 * needs info on routing stuff...
 *  item filters
 *  routing type 
 */




public WayPointItemRouting(TargetType type)
  {
  super(type);
  }

public WayPointItemRouting(NBTTagCompound tag)
  {
  super(tag);
  }

public WayPointItemRouting(int x, int y, int z, TargetType type)
  {
  super(x, y, z, type);
  }

public WayPointItemRouting(int x, int y, int z, int side, TargetType type)
  {
  super(x, y, z, side, type);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  return super.getNBTTag();
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  }

}
