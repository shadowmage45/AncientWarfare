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
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockStairs;
import net.minecraft.world.IBlockAccess;

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
  int id2 = world.getBlockId(x, y-1, z);
  int id3 = world.getBlockId(x, y+1, z);
  boolean cube = isSolidBlock(id);
  boolean cube2 = isSolidBlock(id2);
  boolean cube3 = isSolidBlock(id3);
  boolean ladder;
  if(cube || id==Block.lavaMoving.blockID || id==Block.lavaStill.blockID)//solid unpassable block, or lava
    { 
    return false;
    }
  else if(cube3)//no room to move
    {
    return false;
    }
  else if((id==Block.waterMoving.blockID || id==Block.waterStill.blockID) && !canSwim)//can't swim check
    {
    return false;
    }  
  else if(!canUseLaders && isLadder(id) && !cube2)//ladder use check -- if block is a ladder with air below it
    {    
    return false;            
    }  
  else if(!cube2 && !isLadder(id2) && !isLadder(id))//or if air below and not a ladder
    {    
    return false;    
    } 
  else if(id2==Block.fence.blockID || id2 == Block.fenceGate.blockID || id2 == Block.cobblestoneWall.blockID)
    {
    return false;
    }
  return true;
  }

public boolean isDoor(int x, int y, int z)
  {
  return world.getBlockId(x, y, z)==Block.doorWood.blockID;
  }

protected boolean isCube(int x, int y, int z)
  {
  int id = world.getBlockId(x, y, z);
  return isSolidBlock(id); 
  }

protected boolean isSolidBlock(int id)
  {  
  Block block = Block.blocksList[id];
  if(block==null)
    {
    return false;
    }
  if(block.isOpaqueCube() || block.renderAsNormalBlock())
    {
    return true;
    }
  else if(id == Block.fence.blockID || id == Block.fenceIron.blockID || id == Block.cobblestoneWall.blockID || id == Block.tilledField.blockID || id == Block.glass.blockID || id== Block.thinGlass.blockID)
    {
    return true;
    }  
  else if(id== Block.woodSingleSlab.blockID || id == Block.stoneSingleSlab.blockID || id == Block.chest.blockID)
    {
    return true;
    }
  else if(block instanceof BlockStairs || block instanceof BlockLeaves)
    {
    return true;
    }
  return false;
  }

protected boolean isLadder(int id)
  {
  return id == Block.ladder.blockID || id == Block.vine.blockID;
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
