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
import java.util.Map;

import shadowmage.ancient_warfare.common.aw_structure.data.rules.BlockRule;

/**
 * fully processed structure, ready to build in-game.  Needs to support all of Ruins template
 * options.
 * @author Shadowmage
 *
 */
public class ProcessedStructure
{
/**
 * preservation flags for entire structure
 */
boolean preserveWater = false;
boolean preserveLava = false;
boolean preservePlants = false;
boolean preserveBlocks = false;

/**
 * individual blockRules, will override structure rules for individual blocks
 * (incl advanced feature not supported by Ruins--per block preserve info)
 */
Map<Integer, BlockRule> blockRules = new HashMap<Integer, BlockRule>();

/**
 * array of ruleID references making up this structure
 * these refer to the key in the blockRules map
 * this array basically holds the levels from the ruins template
 */
byte [][][] structure;

/**
 * how many blocks may be non-solid below this structure
 */
int overhangMax;

/**
 * how many blocks vertically above base may be cleared 
 */
int maxVerticalClear;

/**
 * how many blocks around the structure to clear (outside of w,h,l)
 */
int horizontalClearBuffer;

/**
 * maximum vertical fill distance for missing blocks below the structure
 * overrides overhang numbers
 */
int maxLeveling;

/**
 * how many blocks outside of w,h,l should be leveled around the structure
 */
int horizontalLevelBuffer;

/**
 * valid targets to build this structure upon.
 * may be overridden with a custom list
 */
int[] validTargetBlocks = {1,2,3,12,13};

/**
 * i.e. embed_into_distance
 * how far should this structure generate below the chosen site level
 */
int verticalOffset;

int width;//x dimension
int length;//z dimension
int height;//y dimension

public ProcessedStructure(int x, int y, int z)
  {
  this.structure = new byte[x][y][z];
  this.width = x;
  this.length = z;
  this.height = y;
  }

public void setValidTargetBlocks(int[] validTargets)
  {
  this.validTargetBlocks = validTargets;
  }

public int[] getValidTargets()
  {
  return this.validTargetBlocks;
  }

public void addRule(int ruleNumber, BlockRule rule)
  {
  this.blockRules.put(ruleNumber, rule);
  }

}
