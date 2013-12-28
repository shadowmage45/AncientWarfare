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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.gamedata.AWGameData;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.world_gen.StructureEntry;
import shadowmage.ancient_structures.common.world_gen.StructureMap;
import shadowmage.ancient_structures.common.world_gen.WorldStructureGenerator;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class StructureValidatorGround extends StructureValidator
{
/**
 * given an area with a source point, how far above the source-point is the highest acceptable block located? 
 * e.g. a 'normal' setting would be the same height as the structures above-ground height which would allow
 * generation of a structure with no blocks left 'hanging' above it, and minimal cut-in into a cliffside/etc
 * e.g. a 'perfect' setting would be 0, meaning the ground must be flat and level prior to generation
 * 
 * negative values imply to skip leveling checking
 */
int maxLeveling;

/**
 * if true, will clear blocks in leveling bounds PRIOR to template construction.  Set to false if you wish to
 * preserve any existing blocks within the structure bounds.
 */
boolean doLeveling;

/**
 * given an area with a source point, how far below the source point may 'holes' extend into the ground along the
 * edges of the structure.
 * e.g. a 'normal' setting would be 1-2 blocks, which would ensure that the chosen site was flat enough that it would
 * generate with minimal under-fill.
 * e.g. an 'extreme' setting to force-placement might be 4-8 blocks or more.  Placement would often be ugly with only
 * part of the structure resting on the ground.
 * 
 * negative values imply to skip edge-depth checking
 */
int maxFill;

/**
 * if true, will fill _directly_ below the structure down to the specified number of blocks from maxMissingEdgeDepth
 * filling will occur PRIOR to template construction.
 */
boolean doFillBelow;

/**
 * the size of the border around the base structure BB to check for additional functions
 * 0 or negative values denote no border.
 */
int borderSize;

/**
 * same as with structure-leveling -- how much irregularity can there be above the chose ground level
 * negative values imply to skip border leveling tests
 */
int borderMaxLeveling;
boolean doBorderLeveling;

/**
 * how irregular can the border surrounding the structure be in the -Y direction?
 * negative values imply to skip border depth tests
 */
int borderMaxFill;
boolean doBorderFill;

boolean gradientBorder;



Set<String> acceptedTargetBlocks;//list of accepted blocks which the structure may be built upon or filled over -- 100% of blocks directly below the structure must meet this list
Set<String> acceptedTargetBlocksBorder;
Set<String> acceptedTargetBlocksBorderRear;


Set<String> acceptedClearBlocks;//list of blocks which may be cleared/removed during leveling and buffer operations. 100% of blocks to be removed must meet this list

/**
 * world generation selection and clustering settings
 */


public StructureValidatorGround()
  {
  super(StructureValidationType.GROUND);
  }

@Override
protected void readFromNBT(NBTTagCompound tag)
  {

  }

@Override
protected void writeToNBT(NBTTagCompound tag)
  {

  }

@Override
public boolean validatePlacement(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  return validateStructurePlacement(world, x, y, z, face, template);
  }

@Override
public void preGeneration(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  doStructurePrePlacement(world, x, y, z, face, template);
  }

private boolean validateStructurePlacement(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  StructureBB bb = new StructureBB(x, y, z, face, template.xSize, template.ySize, template.zSize, template.xOffset, template.yOffset, template.zOffset);
  StructureMap map = AWGameData.get(world, "AWStructureMap", StructureMap.class);
  
  int xs = bb.getXSize();
  int zs = bb.getZSize();
  int size = ((xs > zs ? xs : zs)/16)+3;
  Collection<StructureEntry> bbCheckList = map.getEntriesNear(world, x, z, size, true, new ArrayList<StructureEntry>());
  bb.expand(borderSize, 0, borderSize);//expand by border-size to help catch issues of border cutting into other structures
  for(StructureEntry entry : bbCheckList)
    {
    if(bb.collidesWith(entry.getBB()))
      {
      AWLog.logDebug("invalid placement, intersects with other structure");
      return false;
      }
    }
  bb.expand(-borderSize, 0, -borderSize);//un-expand the bb, so we can continue to use it for the rest of the checks
  
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
  Set<String> targetBlocks = border? acceptedTargetBlocksBorder : acceptedTargetBlocks;
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
  for(int y = world.provider.getActualHeight(); y >= minY-1 && y>=0; y--)
    {    
    id = chunk.getBlockID(inChunkX, y, inChunkZ);
    block = Block.blocksList[id];
    if(fill>=0 && (border || (x==bb.min.x || x==bb.max.x || z==bb.min.z || z==bb.max.z)) && y < minY && (block==null || !targetBlocks.contains(block.getUnlocalizedName())))
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
      AWLog.logDebug("block too high for structure leveling value");
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
      if(block!=null && !WorldStructureGenerator.skippableWorldGenBlocks.contains(block.getUnlocalizedName()))
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

@Override
protected void setDefaultSettings(StructureTemplate template)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public boolean shouldIncludeForSelection(World world, int x, int y, int z,
    int face, StructureTemplate template)
  {
  // TODO Auto-generated method stub
  return false;
  }
}
