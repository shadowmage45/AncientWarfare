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
package shadowmage.ancient_warfare.common.aw_structure.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.item.AWItemBase;
import shadowmage.ancient_warfare.common.aw_core.network.GUIHandler;
import shadowmage.ancient_warfare.common.aw_structure.data.ScannedStructureData;


public class ItemStructureScanner extends AWItemBase
{


public static Map<EntityPlayer, ScannedStructureData> scannedStructures = new HashMap<EntityPlayer, ScannedStructureData>();

/**
 * @param itemID
 *  
 */
public ItemStructureScanner(int itemID)
  {
  super(itemID, false);
  this.setMaxStackSize(1);  
  this.setIconIndex(0);
  }

@Override
public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOff, float yOff, float zOff)
  {
  BlockPosition hitPos = new BlockPosition(x,y,z);
  if(player.isSneaking())
    {
    hitPos = BlockTools.offsetForSide(hitPos, side);
    }
  return onUsed(world, player, stack, hitPos);
      
  //return super.onItemUse(stack, player, world, x, y, z, side, xOff, yOff, zOff);
  }

@Override
public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
  {
  super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);  
  if(par1ItemStack!=null)
    {
    NBTTagCompound tag;
    if(par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("structData"))
      {
      tag = par1ItemStack.getTagCompound().getCompoundTag("structData");
      }
    else
      {
      tag = new NBTTagCompound();
      }
    if(tag.hasKey("pos1")&&tag.hasKey("pos2") && tag.hasKey("buildKey"))
      {
      par3List.add("ready to scan and process");
      }        
    else if(!tag.hasKey("pos1"))
      {
      par3List.add("item cleared");
      }
    else if(!tag.hasKey("pos2"))
      {
      par3List.add("pos1 set");
      }
    else if(!tag.hasKey("buildKey"))
      {
      par3List.add("pos2 set");
      }    
    }  
  }

@Override
public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,EntityPlayer par3EntityPlayer)
  {
  onUsed(par2World, par3EntityPlayer, par1ItemStack);
  return par1ItemStack;
  }

@Override
public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
  {
  return false;
  }

/**
 * Gets an icon index based on an item's damage value
 */
@Override
public int getIconFromDamage(int par1)
  {
  return this.iconIndex;
  }

public boolean onUsed(World world, EntityPlayer player, ItemStack stack)
  {
  BlockPosition hit = BlockTools.getBlockClickedOn(player, world, player.isSneaking());
  if(hit!=null)
    {
    return onUsed(world, player, stack, hit);
    }
  return false;
  }

/**
 * final onUsed call, passes information on to onActivated
 * @param world
 * @param player
 * @param stack
 * @param hit
 * @return
 */
public boolean onUsed(World world, EntityPlayer player, ItemStack stack, BlockPosition hit)
  {
  return onActivated(world, player, stack, hit);
  }

/**
 * the actual onActivated call, all rightclick/onUsed/onUse functions funnel through to here.
 * @param world
 * @param player
 * @param stack
 * @param hit
 * @return
 */
public boolean onActivated(World world, EntityPlayer player, ItemStack stack, BlockPosition hit)
  {
  if(world.isRemote)
    {
    return true;
    }  
  NBTTagCompound tag;
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("structData"))
    {
    tag = stack.getTagCompound().getCompoundTag("structData");
    }
  else
    {
    tag = new NBTTagCompound();
    }
  if(tag.hasKey("pos1")&&tag.hasKey("pos2") && tag.hasKey("buildKey"))
    {
    BlockPosition pos1 = new BlockPosition(tag.getCompoundTag("pos1"));
    BlockPosition pos2 = new BlockPosition(tag.getCompoundTag("pos2"));
    BlockPosition key = new BlockPosition(tag.getCompoundTag("buildKey"));
    int face = tag.getCompoundTag("buildKey").getInteger("face");
    player.addChatMessage("Initiating Scan and clearing Position Data");
    scanStructure(world, player,pos1, pos2, key, face);
    return true;
    //tag = new NBTTagCompound();//reset scan data in item with clean tag    
    }        
  else if(!tag.hasKey("pos1"))
    {
    tag.setCompoundTag("pos1", hit.writeToNBT(new NBTTagCompound()));
    player.addChatMessage("Setting Scan Position 1");
    }
  else if(!tag.hasKey("pos2"))
    {
    tag.setCompoundTag("pos2", hit.writeToNBT(new NBTTagCompound()));
    player.addChatMessage("Setting Scan Position 2");
    }
  else if(!tag.hasKey("buildKey"))
    {
    tag.setCompoundTag("buildKey", hit.writeToNBT(new NBTTagCompound()));
    tag.getCompoundTag("buildKey").setInteger("face", BlockTools.getPlayerFacingFromYaw(player.rotationYaw));
    player.addChatMessage("Setting Scan Build Position and Facing");
    }
  stack.setTagInfo("structData", tag);
  return true;
  }



/**
 * actually scan the structure.
 * @param world
 * @param player
 * @return
 */
public boolean scanStructure(World world, EntityPlayer player, BlockPosition pos1, BlockPosition pos2, BlockPosition key, int face)
  {
  key = BlockTools.offsetBuildKey(face, pos1, pos2, key);
  ScannedStructureData scanData = new ScannedStructureData(face, pos1, pos2, key);
  scanData.scan(world);     
  this.scannedStructures.put(player, scanData);
  GUIHandler.instance().openGUI(GUIHandler.STRUCTURE_SCANNER, player, world, 0, 0, 0);
  return true;
  }

public static ItemStack clearStructureData(ItemStack stack)
  {
  if(stack!=null && stack.hasTagCompound())
    {    
    stack.setTagInfo("structData", new NBTTagCompound());    
    } 
  return stack;
  }

}
