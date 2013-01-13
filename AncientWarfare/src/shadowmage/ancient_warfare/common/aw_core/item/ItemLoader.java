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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.registry.DescriptionRegistry;
import shadowmage.ancient_warfare.common.aw_core.registry.entry.ItemIDPair;
import shadowmage.ancient_warfare.common.aw_structure.item.ItemBlockScanner;
import shadowmage.ancient_warfare.common.aw_structure.item.ItemDebugBuilder;
import shadowmage.ancient_warfare.common.aw_structure.item.ItemStructureScanner;
import shadowmage.ancient_warfare.common.aw_vehicles.item.ItemVehicleSpawner;
import shadowmage.ancient_warfare.common.aw_vehicles.registry.VehicleAmmoRegistry;
import shadowmage.ancient_warfare.common.aw_vehicles.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.aw_vehicles.registry.entry.VehicleAmmo;
import shadowmage.ancient_warfare.common.aw_vehicles.registry.entry.VehicleUpgrade;

public class ItemLoader
{

/**
 * Items
 */
public static AWItemBase vehicleUpgrade = new AWItemBase(Config.getItemID("itemMulti.vehicleUpgrade", 13001, "Base item for all vehicle upgrades"),true);
public static AWItemBase vehicleAmmo = new AWItemBase(Config.getItemID("itemMulti.vehicleAmmo", 13002, "Base item for all vehicle ammunition types"),true);
public static AWItemBase vehicleSpawner = new ItemVehicleSpawner(Config.getItemID("itemMulti.vehicleSpawner", 13003, "Base item for all vehicle-spawning items"));
public static AWItemBase componentItem = new AWItemBase(Config.getItemID("itemMulti.component", 13004, "Base item for all components and misc items"), true);
public static AWItemBase structureScanner = new ItemStructureScanner(Config.getItemID("itemSingle.structureScanner", 13005, "Item used to scan structures"));

/**
 * debug items, will only be given instances if debug is enabled in Config
 * e.g. will be null unless debug mode is on
 */
public static AWItemBase blockScanner;
public static AWItemBase debugBuilder;

private static ItemLoader INSTANCE;
private ItemLoader(){}
public static ItemLoader instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new ItemLoader();
    }
  return INSTANCE;
  }


/**
 * initial load, called during pre-init from mod core file
 */
public void load()
  {
  this.loadItems();
  this.loadRecipes();  
  this.loadDebugItems();
  }

private void loadItems()
  {  
  this.registerItemSingle(structureScanner, "Structure Scanner", "Structure Scanner", "Structure Scanning Item, Right-Click to Use");
  this.registerItemWithSubtypes(componentItem);
  this.registerItemWithSubtypes(vehicleAmmo);
  this.registerItemWithSubtypes(vehicleSpawner);
  this.registerItemWithSubtypes(vehicleUpgrade);
  }

private void loadRecipes()
  {
  //TODO create recipes..figure out crafting..blahblah..
  }

private void loadDebugItems()
  {
  if(!Config.DEBUG)
    {
    return;
    }
  blockScanner = new ItemBlockScanner(Config.getItemID("debug.blockScanner", 9000));
  debugBuilder = new ItemDebugBuilder(Config.getItemID("debug.builder", 9001));
  this.registerItemSingle(blockScanner, "Block Scanner", "Block Scanning Tool","Sneak-Right-Click to get BlockID/Meta from clicked-on block");
  this.registerItemSingle(debugBuilder, "Debug Builder", "Debug Structure Builder", "Right-Click to construct the most recently scanned building");
  }

/**
 * special registerUpgrade method, directly registers a new upgrade using the vehicleUpgrade item.  calls all necessary calls
 * for languageRegistry, descriptionRegistry, etc----null upgrades are not registered
 * @param dmg
 * @param type
 * @param upgrade
 */
private void registerVehicleUpgradeItem(int dmg, int type, VehicleUpgrade upgrade)
  {
  if(upgrade!=null)
    {    
    this.addSubtypeToItem(vehicleUpgrade, dmg, upgrade.getUpgradeDisplayName());
    VehicleUpgradeRegistry.instance().registerUpgrade(dmg, type, upgrade);
    }
  }

/**
 * special itemRegister function that will create the necessary items and add them to all registers
 * @param dmg
 * @param type
 * @param name
 * @param displayName
 */
private void registerVehicleAmmoItem(int dmg, int type, String name, String displayName)
  {
  ItemIDPair pair = new ItemIDPair(vehicleAmmo.itemID, dmg,true);
  VehicleAmmo entry = new VehicleAmmo(pair, name, displayName,type);
  this.addSubtypeToItem(vehicleAmmo, dmg, displayName);
  VehicleAmmoRegistry.instance().registerAmmoType(entry);
  }

private ItemStack createVehicleAmmoSubtype(int dmg, int type, String name, String displayName)
  {
  this.registerVehicleAmmoItem(dmg, type, name, displayName);
  return new ItemStack(vehicleAmmo.itemID,1,dmg);
  }

private ItemStack createVehicleUpgradeSubtype(int dmg, int type, VehicleUpgrade upgrade)
  {
  this.registerVehicleUpgradeItem(dmg, type, upgrade);
  return new ItemStack(vehicleUpgrade.itemID,1,dmg);
  }

private ItemStack createItemSubtype(AWItemBase item, int dmg, String name)
  {
  this.addSubtypeToItem(item, dmg, name);
  return new ItemStack(item.itemID,1,dmg);
  }

private void registerItemSingle(Item item, String name)
  {
  this.registerItemSingle(item, name, "");
  }

private void registerItemSingle(Item item, String name, String description)
  {
  DescriptionRegistry.instance().registerItemSingle(item, name, description);
  }

private void registerItemSingle(Item item, String name, String description, String tooltip)
  {
  this.registerItemSingle(item, name, description);
  DescriptionRegistry.instance().setToolTip(item.itemID, tooltip);
  }

private void registerItemWithSubtypes(Item item)
  {
  DescriptionRegistry.instance().registerItemWithSubtypes(item.itemID);
  }

private void addSubtypeToItem(AWItemBase item, int dmg, String name)
  {
  item.addSubType(new ItemStack(item.itemID,1,dmg));
  DescriptionRegistry.instance().addSubtypeToItem(item.itemID, dmg, name);
  }

private void addSubtypeToItem(AWItemBase item, int dmg, String name, String tooltip)
  {
  this.addSubtypeToItem(item, dmg, name);
  DescriptionRegistry.instance().setTooltip(item.itemID, dmg, tooltip);
  }

}
