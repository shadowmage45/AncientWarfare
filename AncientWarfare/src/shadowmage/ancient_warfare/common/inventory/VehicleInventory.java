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
package shadowmage.ancient_warfare.common.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.interfaces.IInventoryCallback;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.registry.entry.VehicleAmmoEntry;
import shadowmage.ancient_warfare.common.registry.entry.VehicleUpgrade;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleInventory implements IInventoryCallback
{


private VehicleBase vehicle;

/**
 * individual inventories
 * 
 */
public AWInventoryBasic upgradeInventory = new AWInventoryBasic(6);
public AWInventoryBasic ammoInventory = new AWInventoryBasic(6);
public AWInventoryBasic storageInventory = new AWInventoryBasic(27);

public VehicleInventory(VehicleBase vehicle)
  {  
  this.vehicle = vehicle; 
  this.upgradeInventory.addCallback(this);
  this.ammoInventory.addCallback(this);
  this.storageInventory.addCallback(this);
  }

/**
 * if inventory is valid, write this entire inventory to the passed tag
 * @param commonTag
 */
public void writeToNBT(NBTTagCompound commonTag)
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setCompoundTag("uI", this.upgradeInventory.getNBTTag());
  tag.setCompoundTag("amI", this.ammoInventory.getNBTTag());
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
  this.upgradeInventory.readFromNBT(tag.getCompoundTag("uI"));
  this.ammoInventory.readFromNBT(tag.getCompoundTag("amI"));
  this.storageInventory.readFromNBT(tag.getCompoundTag("sI"));  
  }

@Override
public void onInventoryChanged(IInventory changedInv)
  {
  if(changedInv == this.ammoInventory && !vehicle.worldObj.isRemote)
    {
    vehicle.ammoHelper.updateAmmoCounts();
    }
  else if(changedInv == this.upgradeInventory && !vehicle.worldObj.isRemote)
    {
    vehicle.upgradeHelper.updateUpgrades();
    }  
  }

public List<VehicleUpgrade> getInventoryUpgrades()
  {
  ArrayList<VehicleUpgrade> upgrades = new ArrayList<VehicleUpgrade>();
  for(int i = 0; i < this.upgradeInventory.getSizeInventory(); i++)
    {
    ItemStack stack = this.upgradeInventory.getStackInSlot(i);
    VehicleUpgrade upgrade = VehicleUpgradeRegistry.instance().getUpgrade(stack);
    if(upgrade!=null)
      {
      upgrades.add(upgrade);
      }     
    }
  return upgrades;  
  }

public List<VehicleAmmoEntry> getAmmoCounts()
  {
  ArrayList<VehicleAmmoEntry> counts = new ArrayList<VehicleAmmoEntry>();
  for(int i = 0; i < this.ammoInventory.getSizeInventory(); i++)
    {
    ItemStack stack = this.ammoInventory.getStackInSlot(i);
    IAmmoType ammo = AmmoRegistry.instance().getAmmoForStack(stack);
    if(ammo!=null)
      {
      boolean found = false;
      for(VehicleAmmoEntry ent : counts)
        {
        if(ent.baseAmmoType == ammo)
          {
          found = true;
          ent.ammoCount += stack.stackSize;
          break;
          }
        }
      if(!found)
        {
        counts.add(new VehicleAmmoEntry(ammo));
        counts.get(counts.size()-1).ammoCount+=stack.stackSize;
        }
      }
    }  
  return counts;
  }

}
