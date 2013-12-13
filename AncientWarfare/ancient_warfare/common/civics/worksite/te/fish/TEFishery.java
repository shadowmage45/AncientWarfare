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
package shadowmage.ancient_warfare.common.civics.worksite.te.fish;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class TEFishery extends TEWorkSite
{

ItemStack fishFilter;
int waterBlocks = 0;
float blockDivider = 0.001953125f;//essentially divides the num of blocks found by 512 (max)
float harvestFactor = 0.25f;//the scale factor for this block. 1.0 = 100% chance per block, averaged vs max number of blocks

public TEFishery()
  {
  fishFilter = new ItemStack(Item.fishRaw);
  this.renderBounds = true;
  }

@Override
protected void scan()
  {
  waterBlocks = 0;
  int id;
  int canHold = inventory.canHoldMore(fishFilter);
  for(int y = this.minY; y<=this.maxY; y++)
    {
    for(int z = this.minZ; z<= this.maxZ; z++)
      {
      for(int x = this.minX; x<=this.maxX; x++)
        {
        id = worldObj.getBlockId(x, y, z);
        if(id==Block.waterMoving.blockID || id==Block.waterStill.blockID)
          {
          waterBlocks++;
          if(canHold>0)
            {
            canHold--;         
            this.addWorkPoint(x, y, z, TargetType.FISH_CATCH);
            }
          }
        }
      }
    }  
  }

@Override
protected void doWork(IWorker npc, WorkPoint p)
  {
  if(p.work==TargetType.FISH_CATCH && inventory.canHoldItem(fishFilter, 1))
    {
    float waterPercent = (float)waterBlocks * blockDivider * harvestFactor;
    float check = rng.nextFloat();
    if(check<=waterPercent)      
      {      
      this.tryAddItemToInventory(fishFilter.copy(), regularIndices);
      }
    }
  }

@Override
protected TargetType validateWorkPoint(WorkPoint p)
  {
  int id = worldObj.getBlockId(p.x, p.y, p.z); 
  if(id == Block.waterMoving.blockID || id==Block.waterStill.blockID)
    {
    return TargetType.FISH_CATCH;
    }
  return TargetType.NONE;
  }

}
