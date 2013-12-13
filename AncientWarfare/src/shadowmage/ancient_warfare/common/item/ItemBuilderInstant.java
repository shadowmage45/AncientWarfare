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
package shadowmage.ancient_warfare.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.manager.StructureManager;

public class ItemBuilderInstant extends ItemBuilderBase
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBuilderInstant(int itemID)
  {
  super(itemID);
  }

public void openGUI(EntityPlayer player)
  {
  GUIHandler.instance().openGUI(GUIHandler.STRUCTURE_SELECT, player, player.worldObj, 0, 0, 0);
  }

@Override
public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
  {
  super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4); 
  par3List.add("Right Click: Open GUI");
  par3List.add("(Shift)Right Click: Build Structure");
  if(par1ItemStack!=null)
    {
    NBTTagCompound tag;
    int test;
    boolean val;
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
    if(tag.hasKey("oteam"))
      {
      test = tag.getInteger("oteam");
      if(test>=-0)
        {
        par3List.add("Forced Team: "+test);
        }      
      else
        {
        par3List.add("Forced Team: N/A");
        }
      }
    if(tag.hasKey("sveh"))
      {
      val = tag.getBoolean("sveh");
      par3List.add("Spawn Vehicles: "+val);     
      }
    if(tag.hasKey("snpc"))
      {
      val = tag.getBoolean("snpc");
      par3List.add("Spawn Npcs: "+val);      
      }
    if(tag.hasKey("sgate"))
      {
      val = tag.getBoolean("sgate");
      par3List.add("Spawn Gates: "+val);      
      }
    }  
  }

private NBTTagCompound getDefaultTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", "");
  tag.setBoolean("sveh", true);
  tag.setBoolean("snpc", true);
  tag.setBoolean("sgate", true);
  tag.setInteger("oteam", -1);
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
  if(player.capabilities.isCreativeMode && !isShiftClick(player))
    {
    openGUI(player);
    }
  if(hit==null)
    {
    return true;
    }
  if(isShiftClick(player))
    {
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
      StructureBuildSettings settings = StructureBuildSettings.constructFromNBT(tag);
      ProcessedStructure struct = StructureManager.instance().getStructureServer(tag.getString("name"));
      if(struct==null)
        {
        Config.logError("Structure Manager returned NULL structure to build for name : "+tag.getString("name"));      
        tag = new NBTTagCompound();
        stack.setTagInfo("structData", tag);
        return true;
        }
      if(!this.attemptConstruction(world, struct, hit, BlockTools.getPlayerFacingFromYaw(player.rotationYaw), settings))
        {
        player.addChatMessage("Structure is currently locked for editing!!");
        }
      else
        {
        if(!player.capabilities.isCreativeMode)
          {
          ItemStack item = player.getHeldItem();
          if(item!=null)
            {
            item.stackSize--;
            if(item.stackSize<=0)
              {          
              player.setCurrentItemOrArmor(0, null);
              }
            }
          }
        }
      }
    }
  return true;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player,  ItemStack stack, BlockPosition hit, int side)
  {  
  return true;
  }

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
public boolean attemptConstruction(World world, ProcessedStructure struct,   BlockPosition hit, int facing, StructureBuildSettings settings)
  {
  if(!struct.isLocked())
    {
    BuilderInstant builder = new BuilderInstant(world, struct, facing, hit);
    builder.setOverrides(settings.teamOverride, settings.spawnVehicle, settings.spawnNpc, settings.spawnGate);
    builder.startConstruction();
    return true;
    }
  return false;
  }

}
