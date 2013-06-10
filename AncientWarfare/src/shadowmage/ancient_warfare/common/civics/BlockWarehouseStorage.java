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

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import shadowmage.ancient_warfare.common.block.AWBlockBase;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
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

@Override
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
          ware.removeWarehouseBlock(x, y, z, getStorageSizeFromMeta(meta));
          }
        }
      }
    }
  super.breakBlock(world, x, y, z, id, meta);
  }

public void registerBlockInfo()
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(blockID);
  d.setName("Basic Storage", 0);
  d.setName("Organized Storage", 1);
  d.setName("Advanced Storage",2);
  d.addDisplayStack(new ItemStack(this,1,0));
  d.addDisplayStack(new ItemStack(this,1,1));
  d.addDisplayStack(new ItemStack(this,1,2));
  }

@Override
public boolean canPlaceBlockAt(World world, int x, int y, int z)
  {
  boolean base = super.canPlaceBlockAt(world, x, y, z);
  if(base)
    {
    int blocksFound = 0;
    if(world.getBlockId(x-1, y, z)==this.blockID)
      {
      blocksFound++;
      if(getWarehouseBlockCountNear(world, x-1, y, z)>1)
        {
        base = false;
        }
      }
    if(world.getBlockId(x+1, y, z)==this.blockID)
      {
      blocksFound++;
      if(getWarehouseBlockCountNear(world, x+1, y, z)>1)
        {
        base = false;
        }
      }
    if(world.getBlockId(x, y, z-1)==this.blockID)
      {
      blocksFound++;
      if(getWarehouseBlockCountNear(world, x, y, z-1)>1)
        {
        base = false;
        }
      }
    if(world.getBlockId(x, y, z+1)==this.blockID)
      {
      blocksFound++;
      if(getWarehouseBlockCountNear(world, x, y, z+1)>1)
        {
        base = false;
        }
      }
    if(blocksFound>2)
      {
      base = false;
      }
    }  
  return base;
  }

public static int getWarehouseBlockCountNear(World world, int x, int y, int z)
  {
  int blocksFound = 0;  
  if(world.getBlockId(x-1, y, z)==BlockLoader.warehouseStorage.blockID)
    {
    blocksFound++;
    }
  if(world.getBlockId(x+1, y, z)==BlockLoader.warehouseStorage.blockID)
    {
    blocksFound++;
    }
  if(world.getBlockId(x, y, z-1)==BlockLoader.warehouseStorage.blockID)
    {
    blocksFound++;
    }
  if(world.getBlockId(x, y, z+1)==BlockLoader.warehouseStorage.blockID)
    {
    blocksFound++;
    }
  return blocksFound;
  }

@Override
public boolean canBlockStay(World par1World, int par2, int par3, int par4)
  {
  // TODO Auto-generated method stub
  return super.canBlockStay(par1World, par2, par3, par4);
  }

public static int getStorageSizeFromMeta(int meta)
  {
  return 9 + 9*meta;
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
          int meta = world.getBlockMetadata(x, y, z);
          ware.addWareHouseBlock(x, y, z, getStorageSizeFromMeta(meta));
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
//  Config.logDebug("registering icons for warehouse storage block "+d);
  int levels = 3;
  Icon icon;
  for(int i = 0; i < levels; i++)
    {
    icon = reg.registerIcon("ancientwarfare:civic/civicWarehouseStorage"+(i+1)+"Bottom");
    d.setIcon(icon, i*3);
    icon = reg.registerIcon("ancientwarfare:civic/civicWarehouseStorage"+(i+1)+"Top");
    d.setIcon(icon, i*3 + 1);
    icon = reg.registerIcon("ancientwarfare:civic/civicWarehouseStorage"+(i+1)+"Sides");
    d.setIcon(icon, i*3 + 2);
    }
  }

@Override
public Icon getIcon(int side, int meta)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(blockID);
  if(d!=null)
    {
    switch(side)
    {
    case 0:
    return d.getIconFor(meta*3);
    case 1:
    return d.getIconFor(meta*3+1);
    default:
    return d.getIconFor(meta*3+2);
    }    
    }
  return super.getIcon(side, meta);
  }

@Override
public int idDropped(int par1, Random par2Random, int par3)
  {
  return BlockLoader.warehouseStorage.blockID;
  }

@Override
public int damageDropped(int par1)
  {
  return par1;
  }

@Override
public int quantityDropped(Random par1Random)
  {
  return 1;
  }

@Override
protected ItemStack createStackedBlock(int par1)
  {
  return new ItemStack(BlockLoader.warehouseStorage,1,par1);
  }

@Override
public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
  {
  ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
  ret.add(createStackedBlock(metadata));
  return ret;
  }

@Override
public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
  {
  return new ItemStack(BlockLoader.warehouseStorage,1,world.getBlockMetadata(x, y, z));
  }

}
