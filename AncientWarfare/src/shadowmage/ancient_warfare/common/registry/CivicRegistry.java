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
package shadowmage.ancient_warfare.common.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.utils.BlockLoader;
import cpw.mods.fml.common.registry.LanguageRegistry;

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
  ItemStack regStack = null;
  for(Civic civ : Civic.civicList)
    {
    if(civ!=null)
      {
      for(int i = 0; i < civ.getNumOfRanks(); i++)
        {
        regStack = civ.getDisplayItem(i);
        LanguageRegistry.instance().addName(regStack, civ.getDisplayName(i));
        }
      }
    }
  }

public void setCivicBlock(World world, int x, int y, int z, int type, int rank)
  {
  Block block = getBlockFor(type);
  if(block!=null)
    {
    world.setBlockAndMetadata(x, y, z, block.blockID, type%16);
    TECivic te = (TECivic) world.getBlockTileEntity(x, y, z);
    if(te!=null)
      {
      te.setCivic(getCivicFor(type), rank);      
      }
    }
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
    if(civ!=null)
      {
      for(int i = 0; i < civ.getNumOfRanks(); i++)
        {
        displayStacks.add(civ.getDisplayItem(i));
        }
      }
    }
  displayStackCache = displayStacks;
  return displayStacks;
  }

}
