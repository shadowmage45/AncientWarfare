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
package shadowmage.template_converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.validation.StructureValidator;

public class TemplateConverter
{

private static HashMap<Integer, String> blockIDToName = new HashMap<Integer, String>();
private static HashSet<Integer> specialHandledBlocks = new HashSet<Integer>();//just a temp cache to keep track of what blocks to not register with blanket block rule
private static HashMap<String, List<String>> specialBlockDataTags = new HashMap<String, List<String>>();
private static HashMap<String, String> blockNameToPluginName = new HashMap<String, String>();

short[] templateData = null;
int xSize = 0, ySize = 0, zSize = 0;
int xOffset = 0, yOffset = 0, zOffset = 0;

List<String> groupedLines = new ArrayList<String>();
List<ParsedRule> parsedRules = new ArrayList<ParsedRule>();
int parsedLayers = 0;
int readSizeParams = 0;
int highestRuleNumber = 0;

private File in;
private File out;

static
{
addSpecialHandledBlock(63, "wall_sign", "vanillaSign");
addSpecialHandledBlock(68, "standing_sign", "vanillaSign");
addSpecialHandledBlock(64, "wooden_door", "vanillaDoors");
addSpecialHandledBlock(71, "iron_door", "vanillaDoors");
addSpecialHandledBlock(137, "command_block", "vanillaLogic");
addSpecialHandledBlock(52, "monster_spawner", "vanillaLogic");
addSpecialHandledBlock(61, "furnace", "vanillaLogic");
addSpecialHandledBlock(62, "lit_furnace", "vanillaLogic");
addSpecialHandledBlock(144, "skull", "vanillaLogic");
addSpecialHandledBlock(117, "brewing_stand", "vanillaLogic");
addSpecialHandledBlock(138, "beacon", "vanillaLogic");
addSpecialHandledBlock(54, "chest", "vanillaInventory");
addSpecialHandledBlock(158, "dropper", "vanillaInventory");
addSpecialHandledBlock(23, "dispenser", "vanillaInventory");
addSpecialHandledBlock(154, "hopper", "vanillaInventory");

add17NameMaping(1, "stone");
add17NameMaping(2, "grass");
add17NameMaping(3, "dirt");
add17NameMaping(4, "cobblestone");
add17NameMaping(5, "planks");
add17NameMaping(6, "sapling");
add17NameMaping(7, "bedrock");
add17NameMaping(8, "flowing_water");
add17NameMaping(9, "water");
add17NameMaping(10, "flowing_lava");
add17NameMaping(11, "lava");
add17NameMaping(12, "sand");
add17NameMaping(13, "gravel");
add17NameMaping(14, "gold_ore");
add17NameMaping(15, "iron_ore");
add17NameMaping(16, "coal_ore");
add17NameMaping(17, "log");
add17NameMaping(18, "leaves");
add17NameMaping(19, "sponge");
add17NameMaping(20, "glass");
add17NameMaping(21, "lapis_ore");
add17NameMaping(22, "lapis_block");
add17NameMaping(23, "dispenser");
add17NameMaping(24, "sandstone");
add17NameMaping(25, "noteblock");
add17NameMaping(26, "bed");
add17NameMaping(27, "golden_rail");
add17NameMaping(28, "detector_rail");
add17NameMaping(29, "sticky_piston");
add17NameMaping(30, "web");
add17NameMaping(31, "tallgrass");
add17NameMaping(32, "deadbush");
add17NameMaping(33, "piston");
add17NameMaping(34, "piston_head");
add17NameMaping(35, "wool");
add17NameMaping(36, "piston_extension");
add17NameMaping(37, "yellow_flower");
add17NameMaping(38, "red_flower");
add17NameMaping(39, "brown_mushroom");
add17NameMaping(40, "red_mushroom");
add17NameMaping(41, "gold_block");
add17NameMaping(42, "iron_block");
add17NameMaping(43, "double_stone_slab");
add17NameMaping(44, "stone_slab");
add17NameMaping(45, "brick");
add17NameMaping(46, "tnt");
add17NameMaping(47, "bookshelf");
add17NameMaping(48, "mossy_cobblestone");
add17NameMaping(49, "obsidian");
add17NameMaping(50, "torch");
add17NameMaping(51, "fire");
add17NameMaping(52, "mob_spawner");
add17NameMaping(53, "oak_stairs");
add17NameMaping(53, "chest");
add17NameMaping(55, "redstone_wire");
add17NameMaping(56, "diamond_ore");
add17NameMaping(57, "diamond_block");
add17NameMaping(58, "crafting_table");
add17NameMaping(59, "wheat");
add17NameMaping(60, "farmland");
add17NameMaping(61, "furnace");
add17NameMaping(62, "lit_furnace");
add17NameMaping(63, "standing_sign");
add17NameMaping(64, "wooden_door");
add17NameMaping(65, "ladder");
add17NameMaping(66, "rail");
add17NameMaping(67, "stone_stairs");
add17NameMaping(68, "wall_sign");
add17NameMaping(69, "lever");
add17NameMaping(70, "stone_pressure_plate");
add17NameMaping(71, "iron_door");
add17NameMaping(72, "wooden_pressure_plate");
add17NameMaping(73, "redstone_ore");
add17NameMaping(74, "lit_redstone_ore");
add17NameMaping(75, "unlit_redstone_torch");
add17NameMaping(76, "redstone_torch");
add17NameMaping(77, "stone_button");
add17NameMaping(78, "snow_layer");
add17NameMaping(79, "ice");
add17NameMaping(80, "snow");
add17NameMaping(81, "cactus");
add17NameMaping(82, "clay");
add17NameMaping(83, "reeds");
add17NameMaping(84, "jukebox");
add17NameMaping(85, "fence");
add17NameMaping(86, "pumpkin");
add17NameMaping(87, "netherrack");
add17NameMaping(88, "soul_sand");
add17NameMaping(89, "glowstone");
add17NameMaping(90, "portal");
add17NameMaping(91, "lit_pumpkin");
add17NameMaping(92, "cake");
add17NameMaping(93, "unpowered_repeater");
add17NameMaping(94, "powered_repeater");
add17NameMaping(95, "stained_glass");
add17NameMaping(96, "trapdoor");
add17NameMaping(97, "monster_egg");
add17NameMaping(98, "stonebrick");
add17NameMaping(99, "brown_mushroom_block");
add17NameMaping(100, "red_mushroom_block");
add17NameMaping(101, "iron_bars");
add17NameMaping(102, "glass_pane");
add17NameMaping(103, "melon_block");
add17NameMaping(104, "pumpkin_stem");
add17NameMaping(105, "melon_stem");
add17NameMaping(106, "vine");
add17NameMaping(107, "fence_gate");
add17NameMaping(108, "brick_stairs");
add17NameMaping(109, "stone_brick_stairs");
add17NameMaping(110, "mycelium");
add17NameMaping(111, "waterlily");
add17NameMaping(112, "nether_brick");
add17NameMaping(113, "nether_brick_fence");
add17NameMaping(114, "nether_brick_stairs");
add17NameMaping(115, "nether_wart");
add17NameMaping(116, "enchanting_table");
add17NameMaping(117, "brewing_stand");
add17NameMaping(117, "cauldron");
add17NameMaping(119, "end_portal");
add17NameMaping(120, "end_portal_frame");
add17NameMaping(121, "end_stone");
add17NameMaping(122, "dragon_egg");
add17NameMaping(123, "redstone_lamp");
add17NameMaping(124, "lit_redstone_lamp");
add17NameMaping(125, "double_wooden_slab");
add17NameMaping(126, "wooden_slab");
add17NameMaping(127, "cocoa");
add17NameMaping(128, "sandstone_stairs");
add17NameMaping(129, "emerald_ore");
add17NameMaping(130, "ender_chest");
add17NameMaping(131, "tripwire_hook");
add17NameMaping(132, "tripwire");
add17NameMaping(133, "emerald_block");
add17NameMaping(134, "spruce_stairs");
add17NameMaping(135, "birch_stairs");
add17NameMaping(136, "jungle_stairs");
add17NameMaping(137, "command_block");
add17NameMaping(138, "beacon");
add17NameMaping(139, "cobblestone_wall");
add17NameMaping(140, "flower_pot");
add17NameMaping(141, "carrots");
add17NameMaping(142, "potatoes");
add17NameMaping(143, "wooden_button");
add17NameMaping(144, "skull");
add17NameMaping(145, "anvil");
add17NameMaping(146, "trapped_chest");
add17NameMaping(147, "light_weighted_pressure_plate");
add17NameMaping(148, "heavy_weighted_pressure_plate");
add17NameMaping(149, "unpowered_comparator");
add17NameMaping(150, "powered_comparator");
add17NameMaping(151, "daylight_detector");
add17NameMaping(152, "redstone_block");
add17NameMaping(153, "quartz_ore");
add17NameMaping(154, "hopper");
add17NameMaping(155, "quartz_block");
add17NameMaping(156, "quartz_stairs");
add17NameMaping(157, "activator_rail");
add17NameMaping(158, "dropper");
add17NameMaping(159, "stained_hardened_clay");
add17NameMaping(160, "stained_glass_pane");
add17NameMaping(161, "leaves2");
add17NameMaping(162, "log2");
add17NameMaping(163, "acacia_stairs");
add17NameMaping(164, "dark_oak_stairs");
add17NameMaping(170, "hay_block");
add17NameMaping(171, "carpet");
add17NameMaping(172, "hardened_clay");
add17NameMaping(173, "coal_block");
add17NameMaping(174, "packed_ice");
add17NameMaping(175, "double_plant");

try
  {
  loadDataTags();
  } 
catch (IOException e)
  {
  e.printStackTrace();
  throw new IllegalArgumentException("ERROR LOADING BUILT-IN DATA TAG FILE");
  }
}

private static void add17NameMaping(int id, String name)
  {
  blockIDToName.put(id, name);
  }

private static void addSpecialHandledBlock(int id, String name, String plugin)
  {
  specialHandledBlocks.add(id);
  blockNameToPluginName.put(name, plugin);
  }

private static void loadDataTags() throws IOException
  {
  InputStream is = Converter.class.getResourceAsStream("/shadowmage/template_converter/dataTags.dat");
  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
  
  List<String> groupedLines = new ArrayList<String>();
  
  String line;
  while((line = reader.readLine())!=null)
    {
    if(line.startsWith("#")){continue;}
    else if(line.startsWith("entry:"))
      {
      groupedLines.add(line);
      while((line = reader.readLine())!=null)
        {
        groupedLines.add(line);
        if(line.toLowerCase().startsWith(":endentry"))
          {
          break;
          }
        }
      parseDataTag(groupedLines);
      groupedLines.clear();
      }
    }  
  is.close();
  reader.close();
  }

private static void parseDataTag(List<String> lines)
  {
  String name = null;
  
  List<String> dataLines = new ArrayList<String>();
  Iterator<String> it = lines.iterator();
  String line;
  
  while(it.hasNext() && (line = it.next())!=null)
    {
    if(line.toLowerCase().startsWith("name="))
      {
      name = line.split("=")[1];
      }
    else if(line.startsWith("data:"))
      {
      while(it.hasNext() && (line = it.next())!=null)
        {
        if(line.startsWith(":enddata"))
          {
          break;
          }
        dataLines.add(line);
        }
      }
    }
  if(name!=null && !name.equals(""))
    {
    specialBlockDataTags.put(name, dataLines);
    }
  }

public TemplateConverter(File in, File out)
  {
  this.in = in;
  this.out = out;
  }

private List<String> getTemplateLines(File file) throws IOException
  {
  BufferedReader reader = new BufferedReader(new FileReader(file));
  List<String> lines = new ArrayList<String>();
  String line;
  while((line = reader.readLine())!=null)
    {
    lines.add(line);
    }
  reader.close();
  return lines;
  }

public void doConversion() throws IOException
  {
  this.parseTemplate();
  this.writeTemplate();  
  }

private List<String> parseTag(String tag, Iterator<String> it, List<String> output)
  {
  String line;
  while(it.hasNext() && (line = it.next())!=null)
    {
    if(line.toLowerCase().startsWith(":end"+tag))
      {
      break;
      }
    output.add(line);
    }
  return output;
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
      templateData[StructureTemplate.getIndex(x, yLayer, z, xSize, ySize, zSize)] = data[x];
      }
    z++;
    }  
  }

