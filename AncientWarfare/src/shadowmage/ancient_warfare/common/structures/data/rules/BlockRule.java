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
package shadowmage.ancient_warfare.common.structures.data.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.manager.BlockDataManager;
import shadowmage.ancient_warfare.common.structures.data.BlockData;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class BlockRule
{

/**
 * must be >=0 to be considered valid...this value MUST be set
 */
public short ruleNumber = -1;

/**
 * base chance to lay this block
 */
public byte baseChance = 100;

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
public byte conditional = 0;

public byte swapGroup = -1;

byte team = 0;
public byte orientation = 0;


/**
 * blockData array for each block ID/meta for this blockRule, may contain duplicates for weighting
 */
public BlockData[] blockData;
/**
 * ruins special block data (chests, spawners)
 */
public String[] ruinsSpecialData;
/**
 * int array of vehicle rules which may be used here
 */
public int[] vehicles;
/**
 * int array of npc rules which may be used here
 */
public int[] npcs;

/**
 * possible entity names for spawners
 */
public String[] spawnerTypes;

/**
 * should preserve water/lava/plants when attempting to place this rule?
 */
public boolean preserveWater = false;
public boolean preserveLava = false;
public boolean preservePlants = false;

/**
 * special preserveBlocks flag, see below
 */
public boolean preserveBlocks = false;

/**
 * if preserveBlocks is true, and preservedBlocks is not null, will check preservedBlocks to see
 * if block should be kept or overwritten.  If preservedBlocks is null, all blocks will be preserved
 */
public BlockData[] preservedBlocks;

private BlockRule()
  {
  }

public BlockRule(int ruleNum, int id, int meta)
  {
  this.ruleNumber = (short) ruleNum;
  this.blockData = new BlockData[1];
  this.blockData[0] = new BlockData(id, meta);
  this.order = (byte) BlockDataManager.getBlockPriority(id, meta);
  }

public BlockData getBlockChoice(Random random)
  {
  return blockData[random.nextInt(blockData.length)];
  }

/**
 * checks preservedBlocks list, does not validate plants/etc
 * @param id
 * @param meta
 * @return
 */
public boolean shouldPreserveBlock(int id, int meta)
  {
  if(preservedBlocks!=null)
    {
    for(BlockData data : this.preservedBlocks)
      {
      if(data.id==id && data.meta==meta)
        {
        return true;
        }
      }
    return false;
    }
  return true;
  }

/**
 * parse a blockRule from a ruins format template
 * @param line
 * @param ruleNum
 * @return
 */
public static BlockRule parseRuinsRule(String line, int ruleNum)
  {
  BlockRule rule = new BlockRule();
  rule.ruleNumber = (short) ruleNum;
  String[] split = StringTools.safeParseStringArray("=", line);
  if(split.length>2)
    {
    rule.conditional =  Byte.parseByte(split[0]);
    rule.baseChance = Byte.parseByte(split[1]);
    }
  else //not enough data to make a valid rule, might as well not even look at it
    {
    return null;
    }
  List<String> specialStrings = new ArrayList<String>();
  List<String> spawnerStrings = new ArrayList<String>();
  List<BlockData> parsedBlocks = new ArrayList<BlockData>();
  for(int i = 2; i <split.length; i++)
    {
    String data = split[i];
    if(split[i].toLowerCase().startsWith("preserveBlock"))
      {
      specialStrings.add(data.trim());      
      }
    else if(split[i].toLowerCase().startsWith("mobspawner:"))
      {
      String[] sp = split[i].split(":");
      if(sp.length>1)
        {
        spawnerStrings.add(sp[1].trim());
        }
      }
    else if(split[i].toLowerCase().startsWith("uprightmobspawn"))
      {
      spawnerStrings.add("Creeper");
      spawnerStrings.add("Skeleton");
      spawnerStrings.add("Zombie");
      }
    else if(split[i].toLowerCase().startsWith("easymobspawn"))
      {
      spawnerStrings.add("Zombie");
      spawnerStrings.add("Skeleton");
      }
    else if(split[i].toLowerCase().startsWith("mediummobspawn"))
      {
      spawnerStrings.add("Spider");
      spawnerStrings.add("CaveSpider");
      spawnerStrings.add("Skeleton");
      spawnerStrings.add("Zombie");
      }
    else if(split[i].toLowerCase().startsWith("hardmobspawn"))
      {
      spawnerStrings.add("Blaze");
      spawnerStrings.add("Creeper");
      spawnerStrings.add("Skeleton");
      spawnerStrings.add("CaveSpider");
      }
    else if(split[i].toLowerCase().startsWith("easychest"))
      {
      specialStrings.add(data.trim());
      }
    else if(split[i].toLowerCase().startsWith("mediumchest"))
      {
      specialStrings.add(data.trim());
      }
    else if(split[i].toLowerCase().startsWith("hardchest"))
      {
      specialStrings.add(data.trim());
      }
    else
      {
      int id = 0;
      int meta = 0;
      String[] ruleSplit = data.split("-");
      if(StringTools.isNumber(ruleSplit[0]))
        {
        id = Integer.parseInt(ruleSplit[0].trim());
        }
      else
        {
        id = findBlockByName(ruleSplit[0].trim());
        }
      if(ruleSplit.length>1)
        {
        meta = Integer.parseInt(ruleSplit[1].trim());
        }
      parsedBlocks.add(new BlockData(id, meta));      
      }
    }
  if(parsedBlocks.size()>0)
    {
    rule.blockData = new BlockData[parsedBlocks.size()];
    for(int i = 0; i < parsedBlocks.size(); i++)
      {
      rule.blockData[i] = parsedBlocks.get(i);
      }
    }
  if(specialStrings.size()>0)
    {
    rule.ruinsSpecialData = new String[specialStrings.size()];
    for(int i = 0; i < specialStrings.size(); i++)
      {
      rule.ruinsSpecialData[i] = specialStrings.get(i);
      }
    }
  if(spawnerStrings.size()>0)
    {
    rule.spawnerTypes = new String[spawnerStrings.size()];
    for(int i = 0; i < spawnerStrings.size(); i++)
      {
      rule.spawnerTypes[i] = spawnerStrings.get(i);
      }
    }
  return rule;
  }

/**
 * attempts to find a block from Block.blocksList by name
 * @param name
 * @return id or 0 if not found
 */
private static int findBlockByName(String name)
  {
  if(name==null)
    {
    return 0;
    }
  for(Block block : Block.blocksList)
    {
    if(block==null)
      {
      continue;
      }
    if(block!= null && name.equals(block.getBlockName()));
      {
      return block.blockID;
      }
    }  
  return 0;
  }

/**
 * parse a blockRule from an AW format template
 * @param ruleLines
 * @return
 */
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
      rule.ruleNumber = StringTools.safeParseShort("=", line);      
      }    
    if(line.toLowerCase().startsWith("conditional"))
      {
      rule.conditional = StringTools.safeParseByte("=", line);
      }
    if(line.toLowerCase().startsWith("percent"))
      {
      rule.baseChance = StringTools.safeParseByte("=", line);
      }
    if(line.toLowerCase().startsWith("order"))
      {
      rule.order = StringTools.safeParseByte("=", line);
      }
    if(line.toLowerCase().startsWith("swap"))
      {
      rule.swapGroup = StringTools.safeParseByte("=", line);
      }
    if(line.toLowerCase().startsWith("preservewater"))
      {
      rule.preserveWater = StringTools.safeParseBoolean("=", line);
      }
    if(line.toLowerCase().startsWith("preservelava"))
      {
      rule.preserveLava = StringTools.safeParseBoolean("=", line);
      }
    if(line.toLowerCase().startsWith("preserveplants"))
      {
      rule.preservePlants = StringTools.safeParseBoolean("=", line);
      }
    if(line.toLowerCase().startsWith("preserveblocks"))
      {
      rule.preserveBlocks = StringTools.safeParseBoolean("=", line);
      }
    if(line.toLowerCase().startsWith("preservedblocks"))
      {
      rule.preservedBlocks = parseBlocks(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("blocks"))
      {
      rule.blockData = parseBlocks(line.split("=")[1]);
      }
    if(line.toLowerCase().startsWith("vehicles"))
      {
      rule.vehicles = StringTools.safeParseIntArray("=", line);
      }
    if(line.toLowerCase().startsWith("npcs"))
      {
      rule.npcs = StringTools.safeParseIntArray("=", line);
      }
    if(line.toLowerCase().startsWith("ruinsspecialdata"))
      {
      rule.ruinsSpecialData = StringTools.safeParseStringArray("=", line);
      }    
    if(line.toLowerCase().startsWith("spawner"))
      {
      rule.spawnerTypes = StringTools.safeParseStringArray("=", line);
      }
    }
  if((rule.blockData !=null || rule.vehicles !=null ||rule.npcs!=null || rule.ruinsSpecialData !=null || rule.spawnerTypes != null)&& rule.ruleNumber>=0)
    {
    return rule;
    }  
  return null;
  }

/**
 * parse out an array of blockData from a csv and '-' delimited string 
 * (csv for records, '-' for meta in each record)
 * @param csv
 * @return
 */
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
    bID = csvBlock[0].trim();
    if(csvBlock.length>1)
      {
      mID = csvBlock[1].trim();
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
