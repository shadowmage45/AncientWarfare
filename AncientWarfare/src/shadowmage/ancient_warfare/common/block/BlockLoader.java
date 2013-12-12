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
package shadowmage.ancient_warfare.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_warfare.common.civics.BlockCivic;
import shadowmage.ancient_warfare.common.civics.BlockWarehouseStorage;
import shadowmage.ancient_warfare.common.civics.TECivicTownHall;
import shadowmage.ancient_warfare.common.civics.TECivicWarehouse;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnChicken;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnCow;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnMooshroom;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnPig;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnSheep;
import shadowmage.ancient_warfare.common.civics.worksite.te.builder.TECivicBuilder;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmCactus;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmCarrot;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmCocoa;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmMelon;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmMushroomBrown;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmMushroomRed;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmNetherStalk;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmPotato;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmPumpkin;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmReed;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmWheat;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEWorkSiteFarm;
import shadowmage.ancient_warfare.common.civics.worksite.te.fish.TEFishery;
import shadowmage.ancient_warfare.common.civics.worksite.te.fish.TESquidFarm;
import shadowmage.ancient_warfare.common.civics.worksite.te.mine.TEMine;
import shadowmage.ancient_warfare.common.civics.worksite.te.mine.TEMineQuarry;
import shadowmage.ancient_warfare.common.civics.worksite.te.tree.TETreeFarmBirch;
import shadowmage.ancient_warfare.common.civics.worksite.te.tree.TETreeFarmJungle;
import shadowmage.ancient_warfare.common.civics.worksite.te.tree.TETreeFarmOak;
import shadowmage.ancient_warfare.common.civics.worksite.te.tree.TETreeFarmSpruce;
import shadowmage.ancient_warfare.common.crafting.BlockAWCrafting;
import shadowmage.ancient_warfare.common.gates.BlockGateProxy;
import shadowmage.ancient_warfare.common.gates.TEGateProxy;
import shadowmage.ancient_warfare.common.item.AWItemBlockBase;
import shadowmage.ancient_warfare.common.item.ItemEngine;
import shadowmage.ancient_warfare.common.item.ItemMachine;
import shadowmage.ancient_warfare.common.item.ItemReinforcedBlock;
import shadowmage.ancient_warfare.common.machine.BlockEngine;
import shadowmage.ancient_warfare.common.machine.BlockMiscMachine;
import shadowmage.ancient_warfare.common.machine.EngineData;
import shadowmage.ancient_warfare.common.machine.MachineData;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BlockLoader
{

public static final Block builder = new BlockBuilder(Config.getBlockID("blockSingle.builder", 3750, "Placeholder block for ticked-structure builders."));
public static final Block civicBlock1 = new BlockCivic(Config.getBlockID("blockMulti.civic1", 3751, "Civic Block 1"), "CivicBlock1", 0);
public static final Block civicBlock2 = new BlockCivic(Config.getBlockID("blockMulti.civic2", 3752, "Civic Block 2"), "CivicBlock2", 1);
public static final Block civicBlock3 = new BlockCivic(Config.getBlockID("blockMulti.civic3", 3753, "Civic Block 3"), "CivicBlock3", 2);
public static final Block civicBlock4 = new BlockCivic(Config.getBlockID("blockMulti.civic4", 3754, "Civic Block 4"), "CivicBlock4", 3);
public static final Block warehouseStorage = new BlockWarehouseStorage(Config.getBlockID("blockSingle.warehouseStorage", 3755, "Warehouse Storage Block"));
public static final Block gateProxy = new BlockGateProxy(Config.getBlockID("blockSingle.gateProxy", 3756, "Gate collision/sight check proxy block"));
public static final Block crafting = new BlockAWCrafting(Config.getBlockID("blockMulti.crafting", 3757, "Base block for crafting/research stations"), Config.getConfig().get("renderid", "craftingBlocks", 3707, "renderID for crafting blocks").getInt(3707));
public static final Block reinforced = new BlockReinforced(Config.getBlockID("blockMulti.reinforced", 3758, "Base block for reinforced blocks"), Material.rock, "Reinforced Blocks");
public static final Block machineBlock = new BlockMiscMachine(Config.getBlockID("blockMulti.machine", 3759, "Base block for misc machines"), Material.rock, "Machine");
public static final Block engineBlock = new BlockEngine(Config.getBlockID("blockMulti.engine", 3760, "Base block for misc engines"), Material.rock, "block.multi.engine.0");

public static final ItemStack trashcan = new ItemStack(machineBlock,1,0);
public static final ItemStack mailbox = new ItemStack(machineBlock,1,1);
public static final ItemStack mailboxIndustrial = new ItemStack(machineBlock,1,2);
public static final ItemStack chunkloader = new ItemStack(machineBlock, 1, 3);
public static final ItemStack chunkloaderDeluxe = new ItemStack(machineBlock, 1, 4);
public static final ItemStack mechanicalWorker = new ItemStack(machineBlock, 1, 5);
public static final ItemStack handCrankedEngine = new ItemStack(engineBlock, 1, 0);
public static final ItemStack foodProcessor = new ItemStack(machineBlock, 1, 6);
public static final ItemStack gateLock = new ItemStack(machineBlock, 1, 7);

private static BlockLoader INSTANCE;
private BlockLoader(){}
public static BlockLoader instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new BlockLoader();
    }
  return INSTANCE;
  }

