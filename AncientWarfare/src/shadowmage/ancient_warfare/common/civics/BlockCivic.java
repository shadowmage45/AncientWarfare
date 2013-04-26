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
package shadowmage.ancient_warfare.common.civics;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.CreativeTabAW;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;
import shadowmage.ancient_warfare.common.utils.BlockContainerSimpleSided;

public class BlockCivic extends BlockContainerSimpleSided
{

int blockNum;
/**
 * @param par1
 * @param par2
 * @param par3Material
 */
public BlockCivic(int par1, String name, int blockNum)
  {
  super(par1, Material.rock);
//  this.setTextureFile("/shadowmage/ancient_warfare/resources/block/blocks.png");
//  this.isDefaultTexture = false;
//  this.blockIndexInTexture = 0;
  this.setCreativeTab(CreativeTabAW.instance());
  this.setHardness(3.f);  
//  this.setBlockName(name);
  this.blockNum = blockNum;
  }

public static int getBlockTeam(World world, int x, int y, int z)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te!=null)
    {
    return ((TECivic)te).getTeamNum();
    }
  return 0;
  }

//@Override
//public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
//  {
//
//  }

@Override
public void onBlockAdded(World par1World, int par2, int par3, int par4)
  {
  par1World.setBlockTileEntity(par2, par3, par4, this.createTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4)));
  }

@Override
public boolean onBlockClicked(World world, int x, int y, int z, EntityPlayer player, int sideHit, float hitVecX, float hitVecY,    float hitVecZ)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te!=null)
    {
    return ((TECivic)te).onInteract(world, player);
    }
  return false;
  }

@Override
public IInventory[] getInventoryToDropOnBreak(World world, int x, int y, int z, int par5, int par6)
  {
  TECivic te = (TECivic) world.getBlockTileEntity(x, y, z);
  if(te!=null)
    {
    return new IInventory[]{te};
    }
  return null;
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  TileEntity te = CivicRegistry.instance().getTEFor(world, blockNum*4+meta);
  Config.logDebug("civic block getting te for: "+blockNum+":"+meta+" calc: "+(blockNum*4+meta) + " client: "+world.isRemote);
  return te;
  }

}
