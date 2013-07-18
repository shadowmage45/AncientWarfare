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
package shadowmage.ancient_warfare.common.manager;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import shadowmage.ancient_warfare.common.structures.data.BlockInfo;

public class BlockDataManager
{

private static BlockDataManager INSTANCE;
private BlockDataManager(){}
public static BlockDataManager instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new BlockDataManager();
    }
  return INSTANCE;
  }

public static void loadBlockList()
  {
  addBlock(0, "air");
  /************************************ BASIC SUBTYPED BLOCKS ************************************/
  addBlock(Block.stoneBrick).setIsBasicSubtype();
  addBlock(Block.cloth).setIsBasicSubtype();
  addBlock(Block.leaves).setIsBasicSubtype();
  addBlock(Block.cobblestoneWall).setIsBasicSubtype();
  
  /************************************ ADVANCED SUBTYPED BLOCKS ************************************/
  addBlock(Block.grass);  
  addBlock(Block.stoneSingleSlab);
  addBlock(Block.stoneDoubleSlab);
  addBlock(Block.woodSingleSlab);
  addBlock(Block.woodDoubleSlab);
  
  /************************************ PRIORITY ONLY AND BASIC INVENTORIED BLOCK ENTRIES ************************************/
  addBlock(Block.waterMoving).setPriority(1).setBasicInventoryItem(Item.bucketWater.itemID, 0, 1);
  addBlock(Block.waterStill).setPriority(1).setBasicInventoryItem(Item.bucketWater.itemID, 0, 1);
  addBlock(Block.lavaStill).setPriority(1).setBasicInventoryItem(Item.bucketLava.itemID, 0, 1);
  addBlock(Block.lavaMoving).setPriority(1).setBasicInventoryItem(Item.bucketLava.itemID, 0, 1);
  addBlock(Block.sand).setPriority(1);
  addBlock(Block.gravel).setPriority(1);  
  addBlock(Block.tallGrass).setPriority(1);
  addBlock(Block.deadBush).setPriority(1);
  addBlock(Block.plantRed).setPriority(1);
  addBlock(Block.plantYellow).setPriority(1);
  addBlock(Block.mushroomBrown).setPriority(1);
  addBlock(Block.mushroomRed).setPriority(1);
  addBlock(Block.redstoneWire).setPriority(1).setBasicInventoryItem(Item.redstone.itemID, 0, 1);
  addBlock(Block.crops).setPriority(1).setBasicInventoryItem(Item.seeds.itemID, 0, 4);
  addBlock(Block.signPost).setPriority(1).setBasicInventoryItem(Item.sign.itemID, 0, 1);
  addBlock(Block.cactus).setPriority(1);
  addBlock(Block.reed).setPriority(1);
  addBlock(Block.fence);
  addBlock(Block.netherFence);
  addBlock(Block.skull).setPriority(1);
  addBlock(Block.flowerPot).setPriority(1);
  addBlock(Block.carrot).setPriority(1).setBasicInventoryItem(Item.carrot.itemID, 0, 4);
  addBlock(Block.potato).setPriority(1).setBasicInventoryItem(Item.potato.itemID, 0, 4);
  addBlock(Block.mushroomCapBrown).setPriority(1);
  addBlock(Block.mushroomCapRed).setPriority(1);
  
  /************************************ ROTATABLE/METADATA BLOCKS ************************************/
  addBlock(Block.dropper).setRotatable().setMeta(0, 2, 5, 3, 4).setMeta(1, 1, 1, 1, 1).setMeta(2, 0, 0, 0, 0);
  addBlock(Block.dispenser).setRotatable().setMeta(0, 2, 5, 3, 4).setMeta(1, 1, 1, 1, 1).setMeta(2, 0, 0, 0, 0);
  addBlock(Block.hopperBlock).setRotatable().setMeta(0, 3, 4, 2, 5).setMeta(1, 0, 0, 0, 0).setMeta(2, 11, 12, 10, 13).setMeta(3, 8, 8, 8, 8);
  addBlock(Block.chest).setRotatable().setMeta(0, 2, 5, 3, 4);  
  addBlock(Block.chestTrapped).setRotatable().setMeta(0, 2, 5, 3, 4);
  addBlock(Block.furnaceIdle).setRotatable().setMeta(0, 2, 5, 3, 4);
  addBlock(Block.furnaceBurning).setRotatable().setMeta(0, 2, 5, 3, 4).setBasicInventoryItem(Block.furnaceIdle.blockID, 0, 1);
  addBlock(Block.ladder).setPriority(1).setRotatable().setMeta(0, 2, 5, 3, 4);
  addBlock(Block.signWall).setPriority(1).setRotatable().setMeta(0, 2, 5, 3, 4).setBasicInventoryItem(Item.sign.itemID, 0, 1);
  addBlock(Block.enderChest).setRotatable().setMeta(0, 2, 5, 3, 4);
  
  addBlock(Block.bed).setRotatable().setMeta(0, 0, 1, 2, 3).setMeta(1, 8, 9, 10, 11).setBasicInventoryItem(Item.bed.itemID, 0, 1);
  
  addBlock(Block.rail).setPriority(1).setRotatable().setMeta(0, 0, 1, 0, 1).setMeta(1, 7, 8, 9, 6).setMeta(2, 5, 3, 4, 2);
  addBlock(Block.railPowered).setPriority(1).setRotatable().setMeta(0, 5, 3, 4, 2).setMeta(1, 0, 1, 0, 1).setMeta(2, 13, 11, 12, 10).setMeta(3, 8, 9, 8, 9);
  addBlock(Block.railDetector).setPriority(1).setRotatable().setMeta(0, 5, 3, 4, 2).setMeta(1, 0, 1, 0, 1).setMeta(2, 13, 11, 12, 10).setMeta(3, 8, 9, 8, 9);
  addBlock(Block.railActivator).setPriority(1).setRotatable().setMeta(0, 5, 3, 4, 2).setMeta(1, 0, 1, 0, 1).setMeta(2, 13, 11, 12, 10).setMeta(3, 8, 9, 8, 9);
  
  addBlock(Block.pistonStickyBase).setRotatable().setMeta(0, 2, 5, 3, 4).setMeta(1, 1, 1, 1, 1).setMeta(2, 0, 0, 0, 0).setMeta(3, 10, 13, 11, 12).setMeta(4, 9, 9, 9, 9).setMeta(5, 8, 8, 8, 8);; 
  addBlock(Block.pistonBase).setRotatable().setMeta(0, 2, 5, 3, 4).setMeta(1, 1, 1, 1, 1).setMeta(2, 0, 0, 0, 0).setMeta(3, 10, 13, 11, 12).setMeta(4, 9, 9, 9, 9).setMeta(5, 8, 8, 8, 8);
  addBlock(Block.pistonExtension).setRotatable().setMeta(0, 0, 0, 0, 0).setMeta(1, 1, 1, 1, 1).setMeta(2, 2, 5, 3, 4);
  addBlock(Block.pistonMoving).setRotatable();//TODO ??
  
  addBlock(Block.lever).setPriority(1).setRotatable().setMeta(0, 5, 6, 5, 6).setMeta(1, 13, 14, 13, 14).setMeta(2, 4, 1, 3, 2).setMeta(3, 12, 9, 11, 10).setMeta(4, 7, 0, 7, 0).setMeta(5, 8, 15, 8, 15);
  addBlock(Block.redstoneRepeaterIdle).setPriority(1).setRotatable().setMeta(0, 2, 3, 0, 1).setMeta(1, 6, 7, 4, 5).setMeta(2, 10, 11, 8, 9).setMeta(3, 14, 15, 12, 13).setBasicInventoryItem(Item.redstoneRepeater.itemID, 0, 1);
  addBlock(Block.redstoneRepeaterActive).setPriority(1).setRotatable().setMeta(0, 2, 3, 0, 1).setMeta(1, 6, 7, 4, 5).setMeta(2, 10, 11, 8, 9).setMeta(3, 14, 15, 12, 13).setBasicInventoryItem(Item.redstoneRepeater.itemID, 0, 1);
  addBlock(Block.woodenButton).setRotatable().setMeta(0, 4, 1, 3, 2).setMeta(1, 12, 9, 11, 10);
  addBlock(Block.stoneButton).setRotatable().setMeta(0, 4, 1, 3, 2).setMeta(1, 12, 9, 11, 10);
  addBlock(Block.tripWireSource).setPriority(1).setRotatable().setMeta(0, 2, 3, 0, 1).setMeta(1, 6, 7, 4, 5).setMeta(2, 14, 15, 12, 13);
  addBlock(Block.tripWire).setPriority(1).setRotatable().setMeta(0, 1, 1, 1, 1).setMeta(1, 0, 0, 0, 0);
  
  addBlock(Block.torchWood).setPriority(1).setRotatable().setMeta(0, 4, 1, 3, 2).setMeta(1, 5, 5, 5, 5);  
  addBlock(Block.torchRedstoneIdle).setPriority(1).setRotatable().setMeta(0, 4, 1, 3, 2).setMeta(1, 5, 5, 5, 5);
  addBlock(Block.torchRedstoneActive).setPriority(1).setRotatable().setMeta(0, 4, 1, 3, 2).setMeta(1, 5, 5, 5, 5);  
   
  addBlock(Block.doorWood).setPriority(1).setRotatable().setMeta(0, 1, 2, 3, 0).setMeta(1, 5, 6, 7, 4).setMeta(2, 8, 8, 8, 8).setBasicInventoryItem(Item.doorWood.itemID, 0, 1);
  addBlock(Block.doorIron).setPriority(1).setRotatable().setMeta(0, 1, 2, 3, 0).setMeta(1, 5, 6, 7, 4).setMeta(2, 8, 8, 8, 8).setBasicInventoryItem(Item.doorIron.itemID, 0, 1);
  addBlock(Block.fenceGate).setRotatable().setMeta(0, 0, 1, 2, 3).setMeta(1, 4, 5, 6, 7);//HUH? rotated one block from door data?
  
  addBlock(Block.stairsWoodOak).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.stairsCobblestone).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.stairsBrick).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.stairsStoneBrick).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4); 
  addBlock(Block.stairsSandStone).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.stairsWoodSpruce).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.stairsWoodBirch).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.stairsWoodJungle).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.stairsNetherQuartz).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  
  addBlock(Block.vine).setPriority(1).setRotatable().setMeta(0, 1, 2, 4, 8);
    
  addBlock(Block.anvil).setPriority(1).setRotatable().setMeta(0, 3, 0, 1, 2).setMeta(1, 7, 4, 5, 6).setMeta(2, 11, 8, 9, 10);
   
  addBlock(Block.cocoaPlant).setRotatable().setMeta(0, 0, 1, 2, 3).setMeta(1, 4, 5, 6, 7).setMeta(2, 8, 9, 10, 11).setMeta(3, 12, 13, 14, 15).setBasicInventoryItem(Item.dyePowder.itemID, 3, 1);
  
  addBlock(Block.blockNetherQuartz).setRotatable().setMeta(0, 0, 0, 0, 0).setMeta(1, 1, 1, 1, 1).setMeta(2, 2, 2, 2, 2).setMeta(3, 4, 3, 4, 3);
  
  addBlock(Block.redstoneComparatorIdle).setPriority(1).setRotatable().setMeta(0, 2, 3, 0, 1);
  addBlock(Block.redstoneComparatorActive).setPriority(1).setRotatable().setMeta(0, 2, 3, 0, 1).setBasicInventoryItem(Block.redstoneComparatorIdle.blockID, 0, 1);
  
  addBlock(Block.wood).setMeta(0, 0, 0, 0, 0).setRotatable().setMeta(1, 8, 4, 8, 4).setMeta(2, 1, 1, 1, 1).setMeta(3, 9, 5, 9, 5).setMeta(4, 2, 2, 2, 2).setMeta(5, 10, 6, 10, 6).setMeta(6, 3, 3, 3, 3).setMeta(7, 11, 7, 11, 7);
  /**
   * add single slabs, and double slabs for stone
   */
  int id = Block.stoneSingleSlab.blockID;
  int id2 = Block.stoneDoubleSlab.blockID;
  for(int i = 0; i< 8 ; i++)
    {
    BlockInfo.setInventoryBlock(id, i, id, i, 1);
    BlockInfo.setInventoryBlock(id, i+8, id, i, 1);
    }
  for(int i = 0; i < 8 ; i++)
    {
    BlockInfo.setInventoryBlock(id2, i, id, i, 2);
    }
  
  /**
   * do the same for wood slabs
   */
  id = Block.woodSingleSlab.blockID;
  id2= Block.woodDoubleSlab.blockID;
  for(int i = 0; i< 8 ; i++)
    {
    BlockInfo.setInventoryBlock(id, i, id, i, 1);
    BlockInfo.setInventoryBlock(id, i+8, id, i, 1);
    }
  for(int i = 0; i < 8 ; i++)
    {
    BlockInfo.setInventoryBlock(id2, i, id, i, 2);
    }
  
  id = Block.grass.blockID;
  BlockInfo.setInventoryBlock(id, 0, Block.dirt.blockID, 0, 1);  
  }

public static int getBlockPriority(int id, int meta)
  {
  if(BlockInfo.blockList[id]!=null)
    {
    return (int) BlockInfo.blockList[id].buildOrder;
    }
  return 0;
  }

public static void setMeta(Block block, int set, int a, int b, int c, int d)
  {
  setMeta(block.blockID, set, a, b, c, d);
  }

public static void setMeta(int id, int set, int a, int b, int c, int d)
  {
  if(BlockInfo.blockList[id]!=null)
    {
    BlockInfo.blockList[id].setMeta(set, a, b, c, d);
    }
  return;
  }

/**
 * convenience wrappers, passes params directly into BlockInfo.createEntryFor(.....)
 * @param block
 * @param priority
 * @return
 */
public static BlockInfo addBlock(Block block)
  {
  return BlockInfo.createEntryFor(block);
  }

public static BlockInfo addBlock(int id, String name)
  {
  return BlockInfo.createEntryFor(id, name);
  }

public static int getRotatedMeta(int id, int meta, int rotationAmt)
  {
  if(BlockInfo.blockList[id]==null)
    {
    return meta;
    }
  return BlockInfo.blockList[id].rotateRight(meta, rotationAmt);
  }

}
