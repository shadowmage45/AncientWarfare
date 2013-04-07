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
package shadowmage.ancient_warfare.common.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * a wrapper for a world-obj that will do additional validation of nodes to see if the entity can walk
 * on the node, and fit in the area (used more for vehicles than soldiers)
 * @author Shadowmage
 *
 */
public class PathWorldAccessEntity extends PathWorldAccess
{


World worldObj;//so there is no casting necessary to access world functions (getcolliding bb/etc)
Entity entity;

/**
 * @param world
 */
public PathWorldAccessEntity(World world, Entity entity)
  {
  super(world);
  worldObj = world;
  this.entity = entity;
  }

@Override
public boolean isWalkable(int x, int y, int z, Node src)
  { 
  if(super.isWalkable(x, y, z, src))
    {
    //check to see that entity can fit completely on the block provided.  Should also check to make sure that
    //entity can path from src to this block (diagonals not obstructed)
    return true;
    }
  return false;  
  }

@Override
public boolean isRemote()
  {
  return this.worldObj.isRemote;
  }
}
