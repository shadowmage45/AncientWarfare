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
package shadowmage.ancient_structures.common.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import shadowmage.ancient_framework.common.gamedata.AWGameData;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureValidationSettingsDefault;
import shadowmage.ancient_structures.common.world_gen.StructureEntry;
import shadowmage.ancient_structures.common.world_gen.StructureMap;

public class WorldGenStructureManager
{

private HashMap<String, Set<StructureTemplate>> templatesByBiome = new HashMap<String, Set<StructureTemplate>>();

private static WorldGenStructureManager instance = new WorldGenStructureManager();
private WorldGenStructureManager(){}
public static WorldGenStructureManager instance(){return instance;}

public void loadBiomeList()
  {
  BiomeGenBase biome;
  for(int i = 0; i < BiomeGenBase.biomeList.length; i++)
    {
    biome = BiomeGenBase.biomeList[i];
    if(biome==null){continue;}
    templatesByBiome.put(biome.biomeName, new HashSet<StructureTemplate>());
    }
  }

public void registerWorldGenStructure(StructureTemplate template)
  {
  StructureValidationSettingsDefault validation = template.getValidationSettings();
  Set<String> biomes = validation.getBiomeList();
  if(validation.isBiomeWhiteList())
    {    
    for(String biome : biomes)
      {
      if(templatesByBiome.containsKey(biome))
        {
        templatesByBiome.get(biome).add(template);
        }      
      }
    }
  else//blacklist, skip template-biomes
    {
    for(String biome : templatesByBiome.keySet())
      {
      if(biomes.contains(biome)){continue;}
      templatesByBiome.get(biome).add(template);
      }
    }
  }

/**
 * cached list objects, used for temp searching, as to not allocate new lists for every chunk-generated....
 */
List<StructureEntry> searchCache = new ArrayList<StructureEntry>();
List<StructureTemplate> trimmedPotentialStructures = new ArrayList<StructureTemplate>();
HashMap<String, Integer> distancesFound = new HashMap<String, Integer>();

public StructureTemplate selectTemplateForGeneration(World world, Random rng, int x, int y, int z, int chunkSearchRange)
  {
  StructureMap map = AWGameData.get(world, "AWStructureMap", StructureMap.class);
  if(map==null){return null;}
  int cx, cz, foundValue, chunkDistance;
  float foundDistance, mx, mz;
  cx = x << 4;
  cz = z << 4;
  String biomeName = world.getBiomeGenForCoords(x, z).biomeName;
  Collection<StructureEntry> genEntries = map.getEntriesNear(world, x, z, chunkSearchRange, false, searchCache);
  
  foundValue = 0;
  for(StructureEntry entry : genEntries)
    {
    foundValue += entry.getValue();
    mx = ((entry.getBB().pos2.x - entry.getBB().pos1.x)/2 + entry.getBB().pos1.x) - x;
    mz = ((entry.getBB().pos2.z - entry.getBB().pos1.z)/2 + entry.getBB().pos1.z) - z;
    foundDistance = MathHelper.sqrt_float(mx * mx + mz * mz);
    chunkDistance = ((int)foundDistance)/16;
    if(distancesFound.containsKey(entry.getName()))
      {
      int dist = distancesFound.get(entry.getName());
      if(chunkDistance<dist)
        {
        distancesFound.put(entry.getName(), chunkDistance);
        }
      }
    else
      {
      distancesFound.put(entry.getName(), chunkDistance);
      }
    }
  int remainingValue = AWStructureStatics.maxClusterValue - foundValue;
  Collection<String> generatedUniques = map.getGeneratedUniques();
  Set<StructureTemplate> potentialStructures = templatesByBiome.get(biomeName);
  StructureValidationSettingsDefault settings;
  int dim = world.provider.dimensionId;
  for(StructureTemplate template : potentialStructures)//loop through initial structures, only adding to 2nd list those which meet biome, unique, value, and minDuplicate distance settings
    {
    settings = template.getValidationSettings();
    boolean dimensionFound = false;
    for(int i = 0; i < settings.getAcceptedDimensions().length; i++)
      {
      int dimTest = settings.getAcceptedDimensions()[i];
      if(dimTest == dim)
        {
        dimensionFound = true; 
        break;
        }      
      }
    if(!dimensionFound || (dimensionFound && !settings.isDimensionWhiteList()))//skip if dimension is blacklisted, or not present on whitelist
      {
      continue;
      }
    if(generatedUniques.contains(template.name)){continue;}//skip already generated uniques
    if(settings.getClusterValue()>remainingValue){continue;}//skip if cluster value is to high to place in given area
    if(distancesFound.containsKey(template.name))
      {
      int dist = distancesFound.get(template.name);
      if(dist<settings.getMinDuplicateDistance()){continue;}//skip if minDuplicate distance is not met
      }
    trimmedPotentialStructures.add(template);
    }  
  
  int totalWeight = 0;
  for(StructureTemplate t : trimmedPotentialStructures)
    {
    totalWeight += t.getValidationSettings().getSelectionWeight();
    }
  totalWeight -= totalWeight > 0 ? rng.nextInt(totalWeight) : 0;
  StructureTemplate toReturn = null;
  for(StructureTemplate t : trimmedPotentialStructures)
    {
    totalWeight -= t.getValidationSettings().getSelectionWeight();
    if(totalWeight<=0)
      {
      toReturn = t;
      break;
      }
    }
  distancesFound.clear();
  trimmedPotentialStructures.clear();
  return toReturn;
  }

}
