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
package shadowmage.ancient_warfare.common.aw_structure.data.rules;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import shadowmage.ancient_warfare.common.aw_core.utils.StringTools;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockData;

public class BlockRule
{

/**
 * must be >=0 to be considered valid...this value MUST be set
 */
public short ruleNumber = -1;

/**
 * base chance to lay this block
 */
byte baseChance = 100;

/**
 * ordering of this rule, what pass it should place the block on
 * higher numbers = block placed later (if it depends on other blocks to be present)
 */
public byte order = 0;

/**
 * block conditional
 * 0--always valid
 * 1--must be block below
 * 2--must be block beside
 * 3--must be block above
 */
byte conditional = 0;

byte swapGroup = -1;

byte team = 0;
byte orientation = 0;


/**
 * blockData array for each block ID/meta for this blockRule, may contain duplicates for weighting
 */
public BlockData[] blockData;
public int[] vehicles;
public int[] npcs;

public boolean preserveWater = false;
public boolean preserveLava = false;
public boolean preservePlants = false;
public boolean preserveBlocks = false;

private BlockRule()
  {
  }

public BlockData getBlockChoice(Random random)
  {
  return blockData[random.nextInt(blockData.length)];
  }

public static BlockRule parseRule(List<String> ruleLines)
  {
  String line;
  Iterator<String> it = ruleLines.iterator();
  BlockRule rule = new BlockRule();
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("number"))
      {
      rule.ruleNumber = Short.parseShort(line.split("=")[1]);      
      }    
    if(line.toLowerCase().startsWith("conditional"))
      {
      rule.conditional = Byte.parseByte(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("percent"))
      {
      rule.baseChance = Byte.parseByte(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("order"))
      {
      rule.order = Byte.parseByte(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("preservewater"))
      {
      rule.preserveWater = Boolean.parseBoolean(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("preservelava"))
      {
      rule.preserveLava = Boolean.parseBoolean(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("preserveplants"))
      {
      rule.preservePlants = Boolean.parseBoolean(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("preserveblocks"))
      {
      rule.preserveBlocks = Boolean.parseBoolean(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("blocks"))
      {
      rule.blockData = parseBlocks(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("vehicles"))
      {
      rule.vehicles = StringTools.parseIntArray(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("npcs"))
      {
      rule.npcs = StringTools.parseIntArray(line.split("=")[1]);
      }
    }
  if((rule.blockData !=null || rule.vehicles !=null ||rule.npcs!=null )&& rule.ruleNumber>=0)
    {
    return rule;
    }
  return null;
  }

private static BlockData[] parseBlocks(String csv)
  {  
  String[] csvValues = csv.split(",");
  BlockData[] datas = new BlockData[csvValues.length];
  String[] csvBlock;
  String blockData;
  String bID;
  String mID;
  for(int i = 0; i < csvValues.length; i++)
    {
    blockData = csvValues[i];
    csvBlock = blockData.split("-");
    bID = csvBlock[0];
    if(csvBlock.length>1)
      {
      mID = csvBlock[1];
      }
    else
      {
      mID = "0";
      }    
    datas[i] = new BlockData(Integer.parseInt(bID), Integer.parseInt(mID));
    }
  return datas;
  }

}
