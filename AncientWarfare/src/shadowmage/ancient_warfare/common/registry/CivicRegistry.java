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
package shadowmage.ancient_warfare.common.registry;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.registry.entry.Description;

/**
 * map civic Blocks and TEs to the item damage/rank for the spawner item
 * @author Shadowmage
 *
 */
public class CivicRegistry
{

private static CivicRegistry INSTANCE;
private CivicRegistry(){}

private List<ItemStack> displayStackCache;

public static CivicRegistry instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new CivicRegistry();    
    }
  return INSTANCE;
  }

/**
 * called from AWCore to register item descriptions, etc
 */
public void registerCivics()
  {  
  for(Civic civ : Civic.civicList)
    {
	  if(civ==null || !civ.addToCreativeMenu()){continue;}
    Description d = ItemLoader.instance().addSubtypeInfoToItem(ItemLoader.civicPlacer, civ.getGlobalID(), civ.getDisplayName(), "", civ.getDisplayTooltip());
    d.setIconTexture(civ.getIconTexture(), civ.getGlobalID());
    Block block = getBlockFor(civ.getGlobalID());
    int meta = civ.getGlobalID()%16;
    LanguageRegistry.addName(new ItemStack(block, 1 , meta), civ.getDisplayName());    
    }
  }

public void setCivicBlock(World world, int x, int y, int z, int type)
  {
  Block block = null;
  block = getBlockFor(type); 
  world.setBlock(x, y, z, block.blockID, type%16, 3);    
  TECivic te = (TECivic) world.getBlockTileEntity(x, y, z);    
  te.setCivic(getCivicFor(type));      
  }

public ItemStack getItemFor(int blockNum, int meta)
  {
  return this.getItemForCivic(blockNum*16 + meta);
  }

public ItemStack getItemForCivic(int type)
  {
  if(type>=0 && type<Civic.civicList.length && Civic.civicList[type]!=null)
    {
    return Civic.civicList[type].getItemToConstruct();
    }
  return null;
  }

public Civic getCivicFor(int type)
  {
  if(type>=0 && type < Civic.civicList.length)
    {
    return Civic.civicList[type];
    }
  return null;
  }

protected Block getBlockFor(int type)
  {
  int block = type/16;
  switch(block)
  {
  case 0:
  return BlockLoader.civicBlock1;
  case 1:
  return BlockLoader.civicBlock2;
  case 2:
  return BlockLoader.civicBlock3;
  case 3:
  return BlockLoader.civicBlock4;
  }
  return null;
  }

/**
 * called from CivicBlock to determine which TileEntity is for it.
 * TE is determined by blockID and meta (4 blockIDs used for 64 Civic types @ 16/block)
 * (uses blockNum*4+meta to determine type)
 * must ensure order is not changed between updates, or bad things will occur...
 * @param world
 * @param type
 * @return
 */
public TileEntity getTEFor(World world, int type)
  {
  TECivic te = null;
  if(Civic.civicList[type]!=null)
    {    
    try
      {
      Civic civ = getCivicFor(type);
      if(civ!=null && civ.getTileEntityClass()!=null)
        {
        te = civ.getTileEntityClass().newInstance();
        }      
      if(te!=null)      
        {
        te.worldObj = world;
        }
      } 
    catch (InstantiationException e)
      {
      e.printStackTrace();
      } 
    catch (IllegalAccessException e)
      {
      e.printStackTrace();
      }
    }
  return te;
  }

public List<ItemStack> getDisplayStacks()
  {
  if(displayStackCache!=null)
    {
    return displayStackCache;
    }
  List<ItemStack> displayStacks = new ArrayList<ItemStack>();
  for(Civic civ : Civic.civicList)
    {
    if(civ!=null && civ.addToCreativeMenu())
      {
      ItemStack displayStack = new ItemStack(ItemLoader.civicPlacer,1,civ.getGlobalID());
      NBTTagCompound tag = new NBTTagCompound();
      displayStack.setTagInfo("civicInfo", tag);
      displayStacks.add(displayStack); 
      }
    }
  displayStackCache = displayStacks;
  return displayStacks;
  }

}
