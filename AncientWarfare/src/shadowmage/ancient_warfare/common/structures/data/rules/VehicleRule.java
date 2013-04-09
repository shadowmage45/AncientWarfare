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
package shadowmage.ancient_warfare.common.structures.data.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.structures.data.ScannedEntityEntry;
import shadowmage.ancient_warfare.common.utils.StringTools;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.armors.IVehicleArmorType;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;
import shadowmage.ancient_warfare.common.vehicles.upgrades.IVehicleUpgradeType;

public class VehicleRule
{

int bx;
int by;
int bz;
float ox;
float oy;
float oz;
float rotation;
float pitch;
public short vehicleType;
public short vehicleRank;
public byte[] armorTypes;
public byte[] upgradeTypes;
public byte[] ammoTypes;

private VehicleRule()
  {  
  }

public static VehicleRule populateRule(ScannedEntityEntry entry, VehicleBase vehicle)
  {
  VehicleRule rule = new VehicleRule();
  rule.bx = entry.bx;
  rule.by = entry.by;
  rule.bz = entry.bz;
  rule.ox = entry.ox;
  rule.oy = entry.oy;
  rule.oz = entry.oz;
  rule.rotation = entry.r;
  rule.pitch = entry.p;
  rule.vehicleType = (short) vehicle.vehicleType.getGlobalVehicleType();
  rule.vehicleRank = (short) vehicle.vehicleMaterialLevel;
  rule.armorTypes = new byte[vehicle.inventory.armorInventory.getSizeInventory()];
  rule.upgradeTypes = new byte[vehicle.inventory.upgradeInventory.getSizeInventory()];
  rule.ammoTypes = new byte[vehicle.inventory.upgradeInventory.getSizeInventory()];
  ItemStack stack;
  IVehicleArmorType armor;
  IVehicleUpgradeType upgrade;
  IAmmoType ammo;
  int i;
  for(i = 0; i < vehicle.inventory.armorInventory.getSizeInventory(); i++)
    {
    stack = vehicle.inventory.armorInventory.getStackInSlot(i);
    armor = ArmorRegistry.instance().getArmorForStack(stack);
    if(armor!=null)
      {
      rule.armorTypes[i] = (byte) armor.getGlobalArmorType();
      }
    }
  for(i = 0; i < vehicle.inventory.upgradeInventory.getSizeInventory(); i++)
    {
    stack = vehicle.inventory.upgradeInventory.getStackInSlot(i);
    upgrade = VehicleUpgradeRegistry.instance().getUpgrade(stack);
    if(upgrade!=null)
      {
      rule.upgradeTypes[i] = (byte)upgrade.getUpgradeGlobalTypeNum();
      }
    }
  for(i = 0; i < vehicle.inventory.ammoInventory.getSizeInventory(); i++)
    {
    stack = vehicle.inventory.ammoInventory.getStackInSlot(i);
    ammo = AmmoRegistry.instance().getAmmoForStack(stack);
    if(stack!=null)
      {
      rule.ammoTypes[i] = (byte) ammo.getAmmoType();
      }
    }
  return rule;
  }

public static VehicleRule parseRule(List<String> ruleLines)
  {
  String line;
  VehicleRule rule = new VehicleRule();
  Iterator<String> it = ruleLines.iterator();
  while(it.hasNext())
    {
    line = it.next();   
    if(line.toLowerCase().startsWith("type"))
      {
      rule.vehicleType = StringTools.safeParseShort("=", line);     
      }
    else if(line.toLowerCase().startsWith("rank"))
      {
      rule.vehicleType = StringTools.safeParseShort("=", line);     
      }
    else if(line.toLowerCase().startsWith("armors"))
      {
      rule.armorTypes = StringTools.safeParseByteArray("=", line);      
      }    
    else if(line.toLowerCase().startsWith("upgrades"))
      {
      rule.upgradeTypes = StringTools.safeParseByteArray("=", line);
      }   
    else if(line.toLowerCase().startsWith("ammos"))
      {
      rule.ammoTypes = StringTools.safeParseByteArray("=", line);      
      }  
    else if(line.toLowerCase().startsWith("bx"))
      {
      rule.bx = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("by"))
      {
      rule.by = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("bz"))
      {
      rule.bz = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("ox"))
      {
      rule.ox = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("oy"))
      {
      rule.oy = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("oz"))
      {
      rule.oz = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("rotation"))
      {
      rule.rotation = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("pitch"))
      {
      rule.pitch = StringTools.safeParseFloat("=", line);
      }
    } 
  return rule;
  }

public List<String> getRuleLines()
  {
  ArrayList<String> lines = new ArrayList<String>();
  lines.add("vehicle:");
  lines.add("type="+this.vehicleType);
  lines.add("rank="+this.vehicleRank);
  lines.add("ammos="+StringTools.getCSVStringForArray(ammoTypes));
  lines.add("upgrades="+StringTools.getCSVStringForArray(upgradeTypes));
  lines.add("armors="+StringTools.getCSVStringForArray(armorTypes));
  lines.add("bx="+bx);
  lines.add("by="+by);
  lines.add("bz="+bz);
  lines.add("ox="+ox);
  lines.add("oy="+oy);
  lines.add("oz="+oz);
  lines.add("rotation="+rotation);
  lines.add("pitch="+pitch);
  lines.add(":endvehicle");
  return lines;
  }

}
