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
package shadowmage.ancient_warfare.common.crafting;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.registry.DescriptionRegistry;
import shadowmage.ancient_framework.common.registry.entry.Description;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.block.AWBlockContainer;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.item.AWItemBlockBase;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import cpw.mods.fml.common.registry.GameRegistry;

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
  d.setName("block.multi.crafting.0", 0);
  d.addDisplayStack(new ItemStack(this,1,0));
  d.setName("block.multi.crafting.1", 1);
  d.addDisplayStack(new ItemStack(this,1,1));
  d.setName("block.multi.crafting.2", 2);
  d.addDisplayStack(new ItemStack(this,1,2));
  d.setName("block.multi.crafting.3", 3);
  d.addDisplayStack(new ItemStack(this,1,3));  
  d.setName("block.multi.crafting.4", 4);
  d.addDisplayStack(new ItemStack(this,1,4)); 
  d.setName("block.multi.crafting.5", 5);
  d.addDisplayStack(new ItemStack(this,1,5));  
  d.setName("block.multi.crafting.6", 6);
  d.addDisplayStack(new ItemStack(this,1,6));
  d.setName("block.multi.crafting.7", 7);
  d.addDisplayStack(new ItemStack(this,1,7));
  
  d.setIconTexture(baseTexDir+"researchBlockBottom", 0);
  d.setIconTexture(baseTexDir+"researchBlockTop", 1);
  d.setIconTexture(baseTexDir+"researchBlockFront", 2);
  d.setIconTexture(baseTexDir+"researchBlockBack", 3);
  d.setIconTexture(baseTexDir+"researchBlockLeft", 4);
  d.setIconTexture(baseTexDir+"researchBlockRight", 5);  
  
  GameRegistry.registerTileEntity(TEAWResearch.class, "Research Center");
  GameRegistry.registerTileEntity(TEAWCivicCraft.class, "Engineering Station");
  GameRegistry.registerTileEntity(TEAWStructureCraft.class, "Drafting Station");
  GameRegistry.registerTileEntity(TEAWVehicleCraft.class, "Vehicle Engineering Station");
  GameRegistry.registerTileEntity(TEAWAmmoCraft.class, "Ammo Production Station");
  GameRegistry.registerTileEntity(TEAWNpcCraft.class, "NPC Recruiting Center");
  GameRegistry.registerTileEntity(TEAWAlchemy.class, "Alchemy Station");
  GameRegistry.registerTileEntity(TEAWAutoCrafting.class, "AutoCrafting Station");
  }

@Override
public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
  {
  if(entity==null || entity.worldObj.isRemote)
    {
    return;
    }
  int face = BlockTools.getBlockFacingMetaFromPlayerYaw(entity.rotationYaw);
  TEAWCrafting te = (TEAWCrafting)world.getBlockTileEntity(x, y, z);
  te.setOrientation(face);
  if(entity instanceof EntityPlayer)
    {
    te.teamNum = TeamTracker.instance().getTeamForPlayer((EntityPlayer)entity);
    }
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  switch(meta)
  {
  case 0:
  return new TEAWResearch();
  case 1:
  return new TEAWCivicCraft();
  case 2:
  return new TEAWStructureCraft();
  case 3:
  return new TEAWVehicleCraft();
  case 4:
  return new TEAWAmmoCraft();
  case 5:
  return new TEAWNpcCraft();
  case 6:
  return new TEAWAlchemy();
  case 7:
  return new TEAWAutoCrafting();
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
  int index = side;
  Description d = DescriptionRegistry.instance().getDescriptionFor(blockID);
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

@Override
public int idDropped(int par1, Random par2Random, int par3)
  {
  return BlockLoader.crafting.blockID;
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
  return new ItemStack(BlockLoader.crafting,1,par1);
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
  return new ItemStack(BlockLoader.crafting,1,world.getBlockMetadata(x, y, z));
  }
}
