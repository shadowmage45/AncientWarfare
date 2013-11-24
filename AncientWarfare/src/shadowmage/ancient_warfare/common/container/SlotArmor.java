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
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

class SlotArmor extends Slot
{
/**
 * The armor type that can be placed on that slot, it uses the same values of armorType field on ItemArmor.
 */
final int armorType;


NpcBase player;

public SlotArmor(NpcBase player, IInventory par2IInventory, int par3, int par4, int par5, int par6)
  {
  super(par2IInventory, par3, par4, par5);
  this.player = player;
  this.armorType = par6;
  }

/**
 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
 * of armor slots)
 */
public int getSlotStackLimit()
  {
  return 1;
  }

/**
 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
 */
public boolean isItemValid(ItemStack par1ItemStack)
  {
  Item item = (par1ItemStack == null ? null : par1ItemStack.getItem());
  return par1ItemStack.getItemDamage()==0 && item != null && item.isValidArmor(par1ItemStack, armorType, player);
  }

@SideOnly(Side.CLIENT)

/**
 * Returns the icon index on items.png that is used as background image of the slot.
 */
public Icon getBackgroundIconIndex()
  {
  return ItemArmor.func_94602_b(this.armorType);
  }
}
