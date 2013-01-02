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

public static ArrayList<BlockSwapGroup> groups = new ArrayList<BlockSwapGroup>();

public static boolean isBlockSwappable(int id, int meta)
  {
  for(BlockSwapGroup group : groups)
    {
    if(group.isBlockInGroup(id, meta))
      {
      return true;
      }
    }
  return false;
  }

public BlockSwapGroup()
  {
  groups.add(this);
  }

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
 * primary slabID
 */
public int slabID;
/**
 * slab meta-data for bottom slab
 */
public int slabMeta;
/**
 * slab meta-data for top slab
 */
public int slabSecondMeta;

/**
 * blockID of doubleSlab
 */
public int slabDoubleID;
public int slabDoubleMeta;


/**
 * list of biomes in which this block group type is allowed for swapping-in
 */
public List<String> defaultAllowedBiomes = new ArrayList<String>();

/**
 * biome names in which this is explicitly NOT allowed to be used in
 */
public List<String> defaultExcludedBiomes = new ArrayList<String>();

public boolean isBlockInGroup(int id, int meta)
  {
  if(id==this.baseID.id && meta==this.baseID.meta || id==this.stairID|| id==this.slabID && meta==this.slabMeta || id==this.slabID && meta==this.slabSecondMeta || id==this.slabDoubleID && meta==this.slabDoubleMeta)
    {
    return true;
    }
  else if(this.alternateIDs.size()>0)    
    {
    for(BlockData data : this.alternateIDs)
      {
      if(data.id==id && data.meta == meta)
        {
        return true;
        }
      }
    }  
  return false;
  }


}
