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
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.structures.data.rules.SwapRule;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.IDPairCount;

/**
 * fully processed structure, ready to build in-game. 
 * @author Shadowmage
 *
 */
public class ProcessedStructure extends AWStructure
{

private MemoryTemplate template = new MemoryTemplate();

public ProcessedStructure()
  {

  }

public void setTemplateLines(List<String> lines)
  {
  template.setLines(lines);
  }

public MemoryTemplate getTemplate()
  {
  return template;
  }

public BlockRule getRuleAt(int x, int y, int z)
  {
  if(this.blockRules==null)
    {
    return null;
    }
  return this.blockRules.get(Integer.valueOf(this.structure[x][y][z]));
  }

public static boolean canGenerateAtSurface(World world, BlockPosition hit, int facing, ProcessedStructure struct, int maxOverhang, int maxLeveling, int levelingBuffer, int maxVerticalClear, int clearingBuffer)
  {  
  int missingBlocks = 0;
  boolean canGen = true;  
  StructureBB bb = getBoundingBox(hit, facing, struct.xOffset, struct.ySize, struct.zOffset, struct.xSize, struct.ySize, struct.zSize);
  if(maxLeveling==0)//should level the site, or check for overhang?
    {
    BlockPosition front = bb.pos1.copy();
    front.y --;
    BlockPosition back = bb.pos2.copy();
    back.y = front.y;
    List<BlockPosition> foundationBlocks = BlockTools.getAllBlockPositionsBetween(front, back);
    for(BlockPosition pos : foundationBlocks)
      {      
      if(!isValidTargetBlock(world.getBlockId(pos.x, pos.y, pos.z), struct.validTargetBlocks));
        {
        missingBlocks++;
        }
      if(missingBlocks>maxOverhang)
        {
        Config.logDebug("Rejected due to overhang");
        return false;
        }    
      }
    }
  else
    {
    BlockPosition min = BlockTools.getMin(bb.pos1, bb.pos2);
    min.y += struct.verticalOffset;
    BlockPosition max = BlockTools.getMax(bb.pos1, bb.pos2);
    if(!isValidLevelingTarget(world, min, max, struct.validTargetBlocks, levelingBuffer))
      {
      Config.logDebug("rejected for improper leveling");
      return false;
      }   
    }
  
  if(maxVerticalClear >= struct.ySize -struct.verticalOffset)//the whole thing will be cleared
    {
    Config.logDebug("skipping clearing check");
    return true;
    }
    
  BlockPosition clearTest = bb.pos1.copy();
  BlockPosition clearTest2 = bb.pos2.copy();
  
  clearTest.y+=struct.verticalOffset+ maxVerticalClear - 1;
  List<BlockPosition> nonClearedBlocks = BlockTools.getAllBlockPositionsBetween(clearTest, clearTest2);
  
  if(clearTest.y==clearTest2.y && maxVerticalClear>0)
    {
    Config.logDebug("second skip clearance check");
    }
  
  for(BlockPosition pos : nonClearedBlocks)
    {
    if(world.getBlockId(pos.x, pos.y, pos.z)!=0)
      {
      Config.logDebug("rejected due to clearance");
      return false;
      }
    }
    
  return true;
  }

public boolean canGenerateAtSubSurface(World world, BlockPosition hit, int facing, ProcessedStructure struct)
  {
  /**
   * what to check for underground validation?   * 
   * if !partial underground 
   *   check perimiter for max allowed air?
   */
  int missingBlocks = 0;
  boolean canGen = true;  
  StructureBB bb = getStructureBB(hit, facing);
  if(maxLeveling==0)//should level the site, or check for overhang?
    {
    BlockPosition front = bb.pos1.copy();
    front.y --;
    BlockPosition back = bb.pos2.copy();
    back.y = front.y;
    List<BlockPosition> foundationBlocks = BlockTools.getAllBlockPositionsBetween(front, back);
    for(BlockPosition pos : foundationBlocks)
      {      
      if(!isValidTargetBlock(world.getBlockId(pos.x, pos.y, pos.z), struct.validTargetBlocks))
        {
        missingBlocks++;
        }
      if(missingBlocks>this.maxOverhang)
        {
        Config.logDebug("Rejected due to overhang");
        return false;
        }    
      }
    }
  else
    {
    BlockPosition min = BlockTools.getMin(bb.pos1, bb.pos2);
    min.y += verticalOffset;
    BlockPosition max = BlockTools.getMax(bb.pos1, bb.pos2);
    if(!isValidLevelingTarget(world, min, max, struct.validTargetBlocks, struct.levelingBuffer))
      {
      Config.logDebug("rejected for improper leveling");
      return false;
      }   
    }
  return true;
  }

public static boolean isValidLevelingTarget(World world, BlockPosition min, BlockPosition max, int[] validTargetBlocks, int levelingBuffer)
  {
  //TODO check beneath as well as just beside?
  min.x-= 1 + levelingBuffer;
  min.z-= 1 + levelingBuffer;
  max.x+= 1 + levelingBuffer;
  max.z+= 1 + levelingBuffer;
  
  for(int x = min.x; x <= max.x; x++)
    {
    int id = world.getBlockId(x, min.y, min.z);
    if(!isValidTargetBlock(id, validTargetBlocks) || id==0)
      {
      return false;
      }
    id = world.getBlockId(x, min.y, max.z);
    if(!isValidTargetBlock(id, validTargetBlocks) || id==0)
      {
      return false;
      }
    }
  for(int z = min.z; z <= max.z; z++)
    {
    int id = world.getBlockId(min.x, min.y, z);
    if(!isValidTargetBlock(id, validTargetBlocks) || id==0)
      {
      return false;
      }
    id = world.getBlockId(max.x, min.y, z);
    if(!isValidTargetBlock(id, validTargetBlocks) || id==0)
      {
      return false;
      }
    }
  return true;
  }

public static boolean isValidTargetBlock(int id, int[] validTargetBlocks)
  {
  if(validTargetBlocks==null)
    {
    return true;
    }
  for(int i = 0; i < validTargetBlocks.length; i++)
    {
    if(id==validTargetBlocks[i])
      {
      return true;
      }
    }
  return false;
  }

public BlockData getSwappedData(int swapGroup, String biomeName, BlockData sourceID)
  {  
  if(this.swapRules.containsKey(swapGroup))
    {
    return this.swapRules.get(swapGroup).getSwappedData(biomeName, sourceID);
    }
  return sourceID;
  }

/**
 * returns true if blockIDs==0(handled in overhang and leveling) or is on the validTargetBlocks list
 * @param blocks
 * @param world
 * @return
 */
private static boolean areBlocksValid(List<BlockPosition> blocks, World world, int[] validTargetBlocks)
  {
  int id;
  for(BlockPosition pos : blocks)
    {
    id = world.getBlockId(pos.x, pos.y, pos.z);
    if(id==0)
      {
      continue;
      }
    if(!isValidTargetBlock(id, validTargetBlocks))
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
  hit = getOffsetHitPosition(hit, facing);
  BlockPosition max = hit.copy();
  max.moveForward(facing, zSize);
  max.moveRight(facing, xSize);
  return BlockTools.getAllBlockPositionsBetween(hit, max);
  }

/**
 * get world coordinate BB of this structure if built at hit position and input facing
 * @param hit
 * @param facing
 * @return
 */
public StructureBB getStructureBB(BlockPosition hit, int facing)
  {
  return getBoundingBox(hit, facing, xOffset, verticalOffset, zOffset, xSize, ySize, zSize);
  }

public StructureBB getLevelingBB(BlockPosition hit, int facing)
  {
  return getLevelingBoundingBox(hit, facing, xOffset, verticalOffset, zOffset, xSize, ySize, zSize, maxLeveling, levelingBuffer);}

public StructureBB getClearingBB(BlockPosition hit, int facing)
  { 
  return getClearingBoundinBox(hit, facing, xOffset, verticalOffset, zOffset, xSize, ySize, zSize, maxVerticalClear, clearingBuffer);
  }

/**
 * returns a facing normalized frontleft corner position for this building (no Y adjustment)
 * used by boundingboxes, alters the passed in position
 * @param hit
 * @param facing
 */
public BlockPosition getOffsetHitPosition(BlockPosition hit, int facing)
  {
  BlockPosition test = hit;
  test.moveLeft(facing, this.xOffset);
  test.moveForward(facing, -this.zOffset);  
  return test;
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
        if(rule.blockData==null)
          {
          //TODO...fix handling for no blocks but existing vehicle/npc/gate types
          continue;
          }
        BlockData data = rule.blockData[0];
        
        if(data.id==0)
          {
          continue;
          }
        
        //TODO HACK...
        if(isDoorTop(data.id,data.meta) || isBedTop(data.id, data.meta)) 
          {
          continue;
          }
                
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

private boolean isDoorTop(int id, int meta)
  {  
  return meta==8 && (id == Block.doorWood.blockID || id == Block.doorSteel.blockID );
  }

private boolean isBedTop(int id, int meta)
  {
  return meta >=8 && id == Block.bed.blockID;
  }

public boolean isValidBiome(String biomeName)
  {
  if(this.biomesNotIn == null && this.biomesOnlyIn == null)
    {
    return true;
    }
  if(this.biomesNotIn!=null)
    {
    for(String bio : this.biomesNotIn)
      {
      if(bio.equals(biomeName))
        {
        return false;
        }
      }
    }
  if(this.biomesOnlyIn!=null)
    {
    for(String bio : this.biomesOnlyIn)
      {
      if(bio.equals(biomeName))
        {
        return true;
        }
      }
    return false;
    }  
  return true;
  }

}
