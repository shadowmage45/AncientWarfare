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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import net.minecraft.world.biome.BiomeGenBase;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.manager.StructureManager;
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

public void loadFromDirectory(String pathName)
  {
  try
    {
    String fileName = pathName + "worldGenConfig.cfg";
    File configFile = new File(fileName);
    if(!configFile.exists())
      {
      configFile.createNewFile();
      Config.logDebug("worldGenConfig.cfg could not be located, created empty file");
      return;
      }
    FileInputStream fis = new FileInputStream(configFile);
    List<WorldGenStructureEntry> lst = new ArrayList<WorldGenStructureEntry>();       
    Scanner scan = new Scanner(fis);    
    String line;
    while(scan.hasNext())
      {
      line = scan.next();
      if(!line.startsWith("#"))
        {
        WorldGenStructureEntry ent = new WorldGenStructureEntry(line);
        lst.add(ent);
        }
      }
    scan.close();
    fis.close();
    
    for(WorldGenStructureEntry ent : lst)
      {
      ProcessedStructure struct = StructureManager.instance().getStructureServer(ent.name);
      if(struct!=null)        
        {
        //TODO......meh..
        StructureManager.instance().addStructureToWorldGen(struct, ent.value, ent.weight);
        }
      }
    
    }
  catch(IOException e)
    {
    Config.logError("Error while loading world_gen configuration file, no structures will be generated");
    e.printStackTrace();
    }
  catch(NumberFormatException e)
    {
    Config.logError("Improperly formatted world gen config file, could not parse a number value");
    e.printStackTrace();
    }
  catch(IndexOutOfBoundsException e)
    {
    Config.logError("Improperly formatted world gen config file, an entry was missing one or more csv values\nthe format is<name>,<unique>,<weight>,<value> f");
    e.printStackTrace();
    }
  }

private WorldGenStructureEntry parseEntry(Iterator<String> it)
  {
  //TODO...figure out entry format...
  /**
   * config:
   * dimensionsOnlyIn=
   * dimensionsNotIn=
   * endConfig:
   */
  /**
   * entry:
   * name=structName
   * unique=false
   * weight=1
   * value=0
   * endentry:
   */
  return null;
  }


}
