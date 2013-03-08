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

import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.registry.entry.VehicleUpgrade;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleUpgradeHelper implements INBTTaggable
{

/**
 * currently installed upgrades, will be iterated through linearly to call upgrade.applyEffects, multiple upgrades may have cumulative effects
 */
private List<VehicleUpgrade> upgrades = new ArrayList<VehicleUpgrade>();

/**
 * list of all upgrades that are valid for this vehicle, used by inventoryChecking to see whether it can be installed or not
 */
private List validUpgrades = new ArrayList<VehicleUpgrade>();
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
  this.upgrades.clear();
  List<VehicleUpgrade> upgrades = vehicle.inventory.getInventoryUpgrades();
  for(VehicleUpgrade up : upgrades)
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
    upInts[i] = this.upgrades.get(i).getGlobalUpgradeNum();
    }  
  tag.setIntArray("ints", upInts);
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
    VehicleUpgrade upgrade = VehicleUpgradeRegistry.instance().getUpgrade(up);
    if(upgrade!=null)
      {
      this.upgrades.add(upgrade);
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
  for(VehicleUpgrade upgrade : this.upgrades)
    {
    upgrade.applyUpgradeEffects(vehicle);
    }
  }

public void addValidUpgrade(VehicleUpgrade upgrade)
  {
  if(upgrade!=null && !this.validUpgrades.contains(upgrade))
    {
    this.validUpgrades.add(upgrade);
    }
  }

public void addValidUpgrade(int type)
  {
  VehicleUpgrade upgrade = VehicleUpgradeRegistry.instance().getUpgrade(type);
  if(upgrade!=null && !this.validUpgrades.contains(upgrade))
    {
    this.validUpgrades.add(upgrade);
    }
  }

public void addValidUpgrade(String name)
  {
  VehicleUpgrade upgrade = VehicleUpgradeRegistry.instance().getUpgrade(name);
  if(upgrade!=null && !this.validUpgrades.contains(upgrade))
    {
    this.validUpgrades.add(upgrade);
    }
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  int[] ints = new int[this.upgrades.size()];
  for(int i = 0; i < this.upgrades.size(); i++)
    {
    ints[i]=this.upgrades.get(i).getGlobalUpgradeNum();
    }
  tag.setIntArray("ints", ints);
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
  }

}
