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
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
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
  this.setCreativeTab(null);
  this.setResistance(2000.f);
  this.setHardness(5.f);
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
  }

@Override
public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
  {
  return false;
  }

@Override
public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
  return false;
  }

@Override
public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int x,  int y, int z)
  {
  return AxisAlignedBB.getAABBPool().getAABB(x+0.5d,y+0.5d,z+0.5d,x+0.5d,y+0.5d,z+0.5d);
  }

@Override
public boolean isOpaqueCube()
  {
  return false;
  }

@Override
public boolean isBlockReplaceable(World world, int x, int y, int z)
  {
  return super.isBlockReplaceable(world, x, y, z);
  }

@Override
public ItemStack getPickBlock(MovingObjectPosition target, World world, int x,  int y, int z)
  {
  return null;
  }

@Override
public void registerIcons(IconRegister reg, Description d)
  {
  d.setIcon(reg.registerIcon("ancientwarfare:gate/gateProxy"), 0);
  }

@Override
public int quantityDropped(Random par1Random)
  {
  return 0;
  }

@Override
public float getExplosionResistance(Entity par1Entity)
  {
  // TODO Auto-generated method stub
  return super.getExplosionResistance(par1Entity);
  }

@Override
public float getExplosionResistance(Entity par1Entity, World world, int x,  int y, int z, double explosionX, double explosionY, double explosionZ)
  {
  // TODO Auto-generated method stub
  return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX,
      explosionY, explosionZ);
  }



}
