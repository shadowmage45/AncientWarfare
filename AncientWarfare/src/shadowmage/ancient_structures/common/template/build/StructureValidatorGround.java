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
package shadowmage.ancient_structures.common.template.build;

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
import shadowmage.ancient_structures.common.world_gen.WorldStructureGenerator;

public class StructureValidatorGround extends StructureValidator
{

public static HashSet<String> defaultTargetBlocks = new HashSet<String>();
public static HashSet<String> defaultClearBlocks = new HashSet<String>();

static
{
defaultTargetBlocks.add(Block.dirt.getUnlocalizedName());
defaultTargetBlocks.add(Block.grass.getUnlocalizedName());
defaultTargetBlocks.add(Block.stone.getUnlocalizedName());
defaultTargetBlocks.add(Block.sand.getUnlocalizedName());
defaultTargetBlocks.add(Block.gravel.getUnlocalizedName());
defaultTargetBlocks.add(Block.sandStone.getUnlocalizedName());
defaultTargetBlocks.add(Block.blockClay.getUnlocalizedName());
defaultTargetBlocks.add(Block.oreIron.getUnlocalizedName());
defaultTargetBlocks.add(Block.oreCoal.getUnlocalizedName());

defaultClearBlocks.addAll(defaultTargetBlocks);
defaultClearBlocks.add(Block.waterStill.getUnlocalizedName());
defaultClearBlocks.add(Block.lavaStill.getUnlocalizedName());
defaultClearBlocks.add(Block.cactus.getUnlocalizedName());
defaultClearBlocks.add(Block.vine.getUnlocalizedName());
defaultClearBlocks.add(Block.tallGrass.getUnlocalizedName());
defaultClearBlocks.add(Block.wood.getUnlocalizedName());
defaultClearBlocks.add(Block.plantRed.getUnlocalizedName());
defaultClearBlocks.add(Block.plantYellow.getUnlocalizedName());
defaultClearBlocks.add(Block.deadBush.getUnlocalizedName());
defaultClearBlocks.add(Block.leaves.getUnlocalizedName());
defaultClearBlocks.add(Block.wood.getUnlocalizedName());
defaultClearBlocks.add(Block.snow.getUnlocalizedName());
}

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
    else if(line.toLowerCase().startsWith("validtargetblocks=")){parseStringsToSet(acceptedTargetBlocks, StringTools.safeParseStringArray("=", line), false);}
    else if(line.toLowerCase().startsWith("validclearingblocks=")){parseStringsToSet(acceptedClearBlocks, StringTools.safeParseStringArray("=", line), false);}
    }
  }

