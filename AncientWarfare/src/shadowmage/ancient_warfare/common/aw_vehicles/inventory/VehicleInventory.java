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
package shadowmage.ancient_warfare.common.aw_vehicles.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.aw_core.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.aw_vehicles.VehicleBase;

public class VehicleInventory extends AWInventoryBasic
{
private VehicleBase vehicle;

/**
 * slot counts, used by container for displaying slots in GUI
 * max slot counts are hard-indexed, many may be empty
 */
public int upgradeSlots;
public int ammoSlots;
public int armorSlots;
public int engineSlots;
public int fuelSlots;
public int storageSlots;

public VehicleInventory(VehicleBase vehicle)
  {
  super(54);//0-26 vehicle slots //27-53 storage slots
  this.vehicle = vehicle; 
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  this.writeSlotDataToNBT(tag);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.readSlotDataFromNBT(tag);
  }

public void readSlotDataFromNBT(NBTTagCompound tag)
  {
  this.upgradeSlots = tag.getInteger("uS");
  this.ammoSlots = tag.getInteger("amS");
  this.armorSlots = tag.getInteger("arS");
  this.engineSlots = tag.getInteger("eS");
  this.fuelSlots= tag.getInteger("fS");
  this.storageSlots = tag.getInteger("sS");
  }

/**
 * get a compound tag returning data about slot counts, to be relayed client-side during vehicle client-data sending
 * @return
 */
public void writeSlotDataToNBT(NBTTagCompound tag)
  {
  tag.setInteger("uS", upgradeSlots);
  tag.setInteger("amS", ammoSlots);
  tag.setInteger("arS", armorSlots);
  tag.setInteger("eS", engineSlots);
  tag.setInteger("fS", fuelSlots);
  tag.setInteger("sS", storageSlots);
  }

}
