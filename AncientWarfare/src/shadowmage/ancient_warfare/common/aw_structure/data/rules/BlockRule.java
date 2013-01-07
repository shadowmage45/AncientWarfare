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
package shadowmage.ancient_warfare.common.aw_structure.data.rules;

import java.util.Random;

import shadowmage.ancient_warfare.common.aw_structure.data.BlockData;

public class BlockRule
{

/**
 * base chance to lay this block
 */
final int baseChance;

/**
 * block conditional
 * 0--always valid
 * 1--must be block below
 * 2--must be block beside
 * 3--must be block above
 */
final int conditional;

/**
 * blockData array for each block ID/meta for this blockRule, may contain duplicates for weighting
 */
final BlockData[] blockData;
public boolean preserveBlocks = false;
public boolean preserveWater = false;
public boolean preservePlants = false;

public BlockRule(int chance, int conditional, BlockData[] blockIDs)
  {
  this.baseChance = chance;
  this.conditional = conditional;
  this.blockData = blockIDs;
  }

public BlockRule setPreservationRules(boolean blocks, boolean water, boolean plants)
  {
  this.preserveBlocks = blocks;
  this.preserveWater = water;
  this.preservePlants = plants;
  return this;
  }

public BlockRule setPreserveBlocks()
  {
  this.preserveBlocks = true;
  return this;
  }

public BlockRule setPreserveWater()
  {
  this.preserveWater = true;
  return this;
  }

public BlockRule setPreservePlants()
  {
  this.preservePlants = true;
  return this;
  }

public BlockData getBlockChoice(Random random)
  {
  return blockData[random.nextInt(blockData.length)];
  }

}
