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
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TEFarmCactus extends TEWorkSiteFarm
{

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
  if(x%4==0 && z%4==0 && worldObj.getBlockId(x, y-1, z)==Block.sand.blockID && inventory.containsAtLeast(plantableFilter, 1))
    {
    if(worldObj.getBlockId(x-1, y, z)==0 && worldObj.getBlockId(x+1, y, z)==0 && worldObj.getBlockId(x, y, z-1)==0 && worldObj.getBlockId(x, y, z+1)==0)
      {
      return TargetType.FARM_PLANT;
      }    
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



}
