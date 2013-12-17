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
  return  ruleIndex >= 0 && ruleIndex < templateRules.length ? templateRules[index] : null;
  }

public static int getIndex(int x, int y, int z, int xSize, int ySize, int zSize)
  {
  return (y * ySize * zSize) + (z * ySize) + x; 
  }

@Override
public String toString()
  {
  StringBuilder b = new StringBuilder();
  int index;
  for(int y = 0; y < ySize; y++)
    {
    b.append("\n\n level: ").append(y).append("\n");
    for(int z = 0; z < zSize; z++)
      {
      for(int x = 0; x < xSize; x++)
        {
        index = y*ySize*zSize + z*zSize + x;
        b.append(templateData[index]);
        if(x<xSize-1)
          {
          b.append(",");
          }
        }
      b.append("\n");
      }
    }
  return b.toString();
  }

}
