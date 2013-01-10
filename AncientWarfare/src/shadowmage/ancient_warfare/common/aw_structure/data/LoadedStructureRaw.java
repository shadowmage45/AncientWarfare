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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_structure.data.rules.BlockRule;

/**
 * raw structure as loaded from disk. non validated or converted.
 * @author Shadowmage
 *
 */
public class LoadedStructureRaw
{

String name;
public boolean unique;
public int chunkDistance=0;
public int chunkAttempts=1;

public boolean underground = false;
public int undergroundMinLevel=1;
public int undergroundMaxLevel=255;
public int undergroundMaxAirAbove = 0;
public boolean undergroundAllowPartial = false;

/**
 * preservation flags for entire structure
 */
public boolean preserveWater = false;
public boolean preserveLava = false;
public boolean preservePlants = false;
public boolean preserveBlocks = false;

/**
 * individual blockRules, will override structure rules for individual blocks
 * (incl advanced feature not supported by Ruins--per block preserve info)
 */
List<BlockRule> blockRules = new ArrayList<BlockRule>();


/**
 * array of ruleID references making up this structure
 * these refer to the key in the blockRules map
 * this array basically holds the levels from the ruins template
 */
public short [][][] structure;

/**
 * how many blocks may be non-solid below this structure
 */
public int maxOverhang;

/**
 * how many blocks vertically above base may be cleared 
 */
public int maxVerticalClear;

/**
 * how many blocks around the structure to clear (outside of w,h,l)
 */
public int clearingBuffer;

/**
 * maximum vertical fill distance for missing blocks below the structure
 * overrides overhang numbers
 */
public int maxLeveling;

/**
 * how many blocks outside of w,h,l should be leveled around the structure
 */
public int levelingBuffer;

/**
 * valid targets to build this structure upon.
 * may be overridden with a custom list
 */
public int[] validTargetBlocks = {1,2,3,12,13};

/**
 * i.e. embed_into_distance
 * how far should this structure generate below the chosen site level
 */
public int verticalOffset;
public int xOffset;
public int zOffset;

public int xSize;//x dimension
public int zSize;//z dimension
public int ySize;//y dimension

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
  LinkedList<String> lines = new LinkedList<String>();
  String line;
  /**
   * throw everything into a linked list, close the file
   */
  while(reader.hasNextLine())
    {
    line = reader.nextLine();
    if(line.startsWith("#"))//skip comment lines entirely, no need to parse later
      {
      continue;
      }
    lines.add(reader.nextLine());
    }  
  reader.close();
  /**
   * process from a nice in-memory list
   */
  this.parseLines(lines);
  }

/**
 * parse a list of lines into variables, rules, setups, and layers
 * @param lines
 */
private void parseLines(List<String> lines)
  {
  if(lines==null){return;}
  Iterator<String> it = lines.iterator();
  String line;
  while(it.hasNext())
    {
    line = it.next();    
    if(line.toLowerCase().startsWith("name="))//structure name
      {
      this.name = line.split("=")[1];
      }
    else if(line.toLowerCase().startsWith("unique="))//structure uniqueness
      {
      this.unique = Boolean.parseBoolean(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("underground="))
      {
      this.underground = Boolean.parseBoolean(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("undergroundminlevel="))
      {
      this.undergroundMinLevel = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("undergroundmaxlevel="))
      {
      this.undergroundMaxLevel = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("undergroundmaxairabove="))
      {
      this.undergroundMaxAirAbove = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("undergroundallowpartial="))
      {
      this.undergroundAllowPartial = Boolean.parseBoolean(line.split("=")[1]);
      }    
    else if(line.toLowerCase().startsWith("xsize="))
      {
      this.xSize = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("ysize="))
      {
      this.ySize = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("zsize="))
      {
      this.zSize = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("validtargetblocks="))
      {
      String[] targets = line.split("=");
      targets = targets[1].split(",");
      this.validTargetBlocks = new int[targets.length];
      for(int i = 0; i<targets.length; i++)
        {
        this.validTargetBlocks[i]=Integer.parseInt(targets[i]);
        }
      }
    else if(line.toLowerCase().startsWith("verticaloffset="))
      {
      this.verticalOffset = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("xoffset="))
      {
      this.xOffset = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("zoffset="))
      {
      this.zOffset = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("maxoverhang="))
      {
      this.maxOverhang = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("maxleveling="))
      {
      this.maxLeveling = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("levelingbuffer="))
      {
      this.levelingBuffer = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("maxverticalclear="))
      {
      this.maxVerticalClear = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("clearingbuffer="))
      {
      this.clearingBuffer = Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("preservewater="))// 
      {
      this.preserveWater = Boolean.parseBoolean(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("preservelava="))// 
      {
      this.preserveLava = Boolean.parseBoolean(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("preserveplants="))// 
      {
      this.preservePlants = Boolean.parseBoolean(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("preserveblocks="))// 
      {
      this.preserveBlocks = Boolean.parseBoolean(line.split("=")[1]);
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
    if(line.toLowerCase().startsWith("layer:"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith(":endlayer"))
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
  
  }

/**
 * parse out a single npcRule/setup type from the iterator of lines
 * @param it
 */
private void parseNPC(Iterator<String> it)
  {
  
  }
}