public void load()
  {
  registerBlock(builder, "block.single.builder"); 
  registerBlockWithItem(warehouseStorage, "Warehouse Storage", AWItemBlockBase.class);
  ((BlockWarehouseStorage) warehouseStorage).registerBlockInfo();
  registerBlock(gateProxy, "block.single.gateproxy");
  registerBlock(civicBlock1, "CivicBlock1");
  registerBlock(civicBlock2, "CivicBlock2");
  registerBlock(civicBlock3, "CivicBlock3");
  registerBlock(civicBlock4, "CivicBlock4");  
  registerBlockWithItem(machineBlock, "block.multi.machine.0", ItemMachine.class);
  registerBlockWithItem(engineBlock, "block.multi.engine.0", ItemEngine.class);
  
  registerBlockWithItem(reinforced, "block.multi.reinforced.0", ItemReinforcedBlock.class);
  ((BlockReinforced)reinforced).registerBlockInfo();  
  GameRegistry.registerTileEntity(TEAWBlockReinforced.class, "Reinforced Block");
      
  GameRegistry.registerTileEntity(TEBuilder.class, "AWBuilder");
  GameRegistry.registerTileEntity(TEGateProxy.class, "AWGateProxyTE");
  GameRegistry.registerTileEntity(TEWorkSiteFarm.class, "AWFarmSiteTE");   
  GameRegistry.registerTileEntity(TEFarmWheat.class, "Wheat Farm");
  GameRegistry.registerTileEntity(TEFarmCarrot.class, "Carrot Farm");
  GameRegistry.registerTileEntity(TEFarmPotato.class, "Potato Farm");
  GameRegistry.registerTileEntity(TEFarmReed.class, "Reed Farm");
  GameRegistry.registerTileEntity(TEFarmCactus.class, "Cactus Farm");
  GameRegistry.registerTileEntity(TEFarmMelon.class, "Melon Farm");
  GameRegistry.registerTileEntity(TEFarmPumpkin.class, "Pumpkin Farm");
  GameRegistry.registerTileEntity(TEFarmNetherStalk.class, "Netherstalk Farm");
  GameRegistry.registerTileEntity(TEFarmMushroomRed.class, "Red Mushroom Farm");
  GameRegistry.registerTileEntity(TEFarmMushroomBrown.class, "Brown Mushroom Farm");
  GameRegistry.registerTileEntity(TEMine.class, "Basic Mine");
  GameRegistry.registerTileEntity(TEMineQuarry.class, "Quarry Mine");
  
  GameRegistry.registerTileEntity(TETreeFarmOak.class, "Tree Farm Oak");
  GameRegistry.registerTileEntity(TETreeFarmSpruce.class, "Tree Farm Spruce");
  GameRegistry.registerTileEntity(TETreeFarmBirch.class, "Tree Farm Birch");
  GameRegistry.registerTileEntity(TETreeFarmJungle.class, "Tree Farm Jungle");
  GameRegistry.registerTileEntity(TECivicBuilder.class, "Civic Builder");
  GameRegistry.registerTileEntity(TECivicTownHall.class, "Town Hall");
  GameRegistry.registerTileEntity(TEBarnPig.class, "Pig Farm");
  GameRegistry.registerTileEntity(TEBarnCow.class, "Cow Farm");
  GameRegistry.registerTileEntity(TEBarnChicken.class, "Chicken Farm");
  GameRegistry.registerTileEntity(TEBarnSheep.class, "Sheep Farm");
  GameRegistry.registerTileEntity(TEBarnMooshroom.class, "Mooshroom Farm");
  GameRegistry.registerTileEntity(TEFishery.class, "Fish Farm");
  GameRegistry.registerTileEntity(TESquidFarm.class, "Squid Farm");
  GameRegistry.registerTileEntity(TEFarmCocoa.class, "Cocoa Bean Farm");
  GameRegistry.registerTileEntity(TECivicWarehouse.class, "Warehouse");
  
  ((BlockAWCrafting)crafting).registerBlockInfo();
  
  MachineData.registerBlockData();
  EngineData.registerBlockData();
  }

public void registerBlock(Block block, String name)
  {
  GameRegistry.registerBlock(block, name);
  LanguageRegistry.addName(block, name);
  DescriptionRegistry2.instance().registerBlock(block, false);
  }

public Description registerBlockWithItem(Block block, String name, Class<? extends ItemBlock>clz)
  {
  GameRegistry.registerBlock(block, clz, name);
  LanguageRegistry.addName(block, name);
  return DescriptionRegistry2.instance().registerBlock(block, false);
  }

}