private void parseTemplate() throws IOException
  {
  if(!out.exists()){out.createNewFile();}
  List<String> lines = getTemplateLines(in);    
  Iterator<String> it = lines.iterator();
  String line;
  
  boolean preserveBlocks;
  int ruleNumber = 0;
  
  while(it.hasNext() && (line=it.next())!=null)
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
      yOffset = (StringTools.safeParseInt("=", line));
      }
    else if(line.toLowerCase().startsWith("xoffset="))
      {
      xOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("zoffset"))
      {
      zOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("layer:"))
      {
      parseTag("layer", it, groupedLines);
      parseLayer(groupedLines, templateData, parsedLayers, xSize, ySize, zSize);
      parsedLayers++;
      groupedLines.clear();
      }
    else if(line.toLowerCase().startsWith("rule:"))
      {
      parseTag("rule", it, groupedLines);
      ParsedRule rule = parseOldBlockRule(groupedLines);
      if(rule!=null)
        {
        if(rule.ruleNumber > highestRuleNumber)
          {
          highestRuleNumber = rule.ruleNumber;
          }        
        parsedRules.add(rule);
        }
      groupedLines.clear();
      }
    } 
  if(xSize==0 || ySize==0 || zSize==0 || parsedLayers==0 || parsedRules.size()==0)
    {
    throw new IllegalArgumentException("Not enough data to construct a template!! ::" + String.format("x: %s, y: %s, z:%s, layers:%s, rules:%s", xSize, ySize, zSize, parsedLayers, parsedRules.size()));
    }
  }

