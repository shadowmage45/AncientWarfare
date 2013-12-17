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
package shadowmage.ancient_structures.common.template.scan;

import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class TemplateScanner
{

TemplateRule[] rules;
    
public TemplateScanner()
  {
  
  }

/**
 * 
 * @param world
 * @param min
 * @param max
 * @param key 
 * @param turns # of turns for proper orientation
 * @return
 */
public StructureTemplate scan(World world, BlockPosition min, BlockPosition max, BlockPosition key, int turns)
  {
  int xSize = max.x - min.x+1;
  int ySize = max.y - min.y+1;
  int zSize = max.z - min.z+1;
  
  int xOutSize, zOutSize;
  xOutSize = xSize;
  zOutSize = zSize;
    {
    int swap;
    for(int i = 0; i < turns; i++)
      {
      swap = xOutSize;
      xOutSize = zOutSize;
      zOutSize = swap;
      }
    }
    
  boolean [][][] testOut = new boolean[xOutSize][ySize][zOutSize];
  
  
  key.x = key.x - min.x;
  key.y = key.y - min.y;
  key.z = key.z - min.z; 
  
  BlockTools.rotateInArea(key, xSize, zSize, turns);    
  AWLog.logDebug("should scan: "+min+"::"+max+ " key:"+key + " turns: "+turns);
  
  int scanX, scanZ, scanY;
  BlockPosition destination = new BlockPosition();
  for(scanY = min.y; scanY<=max.y; scanY++)  
    {
    for(scanZ = min.z; scanZ<=max.z; scanZ++)
      {
      for(scanX = min.x; scanX<=max.x; scanX++)
        {
//        AWLog.logDebug("scanX :"+scanX+" minX: "+min.x);
        destination.x = scanX - min.x;
        destination.y = scanY - min.y; 
        destination.z = scanZ - min.z;
        AWLog.logDebug("scanning block..input : "+destination);
        BlockTools.rotateInArea(destination, xSize, zSize, turns);
        AWLog.logDebug("scanning block..output: "+destination);
        if(world.getBlockId(scanX, scanY, scanZ)!=0)
          {
          testOut[destination.x][destination.y][destination.z]=true;
          }
        }
      }
    }
  
  String line = "";
  for(int y = 0; y <ySize; y++)
    {
    AWLog.logDebug("level");
    for(int z = 0; z<zOutSize; z++)
      {
      for(int x = 0; x<xOutSize; x++)
        {
        line = line + (testOut[x][y][z]==true? "1" : "0");
        }
      AWLog.logDebug(line);
      line = "";
      }
    }
  return null;
  }

}
