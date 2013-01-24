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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.client.aw_structure.render.BoundingBoxRender;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.item.AWItemClickable;
import shadowmage.ancient_warfare.common.aw_core.network.GUIHandler;
import shadowmage.ancient_warfare.common.aw_core.utils.IDPairCount;
import shadowmage.ancient_warfare.common.aw_core.utils.Pos3f;
import shadowmage.ancient_warfare.common.aw_structure.AWStructureModule;
import shadowmage.ancient_warfare.common.aw_structure.build.BuilderTicked;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.data.ScannedStructureNormalized;
import shadowmage.ancient_warfare.common.aw_structure.data.ScannedStructureRaw;
import shadowmage.ancient_warfare.common.aw_structure.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;

public class ItemBuilderDirect extends ItemBuilderBase
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBuilderDirect(int itemID)
  {
  super(itemID);
  this.setIconIndex(4);
  this.setMaxStackSize(1);
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
    if(!tag.hasKey("scanning") || tag.getBoolean("scanning")==true)
      {     
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
    else if(tag.hasKey("building"))
      {
      par3List.add("Ready to Build.  Needed Blocks: ");
      NBTTagList blockList = tag.getTagList("blockList");
      for(int i = 0; i < blockList.tagCount(); i++)
        {
        NBTTagCompound blockTag = (NBTTagCompound) blockList.tagAt(i);
        par3List.add(String.valueOf("ID: "+blockTag.getInteger("id")+" meta: "+blockTag.getInteger("mt")+" count: "+blockTag.getInteger("ct")));
        }
      }

    }  
  }

@Override
public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) 
  {
  //TODO clear data if structure is not valid..
  }

/**
 * Gets an icon index based on an item's damage value
 */
@Override
public int getIconFromDamage(int par1)
  {
  return this.iconIndex;
  }

/**
 * 
 * @param world
 * @param player
 * @param stack
 * @param hit null if nothing hit
 * @param side -1 if nothing hit
 * @return
 */
@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  boolean openGUI = false;
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
  if(hit != null && ( !tag.hasKey("scanning") || tag.getBoolean("scanning")==true ))
    {
    if(player.isSneaking())
      {
      hit = BlockTools.offsetForSide(hit, side);
      }
    /**
     * if item is ready to scan, initiate scan
     */
    if(tag.hasKey("pos1")&&tag.hasKey("pos2") && tag.hasKey("buildKey"))
      {
      //TODO move this out...
      
      //TODO other TODO.... really move this stuff out...to playerEntry data...
      BlockPosition pos1 = new BlockPosition(tag.getCompoundTag("pos1"));
      BlockPosition pos2 = new BlockPosition(tag.getCompoundTag("pos2"));
      BlockPosition key = new BlockPosition(tag.getCompoundTag("buildKey"));
      int face = tag.getCompoundTag("buildKey").getInteger("face");
      player.addChatMessage("Initiating Scan and clearing Position Data");
      tag.setString("name", player.getEntityName());
      ProcessedStructure struct = scanAndProcess(world, player, pos1, pos2, key, face);
      
      StructureManager.instance().addTempStructure(player, struct);         
      List<IDPairCount> blockList = struct.getResourceList();
      NBTTagList blocks = new NBTTagList();
      for(IDPairCount ct : blockList)
        {
        if(ct.id==0)
          {
          continue;
          }
        NBTTagCompound countTag = new NBTTagCompound();
        countTag.setInteger("id", ct.id);
        countTag.setInteger("mt", ct.meta);
        countTag.setInteger("ct", ct.count);
        blocks.appendTag(countTag);       
        }
      tag.setTag("blockList", blocks);
               
      tag.setBoolean("scanning", false);
      tag.setBoolean("building", true);
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
    }
  else if(tag.hasKey("building") && tag.getBoolean("building")==true)
    {
    if(player.isSneaking())
      {
      openGUI = true;
      }
    else if(hit!=null)
      {
      hit = BlockTools.offsetForSide(hit, side);
      int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
      ProcessedStructure struct = StructureManager.instance().getTempStructure(player.getEntityName());
      if(struct==null)
        {
        tag = new NBTTagCompound();
        }
      else
        {
        this.attemptConstruction(world, player, hit, face, struct);
        }      
      }
    } 
  /**
   * apply any changes to the itemStackTag
   */
  stack.setTagInfo("structData", tag);
  
  if(openGUI)
    {
    GUIHandler.instance().openGUI(GUIHandler.STRUCTURE_BUILD_DIRECT, player, world, 0, 0, 0);
    }  
  return true;
  }

