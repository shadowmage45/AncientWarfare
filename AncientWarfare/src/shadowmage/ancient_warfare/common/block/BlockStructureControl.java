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

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockStructureControl extends BlockContainer
{

/**
 * @param par1
 * @param par2
 * @param par3Material
 */
protected BlockStructureControl(int par1, int par2, Material par3Material)
  {
  super(par1, par2, par3Material);
  }

public int getBlockTeam(World world, int x, int y, int z)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te!=null)
    {
    return ((TEStructureControl)te).getTeamNum();
    }
  return 0;
  }

@Override
public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te!=null)
    {
    return ((TEStructureControl)te).onInteract(world, player);
    }
  return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
  }

}
