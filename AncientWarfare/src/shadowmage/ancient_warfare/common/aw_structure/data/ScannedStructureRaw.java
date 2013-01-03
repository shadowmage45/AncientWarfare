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

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_structure.data.components.ComponentBlocks;

/**
 * scanned structures will be scanned into this dataStruct
 * will have a process() method that will spit out the output source/data config file
 * @author Shadowmage
 *
 */
public class ScannedStructureRaw
{

int facing;
BlockPosition pos1;
BlockPosition pos2;
/**
 * localCoord buildPosition--the position the relative to frontLeft that the player clicks on
 * to initiate construction
 */
BlockPosition buildKey;//stored for convenience only, passed into compressed structure for output/storage

int xSize;
int ySize;
int zSize;
BlockDataScanInfo[][][] allBlocks;

public ScannedStructureRaw(int facing, BlockPosition close, BlockPosition far, BlockPosition buildKey)
  {
  BlockPosition pos1 = BlockTools.getMin(close, far);
  BlockPosition pos2 = BlockTools.getMax(close, far);
  this.facing = facing;
  BlockPosition size = BlockTools.getBoxSize(pos1, pos2);
  this.xSize = size.x;
  this.ySize = size.y;
  this.zSize = size.z;
  allBlocks = new BlockDataScanInfo[xSize][ySize][zSize];
  }

/**
 * populates allBlocks array with info from the parameters passed during construction.
 */
public void scan(World world)
  {
  int indexX = 0;
  int indexY = 0;
  int indexZ = 0;
  for(int x = pos1.x; x < pos2.x; x++, indexX++)
    {    
    for(int y = pos1.y; y < pos2.y; y++, indexY++)
      {
      for(int z = pos1.z; z < pos2.z; z++, indexZ++)
        {
        allBlocks[indexX][indexY][indexZ] = new BlockDataScanInfo(world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z));        
        }      
      }    
    } 
  }

public ScannedStructureCompressed process()
  {
  ScannedStructureCompressed structure = new ScannedStructureCompressed(this.facing, this.buildKey);
  BlockDataScanInfo info;
  ComponentBlocks component;
  for(int y = 0; y < this.xSize ; y++)
    {
    for(int x = 0; x < this.ySize; x++)
      {
      for(int z = 0; z < this.zSize; z++)
        {
        info = allBlocks[x][y][z];
        if(!info.processed)          
          {
          info.processed = true;
          component = new ComponentBlocks(new BlockPosition(x,y,z), new BlockPosition(x,y,z), info.copy());
          expand(component);          
          }
        }
      }
    }  
  return structure;
  }

public StructureComponent expand(ComponentBlocks comp)
  {
  /**
   * used as a continuance flag for expansion
   * will try to expand at least once in each dimension
   */
  boolean canExpand = true;

  /**
   * individual axis flags, to prevent extra canExpand checks if the previous one failed
   */
  boolean canExpandForward = true;
  boolean canExpandRight = true;
  boolean canExpandUpward = true;

  while(canExpand)
    {
    canExpand = false;
    if(canExpandForward && canExpandForward(comp))
      {
      expandForward(comp);
      canExpand = true;
      }
    else
      {
      canExpandForward = false;
      }
    if(canExpandRight && canExpandRight(comp))
      {
      expandRight(comp);
      canExpand = true;
      }
    else
      {
      canExpandRight = false;
      }
    if(canExpandUpward && canExpandUpward(comp))
      {
      expandUpward(comp);
      canExpand = true;
      }
    else
      {
      canExpandUpward = false;
      }
    }  
  return comp;
  }

public void expandForward(StructureComponent comp)
  {
  comp.pos2.z++;
  for(int x = comp.pos1.x; x <= comp.pos2.x; x++)
    {
    for(int y = comp.pos1.y; y <= comp.pos2.y; y++)
      {
      allBlocks[x][y][comp.pos2.z].processed = true;
      }
    }
  }

public boolean canExpandForward(ComponentBlocks comp)
  {
  if(comp.pos2.z+1>=this.zSize)
    {
    return false;
    }
  for(int x = comp.pos1.x; x <= comp.pos2.x; x++)
    {
    for(int y = comp.pos1.y; y <= comp.pos2.y; y++)
      {  
      BlockDataScanInfo info = allBlocks[x][y][comp.pos2.z+1];
      if(info.processed || !info.equals(comp.blockData))
        {
        return false;
        }
      }
    }
  return true;
  }

public void expandRight(ComponentBlocks comp)
  {
  comp.pos2.x++;
  for(int y = comp.pos1.y; y <= comp.pos2.y; y++)
    {
    for(int z = comp.pos1.z; z <= comp.pos2.z; z++)
      {
      allBlocks[comp.pos2.x][y][z].processed = true;
      }
    }
  }

public boolean canExpandRight(ComponentBlocks comp)
  {
  if(comp.pos2.x+1>=this.xSize)
    {
    return false;
    }
  for(int y = comp.pos1.y; y <= comp.pos2.y; y++)
    {
    for(int z = comp.pos1.z; z <= comp.pos2.z; z++)
      { 
      BlockDataScanInfo info = allBlocks[comp.pos2.x+1][y][z];
      if(info.processed || !info.equals(comp.blockData))
        {
        return false;
        }
      }
    }
  return true;
  }

public void expandUpward(ComponentBlocks comp)
  {
  comp.pos2.y++;
  for(int x = comp.pos1.x; x <= comp.pos2.x; x++)
    {
    for(int z = comp.pos1.z; z <= comp.pos2.z; z++)
      {
      allBlocks[x][comp.pos2.y][z].processed = true;
      }
    }
  }

public boolean canExpandUpward(ComponentBlocks comp)
  {
  if(comp.pos2.y+1>=this.ySize)
    {
    return false;
    }
  for(int x = comp.pos1.x; x <= comp.pos2.x; x++)
    {
    for(int z = comp.pos1.z; z <= comp.pos2.z; z++)
      {  
      BlockDataScanInfo info = allBlocks[x][comp.pos2.y+1][z];
      if(info.processed || !info.equals(comp.blockData))
        {
        return false;
        }      
      }
    }
  return true;
  }

}
