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
package shadowmage.ancient_structures.common.civic;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_structures.common.block.TECivicBuilder;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.item.ItemLoaderCore;

public class CivicBuilder extends Civic
{

/**
 * @param id
 */
public CivicBuilder(int id)
  {
  super(id);
  this.addToCreative = false;
  this.name = "Civic Structure Builder";
  this.tooltip = "Work site for building of structures.";
  this.teClass = TECivicBuilder.class;
  this.workType = CivicWorkType.MINE;
  this.maxWorkers = 4;
  this.isWorkSite = true;
  this.itemIconTexture = "civicMine1";
  this.setBlockIcons("civicBuilderBottom", "civicBuilderTop", "civicBuilderSides");
  }

@Override
public ItemStack getItemToConstruct()
  {
 /**
  * TODO
  */
  return null;
  }

}
