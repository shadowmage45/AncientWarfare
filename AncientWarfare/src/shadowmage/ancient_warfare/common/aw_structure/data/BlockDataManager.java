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
  addBlock(Block.dispenser).setRotatable();  
  addBlock(Block.railPowered).setRotatable();
  addBlock(Block.railDetector).setRotatable();
  addBlock(Block.pistonStickyBase).setRotatable();
  addBlock(Block.tallGrass);
  addBlock(Block.deadBush);
  addBlock(Block.pistonBase).setRotatable();
  addBlock(Block.pistonExtension).setRotatable();
  addBlock(Block.pistonMoving).setRotatable();
  addBlock(Block.plantRed);
  addBlock(Block.plantYellow);
  addBlock(Block.mushroomBrown);
  addBlock(Block.mushroomRed);
  addBlock(Block.torchWood).setRotatable();
  addBlock(Block.stairCompactPlanks).setRotatable();
  addBlock(Block.chest).setRotatable();
  addBlock(Block.redstoneWire);
  addBlock(Block.crops);
  addBlock(Block.stoneOvenIdle).setRotatable();
  addBlock(Block.stoneOvenActive).setRotatable();
  addBlock(Block.signPost);
  addBlock(Block.doorWood).setRotatable();
  addBlock(Block.ladder).setRotatable();
  addBlock(Block.rail).setRotatable();
  addBlock(Block.stairCompactCobblestone).setRotatable();
  addBlock(Block.signWall).setRotatable();
  addBlock(Block.lever).setRotatable();
  addBlock(Block.doorSteel).setRotatable();
  addBlock(Block.torchRedstoneIdle).setRotatable();
  addBlock(Block.torchRedstoneActive).setRotatable();
  addBlock(Block.cactus);
  addBlock(Block.reed);
  addBlock(Block.fence).setRotatable();
  addBlock(Block.redstoneRepeaterIdle).setRotatable();
  addBlock(Block.redstoneLampActive).setRotatable();
  addBlock(Block.vine).setRotatable();
  addBlock(Block.fenceGate).setRotatable();
  addBlock(Block.stairsBrick).setRotatable();
  addBlock(Block.stairsStoneBrickSmooth).setRotatable();
  addBlock(Block.netherFence).setRotatable();
  addBlock(Block.stairsSandStone).setRotatable();
  addBlock(Block.enderChest).setRotatable();
  addBlock(Block.tripWireSource).setRotatable();
  addBlock(Block.tripWire).setRotatable();
  addBlock(Block.stairsWoodSpruce).setRotatable();
  addBlock(Block.stairsWoodBirch).setRotatable();
  addBlock(Block.stairsWoodJungle).setRotatable();
  addBlock(Block.cobblestoneWall).setRotatable();
  addBlock(Block.anvil).setRotatable();
  addBlock(Block.woodenButton).setRotatable();
  addBlock(Block.stoneButton).setRotatable();
  addBlock(Block.skull).setRotatable();
  addBlock(Block.cocoaPlant).setRotatable();
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
