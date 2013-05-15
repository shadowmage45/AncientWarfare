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
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.block.AWBlockBase;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class BlockWarehouseStorage extends AWBlockBase
{

/**
 * @param par1
 * @param par2Material
 * @param baseName
 */
public BlockWarehouseStorage(int par1)
  {
  super(par1, Material.rock, "Warehouse Storage Block");
  }

@Override
public boolean onBlockClicked(World world, int posX, int posY, int posZ, EntityPlayer player, int sideHit, float hitVecX, float hitVecY, float hitVecZ)
  {
  return false;
  }

public void breakBlock(World world, int x, int y, int z, int id, int meta)
  {
  if(!world.isRemote)
    {
    TileEntity te;
    for(Object j : world.loadedTileEntityList)
      {
      te = (TileEntity)j;
      if(te.getClass()==TECivicWarehouse.class)
        {
        TECivicWarehouse ware = (TECivicWarehouse)te;
        if(x>=ware.minX && y>=ware.minY && z>=ware.minZ && x<=ware.maxX && y<=ware.maxY && z <=ware.maxZ)
          {
          ware.removeWarehouseBlock(x, y, z);
          }
        }
      }
    }
  super.breakBlock(world, x, y, z, id, meta);
  }

@Override
public void onBlockAdded(World world, int x, int y, int z)
  {
  if(!world.isRemote)
    {
    TileEntity te;
    for(Object j : world.loadedTileEntityList)
      {
      te = (TileEntity)j;
      if(te.getClass()==TECivicWarehouse.class)
        {
        TECivicWarehouse ware = (TECivicWarehouse)te;
        if(x>=ware.minX && y>=ware.minY && z>=ware.minZ && x<=ware.maxX && y<=ware.maxY && z <=ware.maxZ)
          {
          ware.addWareHouseBlock(x, y, z);
          break;
          }
        }
      }
    }
  super.onBlockAdded(world, x, y, z);
  }

@Override
public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
  {
  super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
  }

@Override
public IInventory[] getInventoryToDropOnBreak(World world, int x, int y, int z, int par5, int par6)
  {
  return null;
  }

@Override
public void registerIcons(IconRegister reg, Description d)
  {

  }

}
