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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.google.common.io.ByteStreams;

import net.minecraft.world.biome.BiomeGenBase;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class WorldGenStructureManager
{


/**
 * <biomename, <structName, structEntry>>
 */
private HashMap<String, WorldGenBiomeStructList> biomesStructureMap = new HashMap<String, WorldGenBiomeStructList>();

private int[] validDimensions;
private int[] invalidDimensions;

private class WorldGenBiomeStructList
{
int totalWeight;//weight for this entire biome, used for structure selection
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
  }

public String getRandomWeightedEntry(Random random)
  {
  int value = random.nextInt(totalWeight);
  for(String name : this.structureEntries.keySet())
    {
    WorldGenStructureEntry ent = this.structureEntries.get(name);
    if(value>ent.weight)
      {
      value-=ent.weight;
      }
    else
      {
      return ent.name;
      }
    }  
  return "";
  }

public String getRandomWeightedEntryBelow(int maxValue, Random random)
  {
  int foundTotalWeight = 0;
  for(String name : this.structureEntries.keySet())
    {
    WorldGenStructureEntry ent = this.structureEntries.get(name);
    if(ent.value<=maxValue)
      {
      foundTotalWeight += ent.weight;
      }    
    }  
  if(foundTotalWeight==0)
    {
    return "";
    }  
  int value = random.nextInt(foundTotalWeight);
  for(String name : this.structureEntries.keySet())
    {
    WorldGenStructureEntry ent = this.structureEntries.get(name);
    if(ent.value<=maxValue)
      {
      if(value>ent.weight)
        {
        value-=ent.weight;
        }
      else
        {
        return ent.name;
        }
      }
    }  
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

public boolean isValidDimension(int dim)
  {
  if(this.validDimensions==null && this.invalidDimensions==null)
    {
    return true;
    }
  else if(this.validDimensions!=null)
    {
    for(int i = 0; i < validDimensions.length; i ++)
      {
      if(dim == validDimensions[i])
        {
        return true;
        }
      }
    }
  else if(this.invalidDimensions!=null)
    {
    for(int i = 0; i < invalidDimensions.length; i ++)
      {
      if(dim == invalidDimensions[i])
        {
        return false;
        }
      }
    }
  return false;
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
      Config.logDebug("Adding to biome list: "+bio.biomeName);
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

private void addStructureEntry(WorldGenStructureEntry ent)
  {
  if(ent==null)
    {
    return;
    }
  Config.logDebug("Attempting to add world gen entry for structure: "+ent.name);
  ProcessedStructure struct = StructureManager.instance().getStructureServer(ent.name);
  if(struct!=null)
    {
    if(struct.biomesNotIn!=null && struct.biomesOnlyIn!=null)
      {
      Config.logError("Error detected in a structure template:  it has both exclusive and inclusive biome lists.  Please use only one or the other.  Structure name: "+struct.name);
      return;
      }
    if(struct.biomesNotIn!=null)
      {
      addToAllButBiomes(ent, struct.biomesNotIn);
      }
    else if(struct.biomesOnlyIn!=null)
      {
      addToOnlyBiomes(ent, struct.biomesOnlyIn);
      }
    else
      {
      addToAllBiomes(ent);
      }
    }
  else
    {
    Config.logDebug("Null structure returned when trying to add world gen entry for structure: "+ent.name);
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

/**
 * 
 * @param fileName the fully qualified file name destination, including path (source is pulled statically)
 */
private void copyDefaultFile(String fileName)
  {
  if(fileName==null)
    {
    return;
    } 
  InputStream is = null;
  FileOutputStream os = null;
  File file = null;
  Config.log("Exporting default AWWorldGen.cfg ....");
  int exportCount = 0;
  byte[] byteBuffer;
  try
    {
    is = this.getClass().getResourceAsStream("/shadowmage/ancient_warfare/resources/config/AWWorldGen.cfg");
    if(is==null)
      {
      return;
      }
    
    file = new File(fileName);

    if(!file.exists())
      {
      file.createNewFile();
      }   

    byteBuffer = ByteStreams.toByteArray(is);
    is.close();
    if(byteBuffer.length>0)
      {
      os = new FileOutputStream(file);        
      os.write(byteBuffer);
      os.close();
      }
    }
  catch(Exception e)
    {
    Config.logError("Error during export of: "+fileName);
    e.printStackTrace();
    }  
  Config.log("Exported default file: AWConfig.cfg");  
  }

public void loadFromDirectory(String pathName)
  {
  try
    {
    String fileName = pathName + "AWWorldGen.cfg";
    File configFile = new File(fileName);
    if(!configFile.exists())
      {
      copyDefaultFile(fileName);
      Config.logDebug("AWWorldGen.cfg could not be located, creating default file.");
      return;
      }
    FileInputStream fis = new FileInputStream(configFile);
           
    Scanner scan = new Scanner(fis);
    List<String> lines= new ArrayList<String>();
    String line;
    while(scan.hasNext())
      {
      line = scan.next();
      if(!line.startsWith("#"))
        {
        lines.add(line);
        }
      }
    scan.close();
    fis.close();
    
    Iterator<String> it= lines.iterator();
    while(it.hasNext())
      {
      line = it.next();
      if(line.toLowerCase().startsWith("entry:"))
        {
        WorldGenStructureEntry ent = parseEntry(it);
        addStructureEntry(ent);
        }
      if(line.toLowerCase().startsWith("config:"))
        {
        parseConfig(it);
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
  }


private void parseConfig(Iterator<String> it)
  {
  String line;
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("config:"))
      {
      continue;
      }
    if(line.toLowerCase().startsWith(":endconfig"))
      {
      break;
      }
    if(line.toLowerCase().startsWith("validDimensions"))
      {
      this.validDimensions = StringTools.safeParseIntArray("=", line);
      }
    if(line.toLowerCase().startsWith("invalidDimensions"))
      {
      this.invalidDimensions = StringTools.safeParseIntArray("=", line);
      }    
    }
  if(this.validDimensions!=null && this.invalidDimensions !=null)
    {
    Config.logError("Invalid World Gen configuration detected.  Entry detected for both valid and invalid dimensions.  You may specify one OR the other, but may not specify both.");
    }
  }

private WorldGenStructureEntry parseEntry(Iterator<String> it)
  {
  String line;  
  String name = "";
  int weight = 0;
  int value = -1;
  boolean unique = false;
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("entry:"))
      {
      continue;
      }
    if(line.toLowerCase().startsWith(":endentry"))
      {
      break;
      }
    if(line.toLowerCase().startsWith("name"))
      {
      name = StringTools.safeParseString("=", line);
      }
    if(line.toLowerCase().startsWith("weight"))
      {
      weight = StringTools.safeParseInt("=", line);
      }
    if(line.toLowerCase().startsWith("value"))
      {
      value = StringTools.safeParseInt("=", line);
      }
    if(line.toLowerCase().startsWith("unique"))
      {
      unique = StringTools.safeParseBoolean("=", line);
      }
    }
  if(name.equals("") || weight == 0 || value == -1)
    {
    Config.logError("Improperly formatted structure in world gen config file");
    return null;
    }
  WorldGenStructureEntry ent = new WorldGenStructureEntry(name, unique, weight, value);
  return ent;
  }


public ProcessedStructure getStructureForBiome(String biomeName, int maxValue, Random random)
  {
  if(this.biomesStructureMap.containsKey(biomeName))
    {
    String name = this.biomesStructureMap.get(biomeName).getRandomWeightedEntryBelow(maxValue, random);        
    return StructureManager.instance().getStructureServer(name);
    }
  return null;
  }

}
