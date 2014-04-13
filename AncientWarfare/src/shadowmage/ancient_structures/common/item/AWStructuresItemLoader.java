/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_structures.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import shadowmage.ancient_structures.AWStructures;
import shadowmage.ancient_structures.common.block.BlockAdvancedSpawner;
import shadowmage.ancient_structures.common.tile.TileAdvancedSpawner;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.AWItemBase;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import cpw.mods.fml.common.registry.GameRegistry;

public class AWStructuresItemLoader
{

private AWStructuresItemLoader(){}
private static AWStructuresItemLoader instance = new AWStructuresItemLoader();
public static AWStructuresItemLoader instance(){return instance;}
private static Configuration config = AWStructures.instance.config.getConfig();
public static CreativeTabs structureTab = new CreativeTabs("Ancient Structures")
{

@Override
public Item getTabIconItem()
  {  
  return structureScanner;
  }

@Override
public String getTranslatedTabLabel()
  {
  return super.getTabLabel();
  }  
};//need to declare this instance prior to any items that use it as their creative tab

public static ItemStructureScanner structureScanner = new ItemStructureScanner(Config.getItemID("item.single.structureScanner", 24022));
public static ItemBuilderCreative structureBuilderCreative = new ItemBuilderCreative(Config.getItemID("item.single.structureBuilderInstant", 24023));
public static ItemSpawnerPlacer spawnerPlacer = new ItemSpawnerPlacer(Config.getItemID("item.single.spawnerPlacer", 24024));
public static ItemStructureGenerator structureGenerator = new ItemStructureGenerator(Config.getItemID("item.single.structureGenerator", 24025));
public static ItemCivicBuilder civicBuilder = new ItemCivicBuilder(Config.getItemID("item.multi.civicBuilder", 24026));

public static BlockAdvancedSpawner spawnerBlock = new BlockAdvancedSpawner(Config.getBlockID("block.advanced_spawner", 1355), "advanced_spawner");

public void registerItems()
  {
  this.registerItemSingle(structureScanner, "item.single.structureScanner", "item.single.structureScanner.description", "item.single.structureScanner.tooltip").setIconTexture("ancientwarfare:builder/structureScanner1", 0);
  this.registerItemSingle(structureBuilderCreative, "item.single.structureBuilderInstant", "item.single.structureBuilderInstant.description", "item.single.structureBuilderInstant.tooltip").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
  this.registerItemSingle(structureGenerator, "item.single.structureGenerator", "item.single.structureGenerator.description", "item.single.structureGenerator.tooltip").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
  this.registerItemSingle(spawnerPlacer, "item.single.spawnerPlacer", "item.single.spawnerPlacer.description", "item.single.spawnerPlacer.tooltip").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
  this.registerItemSingle(civicBuilder, "item.single.civicBuilder", "item.single.civicBuilder.description", "item.single.civicBuilder.tooltip");
  
  GameRegistry.registerBlock(spawnerBlock, ItemBlockAdvancedSpawner.class, "advanced_spawner");
  GameRegistry.registerTileEntity(TileAdvancedSpawner.class, "advanced_spawner_tile");
  }

public Description registerItemSubtyped(AWItemBase item, String baseName)
  {
  Description d = DescriptionRegistry2.instance().registerItem(item, false);
  GameRegistry.registerItem(item, baseName);
  return d;
  }

public Description registerItemSingle(Item item, String name, String desc, String tip)
  {
  Description d = DescriptionRegistry2.instance().registerItem(item, true);
  d.setName(name, 0);
  d.setDescription(desc, 0);
  d.addTooltip(tip, 0);  
  GameRegistry.registerItem(item, name);
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
