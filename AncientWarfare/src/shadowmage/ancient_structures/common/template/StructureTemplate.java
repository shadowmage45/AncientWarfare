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
package shadowmage.ancient_structures.common.template;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_structures.AWStructures;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.template.build.StructureValidationSettings;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;


public class StructureTemplate
{

/**
 * base datas
 */
public final String name;
public final int xSize, ySize, zSize;
public final int xOffset, yOffset, zOffset;

/**
 * stored template data
 */
private TemplateRule[] templateRules;
private short[] templateData;
private String[] templateLines;

/**
 * world generation placement validation settings, optional, should only exist if a world-gen entry was loaded/parsed for this structure
 */
private StructureValidationSettings validationSettings;

public StructureTemplate(String name, int xSize, int ySize, int zSize, int xOffset, int yOffset, int zOffset)
  {
  this.name = name;
  this.xSize = xSize;
  this.ySize = ySize;
  this.zSize = zSize;
  this.xOffset = xOffset;
  this.yOffset = yOffset;
  this.zOffset = zOffset;  
  }

public TemplateRule[] getTemplateRules()
  {
  return templateRules;
  }

public short[] getTemplateData()
  {
  return templateData;
  }

public String[] getTemplateLines()
  {
  return templateLines;
  }

public StructureValidationSettings getValidationSettings()
  {
  return validationSettings;
  }

public void setTemplateLines(String[] lines)
  {
  this.templateLines = lines;
  }

public void setRuleArray(TemplateRule[] rules)
  {
  this.templateRules = rules;
  }

public void setTemplateData(short[] datas)
  {
  this.templateData = datas;
  }

public void setValidationSettings(StructureValidationSettings settings)
  {
  this.validationSettings = settings;
  }

public TemplateRule getRuleAt(int x, int y, int z)
  {
  int index = getIndex(x, y, z, xSize, ySize, zSize);
  int ruleIndex = index >=0 && index < templateData.length ? templateData[index]: -1;
  return ruleIndex >= 0 && ruleIndex < templateRules.length ? templateRules[ruleIndex] : null;
  }

public static int getIndex(int x, int y, int z, int xSize, int ySize, int zSize)
  {
  return (y * xSize * zSize) + (z * xSize) + x; 
  }

@Override
public String toString()
  {
  StringBuilder b = new StringBuilder();
  int index;
  b.append("name: ").append(name).append("\n");
  b.append("size: ").append(xSize).append(", ").append(ySize).append(", ").append(zSize).append("\n");
  b.append("buildKey: ").append(xOffset).append(", ").append(yOffset).append(", ").append(zOffset).append("\n");
  b.append("levels:");
  for(int y = 0; y < ySize; y++)
    {
    b.append("\n\nlevel: ").append(y).append("\n");
    for(int z = 0; z < zSize; z++)
      {
      for(int x = 0; x < xSize; x++)
        {
        index = getIndex( x, y, z, xSize, ySize, zSize);
        b.append(templateData[index]);
        if(x<xSize-1)
          {
          b.append(",");
          }
        }
      b.append("\n");
      }
    }
  b.append("\n\nrules:\n");
  String[] lines;
  for(TemplateRule rule : this.templateRules)
    {
	  if(rule==null){continue;}	 
	  b.append("rule: ").append(rule.ruleNumber).append("\n");
    lines = rule.getRuleLines();
    for(String st : lines)
      {
      b.append(st).append("\n");
      }
    b.append("\n");
    }
  return b.toString();
  }

public void exportTo(File directory)
  {
  File exportFile = new File(directory, name+"."+AWStructureStatics.templateExtension);
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
    writer.write("name: "+name);
    writer.newLine();
    writer.write("size="+xSize+","+ySize+","+zSize);
    writer.newLine();
    writer.write("offset="+xOffset+","+yOffset+","+zOffset);
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
        for(int x = 0; x<xSize; x++)
          {
          short data = templateData[getIndex(x, y, z, xSize, ySize, zSize)];
          AWLog.logDebug("export data: "+data);
          writer.write(String.valueOf(data));
          if(x<xSize-1)
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
    writer.newLine();
    writer.write("#### RULES ####");
    writer.newLine();
    
    for(TemplateRule rule : this.templateRules)
      {
      AWStructures.instance.pluginManager.writeRuleLines(rule, writer);
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

}
