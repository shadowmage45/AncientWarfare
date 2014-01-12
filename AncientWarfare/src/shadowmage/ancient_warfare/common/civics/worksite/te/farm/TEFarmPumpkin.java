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
package shadowmage.ancient_warfare.common.civics.worksite.te.farm;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class TEFarmPumpkin extends TEWorkSiteFarm
{
ItemStack pumpkinFilter;
/**
 * 
 */
public TEFarmPumpkin()
  {
  this.mainBlockID = Block.pumpkinStem.blockID;
  this.mainBlockMatureMeta = 0;
  this.plantableFilter = new ItemStack(Item.pumpkinSeeds);
  pumpkinFilter = new ItemStack(Block.pumpkin);
  }

@Override
protected TargetType validateWorkPoint(int x, int y, int z)
  {
  int id = worldObj.getBlockId(x, y, z);  
  if(id==0 && (worldObj.getBlockId(x, y-1, z)==Block.dirt.blockID || worldObj.getBlockId(x, y-1, z)==Block.grass.blockID))
    {
    return TargetType.FARM_TILL;
    }
  else if(id==0 && worldObj.getBlockId(x, y-1, z)==tilledEarthID && inventory.containsAtLeast(plantableFilter, 1))
    {    
    return TargetType.FARM_PLANT;
    }
  else if(id==Block.pumpkin.blockID && inventory.canHoldItem(pumpkinFilter, 1, resourceSlotIndices))
    {
    return TargetType.FARM_HARVEST;
    }
  return TargetType.NONE;
  }
}
