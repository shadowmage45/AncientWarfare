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
 * similar to logistics pipes
 *    item filter for the location
 *    toggle for command type (stock/withdraw)
 *    toggle for default include or default exclude
 *    toggle for allow partial (ignores stack quantities)(per-slot toggle)
 *      for 'keep stocked' it will pull even singles...
 *      for 'keep emptied' it will pull ALL items, even if less than qty specified
 * 
 * npcs will check their list of routing locations when they have no work
 *    for every keep stocked command
 *      if needs stocked
 *        if a withdraw point has items
 *          add stock command to queue, with pointer to origin, destination,  and items
 *    for every withdraw command
 *      if needs emptied
 *        if a stock point accepts items and has space
 *          add withdraw command to queue, with pointer to destination and items
 *
 * will need a GUI with specialized buttons/slots to accomodate filters and toggles
 *    each button will have an item-filter
 */
boolean withdraw = true;
boolean includeFilter = true;//is filter inclusion or exclusion type?

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
