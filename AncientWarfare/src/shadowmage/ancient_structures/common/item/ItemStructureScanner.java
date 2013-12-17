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
package shadowmage.ancient_structures.common.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.item.AWItemClickable;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_structures.common.structures.data.ProcessedStructure;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.scan.TemplateScanner;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemStructureScanner extends AWItemClickable
{

public static StructureTemplate currentTemplate = null;

public ItemStructureScanner(int itemID)
  {
  super(itemID);
  this.setMaxStackSize(1);  
  this.hasLeftClick = true;
  this.setCreativeTab(AWStructuresItemLoader.structureTab);
  AWLog.logDebug("set creative tab for structure scanner to: "+AWStructuresItemLoader.structureTab);
  }

/**
 * client-side structure setting container, do not access from server-methods!!
 */
ItemStructureSettings viewSettings = new ItemStructureSettings();
@Override
public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List list, boolean par4)
  {
  super.addInformation(par1ItemStack, par2EntityPlayer, list, par4);  
  if(par1ItemStack!=null)
    {
    viewSettings.getSettingsFor(par1ItemStack, viewSettings);
    NBTTagCompound tag;
    if(par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("structData"))
      {
      tag = par1ItemStack.getTagCompound().getCompoundTag("structData");
      }
    else
      {
      tag = new NBTTagCompound();
      }
    if(viewSettings.hasPos1() && viewSettings.hasPos2() && viewSettings.hasBuildKey())
      {
      list.add("Right Click: Scan and Process (4/4)");
      list.add("(Shift)Right Click: Cacnel/clear");
      }        
    else if(!viewSettings.hasPos1())
      {
      list.add("Left Click: Set first bound (1/4)");
      list.add("Hold shift to offset for side hit");
      list.add("(Shift)Right Click: Cacnel/clear");
      }
    else if(!viewSettings.hasPos2())
      {
      list.add("Left Click: Set second bound (2/4)");
      list.add("Hold shift to offset for side hit");
      list.add("(Shift)Right Click: Cacnel/clear");
      }
    else if(!viewSettings.hasBuildKey())
      {
      list.add("Left Click: Set build key and");
      list.add("    direction (3/4)");
      list.add("Hold shift to offset for side hit");
      list.add("(Shift)Right Click: Cacnel/clear");
      }    
    }  
  }

@Override
public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
  {
  return false;
  }

public boolean scanStructure(World world, EntityPlayer player, BlockPosition pos1, BlockPosition pos2, BlockPosition key, int face)
  {
  BlockPosition min = BlockTools.getMin(pos1, pos2);
  BlockPosition max = BlockTools.getMax(pos1, pos2);
  TemplateScanner scanner = new TemplateScanner();
  int turns = face==0 ? 2 : face==1 ? 1 : face==2 ? 0 : face==3 ? 3 : 0; //because for some reason my mod math was off?  
  StructureTemplate template = scanner.scan(world, min, max, key, turns); 
  currentTemplate = template;
  return true;
  }

/**
 * server-side structure setting container, do not access from client-methods!!
 */
ItemStructureSettings scanSettings = new ItemStructureSettings();

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack,  BlockPosition hit, int side)
  {
  if(world.isRemote)
    {
    return true;
    }  
  if(!MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(player.getEntityName()))
    {
    return true;
    }
  scanSettings.getSettingsFor(stack, scanSettings);
  if(player.isSneaking())
    {
    scanSettings.clearSettings();
    scanSettings.setSettingsFor(stack, scanSettings);
    }
  else if(scanSettings.hasPos1() && scanSettings.hasPos2() && scanSettings.hasBuildKey())
    {
    BlockPosition pos1 = scanSettings.pos1;
    BlockPosition pos2 = scanSettings.pos2;
    BlockPosition key = scanSettings.key;
    int face = scanSettings.buildFace;
    if(player.getDistance(key.x+0.5d, key.y, key.z+0.5d) > 10)
      {
      player.addChatMessage("You are too far away to scan that building, move closer to chosen build-key position");
      return false;
      }
    player.addChatMessage("Initiating Scan and clearing Position Data (Step 4/4)");
    scanStructure(world, player, pos1, pos2, key, face);
    scanSettings.clearSettings();
    scanSettings.setSettingsFor(stack, scanSettings);
    return true;
    } 
  return true;
  }

@SideOnly(Side.CLIENT)
@Override
public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
  {  
  if(description!=null)
    {
    par3List.addAll(description.getDisplayStackCache());
    }
  else
    {
    super.getSubItems(par1, par2CreativeTabs, par3List);
    } 
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote)
    {
    return true;
    }  
  if(!MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(player.getEntityName()))
    {
    return true;
    }
  NBTTagCompound tag;
  if(hit!=null && player.isSneaking())
    {
    hit.offsetForMCSide(side);
    }
  scanSettings.getSettingsFor(stack, scanSettings);
  if(scanSettings.hasPos1() && scanSettings.hasPos2() && scanSettings.hasBuildKey())
    {
    player.addChatMessage("Right Click to Process");
    }
  else if(!scanSettings.hasPos1())
    {
    scanSettings.setPos1(hit.x, hit.y, hit.z);
    player.addChatMessage("Setting Scan Position 1 (Step 1/4)");
    }
  else if(!scanSettings.hasPos2())
    {
    scanSettings.setPos2(hit.x, hit.y, hit.z);
    player.addChatMessage("Setting Scan Position 2 (Step 2/4)");
    }
  else if(!scanSettings.hasBuildKey())
    {
    scanSettings.setBuildKey(hit.x, hit.y, hit.z, BlockTools.getPlayerFacingFromYaw(player.rotationYaw));
    player.addChatMessage("Setting Scan Build Position and Facing (Step 3/4)");
    }
  scanSettings.setSettingsFor(stack, scanSettings);
  return true;
  }

}
