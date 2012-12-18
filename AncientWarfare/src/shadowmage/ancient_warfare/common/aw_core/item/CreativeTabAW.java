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
package shadowmage.ancient_warfare.common.aw_core.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class CreativeTabAW extends CreativeTabs
{

private static CreativeTabAW INSTANCE = new CreativeTabAW("Ancient Warfare");

public static CreativeTabAW instance()
  {
  return INSTANCE;
  }

private CreativeTabAW(String label)
  {
  super(label); 
  }

@SideOnly(Side.CLIENT)

/**
 * Get the ItemStack that will be rendered to the tab.
 */
@Override
public ItemStack getIconItemStack()
  {
  return new ItemStack(Item.stick,1);
  }

/**
 * Gets the translated Label.
 */
@Override
public String getTranslatedTabLabel()
  {
  return this.getTabLabel();
  }

}
