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
package shadowmage.ancient_warfare.common.aw_structure.data;

import java.util.HashMap;

import net.minecraft.block.Block;

import shadowmage.ancient_warfare.common.aw_structure.data.swap_groups.BlockSwapGroupBrick;
import shadowmage.ancient_warfare.common.aw_structure.data.swap_groups.BlockSwapGroupCobble;
import shadowmage.ancient_warfare.common.aw_structure.data.swap_groups.BlockSwapGroupNetherBrick;
import shadowmage.ancient_warfare.common.aw_structure.data.swap_groups.BlockSwapGroupSandstone;
import shadowmage.ancient_warfare.common.aw_structure.data.swap_groups.BlockSwapGroupStoneBrick;

public class BlockDataManager
{

static BlockSwapGroup COBBLE = new BlockSwapGroupCobble();
static BlockSwapGroup STONE_BRICK = new BlockSwapGroupStoneBrick();
static BlockSwapGroup NETHER_BRICK = new BlockSwapGroupNetherBrick();
static BlockSwapGroup BRICK = new BlockSwapGroupBrick();
static BlockSwapGroup SANDSTONE = new BlockSwapGroupSandstone();
static BlockSwapGroup WOOD_OAK;
static BlockSwapGroup WOOD_JUNGLE;
static BlockSwapGroup WOOD_SPRUCE;
static BlockSwapGroup WOOD_BIRCH;
static BlockSwapGroup WOOD_RANDOM;

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
  addBlock(Block.sand);
  addBlock(Block.gravel);  
  addBlock(Block.dispenser).setRotatable().setMeta(0, 2, 5, 3, 4);  
  addBlock(Block.railPowered).setRotatable().setMeta(0, 5, 3, 4, 2).setMeta(1, 0, 1, 0, 1).setMeta(2, 13, 11, 12, 10).setMeta(3, 8, 9, 8, 9);
  addBlock(Block.railDetector).setRotatable().setMeta(0, 5, 3, 4, 2).setMeta(1, 0, 1, 0, 1).setMeta(2, 13, 11, 12, 10).setMeta(3, 8, 9, 8, 9);
  addBlock(Block.pistonStickyBase).setRotatable().setMeta(0, 2, 5, 3, 4).setMeta(1, 1, 1, 1, 1).setMeta(2, 0, 0, 0, 0).setMeta(3, 10, 13, 11, 12).setMeta(4, 9, 9, 9, 9).setMeta(5, 8, 8, 8, 8);;
  addBlock(Block.tallGrass);
  addBlock(Block.deadBush);
  addBlock(Block.pistonBase).setRotatable().setMeta(0, 2, 5, 3, 4).setMeta(1, 1, 1, 1, 1).setMeta(2, 0, 0, 0, 0).setMeta(3, 10, 13, 11, 12).setMeta(4, 9, 9, 9, 9).setMeta(5, 8, 8, 8, 8);
  addBlock(Block.pistonExtension).setRotatable().setMeta(0, 0, 0, 0, 0).setMeta(1, 1, 1, 1, 1).setMeta(2, 2, 5, 3, 4);
  addBlock(Block.pistonMoving).setRotatable();
  addBlock(Block.plantRed);
  addBlock(Block.plantYellow);
  addBlock(Block.mushroomBrown);
  addBlock(Block.mushroomRed);
  addBlock(Block.torchWood).setRotatable().setMeta(0, 4, 1, 3, 2).setMeta(1, 5, 5, 5, 5);
  addBlock(Block.stairCompactPlanks).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.chest).setRotatable().setMeta(0, 2, 5, 3, 4);
  addBlock(Block.redstoneWire);
  addBlock(Block.crops);
  addBlock(Block.stoneOvenIdle).setRotatable().setMeta(0, 2, 5, 3, 4);
  addBlock(Block.stoneOvenActive).setRotatable().setMeta(0, 2, 5, 3, 4);
  addBlock(Block.signPost);
  addBlock(Block.doorWood).setRotatable().setMeta(0, 1, 2, 3, 0).setMeta(1, 5, 6, 7, 4);
  addBlock(Block.ladder).setRotatable().setMeta(0, 2, 5, 3, 4);
  addBlock(Block.rail).setRotatable().setMeta(0, 0, 1, 0, 1).setMeta(1, 7, 8, 9, 6).setMeta(2, 5, 3, 4, 2);
  addBlock(Block.stairCompactCobblestone).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.signWall).setRotatable().setMeta(0, 2, 5, 3, 4);
  addBlock(Block.lever).setRotatable().setMeta(0, 5, 6, 5, 6).setMeta(1, 13, 14, 13, 14).setMeta(2, 4, 1, 3, 2).setMeta(3, 12, 9, 11, 10).setMeta(4, 7, 0, 7, 0).setMeta(5, 8, 15, 8, 15);
  addBlock(Block.doorSteel).setRotatable().setMeta(0, 1, 2, 3, 0).setMeta(1, 5, 6, 7, 4);
  addBlock(Block.torchRedstoneIdle).setRotatable().setMeta(0, 4, 1, 3, 2).setMeta(1, 5, 5, 5, 5);
  addBlock(Block.torchRedstoneActive).setRotatable().setMeta(0, 4, 1, 3, 2).setMeta(1, 5, 5, 5, 5);
  addBlock(Block.cactus);
  addBlock(Block.reed);
  addBlock(Block.fence);
  addBlock(Block.redstoneRepeaterIdle).setRotatable().setMeta(0, 2, 3, 0, 1).setMeta(1, 6, 7, 4, 5).setMeta(2, 10, 11, 8, 9).setMeta(3, 14, 15, 12, 13);
  addBlock(Block.redstoneRepeaterActive).setRotatable().setMeta(0, 2, 3, 0, 1).setMeta(1, 6, 7, 4, 5).setMeta(2, 10, 11, 8, 9).setMeta(3, 14, 15, 12, 13);
  addBlock(Block.vine).setRotatable().setMeta(0, 1, 2, 4, 8);
  addBlock(Block.fenceGate).setRotatable().setMeta(0, 0, 1, 2, 3).setMeta(1, 4, 5, 6, 7);
  addBlock(Block.stairsBrick).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.stairsStoneBrickSmooth).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.netherFence);
  addBlock(Block.stairsSandStone).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.enderChest).setRotatable().setMeta(0, 2, 5, 3, 4);
  addBlock(Block.tripWireSource).setRotatable().setMeta(0, 2, 3, 0, 1).setMeta(1, 6, 7, 4, 5).setMeta(2, 14, 15, 12, 13);
  addBlock(Block.tripWire).setRotatable().setMeta(0, 1, 1, 1, 1).setMeta(1, 0, 0, 0, 0);
  addBlock(Block.stairsWoodSpruce).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.stairsWoodBirch).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.stairsWoodJungle).setRotatable().setMeta(0, 2, 1, 3, 0).setMeta(1, 6, 5, 7, 4);
  addBlock(Block.cobblestoneWall);
  addBlock(Block.anvil).setRotatable().setMeta(0, 3, 0, 1, 2).setMeta(1, 7, 4, 5, 6).setMeta(2, 11, 8, 9, 10);
  addBlock(Block.woodenButton).setRotatable().setMeta(0, 4, 1, 3, 2).setMeta(1, 12, 9, 11, 10);
  addBlock(Block.stoneButton).setRotatable().setMeta(0, 4, 1, 3, 2).setMeta(1, 12, 9, 11, 10);
  addBlock(Block.skull);
  addBlock(Block.cocoaPlant).setRotatable().setMeta(0, 0, 1, 2, 3).setMeta(1, 4, 5, 6, 7).setMeta(2, 8, 9, 10, 11).setMeta(3, 12, 13, 14, 15);
  addBlock(Block.flowerPot);
  addBlock(Block.carrot);
  addBlock(Block.potato);
  addBlock(Block.mushroomCapBrown);
  addBlock(Block.mushroomCapRed);    
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

public int getRotatedMeta(int id, int meta, int rotationAmt)
  {
  if(BlockInfo.blockList[id]==null)
    {
    return 0;
    }
  return BlockInfo.blockList[id].rotateRight(meta, rotationAmt);
  }

}
