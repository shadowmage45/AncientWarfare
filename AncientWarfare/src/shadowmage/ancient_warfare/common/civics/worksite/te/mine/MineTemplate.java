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
package shadowmage.ancient_warfare.common.civics.worksite.te.mine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class MineTemplate
{


int xSize;
int ySize;
int zSize;

MineAction[] actionTemplate;

public MineTemplate(int xSize, int ySize, int zSize)
  {
  this.xSize = xSize;
  this.ySize = ySize;
  this.zSize = zSize;
  actionTemplate = new MineAction[xSize*ySize*zSize];
  }

public MineAction getAction(int x, int y, int z)
  {
  int index = zSize*xSize*y + xSize*z +x;
  if(index>=0 && index<this.actionTemplate.length)
    {
    return actionTemplate[index];
    }
  return null;
  }

public void setAction(int x, int y, int z, MineAction action)
  {
  int index = zSize*xSize*y + xSize*z +x;
  if(index>=0 && index<this.actionTemplate.length)
    {
    actionTemplate[index] = action;
    }
  }

public void parseMineTemplate(List<String> lines)
  {
  Iterator<String> it = lines.iterator();
  List<String> layerLines = new ArrayList<String>();
  String line;
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("size"))
      {
      int[] size = StringTools.safeParseIntArray("=", line);
      if(size.length>=2)
        {
        xSize = size[0];
        ySize = size[1];
        zSize = size[2];
        }
      }
    else if(line.toLowerCase().startsWith("layer:"))
      {
      int layerNum = StringTools.safeParseInt(":", line);
      layerLines.clear();
      layerLines.add(line);
      while(it.hasNext())
        {
        line = it.next();
        layerLines.add(line);
        if(line.toLowerCase().startsWith("endlayer:"))
          {
          break;
          }        
        }
      parseLevel(layerLines, layerNum);
      }    
    }
  }

public void parseLevel(List<String> lines, int levelNum)
  {
  Iterator<String> it = lines.iterator();
  String line;
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("layer:"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith(":endlayer"))
      {
      break;
      }
    /**
     * split the line into its triplet-parts
     */
    String[] splits = StringTools.parseStringArray(line);
    }
  }

private class MineAction
{
int order;
int meta;
TargetType action;
private MineAction(TargetType t, int o, int m)
  {
  this.action = t;
  this.meta = m;
  this.order = o;
  }
}

}
