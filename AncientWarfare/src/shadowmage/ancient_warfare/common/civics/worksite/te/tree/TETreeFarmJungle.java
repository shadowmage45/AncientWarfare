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
package shadowmage.ancient_warfare.common.civics.worksite.te.tree;

import shadowmage.ancient_warfare.common.targeting.TargetType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class TETreeFarmJungle extends TETreeFarm
{

/**
 * 
 */
public TETreeFarmJungle()
  {
  this.logMeta = 3;
  saplingID = Block.sapling.blockID;
  saplingMeta = 3;
  this.saplingFilter = new ItemStack(saplingID, 1, saplingMeta);
  }

protected TargetType validateWorkPoint(int x, int y, int z)
  {
  /**
   * NFC how to make these use 2x2 areas....probably a bad idea anyway because of the height..
   */
  int id = worldObj.getBlockId(x, y, z);
  int meta = worldObj.getBlockMetadata(x, y, z);
  if( id==logID && (meta &3) == this.logMeta )
    {
    return TargetType.TREE_CHOP;
    }
  else if(id==0)
    {
    if(x%4==0 && z%4==0)
      {
      id = worldObj.getBlockId(x, y-1, z);
      if((id==Block.dirt.blockID || id==Block.grass.blockID) && inventory.containsAtLeast(saplingFilter, 1))
        {
        return TargetType.TREE_PLANT;
        }
      } 
    }
  return TargetType.NONE;
  }

}
