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

import net.minecraft.world.biome.BiomeGenBase;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;

public class WorldGenStructureManager
{


/**
 * <biomename, <structName, structEntry>>
 */
private HashMap<String, WorldGenBiomeStructList> biomesStructureMap = new HashMap<String, WorldGenBiomeStructList>();

private class WorldGenBiomeStructList
{
int totalWeight;//weight for this entire biome, used for structure selection
Map<Integer, Integer> valueWeights = new HashMap<Integer, Integer>();//total weights for each value, used when selecting a struct below a certain value threshold
Map<String, WorldGenStructureEntry> structureEntries = new HashMap<String, WorldGenStructureEntry>();

/**
 * adds an entry to this list <internal map>, also adds weight to total bin weight.
 * adds weight to value/weight bins.
 * @param entry
 */
public void addEntry(WorldGenStructureEntry entry)
  {
  if(!structureEntries.containsKey(entry.name))
    {
    structureEntries.put(entry.name, entry);
    }
  else
    {
    return;
    }
  totalWeight += entry.weight;
  if(!valueWeights.containsKey(entry.value))
    {
    valueWeights.put(entry.value, 0);
    }
  int totVal = valueWeights.get(entry.value);
  valueWeights.put(entry.value, totVal+entry.weight);
  }

public String getRandomWeightedEntry()
  {
  //TODO
  return "";
  }

public String getRandomWeightedEntryBelow(int value)
  {
  //TODO
  return "";
  }

}

private WorldGenStructureManager(){};
private static WorldGenStructureManager INSTANCE;
public static WorldGenStructureManager instance()  
  {
  if(INSTANCE==null)
    {
    INSTANCE = new WorldGenStructureManager();
    }
  return INSTANCE;
  }

/**
 * blind load method
 */
public void load()
  {
  this.loadBiomesList();
  }

/**
 * adds a biome entry for every biome in the biomesList, this method should be called before loading structures,
 * to ensure that a biome entry is available before world-gen structures are attempted to add to it (will not add to invalid
 * biomes)
 */
private void loadBiomesList()
  {
  this.biomesStructureMap.clear();
  for(BiomeGenBase bio : BiomeGenBase.biomeList)
    {
    if(bio!=null && bio.biomeName!= null && !bio.biomeName.equals(""))
      {
      this.biomesStructureMap.put(String.valueOf(bio.biomeName), new WorldGenBiomeStructList());
      }
    }
  }

public void addStructure(ProcessedStructure struct, boolean unique, int weight, int value)
  {
  if(struct==null)
    {
    return;
    }
  WorldGenStructureEntry ent = new WorldGenStructureEntry(struct.name, unique, weight, value);  
  if(struct.biomesNotIn!=null)
    {
    this.addToAllButBiomes(ent, struct.biomesNotIn);
    }
  else if(struct.biomesOnlyIn!=null)
    {
    this.addToOnlyBiomes(ent, struct.biomesOnlyIn);
    }
  else
    {
    this.addToAllBiomes(ent);
    }
  }

private void addToAllBiomes(WorldGenStructureEntry ent)
  {
  for(String bio : this.biomesStructureMap.keySet())
    {
    WorldGenBiomeStructList lst = this.biomesStructureMap.get(bio);
    lst.addEntry(ent);
    }
  }

private void addToOnlyBiomes(WorldGenStructureEntry ent, String[] only)
  {
  for(String k : this.biomesStructureMap.keySet())
    {
    for(String on : only)
      {
      if(on.equals(k))
        {
        this.biomesStructureMap.get(k).addEntry(ent);
        }
      }
    }  
  }

private void addToAllButBiomes(WorldGenStructureEntry ent, String[] notIn)
  {
  for(String k : this.biomesStructureMap.keySet())
    {
    boolean found = false;
    for(String st : notIn)
      {
      if(st.equals(k))
        {
        found = true;
        break;
        }
      }
    if(!found)
      {
      this.biomesStructureMap.get(k).addEntry(ent);
      }
    }
  }


}
