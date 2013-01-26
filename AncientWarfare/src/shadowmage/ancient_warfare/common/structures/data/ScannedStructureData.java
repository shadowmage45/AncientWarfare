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
package shadowmage.ancient_warfare.common.structures.data;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class ScannedStructureData
{

public BlockPosition pos1;
public BlockPosition pos2;
public BlockPosition buildKey;
public int originFacing;
public int xSize;
public int ySize;
public int zSize;
public BlockData[][][] allBlocks;
public ArrayList<BlockData> blockIDs = new ArrayList<BlockData>();

/**
 * set by structure scanner GUI prior to export
 *  
 */
public String name = "";
public boolean world;
public boolean survival;
public boolean creative;
public int structureWeight = 1;
public int chunkDistance = 0;
public int chunkAttempts = 1;


/**
 * @param face
 * @param pos1
 * @param pos2
 * @param key an offset vector, relative to frontLeft corner.  Number of blocks to offset the structure left, down, and towards viewer
 */
public ScannedStructureData(int face, BlockPosition pos1, BlockPosition pos2,BlockPosition key)
  {
  this.pos1 = BlockTools.getMin(pos1, pos2);
  this.pos2 = BlockTools.getMax(pos1, pos2);
  this.buildKey = key;
  this.originFacing = face;  
  BlockPosition size = BlockTools.getBoxSize(pos1, pos2);
  this.setSize(originFacing, size.x, size.y, size.z);
  this.setArraySize(originFacing, size.x, size.y, size.z);
  }

public void setSize(int facing, int x, int y, int z)
  {
  this.xSize = x;
  this.ySize = y;
  this.zSize = z;
  }

public void setArraySize(int facing, int x, int y, int z)
  {
  this.allBlocks = new BlockData[x][y][z];
  }

public void scan(World world)
  {  
  int indexX = 0;
  int indexY = 0;
  int indexZ = 0;
  int id;
  int meta;
  for(int x = pos1.x; x <= pos2.x; x++, indexX++, indexY=0)
    {
    for(int y = pos1.y; y <= pos2.y; y++, indexY++, indexZ=0)
      {
      for(int z = pos1.z; z <= pos2.z; z++, indexZ++)
        {       
        id = world.getBlockId(x, y, z);
        meta = world.getBlockMetadata(x, y, z);
        if(id==Block.doorWood.blockID || id==Block.doorSteel.blockID)
          {
          int lowerID = world.getBlockId(x, y-1, z);
          if(lowerID==Block.doorWood.blockID || lowerID==Block.doorSteel.blockID)
            {
            meta = 8;
            }
          }
        allBlocks[indexX][indexY][indexZ] = new BlockData(id, meta);        
        }      
      }    
    }  
  
  this.normalizeForNorthFacing();
  }

protected void addToBlocksList(BlockData data)
  {
  boolean shouldAdd = true;
  for(BlockData block : this.blockIDs)
    {
    if(block.id==data.id && block.meta==data.meta)
      {
      shouldAdd = false;
      }
    }
  if(shouldAdd)
    {
    this.blockIDs.add(data.copy());
    }
  }

public int getRuleForBlock(int id, int meta)
  {
  for(int i = 0; i< this.blockIDs.size(); i++)
    {
    if(this.blockIDs.get(i).id==id && this.blockIDs.get(i).meta==meta)
      {
      return i;
      }
    }
  return 0;
  } 

/*********************************************  NORMALIZATION  ***********************************************/

private void normalizeForNorthFacing()
  {
  int newXSize;// = this.xSize;
  int newYSize = this.ySize;
  int newZSize;// = this.zSize;
  BlockData[][][] newBlocks;
  if(this.originFacing==1 || this.originFacing ==3)
    {
    newXSize = this.zSize;
    newZSize = this.xSize;  
    }
  else
    {
    newXSize = this.xSize;
    newZSize = this.zSize;
    }
    
  newBlocks = new BlockData[newXSize][newYSize][newZSize];
  
  System.out.println("sx: "+newBlocks.length);
  System.out.println("sz: "+newBlocks[0][0].length);
  
  this.blockIDs.clear();
  this.addToBlocksList(new BlockData(0,0));
  int rotationAmount = this.getRotationAmount(originFacing, 2);
  for(int x = 0; x<this.xSize; x++)
    {
    for(int y = 0; y < this.ySize; y++)
      {
      for(int z = 0; z< this.zSize; z++)
        {
        BlockPosition pos = getNorthRotatedPosition(x,y,z, this.originFacing, newXSize, newZSize);
        //DEBUG
        System.out.println("facing: "+originFacing);
        System.out.println("currentSize: "+this.xSize+","+ySize+","+zSize);        
        System.out.println("newSize: "+newXSize+","+newYSize+","+newZSize);
        System.out.println("currentPos: "+x+","+y+","+z+" newPos: "+pos.toString());
        BlockData data = this.allBlocks[x][y][z];
        data.rotateRight(rotationAmount);
        newBlocks[pos.x][pos.y][pos.z] = data;
        if(data.id!=0)
          {
          this.addToBlocksList(newBlocks[pos.x][pos.y][pos.z]);
          }
        }
      }
    }  
  this.xSize = newXSize;
  this.ySize = newYSize;
  this.zSize = newZSize;
  this.allBlocks = newBlocks;
  }


private BlockPosition getNorthRotatedPosition(int x, int y, int z, int rotation, int xSize, int zSize)
  {
  if(rotation==0)//south, invert x,z
    {
    return new BlockPosition(xSize - x - 1 , y, zSize - z - 1 );
    }
  if(rotation==1)//west
    {
    return new BlockPosition(xSize - z - 1, y, x);
    }
  if(rotation==2)//north, no change
    {
    return new BlockPosition(x,y,z);
    }
  if(rotation==3)//east
    {
    return new BlockPosition(z, y, zSize - x - 1);
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

/*********************************************  CONVERSION  ***********************************************/

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
