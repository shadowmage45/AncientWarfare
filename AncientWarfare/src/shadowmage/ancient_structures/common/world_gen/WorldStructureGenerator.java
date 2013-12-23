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

import java.util.LinkedList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.gamedata.AWGameData;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.manager.StructureTemplateManager;
import shadowmage.ancient_structures.common.manager.WorldGenStructureManager;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldStructureGenerator implements IWorldGenerator
{

private boolean isGenerating = false;
private LinkedList<DelayedGenerationEntry> delayedChunks = new LinkedList<DelayedGenerationEntry>();
private Random rng = new Random();

@Override
public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
  {
  if(isGenerating)
    {
    delayedChunks.add(new DelayedGenerationEntry(chunkX, chunkZ, world, chunkGenerator, chunkProvider));
    return;
    }
  else
    {
    isGenerating = true;
    generateAt(chunkX, chunkZ, world, chunkGenerator, chunkProvider);
    }
  isGenerating = false;
  while(!delayedChunks.isEmpty())
    {    
    DelayedGenerationEntry entry = delayedChunks.poll();
    generateAt(entry.chunkX, entry.chunkZ, entry.world, entry.generator, entry.provider);
    }
  }

private void generateAt(int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
  {
  long seed = (((long)chunkX)<< 32) | (((long)chunkZ) & 0xffffffffl);
  rng.setSeed(seed);
  int x = chunkX*16 + rng.nextInt(16);
  int y = world.getTopSolidOrLiquidBlock(chunkX, chunkZ);
  int z = chunkZ*16 + rng.nextInt(16);
  int face = rng.nextInt(4);
  StructureTemplate template = WorldGenStructureManager.instance().selectTemplateForGeneration(world, rng, x, y, z, AWStructureStatics.chunkSearchRadius);
  if(template==null){return;}    
  if(attemptStructureGenerationAt(world, x, y, z, face, template))
    {
    StructureMap generatedStructures = AWGameData.get(world, "AWStructureMap", StructureMap.class);
    generatedStructures.setGeneratedAt(world, x, y, z, face, template);
    generatedStructures.markDirty();
    }   
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
public boolean attemptStructureGenerationAt(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  if(validateStructurePlacement(world, x, y, z, face, template))
    {   
    return false;
    }
  generateStructureAt(world, x, y, z, face, template);
  return true;
  }

private boolean validateStructurePlacement(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  /**
   * TODO
   * check leveling
   * check missing edge / filling
   * check border leveling
   * check border missing edge / filling
   * check for intersecting bounding-boxes 
   */
  return false;
  }

private void generateStructureAt(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  /**
   * TODO instantiate builder, build. do world gen clearing and fill during build
   */

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
