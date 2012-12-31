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

import java.util.ArrayList;
import java.util.List;

public abstract class BlockSwapGroup
{

public boolean canBeRandomized = false;

/**
 * the primary solid block that makes up this group
 */
public BlockData baseID;

/**
 * used by cobble and stone brick, for the possibility of randomizing between mossy/cracked
 * during construction of world-gen structures.
 */
public ArrayList<BlockData> alternateIDs = new ArrayList<BlockData>();

/**
 * primary stairID;
 */
public int stairID;
/**
 * stair metaData (not sure if needed)
 */
public int stairMeta;

/**
 * primary slabID
 */
public int slabID;
/**
 * slab meta-data (not sure if needed)
 */
public int slabMeta;

/**
 * list of biomes in which this block group type is allowed for swapping-in
 */
public List<String> defaultAllowedBiomes = new ArrayList<String>();

/**
 * biome names in which this is explicitly NOT allowed to be used in
 */
public List<String> defaultExcludedBiomes = new ArrayList<String>();




}
