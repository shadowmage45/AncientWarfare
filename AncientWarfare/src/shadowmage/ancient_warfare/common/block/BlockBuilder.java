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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.utils.BlockContainerSimpleSided;

public class BlockBuilder extends BlockContainerSimpleSided
{
/**
   * @param par1
   * @param par2Material
   */
public BlockBuilder(int par1)
  {
  super(par1, Material.rock);
  }

@Override
public boolean onBlockClicked(World world, int posX, int posY, int posZ,  EntityPlayer player, int sideHit, float hitVecX, float hitVecY,   float hitVecZ)
  {
  //NOOP
  return false;
  }

@Override
public IInventory[] getInventoryToDropOnBreak(World world, int x, int y, int z,  int par5, int par6)
  {
  //NOOP
  return null;
  }

@Override
public TileEntity createNewTileEntity(World var1)
  {
  return new TEBuilder();
  }

}
