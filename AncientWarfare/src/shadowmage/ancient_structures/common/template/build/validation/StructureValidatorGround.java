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

public class StructureValidatorGround extends StructureValidator
{



int maxLeveling;
boolean doLeveling;
int maxFill;
boolean doFillBelow;
int borderSize;
int borderMaxLeveling;
boolean doBorderLeveling;

int borderMaxFill;
boolean doBorderFill;
boolean gradientBorder;

Set<String> acceptedTargetBlocks;//list of accepted blocks which the structure may be built upon or filled over -- 100% of blocks directly below the structure must meet this list
Set<String> acceptedClearBlocks;//list of blocks which may be cleared/removed during leveling and buffer operations. 100% of blocks to be removed must meet this list


public StructureValidatorGround()
  {
  super(StructureValidationType.GROUND);
  acceptedTargetBlocks = new HashSet<String>();
  acceptedClearBlocks = new HashSet<String>();
  }

@Override
protected void readFromLines(List<String> lines)
  {
  for(String line : lines)
    {
    if(line.toLowerCase().startsWith("leveling=")){maxLeveling = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("fill=")){maxFill = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("border=")){borderSize = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("borderleveling=")){borderMaxLeveling = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("borderfill=")){borderMaxFill = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("doleveling=")){doLeveling = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("dofill=")){doFillBelow = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("doborderleveling=")){doBorderLeveling = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("doborderfill=")){doBorderFill = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("gradientborder=")){gradientBorder = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("validtargetblocks=")){StringTools.safeParseStringsToSet(acceptedTargetBlocks, "=", line, false);}
    else if(line.toLowerCase().startsWith("validclearingblocks=")){StringTools.safeParseStringsToSet(acceptedClearBlocks, "=", line, false);}
    }
  }

@Override
protected void write(BufferedWriter writer) throws IOException
  {
  writer.write("leveling="+maxLeveling);
  writer.newLine();
  writer.write("fill="+maxFill);
  writer.newLine();
  writer.write("border="+borderSize);
  writer.newLine();
  writer.write("borderLeveling="+borderMaxLeveling);
  writer.newLine();
  writer.write("borderFill="+borderMaxFill);
  writer.newLine();
  writer.write("doLeveling="+doLeveling);
  writer.newLine();
  writer.write("doFill="+doFillBelow);
  writer.newLine();
  writer.write("doBorderLeveling="+doBorderLeveling);
  writer.newLine();
  writer.write("doBorderFill="+doBorderFill);
  writer.newLine();  
  writer.write("gradientBorder="+gradientBorder);
  writer.newLine();
  writer.write("validTargetBlocks="+StringTools.getCSVfor(acceptedTargetBlocks));
  writer.newLine();
  writer.write("validClearingBlocks="+StringTools.getCSVfor(acceptedClearBlocks));
  writer.newLine();  
  }

@Override
protected void setDefaultSettings(StructureTemplate template)
  {
  this.acceptedClearBlocks.addAll(WorldStructureGenerator.defaultClearBlocks);
  this.acceptedTargetBlocks.addAll(WorldStructureGenerator.defaultTargetBlocks);
  this.doBorderFill = true;
  this.doBorderLeveling = true;
  this.doFillBelow = true;
  this.doLeveling = true;
  this.gradientBorder = true;  
  int size = (template.ySize-template.yOffset)/3;
  this.borderSize = size;
  this.borderMaxFill = size;
  this.borderMaxLeveling = size;
  this.maxLeveling = template.ySize-template.yOffset;
  this.maxFill = size;
  
  

  }

@Override
public boolean shouldIncludeForSelection(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  if( y <= template.yOffset+maxFill){return false;}
  Block block = Block.blocksList[world.getBlockId(x, y-1, z)];
  if(block==null || !acceptedTargetBlocks.contains(block.getUnlocalizedName())){return false;}
  return true;
  }

@Override
public boolean validatePlacement(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  return validateStructurePlacement3(world, x, y, z, face, template, bb);
  }

@Override
public void preGeneration(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  doStructurePrePlacement(world, x, y, z, face, template);
  }

private boolean validateStructurePlacement3(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  /**
   *  
   * when validating, need to check:
   *    check for missing edge depth/overhang height, and target blocks along outside edge (including border, single check on very outskirts of template)
   *    check for clearing blocks within structure bounds??
   *    check for cut-in height along structure edge
   *    
   */
  int bx, bz, bottomY, topY;
  bottomY = borderSize > 0 ? bb.min.y + template.yOffset - borderMaxFill : bb.min.y - maxFill;
  topY = borderSize> 0 ? bb.min.y+template.yOffset + borderMaxLeveling : bb.min.y + template.yOffset + maxLeveling;
  int maxFillY = borderSize > 0 ? bb.min.y+template.yOffset-1 : bb.min.y-1;
  for(bx = bb.min.x-borderSize; bx<=bb.max.x+borderSize; bx++)
    {
    bz = bb.min.z-borderSize;
    if(!validateBlock(world, bx, bz, bottomY, topY, maxFillY))
      {
      return false;
      }    
    bz = bb.max.z+borderSize;
    if(!validateBlock(world, bx, bz, bottomY, topY, maxFillY))
      {
      return false;
      }
    }
  for(bz = bb.min.z-borderSize+1; bz<=bb.max.z+borderSize-1; bz++)
    {
    bx = bb.min.x-borderSize;
    if(!validateBlock(world, bx, bz, bottomY, topY, maxFillY))
      {
      return false;
      }    
    bx = bb.max.x+borderSize;
    if(!validateBlock(world, bx, bz, bottomY, topY, maxFillY))
      {
      return false;
      }
    }
  return true;
  }

private boolean validateBlock(World world, int x, int z, int minY, int maxY, int maxFillY)
  {
  int topEmptyY = WorldStructureGenerator.getTargetY(world, x, z)+1;
  if(topEmptyY<=minY || topEmptyY>maxY)
    {
    AWLog.logDebug("rejected for leveling or depth test. foundY: "+topEmptyY + " min: "+minY +" max:"+maxY +  " at: "+x+","+topEmptyY+","+z);
    return false;
    }
  Block block;
  if(topEmptyY-1<=maxFillY)
    {
    block = Block.blocksList[world.getBlockId(x, topEmptyY-1, z)];
    if(block==null || !acceptedTargetBlocks.contains(block.getUnlocalizedName()))
      {
      AWLog.logDebug("rejected for invalid target block: "+(block==null ? "air" : block.getUnlocalizedName())+  " at: "+x+","+(topEmptyY-1)+","+z);
      return false;
      }
    }  
  block = Block.blocksList[world.getBlockId(x, topEmptyY, z)];
  if(block!=null && !WorldStructureGenerator.skippableWorldGenBlocks.contains(block.getUnlocalizedName()) && !acceptedTargetBlocks.contains(block.getUnlocalizedName()))
    {
    AWLog.logDebug("rejected for invalid target block: "+block.getUnlocalizedName()+  " at: "+x+","+topEmptyY+","+z);
    return false;
    }
  return true;
  }

private void doStructurePrePlacement(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  StructureBB bb = new StructureBB(x, y, z, face, template);  
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
  boolean doLeveling = border ? doBorderLeveling : this.doLeveling;
  boolean doFill = border ? doBorderFill : doFillBelow;
  int leveling = border? borderMaxLeveling : maxLeveling;
  int fill = border? borderMaxFill : maxFill;
  
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
      int topEmptyBlockY = WorldStructureGenerator.getTargetY(world, x, z)+1;
      minY = minY< topEmptyBlockY ? minY : topEmptyBlockY;
      }    
    }  
  else if(gradientBorder)
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
    if(doLeveling && leveling>0 && y>=minLevelY)
      {
      if(block!=null && (!WorldStructureGenerator.skippableWorldGenBlocks.contains(block.getUnlocalizedName()) || chunk.getBlockID(xInChunk, y-1, zInChunk)==0) && acceptedClearBlocks.contains(block.getUnlocalizedName()))
        {
        chunk.setBlockIDWithMetadata(xInChunk, y, zInChunk, 0, 0);        
        }
      }
    if(doFill && fill>0 && y<=maxFillY)
      {
      if(block==null || !WorldStructureGenerator.skippableWorldGenBlocks.contains(block.getUnlocalizedName()))
        {
        chunk.setBlockIDWithMetadata(xInChunk, y, zInChunk, fillBlockID, 0);
        }
      }
    }
  }

@Override
public int getAdjustedSpawnY(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  return y;
  }

}