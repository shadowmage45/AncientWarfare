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
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class ItemLoader
{

/**
 * Items
 */
public static final AWItemBase vehicleUpgrade = new ItemVehicleUpgrade(Config.getItemID("itemMulti.vehicleUpgrade", 13001, "Base item for all vehicle upgrades"));
public static final AWItemBase ammoItem = new ItemAmmo(Config.getItemID("itemMulti.vehicleAmmo", 13002, "Base item for all vehicle ammunition types"));
public static final AWItemBase vehicleSpawner = new ItemVehicleSpawner(Config.getItemID("itemMulti.vehicleSpawner", 13003, "Base item for all vehicle-spawning items"));
public static final AWItemBase componentItem = new ItemComponent(Config.getItemID("itemMulti.component", 13004, "Base item for all components and misc items"), true);
public static final AWItemBase structureScanner = new ItemStructureScanner(Config.getItemID("itemSingle.structureScanner", 13005, "Item used to scan structures"));
public static final AWItemBase structureCreativeBuilder = new ItemBuilderInstant(Config.getItemID("itemSingle.structureBuilderCreative", 13006, "Creative-Mode Selectable Structure Builder"));
public static final AWItemBase structureBuilderDirect = new ItemBuilderDirect(Config.getItemID("itemSingle.builderDirect", 13007, "Survival mode builder, uses blocks from inventory"));
public static final AWItemBase structureCreativeBuilderTicked = new ItemBuilderTicked(Config.getItemID("itemSingle.structureBuilderCreativeTicked", 13008, "Creative-mode slow (ticked) builder"));
public static final AWItemBase structureEditor = new ItemCreativeEditor(Config.getItemID("itemSingle.templateEditor", 13009, "Creative-mode template editor"));
public static final AWItemBase armorItem = new ItemVehicleArmor(Config.getItemID("itemMulti.vehicleArmor", 13010, "Vehicle Armor Component"));
public static final AWItemBase npcSpawner = new ItemNpcSpawner(Config.getItemID("itemMulti.npcSpawner", 13011, "Npc Spawning Item"));
public static final AWItemBase npcCommandBaton = new ItemNpcCommandBaton(Config.getItemID("itemMulti.commandBaton", 13012, "Npc Command Batons"));
public static final AWItemBase civicPlacer = new ItemCivicPlacer(Config.getItemID("itemMulti.civiPlacer", 13013, "Constructs Civic Buildings/Sites"));
public static final AWItemBase civicBuilder = new ItemCivicBuilder(Config.getItemID("itemMulti.civicBuilder", 13014, "Constructs Structures using Civics"));
public static final AWItemBase courierRouteSlip = new ItemCourierSlip(Config.getItemID("itemMulti.courierSlip", 13015, "Holds Routing Info for a Courier"));
public static final AWItemBase gateSpawner = new ItemGateSpawner(Config.getItemID("itemMulti.gateSpawner", 13016, "Base gate spawning item."));
public static final ItemFood rations = new ItemRation(Config.getItemID("itemSingle.foodRation", 13017, "Food rations for soldiers and npcs."));
public static final AWItemBase researchBook = new ItemResearchBook(Config.getItemID("itemSingle.researchBook", 13017, "Research book to save research progress"));
public static final AWItemBase researchNotes = new ItemComponent(Config.getItemID("itemMulti.researchNotes", 13018, "Research notes"), true);

public static final ItemStack wood1 = new ItemStack(componentItem, 1, 0);
public static final ItemStack wood2 = new ItemStack(componentItem, 1, 1);
public static final ItemStack wood3 = new ItemStack(componentItem, 1, 2);
public static final ItemStack wood4 = new ItemStack(componentItem, 1, 3);
public static final ItemStack iron1 = new ItemStack(componentItem, 1, 4);
public static final ItemStack iron2 = new ItemStack(componentItem, 1, 5);
public static final ItemStack iron3 = new ItemStack(componentItem, 1, 6);
public static final ItemStack iron4 = new ItemStack(componentItem, 1, 7);
public static final ItemStack iron5 = new ItemStack(componentItem, 1, 8);

public static final ItemStack flameCharge = new ItemStack(componentItem, 1, 9);//
public static final ItemStack explosiveCharge = new ItemStack(componentItem, 1, 10);//
public static final ItemStack rocketCharge = new ItemStack(componentItem, 1, 11);//
public static final ItemStack clusterCharge = new ItemStack(componentItem, 1, 12);//
public static final ItemStack napalmCharge = new ItemStack(componentItem, 1, 13);
public static final ItemStack clayCasing = new ItemStack(componentItem, 1, 14);//
public static final ItemStack ironCasing = new ItemStack(componentItem, 1, 15);//
public static final ItemStack mobilityUnit = new ItemStack(componentItem, 1, 16);//
public static final ItemStack turretComponents = new ItemStack(componentItem, 1, 17);//
public static final ItemStack torsionUnit = new ItemStack(componentItem, 1, 18);//
public static final ItemStack counterWeightUnit = new ItemStack(componentItem, 1, 19);//
public static final ItemStack powderCase = new ItemStack(componentItem, 1, 20);//
public static final ItemStack equipmentBay = new ItemStack(componentItem, 1, 21);//
public static final ItemStack ironRings = new ItemStack(componentItem, 1, 22);//

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
  researchNotes.setCreativeTab(null);
  }

