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
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;


public class PathWorldAccessTest extends PathWorldAccess
{


int LADDER = Block.ladder.blockID;

/**
 * @param world
 */
public PathWorldAccessTest()
  {
  super(null);
  this.world = new WorldTestChunk();
  }


public class WorldTestChunk implements IBlockAccess
{

int[][][] worldChunk = new int [100][10][100];
public WorldTestChunk()
  {
  for(int x = 0; x < 100; x++)
    {
    for(int z = 0; z < 100; z++)
      {
      worldChunk[x][0][z] = 1;
      }
    }  

  int closeWall = 3;
  int farWall = 15;
  for(int x = closeWall; x<=farWall ; x++)
    {
    worldChunk[x][1][closeWall] = 1;
    worldChunk[x][2][closeWall] = 1;
    worldChunk[x][1][farWall] = 1;
    worldChunk[x][2][farWall] = 1;
    }  
    worldChunk[closeWall-1][1][farWall-5] = LADDER;
    worldChunk[closeWall-1][2][farWall-5] = LADDER;
    worldChunk[closeWall+1][1][farWall-5] = LADDER;
    worldChunk[closeWall+1][2][farWall-5] = LADDER;
  for(int z = closeWall; z<=farWall ; z++)
    {
    worldChunk[closeWall][1][z] = 1;
    worldChunk[closeWall][2][z] = 1;
    worldChunk[farWall][1][z] = 1;
    worldChunk[farWall][2][z] = 1;
    }
  worldChunk[farWall-2][1][farWall] = 0;
  worldChunk[farWall-2][2][farWall] = 0;    
  }

@Override
public int getBlockId(int x, int y, int z)
  {
  if(x<= 0 || x>=100 || y<=0 || y>=10 || z<=0 || z>=100)
    {
    return 1;
    }
  return worldChunk[x][y][z];  
  }

@Override
public TileEntity getBlockTileEntity(int var1, int var2, int var3)
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3,
    int var4)
  {
  // TODO Auto-generated method stub
  return 0;
  }

@Override
public float getBrightness(int var1, int var2, int var3, int var4)
  {
  // TODO Auto-generated method stub
  return 0;
  }

@Override
public float getLightBrightness(int var1, int var2, int var3)
  {
  // TODO Auto-generated method stub
  return 0;
  }

@Override
public int getBlockMetadata(int var1, int var2, int var3)
  {
  // TODO Auto-generated method stub
  return 0;
  }

@Override
public Material getBlockMaterial(int var1, int var2, int var3)
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public boolean isBlockOpaqueCube(int var1, int var2, int var3)
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public boolean isBlockNormalCube(int x, int y, int z)
  {
  if(x<= 0 || x>=100 || y<=0 || y>=10 || z<=0 || z>=100)
    {
    return true;
    }
  if(worldChunk[x][y][z]==1)
    {
    return true;
    }
  return false;
  }

@Override
public boolean isAirBlock(int var1, int var2, int var3)
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public BiomeGenBase getBiomeGenForCoords(int var1, int var2)
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public int getHeight()
  {
  // TODO Auto-generated method stub
  return 0;
  }

@Override
public boolean extendedLevelsInChunkCache()
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public boolean doesBlockHaveSolidTopSurface(int var1, int var2, int var3)
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public Vec3Pool getWorldVec3Pool()
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public boolean isBlockProvidingPowerTo(int var1, int var2, int var3, int var4)
  {
  // TODO Auto-generated method stub
  return false;
  }
}
}
