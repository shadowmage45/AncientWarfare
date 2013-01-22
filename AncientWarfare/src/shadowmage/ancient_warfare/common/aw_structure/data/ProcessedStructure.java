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
import java.util.HashMap;
import java.util.List;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.utils.IDPair;
import shadowmage.ancient_warfare.common.aw_core.utils.IDPairCount;
import shadowmage.ancient_warfare.common.aw_structure.data.rules.BlockRule;

/**
 * fully processed structure, ready to build in-game. 
 * @author Shadowmage
 *
 */
public class ProcessedStructure extends AWStructure
{


private  List<IDPairCount> cachedCounts = null;

public ProcessedStructure()
  {

  }

public BlockRule getRuleAt(int x, int y, int z)
  {
  return this.blockRules.get(this.structure[x][y][z]);
  }

public boolean canGenerateAt(World world, BlockPosition hit, int facing)
  {
  if(!areBlocksValid(getFoundationBlocks(hit, facing), world))
    {
    return false;
    }  
  //TODO validate structure...leveling, clearing, overhang, underground
  return true;
  }

public boolean isValidTargetBlock(int id)
  {
  for(int i = 0; i < this.validTargetBlocks.length; i++)
    {
    if(id==this.validTargetBlocks[i])
      {
      return true;
      }
    }
  return false;
  }

/**
 * returns true if blockIDs==0(handled in overhang and leveling) or is on the validTargetBlocks list
 * @param blocks
 * @param world
 * @return
 */
private boolean areBlocksValid(List<BlockPosition> blocks, World world)
  {
  int id;
  for(BlockPosition pos : blocks)
    {
    id = world.getBlockId(pos.x, pos.y, pos.z);
    if(id==0)
      {
      continue;
      }
    if(!isValidTargetBlock(id))
      {
      return false;
      }
    }
  return true;  
  } 

/**
 * gets world coordinates of blocks for the foundation of this structure at the given orientation and chosen build position 
 * @param hit
 * @param facing
 * @return
 */
private List<BlockPosition> getFoundationBlocks(BlockPosition hit, int facing)
  {
  BlockPosition pos1 = hit.copy();
  pos1.moveForward(facing, this.zOffset);
  pos1.moveLeft(facing, this.xOffset);
  pos1.y += this.verticalOffset - 1;
  
  BlockPosition pos2 = pos1.copy();  
  pos2.moveRight(facing, this.xSize);
  pos2.moveForward(facing, this.zSize);
  return BlockTools.getAllBlockPositionsBetween(pos1, pos2);
  }

/**
 * get world corrdinate BB of this structure if built at hit position and input facing
 * @param hit
 * @param facing
 * @return
 */
public StructureBB getStructureBB(BlockPosition hit, int facing)
  {
  BlockPosition pos1 = hit.copy();
  pos1.moveForward(facing, this.zOffset);
  pos1.moveLeft(facing, this.xOffset);
  pos1.y += this.verticalOffset;
  
  BlockPosition pos2 = pos1.copy();  
  pos2.moveRight(facing, this.xSize);
  pos2.moveForward(facing, this.zSize);
  pos2.y += this.ySize;
  return new StructureBB(pos1, pos2);
  }

/**
 * return a trimmed and tallied list of id/meta pairs necessary to construct this building
 * used for survival direct builder.
 * @return
 */
public List<IDPairCount> getResourceList()
  {
  List<IDPairCount> finalCounts;
  if(cachedCounts!=null)
    {
    return cachedCounts;
    }
  else
    {
    finalCounts = new ArrayList<IDPairCount>();
    }
  
  for(int x = 0; x < this.structure.length; x++)
    {
    for(int y = 0; y < this.structure[x].length; y++)
      {
      for(int z = 0; z < this.structure[x][y].length; z++)
        {
        BlockRule rule = this.getRuleAt(x, y, z);
        BlockData data = rule.blockData[0];
        IDPairCount count = BlockInfo.getInventoryBlock(data.id, data.meta);        
        boolean found = false;
        for(IDPairCount tc : finalCounts)
          {
          if(tc.id==count.id && tc.meta == count.meta)
            {
            tc.count += count.count;
            found = true;
            break;
            }
          }
        if(!found)
          {
          finalCounts.add(count);
          }        
        }
      }
    }  
  this.cachedCounts = finalCounts;;
  return finalCounts;
  }



}
