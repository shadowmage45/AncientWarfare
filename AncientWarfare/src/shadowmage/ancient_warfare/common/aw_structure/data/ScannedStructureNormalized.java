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
import shadowmage.ancient_warfare.common.aw_structure.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.aw_structure.export.StructureExporter;

/**
 * a north-normalized structure, ready for output to file or direct processing and building in-game
 * holds only basic template values (block IDs/metas/positions, build offset)
 * @author Shadowmage
 *
 */
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
  this.addToBlocksList(new BlockData(0,0));
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
        if(this.allBlocks[pos.x][pos.y][pos.z].id!=0)
          {
          this.addToBlocksList(this.allBlocks[pos.x][pos.y][pos.z]);
          }
        }
      }
    }
  }

private BlockPosition getNorthRotatedPosition(int x, int y, int z, int rotation)
  {
  if(rotation==0)//south, invert x,z
    {
    return new BlockPosition(this.xSize-x-1,y,this.zSize-z-1);
    }
  if(rotation==1)//west, swap +x>+z,+z>-x
    {
    return new BlockPosition(this.xSize-z-1,y,x);
    }
  if(rotation==2)//north, no change
    {
    return new BlockPosition(x,y,z);
    }
  if(rotation==3)//east, swap +x>-z, +z>+x
    {
    return new BlockPosition(z,y,this.zSize-x-1);     
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

/**
 * returns a processed structure populated with enough data to _build_ this structure,
 * though it is not as configured as if done through a proper template
 * @param name
 * @return
 */
public ProcessedStructure convertToProcessedStructure()
  {
  ProcessedStructure struct = new ProcessedStructure();
  struct.name = String.valueOf(this.name);
  struct.xSize = this.xSize;
  struct.ySize = this.ySize;
  struct.zSize = this.zSize;
  
  struct.xOffset = this.buildKey.x;
  struct.verticalOffset = this.buildKey.y;
  struct.zOffset = this.buildKey.z;
  
  BlockData[] blocks = this.getAllBlockTypes();
  for(int i = 0; i < blocks.length; i++)
    {
    BlockData data = blocks[i];
    BlockRule rule = new BlockRule(i, data.id, data.meta);
    struct.blockRules.add(rule);
    }
  struct.structure = new short[xSize][ySize][zSize];
  for(int x = 0; x <struct.structure.length; x++)
    {
    for(int y = 0; y <struct.structure[x].length; y++)
      {
      for(int z = 0; z < struct.structure[x][y].length; z++)
        {
        BlockData data = this.allBlocks[x][y][z];
        struct.structure[x][y][z]=(short) this.getRuleForBlock(data.id, data.meta);
        }
      }
    }
  return struct;
  }

public BlockData[] getAllBlockTypes()
  {  
  BlockData[] datas = new BlockData[this.blockIDs.size()];
  for(int i = 0; i <this.blockIDs.size(); i++)
    {
    datas[i]=this.blockIDs.get(i);
    }
  return datas;
  }

}
