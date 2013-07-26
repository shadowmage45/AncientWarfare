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
package shadowmage.ancient_warfare.common.machine.redstone;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.machine.redstone.logic.IRedstoneLogicTile;

public class TERedstoneLogic extends TileEntity
{


/**
 * tiles on individual sides of the block, index 6 (tile7) is used for center
 */
public IRedstoneLogicTile[] tiles = new IRedstoneLogicTile[7];

public TERedstoneLogic()
  {
  
  }


public void propogateCurrent(ForgeDirection from, ForgeDirection side, int inputCurrent)
  {
  int highestNeighbor = 0;
  ForgeDirection to;
  int x;
  int y;
  int z;
  int id;
  for(int i = 0; i < 6; i++)
    {
    to = ForgeDirection.values()[i];
    if(to==from || !this.isEmittingPower(to.ordinal())){continue;}
    x = this.xCoord + to.offsetX;
    y = this.yCoord + to.offsetY;
    z = this.zCoord + to.offsetZ;
    id = this.worldObj.getBlockId(x, y, z);
    if(id != BlockLoader.redstoneLogic.blockID)
      {
      //check if block is active redstone/torch/etc, and that this block has a component connected to it??
      }
    else
      {
      TERedstoneLogic te = (TERedstoneLogic) this.worldObj.getBlockTileEntity(x, y, z);
      
      }
    }
  }

/**
 * does this TE generate an active (full strength) current?
 * @param side
 * @return
 */
public boolean isPowerProvider(int side)
  {
  for(IRedstoneLogicTile t : this.tiles)
    {
    if(t!=null && t.isProvidingPower(side))
      {
      return true;
      }
    }
  return false;
  }

public boolean isEmittingPower(int sideOut)
  {
  if(tiles[sideOut]!=null)
    {
    return false;
    }
  for(IRedstoneLogicTile t : this.tiles)
    {
    if(t!=null && t.isOutputtingPower(sideOut))
      {
      return true;
      }
    }
  return false;
  }

public boolean isValidTile()
  {
  for(IRedstoneLogicTile t : this.tiles)
    {
    if(t!=null)
      {
      return true;
      }
    }
  return false;
  }

public boolean tryAddTile(int side, int type, int meta, ForgeDirection facing)
  {
  IRedstoneLogicTile tile = RedstoneData.getLogicTile(type, meta);
  if(tile.canPlaceOnSide(side, tiles))
    {
    this.tiles[side]=tile;
    return true;
    }
  return false;
  }

public boolean tryRemovetile(int side)
  {
  boolean flag = this.tiles[side]!=null;
  this.tiles[side] = null;
  return flag;
  }

public void updatePoweredState()
  {
  for(IRedstoneLogicTile t : this.tiles)
    {
    if(t!=null)
      {
      t.updateState(this);
      }
    }
  }

}
