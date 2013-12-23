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

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.gamedata.AWGameData;
import shadowmage.ancient_structures.common.manager.StructureTemplateManager;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldStructureGenerator implements IWorldGenerator
{

public static int chunkSearchRadius = 16;

@Override
public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
  {
  int x = chunkX*16 + random.nextInt(16);
  int y = world.getTopSolidOrLiquidBlock(chunkX, chunkZ);
  int z = chunkZ*16 + random.nextInt(16);
  int face = random.nextInt(4);
  String biomeName = world.getBiomeGenForCoordsBody(x, z).biomeName;
  AWLog.logDebug("found possible gen pos: "+x+","+y+","+z+" biome name: "+biomeName);
  StructureTemplate template = StructureTemplateManager.instance().getTemplate("wgt1");
  StructureMap generatedStructures = AWGameData.get(world, "AWStructureMap", StructureMap.class);
  generatedStructures.setGeneratedAt(world, x, y, z, face, template);
  generatedStructures.markDirty();
  }



}
