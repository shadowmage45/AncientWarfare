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
package shadowmage.ancient_structures.common.world_gen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.gamedata.AWGameData;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.manager.WorldGenStructureManager;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBB;
import shadowmage.ancient_structures.common.template.build.StructureBuilder;
import shadowmage.ancient_structures.common.template.build.StructureValidationSettingsDefault;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldStructureGenerator implements IWorldGenerator
{

private static WorldStructureGenerator instance = new WorldStructureGenerator();
private WorldStructureGenerator(){}
public static WorldStructureGenerator instance(){return instance;}
public static HashSet<String> skippableWorldGenBlocks = new HashSet<String>();

static
{
skippableWorldGenBlocks.add(Block.waterStill.getUnlocalizedName());
skippableWorldGenBlocks.add(Block.lavaStill.getUnlocalizedName());
skippableWorldGenBlocks.add(Block.cactus.getUnlocalizedName());
skippableWorldGenBlocks.add(Block.vine.getUnlocalizedName());
skippableWorldGenBlocks.add(Block.tallGrass.getUnlocalizedName());
skippableWorldGenBlocks.add(Block.wood.getUnlocalizedName());
skippableWorldGenBlocks.add(Block.plantRed.getUnlocalizedName());
skippableWorldGenBlocks.add(Block.plantYellow.getUnlocalizedName());
skippableWorldGenBlocks.add(Block.deadBush.getUnlocalizedName());
skippableWorldGenBlocks.add(Block.leaves.getUnlocalizedName());
}

private boolean isGenerating = false;
private LinkedList<DelayedGenerationEntry> delayedChunks = new LinkedList<DelayedGenerationEntry>();
private Random rng = new Random();

@Override
public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
  {
//  if(true){return;}
  if(isGenerating)
    {
//    AWLog.logDebug("delaying generation for chunk: "+chunkX+", "+chunkZ);
    delayedChunks.add(new DelayedGenerationEntry(chunkX, chunkZ, world, chunkGenerator, chunkProvider));
    return;
    }
  else
    {
    isGenerating = true;
//    AWLog.logDebug("checking structure generation for chunk: "+chunkX+", "+chunkZ);
    generateAt(chunkX, chunkZ, world, chunkGenerator, chunkProvider);
    }
  while(!delayedChunks.isEmpty())
    {    
    DelayedGenerationEntry entry = delayedChunks.poll();
//    AWLog.logDebug("generating delayed chunk: "+entry.chunkX+", "+entry.chunkZ);
    generateAt(entry.chunkX, entry.chunkZ, entry.world, entry.generator, entry.provider);
    }
  isGenerating = false;
  }

private void generateAt(int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
  {
  if(AWStructureStatics.enableStructureGeneration){return;}
  long t1 = System.currentTimeMillis();
  long seed = (((long)chunkX)<< 32) | (((long)chunkZ) & 0xffffffffl);
  rng.setSeed(seed);
  int x = chunkX*16 + rng.nextInt(16);  
  int z = chunkZ*16 + rng.nextInt(16); 
  int y = getTargetY(world, x, z)+1;  
  if(y<=0){return;}
  int face = rng.nextInt(4);
  StructureTemplate template = WorldGenStructureManager.instance().selectTemplateForGeneration(world, rng, x, y, z, AWStructureStatics.chunkSearchRadius);
  int remainingClusterValue = WorldGenStructureManager.instance().getRemainingValue();
  
  if(Statics.DEBUG)
    {
    AWLog.logDebug("Template selection took: "+(System.currentTimeMillis()-t1)+" ms.");
    }
  if(template==null){return;} 
  StructureMap map = AWGameData.get(world, "AWStructureMap", StructureMap.class);
  if(attemptStructureGenerationAt(world, x, y, z, face, template, map))
    {
    AWLog.log(String.format("Generated structure: %s at %s, %s, %s", template.name, x, y, z));
    } 
  }

/**
 * returns the Y coordinate for the top filled block, skipping any blocks in the
 * 'skippableWorldGenBlocks' set
 * @param world
 * @param x
 * @param z
 * @return
 */
public static int getTargetY(World world, int x, int z)
  {
  int id;
  Block block;
  for(int y = world.provider.getActualHeight(); y>=1; y--)
    {
    id = world.getBlockId(x, y, z);
    if(id==0){continue;}
    block = Block.blocksList[id];
    if(block==null){continue;}
    if(skippableWorldGenBlocks.contains(block.getUnlocalizedName())){continue;}
    return y;
    }
  return -1;
  }

/**
 * so that generation methods can be called externally (from test-item, to test for validation settings)
 * @param world
 * @param x
 * @param y
 * @param z
 * @param face
 * @param template
 * @return
 */
public boolean attemptStructureGenerationAt(World world, int x, int y, int z, int face, StructureTemplate template, StructureMap map)
  {
  boolean generate = false;
  long t1, t2;
  t1 = System.currentTimeMillis();
  generate = validateStructurePlacement(world, x, y, z, face, template, map);
  if(Statics.DEBUG)
    {
    AWLog.logDebug("validation took: "+(System.currentTimeMillis()-t1+" ms"));   
    }
  if(generate)
    {    
    t2 = System.currentTimeMillis();
    generateStructureAt(world, x, y, z, face, template, map);
    if(Statics.DEBUG)
      {
      AWLog.logDebug("generation took: "+(System.currentTimeMillis()-t2)+" ms");      
      }
    }  
  return generate;
  }

private void generateStructureAt(World world, int x, int y, int z, int face, StructureTemplate template, StructureMap map)
  {
  doStructurePrePlacement(world, x, y, z, face, template);
  StructureBuilder builder = new StructureBuilder(world, template, face, x, y, z);
  builder.instantConstruction();
  map.setGeneratedAt(world, x, y, z, face, template);
  map.markDirty();
  }

private boolean validateStructurePlacement(World world, int x, int y, int z, int face, StructureTemplate template, StructureMap map)
  {
  StructureBB bb = new StructureBB(x, y, z, face, template.xSize, template.ySize, template.zSize, template.xOffset, template.yOffset, template.zOffset);
  StructureValidationSettingsDefault settings = template.getValidationSettings();
  int borderSize = settings.getBorderSize();
 
  /**
   * check for intersecting bounding boxes
   */
  int xs = bb.getXSize();
  int zs = bb.getZSize();
  int size = ((xs > zs ? xs : zs)/16)+3;
  Collection<StructureEntry> bbCheckList = map.getEntriesNear(world, x, z, size, true, new ArrayList<StructureEntry>());
  bb.expand(borderSize, 0, borderSize);//expand by border-size to help catch issues of border cutting into other structures
  for(StructureEntry entry : bbCheckList)
    {
    if(bb.collidesWith(entry.bb))
      {
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
        if(!validateStructureBlock(world, bx, bz, template.yOffset, settings, bb, true))
          {
          return false;
          }
        }
      else//is inside bounds, must be a regular structure block
        {
        if(!validateStructureBlock(world, bx, bz, template.yOffset, settings, bb, false))
          {
          return false;
          }
        }
      }
    }
  return true;
  }

private boolean validateStructureBlock(World world, int x, int z, int yOffset, StructureValidationSettingsDefault settings, StructureBB bb, boolean border)
  {
  Set<String> targetBlocks = settings.getAcceptedTargetBlocks();
  Set<String> clearBlocks = settings.getAcceptedClearBlocks();
  int fill = border? settings.getBorderMaxFill() : settings.getMaxFill();
  int leveling = border? settings.getBorderMaxLeveling() : settings.getMaxLeveling();
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
  for(int y = world.provider.getActualHeight(); y >= bb.min.y-fill-1; y--)
    {    
    id = chunk.getBlockID(inChunkX, y, inChunkZ);
    block = Block.blocksList[id];
    if(fill>=0 && (border || (x==bb.min.x || x==bb.max.x || z==bb.min.z || z==bb.max.z)) && y <= minY && (block==null || targetBlocks.contains(block.getUnlocalizedName())))
      {
      return false;//fail for
      }
    else if(block==null || skippableWorldGenBlocks.contains(block.getUnlocalizedName()))
      {//block is within the area to be cleared or filled, but not a base target block -- skip empty blocks or 'skippable' blocks
      continue;
      }
    else if(leveling>=0 && y >= bb.min.y + yOffset + leveling)
      {//max leveling target too high
      return false;
      }
    else if(leveling>=0 && !settings.isPreserveBlocks() && y >= bb.min.y+yOffset && !clearBlocks.contains(block.getUnlocalizedName()))
      {//invalid block to clear
      return false;
      }
    else if(fill>=0 && y < (border? bb.min.y+yOffset : bb.min.y) && !targetBlocks.contains(block.getUnlocalizedName()))
      {//invalid block to fill-on-top of
      return false;
      }    
    }    
  return true;
  }

private void doStructurePrePlacement(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  StructureBB bb = new StructureBB(x, y, z, face, template);
  int borderSize = template.getValidationSettings().getBorderSize();
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
  boolean doLeveling = border ? template.getValidationSettings().isDoBorderLeveling() : template.getValidationSettings().isDoLeveling();
  boolean doFill = border ? template.getValidationSettings().isDoBorderFill() : template.getValidationSettings().isDoFillBelow();
  int leveling = border? template.getValidationSettings().getBorderMaxLeveling() : template.getValidationSettings().getMaxLeveling();
  int fill = border? template.getValidationSettings().getBorderMaxFill() : template.getValidationSettings().getMaxFill();
  
  int minFillY = bb.min.y - fill;
  if(border){minFillY+=template.yOffset;}
  int maxFillY = minFillY + fill;
  
  int minLevelY = bb.min.y + template.yOffset;
  int maxLevelY = minLevelY + leveling;
  
  int minY = minFillY< minLevelY ? minFillY : minLevelY;
  if(!border)
    {
    if(fill>0)
      {//for inside-structure bounds, we want to fill down to whatever is existing if fill is>0
      int topEmptyBlockY = getTargetY(world, x, z)+1;
      minY = minY< topEmptyBlockY ? minY : topEmptyBlockY;
      }    
    }  
  else if(template.getValidationSettings().isGradientBorder())
    {
    int step = getStepNumber(x, z, bb.min.x, bb.max.x, bb.min.z, bb.max.z);
    int stepHeight = fill / template.getValidationSettings().getBorderSize();
    maxFillY-=step*stepHeight;
    minLevelY += step*stepHeight;
    minY = minFillY < minLevelY ? minFillY : minLevelY;//reset minY from change to minLevelY
    }
  int maxY = maxFillY> maxLevelY ? maxFillY : maxLevelY;
    
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
    if(doLeveling && leveling>0 && y>=minLevelY)
      {
      world.setBlockToAir(x, y, z);
      }
    if(doFill && fill>0 && y<=maxFillY)
      {
      id = world.getBlockId(x, y, z);
      block = Block.blocksList[id];
      if(block==null || !skippableWorldGenBlocks.contains(block.getUnlocalizedName()))
        {
        world.setBlock(x, y, z, fillBlockID);
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


private boolean validateStructurePlacementOld(World world, int x, int y, int z, int face, StructureTemplate template, StructureMap map)
  {  
  StructureBB bb = new StructureBB(x, y, z, face, template.xSize, template.ySize, template.zSize, template.xOffset, template.yOffset, template.zOffset);
//  AWLog.logDebug("testing structureBB of: "+bb);
  StructureValidationSettingsDefault settings = template.getValidationSettings();
  /**
   * check for colliding bounding boxes
   */
  int xs = bb.getXSize();
  int zs = bb.getZSize();
  int size = ((xs > zs ? xs : zs)/16)+3;
  Collection<StructureEntry> bbCheckList = map.getEntriesNear(world, x, z, size, true, new ArrayList<StructureEntry>());
  for(StructureEntry entry : bbCheckList)
    {
    if(bb.collidesWith(entry.bb))
      {
      return false;
      }
    }
  
  /**
   * most checks are done in a single loop across structure bounds
   */
  int bx, bz;
  int by = bb.min.y - 1;
  int topEmptyBlockY;
  int id;
  Block targetBlock;
  int maxLeveling = settings.getMaxLeveling();
  int maxFill = settings.getMaxFill();
  Set<String> targetBlocks = settings.getAcceptedTargetBlocks();
  Set<String> clearBlocks = settings.getAcceptedClearBlocks();
  for(bx = bb.min.x; bx <= bb.max.x ; bx++)
    {
    for(bz = bb.min.z; bz <= bb.max.z ; bz++)
      {
      /**
       * check for leveling and fill depth
       */
      topEmptyBlockY = getTargetY(world, bx, bz)+1;
      if(topEmptyBlockY<=0){return false;}//fail due to...wtf? no block?
      if(maxFill >= 0 && (bx==bb.min.x || bx==bb.max.x || bz == bb.min.z || bz== bb.max.z))//missing edge depth test
        {        
        if(bb.min.y - topEmptyBlockY > maxFill)
          {
          return false;//fail missing edge depth test
          }
        id = world.getBlockId(bx, topEmptyBlockY-1, bz);
        targetBlock = Block.blocksList[id];
        if(topEmptyBlockY<bb.min.y && !targetBlocks.contains(targetBlock.getUnlocalizedName()))
          {
          return false;//fail for block to be filled on top of is invalid target block
          }
        }      
      if(maxLeveling>=0 && topEmptyBlockY - (bb.min.y+template.yOffset) > maxLeveling)
        {
        return false;//fail for leveling too high
        }
     
      /**
       * check target blocks below structure
       */
      id = world.getBlockId(bx, by, bz);
      targetBlock = Block.blocksList[id];
      if(targetBlock!=null && !targetBlocks.contains(targetBlock.getUnlocalizedName()))
        {
        return false;
        }
      
      /**
       * check clearing blocks
       */
      if(!settings.isPreserveBlocks())
        {
        for(int cy = bb.min.y; cy <= topEmptyBlockY; cy++)
          {       
          id = world.getBlockId(bx, cy, bz);
          targetBlock = Block.blocksList[id];
          if(targetBlock!=null && targetBlock.blockMaterial != Material.plants && !clearBlocks.contains(targetBlock.getUnlocalizedName()))
            {
            return false;//fail for block clear check
            }
          }  
        }          
      }
    }
  
  /**
   * check border for leveling / fill
   * while structure fill was below structure minY level,
   * border fill is below build Y level (input variable y)
   */
  int borderSize = settings.getBorderSize();
  maxLeveling = settings.getBorderMaxLeveling();
  maxFill = settings.getBorderMaxFill();  
  if(borderSize>0)
    {
    if(!checkBorderValidity(world, y, borderSize, maxLeveling, maxFill, bb, clearBlocks, targetBlocks))
      {
      return false;
      }      
    }
  return true;
  }

private boolean checkBorderValidity(World world, int y, int borderSize, int maxLeveling, int maxFill, StructureBB bb, Set<String> clearBlocks, Set<String> targetBlocks)
  {
  int bx, bz;  
  for(bx = bb.min.x - borderSize; bx <= bb.max.x + borderSize; bx++)
    {       
    for(bz = bb.min.z-1; bz >= bb.min.z - borderSize; bz--)
      {
      if(!checkBorderBlockValidity(world, bx, y, bz, maxLeveling, maxFill, clearBlocks, targetBlocks))
        {
        return false;
        }    
      }      
    for(bz = bb.max.z+1; bz <= bb.max.z + borderSize; bz++)
      {
      if(!checkBorderBlockValidity(world, bx, y, bz, maxLeveling, maxFill, clearBlocks, targetBlocks))
        {
        return false;
        }
      }
    }
  for(bz = bb.min.z; bz<=bb.max.z; bz++)
    {
    for(bx = bb.min.x-1; bx >= bb.min.x - borderSize; bx--)
      {
      if(!checkBorderBlockValidity(world, bx, y, bz, maxLeveling, maxFill, clearBlocks, targetBlocks))
        {
        return false;
        }
      }
    for(bx = bb.max.x+1; bx <= bb.max.x + borderSize; bx++)
      {
      if(!checkBorderBlockValidity(world, bx, y, bz, maxLeveling, maxFill, clearBlocks, targetBlocks))
        {
        return false;
        }      
      }
    }
  return true;
  }

private boolean checkBorderBlockValidity(World world, int x, int y, int z, int maxLeveling, int maxFill, Set<String> validClearingBlocks, Set<String> validTargetBlocks)
  {
  int topEmptyBlockY = getTargetY(world, x, z)+1;
  if(topEmptyBlockY<=0){return false;}
  int heightDiff = topEmptyBlockY - y;
  if(maxLeveling>=0 && heightDiff > maxLeveling)
    {
    return false;
    } 
  int id;
  Block block;
  if(maxFill>0 && topEmptyBlockY<=y)
    {
    if(topEmptyBlockY < y-maxFill)
      {
      return false;
      }
    id = world.getBlockId(x, topEmptyBlockY-1, z);
    block = Block.blocksList[id];
    if(block!=null && !validTargetBlocks.contains(block.getUnlocalizedName()))
      {
      return false;
      }    
    }
  if(maxLeveling>0)
    {
    for(int by = topEmptyBlockY-1; by>=y; by--)//check border clearing blocks
      {
      id = world.getBlockId(x, by, z);
      block = Block.blocksList[id];
      if(block!=null && block.blockMaterial!=Material.wood && block.blockMaterial!=Material.plants && block.blockMaterial!= Material.leaves && !validClearingBlocks.contains(block.getUnlocalizedName()))
        {
        return false;
        }
      }    
    }  
  return true;
  }


private class DelayedGenerationEntry
{
int chunkX;
int chunkZ;
World world;
IChunkProvider generator;
IChunkProvider provider;

public DelayedGenerationEntry(int cx, int cz, World world, IChunkProvider gen, IChunkProvider prov)
  {
  this.chunkX = cx;
  this.chunkZ = cz;
  this.world = world;
  this.generator = gen;
  this.provider = prov;
  }
}

}
