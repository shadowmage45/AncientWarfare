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
package shadowmage.ancient_warfare.common.civics.worksite.te.tree;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class TETreeFarmBirch extends TETreeFarm
{

/**
 * 
 */
public TETreeFarmBirch()
  {
  this.logMeta = 2;
  saplingID = Block.sapling.blockID;
  saplingMeta = 2;
  this.saplingFilter = new ItemStack(saplingID, 1, saplingMeta);
  this.logFilter = new ItemStack(Block.wood,1,this.logMeta);
  }

}
