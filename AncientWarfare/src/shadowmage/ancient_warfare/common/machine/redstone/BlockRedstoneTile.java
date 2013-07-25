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

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.block.AWBlockContainer;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class BlockRedstoneTile extends AWBlockContainer
{

/**
 * @param par1
 * @param par2Material
 * @param baseName
 */
public BlockRedstoneTile(int par1, String name)
  {
  super(par1, Material.circuits, name);
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  return new TERedstoneLogic();
  }

@Override
public boolean onBlockClicked(World world, int posX, int posY, int posZ, EntityPlayer player, int sideHit, float hitVecX, float hitVecY, float hitVecZ)
  {
  return false;
  }

@Override
public IInventory[] getInventoryToDropOnBreak(World world, int x, int y, int z, int par5, int par6)
  {
  return null;
  }

@Override
public void registerIcons(IconRegister reg, Description d)
  {
  RedstoneData.registerIcons(reg, d);
  }

@Override
public boolean onBlockEventReceived(World par1World, int par2, int par3,
    int par4, int par5, int par6)
  {
  // TODO Auto-generated method stub
  return super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
  }

@Override
public int isProvidingWeakPower(IBlockAccess par1iBlockAccess, int par2,
    int par3, int par4, int par5)
  {
  // TODO Auto-generated method stub
  return super.isProvidingWeakPower(par1iBlockAccess, par2, par3, par4, par5);
  }

@Override
public boolean canProvidePower()
  {
  // TODO Auto-generated method stub
  return super.canProvidePower();
  }

@Override
public int isProvidingStrongPower(IBlockAccess par1iBlockAccess, int par2,
    int par3, int par4, int par5)
  {
  // TODO Auto-generated method stub
  return super.isProvidingStrongPower(par1iBlockAccess, par2, par3, par4, par5);
  }

@Override
public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
  {
  return super.canConnectRedstone(world, x, y, z, side);
  }

@Override
public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
  {
  TileEntity te = par1World.getBlockTileEntity(par2,  par3,  par4);
  if(te instanceof TERedstoneLogic)
    {
    ((TERedstoneLogic)te).updatePoweredState();
    }
  super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
  }



}
