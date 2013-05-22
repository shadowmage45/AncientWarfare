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
package shadowmage.ancient_warfare.common.structures.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.data.BlockData;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.structures.data.rules.CivicRule;
import shadowmage.ancient_warfare.common.structures.data.rules.EntityRule;
import shadowmage.ancient_warfare.common.structures.data.rules.GateRule;
import shadowmage.ancient_warfare.common.structures.data.rules.InventoryRule;
import shadowmage.ancient_warfare.common.structures.data.rules.NpcRule;
import shadowmage.ancient_warfare.common.structures.data.rules.SwapRule;
import shadowmage.ancient_warfare.common.structures.data.rules.VehicleRule;
import shadowmage.ancient_warfare.common.utils.IDPairCount;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class StructureExporter
{

/**
 * write a structure to file given the fully qualified path and filename
 * @param struct
 * @param name
 * @return
 */
public static boolean writeStructureToFile(ProcessedStructure struct, String name, boolean canOverwrite)
  {
  File outputFile = new File(name);
  if(!outputFile.exists())
    {
    try
      {
      outputFile.createNewFile();
      } 
    catch (IOException e)
      {
      Config.logError("Could not create file for structure: "+name);
      e.printStackTrace();
      return false;
      }
    }
  else if(!canOverwrite)
    {
    Config.logError("Exporting would overwrite structure with name: "+name+"  Operation aborted.  Please choose a different name before rescanning and exporting");
    return false;    
    }
  FileWriter writer = null;
  try
    {
    writer = new FileWriter(outputFile);
    List<String> lines = null;
    if(!struct.getTemplate().getLines().isEmpty())
      {
      lines = struct.getTemplate().getLines();
      }
    else
      {
      lines = getExportLinesFor(struct);
      }
    for(String line : lines)
      {
      writer.write(line+"\n");
      }
    writer.close();
    return true;
    } 
  catch (IOException e)
    {
    Config.logError("Severe error attempting to write structure to export directory!");
    e.printStackTrace();
    }
  return false;
  }

public static List<String> getExportLinesFor(ProcessedStructure struct)
  {
  List<String> lines = new ArrayList<String>();
  Calendar cal = Calendar.getInstance();
  
  lines.add("# Ancient Warfare Structure Template File");
  lines.add("# auto-generated structure file. created on: "+cal.get(cal.MONTH)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR)+ " at: "+cal.get(cal.HOUR_OF_DAY)+":"+cal.get(cal.MINUTE)+":"+cal.get(cal.SECOND));
  lines.add("# Lines beginning with # denote comments");
  lines.add("");
  lines.add("survival="+String.valueOf(struct.survival));
  addTargetBlocks(lines, struct);
  lines.add("");
  lines.add("underground="+String.valueOf(struct.underground));
  lines.add("undergroundMinLevel="+String.valueOf(struct.undergroundMinLevel));
  lines.add("undergroundMaxLevel="+ (255-struct.ySize));
  lines.add("undergroundMaxAirAbove="+String.valueOf(struct.undergroundMaxAirAbove));
  lines.add("minSubmergedDepth="+String.valueOf(struct.minSubmergedDepth));
  lines.add("maxWaterDepth="+String.valueOf(struct.maxWaterDepth));
  lines.add("maxLavaDepth="+String.valueOf(struct.maxLavaDepth));
  lines.add("");
  lines.add("xSize="+String.valueOf(struct.xSize));
  lines.add("ySize="+String.valueOf(struct.ySize));
  lines.add("zSize="+String.valueOf(struct.zSize));
  lines.add("");
  lines.add("verticalOffset="+String.valueOf(struct.verticalOffset));
  lines.add("xOffset="+String.valueOf(struct.xOffset));
  lines.add("zOffset="+String.valueOf(struct.zOffset));
  lines.add("");
  lines.add("maxOverhang="+String.valueOf(struct.getOverHangRaw()));
  lines.add("maxLeveling="+String.valueOf(struct.getLevelingMaxRaw()));
  lines.add("levelingBuffer="+String.valueOf(struct.getLevelingBufferRaw()));
  lines.add("maxVerticalClear="+String.valueOf(struct.getClearingMaxRaw()));
  lines.add("clearingBuffer="+String.valueOf(struct.getClearingBufferRaw()));
  lines.add("preserveWater="+String.valueOf(struct.preserveWater));
  lines.add("preserveLava="+String.valueOf(struct.preserveLava));
  lines.add("preservePlants="+String.valueOf(struct.preservePlants));
  lines.add("preserveBlocks="+String.valueOf(struct.preserveBlocks));
  if(struct.biomesOnlyIn!=null)
    {
    String bio = "biomesOnlyIn=";
    bio = bio + getStringArrayString(struct.biomesOnlyIn);
    lines.add(bio);
    }
  if(struct.biomesNotIn!=null)
    {
    String bio = "biomesNotIn=";
    bio = bio + getStringArrayString(struct.biomesNotIn);
    lines.add(bio);
    }    
  lines.add("");
  lines.add("####BLOCK RULES####");
  addBlockRules(lines, struct);    
  lines.add("");
  lines.add("####SWAP RULESS####");
  addSwapRules(lines, struct);
  lines.add("");  
  lines.add("####VEHICLE RULES####");
  addVehicleRules(lines, struct);
  lines.add("");
  lines.add("####NPC RULES####");
  addNpcRules(lines, struct);
  lines.add("####CIVIC RULES####");
  addCivicRules(lines, struct);
  lines.add("");
  lines.add("####GATE RULES####");
  addGateRules(lines, struct);
  lines.add("");
  lines.add("####ENTITY RULES####");
  addEntityRules(lines, struct);
  lines.add("");
  lines.add("####RESOURCE LIST####");
  addResourceList(lines, struct);
  lines.add("");
  lines.add("####LAYERS####\n");
  addLayers(lines, struct);    
  lines.add("");
  lines.add("####INVENTORIES####\n");
  addInventoryRules(lines, struct);    
  lines.add("");
  return lines;
  }

