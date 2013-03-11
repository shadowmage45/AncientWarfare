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
package shadowmage.ancient_warfare.common.vehicles.helpers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.missiles.DamageType;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.armors.IVehicleArmorType;
import shadowmage.ancient_warfare.common.vehicles.upgrades.IVehicleUpgradeType;

public class VehicleUpgradeHelper implements INBTTaggable
{

/**
 * currently installed upgrades, will be iterated through linearly to call upgrade.applyEffects, multiple upgrades may have cumulative effects
 */
private List<IVehicleUpgradeType> upgrades = new ArrayList<IVehicleUpgradeType>();
private List<IVehicleArmorType> installedArmor = new ArrayList<IVehicleArmorType>();

/**
 * list of all upgrades that are valid for this vehicle, used by inventoryChecking to see whether it can be installed or not
 */
private List validUpgrades = new ArrayList<IVehicleUpgradeType>();
private List validArmorTypes = new ArrayList<IVehicleArmorType>();
private VehicleBase vehicle;

public VehicleUpgradeHelper(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

/**
 * SERVER ONLY
 */
public void updateUpgrades()
  {
  if(vehicle.worldObj.isRemote)
    {
    return;
    }
  this.upgrades.clear();
  List<IVehicleUpgradeType> upgrades = vehicle.inventory.getInventoryUpgrades();
  for(IVehicleUpgradeType up : upgrades)
    {
    if(this.validUpgrades.contains(up))
      {
      this.upgrades.add(up);
      }
    }
  NBTTagCompound tag = new NBTTagCompound();
  int len = this.upgrades.size();
  int[] upInts = new int[len];
  for(int i = 0; i < this.upgrades.size(); i++)
    {
    upInts[i] = this.upgrades.get(i).getUpgradeGlobalTypeNum();
    }
  
  tag.setIntArray("ints", upInts);
  
  this.installedArmor.clear();
  List<IVehicleArmorType> armors = vehicle.inventory.getInventoryArmor();
  for(IVehicleArmorType ar : armors)
    {
    if(this.validArmorTypes.contains(ar))
      {
      this.installedArmor.add(ar);
      }
    }
  int [] arInts = new int[this.installedArmor.size()];
  for(int i = 0; i < this.installedArmor.size(); i++)
    {
    arInts[i] = this.installedArmor.get(i).getGlobalArmorType();        
    }
  tag.setIntArray("ints2", arInts);  
  
  Packet02Vehicle pkt = new Packet02Vehicle();
  pkt.setParams(vehicle);
  pkt.setUpgradeData(tag);
  pkt.sendPacketToAllTrackingClients(vehicle);
  this.updateUpgradeStats();
  }

/**
 * CLIENT ONLY..receives the packet sent above, and sets upgrade list directly from registry
 */
public void handleUpgradePacketData(NBTTagCompound tag)
  {
  this.upgrades.clear();
  int[] upInts = tag.getIntArray("ints");
  for(int i = 0; i < upInts.length; i++)
    {
    int up = upInts[i];
    IVehicleUpgradeType upgrade = VehicleUpgradeRegistry.instance().getUpgrade(up);
    if(upgrade!=null)
      {
      this.upgrades.add(upgrade);
      }
    }
  
  this.installedArmor.clear();
  int[] arInts = tag.getIntArray("ints2");
  for(int i = 0; i < arInts.length; i++)
    {
    IVehicleArmorType armor = ArmorRegistry.instance().getArmorType(arInts[i]);
    if(armor!=null)
      {
      this.installedArmor.add(armor);
      }
    }
  
  this.updateUpgradeStats();
  }

/**
 * reset stats to base stats
 * iterate through upgrades, applying their effects each in turn (multiple same upgrades are cumulative)
 */
public void updateUpgradeStats()
  {
  vehicle.resetUpgradeStats();
  for(IVehicleUpgradeType upgrade : this.upgrades)
    {
    upgrade.applyVehicleEffects(vehicle);
    }
  for(IVehicleArmorType armor : this.installedArmor)
    {
    vehicle.currentExplosionResist += armor.getExplosiveDamageReduction();
    vehicle.currentFireResist += armor.getFireDamageReduction();
    vehicle.currentGenericResist += armor.getGeneralDamageReduction();
    vehicle.currentWeight += armor.getArmorWeight();
    }
  }

public void addValidArmor(IVehicleArmorType armor)
  {
  if(armor!=null && this.validArmorTypes.contains(armor))
    {
    this.validArmorTypes.add(armor);
    }
  }

public void addValidArmor(int type)
  {
  IVehicleArmorType armor = ArmorRegistry.instance().getArmorType(type);
  if(armor!=null && !this.validArmorTypes.contains(armor))
    {
    this.validArmorTypes.add(armor);
    }
  }

public void addValidUpgrade(IVehicleUpgradeType upgrade)
  {
  if(upgrade!=null && !this.validUpgrades.contains(upgrade))
    {
    this.validUpgrades.add(upgrade);
    }
  }

public void addValidUpgrade(int type)
  {
  IVehicleUpgradeType upgrade = VehicleUpgradeRegistry.instance().getUpgrade(type);
  if(upgrade!=null && !this.validUpgrades.contains(upgrade))
    {
    this.validUpgrades.add(upgrade);
    }
  }

public float getScaledDamage(DamageSource src, int amt)
  { 
  if(src==DamageType.explosiveMissile || src== DamageType.explosion || src==DamageType.explosion2)
    {
    return (float) amt * vehicle.currentExplosionResist;
    }
  else if(src==DamageType.fireMissile || src == DamageType.inFire || src== DamageType.lava || src== DamageType.onFire || src.isFireDamage())
    {
    return (float) amt * vehicle.currentFireResist;
    }  
  return (float) amt * vehicle.currentGenericResist;
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  int[] ints = new int[this.upgrades.size()];
  for(int i = 0; i < this.upgrades.size(); i++)
    {
    ints[i]=this.upgrades.get(i).getUpgradeGlobalTypeNum();
    }
  tag.setIntArray("ints", ints);
  
  int[] ints2 = new int[this.installedArmor.size()];
  for(int i = 0; i < this.installedArmor.size(); i++)
    {
    ints2[i]=this.installedArmor.get(i).getGlobalArmorType();
    }
  tag.setIntArray("ints2", ints2);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.upgrades.clear();
  int[] ints = tag.getIntArray("ints");
  for(int i = 0; i < ints.length; i++)
    {
    this.upgrades.add(VehicleUpgradeRegistry.instance().getUpgrade(ints[i]));
    }  
  this.installedArmor.clear();
  ints = tag.getIntArray("ints2");
  for(int i = 0; i < ints.length;i++)
    {
    this.installedArmor.add(ArmorRegistry.instance().getArmorType(ints[i]));
    }
  }

}
