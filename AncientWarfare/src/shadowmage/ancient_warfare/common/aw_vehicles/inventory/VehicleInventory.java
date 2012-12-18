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

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.aw_core.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.aw_core.utils.IInventoryCallback;
import shadowmage.ancient_warfare.common.aw_vehicles.VehicleBase;

public class VehicleInventory implements IInventoryCallback
{
/**
 * has this inventory been initialized, are the size of inventories set
 * and are individual inventories valid (all of them)
 */
private boolean isInventoryValid = false;
private VehicleBase vehicle;

/**
 * individual inventories
 * 
 */
public AWInventoryBasic upgradeInventory = null;
public AWInventoryBasic ammoInventory = null;
public AWInventoryBasic armorInventory = null;
public AWInventoryBasic engineInventory = null;
public AWInventoryBasic storageInventory = null;

/**
 * number of valid slots this inventory possesses
 * set from research stats from spawning item, and re-set at readFromNBT
 * containers will use these numbers to add the appropriate slots
 */
public int upgradeSlots;
public int ammoSlots;
public int armorSlots;
public int engineSlots;
public int storageSlots;

/**
 * must be called by spawner item initially, and is then called by loadFromNBT
 * @param upgrade
 * @param ammo
 * @param armor
 * @param engine
 * @param storage
 */
public void setInventorySizes(int upgrade, int ammo, int armor, int engine, int storage)
  {
  this.upgradeSlots = upgrade;
  this.ammoSlots = ammo;
  this.armorSlots = armor;
  this.engineSlots = engine;
  this.storageSlots = storage;
  this.upgradeInventory = new AWInventoryBasic(upgrade);
  this.ammoInventory = new AWInventoryBasic(ammo);
  this.armorInventory = new AWInventoryBasic(armor);
  this.engineInventory = new AWInventoryBasic(engine);
  this.storageInventory = new AWInventoryBasic(storage);
  this.isInventoryValid = true;
  }

/**
 * if inventory is valid, write this entire inventory to the passed tag
 * @param commonTag
 */
public void writeToNBT(NBTTagCompound commonTag)
  {
  NBTTagCompound tag = new NBTTagCompound();  
  if(!this.isInventoryValid)
    {
    return;
    }  
  tag.setInteger("uS", upgradeSlots);
  tag.setInteger("amS", ammoSlots);
  tag.setInteger("arS", armorSlots);
  tag.setInteger("eS", engineSlots);
  tag.setInteger("sS", storageSlots);
  tag.setCompoundTag("uI", this.upgradeInventory.getNBTTag());
  tag.setCompoundTag("amI", this.ammoInventory.getNBTTag());
  tag.setCompoundTag("arI", this.armorInventory.getNBTTag());
  tag.setCompoundTag("eI", this.engineInventory.getNBTTag());
  tag.setCompoundTag("sI", this.storageInventory.getNBTTag());
  commonTag.setTag("inv", tag);
  }

/**
 * blind read method, inv tag need not even be present
 * if present, will read the entire inventory from tag,
 * including setting initial inventory sizes
 * @param commonTag
 */
public void readFromNBT(NBTTagCompound commonTag)
  {  
  if(!commonTag.hasKey("inv"))
    {
    return;
    }
  NBTTagCompound tag = commonTag.getCompoundTag("inv");  
  this.upgradeSlots = tag.getInteger("uS");
  this.ammoSlots = tag.getInteger("amS");
  this.armorSlots = tag.getInteger("arS");
  this.engineSlots = tag.getInteger("eS");
  this.storageSlots = tag.getInteger("sS");
  this.setInventorySizes(upgradeSlots, ammoSlots, armorSlots, engineSlots, storageSlots);
  this.upgradeInventory.readFromNBT(tag.getCompoundTag("uI"));
  this.ammoInventory.readFromNBT(tag.getCompoundTag("amI"));
  this.armorInventory.readFromNBT(tag.getCompoundTag("arI"));
  this.engineInventory.readFromNBT(tag.getCompoundTag("eI"));
  this.storageInventory.readFromNBT(tag.getCompoundTag("sI"));
  
  }

public VehicleInventory(VehicleBase vehicle)
  {  
  this.vehicle = vehicle; 
  }

@Override
public void onInventoryChanged(IInventory changedInv)
  {
  
  }

}
