/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
package shadowmage.ancient_structures.common.template.build;

import net.minecraft.block.Block;

public class StructureValidationSettings
{

/**
 * unique structure-name
 */
String structureName;

/**
 * given an area with a source point, how far above the source-point is the highest acceptable block located? 
 * e.g. a 'normal' setting would be the same height as the structures above-ground height which would allow
 * generation of a structure with no blocks left 'hanging' above it, and minimal cut-in into a cliffside/etc
 * e.g. a 'perfect' setting would be 0, meaning the ground must be flat and level prior to generation
 * 
 * negative values imply to skip leveling checking
 */
int maxLeveling;

/**
 * if true, will clear blocks in leveling bounds PRIOR to template construction.  Set to false if you wish to
 * preserve any existing blocks within the structure bounds.
 */
boolean doLeveling;

/**
 * given an area with a source point, how far below the source point may 'holes' extend into the ground along the
 * edges of the structure.
 * e.g. a 'normal' setting would be 1-2 blocks, which would ensure that the chosen site was flat enough that it would
 * generate with minimal under-fill.
 * e.g. an 'extreme' setting to force-placement might be 4-8 blocks or more.  Placement would often be ugly with only
 * part of the structure resting on the ground.
 * 
 * negative values imply to skip edge-depth checking
 */
int maxMissingEdgeDepth;

/**
 * if true, will fill _directly_ below the structure down to the specified number of blocks from maxMissingEdgeDepth
 * filling will occur PRIOR to template construction.
 */
boolean doFillBelow;

/**
 * the size of the border around the base structure BB to check for additional functions
 * 0 or negative values denote no border.
 */
int borderSize;

/**
 * same as with structure-leveling -- how much irregularity can there be above the chose ground level
 * negative values imply to skip border leveling tests
 */
int borderMaxLeveling;
boolean doBorderLeveling;

/**
 * how irregular can the border surrounding the structure be in the -Y direction?
 * negative values imply to skip border depth tests
 */
int borderMissingEdgeDepth;

boolean biomeWhiteList;//should treat biome list as white or blacklist?
String[] biomeList;//list of biomes for white/black list.  treated as white/black list from whitelist toggle

boolean dimensionWhiteList;//should treat dimension list as white or blacklist?
int[] acceptedDimensions;//list of accepted dimensions treated as white/black list from whitelist toggle

Block[] acceptedTargetBlocks;//list of accepted blocks which the structure may be built upon -- 100% of blocks directly below the structure must meet this list

Block[] acceptedClearBlocks;//list of blocks which may be cleared/removed during leveling and buffer operations. 100% of blocks to be removed must meet this list

boolean isUnique;//should this structure generate only once?

/**
 * world generation selection and clustering settings
 */
boolean worldGenEnabled;
int selectionWeight;
int clusterValue;
int minDuplicateDistance;
}
