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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import net.minecraft.world.biome.BiomeGenBase;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.utils.StringTools;

import com.google.common.io.ByteStreams;

public class WorldGenStructureManager
{


/**
 * <biomename, <structName, structEntry>>
 */
private HashMap<String, WorldGenBiomeStructList> biomesStructureMap = new HashMap<String, WorldGenBiomeStructList>();

/**
 * config values for world gen global settings
 */
private int[] validDimensions;
private int[] invalidDimensions;
public static int structureGenMinDistance=2;
public static int structureGenMaxCheckRange=16;
public static int structureGenRandomChance=10;
public static int structureGenRandomRange=1000;
public static int structureGenMaxClusterValue=50;

/**
 * name-map of loaded world-gen structure settings...
 */
private HashMap<String, WorldGenStructureEntry> namesStructureMap = new HashMap<String, WorldGenStructureEntry>();

private static File configFile;

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
    Config.logError("Attempt to register duplicate structure name for world-generation. name: "+entry.name);
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

/**
 * actually gets a structure equal to or less than maxValue, so it should get zero-valued structures still
 * @param maxValue
 * @param random
 * @return
 */
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
      if(value>=ent.weight)
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

public WorldGenStructureEntry getEntryFor(String name)
  {
  return this.namesStructureMap.get(name);
  }

public int getValueFor(String name)
  {
  if(this.namesStructureMap.containsKey(name))
    {
    return this.namesStructureMap.get(name).value;
    }
  return 0;
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
//      Config.logDebug("Adding to biome list: "+bio.biomeName);
      this.biomesStructureMap.put(String.valueOf(bio.biomeName.toLowerCase()), new WorldGenBiomeStructList());
      }
    }
  }

/**
 * adds a world-gen entry from a processed structure (used by structure scanner exporter)
 * calls saveConfig, to ensure exported settings are re-loaded upon game restart... 
 * @param struct
 * @param weight
 * @param value
 */
public void addEntry(ProcessedStructure struct, int weight, int value, boolean unique)
  {
  String name = struct.name;
  WorldGenStructureEntry entry = new WorldGenStructureEntry(name, unique, weight, value, -1, -1, -1, -1, null, null, -1);
  this.addStructureEntry(entry);
  this.saveConfig();
  }

