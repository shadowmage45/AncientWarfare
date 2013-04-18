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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.item.CreativeTabAW;
import shadowmage.ancient_warfare.common.utils.BlockContainerSimpleSided;

public abstract class BlockStructureControl extends BlockContainerSimpleSided
{

/**
 * @param par1
 * @param par2
 * @param par3Material
 */
protected BlockStructureControl(int par1)
  {
  super(par1, Material.rock);
  this.setTextureFile("/shadowmage/ancient_warfare/resources/block/blocks.png");
  this.isDefaultTexture = false;
  this.blockIndexInTexture = 0;
  this.setCreativeTab(CreativeTabAW.instance());
  this.setHardness(3.f);
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
public boolean onBlockClicked(World world, int x, int y, int z, EntityPlayer player, int sideHit, float hitVecX, float hitVecY,    float hitVecZ)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te!=null)
    {
    return ((TEStructureControl)te).onInteract(world, player);
    }
  return false;
  }

@Override
public TileEntity createTileEntity(World world, int metadata)
  {
  return null;
  }
}
