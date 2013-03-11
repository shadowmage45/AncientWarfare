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
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.registry.entry.VehicleAmmoEntry;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.armors.IVehicleArmorType;
import shadowmage.ancient_warfare.common.vehicles.upgrades.IVehicleUpgradeType;

public class VehicleInventory implements IInventoryCallback
{


private VehicleBase vehicle;

/**
 * individual inventories
 * 
 */
public AWInventoryBasic upgradeInventory;// = new AWInventoryBasic(6);
public AWInventoryBasic ammoInventory;// = new AWInventoryBasic(6);
public AWInventoryBasic armorInventory;// = new AWInventoryBasic(6);
public AWInventoryBasic storageInventory;// = new AWInventoryBasic(27);

public VehicleInventory(VehicleBase vehicle)
  {  
  this.vehicle = vehicle;   
  }

/**
 * if inventory is valid, write this entire inventory to the passed tag
 * @param commonTag
 */
public void writeToNBT(NBTTagCompound commonTag)
  {
  NBTTagCompound tag = new NBTTagCompound();
  if(this.upgradeInventory!=null)
    {
    tag.setCompoundTag("uI", this.upgradeInventory.getNBTTag());
    }
  if(this.ammoInventory!=null)
    {
    tag.setCompoundTag("amI", this.ammoInventory.getNBTTag());
    }
  if(this.storageInventory!=null)
    {
    tag.setCompoundTag("sI", this.storageInventory.getNBTTag());
    }
  if(this.armorInventory!=null)
    {
    tag.setCompoundTag("arI", this.armorInventory.getNBTTag());
    }
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
  if(this.upgradeInventory!=null)
    {
    this.upgradeInventory.readFromNBT(tag.getCompoundTag("uI"));
    }
  if(this.ammoInventory!=null)
    {
    this.ammoInventory.readFromNBT(tag.getCompoundTag("amI"));
    }
  if(this.storageInventory!=null)
    {
    this.storageInventory.readFromNBT(tag.getCompoundTag("sI"));
    }
  if(this.armorInventory!=null)
    {
    this.armorInventory.readFromNBT(tag.getCompoundTag("arI"));
    }
  }

@Override
public void onInventoryChanged(IInventory changedInv, List<Integer> slotNums)
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

public List<IVehicleArmorType> getInventoryArmor()
  {
  ArrayList<IVehicleArmorType> armors = new ArrayList<IVehicleArmorType>();
  for(int i = 0; i < this.armorInventory.getSizeInventory(); i++)
    {
    ItemStack stack = this.armorInventory.getStackInSlot(i);
    IVehicleArmorType upgrade = ArmorRegistry.instance().getArmorForStack(stack);
    if(upgrade!=null)
      {
      armors.add(upgrade);
      }     
    }
  return armors;  
  }
  
public List<IVehicleUpgradeType> getInventoryUpgrades()
  {
  ArrayList<IVehicleUpgradeType> upgrades = new ArrayList<IVehicleUpgradeType>();
  for(int i = 0; i < this.upgradeInventory.getSizeInventory(); i++)
    {
    ItemStack stack = this.upgradeInventory.getStackInSlot(i);
    IVehicleUpgradeType upgrade = VehicleUpgradeRegistry.instance().getUpgrade(stack);
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
