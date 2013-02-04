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
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.data.BlockData;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.structures.data.rules.SwapRule;
import shadowmage.ancient_warfare.common.utils.IDPairCount;

public class StructureExporter
{


public static boolean writeStructureToFile(ProcessedStructure struct, String name)
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
  else
    {
    Config.logError("Exporting would overwrite structure with name: "+name+"  Operation aborted.  Please choose a different name before rescanning and exporting");
    return false;    
    }
  FileWriter writer = null;
  try
    {
    writer = new FileWriter(outputFile);
    
    Calendar cal = Calendar.getInstance();
    
    writer.write("# Ancient Warfare Structure Template File\n");
    writer.write("# auto-generated structure file. created on: "+cal.get(cal.MONTH)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR)+ " at: "+cal.get(cal.HOUR_OF_DAY)+":"+cal.get(cal.MINUTE)+":"+cal.get(cal.SECOND)+"\n");
    writer.write("# Lines beginning with # denote comments\n");
    writer.write("\n");
    writer.write("name="+struct.name+"\n");
    writer.write("worldgen="+String.valueOf(struct.worldGen)+"\n");
    writer.write("creative="+String.valueOf(struct.creative)+"\n");
    writer.write("survival="+String.valueOf(struct.survival)+"\n");
    writer.write("validTargetblocks=");
    if(struct.validTargetBlocks!=null)
      {
      writeIntArray(writer, struct.validTargetBlocks);
      writer.write("\n");
      }
    else
      {
      writer.write("1,2,3,4,12,13\n");
      }
    writer.write("\n");
    writer.write("unique="+String.valueOf(struct.unique)+"\n");
    writer.write("chunkDistance="+String.valueOf(struct.structureValue)+"\n");
    writer.write("chunkAttempts="+String.valueOf(struct.chunkAttempts)+"\n");
    writer.write("\n");
    writer.write("underground="+String.valueOf(struct.underground)+"\n");
    writer.write("undergroundMinLevel="+String.valueOf(struct.undergroundMinLevel)+"\n");
    writer.write("undergroundMaxLevel="+ (255-struct.ySize)+"\n");
    writer.write("undergroundMaxAirAbove="+String.valueOf(struct.undergroundMaxAirAbove)+"\n");
    writer.write("minSubmergedDepth="+String.valueOf(struct.minSubmergedDepth)+"\n");
    writer.write("maxWaterDepth="+String.valueOf(struct.maxWaterDepth)+"\n");
    writer.write("maxLavaDepth="+String.valueOf(struct.maxLavaDepth)+"\n");
    writer.write("\n");
    writer.write("xSize="+String.valueOf(struct.xSize)+"\n");
    writer.write("ySize="+String.valueOf(struct.ySize)+"\n");
    writer.write("zSize="+String.valueOf(struct.zSize)+"\n");
    writer.write("\n");
    writer.write("verticalOffset="+String.valueOf(struct.verticalOffset)+"\n");
    writer.write("xOffset="+String.valueOf(struct.xOffset)+"\n");
    writer.write("zOffset="+String.valueOf(struct.zOffset)+"\n");
    writer.write("\n");
    writer.write("maxOverhang="+String.valueOf(struct.maxOverhang)+"\n");
    writer.write("maxLeveling="+String.valueOf(struct.maxLeveling)+"\n");
    writer.write("levelingBuffer="+String.valueOf(struct.levelingBuffer)+"\n");
    writer.write("maxVerticalClear="+String.valueOf(struct.maxVerticalClear)+"\n");
    writer.write("clearingBuffer="+String.valueOf(struct.clearingBuffer)+"\n");
    writer.write("preserveWater="+String.valueOf(struct.preserveWater)+"\n");
    writer.write("preserveLava="+String.valueOf(struct.preserveLava)+"\n");
    writer.write("preservePlants="+String.valueOf(struct.preservePlants)+"\n");
    writer.write("preserveBlocks="+String.valueOf(struct.preserveBlocks)+"\n");
    if(struct.biomesOnlyIn!=null)
      {
      writer.write("biomesOnlyIn=");
      writeStringArray(writer, struct.biomesOnlyIn);
      writer.write("\n");
      }
    if(struct.biomesNotIn!=null)
      {
      writer.write("biomesNotIn=");
      writeStringArray(writer, struct.biomesNotIn);
      writer.write("\n");
      }    
    writer.write("\n");
    writer.write("####BLOCK RULES####\n");
    writeBlockRules(writer, struct);    
    writer.write("\n");
    writer.write("####SWAP RULESS####\n");
    writeSwapRules(writer, struct);
    writer.write("\n");
    //TODO
    writer.write("####VEHICLE RULES####\n");
    writer.write("\n");
    writer.write("####NPC RULES####\n");
    writer.write("\n");  
    writer.write("####GATE RULES####\n");
    writer.write("\n");    
    //END TODO
    writer.write("####RESOURCE LIST####\n");
    writeResourceList(writer, struct);
    writer.write("\n");
    writer.write("####LAYERS####\n");
    writeLayers(writer, struct);    
    writer.write("\n");
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

private static void writeResourceList(FileWriter writer, ProcessedStructure struct) throws IOException
  {
  writer.write("resources:\n");
  List<IDPairCount> resList = struct.getResourceList();
  Iterator<IDPairCount> it = resList.iterator();
  while(it.hasNext())
    {
    IDPairCount count = it.next();
    writer.write(String.valueOf(count.id+"-"+count.meta+","+count.count+"\n"));
    }
  writer.write(":endresources\n");
  }

private static void writeSwapRules(FileWriter writer, ProcessedStructure struct) throws IOException
  {
  for(Integer i : struct.swapRules.keySet())
    {
    SwapRule rule = struct.swapRules.get(i);
    List<String> lines= rule.getExportData();
    for(String line : lines)
      {
      writer.write(line);
      }
    }
  }

private static void writeBlockRules(FileWriter writer, ProcessedStructure struct) throws IOException
  {
  Map<Integer, BlockRule> rules = struct.blockRules;
  for(Integer i : rules.keySet())
    {
    BlockRule rule = rules.get(i);
    writer.write("rule:\n");
    writer.write("number="+String.valueOf(rule.ruleNumber)+"\n");
    writer.write("order="+String.valueOf(rule.order)+"\n");
    writer.write("conditional="+String.valueOf(rule.conditional)+"\n");
    writer.write("percent="+String.valueOf(rule.baseChance)+"\n"); 
    if(rule.gateNum>-1)
      {
      writer.write("gate="+String.valueOf(rule.gateNum)+"\n");
      }
    if(rule.blockData!=null)
      {
      writer.write("blocks=");
      writeBlockDataArray(writer, rule.blockData);
      writer.write("\n");
      }
    if(rule.ruinsSpecialData!=null)
      {
      writer.write("ruinsspecialdata=");
      writeStringArray(writer, rule.ruinsSpecialData);
      writer.write("\n");
      }
    if(rule.spawnerTypes!=null)
      {
      writer.write("spawner=");
      writeStringArray(writer, rule.spawnerTypes);
      writer.write("\n");
      }
    if(rule.preserveBlocks==true)
      {
      writer.write("preserveblocks=true\n");
      if(rule.preservedBlocks!=null)
        {
        writer.write("preservedblocks=");
        writeBlockDataArray(writer, rule.preservedBlocks);
        writer.write("\n");
        }
      }    
    if(rule.preserveLava)
      {
      writer.write("preserveLava=true\n");
      }
    if(rule.preservePlants)
      {
      writer.write("preservePlants=true\n");
      }
    if(rule.preserveWater)
      {
      writer.write("preserveWater=true\n");
      }
    if(rule.orientation!=0)
      {
      writer.write("orientation="+String.valueOf(rule.orientation)+"\n");
      }
    if(rule.vehicles!=null)
      {
      writer.write("vehicles=");
      writeIntArray(writer, rule.vehicles);
      writer.write("\n");
      }
    if(rule.npcs!=null)
      {
      writer.write("npcs=");
      writeIntArray(writer, rule.vehicles);
      writer.write("\n");
      }
    writer.write(":endrule\n");
    writer.write("\n");
    } 
  }

private static void writeIntArray(FileWriter writer, int[] ints) throws IOException
  {
  if(ints==null)
    {
    return;
    }
  for(int i = 0; i < ints.length; i++)
    {
    writer.write(String.valueOf(ints[i]));
    if(i+1< ints.length)
      {
      writer.write(",");
      }
    }
  }

private static void writeBlockDataArray(FileWriter writer, BlockData[] datas) throws IOException
  {
  if(datas==null)
    {
    return;
    }  
  for(int i = 0; i < datas.length; i++)
    {
    BlockData data = datas[i];
    if(data==null)
      {
      continue;
      }
    if(i>0)
      {
      writer.write(",");
      }
    writer.write(String.valueOf(data.id+"-"+data.meta));
    }  
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

private static void writeStringArray(FileWriter writer, String[] split) throws IOException
  {
  if(split==null)
    {
    return;
    }
  for(int i = 0; i < split.length; i++)
    {
    writer.write(split[i]);
    if(i+1<split.length)
      {
      writer.write(",");
      }    
    }
  }

}
