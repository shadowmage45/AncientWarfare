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

import net.minecraft.item.ItemStack;
import shadowmage.ancient_framework.AWFramework;
import shadowmage.ancient_framework.common.item.AWItemBase;
import shadowmage.ancient_framework.common.registry.ObjectRegistry;
import shadowmage.ancient_framework.common.registry.entry.Description;

public class ItemLoaderCore
{

private static ObjectRegistry registry = AWFramework.instance.objectRegistry;

public static final AWItemBase vehicleUpgrade = registry.createItem("item.vehicleUpgrade", ItemVehicleUpgrade.class);
public static final AWItemBase ammoItem = registry.createItem("item.vehicleAmmo", ItemAmmo.class);
public static final AWItemBase vehicleSpawner = registry.createItem("item.vehicleSpawner", ItemVehicleSpawner.class);
public static final AWItemBase componentItem = registry.createItem("item.component", ItemComponent.class);
public static final AWItemBase armorItem = registry.createItem("item.vehicleArmor", ItemVehicleArmor.class);
public static final AWItemBase npcSpawner = registry.createItem("item.npcSpawner", ItemNpcSpawner.class);
public static final AWItemBase npcCommandBaton = registry.createItem("item.npcCommandBaton", ItemNpcCommandBaton.class);
public static final AWItemBase civicPlacer = registry.createItem("item.civic", ItemCivicPlacer.class);
public static final AWItemBase routingSlip = registry.createItem("item.routingSlip", ItemCourierSlip.class);
public static final AWItemBase gateSpawner = registry.createItem("item.gateSpawner", ItemGateSpawner.class);
public static final AWItemBase researchNotes = registry.createItem("item.researchNotes.class", ItemResearchNote.class);
public static final AWItemBase researchBook = registry.createItem("item.researchBook", ItemResearchBook.class);
public static final AWItemBase backpack = registry.createItem("item.backpack", ItemBackpack.class);

public static final ItemRation rations = registry.createItemBasic("item.ration", ItemRation.class);


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


private static ItemLoaderCore INSTANCE;
private ItemLoaderCore(){}
public static ItemLoaderCore instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new ItemLoaderCore();
    }
  return INSTANCE;
  }

/**
 * initial load, called during pre-init from mod core file
 */
public void load()
  {
  this.loadItems();
  }

