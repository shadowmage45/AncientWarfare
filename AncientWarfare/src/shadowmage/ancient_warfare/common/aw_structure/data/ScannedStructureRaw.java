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

import java.util.ArrayList;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;

/**
 * a scan of a structure, oriented however it was in the world...will be rotated during processing
 *  /conversion
 * @author Shadowmage
 *
 */
public class ScannedStructureRaw
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
 * @param face
 * @param pos1
 * @param pos2
 * @param key an offset vector, relative to frontLeft corner.  Number of blocks to offset the structure left, down, and towards viewer
 */
public ScannedStructureRaw(int face, BlockPosition pos1, BlockPosition pos2,BlockPosition key)
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
  for(int x = pos1.x; x <= pos2.x; x++, indexX++)
    { 
    indexY = 0;
    for(int y = pos1.y; y <= pos2.y; y++, indexY++)
      {
      indexZ = 0;
      for(int z = pos1.z; z <= pos2.z; z++, indexZ++)
        {       
        System.out.println("scanning block:"+indexX+","+indexY+","+indexZ);
        allBlocks[indexX][indexY][indexZ] = new BlockData(world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z));        
        }      
      }    
    }    
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

public ScannedStructureNormalized process()
  {
  ScannedStructureNormalized struct = new ScannedStructureNormalized(originFacing, pos1, pos2, buildKey);
  struct.process();
  return struct;
  }


}
