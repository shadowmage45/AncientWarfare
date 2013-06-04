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

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.item.AWItemBase;
import shadowmage.ancient_warfare.common.aw_structure.build.BuilderInstant;
import shadowmage.ancient_warfare.common.aw_structure.build.BuilderTicked;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;

/***
 * rendered deprecated by builderDirect.  needs removed completely....
 * @author Shadowmage
 *
 */
@Deprecated
public class ItemDebugBuilder extends AWItemBase
{


public static Map<EntityPlayer, ProcessedStructure> buildStructures = new HashMap<EntityPlayer, ProcessedStructure>();

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemDebugBuilder(int itemID)
  {
  super(itemID, false);
  this.setIconIndex(2);
  }


@Override
public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOff, float yOff, float zOff)
  {
  BlockPosition hitPos = new BlockPosition(x,y,z);
  if(!player.isSneaking())
    {
    hitPos = BlockTools.offsetForSide(hitPos, side);
    }
  return onActivated(world, player, stack, hitPos);
  }

//
//@Override
//public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOff, float yOff, float zOff)
//  {
//  return onActivated(world, player, stack, new BlockPosition(x,y,z));
//  }

@Override
public ItemStack onItemRightClick(ItemStack stack, World world,EntityPlayer player)
  {  
  onActivated(world, player, stack, BlockTools.getBlockClickedOn(player, world, !player.isSneaking()));
  return stack;
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

/**
 * the actual onActivated call, all rightclick/onUsed/onUse functions funnel through to here.
 * @param world
 * @param player
 * @param stack
 * @param hit
 * @return
 */
private boolean onActivated(World world, EntityPlayer player, ItemStack stack, BlockPosition hit)
  {
  if(world.isRemote || hit==null)
    {
    return true;
    }
  ProcessedStructure struct = buildStructures.get(player);
  if(struct==null)
    {
    return true;
    }   
  int rotation = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
  //BuilderInstant builder = new BuilderInstant(world, struct, rotation, hit);
  BuilderTicked builder = new BuilderTicked(struct, rotation, hit);
  builder.setWorld(world);
  builder.startConstruction();
  return true;
  }


}
