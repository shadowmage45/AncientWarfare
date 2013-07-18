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
package shadowmage.ancient_warfare.common.machine;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEngine extends BlockMiscMachine
{

/**
 * @param par1
 * @param par2Material
 * @param baseName
 */
public BlockEngine(int par1, Material par2Material, String baseName)
  {
  super(par1, par2Material, baseName);
  }

@Override
public boolean isOpaqueCube()
  {
  return false;
  }

@Override
public boolean renderAsNormalBlock()
  {
  return false;
  }

@Override
public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int x, int y, int z, int par5)
  {
  return false;
  }

@Override
public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int x, int y, int z, int par5)
  {
  return false;
  }

@Override
public void registerIcons(IconRegister reg, Description d)
  {
  EngineData.registerIcons(reg, d);
  }

@Override
public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  return EngineData.getIcon(te, world.getBlockMetadata(x, y, z), side);
  }

@Override
public Icon getIcon(int side, int meta)
  {
  return EngineData.getIcon(null, meta, side);
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  return EngineData.getTEFor(meta);
  }
}
