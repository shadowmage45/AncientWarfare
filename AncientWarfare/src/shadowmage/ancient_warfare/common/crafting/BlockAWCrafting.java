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
package shadowmage.ancient_warfare.common.crafting;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.block.AWBlockContainer;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class BlockAWCrafting extends AWBlockContainer
{
/**
 * @param par1
 * @param par2Material
 * @param baseName
 */
public BlockAWCrafting(int par1, Material par2Material, String baseName)
  {
  super(par1, par2Material, baseName);
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  switch(meta)
  {
  case 0:
  case 1:
  case 2:
  case 3:
  case 4:
  case 5:
  case 7:
  case 8:
  case 9:
  case 10:
  case 11:
  case 12:
  case 13:
  case 14:
  case 15:
  }
  return null;
  }

@Override
public boolean onBlockClicked(World world, int posX, int posY, int posZ, EntityPlayer player, int sideHit, float hitVecX, float hitVecY, float hitVecZ)
  {
  return false;
  }

@Override
public IInventory[] getInventoryToDropOnBreak(World world, int x, int y, int z, int par5, int par6)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWCrafting)
    {
    return new IInventory[]{(TEAWCrafting)te};
    }
  return null;
  }

@Override
public void registerIcons(IconRegister reg, Description d)
  {
  /**
   * this is going to be fun...
   */
  }

public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
  TEAWCrafting te = (TEAWCrafting) par1IBlockAccess.getBlockTileEntity(par2, par3, par4);
  if(te!=null)
    {
    return te.getIconForSide(this, par5, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
  return this.getIcon(par5, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
  }

}
