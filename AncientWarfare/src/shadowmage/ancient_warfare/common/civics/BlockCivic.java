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
import shadowmage.ancient_framework.common.registry.entry.Description;
import shadowmage.ancient_warfare.common.block.AWBlockContainer;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;

public class BlockCivic extends AWBlockContainer
{

public int blockNum;
/**
 * @param par1
 * @param par2
 * @param par3Material
 */
public BlockCivic(int par1, String baseName, int blockNum)
  {
  super(par1, Material.rock, baseName);
  this.setCreativeTab(null);
  this.setHardness(3.f);  
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
    return te.getInventoryToDropOnBreak();
    }
  return null;
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  TileEntity te = CivicRegistry.instance().getTEFor(world, blockNum*16 + meta);
//  Config.logDebug("civic block getting te for: "+blockNum+":"+meta+" calc: "+(blockNum*16+meta) + " client: "+world.isRemote);
  return te;
  }

@Override
public void registerIcons(IconRegister reg, Description d)
  {
  Civic civ;
  int civNum;
  String iconNames[];
  int iconID;
  for(int i = 0; i < 16; i++)
    {
    civNum = this.blockNum*16 + i;
    civ = CivicRegistry.instance().getCivicFor(civNum);    
    if(civ!=null)
      {
      iconNames = civ.getIconNames();
      iconID = i*3;//bottomID --  *3 is for only 3 textures per civic
      d.setIcon(reg.registerIcon(iconNames[0]), iconID);
      d.setIcon(reg.registerIcon(iconNames[1]), iconID+1);
      d.setIcon(reg.registerIcon(iconNames[2]), iconID+2); 
      }
    }
  }


@Override
public Icon getIcon(int side, int meta)
  {
  Description d = DescriptionRegistry.instance().getDescriptionFor(blockID);
  if(d!=null)
    {
    int iconID = meta*3;//bottomID  --  *3 is for only 3 textures per civic
    if(side==0)
      {
      return d.getIconFor(iconID);
      }
    else if(side==1)
      {
      return d.getIconFor(iconID+1);
      }
    else
      {
      return d.getIconFor(iconID+2);
      }
    }
  return super.getIcon(side, meta);
  }

/**
 * Returns the ID of the items to drop on destruction.
 */
@Override
public int idDropped(int par1, Random par2Random, int par3)
  {
//  if(blockNum*16 + par1==Civic.builder.getGlobalID())
//    {
//    return 0;
//    }
  return ItemLoader.civicPlacer.itemID;
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
  return CivicRegistry.instance().getItemFor(blockNum, par1);
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
//  Config.logDebug("getting id picked for: "+blockNum + " meta: "+world.getBlockMetadata(x, y, z));
  return CivicRegistry.instance().getItemFor(blockNum, world.getBlockMetadata(x, y, z));
  }


}
