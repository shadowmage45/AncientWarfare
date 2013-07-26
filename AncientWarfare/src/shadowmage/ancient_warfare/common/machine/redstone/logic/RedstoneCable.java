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
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.machine.redstone.RedstoneData;
import shadowmage.ancient_warfare.common.machine.redstone.TERedstoneLogic;

public class RedstoneCable extends RedstoneLogicTileGeneric
{

/**
 * @param tileID
 * @param tileMeta
 */
public RedstoneCable(int tileID, int tileMeta)
  {
  super(tileID, tileMeta);
  }

@Override
public boolean canPlaceOnSide(int side, IRedstoneLogicTile[] otherTiles)
  {
  Config.logDebug("trying to place cable on side: "+side);
  return otherTiles[side]==null && (otherTiles[6]==null || otherTiles[6].getTileType() == RedstoneData.CONDUIT);
//  return super.canPlaceOnSide(side, otherTiles);
  }

@Override
public boolean isOutputtingPower(int side)
  {
  /**
   * e.g. if occupying the north wall of THIS block (placed on south wall of a block), send power in that direction
   */
  return this.isPowered && side == this.placementSide.ordinal();
  }

@Override
public boolean updateState(TERedstoneLogic te)
  {
  ForgeDirection direction;
  int x;
  int y;
  int z;
  this.isPowered = false;
  Config.logDebug("updating state..");
  for(int i = 0; i <6 ; i++)
    {
    direction = ForgeDirection.values()[i];
    x = te.xCoord + direction.offsetX;
    y = te.yCoord + direction.offsetY;
    z = te.zCoord + direction.offsetZ;
    if(te.worldObj.isBlockIndirectlyGettingPowered(x, y, z))
      {
      this.isPowered = true;
      break;
      }
    }
  return super.updateState(te);
  }

@Override
public boolean isSubConnected(ForgeDirection side, IRedstoneLogicTile[] tiles)
  {
  return super.isSubConnected(side, tiles);
  }

@Override
public boolean isConnected(ForgeDirection side, IRedstoneLogicTile[] tiles)
  {
  return super.isConnected(side, tiles);
  }



}
