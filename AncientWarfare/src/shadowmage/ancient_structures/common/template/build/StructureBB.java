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
package shadowmage.ancient_structures.common.template.build;

import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;

public class StructureBB
{

public BlockPosition pos1;
public BlockPosition pos2;

public StructureBB(int x, int y, int z, int face, int xSize, int ySize, int zSize, int xOffset, int yOffset, int zOffset)
  {
  this.setFromStructure(x, y, z, face, xSize, ySize, zSize, xOffset, yOffset, zOffset);     
  }

public final StructureBB setFromStructure(int x, int y, int z, int face, int xSize, int ySize, int zSize, int xOffset, int yOffset, int zOffset)
  {
  int destXSize = xSize;
  int destYSize = ySize;
  int destZSize = zSize;
  
  int turns = ((face+2)%4);
  int swap;
  for(int i = 0; i<turns; i++)
    {
    swap = destXSize;
    destXSize = destZSize;
    destZSize = swap;
    }
    
  /**
   * here we take the back-left corner in template space
   */
  BlockPosition destinationKey = new BlockPosition(0, 0, 0);
  
  /**
   * and we rotate that corner into local space
   */
  BlockTools.rotateInArea(destinationKey, xSize, zSize, turns);
  
  /**
   * we are placing destination1 to be the back-let corner of the structure. offset it by the rotated corner to get the correct corner
   */
  BlockPosition destination1 = new BlockPosition(x-destinationKey.x, y-destinationKey.y, z-destinationKey.z);
  
  /**
   * next, offset the back-left corner by the structures build-key offsets
   */
  destination1.moveLeft(face, xOffset);
  destination1.moveForward(face, zOffset);
  destination1.y-=yOffset;
  
  /**
   * copy position to make the front-right corner.
   */
  BlockPosition destination2 = new BlockPosition(destination1);
  
  /**
   * offset this position directly by the size of the structure to get the actual front-right corner
   */
  destination2.offset(destXSize-1, ySize-1, destZSize-1);            
  
  /**
   * calculate structure bounding box min/max from destination 1 and destination 2
   */
  this.pos1 = BlockTools.getMin(destination1, destination2);
  this.pos2 = BlockTools.getMax(destination1, destination2);
  return this;
  }

public StructureBB(BlockPosition pos1, BlockPosition pos2)
  {
  this.pos1 = BlockTools.getMin(pos1, pos2);
  this.pos2 = BlockTools.getMax(pos1, pos2);
  }

@Override
public String toString()
  {
  return pos1.toString() + " : " +pos2.toString();
  }

/**
 * does the input bb share any blocks with this bounding box?
 * @param bb
 * @return
 */
public boolean collidesWith(StructureBB bb)
  {
  if(pos2.x < bb.pos1.x)
    {
    return false;
    }
  if(pos2.y < bb.pos1.y)
    {
    return false;
    }
  if(pos2.z < bb.pos1.z)
    {
    return false;
    }
  if(pos1.x > bb.pos2.x)
    {
    return false;
    }
  if(pos1.y > bb.pos2.y)
    {
    return false;
    }
  if(pos1.z > bb.pos2.z)
    {
    return false;
    }  
  return true;
  }

/**
 * can be used to contract by specifying negative amounts...
 * @param amt
 */
public void expand(int x, int y, int z)
  {
  pos1.x-=x;
  pos1.y-=y;
  pos1.z-=z;
  pos2.x+=x;
  pos2.y+=y;
  pos2.z+=z;
  }
}
