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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import shadowmage.ancient_warfare.common.aw_structure.data.rules.BlockRule;

/**
 * raw structure as loaded from disk. non validated or converted.
 * @author Shadowmage
 *
 */
public class LoadedStructureRaw
{

String name;

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
Map<Integer, BlockRule> blockRules = new HashMap<Integer, BlockRule>();
Map<Integer, BlockRule> specBlockRules = new HashMap<Integer, BlockRule>();

/**
 * array of ruleID references making up this structure
 * these refer to the key in the blockRules map
 * this array basically holds the levels from the ruins template
 */
public byte [][][] structure;

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
public int horizontalClearBuffer;

/**
 * maximum vertical fill distance for missing blocks below the structure
 * overrides overhang numbers
 */
public int maxLeveling;

/**
 * how many blocks outside of w,h,l should be leveled around the structure
 */
public int horizontalLevelBuffer;

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

public int xSize;//x dimension
public int zSize;//z dimension
public int ySize;//y dimension

File file;
public boolean isValid = true;

public LoadedStructureRaw(File file)
  {
  this.file = file;
  }

public void processFile()
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
  
    
  }

/**
 * read name, dimensions
 * @param lines
 */
public void readBasicStats(List<String> lines)
  {
  if(lines==null){return;}
  Iterator<String> it = lines.iterator();
  String line;
  while(it.hasNext())
    {
    line = it.next();
    if(line.startsWith("name="))
      {
      if(line.length()>5)
        {
        this.name = line.substring(5);
        }
      it.remove();
      }
    if(line.startsWith("width="))
      {
      if(line.length()>6)
        {
        this.xSize = Integer.valueOf(line.substring(6));
        }
      it.remove();
      }
    
    
    
    }  
  }


}
