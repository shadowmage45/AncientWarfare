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
package shadowmage.ancient_warfare.common.structures.data;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import shadowmage.ancient_warfare.common.structures.build.Builder;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.structures.data.rules.NPCRule;
import shadowmage.ancient_warfare.common.structures.data.rules.SwapRule;
import shadowmage.ancient_warfare.common.structures.data.rules.VehicleRule;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.IDPairCount;

public abstract class AWStructure
{

/**
 * used to determine if the structure can be edited, (won't allow editing if there are known builders using
 * the struct)
 */
private Set<Builder> openBuilders = new HashSet<Builder>();

/**
 * if it is being edited....
 */
public boolean lockedForEdit = false;

/**
 * mostly file IO stuff...
 */
public String md5;
public String filePath;
public String name = "";

/**
 * is this structure available in survival-mode drafting station (should have a valid resourceList)
 */
public boolean survival = false;

/**
 * advanced world-gen settings (can spawn underground, in water/lava, partially underground, etc..)
 */
public boolean underground = false;
public int undergroundMinLevel=1;
public int undergroundMaxLevel=255;
public int undergroundMaxAirAbove = 0;
public int minSubmergedDepth = 0;
public int maxWaterDepth = 0;
public int maxLavaDepth = 0;

/**
 * preservation flags for entire structure
 */
public boolean preserveWater = false;
public boolean preserveLava = false;
public boolean preservePlants = false;
public boolean preserveBlocks = false;

/**
 * individual blockRules, will override structure rules for individual blocks
 * (incl advanced feature not supported by Ruins--per block preserve info)
 */
public Map<Integer, BlockRule> blockRules = new HashMap<Integer, BlockRule>();
public Map<Integer, VehicleRule> vehicleRules = new HashMap<Integer, VehicleRule>();
public Map<Integer, NPCRule> NPCRules = new HashMap<Integer, NPCRule>();
public Map<Integer, SwapRule> swapRules = new HashMap<Integer, SwapRule>();

/**
 * only set to false for bad values during parsing, struct is then discarded and not loaded into structures map
 */
public boolean isValid = true;

/**
 * if resourceList has been calculated or loaded from disk, this will not be null...
 */
public  List<IDPairCount> cachedCounts = null;

/**
 * structure biome settings
 */

public String[] biomesOnlyIn;
public String[] biomesNotIn;

/**
 * array of ruleID references making up this structure
 * these refer to the key in the blockRules map
 * this array basically holds the levels from the ruins template
 */
public short [][][] structure;

/**
 * how many blocks may be non-solid below this structure
 */
public int maxOverhang;

/**
 * how many blocks vertically above base may be cleared 
 */
public int maxVerticalClear;

/**
 * how many blocks around the structure to clear (outside of w,h,l)
 */
public int clearingBuffer;

/**
 * maximum vertical fill distance for missing blocks below the structure
 * overrides overhang numbers
 */
public int maxLeveling;

/**
 * how many blocks outside of w,h,l should be leveled around the structure
 */
public int levelingBuffer;

/**
 * valid targets to build this structure upon.
 * may be overridden with a custom list
 */
public int[] validTargetBlocks = {1,2,3,12,13};

/**
 * i.e. embed_into_distance
 * how far should this structure generate below the chosen site level
 */
public int verticalOffset;
public int xOffset;
public int zOffset;

public int xSize;//x dimension
public int zSize;//z dimension
public int ySize;//y dimension

public int openBuilderCount()
  {
  return this.openBuilders.size();
  }

public void addBuilder(Builder build)
  {
  this.openBuilders.add(build);
  }

public void removeBuilder(Builder build)
  {
  this.openBuilders.remove(build);
  }

public static StructureBB getBoundingBox(BlockPosition hit, int facing, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize)  
  {  
  BlockPosition fl = hit.copy();
  fl.moveLeft(facing, xOffset);
  fl.moveForward(facing, zOffset);
  fl.y -=yOffset;
  BlockPosition br = fl.copy();
  br.y+= ySize-1;
  br.moveRight(facing, xSize-1);
  br.moveForward(facing, zSize-1);
  return new StructureBB(fl, br);
  }

public static StructureBB getLevelingBoundingBox(BlockPosition hit, int facing, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, int maxLeveling, int levelingBuffer)
  {
  StructureBB bb = getBoundingBox(hit, facing, xOffset, yOffset, zOffset, xSize, ySize, zSize);
  BlockPosition min = BlockTools.getMin(bb.pos1, bb.pos2);
  BlockPosition max = BlockTools.getMax(bb.pos1, bb.pos2);
  min.y+=yOffset;
  min.y--;  
  max.y = min.y;  
  min.y -= maxLeveling - 1;  
  min.x -= levelingBuffer;
  min.z -= levelingBuffer;
  max.x += levelingBuffer;
  max.z += levelingBuffer;
  bb.pos1 = min;
  bb.pos2 = max;
  return bb;
  }

public static StructureBB getClearingBoundinBox(BlockPosition hit, int facing, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, int maxVerticalClear, int clearingBuffer)
  {
  StructureBB bb = getBoundingBox(hit, facing, xOffset, yOffset, zOffset, xSize, ySize, zSize);
  
  BlockPosition fl = bb.pos1.copy();
  BlockPosition br = bb.pos2.copy();
  
  fl.y += yOffset;
  fl.moveLeft(facing, clearingBuffer);
  fl.moveBack(facing, clearingBuffer);
  br.y = fl.y + maxVerticalClear - 1;;
  br.moveRight(facing, clearingBuffer);
  br.moveForward(facing, clearingBuffer);
  
  bb.pos1 = fl;
  bb.pos2 = br;
  return bb;
  }

}
