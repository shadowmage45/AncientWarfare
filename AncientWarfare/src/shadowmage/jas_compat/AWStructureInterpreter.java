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
package shadowmage.jas_compat;

import jas.api.StructureInterpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.entity.EntityList;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.utils.AWGameData;
import shadowmage.ancient_structures.common.world_gen.StructureEntry;
import shadowmage.ancient_structures.common.world_gen.StructureMap;

public class AWStructureInterpreter implements StructureInterpreter
{

private static final String GROUP_CATEGORY = "a_structure_groups";

private static final String GROUP_SPAWNS_CATEGORY = "b_structure_group_spawn_lists";

/**
 * structure name to group name map.  loaded from user-defined config file.
 */
HashMap<String, String> structureGroupMap = new HashMap<String, String>();

/**
 * map of default spawnListEntries by structure group name.  Default entries are loaded from config file.
 */
HashMap<String, Collection<SpawnListEntry>> structureGroupDefaultSpawnEntries = new HashMap<String, Collection<SpawnListEntry>>(); 

/**
 * a cached list of all structure-group names (aka StructureKeys).
 * Loaded from config file and populated when the structureGroupMap is built.
 */
private List<String> keys = new ArrayList<String>();

public AWStructureInterpreter()
  {
  
  }

@Override
public Collection<String> getStructureKeys()
  {
  return keys;
  }

@Override
public Collection<SpawnListEntry> getStructureSpawnList(String structureKey)
  {
  if(structureGroupDefaultSpawnEntries.containsKey(structureKey))
    {
    return structureGroupDefaultSpawnEntries.get(structureKey);
    }
  return Collections.emptyList();
  }

@Override
public String areCoordsStructure(World world, int xCoord, int yCoord, int zCoord)
  {
  StructureMap map = AWGameData.get(world, StructureMap.name, StructureMap.class);
  List<StructureEntry> entries = new ArrayList<StructureEntry>(); 
  map.getEntriesNear(world, xCoord, zCoord, 1, true, entries);
  
  String structureName = null;
  String structureGroup = null;
  for(StructureEntry entry : entries)
    {
    if(entry.getBB().isPositionInBoundingBox(xCoord, yCoord, zCoord))
      {
      structureName = entry.getName();
      break;
      }
    }  
  if(structureName==null)
    {
    return null;
    }
  structureGroup = getStructureGroup(structureName);
  return (structureGroup==null || structureGroup.isEmpty()) ? null : structureGroup;
  }

@Override
public boolean shouldUseHandler(World world, BiomeGenBase biomeGenBase)
  {
  return true;
  }

/**
 * given a structure name, should return the structure group that that structure belongs to.
 * structure groups are user-defined and loaded from config file.
 * @param structureName
 * @return
 */
private String getStructureGroup(String structureName)
  {
  return structureGroupMap.get(structureName);
  }

/**
 * loads structure groups and spawn lists from the passed in config file.
 * @param config
 */
public void loadStructureGroups(Configuration config)
  {
  config.addCustomCategoryComment(GROUP_CATEGORY, "Each entry defines a structure group.\n" +
  		"The key is the group name, and the values are the structures that belong to that group.\n" +
  		"Custom groups may be added, and will be parsed / added to JAS structure-spawning list(s).\n" +
  		"Each structure may only be specified for a single group -- multiple assignments will result in\n" +
  		"undefined behavior (one will overwrite another, no guarantee about ordering)\n"+
  		"Structure group names should be defined in lower case.  Structure names should use the exact name\n" +
  		"of the template, including capitalization.\n"+
  		"An couple of example structure groups are:\n" +
  		"S:example_group <\n" +
  		"    logCabin\n" +
  		"    fortress1\n" +
  		" >\n" +
  		"S:example_group_2 <\n" +
  		"    fortress2\n" +
  		"    towerLarge\n" +
  		"    \"Large Crypt\"\n" +
  		" >\n");
  
  String[] testGroup = new String[]{"logCabin2","fortress1"};
  config.get(GROUP_CATEGORY, "teststructuregroup", testGroup);
  
  ConfigCategory category = config.getCategory(GROUP_CATEGORY);
  
  Property property;
  Set<Entry<String, Property>> entrySet = category.entrySet();

  String group;
  String[] structures;
  
  for(Entry<String, Property> entry : entrySet)
    {
    group = entry.getKey();
    structures = entry.getValue().getStringList();
    for(String name : structures)
      {
      structureGroupMap.put(name, group);
      }
    keys.add(group);
    }  
  
  
  config.addCustomCategoryComment(GROUP_SPAWNS_CATEGORY, "Structure Group Default Spawn settings.\n" +
  		"There is one sub-category per structure group.\n" +
  		"Keys for the sub-categories are the entity names.\n" +
  		"Values are the weight/spawn settings for that entity.\n" +
  		"Entries follow use the MC convention for definitions:\n" +
      "(Name=weight-min-max)\n" +
  		"If no entry is found, it will be defaulted to Zombie, 4-4-4-4.\n" +
  		"These lists can further be customized in the JAS per-world settings\n" +
  		"located in the file StructureSpawns.cfg.\n\n" +
  		"Example groups:\n" +
  		"  example_group {\n"+
      "      S:Zombie=8-4-4-4\n"+
      "      S:Creeper=2-4-4-4\n"+
      "  }\n\n" +
      "  example_group_2 {\n" +
      "      S:Skeleton=10-4-4-4\n" +
      "      S:PigZombie=2-4-4-4\n"+
      "  }\n");
  
  category = config.getCategory(GROUP_SPAWNS_CATEGORY);
    
  Set<ConfigCategory> children = category.getChildren();
  
  List<SpawnListEntry> entryList;
  SpawnListEntry spawnEntry;
  String name;
  Class clz;
  String settings;
  String[] splitSettings;
  
  Configuration dummyConfig;
  for(ConfigCategory child : children)
    {        
    entryList = new ArrayList<SpawnListEntry>();
    entrySet = child.entrySet();
    group = child.getQualifiedName().replace(GROUP_SPAWNS_CATEGORY+".", "");
    for(Entry<String, Property> entry : entrySet)
      {
      name = entry.getKey();
      clz = (Class) EntityList.stringToClassMapping.get(name);
      if(clz==null)
        {
        AWLog.logError("could not locate entity class for: "+name+" while loading entity group for:"+group+". Please verify the entity name and fix the config file.");
        continue;
        }
      settings = entry.getValue().getString();
      splitSettings = settings.split("-", -1);
      if(splitSettings==null || splitSettings.length<3)
        {
        AWLog.logError("Entity spawn settings length is <3!! Entity : "+name+" Settings: "+settings+".  Please verify this entry and fix the error in order to load this entity setting.");
        continue;
        }
      spawnEntry = new SpawnListEntry(clz, Integer.parseInt(splitSettings[0]), Integer.parseInt(splitSettings[1]), Integer.parseInt(splitSettings[2]));
      entryList.add(spawnEntry);
      }    
    if(!entryList.isEmpty())
      {
      structureGroupDefaultSpawnEntries.put(group, entryList);      
      }
    AWLog.log("Loaded entity list for structure group: "+group+" of : "+entryList);
    }    
  
  
  config.save();
  }

}
