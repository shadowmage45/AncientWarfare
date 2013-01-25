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
package shadowmage.ancient_warfare.common.aw_structure.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.data.rules.BlockRule;

public class StructureExporterRuins
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
    Config.logError("Exporting would overwrite structure with name: "+name+"  Operation aborted.  Please choose a different name before attempting to export again.");
    return false;    
    }
  FileWriter writer = null;
  try
    {
    writer = new FileWriter(outputFile);
    
    Calendar cal = Calendar.getInstance();
    
    writer.write("# Ruins Structure Template File\n");
    writer.write("# Auto-generated structure file. created on: "+cal.get(cal.MONTH)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR)+ " at: "+cal.get(cal.HOUR_OF_DAY)+":"+cal.get(cal.MINUTE)+":"+cal.get(cal.SECOND)+"\n");
    writer.write("# Template was generated with the Ancient Warfare structure exporter");
    writer.write("# Lines beginning with # denote comments\n");
    writer.write("\n");    
    writer.write("weight="+String.valueOf(struct.structureWeight)+"\n");
    writer.write("unique="+getIntForBoolean(struct.unique)+"\n");
    writeAcceptableBlocks(writer, struct);
    writer.write("\n");   
    writer.write("dimensions="+struct.ySize+","+struct.xSize+","+struct.zSize+"\n");
    writer.write("\n");
    writer.write("embed_into_distance="+struct.verticalOffset+"\n");
    writer.write("\n");
    writer.write("allowable_overhang="+struct.maxOverhang+"\n");
    writer.write("max_leveling="+struct.maxLeveling+"\n");
    writer.write("levelingBuffer="+struct.levelingBuffer+"\n");
    writer.write("max_cut_in="+struct.maxVerticalClear+"\n");
    writer.write("cut_in_buffer="+struct.clearingBuffer+"\n");
    writer.write("preserve_water="+getIntForBoolean(struct.preserveWater)+"\n");
    writer.write("preserve_lava="+getIntForBoolean(struct.preserveLava)+"\n");
    writer.write("preserve_plants="+getIntForBoolean(struct.preservePlants)+"\n");
    writer.write("\n");
    writer.write("####BLOCK RULES####\n");
    writeBlockRules(writer, struct);    
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

private static void writeAcceptableBlocks(FileWriter writer, ProcessedStructure struct) throws IOException
  {
  writer.write("acceptable_target_blocks=");
  writeIntArray(writer, struct.validTargetBlocks);
  writer.write("\n");
  }

private static void writeBlockRules(FileWriter writer, ProcessedStructure struct) throws IOException
  {
  int ruleNum = 1;
  for(BlockRule rule : struct.blockRules)
    {
    writeSingleBlockRule(writer, ruleNum, rule);
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

private static void writeSingleBlockRule(FileWriter writer, int ruleNum, BlockRule rule) throws IOException
  {
  writer.write("rule"+ruleNum+"=");  
  writer.write(rule.conditional+",");
  writer.write(rule.baseChance+",");
  
  int totalElements = rule.blockData.length + rule.ruinsSpecialData.length;
  int totalWritten = 0;
  for(int i = 0; i < rule.blockData.length; i++)
    {   
    writer.write(rule.blockData[i].id+"-"+rule.blockData[i].meta);
    totalWritten++;
    if(totalWritten<totalElements)
      {
      writer.write(",");
      }
    }
  for(int i = 0; i <rule.ruinsSpecialData.length; i++)
    {
    writer.write(rule.ruinsSpecialData[i]);
    totalWritten++;
    if(totalWritten<totalElements)
      {
      writer.write(",");
      }
    }  
  writer.write("\n");
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
  writer.write("layer\n");
  for(int z = 0; z <struct.structure[0][0].length; z++)
    {
    for(int x = 0; x<struct.structure.length; x++)
      {
      if(x>0)
        {
        writer.write(",");
        }
      int rule = struct.structure[x][layerNumber][z];      
      writer.write(String.valueOf(rule));      
      }
    writer.write("\n");
    }  
  writer.write("endlayer\n");
  writer.write("\n");
  }

private static int getIntForBoolean(boolean in)
  {
  return in == true ? 1: 0;
  }

}
