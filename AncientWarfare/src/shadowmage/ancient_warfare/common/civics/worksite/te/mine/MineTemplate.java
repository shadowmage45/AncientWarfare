/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.civics.worksite.te.mine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class MineTemplate
{
int xSize;
int ySize;
int zSize;

MineActionPoint[] actionTemplate;

public MineTemplate(int xSize, int ySize, int zSize)
  {
  this.xSize = xSize;
  this.ySize = ySize;
  this.zSize = zSize;
  actionTemplate = new MineActionPoint[xSize*ySize*zSize];
  }

public MineTemplate(List<String> templateLines)
  {
  this.parseMineTemplate(templateLines);
  }

public MineActionPoint getAction(int x, int y, int z)
  {
  int index = zSize*xSize*y + xSize*z +x;
  if(actionTemplate!=null && index>=0 && index<this.actionTemplate.length)
    {
    return actionTemplate[index];
    }
  return null;
  }

public void setAction(int x, int y, int z, MineActionPoint action)
  {
  int index = zSize*xSize*y + xSize*z +x;
  if(this.actionTemplate!=null && index>=0 && index<this.actionTemplate.length)
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
      if(size.length>=3)
        {
        xSize = size[0];
        ySize = size[1];
        zSize = size[2];
        this.actionTemplate = new MineActionPoint[xSize*ySize*zSize];
        }
      }
    else if(line.toLowerCase().startsWith("layer:"))
      {
      if(this.actionTemplate!=null)
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
      else
        {
        Config.logError("Error parsing mine template -- size was not set before levels");
        }
      }    
    }
  }

public void parseLevel(List<String> lines, int levelNum)
  {
  Iterator<String> it = lines.iterator();
  String line;
  int zIndex = 0;
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
    this.parseLayerLine(StringTools.parseStringArray(line), levelNum, zIndex);    
    zIndex++;
    }
  }

protected void parseLayerLine(String[] splits, int y, int z)
  {
  String l;
  String subSplit[];
  int order;
  int meta;
  int actionOrdinal;
  for(int x = 0; x<splits.length; x++)
    {
    l = splits[x];
    subSplit = l.split("-");
    if(subSplit.length>=3)
      {
      order = StringTools.safeParseInt(subSplit[0]);
      meta = StringTools.safeParseInt(subSplit[1]);
      actionOrdinal = StringTools.safeParseInt(subSplit[2]);
      TargetType t = TargetType.values()[actionOrdinal];
      this.setAction(x, y, z, new MineActionPoint(order,meta,t));
      }
    }
  }

}
