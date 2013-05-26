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
package shadowmage.ancient_warfare.common.container;

import java.util.List;

import shadowmage.ancient_warfare.client.registry.RenderRegistry;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class SlotResourceOnly extends Slot
{

List<ItemStack> itemFilters;

/**
 * @param par1iInventory
 * @param par2
 * @param par3
 * @param par4
 */
public SlotResourceOnly(IInventory par1iInventory, int par2, int par3, int par4, List<ItemStack> filters)
  {
  super(par1iInventory, par2, par3, par4);
  this.itemFilters = filters;
  }

@Override
public boolean isItemValid(ItemStack par1ItemStack)
  {
  for(ItemStack item : this.itemFilters)
    {
    if(InventoryTools.doItemsMatch(item, par1ItemStack))
      {
      return true;
      }
    }
  return false;
  }

}
