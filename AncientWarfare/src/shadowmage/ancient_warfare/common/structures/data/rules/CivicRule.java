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
package shadowmage.ancient_warfare.common.structures.data.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class CivicRule
{

int x;
int y;
int z;
int civicType = 0;
int xMin;
int yMin;
int zMin;
int xSize;
int ySize;
int zSize;

public static CivicRule populateRule(int x, int y, int z, TECivic te)
  {
  CivicRule rule = new CivicRule();
  rule.x = x;
  rule.y = y;
  rule.z = z;
  rule.civicType = te.getCivic().getGlobalID();   
  rule.xMin = te.minX-te.xCoord;
  rule.yMin = te.minY-te.yCoord;
  rule.zMin = te.minZ-te.zCoord;
//  rule.xMin = x + te.minX-te.xCoord;
//  rule.yMin = y + te.minY-te.yCoord;
//  rule.zMin = z + te.minZ-te.zCoord;
  rule.xSize = te.maxX - te.minX + 1;
  rule.ySize = te.maxY - te.minY + 1;
  rule.zSize = te.maxZ - te.minZ + 1;  
  return rule;
  }

/**
 * normalize the x,y,z internal coords of this rule to represent a north-faced structure
 * @param currentFacing
 * @param xSize structure xSize as north-facing
 * @param ySize structure zSize as north-facing
 */
public void normalizeForNorthFacing(int currentFacing, int xSize, int zSize)
  {
  BlockPosition pos = BlockTools.getNorthRotatedPosition(x,y,z, currentFacing, xSize, zSize);   
  /**
   * corners of block bounds relative to TL corner of scanned stucture
   */  
  BlockPosition c1 = BlockTools.getNorthRotatedPosition(x+xMin, yMin, z+zMin, currentFacing, xSize, zSize);
  
//  BlockPosition corner2 = BlockTools.getNorthRotatedPosition(x+xMin+this.xSize, yMin, z+zMin, currentFacing, xSize, zSize);
//  BlockPosition corner3 = BlockTools.getNorthRotatedPosition(x+xMin, yMin, z+zMin+this.zSize, currentFacing, xSize, zSize);
//  BlockPosition corner4 = BlockTools.getNorthRotatedPosition(x+xMin+this.xSize, yMin, z+zMin+this.zSize, currentFacing, xSize, zSize);
  
  x = pos.x;
  z = pos.z;   
  
  BlockPosition c2 = null;
  switch(currentFacing)
  {
  case 0:
  c2= new BlockPosition(c1.x-(this.xSize-1), c1.y, c1.z-(this.zSize-1));
  //x--
  //z
  break;
  case 1:
  c2 = new BlockPosition(c1.x- (this.zSize-1), c1.y, c1.z+ (this.xSize-1));
  //x++   zsize
  //z--   xsize
  
  break;
  case 2:
  c2 = new BlockPosition(c1.x+(this.xSize-1), c1.y, c1.z+(this.zSize-1));
  //x++ xsize
  //z++ zsize
  break;
  case 3:
  c2 = new BlockPosition(c1.x + (this.zSize-1), c1.y, c1.z-(this.xSize-1));
  //x--   zsize
  //z++   xsize
  break;
  }
  if(currentFacing==1 || currentFacing==3)
    {
    int swap = this.xSize;
    this.xSize = this.zSize;
    this.zSize = swap;
    }
  BlockPosition min = BlockTools.getMin(c1, c2);
  xMin = min.x-x;
  zMin = min.z-z;
  Config.logDebug("new mx,mz: "+xMin+","+zMin);
  }

public void handleWorldPlacement(World world, int facing, ProcessedStructure struct, BlockPosition buildPos)
  {
  BlockPosition target = BlockTools.getTranslatedPosition(buildPos, new BlockPosition(x-struct.xOffset,y-struct.verticalOffset, z-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
  
  BlockPosition c1 = BlockTools.getTranslatedPosition(buildPos, new BlockPosition(x+xMin-struct.xOffset, y+yMin-struct.verticalOffset, z+zMin-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
  BlockPosition c2 = null;
  switch(facing)
  {
  case 0:
  c2= new BlockPosition(c1.x - (xSize-1), c1.y, c1.z - (zSize-1));
  //x--
  //z
  break;
  case 1:
  c2 = new BlockPosition(c1.x + (zSize-1), c1.y, c1.z - (xSize-1) );
  //x++   zsize
  //z--   xsize
  
  break;
  case 2:
  c2 = new BlockPosition(c1.x + (xSize-1), c1.y, c1.z + (zSize-1));
  //x++ xsize
  //z++ zsize
  break;
  case 3:
  c2 = new BlockPosition(c1.x - (zSize-1), c1.y, c1.z + (xSize-1));
  //x--   zsize
  //z++   xsize
  break;
  }
  BlockPosition min = BlockTools.getMin(c1, c2);
  BlockPosition max = BlockTools.getMax(c1, c2);
  max.y += (ySize-1);
  CivicRegistry.instance().setCivicBlock(world, target.x, target.y, target.z, civicType);
  TECivic te = (TECivic) world.getBlockTileEntity(target.x, target.y, target.z);
  te.setBounds(min.x, min.y, min.z, max.x, max.y, max.z);
  world.markBlockForUpdate(target.x, target.y, target.z);
  }  

public List<String> getRuleLines()
  {
  ArrayList<String> lines = new ArrayList<String>();
  lines.add("civic:");
  lines.add("type="+civicType);
  lines.add("pos=" + StringTools.getCSVStringForArray(new int[]{x,y,z}));
  lines.add("bounds="+StringTools.getCSVStringForArray(new int[]{xMin, yMin, zMin, xSize, ySize, zSize}));  
  lines.add(":endcivic");
  return lines;
  }

public static CivicRule parseLines(List<String> lines)
  {
  CivicRule rule = new CivicRule();
  String line;
  Iterator<String> it = lines.iterator();
  boolean valid = true;
  rule.civicType = -1;
  while(it.hasNext())
    {    
    line = it.next();   
    if(line.toLowerCase().startsWith("type"))
      {
      rule.civicType = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("pos"))
      {
      int[] pos = StringTools.safeParseIntArray("=", line);
      if(pos.length>=2)
        {
        rule.x = pos[0];
        rule.y = pos[1];
        rule.z = pos[2];
        }
      else
        {
        valid = false;
        }
      }
    else if(line.toLowerCase().startsWith("bounds"))
      {
      int[] bounds = StringTools.safeParseIntArray("=", line);
      if(bounds.length>=5)
        {
        rule.xMin = bounds[0];
        rule.yMin = bounds[1];
        rule.zMin = bounds[2];
        rule.xSize = bounds[3];
        rule.ySize = bounds[4];
        rule.zSize = bounds[5];
        }
      else
        {
        valid = false;
        }
      }
    }
  if(!valid || rule.civicType<0)
    {
    return null;
    }
  return rule;
  }

}
