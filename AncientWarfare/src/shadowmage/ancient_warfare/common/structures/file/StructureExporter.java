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
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.data.BlockData;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;

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
    writer.write("structureWeight="+struct.structureWeight+"\n");
    writer.write("\n");
    writer.write("unique="+String.valueOf(struct.unique)+"\n");
    writer.write("chunkDistance="+struct.chunkDistance+"\n");
    writer.write("chunkAttempts="+struct.chunkAttempts+"\n");
    writer.write("\n");
    writer.write("underground="+String.valueOf(struct.underground)+"\n");
    writer.write("undergroundMinLevel="+struct.undergroundMinLevel+"\n");
    writer.write("undergroundMaxLevel="+ (255-struct.ySize)+"\n");
    writer.write("undergroundMaxAirAbove="+struct.undergroundMaxAirAbove+"\n");
    writer.write("undergroundAllowPartial="+String.valueOf(struct.undergroundAllowPartial)+"\n");
    writer.write("\n");
    writer.write("xSize="+struct.xSize+"\n");
    writer.write("ySize="+struct.ySize+"\n");
    writer.write("zSize="+struct.zSize+"\n");
    writer.write("\n");
    writer.write("verticalOffset="+struct.verticalOffset+"\n");
    writer.write("xOffset="+struct.xOffset+"\n");
    writer.write("zOffset="+struct.zOffset+"\n");
    writer.write("\n");
    writer.write("maxOverhang="+struct.maxOverhang+"\n");
    writer.write("maxLeveling="+struct.maxLeveling+"\n");
    writer.write("levelingBuffer="+struct.levelingBuffer+"\n");
    writer.write("maxVerticalClear="+struct.maxVerticalClear+"\n");
    writer.write("clearingBuffer="+struct.clearingBuffer+"\n");
    writer.write("preserveWater="+struct.preserveWater+"\n");
    writer.write("preserveLava="+struct.preserveLava+"\n");
    writer.write("preservePlants="+struct.preservePlants+"\n");
    writer.write("preserveBlocks="+struct.preserveBlocks+"\n"); 
    writer.write("biomesOnlyIn=");
    writeStringArray(writer, struct.biomesOnlyIn);
    writer.write("\n");
    writer.write("biomesNotIn=");
    writeStringArray(writer, struct.biomesNotIn);
    writer.write("\n");    
    writer.write("\n");
    writer.write("####BLOCK RULES####\n");
    writeBlockRules(writer, struct);    
    writer.write("\n");
    //TODO
    writer.write("####VEHICLE RULES####\n");
    writer.write("\n");
    writer.write("####NPC RULES####\n");
    writer.write("\n");
    //END TODO
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

private static void writeBlockRules(FileWriter writer, ProcessedStructure struct) throws IOException
  {
  List<BlockRule> rules = struct.blockRules;
  for(BlockRule rule : rules)
    { 
    writer.write("rule:\n");
    writer.write("number="+rule.ruleNumber+"\n");
    writer.write("order="+rule.order+"\n");
    writer.write("conditional="+rule.conditional+"\n");
    writer.write("percent="+rule.baseChance+"\n"); 
    if(rule.blockData!=null)
      {
      writer.write("blocks=");
      writeBlockDataArray(writer, rule.blockData);
      writer.write("\n");
      }
    if(rule.ruinsSpecialData!=null)
      {
      writer.write("ruinsSpecialData=");
      writeStringArray(writer, rule.ruinsSpecialData);
      writer.write("\n");
      }
    if(rule.preserveBlocks==true)
      {
      writer.write("preserveBlocks=true\n");
      if(rule.preservedBlocks!=null)
        {
        writer.write("preservedBlocks=");
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
      writer.write("orientation="+rule.orientation+"\n");
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
    writer.write(ints[i]);
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
    writer.write(data.id+"-"+data.meta);
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
      writer.write(struct.structure[x][layerNumber][z]);
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