private void loadItems()
  {  
  this.registerItemSingle(structureCreativeBuilderTicked, "Creative Builder Ticked", "Creative Mode Building Tool with Builder Block", "Creative Template Builder").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
  this.registerItemSingle(structureScanner, "Structure Scanner", "Structure Scanner", "Creative Template Creator").setIconTexture("ancientwarfare:builder/structureScanner1", 0);
  this.registerItemSingle(structureCreativeBuilder, "Creative Builder", "Creative Mode Building Tool", "Creative Template Builder").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
  this.registerItemSingle(structureBuilderDirect, "Structure Builder Direct", "Survival Mode Quick Building Tool", "Scans and Builds from inventory").setIconTexture("ancientwarfare:builder/structureScanner1", 0);
  this.registerItemSingle(structureEditor, "Structure Editor", "Structure Template Editor", "Right-Click to open editor GUI").setIconTexture("ancientwarfare:builder/testIcon1", 0);
  this.registerItemSubtyped(ammoItem);
  this.registerItemSubtyped(vehicleSpawner);
  this.registerItemSubtyped(vehicleUpgrade);
  this.registerItemSubtyped(armorItem);
  this.registerItemSubtyped(npcSpawner);
  this.registerItemSubtyped(npcCommandBaton);
  this.addSubtypeInfoToItem(npcCommandBaton, 0, "Simple Command Baton", "","Command following NPCs").addDisplayStack(new ItemStack(npcCommandBaton,1,0)).setIconTexture("ancientwarfare:npc/baton1", 0);
  this.addSubtypeInfoToItem(npcCommandBaton, 1, "Master Command Baton", "","Command NPCs in area").addDisplayStack(new ItemStack(npcCommandBaton,1,1)).setIconTexture("ancientwarfare:npc/baton3", 1);
  this.registerItemSubtyped(civicPlacer);
  this.registerItemSingle(civicBuilder, "Civic Structure Builder", "", "Construction Site");
  this.registerItemSubtyped(courierRouteSlip);
  this.addSubtypeInfoToItem(courierRouteSlip, 0, "Basic Routing Slip","","4 Routing Slots").addDisplayStack(new ItemStack(courierRouteSlip,1,0)).setIconTexture("ancientwarfare:npc/route1", 0);
  this.addSubtypeInfoToItem(courierRouteSlip, 1, "Intermediate Routing Slip","","6 Routing Slots").addDisplayStack(new ItemStack(courierRouteSlip,1,1)).setIconTexture("ancientwarfare:npc/route2", 1);
  this.addSubtypeInfoToItem(courierRouteSlip, 2, "Advanced Routing Slip","","8 Routing Slots").addDisplayStack(new ItemStack(courierRouteSlip,1,2)).setIconTexture("ancientwarfare:npc/route3", 2);
  this.registerItemSubtyped(gateSpawner);
  this.registerItemSingle(rations, "Food Ration", "", "Restores 1 Hunger (2 Upkeep value)");
  
  this.registerItemSingle(researchBook, "Research Book", "", "Records research progress").setIconTexture("ancientwarfare:misc/researchBook", 0);
  
  /**
   * register main component item (misc random items) 
   */
  this.registerItemSubtyped(componentItem);
  this.addSubtypeInfoToItem(componentItem, 0, "Rough Wood Materials").addDisplayStack(wood1).setIconTexture("ancientwarfare:misc/materialWood1", 0);
  this.addSubtypeInfoToItem(componentItem, 1, "Treated Wood Materials").addDisplayStack(wood2).setIconTexture("ancientwarfare:misc/materialWood2", 1);
  this.addSubtypeInfoToItem(componentItem, 2, "IronShod Wood Materials").addDisplayStack(wood3).setIconTexture("ancientwarfare:misc/materialWood3", 2);
  this.addSubtypeInfoToItem(componentItem, 3, "Iron Cored Wood Materials").addDisplayStack(wood4).setIconTexture("ancientwarfare:misc/materialWood4", 3);
  this.addSubtypeInfoToItem(componentItem, 4, "Rough Iron Materials").addDisplayStack(iron1).setIconTexture("ancientwarfare:misc/materialIron1", 4);
  this.addSubtypeInfoToItem(componentItem, 5, "Fine Iron Materials").addDisplayStack(iron2).setIconTexture("ancientwarfare:misc/materialIron2", 5);
  this.addSubtypeInfoToItem(componentItem, 6, "Tempered Iron Materials").addDisplayStack(iron3).setIconTexture("ancientwarfare:misc/materialIron3", 6);
  this.addSubtypeInfoToItem(componentItem, 7, "Minor Alloy Iron Materials").addDisplayStack(iron4).setIconTexture("ancientwarfare:misc/materialIron4", 7);
  this.addSubtypeInfoToItem(componentItem, 8, "Alloy Materials").addDisplayStack(iron5).setIconTexture("ancientwarfare:misc/materialIron5", 8);
  this.addSubtypeInfoToItem(componentItem, 9, "Flame Charge").addDisplayStack(flameCharge).setIconTexture("ancientwarfare:ammo/ammoFlameCharge", 9);
  this.addSubtypeInfoToItem(componentItem, 10, "Explosive Charge").addDisplayStack(explosiveCharge).setIconTexture("ancientwarfare:ammo/ammoExplosiveCharge", 10);
  this.addSubtypeInfoToItem(componentItem, 11, "Rocket Charge").addDisplayStack(rocketCharge).setIconTexture("ancientwarfare:ammo/ammoRocketCharge", 11);
  this.addSubtypeInfoToItem(componentItem, 12, "Cluster Charge").addDisplayStack(clusterCharge).setIconTexture("ancientwarfare:ammo/ammoClusterCharge", 12);
  this.addSubtypeInfoToItem(componentItem, 13, "Napalm Charge").addDisplayStack(napalmCharge).setIconTexture("ancientwarfare:ammo/ammoNapalmCharge", 13);
  this.addSubtypeInfoToItem(componentItem, 14, "Clay Casing").addDisplayStack(clayCasing).setIconTexture("ancientwarfare:ammo/ammoClayCasing", 14);
  this.addSubtypeInfoToItem(componentItem, 15, "Iron Casing").addDisplayStack(ironCasing).setIconTexture("ancientwarfare:ammo/ammoIronCasing", 15);
  this.addSubtypeInfoToItem(componentItem, 16, "Mobility Unit").addDisplayStack(mobilityUnit).setIconTexture("ancientwarfare:misc/vehicleMobilityUnit", 16);
  this.addSubtypeInfoToItem(componentItem, 17, "Turret Unit").addDisplayStack(turretComponents).setIconTexture("ancientwarfare:misc/vehicleTurretUnit", 17);
  this.addSubtypeInfoToItem(componentItem, 18, "Torsion Unit").addDisplayStack(torsionUnit).setIconTexture("ancientwarfare:misc/vehicleTorsionUnit", 18);
  this.addSubtypeInfoToItem(componentItem, 19, "CounterWeight Unit").addDisplayStack(counterWeightUnit).setIconTexture("ancientwarfare:misc/vehicleCounterWeightUnit", 19);
  this.addSubtypeInfoToItem(componentItem, 20, "Powder Case").addDisplayStack(powderCase).setIconTexture("ancientwarfare:misc/vehiclePowderUnit", 20);
  this.addSubtypeInfoToItem(componentItem, 21, "Equipment Bay").addDisplayStack(equipmentBay).setIconTexture("ancientwarfare:misc/vehicleEquipmentBay", 21);
  this.addSubtypeInfoToItem(componentItem, 22, "Iron Rings").addDisplayStack(ironRings).setIconTexture("ancientwarfare:misc/ironRings", 22);
  }

