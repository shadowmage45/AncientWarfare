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

import shadowmage.ancient_structures.common.template.rule.TemplateRule;


public class StructureTemplate
{

public final String name;
private TemplateRule[] templateRules;
private short[] templateData;
private String[] templateLines;
int xSize, ySize, zSize;

public StructureTemplate(String name)
  {
  this.name = name;
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

public TemplateRule getRuleAt(int x, int y, int z)
  {
  int index = y * ySize * zSize + z * zSize + x;
  int ruleIndex = index >=0 && index < templateData.length ? templateData[index]: -1;
  return  ruleIndex >= 0 && ruleIndex < templateRules.length ? templateRules[index] : null;
  }

}
