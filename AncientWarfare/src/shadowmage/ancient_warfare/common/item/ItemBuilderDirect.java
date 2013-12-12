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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_framework.common.utils.IDPairCount;
import shadowmage.ancient_structures.common.AWStructures;
import shadowmage.ancient_structures.common.structures.build.BuilderTicked;
import shadowmage.ancient_structures.common.structures.data.ProcessedStructure;
import shadowmage.ancient_structures.common.structures.data.ScannedStructureData;
import shadowmage.ancient_structures.common.structures.data.StructureBuildSettings;
import shadowmage.ancient_structures.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.interfaces.IScannerItem;
import shadowmage.ancient_warfare.common.manager.StructureManager;

public class ItemBuilderDirect extends ItemBuilderBase implements IScannerItem
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBuilderDirect(int itemID)
  {
  super(itemID);
//  this.setIconIndex(4);
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
    if(!tag.hasKey("scanning") || tag.getBoolean("scanning")==true)
      {     
      if(tag.hasKey("pos1")&&tag.hasKey("pos2") && tag.hasKey("buildKey"))
        {        
        list.add("Right Click: Scan and Process");
        list.add("(Shift)Right Click: Clear/cancel");
        }        
      else if(!tag.hasKey("pos1"))
        {
        list.add("Left Click: Set first scan bounds");
        list.add("Hold Shift to offset for side hit");
        list.add("(Shift)Right Click: Clear/cancel");
        }
      else if(!tag.hasKey("pos2"))
        {
        list.add("Left Click: Set second scan bounds");
        list.add("Hold Shift to offset for side hit");
        list.add("(Shift)Right Click: Clear/cancel");
        }
      else if(!tag.hasKey("buildKey"))
        {
        list.add("Left Click: Set build key and facing");
        list.add("Hold Shift to offset for side hit");
        list.add("(Shift)Right Click: Clear/cancel");
        }
      }
    else if(tag.hasKey("building"))
      {
      list.add("Right Click: Open GUI");
      list.add("(Shift)Right Click: Build Structure");
      list.add("Ready to Build.  Needed Blocks: ");
      NBTTagList blockList = tag.getTagList("blockList");
      for(int i = 0; i < blockList.tagCount(); i++)
        {
        NBTTagCompound blockTag = (NBTTagCompound) blockList.tagAt(i);
        list.add(String.valueOf("ID: "+blockTag.getInteger("id")+" meta: "+blockTag.getInteger("mt")+" count: "+blockTag.getInteger("ct")));
        }
      }

    }  
  }

@Override
public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) 
  {
  if(world.isRemote)
    {
    return;
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
  if(tag.hasKey("building") && tag.getBoolean("building")==true)
    {
    if(entity instanceof EntityPlayer)
      {
      EntityPlayer player = (EntityPlayer)entity;
      if(StructureManager.instance().getTempStructure(player.getEntityName())==null)
        {
        tag = new NBTTagCompound();
        stack.setTagInfo("structData", tag);
        }
      }
    }
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
  
  if(!tag.hasKey("name"))//hit was not null, and has no current structure
    {    
    if(isShiftClick(player))
      {
      tag = new NBTTagCompound();    
      }
    }
  else 
    {
    if(isShiftClick(player))
      {
      if(hit!=null)
        {
        hit = BlockTools.offsetForSide(hit, side);
        int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
        StructureBuildSettings settings = StructureBuildSettings.constructFromNBT(tag);
        settings.spawnGate = false;
        settings.spawnNpc = false;
        settings.spawnVehicle = false;
        ProcessedStructure struct = StructureManager.instance().getTempStructure(player.getEntityName());
        if(struct==null)
          {
          tag = new NBTTagCompound();
          }
        else
          {
          this.attemptConstruction(world, player, hit, face, struct, settings);
          } 
        }
      else
        {
        tag = new NBTTagCompound();
        }
      }
    else
      {
      GUIHandler.instance().openGUI(GUIHandler.STRUCTURE_BUILD_DIRECT, player, world, 0, 0, 0);           
      } 
    } 
  /**
   * apply any changes to the itemStackTag
   */
  stack.setTagInfo("structData", tag);
  return true;
  }

private ProcessedStructure scanAndProcess(World world, EntityPlayer player, BlockPosition pos1, BlockPosition pos2, BlockPosition key, int face)
  {
  key = BlockTools.offsetBuildKey(face, pos1, pos2, key);
  ScannedStructureData raw = new ScannedStructureData(face, pos1, pos2, key);
  raw.scan(world);
  raw.name = player.getEntityName();
  return raw.convertToProcessedStructure();  
  }

private boolean attemptConstruction(World world, EntityPlayer player, BlockPosition hit, int face, ProcessedStructure struct, StructureBuildSettings settings)
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
    return this.attemptConstruction(world, struct, hit, face, settings);
    }
  return false;
  }

@Override
public boolean attemptConstruction(World world, ProcessedStructure struct,   BlockPosition hit, int facing, StructureBuildSettings settings)
  {
  if(!struct.isLocked())
    {
    BuilderTicked builder = new BuilderTicked(world, struct, facing, hit);
    builder.setWorld(world);
    builder.startConstruction();
    builder.setOverrides(-1, false, false, true);
    AWStructures.instance.addBuilder(builder);
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
public StructureClientInfo getStructureForStack(ItemStack stack)
  {
  return StructureManager.instance().getClientTempStructure();
  }

@Override
public boolean renderBuilderBlockBB()
  {
  return false;
  }

public static boolean isScanning(ItemStack stack)
  {
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("structData"))
    {
    return !stack.getTagCompound().getCompoundTag("structData").hasKey("name");//.getBoolean("scanning")==true;
    }
  return false;
  }

/**
 * these ugly hacks only work because the NBT structure for scan data -- "structData" is the same between
 * most of the items....
 */
@Override
public BlockPosition getScanPos1(ItemStack stack)
  {  
  return ItemStructureScanner.getPos1(stack);  
  }

@Override
public BlockPosition getScanPos2(ItemStack stack)
  {
  return ItemStructureScanner.getPos2(stack);
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
  if(hit==null){return true;}
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("structData"))
    {
    tag = stack.getTagCompound().getCompoundTag("structData");
    }
  else
    {
    tag = new NBTTagCompound();
    } 
  
  if(!tag.hasKey("name"))//hit was not null, and has no current structure
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
      BlockPosition pos1 = new BlockPosition(tag.getCompoundTag("pos1"));
      BlockPosition pos2 = new BlockPosition(tag.getCompoundTag("pos2"));
      BlockPosition key = new BlockPosition(tag.getCompoundTag("buildKey"));
      int face = tag.getCompoundTag("buildKey").getInteger("face");
      player.addChatMessage("Initiating Scan and clearing Position Data");
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
      tag = new NBTTagCompound();
      tag.setTag("blockList", blocks);
      tag.setString("name", player.getEntityName());               
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
  /**
   * apply any changes to the itemStackTag
   */
  stack.setTagInfo("structData", tag);  
  return false;
  }

}