private void loadItems()
  {  
//  this.registerItemSingle(structureCreativeBuilderTicked, "item.single.structureBuilderTicked", "item.single.structureBuilderTicked.description", "item.single.structureBuilderTicked.tooltip").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
//  this.registerItemSingle(structureScanner, "item.single.structureScanner", "item.single.structureScanner.description", "item.single.structureScanner.tooltip").setIconTexture("ancientwarfare:builder/structureScanner1", 0);
//  this.registerItemSingle(structureCreativeBuilder, "item.single.structureBuilderInstant", "item.single.structureBuilderInstant.description", "item.single.structureBuilderInstant.tooltip").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
//  this.registerItemSingle(structureBuilderDirect, "item.single.structureBuilderDirect", "item.single.structureBuilderDirect.description", "item.single.structureBuilderDirect.tooltip").setIconTexture("ancientwarfare:builder/structureScanner1", 0);
//  this.registerItemSingle(structureEditor, "item.single.structureEditor", "item.single.structureEditor.description", "item.single.structureEditor.tooltip").setIconTexture("ancientwarfare:builder/testIcon1", 0);
//  this.registerItemSingle(civicBuilder, "item.single.civicBuilder", "item.single.civicBuilder.description", "item.single.civicBuilder.tooltip");
//  this.registerItemSingle(rations, "item.single.rations", "item.single.rations.description", "item.single.rations.tooltip");
	
  rations.description = AWFramework.instance.objectRegistry.registerObject("item.single.rations", rations, rations.itemID);
  rations.description.addDisplayStack(new ItemStack(rations, 1));
  
  this.registerItemSingle(researchBook, "item.single.researchBook", "item.single.researchBook.description", "item.single.researchBook").setIconTexture("ancientwarfare:misc/researchBook", 0);
  
  this.addSubtypeInfoToItem(npcCommandBaton, 0, "item.baton.0", "item.baton.0.description","item.baton.0.tooltip").setIconTexture("ancientwarfare:npc/baton1", 0);
  this.addSubtypeInfoToItem(npcCommandBaton, 1, "item.baton.1", "item.baton.0.description","item.baton.1.tooltip").setIconTexture("ancientwarfare:npc/baton3", 1);
  this.addSubtypeInfoToItem(routingSlip, 0, "item.routingSlip.0","item.routingSlip.0.description","item.routingSlip.0.tooltip").setIconTexture("ancientwarfare:npc/route1", 0);
  this.addSubtypeInfoToItem(routingSlip, 1, "item.routingSlip.1","item.routingSlip.1.description","item.routingSlip.1.tooltip").setIconTexture("ancientwarfare:npc/route2", 1);
  this.addSubtypeInfoToItem(routingSlip, 2, "item.routingSlip.2","item.routingSlip.2.description","item.routingSlip.2.tooltip").setIconTexture("ancientwarfare:npc/route3", 2);   
  this.addSubtypeInfoToItem(backpack, 0, "item.backpack.0").addTooltip("item.backpack.0.tooltip", 0).setIconTexture("ancientwarfare:misc/backpack", 0);
  this.addSubtypeInfoToItem(backpack, 16, "item.backpack.16").addTooltip("item.backpack.16.tooltip", 16).setIconTexture("ancientwarfare:misc/backpack", 16);
  this.addSubtypeInfoToItem(backpack, 32, "item.backpack.32").addTooltip("item.backpack.32.tooltip", 32).setIconTexture("ancientwarfare:misc/backpack", 32);
  this.addSubtypeInfoToItem(backpack, 48, "item.backpack.48").addTooltip("item.backpack.48.tooltip", 48).setIconTexture("ancientwarfare:misc/backpack", 48);
  this.addSubtypeInfoToItem(componentItem, 0, "item.component.0").addTooltip("item.component.0.tooltip", 0).setIconTexture("ancientwarfare:misc/materialWood1", 0);
  this.addSubtypeInfoToItem(componentItem, 1, "item.component.1").addTooltip("item.component.1.tooltip", 1).setIconTexture("ancientwarfare:misc/materialWood2", 1);
  this.addSubtypeInfoToItem(componentItem, 2, "item.component.2").addTooltip("item.component.2.tooltip", 2).setIconTexture("ancientwarfare:misc/materialWood3", 2);
  this.addSubtypeInfoToItem(componentItem, 3, "item.component.3").addTooltip("item.component.3.tooltip", 3).setIconTexture("ancientwarfare:misc/materialWood4", 3);
  this.addSubtypeInfoToItem(componentItem, 4, "item.component.4").addTooltip("item.component.4.tooltip", 4).setIconTexture("ancientwarfare:misc/materialIron1", 4);
  this.addSubtypeInfoToItem(componentItem, 5, "item.component.5").addTooltip("item.component.5.tooltip", 5).setIconTexture("ancientwarfare:misc/materialIron2", 5);
  this.addSubtypeInfoToItem(componentItem, 6, "item.component.6").addTooltip("item.component.6.tooltip", 6).setIconTexture("ancientwarfare:misc/materialIron3", 6);
  this.addSubtypeInfoToItem(componentItem, 7, "item.component.7").addTooltip("item.component.7.tooltip", 7).setIconTexture("ancientwarfare:misc/materialIron4", 7);
  this.addSubtypeInfoToItem(componentItem, 8, "item.component.8").addTooltip("item.component.8.tooltip", 8).setIconTexture("ancientwarfare:misc/materialIron5", 8);
  this.addSubtypeInfoToItem(componentItem, 9, "item.component.9").addTooltip("item.component.9.tooltip", 9).setIconTexture("ancientwarfare:ammo/ammoFlameCharge", 9);
  this.addSubtypeInfoToItem(componentItem, 10, "item.component.10").addTooltip("item.component.10.tooltip", 10).setIconTexture("ancientwarfare:ammo/ammoExplosiveCharge", 10);
  this.addSubtypeInfoToItem(componentItem, 11, "item.component.11").addTooltip("item.component.11.tooltip", 11).setIconTexture("ancientwarfare:ammo/ammoRocketCharge", 11);
  this.addSubtypeInfoToItem(componentItem, 12, "item.component.12").addTooltip("item.component.12.tooltip", 12).setIconTexture("ancientwarfare:ammo/ammoClusterCharge", 12);
  this.addSubtypeInfoToItem(componentItem, 13, "item.component.13").addTooltip("item.component.13.tooltip", 13).setIconTexture("ancientwarfare:ammo/ammoNapalmCharge", 13);
  this.addSubtypeInfoToItem(componentItem, 14, "item.component.14").addTooltip("item.component.14.tooltip", 14).setIconTexture("ancientwarfare:ammo/ammoClayCasing", 14);
  this.addSubtypeInfoToItem(componentItem, 15, "item.component.15").addTooltip("item.component.15.tooltip", 15).setIconTexture("ancientwarfare:ammo/ammoIronCasing", 15);
  this.addSubtypeInfoToItem(componentItem, 16, "item.component.16").addTooltip("item.component.16.tooltip", 16).setIconTexture("ancientwarfare:misc/vehicleMobilityUnit", 16);
  this.addSubtypeInfoToItem(componentItem, 17, "item.component.17").addTooltip("item.component.17.tooltip", 17).setIconTexture("ancientwarfare:misc/vehicleTurretUnit", 17);
  this.addSubtypeInfoToItem(componentItem, 18, "item.component.18").addTooltip("item.component.18.tooltip", 18).setIconTexture("ancientwarfare:misc/vehicleTorsionUnit", 18);
  this.addSubtypeInfoToItem(componentItem, 19, "item.component.19").addTooltip("item.component.19.tooltip", 19).setIconTexture("ancientwarfare:misc/vehicleCounterWeightUnit", 19);
  this.addSubtypeInfoToItem(componentItem, 20, "item.component.20").addTooltip("item.component.20.tooltip", 20).setIconTexture("ancientwarfare:misc/vehiclePowderUnit", 20);
  this.addSubtypeInfoToItem(componentItem, 21, "item.component.21").addTooltip("item.component.21.tooltip", 21).setIconTexture("ancientwarfare:misc/vehicleEquipmentBay", 21);
  this.addSubtypeInfoToItem(componentItem, 22, "item.component.22").addTooltip("item.component.22.tooltip", 22).setIconTexture("ancientwarfare:misc/ironRings", 22);
  this.addSubtypeInfoToItem(componentItem, 23, "item.component.23").addTooltip("item.component.23.tooltip", 23).setIconTexture("ancientwarfare:misc/cement", 23);
  this.addSubtypeInfoToItem(componentItem, 24, "item.component.24").addTooltip("item.component.24.tooltip", 24).setIconTexture("ancientwarfare:misc/hammer1", 24);
  this.addSubtypeInfoToItem(componentItem, 25, "item.component.25").addTooltip("item.component.25.tooltip", 25).setIconTexture("ancientwarfare:misc/hammer2", 25);
  this.addSubtypeInfoToItem(componentItem, 26, "item.component.26").addTooltip("item.component.26.tooltip", 26).setIconTexture("ancientwarfare:misc/hammer3", 26);
  this.addSubtypeInfoToItem(componentItem, 27, "item.component.27").addTooltip("item.component.27.tooltip", 27).setIconTexture("ancientwarfare:misc/quill1", 27);
  this.addSubtypeInfoToItem(componentItem, 28, "item.component.28").addTooltip("item.component.28.tooltip", 28).setIconTexture("ancientwarfare:misc/quill2", 28);  
  this.addSubtypeInfoToItem(componentItem, 29, "item.component.29").addTooltip("item.component.29.tooltip", 29).setIconTexture("ancientwarfare:misc/quill3", 29);
  }

