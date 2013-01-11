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
package shadowmage.ancient_warfare.common.aw_structure.data;

import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;

public class ScannedStructureNormalized extends ScannedStructureRaw
{

/**
   * @param face
   * @param pos1
   * @param pos2
   * @param key
   */
public ScannedStructureNormalized(int face, BlockPosition pos1, BlockPosition pos2, BlockPosition key)
  {
  super(face, pos1, pos2, key);
  }

@Override
public void setSize(int facing, int x, int y, int z)
  {
  if(facing==0 || facing==2)
    {
    this.xSize = x;
    this.ySize = y;
    this.zSize = z;
    }
  else
    {
    this.xSize = z;
    this.ySize = y;
    this.zSize = x;
    }  
  }

@Override
public void setArraySize(int facing, int x, int y, int z)
  {
  if(facing==0 || facing==2)
    {
    this.allBlocks = new BlockData[x][y][z];
    }
  else
    {
    this.allBlocks = new BlockData[z][y][x];
    }  
  }

/**
 * converts a raw structure into a properly oriented structure ready to save/export
 * @param raw
 */
public void processRawStructure(ScannedStructureRaw raw)
  {
  for(int x = 0; x<raw.xSize; x++)
    {
    for(int y = 0; y < raw.ySize; y++)
      {
      for(int z = 0; z< raw.zSize; z++)
        {
        BlockPosition pos = getNorthRotatedPosition(x,y,z, this.originFacing);
        this.allBlocks[pos.x][pos.y][pos.z]= raw.allBlocks[x][y][z];
        int rotationAmount = this.getRotationAmount(originFacing, 2);
        this.allBlocks[pos.x][pos.y][pos.z].rotateRight(rotationAmount);        
        }
      }
    }
  }

private BlockPosition getNorthRotatedPosition(int x, int y, int z, int rotation)
  {
  if(rotation==0)//south, invert x,z
    {
    return new BlockPosition(this.xSize-x,y,this.zSize-z);
    }
  if(rotation==1)//east, swap +x>+z,+z>-x
    {
    return new BlockPosition(this.xSize-z,y,x);
    }
  if(rotation==2)//north, no change
    {
    return new BlockPosition(x,y,z);
    }
  if(rotation==3)//west, swap +x>-z, +z>+x
    {
    return new BlockPosition(z,y,this.zSize-x);     
    }
  return null;
  }

private int getRotationAmount(int start, int destination)
  {
  if(start==destination)
    {
    return 0;
    }
  int turn = destination-start;
  if(turn<0)
    {
    turn += 4;
    }  
  return turn;
  }

}
