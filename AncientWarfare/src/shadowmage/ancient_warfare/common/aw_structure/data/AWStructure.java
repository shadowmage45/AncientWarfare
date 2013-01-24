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

import shadowmage.ancient_warfare.common.aw_structure.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.aw_structure.data.rules.NPCRule;
import shadowmage.ancient_warfare.common.aw_structure.data.rules.VehicleRule;

public abstract class AWStructure
{

public String name;
public boolean worldGen = false;
public boolean creative = true;
public boolean survival = false;
public boolean unique;
public int structureWeight=1;
public int chunkDistance=0;
public int chunkAttempts=1;

public boolean underground = false;
public int undergroundMinLevel=1;
public int undergroundMaxLevel=255;
public int undergroundMaxAirAbove = 0;
public boolean undergroundAllowPartial = false;

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
public List<BlockRule> blockRules = new ArrayList<BlockRule>();
public List<VehicleRule> vehicleRules = new ArrayList<VehicleRule>();
public List<NPCRule> NPCRules = new ArrayList<NPCRule>();

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


}