private static void addGateRules(List<String> lines, ProcessedStructure struct)
  {
  List<String> ruleLines;
  for(GateRule rule : struct.gateRules)
    {
    ruleLines = rule.getRuleLines();
    lines.add("");
    lines.addAll(ruleLines);
    lines.add("");
    }
  }

private static void addInventoryRules(List<String> lines, ProcessedStructure struct)
  {
  List<String> ruleLines;
  for(InventoryRule rule : struct.inventoryRules.values())
    {
    ruleLines = rule.getRuleLines();
    lines.add("");
    lines.addAll(ruleLines);
    lines.add("");
    }
  }

private static void addVehicleRules(List<String> lines, ProcessedStructure struct)
  {
  List<String> ruleLines;
  for(VehicleRule rule : struct.vehicleRules)
    {
    ruleLines = rule.getRuleLines();
    lines.add("");
    lines.addAll(ruleLines);
    lines.add("");
    }
  }

private static void addCivicRules(List<String> lines, ProcessedStructure struct)
  {
  List<String> ruleLines;
  for(CivicRule rule : struct.civicRules)
    {
    ruleLines = rule.getRuleLines();
    lines.add("");
    lines.addAll(ruleLines);
    lines.add("");
    }
  }

private static void addNpcRules(List<String> lines, ProcessedStructure struct)
  {
  List<String> ruleLines;
  for(NpcRule rule : struct.NPCRules)
    {
    ruleLines = rule.getRuleLines();
    lines.add("");
    lines.addAll(ruleLines);
    lines.add("");
    }
  }

private static void addEntityRules(List<String> lines, ProcessedStructure struct)
  {
  List<String> ruleLines;
  for(EntityRule rule : struct.entityRules)
    {
    ruleLines = rule.getRuleLines();
    lines.add("");
    lines.addAll(ruleLines);
    lines.add("");
    }
  }

private static void addTargetBlocks(List<String> lines, ProcessedStructure struct)
  {
  String line = "validTargetblocks=";
  if(struct.validTargetBlocks!=null)
    {
    int[] ints = struct.validTargetBlocks;
    for(int i = 0; i < ints.length; i++)
      {    
      line = line +String.valueOf(ints[i]);
      if(i+1< ints.length)
        {
        line = line + ",";
        }
      }
    }
  else
    {
    line = line + "1,2,3,4,12,13";
    }
  lines.add(line);
  }

private static void addResourceList(List<String> lines, ProcessedStructure struct)
  {
  lines.add("resources:");
  List<IDPairCount> resList = struct.getResourceList();
  Iterator<IDPairCount> it = resList.iterator();
  while(it.hasNext())
    {
    IDPairCount count = it.next();
    lines.add(String.valueOf(count.id+"-"+count.meta+","+count.count));
    }
  lines.add(":endresources");
  }

private static void addSwapRules(List<String> lines, ProcessedStructure struct)
  {
  for(Integer i : struct.swapRules.keySet())
    {
    SwapRule rule = struct.swapRules.get(i);
    List<String> ruleLines= rule.getExportData();
    for(String line : ruleLines)
      {
      lines.add(line);
      }
    lines.add("");
    }
  }

