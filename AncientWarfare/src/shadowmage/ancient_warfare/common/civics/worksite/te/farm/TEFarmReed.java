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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TEFarmReed extends TEWorkSiteFarm
{

public TEFarmReed()
  {
  plantableFilter = new ItemStack(Item.reed);
  mainBlockID = Block.reed.blockID;
  }

protected boolean isWater(int x, int y, int z)
  {
  int id = worldObj.getBlockId(x, y, z);
  return id==Block.waterMoving.blockID || id==Block.waterStill.blockID;
  }

@Override
protected TargetType validateWorkPoint(int x, int y, int z)
  {
  TargetType t = TargetType.NONE;
  boolean addPoint = false;
  boolean foundWater =false;
  int id = worldObj.getBlockId(x, y, z);
  int id2 = worldObj.getBlockId(x, y-1, z);
  if(id==Block.reed.blockID)
    {
    if(id2==Block.reed.blockID && worldObj.getBlockId(x, y-2, z)==Block.reed.blockID)
      {
      return TargetType.FARM_HARVEST;
      }    
    }
  else if(id==0 && id2==Block.dirt.blockID || id2==Block.grass.blockID || id2==Block.sand.blockID)
    {
    if(isWater(x-1, y-1, z) || isWater(x+1,y-1,z) || isWater(x,y-1,z-1) || isWater(x,y-1,z+1))
      {
      return TargetType.FARM_PLANT;
      }       
    } 
  return TargetType.NONE;
  }

}
