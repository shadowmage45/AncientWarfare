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
   along with Foobar.  If not, see <http://www.gnu.org/licenses/>.


 */
package shadowmage.ancient_warfare.common.aw_core.registry.entry;

import net.minecraft.item.ItemStack;

/**
 * @author Shadowmage
 * lightweight wrapper for the base information to make up an item
 */
public class ItemIDPair
{

public ItemIDPair(int id, int dmg)
  {
  this.itemID = id;
  this.dmg = dmg;  
  }
public int itemID;
public int dmg;

public boolean equals (ItemStack stack)
  {
  return itemID==stack.itemID && dmg==stack.getItemDamage();
  }

public boolean equals (ItemIDPair pair)
  {
  return pair.itemID==this.itemID && pair.dmg ==this.dmg;
  }

public boolean equals (int id, int dmg)
  {
  return this.itemID==id && this.dmg==dmg;
  }
}
