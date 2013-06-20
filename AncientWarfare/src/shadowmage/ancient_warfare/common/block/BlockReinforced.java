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
package shadowmage.ancient_warfare.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockReinforced extends AWBlockContainer
{


/**
 * @param par1
 * @param par2Material
 * @param baseName
 */
public BlockReinforced(int par1, Material par2Material, String baseName)
  {
  super(par1, par2Material, baseName);
  }

@Override
public boolean renderAsNormalBlock()
  {
  return true;
  }

public void registerBlockInfo()
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(this.blockID);
  d.setName("Reinforced Stone Brick", 0);
  d.setName("Reinforced Red Brick", 1);
  d.setName("Reinforced Nether Brick", 2);
  d.addDisplayStack(new ItemStack(this,1,0));
  d.addDisplayStack(new ItemStack(this,1,1));
  d.addDisplayStack(new ItemStack(this,1,2));
  d.setIconTexture("ancientwarfare:reinforced/stoneBrick", 0);
  d.setIconTexture("ancientwarfare:reinforced/redBrick", 1);
  d.setIconTexture("ancientwarfare:reinforced/netherBrick", 2);
  }

@Override
@SideOnly(Side.CLIENT)
public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
  {
  TEAWBlockReinforced te = (TEAWBlockReinforced)par1iBlockAccess.getBlockTileEntity(par2, par3, par4);
  Description d = DescriptionRegistry2.instance().getDescriptionFor(this.blockID);
  if(d==null){return null;}
  return d.getIconFor(te.baseBlockID);
  }

@Override
public Icon getIcon(int side, int meta)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(this.blockID);
  if(d==null){return null;}
  return d.getIconFor(0);
  }

@Override
public int getRenderType()
  {  
  return 0;
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  return new TEAWBlockReinforced();
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
  d.registerIcons(reg);
  }

/**
 * Called when the block is destroyed by an explosion.
 * Useful for allowing the block to take into account tile entities,
 * metadata, etc. when exploded, before it is removed.
 *
 * @param world The current world
 * @param x X Position
 * @param y Y Position
 * @param z Z Position
 * @param Explosion The explosion instance affecting the block
 */
public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
  {
  TEAWBlockReinforced te = (TEAWBlockReinforced) world.getBlockTileEntity(x, y, z);
  te.onExploded(explosion);
  }

@Override
public float getBlockHardness(World world, int x, int y, int z)
  {
  // TODO Auto-generated method stub
  return super.getBlockHardness(world, x, y, z);
  }

@Override
public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
  {
  // TODO Auto-generated method stub
  super.onBlockDestroyedByPlayer(world, x, y, z, meta);
  }

@Override
public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z)
  {
  // TODO Auto-generated method stub
  return super.getPlayerRelativeBlockHardness(player, world, x, y, z);
  }

@Override
public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
  {
  // TODO Auto-generated method stub
  super.onBlockHarvested(world, x, y, z, meta, player);
  }

@Override
public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
  {
  // TODO Auto-generated method stub
  return super.removeBlockByPlayer(world, player, x, y, z);
  }


}
