/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
package shadowmage.ancient_structures.common.template.build.validation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBB;
import shadowmage.ancient_structures.common.world_gen.WorldStructureGenerator;

public class StructureValidatorHarbor extends StructureValidator
{

BlockPosition testPosition1 = new BlockPosition();
BlockPosition testPosition2 = new BlockPosition();

Set<String> validTargetBlocks;
Set<String> validTargetBlocksSide;
Set<String> validTargetBlocksRear;
int borderSize;
int maxFill;
int maxLeveling;

public StructureValidatorHarbor()
  {
  super(StructureValidationType.HARBOR);
  validTargetBlocks = new HashSet<String>();
  validTargetBlocksSide = new HashSet<String>();
  validTargetBlocksRear = new HashSet<String>();
  validTargetBlocksRear.add(Block.waterMoving.getUnlocalizedName());  
  validTargetBlocksSide.add(Block.waterMoving.getUnlocalizedName());
  }

@Override
protected void readFromLines(List<String> lines)
  {

  }

@Override
protected void write(BufferedWriter writer) throws IOException
  {

  }

@Override
protected void setDefaultSettings(StructureTemplate template)
  {
  validTargetBlocks.addAll(WorldStructureGenerator.defaultTargetBlocks);
  validTargetBlocksSide.addAll(WorldStructureGenerator.defaultTargetBlocks);
  }

@Override
public boolean shouldIncludeForSelection(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  /**
   * testing that front target position is valid block
   * then test back target position to ensure that it has water at same level
   * or at an acceptable level difference
   */
  Block block = Block.blocksList[world.getBlockId(x, y-1, z)];  
  if(block!=null && validTargetBlocks.contains(block.getUnlocalizedName()))
    {
    testPosition1.reassign(x, y, z);
    testPosition1.moveForward(face, template.zSize-template.zOffset-1);
    for(int by = y-1; by>= by-maxLeveling; by--)
      {
      block = Block.blocksList[world.getBlockId(testPosition1.x, y, testPosition1.z)];
      if(block==Block.waterStill || block==Block.waterMoving)
        {
        return true;
        }
      }
    }
  return false;
  }

@Override
public int getAdjustedSpawnY(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  testPosition1.reassign(x, y, z);
  testPosition1.moveForward(face, template.zSize-template.zOffset-1);
  return WorldStructureGenerator.getTargetY(world, testPosition1.x, testPosition1.z, false)+1;
  }

@Override
public boolean validatePlacement(World world, int x, int y, int z, int face,  StructureTemplate template, StructureBB bb)
  {
  int bx, bz, by;  
  int side;
  int minX, minZ, maxX, maxZ;
  
  int minLevelingY = bb.min.y + template.yOffset;
  int maxLevelingY = minLevelingY + maxLeveling;
  int minFillY = bb.min.y + template.yOffset - maxFill - 1;
  int maxFillY = minFillY + maxFill;
  
  testPosition1 = bb.getFLCorner(face, testPosition1);
  testPosition2 = bb.getFRCorner(face, testPosition2);
  minX = Math.min(testPosition1.x, testPosition2.x);
  maxX = Math.max(testPosition1.x, testPosition2.x);
  minZ = Math.min(testPosition1.z, testPosition2.z);
  maxZ = Math.max(testPosition1.z, testPosition2.z);
  for(bx = minX; bx<=maxX; bx++)
    {
    for(bz = minZ; bz<=maxZ; bz++)
      {
      by = validateBlockHeight(world, bx, bz, minFillY, maxLevelingY, true);
      if(!validateBlockType(world, bx, by, bz, validTargetBlocks, false))
        {
        return false;
        }
      }
    }
  
  testPosition1 = bb.getRLCorner(face, testPosition1);
  testPosition2 = bb.getRRCorner(face, testPosition2);
  minX = Math.min(testPosition1.x, testPosition2.x);
  maxX = Math.max(testPosition1.x, testPosition2.x);
  minZ = Math.min(testPosition1.z, testPosition2.z);
  maxZ = Math.max(testPosition1.z, testPosition2.z);  
  for(bx = minX; bx<=maxX; bx++)
    {
    for(bz = minZ; bz<=maxZ; bz++)
      {
      by = validateBlockHeight(world, bx, bz, minFillY, maxLevelingY, true);
      if(!validateBlockType(world, bx, by, bz, validTargetBlocksRear, false))
        {
        return false;
        }
      }
    }
  
  testPosition1 = bb.getFRCorner(face, testPosition1);
  testPosition2 = bb.getRRCorner(face, testPosition2);
  minX = Math.min(testPosition1.x, testPosition2.x);
  maxX = Math.max(testPosition1.x, testPosition2.x);
  minZ = Math.min(testPosition1.z, testPosition2.z);
  maxZ = Math.max(testPosition1.z, testPosition2.z);  
  for(bx = minX; bx<=maxX; bx++)
    {
    for(bz = minZ; bz<=maxZ; bz++)
      {
      by = validateBlockHeight(world, bx, bz, minFillY, maxLevelingY, true);
      if(!validateBlockType(world, bx, by, bz, validTargetBlocksSide, false))
        {
        return false;
        }
      }
    }
  
  testPosition1 = bb.getFLCorner(face, testPosition1);
  testPosition2 = bb.getRLCorner(face, testPosition2);
  minX = Math.min(testPosition1.x, testPosition2.x);
  maxX = Math.max(testPosition1.x, testPosition2.x);
  minZ = Math.min(testPosition1.z, testPosition2.z);
  maxZ = Math.max(testPosition1.z, testPosition2.z);  
  for(bx = minX; bx<=maxX; bx++)
    {
    for(bz = minZ; bz<=maxZ; bz++)
      {
      by = validateBlockHeight(world, bx, bz, minFillY, maxLevelingY, true);
      if(!validateBlockType(world, bx, by, bz, validTargetBlocksSide, false))
        {
        return false;
        }
      }
    }  
  return true;
  }

@Override
public void preGeneration(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  
  }

@Override
public void handleClearAction(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  
  }

}
