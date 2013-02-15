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
package shadowmage.ancient_warfare.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.structures.build.BuilderInstant;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureBB;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class ItemBuilderInstant extends ItemBuilderBase
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBuilderInstant(int itemID)
  {
  super(itemID);
  this.setIconIndex(3);
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

private NBTTagCompound getDefaultTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", "");
  tag.setInteger("veh", -2);
  tag.setInteger("npc", -2);
  tag.setInteger("gate", -2);
  tag.setInteger("team", -2);
  return tag;
  }

private void clearStructureData(ItemStack stack)
  {
  stack.setTagInfo("structData", getDefaultTag());
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote)
    {
    return true;
    }  
  if(isShiftClick(player))
    {
    openGUI(player);
    return true;
    }
  if(hit==null)
    {
    return true;
    }
  hit = BlockTools.offsetForSide(hit, side);   
  NBTTagCompound tag;
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("structData"))
    {
    tag = stack.getTagCompound().getCompoundTag("structData");
    }
  else
    {
    tag = new NBTTagCompound();
    }
  if(tag.hasKey("name") && hit !=null)
    {    
    ProcessedStructure struct = StructureManager.instance().getStructureServer(tag.getString("name"));
    if(struct==null)
      {
      Config.logError("Structure Manager returned NULL structure to build for name : "+tag.getString("name"));      
      tag = new NBTTagCompound();
      stack.setTagInfo("structData", tag);
      return true;
      }
    if(!this.attemptConstruction(world, struct, hit, BlockTools.getPlayerFacingFromYaw(player.rotationYaw)))
      {
      player.addChatMessage("Structure is currently locked for editing!!");
      }
    }
  return true;
  }

//protected void attemptConstruction(World world, ProcessedStructure struct, int face, BlockPosition hit)
//  {
//  
//  BuilderInstant builder = new BuilderInstant(world, struct, face, hit);
//  builder.startConstruction();
//  } 

@Override
public StructureClientInfo getStructureForStack(ItemStack stack)
  {
  if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("structData") || !stack.getTagCompound().getCompoundTag("structData").hasKey("name"))
    {
    return null;    
    }
  return StructureManager.instance().getClientStructure(stack.getTagCompound().getCompoundTag("structData").getString("name"));  
  }

protected NBTTagCompound getStructData(ItemStack stack)
  {
  NBTTagCompound tag;
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("structData"))
    {    
    tag = stack.getTagCompound().getCompoundTag("structData");
    }
  else
    {
    tag = new NBTTagCompound();
    stack.setTagInfo("structData", tag);
    }
  return tag;
  }

@Override
public boolean renderBuilderBlockBB()
  {
  return false;
  }


@Override
public boolean attemptConstruction(World world, ProcessedStructure struct,   BlockPosition hit, int facing)
  {
  if(!struct.isLocked())
    {
    
    //DEBUG...
    StructureBB levelingBB = struct.getLevelingBoundingBox(hit, facing, struct.xOffset, struct.verticalOffset, struct.zOffset, struct.xSize, struct.ySize, struct.zSize, struct.getLevelingMax(), struct.getLevelingBuffer());
    if(!struct.isValidLevelingTarget(world, levelingBB, struct.validTargetBlocks, struct.getLevelingBuffer()))
      {
      Config.logDebug("invalid leveling target!!");
      }
    //END DEBUG....   
    
    BuilderInstant builder = new BuilderInstant(world, struct, facing, hit);
    builder.startConstruction();
    return true;
    }
  return false;
  }


}
