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
package shadowmage.ancient_warfare.common.world_gen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.utils.Pair;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenManager implements IWorldGenerator, INBTTaggable
{

//TODO change back to private...
public static Map<Integer, GeneratedStructureMap> dimensionStructures = new HashMap<Integer, GeneratedStructureMap>();

private WorldGenManager(){};
private static WorldGenManager INSTANCE;
public static WorldGenManager instance()  
  {
  if(INSTANCE==null)
    {
    INSTANCE = new WorldGenManager();
    }
  return INSTANCE;
  }

@Override
public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
  {
  int dim = world.getWorldInfo().getDimension();
  int maxRange = 50;
  if(!dimensionStructures.containsKey(dim))
    {
    dimensionStructures.put(dim, new GeneratedStructureMap());
    }
  Pair<Float, Integer> values =dimensionStructures.get(dim).getClosestStructureDistance(chunkX, chunkZ, maxRange); 
  float dist = values.key();
  int foundValue = values.value();
  if(dist==-1)
    {
    dist = maxRange;
    }
  ProcessedStructure struct = StructureManager.instance().getStructureForGenDistance((int)dist,  random);  
  if(struct==null)
    {
    return;
    }
  
  int x = chunkX + random.nextInt(16);
  int z = chunkZ + random.nextInt(16);  
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  //TODO save generated struct map
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  //TODO load gen structure map  
  }



}