private ParsedRule parseOldBlockRule(List<String> lines)
  {
  int number = 0;
  int id = 0;
  int meta = 0;
  int buildPass = 0;
  for(String line : lines)
    {
    if(line.toLowerCase().startsWith("number="))
      {
      number = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("blocks="))
      {
      String[] blockLines = StringTools.safeParseString("=", line).split(",");
      String[] blockData = blockLines[0].split("-");
      id = StringTools.safeParseInt(blockData[0]);
      meta = StringTools.safeParseInt(blockData[1]);
      }
    else if(line.toLowerCase().startsWith("order="))
      {
      buildPass = StringTools.safeParseInt("=", line);
      }    
    }
  
  String blockName = blockIDToName.get(id);
  if(blockName==null)
    {
    return null;
    }  
  else if(specialHandledBlocks.contains(id))
    {
    return parseSpecialBlockRule(number, blockName, id, meta, buildPass);
    }
  else
    {
    return new ParsedRule(number, blockName, meta, buildPass, "vanillaBlocks");
    }
  }

private ParsedRule parseSpecialBlockRule(int ruleNumber, String blockName, int id, int meta, int buildPass)
  {
  List<String> ruleData = specialBlockDataTags.get(blockName);
  if(ruleData!=null)
    {
    ParsedRule rule = new ParsedRule(ruleNumber, blockName, meta, buildPass, blockNameToPluginName.get(blockName));
    rule.setData(ruleData);
    return rule;
    }
  return null;
  }