public Description registerItemSubtyped(AWItemBase item, String baseName)
  {  
  Description d = AWFramework.instance.objectRegistry.registerItem(baseName, item);
  return d;
  }

public Description registerItemSingle(AWItemBase item, String name, String desc, String tip)
  {
  return AWFramework.instance.objectRegistry.registerItem(name, item).setName(name, 0).setDescription(desc, 0).addTooltip(tip, 0);
  }

public Description addSubtypeInfoToItem(AWItemBase item, int damage, String name, String desc, String tooltip)
  {
  Description d = AWFramework.instance.objectRegistry.getDescriptionFor(item.itemID);
  d.setName(name, damage);
  d.setDescription(desc, damage);
  d.addTooltip(desc, damage);
  d.addDisplayStack(new ItemStack(item, 1, damage));
  return d;
  }

public Description addSubtypeInfoToItem(AWItemBase item, int damage, String name)
  {
  Description d = AWFramework.instance.objectRegistry.getDescriptionFor(item.itemID);
  d.setName(name, damage);
  d.addDisplayStack(new ItemStack(item, 1, damage));
  return d;
  }

public Description addSubtypeInfoWithIconTexture(AWItemBase item, int damage, String name, String texture)
  {
  Description d = AWFramework.instance.objectRegistry.getDescriptionFor(item.itemID);
  d.setName(name, damage);
  d.setIconTexture(texture, damage);
  d.addDisplayStack(new ItemStack(item, 1, damage));
  return d;
  }
}