private ProcessedStructure scanAndProcess(World world, EntityPlayer player, BlockPosition pos1, BlockPosition pos2, BlockPosition key, int face)
  {
  key = BlockTools.offsetBuildKey(face, pos1, pos2, key);
  ScannedStructureRaw raw = new ScannedStructureRaw(face, pos1, pos2, key);
  raw.scan(world);
  ScannedStructureNormalized norm = raw.process();
  norm.name = player.getEntityName();
  return norm.convertToProcessedStructure();  
  }

private boolean attemptConstruction(World world, EntityPlayer player, BlockPosition hit, int face, ProcessedStructure struct)
  {
  List<IDPairCount> counts = struct.getResourceList();
  
  boolean shouldConstruct = player.capabilities.isCreativeMode;
  
  if(!shouldConstruct)
    {
    List<IDPairCount> need = getNeededBlocks(player, counts);
    if(need.size()>0)
      {
      player.addChatMessage("Missing components:");
      for(IDPairCount entry : need)
        {
        player.addChatMessage(entry.toString());
        }
      }
    else
      {
      if(!decrementItems(player, counts))
        {
        Config.logError("Error removing items from player inventory!");        
        }
      shouldConstruct = true;
      }
    }  
  if(shouldConstruct)
    {
    BuilderTicked builder = new BuilderTicked(struct, face, hit);
    builder.setWorld(world);
    builder.startConstruction();
    AWStructureModule.instance().addBuilder(builder);
    return true;
    }
  return false;
  }

private List<IDPairCount> getNeededBlocks(EntityPlayer player, List<IDPairCount> counts)
  {
  ArrayList<IDPairCount> needed = new ArrayList<IDPairCount>();
  if(player==null || counts == null)
    {
    return needed;
    }  
  for(IDPairCount entry : counts)
    {
    if(entry.id==0)
      {
      continue;
      }
    IDPairCount need = entry.copy();
    for(int index = 0; index < player.inventory.getSizeInventory(); index++)
      {
      ItemStack stack = player.inventory.getStackInSlot(index);
      if(stack==null)
        {
        continue;
        }
     
      if(stack.itemID==entry.id && stack.getItemDamage() == entry.meta)
        {
        need.count -= stack.stackSize;
        if(need.count<=0)
          {
          need.count=0;
          break;
          }
        }
      }
    if(need.count>0)
      {
      needed.add(need);
      }
    }
  return needed;
  }

private boolean decrementItems(EntityPlayer player, List<IDPairCount> counts)
  {
  int countFails = 0;
  for(IDPairCount entry : counts)
    {
    if(entry.id==0)
      {
      continue;
      }
    int countLeft = entry.count;    
    for(int index = 0; index < player.inventory.getSizeInventory(); index++)
      {      
      ItemStack stack = player.inventory.getStackInSlot(index);
      if(stack==null)
        {
        continue;
        }      
      if(stack.itemID==entry.id && stack.getItemDamage()==entry.meta)
        {
        int decAmt;
        decAmt = stack.stackSize < countLeft? stack.stackSize : countLeft;
        stack.stackSize -= decAmt;
        countLeft-=decAmt;
        if(stack.stackSize==0)
          {
          player.inventory.setInventorySlotContents(index, null);
          }
        if(countLeft<=0)
          {
          break;
          }
        }
      }
    if(countLeft>0)
      {
      countFails++;
      }
    }  
  player.openContainer.detectAndSendChanges();
  if(countFails==0)
    {
    return true;
    }
  return false;
  }

@Override
public List<AxisAlignedBB> getBBForStructure(EntityPlayer player, String name)
  {
  StructureClientInfo struct = StructureManager.instance().getClientTempStructure();
  if(struct==null)
    {
    return null;
    }
  BlockPosition hit = BlockTools.getBlockClickedOn(player, player.worldObj, true);
  if(hit==null)
    {
    return null;
    }
  int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);  
  hit = this.offsetForWorldRender(hit, face);
  AxisAlignedBB b = struct.getBBForRender(hit, face);  
  ArrayList<AxisAlignedBB> bbs = new ArrayList<AxisAlignedBB>();
  bbs.add(b);
  return bbs;
  }


}
