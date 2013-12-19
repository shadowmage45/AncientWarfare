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
package shadowmage.ancient_structures.common.template.save;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_structures.AWStructures;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureValidationSettings;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class TemplateExporter
{


public static void exportTo(StructureTemplate template, File directory)
  {
  File exportFile = new File(directory, template.name+"."+AWStructureStatics.templateExtension);
  if(!exportFile.exists())
    {
    try
      {
      exportFile.createNewFile();
      } 
    catch (IOException e)
      {
      AWLog.logError("Could not export template..could not create file : "+exportFile.getAbsolutePath());
      e.printStackTrace();
      return;
      }
    }  
  BufferedWriter writer = null;
  try
    {
    writer = new BufferedWriter(new FileWriter(exportFile));
    
    writeHeader(template, writer);    
    writeValidationSettings(template.getValidationSettings(), writer);    
    writeLayers(template, writer);
    
    writer.write("#### RULES ####");
    writer.newLine();
    TemplateRule[] templateRules = template.getTemplateRules();
    for(TemplateRule rule : templateRules)
      {
      writeRuleLines(rule, writer);
      }
    } 
  catch (IOException e)
    {
    AWLog.logError("Could not export template..could not create file : "+exportFile.getAbsolutePath());
    e.printStackTrace();
    }
  finally
    {
    if(writer!=null)
      {
      try
        {
        writer.close();
        } 
      catch (IOException e)
        {
        AWLog.logError("Could not export template..could not close file : "+exportFile.getAbsolutePath());
        e.printStackTrace();
        }
      }
    }
  }

private static void writeValidationSettings(StructureValidationSettings settings, BufferedWriter writer) throws IOException
  {
  writer.write("#### VALIDATION ####");    
  writer.newLine(); 
  settings.writeSettings(writer);  
  writer.newLine();
  }

private static void writeHeader(StructureTemplate template, BufferedWriter writer) throws IOException
  {
  Calendar cal = Calendar.getInstance();
  writer.write("# Ancient Warfare Structure Template File");
  writer.newLine();
  writer.write("# auto-generated structure file. created on: "+(cal.get(cal.MONTH)+1)+"/"+cal.get(cal.DAY_OF_MONTH)+"/"+cal.get(cal.YEAR)+ " at: "+cal.get(cal.HOUR_OF_DAY)+":"+cal.get(cal.MINUTE)+":"+cal.get(cal.SECOND));
  writer.newLine();
  writer.write("# Lines beginning with # denote comments");
  writer.newLine();
  writer.newLine();
  writer.write("header:");
  writer.newLine();
  writer.write("version=2.0");
  writer.newLine();    
  writer.write("name="+template.name);
  writer.newLine();
  writer.write("size="+template.xSize+","+template.ySize+","+template.zSize);
  writer.newLine();
  writer.write("offset="+template.xOffset+","+template.yOffset+","+template.zOffset);
  writer.newLine();
  writer.write(":endheader");
  writer.newLine();
  writer.newLine();
  }

private static void writeLayers(StructureTemplate template, BufferedWriter writer) throws IOException
  { 
  writer.write("#### LAYERS ####");
  writer.newLine();
  for(int y = 0; y< template.ySize; y++)
    {
    writer.write("layer: "+y);
    writer.newLine();
    for(int z = 0 ; z<template.zSize; z++)
      {
      for(int x = 0; x<template.xSize; x++)
        {
        short data = template.getTemplateData()[template.getIndex(x, y, z, template.xSize, template.ySize, template.zSize)];          
        writer.write(String.valueOf(data));
        if(x<template.xSize-1)
          {
          writer.write(",");
          }
        }
      writer.newLine();
      }
    writer.write(":endlayer");
    writer.newLine();
    }
  writer.newLine();
  }

public final static void writeRuleLines(TemplateRule rule, BufferedWriter out) throws IOException
  {
  if(rule==null)
    {
    return;
    }
  String id = AWStructures.instance.pluginManager.getPluginNameFor(rule.getClass());
  if(id==null)
    {
    return;
    }
  out.write("rule:");
  out.newLine();
  out.write("plugin="+id);
  out.newLine();
  out.write("number="+rule.ruleNumber);
  out.newLine();
  out.write("data:");
  out.newLine();
  rule.writeRuleData(out);
  out.write(":enddata");
  out.newLine();
  out.write(":endrule");
  out.newLine();
  out.newLine();
  }


}
