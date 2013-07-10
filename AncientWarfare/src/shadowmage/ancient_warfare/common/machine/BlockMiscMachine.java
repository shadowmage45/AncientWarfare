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
package shadowmage.ancient_warfare.common.machine;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.block.AWBlockContainer;
import shadowmage.ancient_warfare.common.interfaces.IInteractable;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class BlockMiscMachine extends AWBlockContainer
{

public BlockMiscMachine(int par1, Material par2Material, String baseName)
  {
  super(par1, par2Material, baseName);
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  return MachineData.getTEFor(meta);
  }

@Override
public boolean onBlockClicked(World world, int posX, int posY, int posZ, EntityPlayer player, int sideHit, float hitVecX, float hitVecY, float hitVecZ)
  {
  TileEntity te = world.getBlockTileEntity(posX, posY, posZ);
  if(te instanceof IInteractable)
    {
    ((IInteractable)te).onPlayerInteract(player);
    return true;
    }
  return false;
  }

@Override
public IInventory[] getInventoryToDropOnBreak(World world, int x, int y, int z,  int par5, int par6)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te instanceof IInventory)
    {
    return new IInventory[]{(IInventory)te};
    }  
  return null;
  }

@Override
public void breakBlock(World world, int x, int y, int z, int par5, int par6)
  {
  super.breakBlock(world, x, y, z, par5, par6);
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEMachine)
    {
    ((TEMachine)te).onBlockBreak();
    }
  }

@Override
public void registerIcons(IconRegister reg, Description d)
  {
  MachineData.registerIcons(reg, d);
  }

@Override
public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  return MachineData.getIcon(te, world.getBlockMetadata(x, y, z), side);
  }

@Override
public Icon getIcon(int side, int meta)
  {
  return MachineData.getIcon(null, meta, side);
  }

}
