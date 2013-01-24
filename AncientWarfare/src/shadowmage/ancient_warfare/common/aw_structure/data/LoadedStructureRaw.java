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
package shadowmage.ancient_warfare.common.aw_structure.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.utils.StringTools;
import shadowmage.ancient_warfare.common.aw_structure.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.aw_structure.data.rules.NPCRule;
import shadowmage.ancient_warfare.common.aw_structure.data.rules.VehicleRule;

/**
 * raw structure as loaded from disk.
 * @author Shadowmage
 *
 */
public class LoadedStructureRaw extends ProcessedStructure
{

File file;
public boolean isValid = true;
private int currentLayer = 0;//used during parsing to increment Y upwards with each layer parsed

public LoadedStructureRaw(File file)
  {
  this.file = file;
  this.processFile();
  }

private void processFile()
  {
  //TODO move this out to the loader class....
  Scanner reader = null;
  try
    {
    reader = new Scanner(new FileInputStream(file));
    } 
  catch (FileNotFoundException e)
    {
    isValid = false;
    e.printStackTrace();    
    return;
    }
  List<String> lines = new ArrayList<String>();
  String line;
  /**
   * throw everything into a list, close the file
   */
  while(reader.hasNextLine())
    {
    line = reader.nextLine();
    if(line.startsWith("#"))//skip comment lines entirely, no need to parse later
      {
      continue;
      }
    lines.add(line);
    }  
  reader.close();
  /**
   * process from a nice in-memory list
   */
  try
    {
    if(file.getName().endsWith(".aws"))
      {
      this.parseLinesAW(lines);
      }
    else
      {
      this.parseLinesRuins(lines);
      }
    }
  catch(Exception e)
    {
    this.isValid = false;
    Config.logError("Error parsing structure for file: "+file.getName());
    }
  }

private void parseLinesRuins(List<String> lines)
  {
  if(lines==null)
    {
    this.isValid = false;
    return;
    }
  Iterator<String> it = lines.iterator();
  String line;
  while(it.hasNext() && (line = it.next())!=null)
    {
    if(line.toLowerCase().startsWith("acceptable_target_blocks"))
      {
      this.validTargetBlocks = StringTools.safeParseIntArray("=", line);
      }
    else if(line.toLowerCase().startsWith("dimensions"))
      {
      int[] dim = StringTools.safeParseIntArray("=", line);
      if(dim.length != 3 )
        {
        this.isValid = false;
        Config.logError("Error encountered while parsing Ruins .tml file, improper dimensions specified");
        return;
        }
      this.ySize = dim[0];
      this.xSize = dim[1];
      this.zSize = dim[2];
      this.xOffset = this.xSize/2;
      this.zOffset = this.zSize/2;
      }
    else if(line.toLowerCase().startsWith("weight"))
      {
      this.structureWeight = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("unique"))
      {
      this.unique = StringTools.safeParseIntAsBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("embed_into_distance"))
      {
      this.verticalOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("allowable_overhang"))
      {
      this.maxOverhang = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("max_cut_in"))
      {
      this.maxVerticalClear = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("max_leveling"))
      {
      this.maxLeveling = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("leveling_buffer"))
      {
      this.levelingBuffer = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("preserve_water"))
      {
      this.preserveWater = StringTools.safeParseIntAsBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("preserve_pants"))
      {
      this.preserveLava = StringTools.safeParseIntAsBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("preserve_lava"))
      {
      this.preservePlants = StringTools.safeParseIntAsBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("rule"))
      {
      BlockRule rule = BlockRule.parseRuinsRule(line, this.blockRules.size());
      if(rule!=null)
        {
        this.blockRules.add(rule);
        }
      }
    else if(line.toLowerCase().startsWith("layer"))
      {
      this.parseLayer(it);
      }    
    }  
  }

/**
 * parse a list of lines into variables, rules, setups, and layers
 * @param lines
 */
private void parseLinesAW(List<String> lines)
  {
  if(lines==null)
    {
    this.isValid = false;
    return;
    }
  Iterator<String> it = lines.iterator();
  String line;
  while(it.hasNext())
    {
    line = it.next();    
    if(line.toLowerCase().startsWith("name"))//structure name
      {
      this.name = line.split("=")[1];
      }
    else if(line.toLowerCase().startsWith("worldgen"))
      {
      this.worldGen = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("creative"))
      {
      this.creative = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("survival"))
      {
      this.survival = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("unique"))//structure uniqueness
      {
      this.unique = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("structureWeight"))
      {
      this.structureWeight = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("underground"))
      {
      this.underground = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("undergroundminlevel"))
      {
      this.undergroundMinLevel = StringTools.safeParseInt("=", line);//Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("undergroundmaxlevel"))
      {
      this.undergroundMaxLevel = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("undergroundmaxairabove"))
      {
      this.undergroundMaxAirAbove = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("undergroundallowpartial"))
      {
      this.undergroundAllowPartial = StringTools.safeParseBoolean("=", line);
      }    
    else if(line.toLowerCase().startsWith("xsize"))
      {
      this.xSize = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("ysize"))
      {
      this.ySize = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("zsize"))
      {
      this.zSize = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("validtargetblocks"))
      {      
      this.validTargetBlocks = StringTools.safeParseIntArray("=",line);
      }
    else if(line.toLowerCase().startsWith("verticaloffset"))
      {
      this.verticalOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("xoffset"))
      {
      this.xOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("zoffset"))
      {
      this.zOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("maxoverhang"))
      {
      this.maxOverhang = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("maxleveling"))
      {
      this.maxLeveling = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("levelingbuffer"))
      {
      this.levelingBuffer = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("maxverticalclear"))
      {
      this.maxVerticalClear = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("clearingbuffer"))
      {
      this.clearingBuffer = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("preservewater"))// 
      {
      this.preserveWater = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("preservelava"))// 
      {
      this.preserveLava = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("preserveplants"))// 
      {
      this.preservePlants = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("preserveblocks"))// 
      {
      this.preserveBlocks = StringTools.safeParseBoolean("=", line);
      }    
    else if(line.toLowerCase().startsWith("biomesonlyin"))
      {
      this.biomesOnlyIn = StringTools.safeParseStringArray("=", line);
      }
    else if(line.toLowerCase().startsWith("biomesnotin"))
      {
      this.biomesNotIn = StringTools.safeParseStringArray("=", line);
      }
    
    
    
    /**
     * parse out block rules
     */
    else if(line.toLowerCase().startsWith("rule:"))
      {      
      parseRule(it);
      }
    
    /**
     * parse out vehicle setups
     */
    else if(line.toLowerCase().startsWith("vehicle:"))
      {      
      parseVehicle(it);
      }
    
    /**
     * parse out npc setups
     */
    else if(line.toLowerCase().startsWith("npc:"))
      {      
      parseNPC(it);
      }
    
    /**
     * parse out layers
     */
    else if(line.toLowerCase().startsWith("layer:"))
      {
      parseLayer(it);
      }
    
    if(!this.isValid)
      {
      break;
      }
    }  
  }

private void parseLayer(Iterator<String> it)
  {
  if(!it.hasNext())
    {
    return;
    }  
  if(structure==null)
    {
    if(this.xSize ==0 || this.ySize==0 || this.zSize==0)
      {
      Config.logError("Invalid structure size while attempting to create layers, one or more axis are size 0!");
      this.isValid = false;
      return;
      }
    else
      {
      structure = new short[xSize][ySize][zSize];
      }
    }
  String line;
  String [] vals;
  int currentRow = 0;
  
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("layer:") || line.toLowerCase().startsWith("layer"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith(":endlayer") || line.toLowerCase().startsWith("endlayer"))
      {
      break;      
      }
    else
      {
      vals = line.split(",");
      for(int i = 0; i < vals.length; i++)
        {
        if(i>=structure.length ||currentLayer>=structure[i].length || currentRow>= structure[i][currentLayer].length)
          {
          Config.logError("Error parsing layer in structure, an array index was out of bounds! (Layer larger than dimensions!)");
          this.isValid = false;
          break;
          }
        this.structure[i][currentLayer][currentRow] = Short.parseShort(vals[i]);
        }      
      currentRow++;
      }    
    }
  this.currentLayer++;
  }

/**
 * parses out a single blockRule from the iterator of lines passed in
 * @param it
 */
private void parseRule(Iterator<String> it)
  {
  if(!it.hasNext())
    {
    this.isValid = false;
    return;
    }
  ArrayList<String> ruleLines = new ArrayList<String>();  
  String line;  
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("rule:"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith(":endrule"))
      {
      break;      
      }
    else
      {
      ruleLines.add(line);      
      }    
    }     
  BlockRule rule = BlockRule.parseRule(ruleLines);
  if(rule!=null)
    {    
    this.blockRules.add(rule);    
    }
  else
    {
    Config.logError("Error parsing block rule for structure!");
    this.isValid = false;
    }
  }

/**
 * parse out a single vehicleRule/setup type from the iterator of lines
 * @param it
 */
private void parseVehicle(Iterator<String> it)
  {
  if(!it.hasNext())
    {
    this.isValid = false;
    return;
    }
  ArrayList<String> ruleLines = new ArrayList<String>();  
  String line;  
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("rule:"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith(":endrule"))
      {
      break;      
      }
    else
      {
      ruleLines.add(line);      
      }    
    }     
  VehicleRule rule = VehicleRule.parseRule(ruleLines);
  if(rule!=null)
    {    
    this.vehicleRules.add(rule);    
    }
  else
    {
    Config.logError("Error parsing vehicle rule for structure!");
    this.isValid = false;
    }
  }

/**
 * parse out a single npcRule/setup type from the iterator of lines
 * @param it
 */
private void parseNPC(Iterator<String> it)
  {
  
  }
}
