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
package shadowmage.ancient_warfare.common.aw_core.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockTools
{

public static BlockPosition offsetForSide(BlockPosition pos, int sideHit)
  {
  int x = pos.x;
  int y = pos.y;
  int z = pos.z;
  /**
   * if should offset for side hit (block clicked IN)
   */
  
  switch (sideHit)
    {
    case 0:
    pos.y--;
    break;
    case 1:
    pos.y++;
    break;
    case 2:
    pos.z--;
    break;
    case 3:
    pos.z++;
    break;
    case 4:
    pos.x--;
    break;
    case 5:
    pos.x++;
    }  
  return pos;    
  }

/**
 * will return null if nothing is in range
 * @param player
 * @param world
 * @param offset
 * @return
 */
public static BlockPosition getBlockClickedOn(EntityPlayer player, World world, boolean offset)
  {
  float scaleFactor = 1.0F;
  float rotPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * scaleFactor;
  float rotYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * scaleFactor;
  double testX = player.prevPosX + (player.posX - player.prevPosX) * scaleFactor;
  double testY = player.prevPosY + (player.posY - player.prevPosY) * scaleFactor + 1.62D - player.yOffset;
  double testZ = player.prevPosZ + (player.posZ - player.prevPosZ) * scaleFactor;
  Vec3 testVector = Vec3.createVectorHelper(testX, testY, testZ);
  float var14 = MathHelper.cos(-rotYaw * 0.017453292F - (float)Math.PI);
  float var15 = MathHelper.sin(-rotYaw * 0.017453292F - (float)Math.PI);
  float var16 = -MathHelper.cos(-rotPitch * 0.017453292F);
  float vectorY = MathHelper.sin(-rotPitch * 0.017453292F);
  float vectorX = var15 * var16;
  float vectorZ = var14 * var16;
  double reachLength = 5.0D;
  Vec3 testVectorFar = testVector.addVector(vectorX * reachLength, vectorY * reachLength, vectorZ * reachLength);
  MovingObjectPosition testHitPosition = world.rayTraceBlocks_do(testVector, testVectorFar, true);

  /**
   * if nothing was hit, return null
   */
  if (testHitPosition == null)
    {
    return null;
    }

  Vec3 var25 = player.getLook(scaleFactor);
  boolean var26 = false;
  float var27 = 1.0F;
  List entitiesPossiblyHitByVector = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(var25.xCoord * reachLength, var25.yCoord * reachLength, var25.zCoord * reachLength).expand(var27, var27, var27));
  Iterator entityIterator = entitiesPossiblyHitByVector.iterator();
  while (entityIterator.hasNext())
    {
    Entity testEntity = (Entity)entityIterator.next();
    if (testEntity.canBeCollidedWith())
      {
      float bbExpansionSize = testEntity.getCollisionBorderSize();
      AxisAlignedBB entityBB = testEntity.boundingBox.expand(bbExpansionSize, bbExpansionSize, bbExpansionSize);
      /**
       * if an entity is hit, return its position
       */
      if (entityBB.isVecInside(testVector))
        {
        return new BlockPosition(testEntity.posX, testEntity.posY, testEntity.posZ);         
        }
      }
    }
  /**
   * if no entity was hit, return the position impacted.
   */
  int var42 = testHitPosition.blockX;
  int var43 = testHitPosition.blockY;
  int var44 = testHitPosition.blockZ;
  /**
   * if should offset for side hit (block clicked IN)
   */
  if(offset)
    {
    switch (testHitPosition.sideHit)
      {
      case 0:
      --var43;
      break;
      case 1:
      ++var43;
      break;
      case 2:
      --var44;
      break;
      case 3:
      ++var44;
      break;
      case 4:
      --var42;
      break;
      case 5:
      ++var42;
      }
    }      
  return new BlockPosition(var42, var43, var44); 
  }

