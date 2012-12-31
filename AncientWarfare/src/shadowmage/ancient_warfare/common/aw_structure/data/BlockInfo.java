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

/**
 * full info wrapper on a block.
 * Holds priority information (which pass to build it in, for ladders, doors, trapdoors, torches, redstone dust, etc)
 * Holds rotation metadata table
 * @author Shadowmage
 *
 */
public class BlockInfo
{

/**
 * MC blockID for this block
 */
int blockID;

/**
 * 0 is default, and built first.  Should be used for base blocks that do not need any blocks nearby to stay valid
 * 1 is for first-tier rotatable blocks (stairs, slabs)
 * 2 is for decorative blocks and those that need nearby structure to be valid (ladder, vine, torch, trapdoor, redstone)
 * 3 is for last-detail blocks, which may rely on blocks in list3 to be valid (2nd block for a door)
 */
int buildPriority;

/**
 * metadata rotation lists
 */
byte[] meta0Rotation = new byte[4];
byte[] meta1Rotation = new byte[4];
byte[] meta2Rotation = new byte[4];
byte[] meta3Rotation = new byte[4];

/**
 * a reference to the swap group that this block belongs to.
 * NULL if block does not belong to a group
 */
BlockSwapGroup swapGroup;

}
