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

import java.util.Arrays;
import java.util.LinkedList;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockData;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockInfo;
import shadowmage.ancient_warfare.common.aw_structure.data.ComponentBoundingBox;
import shadowmage.ancient_warfare.common.aw_structure.data.ComponentBase;

public class ComponentBlocks extends ComponentBase
{

public BlockData blockData;
LinkedList<BlockPosition> blocks = new LinkedList<BlockPosition>();

/**
   * @param pos1
   * @param pos2
   */
public ComponentBlocks(BlockPosition pos1, BlockPosition pos2, BlockData data)
  {
  super(pos1, pos2);
  this.blockData = data;
  this.blocks.addAll(BlockTools.getAllBlockPositionsBetween(pos1, pos2));
  }

@Override
public void placeSingleElement(World world, BlockPosition startPos,  int facing, int rotationAmount)
  {
  if(blocks.isEmpty())
    {
    return;
    }
  BlockPosition pos = startPos.copy();
  BlockPosition cons = blocks.pop();
  pos.moveForward(facing, cons.x);
  pos.moveRight(facing, cons.z);
  pos.y += cons.y;
  int meta = this.blockData.meta;
  if(BlockInfo.blockList[this.blockData.id]!=null)
    {
    if(BlockInfo.blockList[this.blockData.id].isRotatable())
      {
      meta = BlockInfo.blockList[this.blockData.id].rotateRight(this.blockData.meta, rotationAmount);
      }
    }  
  }
 
@Override
public boolean isFinished()
  {
  return this.blocks.isEmpty();
  }

}
