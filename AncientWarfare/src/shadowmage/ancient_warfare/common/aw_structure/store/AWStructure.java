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
package shadowmage.ancient_warfare.common.aw_structure.store;

/**
 * In memory stored structure data.  Builders will pull from an instance
 * of this class to populate block and component lists.
 * @author Shadowmage
 *
 */
public class AWStructure
{
/**
 * The beginning orientation of this structure; i.e. what direction the player was facing when he scanned it.
 * Determines where the 'door' will be.
 */
int facing;

/**
 * Used by GUI's, need not be unique, but should be (mostly set by players, might enforce validation on entry
 * by a player)
 */
String name;

}
