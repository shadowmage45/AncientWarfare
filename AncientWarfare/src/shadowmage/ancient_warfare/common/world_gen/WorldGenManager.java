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

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.build.BuilderInstant;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
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
  int dim =world.getWorldInfo().getDimension();
  if(dim!=0)
    {
    return;//TODO setup config for other dimensions
    }
  int maxRange = Config.structureGenMaxCheckRange;
  int minRange = Config.structureGenMinDistance;
  float dist = 0;
  int foundValue = 0;
  if(! WorldGenManager.instance().dimensionStructures.containsKey(dim))
    {
    WorldGenManager.instance().dimensionStructures.put(dim, new GeneratedStructureMap());
    }
  Pair<Float, Integer> values =  WorldGenManager.instance().dimensionStructures.get(dim).getClosestStructureDistance(chunkX, chunkZ, maxRange);
  foundValue = values.value();
  if(values.key()==-1)
    {
    dist = maxRange;
    }
  else
    {
    dist = values.key();
    }      
  ProcessedStructure struct = StructureManager.instance().getRandomWeightedStructureBelowValue(random, Config.structureGenMaxClusterValue-foundValue);
  int randCheck = random.nextInt(Config.structureGeneratorRandomRange);
  float valPercent = (float)foundValue / (float) Config.structureGenMaxClusterValue;
  valPercent = 1 - valPercent;
  if(valPercent<.4f)
    {
    valPercent = .4f;
    }
  float randChance = Config.structureGeneratorRandomChance * valPercent;
  if(randCheck > randChance)
    {
    return;
    }  
  if(struct!=null && dist >= minRange  && foundValue + struct.structureValue < Config.structureGenMaxClusterValue)
    {
    int x = chunkX*16 + random.nextInt(16);
    int z = chunkZ*16 + random.nextInt(16);  
    
    int y = getTopBlockHeight(world, x, z, struct.preserveWater, struct.preserveLava, struct.validTargetBlocks);
    if(y==-1)
      {
      Config.logDebug("invalid topBlock");
      return;
      }
    int face = random.nextInt(4);
    BlockPosition hit = new BlockPosition(x,y,z);    
    if(!struct.canGenerateAtSurface(world, hit.copy(), face))
      {
      Config.logDebug("site rejected by structure: "+struct.name);
      return;
      }  
    hit.y++;   
    Config.logDebug("structBB : "+struct.getStructureBB(hit, face));
    BuilderInstant builder = new BuilderInstant(world, struct, face, hit);
    builder.startConstruction();
    WorldGenManager.instance().dimensionStructures.get(dim).setGeneratedAt(chunkX, chunkZ, struct.structureValue, struct.name);
    } 
  }

/**
 * return the topMost solid block, -1 if no valid block is found
 * @param world
 * @param x
 * @param z
 * @return
 */
public int getTopBlockHeight(World world, int x, int z, boolean allowWater, boolean allowLava, int[] allowedTargetBlocks)
  {
  int top = world.provider.getActualHeight();
  for(int i = top; i > 0; i--)
    {
    int id = world.getBlockId(x, i, z);
    if(id!=0 && Block.isNormalCube(id) && id != Block.wood.blockID)
      {      
      if(allowedTargetBlocks !=null)
        {        
        boolean valid = false;
        for(int allowedID : allowedTargetBlocks)
          {
          if(id==allowedID)
            {
            valid = true;
            }
          }
        if(valid)
          {
          int topID = world.getBlockId(x, i+1, z);
          if(topID == Block.waterMoving.blockID || topID == Block.waterStill.blockID)
            {
            if(allowWater)
              {
              
              }
            return -1;
            //TODO get count of water above block...            
            }
          else if(topID == Block.lavaMoving.blockID || topID== Block.lavaStill.blockID)
            {
            if(allowLava)
              {
              
              }
            return -1;
            //TODO count lava blocks...
            }
          else if(!Block.isNormalCube(topID))//else it is some other non-solid block, like snow, grass, leaves
            {
            return i;
            }
          }
        }
      return -1;
      }
    }
  return -1;
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
