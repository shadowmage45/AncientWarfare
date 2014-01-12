/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.item.ItemLoader;

public class SlotExcludeBackpack extends Slot
{

/**
 * @param par1iInventory
 * @param par2
 * @param par3
 * @param par4
 */
public SlotExcludeBackpack(IInventory par1iInventory, int par2, int par3, int par4)
  {
  super(par1iInventory, par2, par3, par4);
  }

@Override
public boolean isItemValid(ItemStack par1ItemStack)
  {
  return super.isItemValid(par1ItemStack) && par1ItemStack!=null && par1ItemStack.getItem()!=ItemLoader.backpack;
  }


  

}