/**
 * checks to see if TEST lies somewhere in the cube bounded by pos1 and pos2
 * @param test
 * @param pos1
 * @param pos2
 * @return true if it does
 */
public static boolean isPositionWithinBounds(BlockPosition test, BlockPosition pos1, BlockPosition pos2)
  {
  int minX;
  int maxX;
  int minY;
  int maxY;
  int minZ;
  int maxZ;
  if(pos1.x < pos2.x)
    {
    minX = pos1.x;
    maxX = pos2.x;
    }
  else
    {
    minX = pos2.x;
    maxX = pos1.x;
    }
  if(pos1.y < pos2.y)
    {
    minY = pos1.y;
    maxY = pos2.y;
    }
  else
    {
    minY = pos2.y;
    maxY = pos1.y;
    }
  if(pos1.z < pos2.z)
    {
    minZ = pos1.z;
    maxZ = pos2.z;
    }
  else
    {
    minZ = pos2.z;
    maxZ = pos1.z;
    }
  BlockPosition min = new BlockPosition(minX, minY, minZ);
  BlockPosition max = new BlockPosition(maxX, maxY, maxZ);

  if(test.x >= min.x && test.x <=max.x)
    {
    if(test.y >= min.y && test.y <=max.y)
      {
      if(test.z >= min.z && test.z <=max.z)
        {
        return true;
        }
      }
    }
  return false;
  }

/**
 * returns an array of positions of every block between the pair of coordinates passed
 * returns NULL if any axis spans more than 20 blocks 
 * @param pos1
 * @param pos2
 * @return
 */
public static ArrayList<BlockPosition> getAllBlockPositionsBetween(BlockPosition pos1, BlockPosition pos2)
  {  
  int minX;
  int maxX;
  int minY;
  int maxY;
  int minZ;
  int maxZ;
  if(pos1.x < pos2.x)
    {
    minX = pos1.x;
    maxX = pos2.x;
    }
  else
    {
    minX = pos2.x;
    maxX = pos1.x;
    }
  if(pos1.y < pos2.y)
    {
    minY = pos1.y;
    maxY = pos2.y;
    }
  else
    {
    minY = pos2.y;
    maxY = pos1.y;
    }
  if(pos1.z < pos2.z)
    {
    minZ = pos1.z;
    maxZ = pos2.z;
    }
  else
    {
    minZ = pos2.z;
    maxZ = pos1.z;
    }
  BlockPosition min = new BlockPosition(minX, minY, minZ);
  BlockPosition max = new BlockPosition(maxX, maxY, maxZ);
  BlockPosition diff = max.getOffsetFrom(min);
  if(diff.x >20 || diff.y > 20 || diff.z > 20)//if there are ALOT of blocks in the cube
    {
    return null;
    }
  ArrayList<BlockPosition> blocks = new ArrayList<BlockPosition>();
  blocks.clear();
  int cX;//
  int cY;
  int cZ;
  for(cX = min.x ; cX <= max.x; cX++)
    {
    for(cY = min.y ; cY <= max.y; cY++)
      {
      for(cZ = min.z ; cZ <= max.z; cZ++)
        {
        blocks.add(new BlockPosition(cX, cY, cZ));
        }
      }    
    }  
  return blocks;
  }

/**
 * checks to see if the pair share at least one axis
 * and are not identical
 * @param pos1
 * @param pos2
 * @return true if pair make a valid planar coordinate pair
 */
public static boolean arePositionsValidPair(BlockPosition pos1, BlockPosition pos2)
  {  
  byte validCount = 0;
  if(pos1.x == pos2.x)
    {
    validCount++;    
    }
  if(pos1.y == pos2.y)
    {
    validCount++;
    }
  if(pos1.z == pos2.z)
    {
    validCount++;
    }
  if(validCount >0 && validCount < 3)
    {
    return true;
    }
  return false;
  }