private static void addBlockRules(List<String> lines, ProcessedStructure struct)
  {
  Map<Integer, BlockRule> rules = struct.blockRules;
  for(Integer i : rules.keySet())
    {
    BlockRule rule = rules.get(i);
    lines.add("rule:");
    lines.add("number="+String.valueOf(rule.ruleNumber));
    lines.add("order="+String.valueOf(rule.order));
    lines.add("conditional="+String.valueOf(rule.conditional));
    lines.add("percent="+String.valueOf(rule.baseChance));
    if(rule.blockData!=null)
      {
      String blockLine = "blocks=";
      blockLine = blockLine + getBlockDataArrayString(rule.blockData);
      lines.add(blockLine);
      }
    if(rule.inventoryRules!=null)
      {
      String invLine = "inventory=";
      invLine = invLine + StringTools.getCSVStringForArray(rule.inventoryRules);
      lines.add(invLine);
      }
    if(rule.ruinsSpecialData!=null)
      {
      String ruinsLine = "ruinsspecialdata=";
      ruinsLine = ruinsLine + getStringArrayString(rule.ruinsSpecialData);
      lines.add(ruinsLine);
      }
    if(rule.spawnerTypes!=null)
      {
      String spawnerLine = "spawner=";
      spawnerLine = spawnerLine + getStringArrayString(rule.spawnerTypes);
      lines.add(spawnerLine);
      }
    if(rule.preserveBlocks==true)
      {
      lines.add("preserveblocks=true");
      if(rule.preservedBlocks!=null)
        {
        String presLine = "preservedblocks=";
        presLine = presLine + getBlockDataArrayString(rule.preservedBlocks);
        lines.add(presLine);
        }
      }    
    if(rule.preserveLava)
      {
      lines.add("preserveLava=true");
      }
    if(rule.preservePlants)
      {
      lines.add("preservePlants=true");
      }
    if(rule.preserveWater)
      {
      lines.add("preserveWater=true");
      }    
    lines.add(":endrule");
    lines.add("");
    } 
  }

private static String getIntArrayString(int[] ints)
  {
  if(ints==null)
    {
    return "";
    }
  String line = "";
  for(int i = 0; i < ints.length; i++)
    {
    line = line + (String.valueOf(ints[i]));
    if(i+1< ints.length)
      {
      line = line +(",");
      }
    }
  return line;
  }

private static String getBlockDataArrayString(BlockData [] datas)
  {
  if(datas==null)
    {
    return "";
    }
  String line = "";
  for(int i = 0; i < datas.length; i++)
    {
    BlockData data = datas[i];
    if(data==null)
      {
      continue;
      }
    if(i>0)
      {
      line = line +(",");
      }
    line = line +(String.valueOf(data.id+"-"+data.meta));
    }  
  return line;
  }

private static void addLayers(List<String> lines, ProcessedStructure struct)
  {
  for(int y = 0; y< struct.ySize; y++)
    {
    addSingleLayer(lines, struct, y);
    }
  }

private static void addSingleLayer(List<String> lines, ProcessedStructure struct, int layerNumber)
  {
  
  lines.add("layer:");
  for(int z = 0; z <struct.structure[0][0].length; z++)
    {
    String layerLine = "";
    for(int x = 0; x<struct.structure.length; x++)
      {
      layerLine = layerLine + String.valueOf(struct.structure[x][layerNumber][z]);
      if(x < struct.structure.length-1)
        {
        layerLine = layerLine + ",";
        }
      }
    lines.add(layerLine);
    }  
  lines.add(":endlayer");
  lines.add(""); 
  }

private static void writeLayers(FileWriter writer, ProcessedStructure struct) throws IOException
  {
  for(int y = 0; y< struct.ySize; y++)
    {
    writeSingleLayer(writer, struct, y);
    }
  }

private static void writeSingleLayer(FileWriter writer, ProcessedStructure struct, int layerNumber) throws IOException
  {
  writer.write("layer:\n");
  for(int z = 0; z <struct.structure[0][0].length; z++)
    {
    for(int x = 0; x<struct.structure.length; x++)
      {
      writer.write(String.valueOf(struct.structure[x][layerNumber][z]));
      if(x < struct.structure.length-1)
        {
        writer.write(",");
        }
      }
    writer.write("\n");
    }  
  writer.write(":endlayer\n");
  writer.write("\n"); 
  }

private static String getStringArrayString(String [] split)
  {
  if(split==null)
    {
    return "";
    }
  String line ="";
  for(int i = 0; i < split.length; i++)
    {
    line = line + (split[i]);
    if(i+1<split.length)
      {
      line = line +(",");
      }    
    }
  return line;
  }

}
