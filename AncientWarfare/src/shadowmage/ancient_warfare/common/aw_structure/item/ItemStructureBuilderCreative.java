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

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.item.AWItemBase;
import shadowmage.ancient_warfare.common.aw_core.network.GUIHandler;
import shadowmage.ancient_warfare.common.aw_structure.build.BuilderInstant;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;

public class ItemStructureBuilderCreative extends AWItemBase
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemStructureBuilderCreative(int itemID)
  {
  super(itemID, false);
  this.setIconIndex(3);
  }

public void openGUI(EntityPlayer player)
  {
  GUIHandler.instance().openGUI(GUIHandler.STRUCTURE_SELECT, player, player.worldObj, 0, 0, 0);
  }

/**
 * only called when something is actually HIT with the click
 */
@Override
public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOff, float yOff, float zOff)
  {
  return onUsed(world, player, stack, new BlockPosition(x,y,z));
  }

@Override
public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
  {
  super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);  
  if(par1ItemStack!=null)
    {
    NBTTagCompound tag;
    int test;
    if(par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("structData"))
      {
      tag = par1ItemStack.getTagCompound().getCompoundTag("structData");
      }
    else
      {
      tag = new NBTTagCompound();
      }
    if(tag.hasKey("name"))
      {
      par3List.add("Structure Name: "+ tag.getString("name"));
      }   
    else
      {
      par3List.add("Structure Name: "+ "No Selection");
      }
    if(tag.hasKey("team"))
      {
      test = tag.getInteger("team");
      if(test>=0)
        {
        par3List.add("Forced Team Num: "+test);
        }      
      }
    if(tag.hasKey("veh"))
      {
      test = tag.getInteger("veh");
      if(test>=-1)
        {
        par3List.add("Forced Vehicle Type: "+test);
        }      
      }
    if(tag.hasKey("npc"))
      {
      test = tag.getInteger("npc");
      if(test>-1)
        {
        par3List.add("Forced NPC Type: "+test);
        }      
      }
    if(tag.hasKey("gate"))
      {
      test = tag.getInteger("gate");
      if(test>-1)
        {
        par3List.add("Forced Gate Type: "+test);
        }  
      }
    }  
  }

/**
 * called when nothing is hit with right-click
 */
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

public boolean onUsed(World world, EntityPlayer player, ItemStack stack)
  {
  BlockPosition hit = BlockTools.getBlockClickedOn(player, world, false);
  return onUsed(world, player, stack, hit);
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
  if(player.isSneaking())
    {
    openGUI(player);
    return true;
    }
  if(hit==null)
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
  if(tag.hasKey("name"))
    {
    ProcessedStructure struct = StructureManager.instance().getStructure(tag.getString("name"));
    if(struct==null)
      {
      Config.logError("Structure Manager returned NULL structure to build for name : "+tag.getString("name"));
      return true;
      }
    BuilderInstant builder = new BuilderInstant(world, struct, BlockTools.getPlayerFacingFromYaw(player.rotationYaw), hit);

    builder.startConstruction();
    }
  return true;
  }


}
