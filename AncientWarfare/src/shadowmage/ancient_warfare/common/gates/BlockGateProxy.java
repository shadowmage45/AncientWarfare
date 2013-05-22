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
package shadowmage.ancient_warfare.common.gates;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.block.AWBlockContainer;
import shadowmage.ancient_warfare.common.item.CreativeTabAW;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class BlockGateProxy extends AWBlockContainer
{

/**
 * @param par1
 * @param par2Material
 * @param baseName
 */
public BlockGateProxy(int par1)
  {
  super(par1, Material.rock, "AW Gate Proxy Block");
  this.setCreativeTab(CreativeTabAW.normal);
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  return new TEGateProxy();
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
public boolean renderAsNormalBlock()
  {
  return false;
//  return super.renderAsNormalBlock();
  }

@Override
public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
  {
  return false;
//  return super.shouldSideBeRendered(par1iBlockAccess, par2, par3, par4, par5);
  }

@Override
public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
  return false;
//  return par1IBlockAccess.getBlockMaterial(par2, par3, par4).isSolid();
  }

@Override
public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2,  int par3, int par4)
  {
  return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
  }

@Override
public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
  {
  return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
  }

@Override
public boolean isOpaqueCube()
  {
  return false;
//  return super.isOpaqueCube();
  }

@Override
public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
  {  
  super.setBlockBoundsBasedOnState(world, x, y, z);
  }

@Override
public void setBlockBoundsForItemRender()
  {
  // TODO Auto-generated method stub
  super.setBlockBoundsForItemRender();
  }

@Override
public boolean isBlockReplaceable(World world, int x, int y, int z)
  {
  return super.isBlockReplaceable(world, x, y, z);
  }

@Override
public ItemStack getPickBlock(MovingObjectPosition target, World world, int x,  int y, int z)
  {
  return super.getPickBlock(target, world, x, y, z);
  }

@Override
public void registerIcons(IconRegister reg, Description d)
  {
  // TODO Auto-generated method stub
  }



}
