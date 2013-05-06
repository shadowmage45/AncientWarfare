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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.interfaces.IScannerItem;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class ItemCivicPlacer extends AWItemClickable implements IScannerItem
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemCivicPlacer(int itemID)
  {
  super(itemID, true);
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  super.addInformation(stack, player, list, par4);
  if(stack!=null)
    {
    if(stack.hasTagCompound() && stack.getTagCompound().hasKey("civicInfo"))
      {
      NBTTagCompound tag = stack.getTagCompound().getCompoundTag("civicInfo");     
      int type = stack.getItemDamage();
      Civic civ = CivicRegistry.instance().getCivicFor(type);        
      if(civ!=null)          
        {
        if(civ.isDepository())
          {
          list.add("Block-Only: right click to place");
          }
        else if(tag.hasKey("pos2"))
          {
          list.add("Has both bounds positions set");
          list.add("Click to Place Civic Block");
          }
        else if(tag.hasKey("pos1"))
          {
          list.add("Has first bounds position set");
          list.add("Click to Set Second Position");
          }
        else
          {
          list.add("Click to Set First Position");
          }
        }
      else
        {
        list.add("Invalid or Corrupt Item NBT");
        } 
      }
    else
      {
      list.add("Invalid or Corrupt Item NBT");
      }
    }  
  }

@Override
public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
  {
  par3List.addAll(CivicRegistry.instance().getDisplayStacks());
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote)
    {
    return true;
    }
  if(hit!=null && stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey("civicInfo"))
    {
    Civic civ = CivicRegistry.instance().getCivicFor(stack.getItemDamage());
    NBTTagCompound tag = stack.getTagCompound().getCompoundTag("civicInfo");
    if(civ.isDepository())
      {
      hit.offsetForMCSide(side);
      placeCivicBlock(world, hit, stack.getItemDamage());
      ItemStack item = player.getCurrentEquippedItem();
      if(item!=null && item.itemID == ItemLoader.civicPlacer.itemID)
        {
        if(!player.capabilities.isCreativeMode)
          {
          stack.stackSize--;
          if(stack.stackSize<=0)
            {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
          }             
        }
      }
    else if(tag.hasKey("pos2") && tag.hasKey("pos1"))
      {
      BlockPosition pos1 = new BlockPosition(tag.getCompoundTag("pos1"));
      BlockPosition pos2 = new BlockPosition(tag.getCompoundTag("pos2"));
      BlockPosition min = BlockTools.getMin(pos1, pos2);
      BlockPosition max = BlockTools.getMax(pos1, pos2);
      hit.offsetForMCSide(side);
      boolean placeBlock = true;
      if(hit.x >= min.x && hit.x <= max.x && hit.y >= min.y && hit.y <=max.y && hit.z>=min.z && hit.z <=max.z)
        {//if block is inside bounds, reject
        placeBlock = false;
        }
      else if(hit.x<min.x-1 || hit.x>max.x+1 || hit.y<min.y-1 || hit.y>max.y+1 || hit.z< min.z-1 || hit.z>max.z+1)
        {//if not adjacent to work bounds, reject
        placeBlock = false;
        }
      if(placeBlock)
        {  
        placeCivicBlock(world, hit, pos1, pos2,  stack.getItemDamage());
        ItemStack item = player.getCurrentEquippedItem();
        if(item!=null && item.itemID == ItemLoader.civicPlacer.itemID)
          {
          if(!player.capabilities.isCreativeMode)
            {
            stack.stackSize--;
            if(stack.stackSize<=0)
              {
              player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
              }
            }
          else
            {
            NBTTagCompound newtag = new NBTTagCompound();
            stack.setTagInfo("civicInfo", newtag);
            player.openContainer.detectAndSendChanges();
            }        
          }
        }
      else
        {
        player.addChatMessage("Please choose a position directly adjacent to the work bounds!");
        }      
      }
    else if(tag.hasKey("pos1"))
      {
      BlockPosition pos1 = new BlockPosition(tag.getCompoundTag("pos1"));
      
      if(civ!=null)
        {
        int maxWidth = civ.getMaxWorkSizeWidth();
        int maxHeight = civ.getMaxWorkSizeHeight();
        int maxArea = civ.getMaxWorkAreaCube();
        if(player.isSneaking())
          {
          hit.offsetForMCSide(side);
          }
        int width1 = Math.abs(hit.z-pos1.z)+1;
        int width2 = Math.abs(hit.x-pos1.x)+1;
        int height = Math.abs(hit.y-pos1.y)+1;
        int totalArea = width1*width2*height;
        if(width1 <= maxWidth && width2<=maxWidth && height <=maxHeight && totalArea <= maxArea)//
          {          
          tag.setCompoundTag("pos2", hit.writeToNBT(new NBTTagCompound()));
          }
        else
          {
          player.addChatMessage("Too large of an area, try a smaller area!");
          if(width1 > maxWidth)
            {
            player.addChatMessage("Z axis is too large by: "+(width1-maxWidth)+" blocks");
            }
          if(width2 > maxWidth)
            {
            player.addChatMessage("X axis is too large by: "+(width2-maxWidth)+" blocks");
            }
          if(height > maxHeight)
            {
            player.addChatMessage("Y axis is too large by: "+(height-maxHeight)+" blocks");
            }
          if(totalArea > maxArea)
            {
            player.addChatMessage("Cubed area is too large by: "+(totalArea-maxArea)+" blocks");
            }
          }        
        }
      }
    else
      {      
      if(player.isSneaking())
        {
        hit.offsetForMCSide(side);
        }
      tag.setCompoundTag("pos1", hit.writeToNBT(new NBTTagCompound()));
      }
    }
  return true;
  }

