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

import java.io.File;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.item.AWItemBase;
import shadowmage.ancient_warfare.common.aw_core.network.GUIHandler;
import shadowmage.ancient_warfare.common.aw_structure.AWStructureModule;
import shadowmage.ancient_warfare.common.aw_structure.build.BuilderTicked;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.data.ScannedStructureNormalized;
import shadowmage.ancient_warfare.common.aw_structure.data.ScannedStructureRaw;
import shadowmage.ancient_warfare.common.aw_structure.export.StructureExporter;

public class ItemBuilderDirect extends AWItemBase
{

private static HashMap<String, ProcessedStructure> scannedStructures = new HashMap<String, ProcessedStructure>();

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBuilderDirect(int itemID)
  {
  super(itemID, false);
  this.setIconIndex(4);
  this.setMaxStackSize(1);
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
    if(tag.hasKey("scanning"))
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
      //TODO add blockList data to tooltip
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
  boolean building = isItemBuilding(stack);
  boolean offset = building ? true : player.isSneaking();
  BlockPosition hit = BlockTools.getBlockClickedOn(player, world, offset);
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
  
  /**
   * if it does not have a scanning key, or scanning key is true...
   */
  if(hit != null && ( tag.hasKey("scanning") || tag.getBoolean("scanning")==true ))
    {
    /**
     * if item is ready to scan, initite scan
     */
    if(tag.hasKey("pos1")&&tag.hasKey("pos2") && tag.hasKey("buildKey"))
      {
      BlockPosition pos1 = new BlockPosition(tag.getCompoundTag("pos1"));
      BlockPosition pos2 = new BlockPosition(tag.getCompoundTag("pos2"));
      BlockPosition key = new BlockPosition(tag.getCompoundTag("buildKey"));
      int face = tag.getCompoundTag("buildKey").getInteger("face");
      player.addChatMessage("Initiating Scan and clearing Position Data");
      this.scannedStructures.put(player.getEntityName(), scanAndProcess(world, player, pos1, pos2, key, face));   
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
      int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
      ProcessedStructure struct = this.scannedStructures.get(player.getEntityName());
      if(struct==null)
        {
        tag.setBoolean("building", false);
        tag.setBoolean("scanning", false);
        }
      else
        {
        BuilderTicked builder = new BuilderTicked(world, struct, face, hit);
        AWStructureModule.instance().addBuilder(builder);
        builder.startConstruction();
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

public static void clearBuildingData(ItemStack stack)
  {
  if(stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey("building"))
    {
    stack.getTagCompound().getCompoundTag("structData").setBoolean("building", false);
    }
  }

public static void clearScanningData(ItemStack stack)
  {
  if(stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey("scanning"))
    {
    stack.getTagCompound().getCompoundTag("structData").setBoolean("scanning", false);
    }
  }



/**
 * actually scan the structure.
 * @param world
 * @param player
 * @return
 */
public boolean scanStructure(World world, EntityPlayer player, BlockPosition pos1, BlockPosition pos2, BlockPosition key, int face)
  {
  //  key = offsetBuildKey(face, pos1, pos2, key);
  //  ScannedStructureRaw rawStructure = new ScannedStructureRaw(face ,pos1, pos2, key);
  //  rawStructure.scan(world);
  //  ScannedStructureNormalized normalizedStructure = rawStructure.process();
  //  String name = String.valueOf(System.currentTimeMillis());
  //  this.scannedStructures.put(player, normalizedStructure);
  //  GUIHandler.instance().openGUI(GUIHandler.STRUCTURE_SCANNER, player, world, 0, 0, 0);



  //  if(AWStructureModule.outputDirectory!=null)  
  //    {
  //    normalizedStructure.writeToFile(AWStructureModule.outputDirectory+name+".aws");
  //    if(Config.DEBUG)
  //      {
  //      ItemDebugBuilder.buildStructures.put(player, StructureLoader.processSingleStructure(new File(AWStructureModule.outputDirectory+name+".aws")));
  //      }
  //    }
  //  else
  //    {
  //    Config.logError("Invalid export/output directory specified in source, could not export structure!");
  //    }
  return true;
  }

/**
 * clears ALL struct data by replacing structData tag with empty tag
 * @param stack
 * @return
 */
public static ItemStack clearStructureData(ItemStack stack)
  {
  if(stack!=null && stack.hasTagCompound())
    {    
    stack.setTagInfo("structData", new NBTTagCompound());    
    } 
  return stack;
  }

private ProcessedStructure scanAndProcess(World world, EntityPlayer player, BlockPosition pos1, BlockPosition pos2, BlockPosition key, int face)
  {
  key = BlockTools.offsetBuildKey(face, pos1, pos2, key);
  ScannedStructureRaw raw = new ScannedStructureRaw(face, pos1, pos2, key);
  raw.scan(world);
  ScannedStructureNormalized norm = raw.process();
  File oldFile = new File(AWStructureModule.playerTempDirectory+player.getEntityName()+".aws");
  if(oldFile.exists())
    {
    oldFile.delete();
    }
  StructureExporter.writeStructureToFile(norm, AWStructureModule.playerTempDirectory+player.getEntityName()+".aws");
  return norm.convertToProcessedStructure(player.getEntityName());  
  }

public static void addStructures(List<ProcessedStructure> structs)
  {
  scannedStructures.clear();
  for(ProcessedStructure struct : structs)
    {
    scannedStructures.put(struct.name, struct);
    }
  }

private boolean isItemScanning(ItemStack stack)
  {
  return stack.hasTagCompound() && stack.getTagCompound().getBoolean("scanning")==true;
  }

private boolean isItemBuilding(ItemStack stack)
  {
  return stack.hasTagCompound() && stack.getTagCompound().getBoolean("building")==true;
  }


}
