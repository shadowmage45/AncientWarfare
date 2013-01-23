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
import shadowmage.ancient_warfare.common.aw_core.block.BlockLoader;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.item.AWItemClickable;
import shadowmage.ancient_warfare.common.aw_core.network.GUIHandler;
import shadowmage.ancient_warfare.common.aw_structure.block.TEBuilder;
import shadowmage.ancient_warfare.common.aw_structure.build.BuilderTicked;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;

public class ItemStructureBuilderCreativeTicked extends AWItemClickable
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemStructureBuilderCreativeTicked(int itemID)
  {
  super(itemID, false);
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
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
  if(player.isSneaking())
    {
    openGUI(player);
    return true;
    }
  if(hit==null)
    {
    return true;
    }  
  hit = BlockTools.offsetForSide(hit, side);
  if(!tag.hasKey("buildBlock"))
    {    
    player.addChatMessage("Setting Builder Block Position");
    tag.setCompoundTag("buildBlock", hit.writeToNBT(new NBTTagCompound()));
    return true;
    }  
  if(tag.hasKey("name") && tag.hasKey("buildBlock"))
    {
    String name = tag.getString("name");
    ProcessedStructure struct = StructureManager.instance().getStructure(name);
    if(struct==null)
      {
      Config.logError("Structure Manager returned NULL structure to build for name : "+tag.getString("name"));      
      return true;
      }
    BlockPosition buildBlock = new BlockPosition(tag.getCompoundTag("buildBlock"));
    
    //TODO IMPORTANT validate that builder block would not be inside bounds of structure
    
    world.setBlockAndMetadata(buildBlock.x, buildBlock.y, buildBlock.z, BlockLoader.instance().builder.blockID, BlockTools.getBlockFacingMetaFromPlayerYaw(player.rotationYaw));
    
    TEBuilder te = (TEBuilder) world.getBlockTileEntity(buildBlock.x, buildBlock.y, buildBlock.z);
    if(te!=null)
      {
      BuilderTicked builder = new BuilderTicked(struct, BlockTools.getPlayerFacingFromYaw(player.rotationYaw), hit);
      builder.setWorld(world);
      te.setBuilder(builder);
      }
    tag = new NBTTagCompound();
    tag.setString("name", name);
    stack.setTagInfo("structData", tag);
    }  
  return true;
  }

public void openGUI(EntityPlayer player)
  {
  GUIHandler.instance().openGUI(GUIHandler.STRUCTURE_SELECT, player, player.worldObj, 0, 0, 0);
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
    if(tag.hasKey("buildBlock"))
      {
      par3List.add("Builder Block Position Set"); 
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

@Override
public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
  {
  
  return false;
  }



}