private void addStructureEntry(WorldGenStructureEntry ent)
  {
  if(ent==null)
    {
    return;
    }
//  Config.logDebug("Attempting to add world gen entry for structure: "+ent.name);
  ProcessedStructure struct = StructureManager.instance().getStructureServer(ent.name);
  String[] biomesNotIn = null;
  String[] biomesOnlyIn = null;
  if(ent.hasBiomeOverride())
    {
    biomesNotIn = ent.biomesNot;
    biomesOnlyIn = ent.biomesOnly;
    }
  else
    {
    biomesNotIn = struct.biomesNotIn;
    biomesOnlyIn = struct.biomesOnlyIn;
    }
  if(struct!=null)
    {
    if(biomesNotIn!=null && biomesOnlyIn!=null)
      {
      Config.logError("Error detected in a structure template:  it has both exclusive and inclusive biome lists.  Please use only one or the other.  Structure name: "+struct.name);
      return;
      }    
    if(biomesNotIn!=null)
      {
      addToAllButBiomes(ent, biomesNotIn);
      }
    else if(biomesOnlyIn!=null)
      {
      addToOnlyBiomes(ent, biomesOnlyIn);
      }
    else
      {
      addToAllBiomes(ent);
      }
    if(!this.namesStructureMap.containsKey(ent.name))
      {
      this.namesStructureMap.put(ent.name, ent);
      }
    struct.setWorldGenEntry(ent);
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
      if(on.equalsIgnoreCase(k))
        {
        if(this.biomesStructureMap.containsKey(k))
          {
//          Config.logDebug("Adding: "+ent.name+"  to biome: "+k);
          this.biomesStructureMap.get(k).addEntry(ent);
          }
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
      if(st.equalsIgnoreCase(k))
        {
        found = true;
        break;
        }
      }
    if(!found && this.biomesStructureMap.containsKey(k))
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

/**
 * load config from file, or export default if none exists
 * @param pathName
 */
public void loadConfig(String pathName)
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
    this.configFile = configFile;
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

/**
 * attempts to save current in-game config out to disk, using currently set config file
 * fails if current file is null (means original load failed...you've got bigger problems)
 */
public void saveConfig()
  {
  if(this.configFile==null)
    {
    Config.logError("Null file reference when attempting to save world-gen config to disk");
    return;
    }
  try
    {
    FileWriter writer = new FileWriter(this.configFile);
    List<String> lines = this.getConfigLines();
    for(String line : lines)
      {
      writer.write(line+"\n");      
      }
    writer.write("\n");
    
    for(String name : this.namesStructureMap.keySet())
      {
      WorldGenStructureEntry ent = this.namesStructureMap.get(name);
      writer.write("\n");      
      lines = ent.getEntryLines();
      for(String line : lines)
        {
        writer.write(line+"\n");      
        }
      }    
    writer.close();
    }
  catch(IOException e)
    {
    Config.logError("Error while attempting to save world-gen config to disk");
    }
  }

private List<String> getConfigLines()
  {
  ArrayList<String> lines = new ArrayList<String>();
  lines.add("config:");  
  if(this.invalidDimensions!=null)
    {
    lines.add("invalidDimensions="+StringTools.getCSVStringForArray(invalidDimensions));
    }
  else if(this.validDimensions!=null)
    {
    lines.add("validDimensions="+StringTools.getCSVStringForArray(validDimensions));
    }
  lines.add(":endconfig");
  return lines;
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
    if(line.toLowerCase().startsWith("validdimensions"))
      {
      this.validDimensions = StringTools.safeParseIntArray("=", line);
      }
    if(line.toLowerCase().startsWith("invaliddimensions"))
      {
      this.invalidDimensions = StringTools.safeParseIntArray("=", line);
      }  
    if(line.toLowerCase().startsWith("structuregenmindistance"))
      {
      this.structureGenMinDistance = StringTools.safeParseInt("=", line);
      }
    if(line.toLowerCase().startsWith("structuregenmaxcheckrange"))
      {
      this.structureGenMaxCheckRange = StringTools.safeParseInt("=", line);
      }
    if(line.toLowerCase().startsWith("structuregenrandomchance"))
      {
      this.structureGenRandomChance = StringTools.safeParseInt("=", line);
      }
    if(line.toLowerCase().startsWith("structuregenrandomrange"))
      {
      this.structureGenRandomRange = StringTools.safeParseInt("=", line);
      }
    if(line.toLowerCase().startsWith("structuregenmaxclustervalue"))
      {
      this.structureGenMaxClusterValue = StringTools.safeParseInt("=", line);
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
  int mC = -1;
  int cB = -1;
  int mL = -1;
  int lB = -1;
  int ov = -1;
  String[] bO = null;
  String[] bN = null;
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
    if(line.toLowerCase().startsWith("maxverticalclear"))
      {
      mC = StringTools.safeParseInt("=", line);
      }
    if(line.toLowerCase().startsWith("clearingbuffer"))
      {
      cB = StringTools.safeParseInt("=", line);
      }
    if(line.toLowerCase().startsWith("maxleveling"))
      {
      mL = StringTools.safeParseInt("=", line);
      }
    if(line.toLowerCase().startsWith("levelingbuffer"))
      {
      lB = StringTools.safeParseInt("=", line);
      }
    if(line.toLowerCase().startsWith("biomesnotin"))
      {
      bN = StringTools.safeParseStringArray("=", line);
      }
    if(line.toLowerCase().startsWith("biomesonlyin"))
      {
      bO = StringTools.safeParseStringArray("=", line);
      }   
    if(line.toLowerCase().startsWith("overhang"))
      {
      ov = StringTools.safeParseInt("=", line);
      }
    }
  if(name.equals("") || weight == 0 || value == -1)
    {
    Config.logError("Improperly formatted structure in world gen config file");
    return null;
    }
  if(mC >=0 && cB<0)
    {
    cB = 0;
    }
  if(mL >=0 && lB < 0)
    {
    lB = 0;
    }
  WorldGenStructureEntry ent = new WorldGenStructureEntry(name, unique, weight, value, mC, cB, mL, lB, bO, bN, ov);
  return ent;
  }

/**
 * attempt to find a world-gen structure valid for the given biome and maxValue, null if none
 * @param biomeName
 * @param maxValue
 * @param random
 * @return
 */
public ProcessedStructure getStructureForBiome(String biomeName, int maxValue, Random random)
  {
  if(this.biomesStructureMap.containsKey(biomeName.toLowerCase()))
    {
    String name = this.biomesStructureMap.get(biomeName.toLowerCase()).getRandomWeightedEntryBelow(maxValue, random);        
    return StructureManager.instance().getStructureServer(name);
    }
  return null;
  }

}
