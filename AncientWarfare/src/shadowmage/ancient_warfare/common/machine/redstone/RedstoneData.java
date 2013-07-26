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
package shadowmage.ancient_warfare.common.machine.redstone;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.machine.redstone.logic.IRedstoneLogicTile;
import shadowmage.ancient_warfare.common.machine.redstone.logic.RedstoneCable;
import shadowmage.ancient_warfare.common.machine.redstone.logic.RedstoneLogicTileGeneric;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class RedstoneData
{

public static final int GENERIC = 0;
public static final int BARE_CABLE = 1;
public static final int INSULATED_CABLE = 2;
public static final int CONDUIT = 3;

public static IRedstoneLogicTile getLogicTile(int tileType, int tileMeta)
  {
  RedstoneLogicTileGeneric tile = null;
  
  switch(tileType)
  {
  case GENERIC:
    {
    tile = new RedstoneLogicTileGeneric(GENERIC, tileMeta);
    }
  break;
  
  case BARE_CABLE:
    {
    tile = new RedstoneCable(BARE_CABLE, tileMeta);
    }
  break;
  
  case INSULATED_CABLE:
    {
    
    }
  break;
  
  case CONDUIT:
    {
    
    }
  break;
  }
  
  if(tile==null)
    {
    tile = new RedstoneLogicTileGeneric(tileType, tileMeta);
    }  
  return tile;
  }

public static int getTileType(ItemStack stack)
  {
  if(stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey("logic"))
    {
    return stack.getTagCompound().getCompoundTag("logic").getInteger("type");
    }
  return 0;
  }

public static int getTileMeta(ItemStack stack)
  {
  if(stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey("logic"))
    {
    return stack.getTagCompound().getCompoundTag("logic").getInteger("meta");
    }
  return 0;
  }

public static void registerBlockInfo()
  {
//  if(true)//TODO HACK DEBUG
//    {
//    return;
//    }
  Description d = DescriptionRegistry2.instance().getDescriptionFor(BlockLoader.redstoneLogic.blockID);  
  ItemStack stack;    
  d.addDisplayStack(getStackForType(1, 0));
  }

public static ItemStack getStackForType(int type, int meta)
  {
  ItemStack stack = new ItemStack(BlockLoader.redstoneLogic.blockID, 1, 0);
  NBTTagCompound tag = new NBTTagCompound("Tag");
  NBTTagCompound logicTag = new NBTTagCompound();
  logicTag.setInteger("type", type);
  logicTag.setInteger("meta", meta);  
  tag.setCompoundTag("logic", logicTag);
  stack.setTagCompound(tag);
  return stack;
  }

public static void registerIcons(IconRegister registry, Description d)
  {
  
  }

}