/**
 * checks to see if the pair share exactly one axis on the horizontal plane
 * and are not identical
 * @param pos1
 * @param pos2
 * @return true if pair make a valid planar coordinate pair
 */
public static boolean arePositionsValidHorizontalPair(BlockPosition pos1, BlockPosition pos2)
  {  
  byte validCount = 0;
  if(pos1.x == pos2.x)
    {
    validCount++;    
    }
  if(pos1.z == pos2.z)
    {
    validCount++;
    }
  if(validCount == 1)
    {
    return true;
    }
  return false;
  }

/**
 * returns the absolute size of the box bounded by pos1 and pos2, as a vector (start at 0,0,0, return value is the final x,y,z size)
 * @param pos1
 * @param pos2
 * @return
 */
public static BlockPosition getBoxSize(BlockPosition pos1, BlockPosition pos2)
  {  
  int x = getDifference(pos1.x, pos2.x);
  int y = getDifference(pos1.y, pos2.y);
  int z = getDifference(pos1.z, pos2.z);
  return new BlockPosition(x+1,y+1,z+1);
  }

public static int getDifference(int a, int b)
  {
  return a< b? b-a : a-b;
  }

public static int getMin(int a, int b)
  {
  return a < b ? a : b;
  }

public static int getMax(int a, int b)
  {
  return a < b ? b : a;
  }

public static BlockPosition getMin(BlockPosition pos1, BlockPosition pos2)
  {
  BlockPosition pos = new BlockPosition(getMin(pos1.x, pos2.x), getMin(pos1.y, pos2.y), getMin(pos1.z, pos2.z));
  return pos;
  }

public static BlockPosition getMax(BlockPosition pos1, BlockPosition pos2)
  {
  BlockPosition pos = new BlockPosition(getMax(pos1.x, pos2.x), getMax(pos1.y, pos2.y), getMax(pos1.z, pos2.z));
  return pos;
  }


public static int getFacingFromSide(int side)
  {
  return 0;
  }

/**
 * returns a facing direction essentially opposite from the players current facing direction
 * so that the block will face towards the player on spawn
 * @param player
 * @return
 */
public static int getBlockFacingMetaFromPlayerYaw(float rotation)
  {  
  //north = 180, south = 0
  //east = 270
  double yaw = (double)rotation;
  while(yaw < 0.d)
    {
    yaw+=360.d;
    }
  while(yaw >=360.d)
    {
    yaw-=360.d;
    }
  double adjYaw = yaw +45;
  adjYaw *=4;//multiply by four
  adjYaw /= 360.d;
  int facing = (MathHelper.floor_double(adjYaw)) % 4;//round down, mod 4 for a 0-3 range
  
  if(facing==0)//correct
    {
    return 2;
    }
  if(facing==1)//correct
    {
    return 5;
    }
  if(facing==2)//correct
    {
    return 3;
    }
  if(facing==3)
    {
    return 4;//correct
    }
  
//  this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
//  this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
//  this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
//  this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
//  this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
//  this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));    
  
  return facing;
  }

public static int getPlayerFacingFromYaw(float rotation)
  {
  double yaw = (double)rotation;
  while(yaw < 0.d)
    {
    yaw+=360.d;
    }
  while(yaw >=360.d)
    {
    yaw-=360.d;
    }
  double adjYaw = yaw +45;
  adjYaw *=4;//multiply by four
  adjYaw /= 360.d;
  int facing = (MathHelper.floor_double(adjYaw)) % 4;//round down, mod 4 for a 0-3 range
  return facing;
  }

public static int getPlayerFacingFromMeta(int meta)
  {
  if(meta==2)
    {
    return 0;
    }
  if(meta==5){return 1;}
  if(meta==3){return 2;}
  if(meta==4){return 3;}
  return 0;
  }

public static int getBlockFacingFromMeta(int meta)
  {
  return (getPlayerFacingFromMeta(meta)+2) %4;
  }

}
