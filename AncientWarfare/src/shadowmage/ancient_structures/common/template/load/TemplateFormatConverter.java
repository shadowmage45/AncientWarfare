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
package shadowmage.ancient_structures.common.template.load;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.TemplateRuleVanillaBlocks;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;



public class TemplateFormatConverter
{

private static HashSet<Block> specialHandledBlocks = new HashSet<Block>();//just a temp cache to keep track of what blocks to not register with blanket block rule
static
{
specialHandledBlocks.add(Block.mobSpawner);
specialHandledBlocks.add(Block.signPost);
specialHandledBlocks.add(Block.signWall);
specialHandledBlocks.add(Block.doorIron);
specialHandledBlocks.add(Block.doorWood);
specialHandledBlocks.add(Block.chest);
specialHandledBlocks.add(Block.dropper);
specialHandledBlocks.add(Block.dispenser);
specialHandledBlocks.add(Block.enderChest);
specialHandledBlocks.add(Block.commandBlock);
specialHandledBlocks.add(Block.furnaceBurning);
specialHandledBlocks.add(Block.furnaceIdle);
specialHandledBlocks.add(Block.hopperBlock);
specialHandledBlocks.add(Block.skull);
specialHandledBlocks.add(Block.brewingStand);
}

public StructureTemplate convertOldTemplate(File file, List<String> templateLines)
  {
  AWLog.logDebug("should read old template format...");
  
  /**
   * parsed-out data, to be used to construct new template
   */
  List<TemplateRule> parsedRules = new ArrayList<TemplateRule>();
  short[] templateData = null;
  int xSize = 0, ySize = 0, zSize = 0;
  int xOffset = 0, yOffset = 0, zOffset = 0;
  boolean preserveBlocks;
   
  
  String name = file.getName();
  if(name.length()>=4)
    {
    name = name.substring(0, name.length()-4);
    }
  Iterator<String> it = templateLines.iterator();
  List<String> groupedLines = new ArrayList<String>();
  String line;
  int parsedLayers = 0;
  int readSizeParams = 0;
  int highestRuleNumber = 0;
  while(it.hasNext() && (line = it.next())!=null)
    {    
    if(line.toLowerCase().startsWith("xsize="))
      {
      xSize = StringTools.safeParseInt("=", line);
      readSizeParams++;
      if(readSizeParams==3)
        {
        templateData = new short[xSize*ySize*zSize];
        }
      }
    else if(line.toLowerCase().startsWith("ysize="))
      {
      ySize = StringTools.safeParseInt("=", line);
      readSizeParams++;
      if(readSizeParams==3)
        {
        templateData = new short[xSize*ySize*zSize];
        }
      }
    else if(line.toLowerCase().startsWith("zsize="))
      {
      zSize = StringTools.safeParseInt("=", line);
      readSizeParams++;
      if(readSizeParams==3)
        {
        templateData = new short[xSize*ySize*zSize];
        }
      }
    else if(line.toLowerCase().startsWith("verticaloffset="))
      {
      yOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("xoffset="))
      {
      xOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("zoffset"))
      {
      zOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("rule:"))
      {
      while(it.hasNext() && (line = it.next())!=null)
        {
        if(line.toLowerCase().startsWith(":endrule"))
          {
          break;
          }
        groupedLines.add(line);
        } 
      TemplateRule rule = parseOldBlockRule(groupedLines);
      if(rule!=null)
        {
        if(rule.ruleNumber>highestRuleNumber)
          {
          highestRuleNumber = rule.ruleNumber;
          }        
        parsedRules.add(rule);
        }
      groupedLines.clear();
      }
    else if(line.toLowerCase().startsWith("layer:"))
      {
      while(it.hasNext() && (line = it.next())!=null)
        {
        if(line.toLowerCase().startsWith(":endlayer"))
          {
          break;
          }
        groupedLines.add(line);
        }
      parseLayer(groupedLines, templateData, parsedLayers, xSize, ySize, zSize);
      parsedLayers++;
      groupedLines.clear();
      }    
    }  
  TemplateRule[] rules = new TemplateRule[highestRuleNumber+1];
  TemplateRule rule;
  for(int i = 0; i < parsedRules.size(); i++)
    {
    rule = parsedRules.get(i);
    if(rules[rule.ruleNumber]==null)
      {
      rules[rule.ruleNumber] = rule;
      }
    else
      {
      AWLog.logError("error parsing template rules, duplicate rule number detected for: "+rule.ruleNumber);
      }
    }
  StructureTemplate template = new StructureTemplate(name, xSize, ySize, zSize, xOffset, yOffset, zOffset);
  template.setRuleArray(rules);
  template.setTemplateData(templateData);
  return null;
  }

private TemplateRule parseOldBlockRule(List<String> lines)
  {
  int number = 0;
  int id = 0;
  int meta = 0;
  int buildPass = 0;
  for(String line : lines)
    {
    if(line.toLowerCase().startsWith("number=")){number = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("blocks="))
      {
      String[] blockLines = StringTools.safeParseString("=", line).split(",");
      String[] blockData = blockLines[0].split("-");
      id = StringTools.safeParseInt(blockData[0]);
      meta = StringTools.safeParseInt(blockData[1]);
      }
    else if(line.toLowerCase().startsWith("order=")){buildPass = StringTools.safeParseInt("=", line);}    
    }
  
  Block block = Block.blocksList[id];
  
  if(block==null)//skip any add modded blocks and air block rule (0/null)
    {    
    return null;
    }  
  else if(id>256)
    {
    return parseModBlock(block, number, buildPass, meta);
    
    }
  else if(specialHandledBlocks.contains(block))
    {
    return parseSpecialBlockRule(block, number, buildPass, meta);
    }
  else
    {
    TemplateRuleVanillaBlocks rule = new TemplateRuleVanillaBlocks();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    return rule;
    }
  }

private TemplateRule parseSpecialBlockRule(Block block, int number, int buildPass, int meta)
  {
  TemplateRule rule = null;
  if(block==Block.doorWood || block==Block.doorIron){}//vanilla door rule
  else if(block==Block.mobSpawner){}//vanilla spawner rule
  else if(block==Block.signPost || block==Block.signWall){}//vanilla sign rule
  else if(block==Block.skull){}
  else if(block==Block.commandBlock){}
  else if(block==Block.furnaceBurning){}
  else if(block==Block.brewingStand){}
  else {}//else it is just an inventory carrying block, handle with appropriate inventory rule
  return rule;
  }

private TemplateRule parseModBlock(Block block, int number, int buildPass, int meta)
  {
  /**
   * TODO add default modded block-handling rule (registered last, placed into registry for every block not already filled)
   * -- it is essentially a straight id/meta handler, ignores tile data (may even remove rule if block has a tile-entity)
   */
  return null;
  }

private void parseLayer(List<String> lines, short[] templateData, int yLayer, int xSize, int ySize, int zSize)
  {
  if(templateData==null){throw new IllegalArgumentException("cannot fill data into null template data array");}
  int z = 0;
  for(String st : lines)
    {
    if(st.startsWith("layer:") || st.startsWith(":endlayer"))
      {
      continue;
      }
    short[] data = StringTools.parseShortArray(st);
    for(int x = 0; x < xSize && x < data.length; x++)
      {
      templateData[StructureTemplate.getIndex(x, yLayer, z, xSize, ySize, zSize)] = (short) (data[x]==0? 0 : data[x]-1);
      }
    z++;
    }  
  }


}
