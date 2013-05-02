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
package shadowmage.ancient_warfare.common.civics.worksite.te.farm;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TEFarmCactus extends TEWorkSiteFarm
{
int maxSearchHeight = 4;
/**
 * 
 */
public TEFarmCactus()
  {
  this.mainBlockID = Block.cactus.blockID;
  plantableFilter = new ItemStack(Block.cactus);
  }

@Override
protected TargetType validateWorkPoint(int x, int y, int z)
  {
  int id = worldObj.getBlockId(x, y, z);
  int id2 = worldObj.getBlockId(x, y-1, z);
  if(id==0 && x%2==0 && z%2==0 && id2==Block.sand.blockID && Block.cactus.canBlockStay(worldObj, x, y, z) && inventory.containsAtLeast(plantableFilter, 1))
    {
    return TargetType.FARM_PLANT;
    }
  else if(id==this.mainBlockID && inventory.canHoldItem(plantableFilter, 1))
    {
    if(worldObj.getBlockId(x, y-1, z)==mainBlockID && worldObj.getBlockId(x, y-2, z)==mainBlockID)
      {
      return TargetType.FARM_HARVEST;
      }
    }
  return TargetType.NONE;
  }

@Override
protected void scan()
  {
  TargetType t;
  for(int y = this.minY; y<=this.maxY+this.maxSearchHeight; y++)
    {
    for(int x = this.minX; x<=this.maxX; x++)
      {
      for(int z = this.minZ; z<=this.maxZ; z++)
        {        
        t = this.validateWorkPoint(x, y, z);
        if(t!=TargetType.NONE)
          {
          this.addWorkPoint(x, y, z, t);
          }
        }
      }
    }
  }

@Override
public AxisAlignedBB getSecondaryRenderBounds()
  {
  return AxisAlignedBB.getAABBPool().getAABB(minX, maxY+1, minZ, maxX+1, maxY+1+maxSearchHeight, maxZ+1);
  }
}
