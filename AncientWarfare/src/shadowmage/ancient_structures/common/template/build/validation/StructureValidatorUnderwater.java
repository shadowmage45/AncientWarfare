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
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBB;
import shadowmage.ancient_structures.common.world_gen.WorldStructureGenerator;

public class StructureValidatorUnderwater extends StructureValidator
{

int minWaterDepth;
int maxWaterDepth;

public StructureValidatorUnderwater()
  {
  super(StructureValidationType.UNDERWATER);
  minWaterDepth = 1;
  maxWaterDepth = 40;
  }

@Override
protected void readFromLines(List<String> lines)
  {
  for(String line : lines)
    {
    if(line.toLowerCase().startsWith("minwaterdepth=")){minWaterDepth = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("maxwaterdepth=")){maxWaterDepth = StringTools.safeParseInt("=", line);}
    }
  }

@Override
protected void write(BufferedWriter out) throws IOException
  {
  out.write("minWaterDepth="+minWaterDepth);
  out.newLine();
  out.write("minWaterDepth="+maxWaterDepth);
  out.newLine(); 
  }

@Override
protected void setDefaultSettings(StructureTemplate template)
  {
  this.validTargetBlocks.addAll(WorldStructureGenerator.defaultTargetBlocks);
  int size = (template.ySize-template.yOffset)/3;
  this.borderSize = size;  
  this.maxLeveling = template.ySize-template.yOffset;
  this.maxFill = size;
  }

@Override
public boolean shouldIncludeForSelection(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  int id;
  int water = 0;
  int startY = y-1;
  y = WorldStructureGenerator.getTargetY(world, x, z, true)+1;
  water = startY - y + 1;
  if(water<minWaterDepth || water>maxWaterDepth)
    {  
    return false;
    }
  return true;
  }

@Override
public int getAdjustedSpawnY(World world, int x, int y, int z, int face,  StructureTemplate template, StructureBB bb)
  {
  return WorldStructureGenerator.getTargetY(world, x, z, true)+1;
  }

@Override
public boolean validatePlacement(World world, int x, int y, int z, int face,  StructureTemplate template, StructureBB bb)
  {
  int minY = getMinY(template, bb);
  int maxY = getMaxY(template, bb);
  int bx, bz;
  for(bx = bb.min.x-borderSize; bx<=bb.max.x+borderSize; bx++)
    {
    bz = bb.min.z-borderSize;
    if(!validateBlockHeightAndType(world, bx, bz, minY, maxY, true, this.validTargetBlocks))
      {
      return false;
      }
              
    bz = bb.max.z+borderSize;
    if(!validateBlockHeightAndType(world, bx, bz, minY, maxY, true, this.validTargetBlocks))
      {
      return false;
      }
    }
  for(bz = bb.min.z-borderSize+1; bz<=bb.max.z+borderSize-1; bz++)
    {
    bx = bb.min.x-borderSize;
    if(!validateBlockHeightAndType(world, bx, bz, minY, maxY, true, this.validTargetBlocks))
      {
      return false;
      }
    
    bx = bb.max.x+borderSize;
    if(!validateBlockHeightAndType(world, bx, bz, minY, maxY, true, this.validTargetBlocks))
      {
      return false;
      }
    }
  return true;
  }

@Override
public void preGeneration(World world, int x, int y, int z, int face,  StructureTemplate template, StructureBB bb)
  {
  doStructurePrePlacement(world, x, y, z, face, template, bb);
  }

private void doStructurePrePlacement(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {    
  for(int bx = bb.min.x-borderSize; bx<= bb.max.x+borderSize; bx++)
    {
    for(int bz = bb.min.z-borderSize; bz<= bb.max.z+borderSize; bz++)
      {
      if(bx<bb.min.x || bx>bb.max.x || bz<bb.min.z || bz>bb.max.z)
        {//is border block, do border clear/fill
        doStructurePrePlacementBlockPlace(world, bx, bz, template, bb, true);
        }
      else
        {//is structure block, do structure clear/fill
        doStructurePrePlacementBlockPlace(world, bx, bz, template, bb, false);
        }
      }
    }  
  }

private void doStructurePrePlacementBlockPlace(World world, int x, int z, StructureTemplate template, StructureBB bb, boolean border)
  {
  int leveling = maxLeveling;
  int fill = maxFill;
  
  /**
   * most of this is just to try and minimize the total Y range that is examined for clear/fill
   */
  int minFillY = bb.min.y - fill;
  if(border){minFillY+=template.yOffset;}
  int maxFillY = (minFillY + fill) -1;
  
  int minLevelY = bb.min.y + template.yOffset;
  int maxLevelY = minLevelY + leveling;
  
  int minY = minFillY< minLevelY ? minFillY : minLevelY;
  if(!border)
    {
    if(fill>0)
      {//for inside-structure bounds, we want to fill down to whatever is existing if fill is>0    
      int topEmptyBlockY = WorldStructureGenerator.getTargetY(world, x, z, true)+1;
      minY = minY< topEmptyBlockY ? minY : topEmptyBlockY;
      }    
    }  
  else
    {
    int step = WorldStructureGenerator.getStepNumber(x, z, bb.min.x, bb.max.x, bb.min.z, bb.max.z);
    int stepHeight = fill / borderSize;
    maxFillY -= step*stepHeight;
    minLevelY += step*stepHeight;
    minY = minFillY < minLevelY ? minFillY : minLevelY;//reset minY from change to minLevelY
    }
  
  minY = minY<=0 ? 1 : minY;
  int maxY = maxFillY> maxLevelY ? maxFillY : maxLevelY;
  
  int xInChunk = x&15;
  int zInChunk = z&15;  
  Chunk chunk = world.getChunkFromBlockCoords(x, z);
  
  int id;
  Block block;
  BiomeGenBase biome = world.getBiomeGenForCoords(x, z);  
  int fillBlockID = Block.grass.blockID;
  if(biome!=null && biome.topBlock>=1)
    {
    fillBlockID = biome.topBlock;
    }
  for(int y = minY; y <=maxY; y++)
    {    
    id = world.getBlockId(x, y, z);
    block = Block.blocksList[id];
    if(leveling>0 && y>=minLevelY)
      {
      if(block!=null && !WorldStructureGenerator.skippableWorldGenBlocks.contains(block.getUnlocalizedName()))
        {
        chunk.setBlockIDWithMetadata(xInChunk, y, zInChunk, Block.waterStill.blockID, 0);        
        }
      }
    if(fill>0 && y<=maxFillY)
      {
      if(block==null || !WorldStructureGenerator.skippableWorldGenBlocks.contains(block.getUnlocalizedName()))
        {
        chunk.setBlockIDWithMetadata(xInChunk, y, zInChunk, fillBlockID, 0);
        }
      }
    }
  }

@Override
public void handleClearAction(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  world.setBlock(x, y, z, Block.waterStill.blockID);
  }

}
