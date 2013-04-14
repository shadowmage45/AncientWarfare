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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PathWorldAccess
{

public boolean canOpenDoors;
public boolean canSwim;
public boolean canDrop;
public boolean canUseLaders;

IBlockAccess world;

int DOOR = Block.doorWood.blockID;

public PathWorldAccess(IBlockAccess world)
  {
  this.world = world;
  }

public int getBlockId(int x, int y, int z)
  {
  return world.getBlockId(x, y, z);
  }

public int getTravelCost(int x, int y, int z)
  {
  int id = world.getBlockId(x, y, z);
  if((id==Block.waterMoving.blockID || id==Block.waterStill.blockID))//can't swim check
    {
    return 30;
    }  
  return 10;
  }

public boolean isWalkable(int x, int y, int z)
  {
  int id = world.getBlockId(x, y, z);
  boolean cube = isCube(x, y, z);
  boolean ladder;  
  if((id==Block.waterMoving.blockID || id==Block.waterStill.blockID)&&!canSwim)//can't swim check
    {
    return false;
    }  
  else if(!canUseLaders && isLadder(id))//ladder use check -- if block is a ladder with air below it
    {
    if(!isCube(x,y-1,z))//air/ladder/non-solid block below
      {
      return false;
      }        
    }
  else if(cube || id==Block.lavaMoving.blockID || id==Block.lavaStill.blockID)//solid unpassable block, or lava
    { 
    return false;
    }
  else if(!isCube(x, y-1, z) && !isLadder(id))//or if air below and not a ladder
    {
    if(!isLadder(world.getBlockId(x, y-1, z)))
      {
      return false;
      }
    }  
  else 
    {    
    id = world.getBlockId(x, y+1, z);
    if(isCube(x, y+1, z) && !isLadder(id)) //id!=0
      {
      return false;
      }
    }
  return true;
  }

protected boolean isCube(int x, int y, int z)
  {
  return world.isBlockNormalCube(x, y, z) || world.getBlockId(x, y, z) == Block.leaves.blockID; 
  }

protected boolean isLadder(int id)
  {
  return id == Block.ladder.blockID || id == Block.vine.blockID;
  }  

protected boolean isLadder(int x, int y, int z)
  {
  return this.isLadder(world.getBlockId(x, y, z));
  }

public boolean isWalkable(int x, int y, int z, Node src)
  {
  return this.isWalkable(x, y, z);
  }

public boolean isRemote()
  {
  return false;
  }


}
