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
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.gamedata.AWGameData;
import shadowmage.ancient_framework.common.utils.Trig;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.manager.WorldGenStructureManager;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBB;
import shadowmage.ancient_structures.common.template.build.StructureBuilderWorldGen;
import shadowmage.ancient_structures.common.template.build.StructureValidationSettingsDefault;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldStructureGenerator implements IWorldGenerator
{

private static WorldStructureGenerator instance = new WorldStructureGenerator();
private WorldStructureGenerator(){}
public static WorldStructureGenerator instance(){return instance;}


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
  long t1 = System.currentTimeMillis();
  long seed = (((long)chunkX)<< 32) | (((long)chunkZ) & 0xffffffffl);
  rng.setSeed(seed);
  int x = chunkX*16 + rng.nextInt(16);  
  int z = chunkZ*16 + rng.nextInt(16);  
  int face = rng.nextInt(4);
  StructureTemplate template = WorldGenStructureManager.instance().selectTemplateForGeneration(world, rng, x, z, AWStructureStatics.chunkSearchRadius);
  if(template==null){return;}
  int y = getTargetY(world, x, z)+1;
  if(y<=0){return;}
  StructureMap map = AWGameData.get(world, "AWStructureMap", StructureMap.class);
  if(attemptStructureGenerationAt(world, x, y, z, face, template, map))
    {
    long t2 = System.currentTimeMillis() - t1;
    AWLog.log(String.format("Generated structure: %s at %s, %s, %s  generation took: %sms", template.name, x, y, z, t2));
    }   
  else
    {
    long t2 = System.currentTimeMillis() - t1;
    AWLog.log(String.format("failed generation of structure: %s at %s, %s, %s  generation took: %sms", template.name, x, y, z, t2));
    }
  }

