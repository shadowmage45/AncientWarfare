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
package shadowmage.ancient_warfare.common.aw_structure.data.components;

import java.util.LinkedList;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockInfo;
import shadowmage.ancient_warfare.common.aw_structure.data.ComponentBoundingBox;
import shadowmage.ancient_warfare.common.aw_structure.data.StructureComponent;

public class ComponentBlocks extends StructureComponent
{

ComponentBoundingBox boundingBox;
int id;
int meta;
LinkedList<BlockPosition> blocks = new LinkedList<BlockPosition>();

@Override
public void placeSingleElement(World world, BlockPosition startPos,  int facing, int rotationAmount)
  {
  BlockPosition pos = startPos.copy();
  BlockPosition cons = blocks.pop();
  pos.moveForward(facing, cons.x);
  pos.moveRight(facing, cons.z);
  pos.y += blocks.peekFirst().y;
  int meta = this.meta;
  if(BlockInfo.blockList[id]!=null)
    {
    if(BlockInfo.blockList[id].isRotatable())
      {
      meta = BlockInfo.blockList[id].rotateRight(this.meta, rotationAmount);
      }
    }
  
  }
 
@Override
public boolean isFinished()
  {
  return this.blocks.isEmpty();
  }

}
