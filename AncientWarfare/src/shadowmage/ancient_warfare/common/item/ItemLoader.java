/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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

import cpw.mods.fml.common.registry.GameRegistry;
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
public static final AWItemBase vehicleUpgrade = new ItemVehicleUpgrade(Config.getItemID("itemMulti.vehicleUpgrade", 24001, "Base item for all vehicle upgrades"));
public static final AWItemBase ammoItem = new ItemAmmo(Config.getItemID("itemMulti.vehicleAmmo", 24002, "Base item for all vehicle ammunition types"));
public static final AWItemBase vehicleSpawner = new ItemVehicleSpawner(Config.getItemID("itemMulti.vehicleSpawner", 24003, "Base item for all vehicle-spawning items"));
public static final AWItemBase componentItem = new ItemComponent(Config.getItemID("itemMulti.component", 24004, "Base item for all components and misc items"), true);
public static final AWItemBase structureScanner = new ItemStructureScanner(Config.getItemID("itemSingle.structureScanner", 24005, "Item used to scan structures"));
public static final AWItemBase structureCreativeBuilder = new ItemBuilderInstant(Config.getItemID("itemSingle.structureBuilderCreative", 24006, "Creative-Mode Selectable Structure Builder"));
public static final AWItemBase structureBuilderDirect = new ItemBuilderDirect(Config.getItemID("itemSingle.builderDirect", 24007, "Survival mode builder, uses blocks from inventory"));
public static final AWItemBase structureCreativeBuilderTicked = new ItemBuilderTicked(Config.getItemID("itemSingle.structureBuilderCreativeTicked", 24008, "Creative-mode slow (ticked) builder"));
public static final AWItemBase structureEditor = new ItemCreativeEditor(Config.getItemID("itemSingle.templateEditor", 24009, "Creative-mode template editor"));
public static final AWItemBase armorItem = new ItemVehicleArmor(Config.getItemID("itemMulti.vehicleArmor", 24010, "Vehicle Armor Component"));
public static final AWItemBase npcSpawner = new ItemNpcSpawner(Config.getItemID("itemMulti.npcSpawner", 24011, "Npc Spawning Item"));
public static final AWItemBase npcCommandBaton = new ItemNpcCommandBaton(Config.getItemID("itemMulti.commandBaton", 24012, "Npc Command Batons"));
public static final AWItemBase civicPlacer = new ItemCivicPlacer(Config.getItemID("itemMulti.civiPlacer", 24024, "Constructs Civic Buildings/Sites"));
public static final AWItemBase civicBuilder = new ItemCivicBuilder(Config.getItemID("itemMulti.civicBuilder", 24014, "Constructs Structures using Civics"));
public static final AWItemBase courierRouteSlip = new ItemCourierSlip(Config.getItemID("itemMulti.courierSlip", 24015, "Holds Routing Info for a Courier"));
public static final AWItemBase gateSpawner = new ItemGateSpawner(Config.getItemID("itemMulti.gateSpawner", 24016, "Base gate spawning item."));
public static final ItemFood rations = new ItemRation(Config.getItemID("itemSingle.foodRation", 24017, "Food rations for soldiers and npcs."));
public static final AWItemBase researchNotes = (AWItemBase) new ItemResearchNote(Config.getItemID("itemMulti.researchNotes", 24018, "Research notes"));
public static final AWItemBase backpack = new ItemBackpack(Config.getItemID("itemMulti.backpack", 24019, "Backpack"));
public static final AWItemBase researchBook = new ItemResearchBook(Config.getItemID("itemSingle.researchBook", 24020, "Research book to save research progress"));

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
public static final ItemStack cement = new ItemStack(componentItem, 1, 23);
public static final ItemStack hammer1 = new ItemStack(componentItem, 1, 24);
public static final ItemStack hammer2 = new ItemStack(componentItem, 1, 25);
public static final ItemStack hammer3 = new ItemStack(componentItem, 1, 26);
public static final ItemStack quill1 = new ItemStack(componentItem, 1, 27);
public static final ItemStack quill2 = new ItemStack(componentItem, 1, 28);
public static final ItemStack quill3 = new ItemStack(componentItem, 1, 29);


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
  this.loadDebugItems();
  }

