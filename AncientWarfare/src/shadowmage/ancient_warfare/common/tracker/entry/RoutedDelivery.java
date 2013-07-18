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
package shadowmage.ancient_warfare.common.tracker.entry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class RoutedDelivery implements INBTTaggable
{

String destination;
ItemStack item;
int ticksLeft = 0;

RoutedDelivery(){}

public RoutedDelivery(String destination, ItemStack item, int travelTicks)
  {
  this.destination = destination;
  this.item = item;
  this.ticksLeft = travelTicks;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.destination = tag.getString("destination");
  this.item = InventoryTools.loadStackFromTag(tag.getCompoundTag("item"));
  this.ticksLeft = tag.getInteger("ticks");
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("destination", destination);
  tag.setCompoundTag("item", InventoryTools.writeItemStackToTag(item, new NBTTagCompound()));
  tag.setInteger("ticks", this.ticksLeft);
  return tag;
  }
}
