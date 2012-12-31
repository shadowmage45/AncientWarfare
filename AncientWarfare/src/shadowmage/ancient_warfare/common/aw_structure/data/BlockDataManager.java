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

/**
 * Block info list, will hold one entry for every vanilla block that has meta-data
 */
static BlockInfo[] blockInfo = new BlockInfo[4096];

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

//list of block swap-type groups, or map by enum/type?

//list of BlockInfo, entry for every vanilla block that has metadata or is part of a swap group

//list of rotatable blocks, w/ rotation metadata

 

}
