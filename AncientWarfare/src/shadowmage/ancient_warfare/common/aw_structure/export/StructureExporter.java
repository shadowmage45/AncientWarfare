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
import java.sql.Struct;
import java.util.Calendar;
import java.util.Date;

import com.google.common.collect.Lists;

import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockData;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockDataManager;
import shadowmage.ancient_warfare.common.aw_structure.data.ScannedStructureNormalized;
import shadowmage.ancient_warfare.common.aw_structure.data.ScannedStructureRaw;

public class StructureExporter
{


public static void writeStructureToFile(ScannedStructureNormalized struct, String name)
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
      }
    }
  FileWriter writer = null;
  try
    {
    writer = new FileWriter(outputFile);
    
    Date date = new Date(System.currentTimeMillis());
    Calendar cal = Calendar.getInstance();
    
    writer.write("# Ancient Warfare Structure Template File\n");
    writer.write("# auto-generated structure file. created on: "+cal.get(cal.MONTH)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR)+ " at: "+cal.get(cal.HOUR_OF_DAY)+":"+cal.get(cal.MINUTE)+":"+cal.get(cal.SECOND)+"\n");
    writer.write("# Lines beginning with # denote comments\n");
    writer.write("\n");
    writer.write("name="+String.valueOf(System.currentTimeMillis())+"\n");
    writer.write("\n");
    writer.write("unique=false\n");
    writer.write("chunkDistance=0\n");
    writer.write("chunkAttempts=1\n");
    writer.write("\n");
    writer.write("underground=false\n");
    writer.write("undergroundMinLevel=1\n");
    writer.write("undergroundMaxLevel="+ (255-struct.ySize)+"\n");
    writer.write("undergroundMaxAirAbove=0\n");
    writer.write("undergroundAllowPartial=false\n");
    writer.write("\n");
    writer.write("xSize="+struct.xSize+"\n");
    writer.write("ySize="+struct.ySize+"\n");
    writer.write("zSize="+struct.zSize+"\n");
    writer.write("\n");
    writer.write("verticalOffset="+struct.buildKey.y+"\n");
    writer.write("xOffset="+struct.buildKey.x+"\n");
    writer.write("zOffset="+struct.buildKey.z+"\n");
    writer.write("\n");
    writer.write("maxOverhang=0\n");
    writer.write("maxLeveling=0\n");
    writer.write("levelingBuffer=0\n");
    writer.write("maxVerticalClear=0\n");
    writer.write("clearingBuffer=0\n");
    writer.write("preserveWater=false\n");
    writer.write("preserveLava=false\n");
    writer.write("preservePlants=false\n");
    writer.write("preserveBlocks=false\n");    
    writer.write("\n");
    writer.write("####BLOCK RULES####\n");
    //writeAirRule(writer);
    writeBlockRules(writer, struct);    
    writer.write("\n");
    writer.write("####LAYERS####\n");
    writeLayers(writer, struct);
    
    writer.close();
    } 
  catch (IOException e)
    {
    Config.logError("Severe error attempting to write structure to export directory!");
    e.printStackTrace();
    }
  }

private static void writeAirRule(FileWriter writer) throws IOException
  {
  writeSingleBlockRule(writer,0,0,0);
  }

private static void writeBlockRules(FileWriter writer, ScannedStructureNormalized struct) throws IOException
  {
  BlockData[] datas = findAllBlockTypes(struct);
  for(int i = 0; i < datas.length; i++)
    {
    writeSingleBlockRule(writer, i, datas[i].id, datas[i].meta);
    }
  }

private static BlockData[] findAllBlockTypes(ScannedStructureRaw struct)
  {  
  BlockData[] datas = new BlockData[struct.blockIDs.size()];
  for(int i = 0; i <struct.blockIDs.size(); i++)
    {
    datas[i]=struct.blockIDs.get(i);
    }
  return datas;
  }

private static void writeSingleBlockRule(FileWriter writer, int ruleNum, int id, int meta) throws IOException
  {
  writer.write("rule:\n");
  writer.write("number="+ruleNum+"\n");
  writer.write("order="+BlockDataManager.getBlockPriority(id, meta)+"\n");
  writer.write("conditional=0\n");
  writer.write("percent=100\n");
  writer.write("blocks="+id+"-"+meta+"\n");
  writer.write(":endrule\n");
  writer.write("\n");
  }

private static void writeLayers(FileWriter writer, ScannedStructureNormalized struct) throws IOException
  {
  for(int y = 0; y< struct.ySize; y++)
    {
    writeSingleLayer(writer, struct, y);
    }
  }

private static void writeSingleLayer(FileWriter writer, ScannedStructureNormalized struct, int layerNumber) throws IOException
  {
  writer.write("layer:\n");
  for(int z = 0; z <struct.allBlocks[0][0].length; z++)
    {
    for(int x = 0; x<struct.allBlocks.length; x++)
      {
      BlockData data = struct.allBlocks[x][layerNumber][z];
      int ruleNum = struct.getRuleForBlock(data.id, data.meta);
      writer.write(String.valueOf(ruleNum));
      if(x < struct.allBlocks.length-1)
        {
        writer.write(",");
        }
      }
    writer.write("\n");
    }  
  writer.write(":endlayer\n");
  writer.write("\n");
  }

}
