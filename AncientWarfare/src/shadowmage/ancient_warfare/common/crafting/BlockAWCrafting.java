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

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.block.AWBlockContainer;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.AWItemBlockBase;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class BlockAWCrafting extends AWBlockContainer
{

int renderID;

/**
 * @param par1
 * @param par2Material
 * @param baseName
 */
public BlockAWCrafting(int par1, int renderID)
  {
  super(par1, Material.rock, "AWCraftingBlock");
  this.renderID = renderID;
  this.setLightOpacity(0);
  }

//@Override
//public int getRenderType()
//  {
//  return renderID;
//  }

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
public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
  {
  return false;
  }

@Override
public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
  return false;
  }

public void registerBlockInfo()
  {
  String baseTexDir = "ancientwarfare:crafting/";
  Description d = BlockLoader.instance().registerBlockWithItem(this, "AWCraftingBlock", AWItemBlockBase.class);
  d.setName("Research Table", 0);
  d.addDisplayStack(new ItemStack(this,1,0));
  d.setName("Engineering Station", 1);
  d.addDisplayStack(new ItemStack(this,1,1));
  d.setName("Civil Engineering Station", 2);
  d.addDisplayStack(new ItemStack(this,1,2));
  
  d.setIconTexture(baseTexDir+"researchBlockBottom", 0);
  d.setIconTexture(baseTexDir+"researchBlockTop", 1);
  d.setIconTexture(baseTexDir+"researchBlockFront", 2);
  d.setIconTexture(baseTexDir+"researchBlockBack", 3);
  d.setIconTexture(baseTexDir+"researchBlockLeft", 4);
  d.setIconTexture(baseTexDir+"researchBlockRight", 5);  
  
  GameRegistry.registerTileEntity(TEAWResearch.class, "Research Center");
  GameRegistry.registerTileEntity(TEAWEngineering.class, "Engineering Station");
  GameRegistry.registerTileEntity(TEAWStructureCraft.class, "Civil Engineering Station");
  GameRegistry.registerTileEntity(TEAWVehicleCraft.class, "Vehicle Engineering Station");
  }

@Override
public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack stack)
  {
  if(entity==null || entity.worldObj.isRemote)
    {
    return;
    }
  int face = BlockTools.getBlockFacingMetaFromPlayerYaw(entity.rotationYaw);
  TEAWCrafting te = (TEAWCrafting)world.getBlockTileEntity(x, y, z);
  te.setOrientation(face);
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  switch(meta)
  {
  case 0:
  return new TEAWResearch();
  case 1:
  return new TEAWEngineering();
  case 2:
  return new TEAWStructureCraft();
  case 3:
  return new TEAWVehicleCraft();
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
public IInventory[] getInventoryToDropOnBreak(World world, int x, int y, int z, int par5, int par6)
  {
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te instanceof IInventory)
    {
    return new IInventory[]{(IInventory)te};
    }
  return null;
  }

@Override
public void registerIcons(IconRegister reg, Description d)
  {
  if(d==null){return;}
  for(int k = 0; k< 6; k++)
    {
    String tex = d.getIconTexture(k);   
    d.setIcon(reg.registerIcon(tex), k);
    }
  }

@Override
public Icon getIcon(int side, int meta)
  {
  int index = meta*6 + side;
  Description d = DescriptionRegistry2.instance().getDescriptionFor(blockID);
  if(d!=null)
    {
    return d.getIconFor(index);
    }
  return super.getIcon(side, meta);
  }

@Override
public boolean onBlockClicked(World world, int posX, int posY, int posZ, EntityPlayer player, int sideHit, float hitVecX, float hitVecY,    float hitVecZ)
  {
  if(world.isRemote){return true;}
  TEAWCrafting te = (TEAWCrafting)world.getBlockTileEntity(posX, posY, posZ);
  te.onBlockClicked(player);
  return true;
  }

}