public void placeCivicBlock(World world,  BlockPosition hit, BlockPosition pos1, BlockPosition pos2, int type)
  {
  if(hit==null || pos1==null || pos2==null || world==null)
    {
    return;
    }
  BlockPosition min = BlockTools.getMin(pos1, pos2);
  BlockPosition max = BlockTools.getMax(pos1, pos2);
  CivicRegistry.instance().setCivicBlock(world, hit.x, hit.y, hit.z, type);
  TECivic te = (TECivic) world.getBlockTileEntity(hit.x, hit.y, hit.z);
  te.setBounds(min.x, min.y, min.z, max.x, max.y, max.z);
  world.markBlockForUpdate(hit.x, hit.y, hit.z);
  }

/**
 * place a no-bounds civic (depository...)
 * @param world
 * @param hit
 * @param type
 */
public void placeCivicBlock(World world, BlockPosition hit, int type)
  {
  CivicRegistry.instance().setCivicBlock(world, hit.x, hit.y, hit.z, type);
  TECivic te = (TECivic) world.getBlockTileEntity(hit.x, hit.y, hit.z);
  te.setBounds(hit.x, hit.y, hit.z, hit.x, hit.y, hit.z);
  world.markBlockForUpdate(hit.x, hit.y, hit.z);
  }

@Override
public BlockPosition getScanPos1(ItemStack stack)
  {
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("civicInfo"))
    {
    NBTTagCompound tag = stack.getTagCompound().getCompoundTag("civicInfo");
    if(tag.hasKey("pos1"))
      {
      return new BlockPosition(tag.getCompoundTag("pos1"));
      }
    }
  return null;
  }

@Override
public BlockPosition getScanPos2(ItemStack stack)
  {
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("civicInfo"))
    {
    NBTTagCompound tag = stack.getTagCompound().getCompoundTag("civicInfo");
    if(tag.hasKey("pos2"))
      {
      return new BlockPosition(tag.getCompoundTag("pos2"));
      }
    }
  return null;
  }

//needs addInfo
//NBT will have a Pos1 and Pos2 (BlockPosition)
//NBT will have type and rank (store type as dmg)
//might want left-click to set positions normal, shift-left click to set position w/offset, shift-right click to clear current pos1/2

}
