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
package shadowmage.ancient_warfare.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.machine.redstone.RedstoneData;
import shadowmage.ancient_warfare.common.machine.redstone.TERedstoneLogic;


public class ItemRedstoneLogic extends AWItemBlockBase
{

/**
 * @param par1
 */
public ItemRedstoneLogic(int par1)
  {
  super(par1);
  this.setHasSubtypes(true);
  this.setFull3D();
  }

@Override
public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  TERedstoneLogic ter = null;
  if(te instanceof TERedstoneLogic)
    {
    ter = (TERedstoneLogic)te;
    }
  boolean flag = ter!=null || super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
  if(flag)
    {
    te = world.getBlockTileEntity(x, y, z);    
    int type = RedstoneData.getTileType(stack);
    int meta = RedstoneData.getTileMeta(stack);
    ForgeDirection facing = ForgeDirection.UNKNOWN;

    ForgeDirection sideDirection = ForgeDirection.getOrientation(side).getOpposite();
    Config.logDebug(String.format("attempting placement %s::%s  on side: %s", type, meta, side));
    /**
     * TODO calc facing direction for tiles...wall tiles will need to use hit vec, floor/ceiling can use player facing (translated to forge direction)
     */
    if(side==0 || side==1)
      {
      facing = ForgeDirection.NORTH;
      }
    else
      {
      facing = ForgeDirection.UP;
      }    
    if(te instanceof TERedstoneLogic)
      {
      ter = (TERedstoneLogic)te;
      if(type==0)//handling for dummy/generic (invalid) data
        {
        Config.logDebug("type0 detected...");
        if(!ter.isValidTile())
          {
          Config.logDebug("invalid tile, removing...");
          world.setBlockToAir(x, y, z);
          }
        return false;
        }
      if(!ter.tryAddTile(sideDirection.ordinal(), type, meta, facing) && !ter.isValidTile())
        {
        Config.logDebug("could not add tile, and base tile is invalid tile, removing...");
        world.setBlockToAir(x, y, z);
        return false;
        }
      world.notifyBlockOfNeighborChange(x, y, z, BlockLoader.redstoneLogic.blockID);
      }
    else
      {
      Config.logDebug("invalid tile!!");
      }
    }
  return flag;
  }



}
