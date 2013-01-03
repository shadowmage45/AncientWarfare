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
package shadowmage.ancient_warfare.common.aw_structure.data;

import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;

public class ComponentBoundingBox
{

public BlockPosition frontLeftBottom;
public BlockPosition farRightTop;

public ComponentBoundingBox(int fX, int fY, int fZ, int rX, int rY, int rZ)
  {
  this.frontLeftBottom = new BlockPosition(fX,fY,fZ);
  this.farRightTop = new BlockPosition(rX,rY,rZ);
  }

public ComponentBoundingBox(BlockPosition pos1, BlockPosition pos2)
  {
  this.frontLeftBottom = pos1.copy();
  this.farRightTop = pos2.copy();
  }

/**
 * construct the BB as a zero oriented BB from vector
 * (fL is 0,0,0, bR is input vector)
 * @param vec
 */
public ComponentBoundingBox(BlockPosition vec)
  {
  this.frontLeftBottom = new BlockPosition(0,0,0);
  this.farRightTop = vec.copy();
  }

public int sizeX()
  {
  return BlockTools.getDifference(frontLeftBottom.x, farRightTop.x);
  }

public int sizeY()
  {
  return BlockTools.getDifference(frontLeftBottom.y, farRightTop.y);
  }

public int sizeZ()
  {
  return BlockTools.getDifference(frontLeftBottom.z, farRightTop.z);
  }

}
