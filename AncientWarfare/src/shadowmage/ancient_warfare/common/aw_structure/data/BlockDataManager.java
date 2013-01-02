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
  addBlock(0, "air", 0);
  addBlock(Block.sand,2);
  addBlock(Block.gravel,2);
  addBlock(Block.dispenser,1).setRotatable();  
  addBlock(Block.railPowered,3).setRotatable();
  addBlock(Block.railDetector,3).setRotatable();
  addBlock(Block.pistonStickyBase,1).setRotatable();
  addBlock(Block.tallGrass,3);
  addBlock(Block.deadBush,3);
  addBlock(Block.pistonBase,1).setRotatable();
  addBlock(Block.pistonExtension,1).setRotatable();
  addBlock(Block.pistonMoving,1).setRotatable();
  addBlock(Block.plantRed,3);
  addBlock(Block.plantYellow,3);
  addBlock(Block.mushroomBrown,3);
  addBlock(Block.mushroomRed,3);
  addBlock(Block.torchWood,3).setRotatable();
  addBlock(Block.stairCompactPlanks,1).setRotatable();
  addBlock(Block.chest,1).setRotatable();
  addBlock(Block.redstoneWire,3);
  addBlock(Block.crops,3);
  addBlock(Block.stoneOvenIdle,1).setRotatable();
  addBlock(Block.stoneOvenActive,1).setRotatable();
  addBlock(Block.signPost,3);
  addBlock(Block.doorWood,3).setRotatable();
  addBlock(Block.ladder,3).setRotatable();
  addBlock(Block.rail,3).setRotatable();
  addBlock(Block.stairCompactCobblestone,1).setRotatable();
  addBlock(Block.signWall,3).setRotatable();
  addBlock(Block.lever,3).setRotatable();
  addBlock(Block.doorSteel,3).setRotatable();
  addBlock(Block.torchRedstoneIdle,3).setRotatable();
  addBlock(Block.torchRedstoneActive,3).setRotatable();
  addBlock(Block.cactus,3);
  addBlock(Block.reed,3);
  addBlock(Block.fence,1).setRotatable();
  addBlock(Block.redstoneRepeaterIdle,3).setRotatable();
  addBlock(Block.redstoneLampActive,3).setRotatable();
  addBlock(Block.vine,3).setRotatable();
  addBlock(Block.fenceGate,1).setRotatable();
  addBlock(Block.stairsBrick,1).setRotatable();
  addBlock(Block.stairsStoneBrickSmooth,1).setRotatable();
  addBlock(Block.netherFence,1).setRotatable();
  addBlock(Block.stairsSandStone,1).setRotatable();
  addBlock(Block.enderChest,1).setRotatable();
  addBlock(Block.tripWireSource,3).setRotatable();
  addBlock(Block.tripWire,3).setRotatable();
  addBlock(Block.stairsWoodSpruce,1).setRotatable();
  addBlock(Block.stairsWoodBirch,1).setRotatable();
  addBlock(Block.stairsWoodJungle,1).setRotatable();
  addBlock(Block.cobblestoneWall,1).setRotatable();
  addBlock(Block.anvil,3).setRotatable();
  addBlock(Block.woodenButton,3).setRotatable();
  addBlock(Block.stoneButton,3).setRotatable();
  addBlock(Block.skull,3);
  addBlock(Block.cocoaPlant,3);
  addBlock(Block.flowerPot,3);
  addBlock(Block.carrot,3);
  addBlock(Block.potato,3);
  addBlock(Block.mushroomCapBrown,3);
  addBlock(Block.mushroomCapRed,3);  
  
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
public static BlockInfo addBlock(Block block, int priority)
  {
  return BlockInfo.createEntryFor(block, priority);
  }

public static BlockInfo addBlock(int id, String name, int priority)
  {
  return BlockInfo.createEntryFor(id, name, priority);
  }

}
