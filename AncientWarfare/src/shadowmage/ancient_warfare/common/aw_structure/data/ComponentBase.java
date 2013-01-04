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

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;

public abstract class ComponentBase
{

/**
 * two blockPositions defining the bounding box for this component
 * local relative to the structure defining the box
 */
public BlockPosition pos1;
public BlockPosition pos2;

public ComponentBase(BlockPosition pos1, BlockPosition pos2)
  {
  this.pos1 = pos1;
  this.pos2 = pos2;  
  }

public abstract void placeSingleElement(World world, BlockPosition startPos, int originFace, int rotationAmount);

public abstract boolean isFinished();

}
