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
package shadowmage.ancient_warfare.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry;

public class ItemLoader
{

/**
 * Items
 */
public static final AWItemBase vehicleUpgrade = new ItemVehicleUpgrade(Config.getItemID("itemMulti.vehicleUpgrade", 13001, "Base item for all vehicle upgrades"));
public static final AWItemBase ammoItem = new ItemAmmo(Config.getItemID("itemMulti.vehicleAmmo", 13002, "Base item for all vehicle ammunition types"));
public static final AWItemBase vehicleSpawner = new ItemVehicleSpawner(Config.getItemID("itemMulti.vehicleSpawner", 13003, "Base item for all vehicle-spawning items"));
public static final AWItemBase componentItem = new AWItemBase(Config.getItemID("itemMulti.component", 13004, "Base item for all components and misc items"), true);
public static final AWItemBase structureScanner = new ItemStructureScanner(Config.getItemID("itemSingle.structureScanner", 13005, "Item used to scan structures"));
public static final AWItemBase structureCreativeBuilder = new ItemBuilderInstant(Config.getItemID("itemSingle.structureBuilderCreative", 13006, "Creative-Mode Selectable Structure Builder"));
public static final AWItemBase structureBuilderDirect = new ItemBuilderDirect(Config.getItemID("itemSingle.builderDirect", 13007, "Survival mode builder, uses blocks from inventory"));
public static final AWItemBase structureCreativeBuilderTicked = new ItemBuilderTicked(Config.getItemID("itemSingle.structureBuilderCreativeTicked", 13008, "Creative-mode slow (ticked) builder"));
public static final AWItemBase structureEditor = new ItemCreativeEditor(Config.getItemID("itemSingle.templateEditor", 13009, "Creative-mode template editor"));
public static final AWItemBase armorItem = new ItemVehicleArmor(Config.getItemID("itemMulti.vehicleArmor", 13010, "Vehicle Armor Component"));
public static final AWItemBase npcSpawner = new ItemNpcSpawner(Config.getItemID("itemMulti.npcSpawner", 13010, "Npc Spawning Item"));

/**
 * debug items, will only be given instances if debug is enabled in Config
 * e.g. will be null unless debug mode is on
 */
public static AWItemBase blockScanner;


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
  this.registerItemSingle(structureCreativeBuilderTicked, "Creative Builder Ticked", "Creative Mode Building Tool with Builder Block", "Right-Click to Use, Sneak+Right-Click to open GUI");
  this.registerItemSingle(structureScanner, "Structure Scanner", "Structure Scanner", "Structure Scanning Item, Right-Click to Use");
  this.registerItemSingle(structureCreativeBuilder, "Creative Builder", "Creative Mode Building Tool", "Right-Click to Build, Sneak+Right-Click to open GUI");
  this.registerItemSingle(structureBuilderDirect, "Structure Builder Direct", "Survival Mode Quick Building Tool", "Right-Click to Scan, and then Build");
  this.registerItemSingle(structureEditor, "Structure Editor", "Structure Template Editor", "Right-Click to open editor selection GUI");
  this.registerItemWithSubtypes(componentItem);
  this.registerItemWithSubtypes(ammoItem);
  this.registerItemWithSubtypes(vehicleSpawner);
  this.registerItemWithSubtypes(vehicleUpgrade);
  this.registerItemWithSubtypes(armorItem);
  this.registerItemWithSubtypes(npcSpawner);
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
  this.registerItemSingle(blockScanner, "Block Scanner", "Block Scanning Tool","Sneak-Right-Click to get BlockID/Meta from clicked-on block");
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

public void addSubtypeToItem(AWItemBase item, int dmg, String name, String tooltip)
  {
  this.addSubtypeToItem(item, dmg, name);
  DescriptionRegistry.instance().setTooltip(item.itemID, dmg, tooltip);
  }

}