private void parseStringsToSet(Set<String> toFill, String[] data, boolean lowerCase)
  {
  for(String name : data)
    {
    toFill.add(lowerCase? name.toLowerCase() : name);
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
  writer.write("validTargetBlocks="+StringTools.getCSVValueFor(acceptedTargetBlocks.toArray(new String[acceptedTargetBlocks.size()])));
  writer.newLine();
  writer.write("validClearingBlocks="+StringTools.getCSVValueFor(acceptedClearBlocks.toArray(new String[acceptedClearBlocks.size()])));
  writer.newLine();  
  }

@Override
protected void setDefaultSettings(StructureTemplate template)
  {
  this.acceptedClearBlocks.addAll(defaultClearBlocks);
  this.acceptedTargetBlocks.addAll(defaultTargetBlocks);
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
  if(topEmptyY-1<=maxFillY)
    {
    Block block = Block.blocksList[world.getBlockId(x, topEmptyY-1, z)];
    if(block==null || !acceptedTargetBlocks.contains(block.getUnlocalizedName()))
      {
      AWLog.logDebug("rejected for invalid target block: "+(block==null ? "air" : block.getUnlocalizedName())+  " at: "+x+","+topEmptyY+","+z);
      return false;
      }
    }  
  return true;
  }

private boolean validateStructurePlacement(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {  
  /**
   * search the entire structure area, min->max for valid target conditions.
   */
  int bx, by, bz;
  for(bx = bb.min.x-borderSize; bx <= bb.max.x+borderSize ; bx++)
    {
    for(bz = bb.min.z-borderSize; bz <= bb.max.z+borderSize ; bz++)
      {      
      if(bx < bb.min.x || bx > bb.max.x || bz < bb.min.z || bz > bb.max.z)//is outside bounds, must be a border block
        {
        if(!validateStructureBlock(world, bx, bz, template.yOffset, bb, true))
          {
          return false;
          }
        }
      else//is inside bounds, must be a regular structure block
        {
        if(!validateStructureBlock(world, bx, bz, template.yOffset, bb, false))
          {
          return false;
          }
        }
      }
    }
  return true;
  }

private boolean validateStructureBlock(World world, int x, int z, int yOffset, StructureBB bb, boolean border)
  {
  Set<String> targetBlocks = acceptedTargetBlocks;
  Set<String> clearBlocks = acceptedClearBlocks;
  int fill = border? borderMaxFill : maxFill;
  int leveling = border? borderMaxLeveling : maxLeveling;
  int id;
  int minY = bb.min.y-fill-1;
  if(border)
    {
    minY += yOffset;
    }
  Chunk chunk = world.getChunkFromBlockCoords(x, z);
  int inChunkX = x & 15;
  int inChunkZ = z & 15;  
  Block block;
  for(int y = world.provider.getActualHeight(); y >= minY && y>=0; y--)
    {    
    id = chunk.getBlockID(inChunkX, y, inChunkZ);
    block = Block.blocksList[id];
    if(fill>=0 && (border || (x==bb.min.x || x==bb.max.x || z==bb.min.z || z==bb.max.z)) && y <= minY && (block==null || !targetBlocks.contains(block.getUnlocalizedName())))
      {
      AWLog.logDebug("invalid edge border depth or target block: y: "+y + " minY: "+minY+ " block: "+block);
      return false;//fail for border-edge-depth test
      }
    else if(block==null || WorldStructureGenerator.skippableWorldGenBlocks.contains(block.getUnlocalizedName()))
      {//block is within the area to be cleared or filled, but not a base target block -- skip empty blocks or 'skippable' blocks
      continue;
      }
    else if(leveling>=0 && y >= bb.min.y + yOffset + leveling)
      {//max leveling target too high
      AWLog.logDebug("block too high for structure leveling value: "+y+ " target: "+(bb.min.y+yOffset+leveling));
      return false;
      }
    else if(leveling>=0 && !isPreserveBlocks() && y >= bb.min.y+yOffset && !clearBlocks.contains(block.getUnlocalizedName()))
      {//invalid block to clear
      AWLog.logDebug("invalid clearing block");
      return false;
      }
    else if(fill>=0 && y < (border? bb.min.y+yOffset : bb.min.y) && !targetBlocks.contains(block.getUnlocalizedName()))
      {//invalid block to fill-on-top of
      AWLog.logDebug("invalid fill-on-top-of block: "+block.getUnlocalizedName());
      return false;
      }    
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
    int step = getStepNumber(x, z, bb.min.x, bb.max.x, bb.min.z, bb.max.z);
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
      if(block!=null && !WorldStructureGenerator.skippableWorldGenBlocks.contains(block.getUnlocalizedName()) && acceptedClearBlocks.contains(block.getUnlocalizedName()))
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

private int getStepNumber(int x, int z, int minX, int maxX, int minZ, int maxZ)
  {
  int steps = 0;
  if(x<minX-1)
    {
    steps += (minX-1) - x;
    }
  else if(x > maxX+1)
    {
    steps += x - (maxX+1);
    }  
  if(z<minZ-1)
    {
    steps += (minZ-1) - z;
    }
  else if(z > maxZ+1)
    {
    steps += z - (maxZ+1);
    }  
//  AWLog.logDebug("getting step number for: "+x+","+z+" min: "+minX+","+minZ+" :: max: "+maxX+","+maxZ + " stepHeight: "+steps);
  return steps;
  }

}
