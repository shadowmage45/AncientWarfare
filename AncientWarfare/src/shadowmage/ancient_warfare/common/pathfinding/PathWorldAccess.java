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
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.gates.TEGateProxy;

public class PathWorldAccess
{

public boolean canOpenDoors;
public boolean canSwim;
public boolean canDrop;
public boolean canUseLaders;

IBlockAccess world;

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

/**
 * checks the collision bounds of the block at x,y,z to make sure it is <= 0.5 tall (pathable)
 * @param x
 * @param y
 * @param z
 * @return true if it is a pathable block, false if it fails bounds checks
 */
protected boolean checkBlockBounds(int x, int y, int z)  
  {
  Block block;
  int id = world.getBlockId(x, y, z);
  if(!isPathable(id) || id==Block.waterMoving.blockID || id==Block.waterStill.blockID)
    {
    return false;
    }
  block = Block.blocksList[id];
  if(block!=null)
    {
    block.setBlockBoundsBasedOnState(world, x, y, z);
    if(block.getBlockBoundsMaxY() > 0.5d)
      {
      return false;
      }
    }
  return true;
  }

public boolean isWalkable(int x, int y, int z)
  {  
  int id = world.getBlockId(x, y, z);
  int id2 = world.getBlockId(x, y-1, z);
  int id3 = world.getBlockId(x, y+1, z);
  boolean cube = !checkBlockBounds(x, y, z);//isSolidBlock(id);
  boolean cube2 = !checkBlockBounds(x, y-1, z);//isSolidBlock(id2);
  boolean cube3 = !checkBlockBounds(x, y+1, z);//isSolidBlock(id3);
  boolean ladder;
  /**
   * check basic early out
   * check for doors
   * check for gates
   * check for ladders
   * check for water
   * check block bounds
   */
  if(cube)
    {
    return false;
    }
  if(!isPathable(id))//solid unpassable block, or lava
    { 
    return false;
    }
  if(id==0 && cube2 && id3==0)//early out check for the most basic of pathable areas
    {
    return true;
    }
  if(id==BlockLoader.gateProxy.blockID)
    {
    if(!canOpenDoors)//if can't open doors, auto fail
      {
      return false;
      }    
    else if(!cube2 || (cube3 && id3!=BlockLoader.gateProxy.blockID) || id2==BlockLoader.gateProxy.blockID || id3==0)
      {
      /**
       * else fail out if block below is not solid, or block above IS solid but not a gate block
       * or block below is a gate block (dont' walk in a gate block ON a gate block)
       * (allow gate blocks because they are generally tall...)
       */
      return false;
      }
    else
      {
      TEGateProxy proxy = (TEGateProxy)world.getBlockTileEntity(x, y, z);
      if(proxy.owner==null || !proxy.owner.getGateType().canSoldierActivate() || proxy.owner.wasPowered)
        {
        return false;
        }
      }
    }
  else if(!canOpenDoors && isDoor(x, y, z))
    {
    return false;
    }  
  else if(isWater(id))//can't swim check
    {
    if(!canSwim)
      {
      return false;
      }
    else if(id3!=0)
      {
      return false;
      }    
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
  else if(cube3)//no room to move
    {
    return false;
    }
  return true;
  }

public boolean isWater(int id)
  {
  return id==Block.waterMoving.blockID || id==Block.waterStill.blockID;
  }

public boolean isDoor(int x, int y, int z)
  {
  int id = world.getBlockId(x, y, z);
  return id==Block.doorWood.blockID || id==Block.fenceGate.blockID;
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
  else if(id== Block.woodSingleSlab.blockID || id == Block.stoneSingleSlab.blockID || id == Block.chest.blockID || id== Block.chestTrapped.blockID || id == Block.slowSand.blockID)
    {
    return true;
    }
  else if(id== BlockLoader.gateProxy.blockID)
    {
    return true;
    }
  else if(block instanceof BlockStairs || block instanceof BlockLeaves)
    {
    return true;
    }
  return false;
  }

protected boolean isPathable(int id)
  {
  if(id==Block.lavaMoving.blockID || id==Block.lavaStill.blockID || id==Block.fence.blockID || id==Block.fenceIron.blockID|| id == Block.cobblestoneWall.blockID || id==Block.thinGlass.blockID)
    {
    return false;
    }
  return true;
  }

protected boolean checkColidingEntities(int x, int y, int z)
  {
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
