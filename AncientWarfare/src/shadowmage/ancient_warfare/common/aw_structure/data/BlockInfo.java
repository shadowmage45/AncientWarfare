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

import net.minecraft.block.Block;

/**
 * info wrapper for blocks which may be rotated dynamically
 * @author Shadowmage
 */
public class BlockInfo
{

/**
 * contains data on all _special_ blocks, that need build priority or rotation data
 * e.g. Air: priority 0, to build/clear air blocks first
 * e.g. Stone: priority 1, needs no special treatment
 * e.g. Sand: priority 2, needs to be placed after solid non-moving blocks
 * e.g  Ladder: priority 3, to build after all normal first/second tier blocks are placed
 */
public static BlockInfo[] blockList = new BlockInfo[4096];


public BlockInfo(int id, String name)
  {
  this.blockID = id;
  this.name = name;
  blockList[id]=this;
  }

public BlockInfo(Block block)
  {
  this(block.blockID,block.getBlockName());
  }

public BlockInfo setMeta(int set, int a, int b, int c, int d)
  {
  if(set>=0 && set<4)
    {
    this.metaRotations[set][0]=(byte)a;
    this.metaRotations[set][1]=(byte)b;
    this.metaRotations[set][2]=(byte)c;
    this.metaRotations[set][3]=(byte)d;
    }
  return this;
  }

public BlockInfo setMeta1(int a, int b, int c, int d)
  {
  return setMeta(0, a, b, c, d);  
  }

public BlockInfo setMeta2(int a, int b, int c, int d)
  {
  return setMeta(1, a, b, c, d);
  }

public BlockInfo setMeta3(int a, int b, int c, int d)
  {
  return setMeta(2, a, b, c, d);
  }

public BlockInfo setMeta4(int a, int b, int c, int d)
  {
  return setMeta(3, a, b, c, d);
  }

public BlockInfo setRotatable()
  {
  this.rotatable = true;
  return this;
  }

public boolean isRotatable()
  {
  return this.rotatable;
  }

/**
 * MC blockID for this block
 */
int blockID;

/**
 * name of the block
 */
String name = "";

/**
 * can be rotated through meta-data
 */
boolean rotatable = false;

/**
 * metadata rotation tables, one entry for each possible meta-data, broken into four tables.  Most blocks will only need
 * one or two tables.
 * all rotations will fallback to meta-data 0 if no valid information is found in the table
 */
byte[][] metaRotations = new byte[4][4];

/**
 * return rotated metadata for this block for one turn to the right
 * @param current
 * @return
 */
public int rotateRight(int current)
  {
  if(!this.rotatable)
    {
    return current;
    }
  byte cur = (byte)current;
  for(int i = 0; i <4; i++)
    {
    for(int j = 0; j <4; j++)
      {
      if(metaRotations[i][j]==cur)
        {
        if(j<3)
          {
          return metaRotations[i][j+1];
          }
        else
          {
          return metaRotations[i][0];
          }
        }
      }
    }  
  return 0;
  }

/**
 * return metadata for this block rotated specified number of turns to the right
 * @param current
 * @param turns
 * @return
 */
public int rotateRight(int current, int turns)
  {
  if(!this.rotatable)
    {
    return current;
    }
  for(int i = 0; i < turns; i++)
    {
    current = rotateRight(current);
    }
  return current;
  }

/**
 * create a BlockInfo entry for specified Block, and ensure it is added into
 * the BlockInfo.blockList[]
 * @param block
 * @param priority
 * @return
 */
public static BlockInfo createEntryFor(Block block)
  {
  return createEntryFor(block.blockID, block.getBlockName());
  }

public static BlockInfo createEntryFor(int id, String name)
  {
  BlockInfo info = new BlockInfo(id, name);
  if(blockList[id]==null)
    {
    blockList[id]=info;
    }
  return info;
  }

public static int getRotatedMeta(int id, int meta, int rotationAmt)  
  {
  if(blockList[id]==null)
    {
    return meta;
    }
  return blockList[id].rotateRight(meta, rotationAmt);
  }

}
