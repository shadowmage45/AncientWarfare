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
package shadowmage.ancient_warfare.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class ItemBackpack extends AWItemClickable
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBackpack(int itemID)
  {
  super(itemID, true);
  this.maxStackSize = 1;
  this.hasLeftClick = false;
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(!world.isRemote)
    {
    GUIHandler.instance().openGUI(GUIHandler.BACKPACK, player, world, 0, 0, 0);
    }
  return false;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  return false;
  }

public static AWInventoryBasic getInventoryFor(ItemStack stack)
  {
  AWInventoryBasic inventory = null;
  if(stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey("AWBackPack"))
    {
    NBTTagCompound tag = stack.getTagCompound().getCompoundTag("AWBackPack");
    int size = tag.getInteger("size");
    inventory = new AWInventoryBasic(size);
    inventory.readFromNBT(tag);
    }
  if(inventory == null)
    {
    inventory = new AWInventoryBasic((stack.getItemDamage()/16)*9 + 9);
    }
  return inventory;
  }

public static void writeInventoryToItem(ItemStack stack, AWInventoryBasic inventory)
  {
  if(stack!=null && inventory!=null && stack.itemID == ItemLoader.backpack.itemID)
    {
    NBTTagCompound tag = inventory.getNBTTag();
    tag.setInteger("size", inventory.getSizeInventory());
    stack.setTagInfo("AWBackPack", tag);
    }
  }

}
