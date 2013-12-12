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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;

public class ItemBlockScanner extends AWItemBase
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBlockScanner(int itemID)
  {
  super(itemID, false);
  this.setMaxStackSize(1);
  this.setMaxDamage(0);
  }

@Override
public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOff, float yOff, float zOff)
  {
  return onActivated(world, player, stack, new BlockPosition(x,y,z), side);
  }

@Override
public ItemStack onItemRightClick(ItemStack stack, World world,EntityPlayer player)
  {
  onActivated(world, player, stack, BlockTools.getBlockClickedOn(player, world, false), -1);
  return stack;
  }

@Override
public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
  {
  return false;
  }

/**
 * the actual onActivated call, all rightclick/onUsed/onUse functions funnel through to here.
 * @param world
 * @param player
 * @param stack
 * @param hit
 * @return
 */
private boolean onActivated(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote || hit==null)
    {
    return true;
    }
  int id = world.getBlockId(hit.x, hit.y, hit.z);
  int meta = world.getBlockMetadata(hit.x, hit.y, hit.z);
  int rotation = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
  String facing = rotation==0 ? "South": rotation==1? "West": rotation==2?"North":rotation==3?"East":"huh?";  
  player.addChatMessage("ID: "+id+"  M: "+meta+"  player facing: "+facing+" "+rotation +  " side: "+side);
  Config.logDebug("ID: "+id+"  M: "+meta+"  player facing: "+facing+" "+rotation+" player yaw: "+player.rotationYaw);
  return true;
  }


}