private void loadItems()
  {  
  this.registerItemSingle(structureCreativeBuilderTicked, "item.single.structureBuilderTicked", "item.single.structureBuilderTicked.description", "item.single.structureBuilderTicked.tooltip").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
  this.registerItemSingle(structureScanner, "item.single.structureScanner", "item.single.structureScanner.description", "item.single.structureScanner.tooltip").setIconTexture("ancientwarfare:builder/structureScanner1", 0);
  this.registerItemSingle(structureCreativeBuilder, "item.single.structureBuilderInstant", "item.single.structureBuilderInstant.description", "item.single.structureBuilderInstant.tooltip").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
  this.registerItemSingle(structureBuilderDirect, "item.single.structureBuilderDirect", "item.single.structureBuilderDirect.description", "item.single.structureBuilderDirect.tooltip").setIconTexture("ancientwarfare:builder/structureScanner1", 0);
  this.registerItemSingle(structureEditor, "item.single.structureEditor", "item.single.structureEditor.description", "item.single.structureEditor.tooltip").setIconTexture("ancientwarfare:builder/testIcon1", 0);
  this.registerItemSubtyped(ammoItem, "ammo");
  this.registerItemSubtyped(vehicleSpawner, "vehicleSpawner");
  this.registerItemSubtyped(vehicleUpgrade, "vehicleUpgrade");
  this.registerItemSubtyped(armorItem, "vehicleArmor");
  this.registerItemSubtyped(npcSpawner, "npcSpawner");
  this.registerItemSubtyped(npcCommandBaton, "commandBaton");
  this.addSubtypeInfoToItem(npcCommandBaton, 0, "item.baton.0", "item.baton.0.description","item.baton.0.tooltip").addDisplayStack(new ItemStack(npcCommandBaton,1,0)).setIconTexture("ancientwarfare:npc/baton1", 0);
  this.addSubtypeInfoToItem(npcCommandBaton, 1, "item.baton.1", "item.baton.0.description","item.baton.1.tooltip").addDisplayStack(new ItemStack(npcCommandBaton,1,1)).setIconTexture("ancientwarfare:npc/baton3", 1);
  this.registerItemSubtyped(civicPlacer, "civicItem");
  this.registerItemSingle(civicBuilder, "item.single.civicBuilder", "item.single.civicBuilder.description", "item.single.civicBuilder.tooltip");
  this.registerItemSubtyped(courierRouteSlip, "routingSlip");
  this.addSubtypeInfoToItem(courierRouteSlip, 0, "item.routingSlip.0","item.routingSlip.0.description","item.routingSlip.0.tooltip").addDisplayStack(new ItemStack(courierRouteSlip,1,0)).setIconTexture("ancientwarfare:npc/route1", 0);
  this.addSubtypeInfoToItem(courierRouteSlip, 1, "item.routingSlip.1","item.routingSlip.1.description","item.routingSlip.1.tooltip").addDisplayStack(new ItemStack(courierRouteSlip,1,1)).setIconTexture("ancientwarfare:npc/route2", 1);
  this.addSubtypeInfoToItem(courierRouteSlip, 2, "item.routingSlip.2","item.routingSlip.2.description","item.routingSlip.2.tooltip").addDisplayStack(new ItemStack(courierRouteSlip,1,2)).setIconTexture("ancientwarfare:npc/route3", 2);
  this.registerItemSubtyped(gateSpawner, "gateSpawner");
  this.registerItemSingle(rations, "item.single.rations", "item.single.rations.description", "item.single.rations.tooltip");
   
  this.registerItemSubtyped(backpack, "backpack");
  this.addSubtypeInfoToItem(backpack, 0, "item.backpack.0").addTooltip("item.backpack.0.tooltip", 0).addDisplayStack(new ItemStack(backpack,1,0)).setIconTexture("ancientwarfare:misc/backpack", 0);
  this.addSubtypeInfoToItem(backpack, 16, "item.backpack.16").addTooltip("item.backpack.16.tooltip", 16).addDisplayStack(new ItemStack(backpack,1,16)).setIconTexture("ancientwarfare:misc/backpack", 16);
  this.addSubtypeInfoToItem(backpack, 32, "item.backpack.32").addTooltip("item.backpack.32.tooltip", 32).addDisplayStack(new ItemStack(backpack,1,32)).setIconTexture("ancientwarfare:misc/backpack", 32);
  this.addSubtypeInfoToItem(backpack, 48, "item.backpack.48").addTooltip("item.backpack.48.tooltip", 48).addDisplayStack(new ItemStack(backpack,1,48)).setIconTexture("ancientwarfare:misc/backpack", 48);
    
  this.registerItemSubtyped(researchNotes, "researchNotes");
  this.registerItemSingle(researchBook, "item.single.researchBook", "item.single.researchBook.description", "item.single.researchBook").setIconTexture("ancientwarfare:misc/researchBook", 0);
      
  /**
   * register main component item (misc random items) 
   */
  this.registerItemSubtyped(componentItem, "componentItem");
  this.addSubtypeInfoToItem(componentItem, 0, "item.component.0").addTooltip("item.component.0.tooltip", 0).addDisplayStack(wood1).setIconTexture("ancientwarfare:misc/materialWood1", 0);
  this.addSubtypeInfoToItem(componentItem, 1, "item.component.1").addTooltip("item.component.1.tooltip", 1).addDisplayStack(wood2).setIconTexture("ancientwarfare:misc/materialWood2", 1);
  this.addSubtypeInfoToItem(componentItem, 2, "item.component.2").addTooltip("item.component.2.tooltip", 2).addDisplayStack(wood3).setIconTexture("ancientwarfare:misc/materialWood3", 2);
  this.addSubtypeInfoToItem(componentItem, 3, "item.component.3").addTooltip("item.component.3.tooltip", 3).addDisplayStack(wood4).setIconTexture("ancientwarfare:misc/materialWood4", 3);
  this.addSubtypeInfoToItem(componentItem, 4, "item.component.4").addTooltip("item.component.4.tooltip", 4).addDisplayStack(iron1).setIconTexture("ancientwarfare:misc/materialIron1", 4);
  this.addSubtypeInfoToItem(componentItem, 5, "item.component.5").addTooltip("item.component.5.tooltip", 5).addDisplayStack(iron2).setIconTexture("ancientwarfare:misc/materialIron2", 5);
  this.addSubtypeInfoToItem(componentItem, 6, "item.component.6").addTooltip("item.component.6.tooltip", 6).addDisplayStack(iron3).setIconTexture("ancientwarfare:misc/materialIron3", 6);
  this.addSubtypeInfoToItem(componentItem, 7, "item.component.7").addTooltip("item.component.7.tooltip", 7).addDisplayStack(iron4).setIconTexture("ancientwarfare:misc/materialIron4", 7);
  this.addSubtypeInfoToItem(componentItem, 8, "item.component.8").addTooltip("item.component.8.tooltip", 8).addDisplayStack(iron5).setIconTexture("ancientwarfare:misc/materialIron5", 8);
  this.addSubtypeInfoToItem(componentItem, 9, "item.component.9").addTooltip("item.component.9.tooltip", 9).addDisplayStack(flameCharge).setIconTexture("ancientwarfare:ammo/ammoFlameCharge", 9);
  this.addSubtypeInfoToItem(componentItem, 10, "item.component.10").addTooltip("item.component.10.tooltip", 10).addDisplayStack(explosiveCharge).setIconTexture("ancientwarfare:ammo/ammoExplosiveCharge", 10);
  this.addSubtypeInfoToItem(componentItem, 11, "item.component.11").addTooltip("item.component.11.tooltip", 11).addDisplayStack(rocketCharge).setIconTexture("ancientwarfare:ammo/ammoRocketCharge", 11);
  this.addSubtypeInfoToItem(componentItem, 12, "item.component.12").addTooltip("item.component.12.tooltip", 12).addDisplayStack(clusterCharge).setIconTexture("ancientwarfare:ammo/ammoClusterCharge", 12);
  this.addSubtypeInfoToItem(componentItem, 13, "item.component.13").addTooltip("item.component.13.tooltip", 13).addDisplayStack(napalmCharge).setIconTexture("ancientwarfare:ammo/ammoNapalmCharge", 13);
  this.addSubtypeInfoToItem(componentItem, 14, "item.component.14").addTooltip("item.component.14.tooltip", 14).addDisplayStack(clayCasing).setIconTexture("ancientwarfare:ammo/ammoClayCasing", 14);
  this.addSubtypeInfoToItem(componentItem, 15, "item.component.15").addTooltip("item.component.15.tooltip", 15).addDisplayStack(ironCasing).setIconTexture("ancientwarfare:ammo/ammoIronCasing", 15);
  this.addSubtypeInfoToItem(componentItem, 16, "item.component.16").addTooltip("item.component.16.tooltip", 16).addDisplayStack(mobilityUnit).setIconTexture("ancientwarfare:misc/vehicleMobilityUnit", 16);
  this.addSubtypeInfoToItem(componentItem, 17, "item.component.17").addTooltip("item.component.17.tooltip", 17).addDisplayStack(turretComponents).setIconTexture("ancientwarfare:misc/vehicleTurretUnit", 17);
  this.addSubtypeInfoToItem(componentItem, 18, "item.component.18").addTooltip("item.component.18.tooltip", 18).addDisplayStack(torsionUnit).setIconTexture("ancientwarfare:misc/vehicleTorsionUnit", 18);
  this.addSubtypeInfoToItem(componentItem, 19, "item.component.19").addTooltip("item.component.19.tooltip", 19).addDisplayStack(counterWeightUnit).setIconTexture("ancientwarfare:misc/vehicleCounterWeightUnit", 19);
  this.addSubtypeInfoToItem(componentItem, 20, "item.component.20").addTooltip("item.component.20.tooltip", 20).addDisplayStack(powderCase).setIconTexture("ancientwarfare:misc/vehiclePowderUnit", 20);
  this.addSubtypeInfoToItem(componentItem, 21, "item.component.21").addTooltip("item.component.21.tooltip", 21).addDisplayStack(equipmentBay).setIconTexture("ancientwarfare:misc/vehicleEquipmentBay", 21);
  this.addSubtypeInfoToItem(componentItem, 22, "item.component.22").addTooltip("item.component.22.tooltip", 22).addDisplayStack(ironRings).setIconTexture("ancientwarfare:misc/ironRings", 22);
  this.addSubtypeInfoToItem(componentItem, 23, "item.component.23").addTooltip("item.component.23.tooltip", 23).addDisplayStack(cement).setIconTexture("ancientwarfare:misc/cement", 23);
  this.addSubtypeInfoToItem(componentItem, 24, "item.component.24").addTooltip("item.component.24.tooltip", 24).addDisplayStack(hammer1).setIconTexture("ancientwarfare:misc/hammer1", 24);
  this.addSubtypeInfoToItem(componentItem, 25, "item.component.25").addTooltip("item.component.25.tooltip", 25).addDisplayStack(hammer2).setIconTexture("ancientwarfare:misc/hammer2", 25);
  this.addSubtypeInfoToItem(componentItem, 26, "item.component.26").addTooltip("item.component.26.tooltip", 26).addDisplayStack(hammer3).setIconTexture("ancientwarfare:misc/hammer3", 26);
  this.addSubtypeInfoToItem(componentItem, 27, "item.component.27").addTooltip("item.component.27.tooltip", 27).addDisplayStack(quill1).setIconTexture("ancientwarfare:misc/quill1", 27);
  this.addSubtypeInfoToItem(componentItem, 28, "item.component.28").addTooltip("item.component.28.tooltip", 28).addDisplayStack(quill2).setIconTexture("ancientwarfare:misc/quill2", 28);  
  this.addSubtypeInfoToItem(componentItem, 29, "item.component.29").addTooltip("item.component.29.tooltip", 29).addDisplayStack(quill3).setIconTexture("ancientwarfare:misc/quill3", 29);
  }

private void loadDebugItems()
  {
  if(!Config.DEBUG)
    {
    return;
    }  
  blockScanner = new ItemBlockScanner(Config.getItemID("debug.blockScanner", 9000));
  this.registerItemSingle(blockScanner, "item.single.debug.blockScanner", "item.single.debug.blockScanner.description","item.single.debug.blockScanner.tooltip").setIconTexture("ancientwarfare:testIcon1", 0);
  }

public Description registerItemSubtyped(AWItemBase item, String baseName)
  {
  Description d = DescriptionRegistry2.instance().registerItem(item, false);
//  GameRegistry.registerItem(item, baseName);
  return d;
  }

public Description registerItemSingle(Item item, String name, String desc, String tip)
  {
  Description d = DescriptionRegistry2.instance().registerItem(item, true);
  d.setName(name, 0);
  d.setDescription(desc, 0);
  d.addTooltip(tip, 0);  
//  GameRegistry.registerItem(item, name);
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
