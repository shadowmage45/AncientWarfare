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

import java.util.List;
import java.util.Random;

import shadowmage.ancient_warfare.common.aw_structure.data.BlockData;

public class BlockRule
{

/**
 * base chance to lay this block
 */
int baseChance = 100;

/**
 * block conditional
 * 0--always valid
 * 1--must be block below
 * 2--must be block beside
 * 3--must be block above
 */
int conditional = 0;

/**
 * blockData array for each block ID/meta for this blockRule, may contain duplicates for weighting
 */
public BlockData[] blockData;

public boolean preserveWater = false;
public boolean preserveLava = false;
public boolean preservePlants = false;
public boolean preserveBlocks = false;

public BlockRule(int chance, int conditional, BlockData[] blockIDs)
  {
  this.baseChance = chance;
  this.conditional = conditional;
  this.blockData = blockIDs;
  }

public BlockData getBlockChoice(Random random)
  {
  return blockData[random.nextInt(blockData.length)];
  }

public static BlockRule parseRule(List<String> ruleLines)
  {
  return null;
  }

}
