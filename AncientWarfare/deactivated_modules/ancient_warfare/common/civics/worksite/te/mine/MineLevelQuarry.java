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

import net.minecraft.block.Block;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class MineLevelQuarry extends MineLevel
{

/**
 * @param xPos
 * @param yPos
 * @param zPos
 * @param xSize
 * @param ySize
 * @param zSize
 */
public MineLevelQuarry(int xPos, int yPos, int zPos, int xSize, int ySize, int zSize)
  {
  super(xPos, yPos, zPos, xSize, ySize, zSize);
  this.levelSize = 1;
  }

@Override
protected void scanLevel(TEMine mine, World world)
  {
  int id;
  for(int x = this.minX; x < this.minX + this.xSize; x++)
    {
    for(int z = this.minZ; z < this.minZ + this.zSize; z++)
      {      
      boolean addLadder = false;
      byte ladderMeta = 0;
      if(x==this.minX && z==this.minZ + this.zSize/2)
        {
        addLadder = true;
        ladderMeta = 5;
        id = world.getBlockId(x-1, minY, z);
        if(needsFilled(id))
          {
          this.addNewPoint(x-1, minY, z, TargetType.MINE_FILL);
          }
        }
      else if(x==this.minX+this.xSize-1 && z==this.minZ + this.zSize/2)
        {
        addLadder = true;
        ladderMeta = 4;
        id = world.getBlockId(x+1, minY, z);
        if(needsFilled(id))
          {
          this.addNewPoint(x+1, minY, z, TargetType.MINE_FILL);
          }
        }
      else if(z==this.minZ && x==this.minX + this.xSize/2)
        {
        addLadder = true;
        ladderMeta = 3;
        id = world.getBlockId(x, minY, z-1);
        if(needsFilled(id))
          {
          this.addNewPoint(x, minY, z-1, TargetType.MINE_FILL);
          }
        }
      else if(z==this.minZ + this.zSize-1 && x==this.minX+this.xSize/2)
        {
        addLadder = true;
        ladderMeta = 2;
        id = world.getBlockId(x, minY, z+1);
        if(needsFilled(id))
          {
          this.addNewPoint(x, minY, z+1, TargetType.MINE_FILL);
          }
        }
      else if(shouldClear(world.getBlockId(x, minY, z)))
        {
        this.addNewPoint(x, minY, z, TargetType.MINE_CLEAR);
        } 
      if(addLadder)
        {
        if(world.getBlockId(x, minY, z)!=Block.ladder.blockID)
          {
          this.addNewPoint(x, minY, z, TargetType.MINE_CLEAR);
          this.addNewPoint(x, minY, z, ladderMeta, TargetType.MINE_LADDER);          
          }        
        }
      }
    }
  }

}
