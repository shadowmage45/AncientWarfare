package shadowmage.ancient_warfare.common.utils;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.item.CreativeTabAW;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockContainerSimpleSided extends BlockContainer
{


private Random RNG = new Random();
public BlockContainerSimpleSided(int par1, Material par2Material)
  {
  super(par1, par2Material); 
  this.setTextureFile("/shadowmage/ancient_warfare/resources/block/blocks.png");
  this.isDefaultTexture = false;
  this.blockIndexInTexture = 0;
  this.setCreativeTab(CreativeTabAW.instance());
  this.setHardness(3.f);
  this.setBlockName("abstractBlock1");
  }

/**called by renderStandardBlock**/
@SideOnly(Side.CLIENT)
/**
 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
 */
public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
  if (par5 == 1)//up
    {
    return this.blockIndexInTexture + 2;//return top
    }
  else if (par5 == 0)//down
    {
    return 3;//return bottom
    }
  else
    {
    int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
    if(par5==var6)
      {
      return this.blockIndexInTexture;//return front
      }
    }
  return this.blockIndexInTexture + 1;//return side texture
  }

/**
 * Returns the block texture based on the side being looked at.  Args: side
 * Called by RenderBlocks.renderBlockAsItem
 */
public int getBlockTextureFromSide(int par1)
  {
  if(par1==0){return 3;}
  else if(par1==1){return this.blockIndexInTexture+2;}
  else if(par1==4){return this.blockIndexInTexture;}  
  return this.blockIndexInTexture+1; 
  }

/**
 * equivalent of onBlockActivated, used to activate a TE/open a gui/toggle a lever/etc
 * @param world
 * @param posX
 * @param posY
 * @param posZ
 * @param par5EntityPlayer
 * @param par6
 * @param par7
 * @param par8
 * @param par9
 * @return
 */
public abstract boolean onBlockClicked(World world, int posX, int posY, int posZ, EntityPlayer player, int sideHit, float hitVecX, float hitVecY, float hitVecZ);

/**
 * if this block has persistent inventory, return it here to be dropped on destruction
 * @param world
 * @param x
 * @param y
 * @param z
 * @param par5
 * @param par6
 * @return
 */
public abstract IInventory[] getInventoryToDropOnBreak(World world, int x, int y, int z, int par5, int par6);

@Override
public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int sideHit, float hitVecX, float hitVecY, float hitVecZ)
  {  
  return this.onBlockClicked(world, posX, posY, posZ, player, sideHit, hitVecX, hitVecY, hitVecZ);
  }

/**
 * Called when the block is placed in the world.
 */
@Override
public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
  {
  int blockFacing = 0;
  if(par5EntityLiving!=null)
    {
    blockFacing = BlockTools.getBlockFacingMetaFromPlayerYaw(par5EntityLiving.rotationYaw);
    }  
  par1World.setBlockMetadataWithNotify(x, y, z, blockFacing); 
  }

@Override
public boolean canProvidePower()
  { 
  return false;
  }

/**
 * Called whenever the block is added into the world. Args: world, x, y, z
 */
@Override
public void onBlockAdded(World par1World, int par2, int par3, int par4)
  {
  this.setDefaultDirection(par1World, par2, par3, par4);
  super.onBlockAdded(par1World, par2, par3, par4);  
  }

/**
 * set a blocks direction
 */
private void setDefaultDirection(World par1World, int par2, int par3, int par4)
  {
  if (!par1World.isRemote)
    {
    int var5 = par1World.getBlockId(par2, par3, par4 - 1);
    int var6 = par1World.getBlockId(par2, par3, par4 + 1);
    int var7 = par1World.getBlockId(par2 - 1, par3, par4);
    int var8 = par1World.getBlockId(par2 + 1, par3, par4);
    byte var9 = 3;
    if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6])
      {
      var9 = 3;
      }
    if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5])
      {
      var9 = 2;
      }
    if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8])
      {
      var9 = 5;
      }
    if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7])
      {
      var9 = 4;
      }
    par1World.setBlockMetadataWithNotify(par2, par3, par4, var9);
    }
  }

/**
 * ejects contained items into the world, and notifies neighbours of an update, as appropriate
 */
@Override
public void breakBlock(World world, int x, int y, int z, int par5, int par6)
  {
  IInventory[] inventoriesToDrop = this.getInventoryToDropOnBreak(world,x,y,z,par5,par6);
  if(inventoriesToDrop!=null && inventoriesToDrop.length>0)
    {
    for(IInventory inv : inventoriesToDrop)
      {
      this.dropItems(world, x, y, z, par5, par6, inv);
      }
    }   
  super.breakBlock(world, x, y, z, par5, par6);
  }

public void dropItems(World world, int x, int y, int z, int par5, int par6, IInventory inventory)
  {
  if (inventory != null)
    {
    for (int slotIndex = 0; slotIndex < inventory.getSizeInventory(); ++slotIndex)
      {
      ItemStack currentStack = inventory.getStackInSlot(slotIndex);

      if (currentStack != null)
        {
        float xOff = this.RNG.nextFloat() * 0.8F + 0.1F;
        float yOff = this.RNG.nextFloat() * 0.8F + 0.1F;
        float zOff = this.RNG.nextFloat() * 0.8F + 0.1F;

        while (currentStack.stackSize > 0)
          {
          int randomDropQty = this.RNG.nextInt(21) + 10;

          if (randomDropQty > currentStack.stackSize)
            {
            randomDropQty = currentStack.stackSize;
            }
          currentStack.stackSize -= randomDropQty;
          EntityItem var14 = new EntityItem(world, (double)((float)x + xOff), (double)((float)y + yOff), (double)((float)z + zOff), new ItemStack(currentStack.itemID, randomDropQty, currentStack.getItemDamage()));

          if (currentStack.hasTagCompound())
            {
            var14.func_92014_d().setTagCompound((NBTTagCompound)currentStack.getTagCompound().copy());
            }
          float var15 = 0.05F;
          var14.motionX = (double)((float)this.RNG.nextGaussian() * var15);
          var14.motionY = (double)((float)this.RNG.nextGaussian() * var15 + 0.2F);
          var14.motionZ = (double)((float)this.RNG.nextGaussian() * var15);
          world.spawnEntityInWorld(var14);
          }
        }
      }
    }
  }

public TileEntity createNewTileEntity(World world)
  {
  return getNewTileEntity(world, 0);
  }

public abstract TileEntity getNewTileEntity(World world, int meta);

@Override
public TileEntity createTileEntity(World world, int meta)
  {
  return getNewTileEntity(world, meta);
  }
}
