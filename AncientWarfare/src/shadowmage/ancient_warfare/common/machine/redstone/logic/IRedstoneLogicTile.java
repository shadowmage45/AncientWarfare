/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.machine.redstone.logic;

import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.machine.redstone.TERedstoneLogic;

public interface IRedstoneLogicTile extends INBTTaggable
{

/**
 * @return global type number
 */
int getTileType();

/**
 * @return logic tile meta (used for wiring colors, some other options (timers))
 */
int getTileMeta();



/**
 * can this tile be placed with the other tiles in a block?
 * @param side
 * @param otherTiles
 * @return true if can be placed on side
 */
boolean canPlaceOnSide(int side, IRedstoneLogicTile [] otherTiles); 

/**
 * is there an internal connection going from THIS tile to the passed in side?
 * @param side
 * @return
 */
boolean isSubConnected(ForgeDirection side, IRedstoneLogicTile[] tiles);

/**
 * is there a connection between this tile and EXTERNAL 
 * @param side
 * @return
 */
boolean isConnected(ForgeDirection side, IRedstoneLogicTile[] tiles);

/**
 * is this tile outputting power towards the side
 * @param side
 * @return
 */
boolean isOutputtingPower(int side);

/**
 * @param te
 * @return true if state changed, should update client
 */
boolean updateState(TERedstoneLogic te);

/**
 * @return true if this tile is in the powered state
 */
boolean isPowered();

/**
 * the side the tile is facing (default output)
 * @return
 */
ForgeDirection getFacingDirection();

/**
 * the side the tile resides on
 * @return
 */
ForgeDirection getPlacementSide();

}
