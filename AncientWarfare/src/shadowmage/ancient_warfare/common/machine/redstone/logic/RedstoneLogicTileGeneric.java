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

import shadowmage.ancient_warfare.common.machine.redstone.TERedstoneLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public class RedstoneLogicTileGeneric implements IRedstoneLogicTile
{

/**
 * tileID reference, used to save/load from NBT
 */
protected int tileID;

/**
 * used for wire color or channelID
 */
protected int tileMeta;

protected boolean isPowered = false;

protected ForgeDirection facingDirection = ForgeDirection.UNKNOWN;
protected ForgeDirection placementSide = ForgeDirection.DOWN;
protected boolean[] connections = new boolean[6];

public RedstoneLogicTileGeneric(int tileID, int tileMeta)
  {
  this.tileID = tileID;
  this.tileMeta = tileMeta;
  }

@Override
public int getTileType()
  {
  return tileID;
  }

@Override
public int getTileMeta()
  {
  return tileMeta;
  }

@Override
public boolean canPlaceOnSide(int side, IRedstoneLogicTile [] otherTiles)
  {
  return false;
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("id", this.tileID);
  tag.setInteger("mt", this.tileMeta);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.tileID = tag.getInteger("id");
  this.tileMeta = tag.getInteger("mt"); 
  }

@Override
public boolean isOutputtingPower(int side)
  {
  return this.isPowered;
  }

@Override
public boolean updateState(TERedstoneLogic te)
  {
  return false;
  }

@Override
public boolean isPowered()
  {
  return this.isPowered;
  }

@Override
public ForgeDirection getFacingDirection()
  {
  return this.facingDirection;
  }

@Override
public boolean isSubConnected(ForgeDirection side, IRedstoneLogicTile[] tiles)
  {
  return false;
  }

@Override
public boolean isConnected(ForgeDirection side, IRedstoneLogicTile[] tiles)
  {
  return false;
  }

@Override
public ForgeDirection getPlacementSide()
  {
  return placementSide;
  }


}