private void writeTemplate() throws IOException
  {
  String message = String.format("writing template...layers: %s, rules: %s", parsedLayers, parsedRules.size());
  System.out.println(message);
  
  Calendar cal = Calendar.getInstance();
  BufferedWriter writer = new BufferedWriter(new FileWriter(out));
  writer.write("# Ancient Warfare Structure Template File");
  writer.newLine();
  writer.write("# Converted from old template format on: "+(cal.get(cal.MONTH)+1)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR)+ " at: "+cal.get(cal.HOUR_OF_DAY)+":"+cal.get(cal.MINUTE)+":"+cal.get(cal.SECOND));
  writer.newLine();
  writer.write("# Lines beginning with # denote comments");
  writer.newLine();
  writer.newLine();
  writer.write("header:");
  writer.newLine();
  writer.write("version=2.0");
  writer.newLine();    
  writer.write("name="+out.getName().substring(0, out.getName().length()-4));
  writer.newLine();
  writer.write("size="+xSize+","+ySize+","+zSize);
  writer.newLine();
  writer.write("offset="+xOffset+","+yOffset+","+zOffset);
  writer.newLine();
  writer.write(":endheader");
  writer.newLine();
  
  writer.newLine();
  writer.write("#### VALIDATION ####");    
  writer.newLine(); 
  writer.write("validation:");
  writer.newLine();
  writer.write("type=ground");
  writer.newLine();
  writer.write("worldGenEnabled=false");
  writer.newLine();
  writer.write("unique=false");
  writer.newLine();
  writer.write("preserveBlocks=false");
  writer.newLine();
  writer.write("selectionWeight=1");
  writer.newLine();
  writer.write("clusterValue=1");
  writer.newLine();
  writer.write("minDuplicateDistance=1");
  writer.newLine();
  writer.write("dimensionWhiteList=false");
  writer.newLine();
  writer.write("dimensionList=");
  writer.newLine();
  writer.write("biomeWhiteList=false");
  writer.newLine();
  writer.write("biomeList=");
  writer.newLine();
  writer.write("leveling=0");
  writer.newLine();
  writer.write("fill=0");
  writer.newLine();
  writer.write("border=0");
  writer.newLine();
  writer.write("validTargetBlocks=grass,sand,clay,dirt,stone,gravel,sandstone,iron_ore,coal_ore");
  writer.newLine();
  writer.write("data:");
  writer.newLine();
  writer.write(":enddata");
  writer.newLine();
  writer.write(":endvalidation");
  writer.newLine(); 
  
  writer.newLine();
  writer.write("#### LAYERS ####");
  writer.newLine();
  for(int y = 0; y< ySize; y++)
    {
    writer.write("layer: "+y);
    writer.newLine();
    for(int z = 0 ; z<zSize; z++)
      {
      for(int x = 0; x < xSize; x++)
        {        
        short data = templateData[getIndex(x,y,z, xSize,ySize,zSize)];          
        writer.write(String.valueOf(data));
        if(x < xSize-1)
          {
          writer.write(",");
          }
        }
      writer.newLine();
      }
    writer.write(":endlayer");
    writer.newLine();
    writer.newLine();
    }
  
  writer.write("#### RULES ####");
  writer.newLine();
  for(ParsedRule rule : this.parsedRules)
    {
    for(String line : rule.getRuleLines())
      {
      writer.write(line);
      writer.newLine();
      }
    writer.newLine();
    }
  
  writer.newLine();
  writer.write("#### ENTITIES ####");  
  writer.newLine();
  writer.close();
  }

private static int getIndex(int x, int y, int z, int xSize, int ySize, int zSize)
  {
  return (y * xSize * zSize) + (z * xSize) + x; 
  }

private static class ParsedRule
{
int ruleNumber;
String blockName;
int meta;
int buildPass;
String pluginHandlerType;
List<String> data = new ArrayList<String>();

public ParsedRule(int ruleNumber, String blockName, int meta, int buildPass, String pluginName)
  {
  this.ruleNumber = ruleNumber;
  this.blockName = blockName;
  this.meta = meta;
  this.buildPass = buildPass;
  this.pluginHandlerType = pluginName;
  }

public void setData(List<String> data)
  {
  this.data.clear();
  this.data.addAll(data);
  }

public List<String> getRuleLines()
  {
  List<String> lines = new ArrayList<String>();
  lines.add("rule:");
  lines.add("plugin="+pluginHandlerType);
  lines.add("number="+ruleNumber);
  lines.add("data:");
  lines.add("TAG=8=blockName{"+blockName+"}");
  lines.add("TAG=3=meta{"+meta+"}");
  lines.add("TAG=3=buildPass{"+buildPass+"}");
  lines.addAll(data);
  lines.add(":enddata");
  lines.add(":endrule");  
  return lines;
  }
}

}