public static int getTargetY(World world, int x, int z)
  {
  int id;
  Block block;
  for(int y = world.provider.getActualHeight(); y>1; y--)
    {
    id = world.getBlockId(x, y, z);
    if(id==0){continue;}
    block = Block.blocksList[id];
    if(block==null || block.isAirBlock(world, x, y, z) || block.blockMaterial==Material.leaves || block.blockMaterial==Material.snow || block.blockMaterial==Material.plants){continue;}
    if(block==Block.tallGrass || block==Block.plantYellow || block==Block.deadBush || block==Block.cactus || block==Block.plantRed){continue;}    
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
  if(validateStructurePlacement(world, x, y, z, face, template, map))
    {   
    generateStructureAt(world, x, y, z, face, template, map);  
    return true;
    }
  return false;
  }

private boolean validateStructurePlacement(World world, int x, int y, int z, int face, StructureTemplate template, StructureMap map)
  {  
  StructureBB bb = new StructureBB(x, y, z, face, template.xSize, template.ySize, template.zSize, template.xOffset, template.yOffset, template.zOffset);
  AWLog.logDebug("testing structureBB of: "+bb);
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
//    AWLog.logDebug("testing vs existing bb: "+entry.getBB() + "  this:  "+bb);
    if(bb.collidesWith(entry.bb))
      {
//      AWLog.logDebug("structure failed placement for bb intersect with: "+entry.name);
      return false;
      }
    }
  
  /**
   * most checks are done in a single loop across structure bounds
   */
  int bx, bz;
  int by = bb.pos1.y - 1;
  int topEmptyBlockY;
  int id;
  Block targetBlock;
  int maxLeveling = settings.getMaxLeveling();
  int maxFill = settings.getMaxFill();
  Set<String> targetBlocks = settings.getAcceptedTargetBlocks();
  Set<String> clearBlocks = settings.getAcceptedClearBlocks();
  for(bx = bb.pos1.x; bx <= bb.pos2.x ; bx++)
    {
    for(bz = bb.pos1.z; bz <= bb.pos2.z ; bz++)
      {
      /**
       * check for leveling and fill depth
       */
      topEmptyBlockY = getTargetY(world, bx, bz)+1;
      if(topEmptyBlockY<=0){return false;}//fail due to...wtf? no block?
      if(maxFill >= 0 && (bx==bb.pos1.x || bx==bb.pos2.x || bz == bb.pos1.z || bz== bb.pos2.z))//missing edge depth test
        {
        
        if(bb.pos1.y - topEmptyBlockY > maxFill)
          {
          AWLog.logDebug("structure failed validation for fill depth test. val: "+maxFill + " found: "+(bb.pos1.y - topEmptyBlockY));
          return false;//fail missing edge depth test
          }
        id = world.getBlockId(bx, topEmptyBlockY-1, bz);
        targetBlock = Block.blocksList[id];
        if(topEmptyBlockY<bb.pos1.y && !targetBlocks.contains(targetBlock.getUnlocalizedName()))
          {
          AWLog.logDebug("structure failed validation for invalid target fill block");
          return false;//fail for block to be filled on top of is invalid target block
          }
        }      
      if(maxLeveling>=0 && topEmptyBlockY - (bb.pos1.y+template.yOffset) > maxLeveling)
        {
        AWLog.logDebug("structure failed validation for invalid leveling. maxLevel: "+maxLeveling +" found leveling difference: "+(topEmptyBlockY-(bb.pos1.y+template.yOffset)));
        return false;//fail for leveling too high
        }
     
      /**
       * check target blocks below structure
       */
      id = world.getBlockId(bx, by, bz);
      targetBlock = Block.blocksList[id];
      if(targetBlock!=null && !targetBlocks.contains(targetBlock.getUnlocalizedName()))
        {
        AWLog.logDebug("structure failed validation for invalid target block: "+targetBlock.getUnlocalizedName());
        return false;
        }
      
      /**
       * check clearing blocks
       */
      if(!settings.isPreserveBlocks())
        {
        for(int cy = bb.pos1.y; cy <= topEmptyBlockY; cy++)
          {       
          id = world.getBlockId(bx, cy, bz);
          targetBlock = Block.blocksList[id];
          if(targetBlock!=null && targetBlock.blockMaterial != Material.plants && !clearBlocks.contains(targetBlock.getUnlocalizedName()))
            {
            AWLog.logDebug("structure failed validation for invalid clearing block: "+targetBlock.getUnlocalizedName());
            return false;//fail for block clear check
            }
          }  
        }          
      }
    }
  
  /**
   * check border for leveling / fill
   * while structure fill was below structure Y level
   * border fill is below build Y level (input variable y)
   */
  int borderSize = settings.getBorderSize();
  maxLeveling = settings.getBorderMaxLeveling();
  maxFill = settings.getBorderMaxFill();  
  if(borderSize>0)
    {
    AWLog.logDebug("checking along x-axis. min " +(bb.pos1.x-borderSize)+ " max: "+(bb.pos2.x+borderSize));
    AWLog.logDebug("z-s to check: "+(bb.pos1.z-borderSize)+" to: "+(bb.pos1.z) + "  and   " + (bb.pos2.z) + " to " +(bb.pos2.z+borderSize));
    //check min and max Z along X axis, and corners
    for(bx = bb.pos1.x - borderSize; bx <= bb.pos2.x + borderSize; bx++)
      {   
      
      for(bz = bb.pos1.z-1; bz >= bb.pos1.z - borderSize; bz--)
        {
        if(!checkBorderBlockValidity(world, bx, y, bz, maxLeveling, maxFill, clearBlocks))
          {
          AWLog.logDebug("structure failed validation for border clearing or leveling test");
          return false;
          }//fail border level or clearing depth test        
        }      
      for(bz = bb.pos2.z+1; bz <= bb.pos2.z + borderSize; bz++)
        {
        if(!checkBorderBlockValidity(world, bx, y, bz, maxLeveling, maxFill, clearBlocks))
          {
          AWLog.logDebug("structure failed validation for border clearing or leveling test");
          return false;
          }//fail border level or clearing depth test
        }
      }
    AWLog.logDebug("checking along z-axis");
    //check min+1 and max-1 X along Z axis (already checked corners in previous test)
    for(bz = bb.pos1.z; bz<=bb.pos2.z; bz++)
      {
      for(bx = bb.pos1.x-1; bx >= bb.pos1.x - borderSize; bx--)
        {
        if(!checkBorderBlockValidity(world, bx, y, bz, maxLeveling, maxFill, clearBlocks))
          {
          AWLog.logDebug("structure failed validation for border clearing or leveling test");
          return false;
          }//fail border level or clearing depth test
        }
      for(bx = bb.pos2.x+1; bx <= bb.pos2.x + borderSize; bx++)
        {
        if(!checkBorderBlockValidity(world, bx, y, bz, maxLeveling, maxFill, clearBlocks))
          {
          AWLog.logDebug("structure failed validation for border clearing or leveling test");
          return false;
          }//fail border level or clearing depth test
        }
      }  
    }
  return true;
  }

private boolean checkBorderBlockValidity(World world, int x, int y, int z, int maxLeveling, int maxFill, Set<String> validClearingBlocks)
  {
  int topEmptyBlockY = getTargetY(world, x, z)+1;
  if(topEmptyBlockY<=0){return false;}
  int heightDiff = topEmptyBlockY - y;
  if(maxLeveling>=0 && heightDiff > maxLeveling)
    {
    AWLog.logDebug("failed border leveling "+maxLeveling + " found: "+heightDiff + " at: "+x+","+z + " topBlock: "+topEmptyBlockY);
    return false;
    }//fail borderLeveling
  else if(maxFill>=0 && -heightDiff>maxFill)
    {
    AWLog.logDebug("failed border fill "+maxFill + " found: "+ (-heightDiff) + " at: "+x+","+z + " topBlock: "+topEmptyBlockY);
    return false;
    }//fail border depth test
  int id;
  Block block;
  if(maxLeveling>0)
    {
    for(int by = topEmptyBlockY-1; by>=y; by--)//check border clearing blocks
      {
      id = world.getBlockId(x, by, z);
      block = Block.blocksList[id];
      if(block!=null && !validClearingBlocks.contains(block.getUnlocalizedName()))
        {
        AWLog.logDebug("failed border clearing block test ");
        return false;
        }
      }
    }  
  return true;
  }

private void generateStructureAt(World world, int x, int y, int z, int face, StructureTemplate template, StructureMap map)
  {
  StructureBuilderWorldGen builder = new StructureBuilderWorldGen(world, template, face, x, y, z);
  builder.instantConstruction();
  map.setGeneratedAt(world, x, y, z, face, template);
  map.markDirty();
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
