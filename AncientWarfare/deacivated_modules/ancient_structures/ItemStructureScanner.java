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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.interfaces.IScannerItem;


public class ItemStructureScanner extends AWItemClickable implements IScannerItem
{


public static Map<EntityPlayer, ProcessedStructure> scannedStructures = new HashMap<EntityPlayer, ProcessedStructure>();

/**
 * @param itemID
 *  
 */
public ItemStructureScanner(int itemID)
  {
  super(itemID, false);
  this.setMaxStackSize(1);  
  this.hasLeftClick = true;
  }

@Override
public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List list, boolean par4)
  {
  super.addInformation(par1ItemStack, par2EntityPlayer, list, par4);  
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
      list.add("Right Click: Scan and Process (4/4)");
      list.add("(Shift)Right Click: Cacnel/clear");
      }        
    else if(!tag.hasKey("pos1"))
      {
      list.add("Left Click: Set first bound (1/4)");
      list.add("Hold shift to offset for side hit");
      list.add("(Shift)Right Click: Cacnel/clear");
      }
    else if(!tag.hasKey("pos2"))
      {
      list.add("Left Click: Set second bound (2/4)");
      list.add("Hold shift to offset for side hit");
      list.add("(Shift)Right Click: Cacnel/clear");
      }
    else if(!tag.hasKey("buildKey"))
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
  this.scannedStructures.put(player, scanData.convertToProcessedStructure());
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

public static boolean isScannerItem(int id)
  {
  return id == ItemLoaderCore.structureScanner.itemID;
  }

public static BlockPosition getPos1(ItemStack stack)
  {
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("structData") && stack.getTagCompound().getCompoundTag("structData").hasKey("pos1"))
    {
    return new BlockPosition(stack.getTagCompound().getCompoundTag("structData").getCompoundTag("pos1"));
    }
  return null;
  }

public static BlockPosition getPos2(ItemStack stack)
  {
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("structData") && stack.getTagCompound().getCompoundTag("structData").hasKey("pos2"))
    {
    return new BlockPosition(stack.getTagCompound().getCompoundTag("structData").getCompoundTag("pos2"));
    }
  return null;
  }

@Override
public BlockPosition getScanPos1(ItemStack stack)
  {
  return getPos1(stack);
  }

@Override
public BlockPosition getScanPos2(ItemStack stack)
  {
  return getPos2(stack);
  }

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
    if(player.getDistance(key.x+0.5d, key.y, key.z+0.5d) > 10)
      {
      player.addChatMessage("You are too far away to scan that building, move closer to chosen build-key position");
      return false;
      }
    int face = tag.getCompoundTag("buildKey").getInteger("face");
    player.addChatMessage("Initiating Scan and clearing Position Data (Step 4/4)");
    scanStructure(world, player,pos1, pos2, key, face);
    return true;
    } 
  else if(isShiftClick(player))
    {
    tag = new NBTTagCompound();  
    stack.setTagInfo("structData", tag);
    }
  return true;
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
  if(hit!=null && isShiftClick(player))
    {
    hit.offsetForMCSide(side);
    }
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
    player.addChatMessage("Right Click to Process");
    }        
  else if(!tag.hasKey("pos1"))
    {
    tag.setCompoundTag("pos1", hit.writeToNBT(new NBTTagCompound()));
    player.addChatMessage("Setting Scan Position 1 (Step 1/4)");
    }
  else if(!tag.hasKey("pos2"))
    {
    tag.setCompoundTag("pos2", hit.writeToNBT(new NBTTagCompound()));
    player.addChatMessage("Setting Scan Position 2 (Step 2/4)");
    }
  else if(!tag.hasKey("buildKey"))
    {
    tag.setCompoundTag("buildKey", hit.writeToNBT(new NBTTagCompound()));
    tag.getCompoundTag("buildKey").setInteger("face", BlockTools.getPlayerFacingFromYaw(player.rotationYaw));
    player.addChatMessage("Setting Scan Build Position and Facing (Step 3/4)");
    }
  stack.setTagInfo("structData", tag);
  return true;
  }

}
