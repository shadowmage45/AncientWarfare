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

boolean openDoors;
boolean canSwim;
boolean canDrop;

IBlockAccess world;

int LADDER = Block.ladder.blockID;
int DOOR = Block.doorWood.blockID;

public PathWorldAccess(IBlockAccess world)
  {
  this.world = world;
  }

public int getBlockId(int x, int y, int z)
  {
  return world.getBlockId(x, y, z);
  }

public boolean isWalkable(int x, int y, int z, Node src)
  {
  int id = world.getBlockId(x, y, z);
  boolean cube = world.isBlockNormalCube(x, y, z);
  if(id==LADDER)
    {
    
    }
  else if(cube || id==Block.waterMoving.blockID || id==Block.waterStill.blockID || id==Block.lavaMoving.blockID || id==Block.lavaStill.blockID)//if solid and not a ladder//id!=0
    {
    return false;
    }    
  else if(!world.isBlockNormalCube(x, y-1, z) && id!=LADDER)//or if air below and not a ladder
    {    //world.getBlockId(x, y-1, z)==0
    return false;
    }  
  else 
    {    
    id = world.getBlockId(x, y+1, z);
    if(world.isBlockNormalCube(x, y+1, z) && id !=LADDER) //id!=0
      {
      return false;
      }
    }
  return true;
  }

public boolean isRemote()
  {
  return false;
  }


}