private void loadRecipes()
  {
  //TODO load any normal vanilla style recipes here.  Check config to see if should enable basic recipes for other things (vehicles, ammo, npcs, civics, upgrades, armor, wetf)
  }

private void loadDebugItems()
  {
  if(!Config.DEBUG)
    {
    return;
    }  
  blockScanner = new ItemBlockScanner(Config.getItemID("debug.blockScanner", 9000));
  this.registerItemSingle(blockScanner, "Block Scanner", "Block Scanning Tool","Display id/meta from block clicked.").setIconTexture("ancientwarfare:testIcon1", 0);
  }

public Description registerItemSubtyped(AWItemBase item)
  {
  Description d = DescriptionRegistry2.instance().registerItem(item, false);
  return d;
  }

public Description registerItemSingle(Item item, String name, String desc, String tip)
  {
  Description d = DescriptionRegistry2.instance().registerItem(item, true);
  d.setName(name, 0);
  d.setDescription(desc, 0);
  d.addTooltip(tip, 0);  
  return d;
  }

public Description addSubtypeInfoToItem(AWItemBase item, int damage, String name, String desc, String tooltip)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(item.itemID);
  if(d!=null)
    {
    d.setName(name, damage);
    d.setDescription(desc, damage);
    d.addTooltip(tooltip, damage);  
    }  
  return d;
  }

public Description addSubtypeInfoToItem(AWItemBase item, int damage, String name)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(item.itemID);
  if(d!=null)
    {
    d.setName(name, damage);   
    }  
  return d;
  }

public Description addSubtypeInfoWithIconTexture(AWItemBase item, int damage, String name, String texture)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(item.itemID);
  if(d!=null)
    {
    d.setName(name, damage);   
    d.setIconTexture(texture, damage);
    }  
  return d;
  }
}
