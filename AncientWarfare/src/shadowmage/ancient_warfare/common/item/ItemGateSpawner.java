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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.gates.types.Gate;
import shadowmage.ancient_warfare.common.interfaces.IScannerItem;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class ItemGateSpawner extends AWItemClickable implements IScannerItem
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemGateSpawner(int itemID)
  {
  super(itemID, true);
  this.hasLeftClick = true;
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote)
    {
    return true;
    }  
  NBTTagCompound tag;
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWGateInfo"))
    {
    tag = stack.getTagCompound().getCompoundTag("AWGateInfo");
    }
  else
    {
    tag = new NBTTagCompound();
    }
  if(isShiftClick(player))
    {
    tag = new NBTTagCompound();
    }
  else if(tag.hasKey("pos1") && tag.hasKey("pos2"))
    {
	  Config.logDebug("getting gate for damage: "+stack.getItemDamage() +" :: "+  Gate.getGateByID(stack.getItemDamage()));
	  byte facing = (byte) BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
    EntityGate entity = Gate.constructGate(world, new BlockPosition(tag.getCompoundTag("pos1")), new BlockPosition(tag.getCompoundTag("pos2")), Gate.getGateByID(stack.getItemDamage()), facing);
    entity.teamNum = TeamTracker.instance().getTeamForPlayer(player);
    Gate.getGateByID(stack.getItemDamage()).onGateFinishClose(entity);
    world.spawnEntityInWorld(entity);
    Config.logDebug("registering gate use final--should build");
    /**
     * do nothing, wait for right click for build order
     */
    return false;
    }
  stack.setTagCompound(tag);
  return false;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote || hit==null)
    {
    return true;
    }
  hit.offsetForMCSide(side);
  NBTTagCompound tag;
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWGateInfo"))
    {
    tag = stack.getTagCompound().getCompoundTag("AWGateInfo");
    }
  else
    {
    tag = new NBTTagCompound();
    }
  if(tag.hasKey("pos1") && tag.hasKey("pos2"))
    {
    /**
     * do nothing, wait for right click for build order
     */
    }
  else if(tag.hasKey("pos1"))
    {
    tag.setCompoundTag("pos2", hit.writeToNBT(new NBTTagCompound()));
    player.addChatMessage("Setting second gate bounds position");
    }
  else
    {
    tag.setCompoundTag("pos1", hit.writeToNBT(new NBTTagCompound()));
    player.addChatMessage("Setting first gate bounds position");
    }
  stack.setTagInfo("AWGateInfo", tag);
  return false;
  }

@Override
public BlockPosition getScanPos1(ItemStack stack)
  {
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWGateInfo") && stack.getTagCompound().getCompoundTag("AWGateInfo").hasKey("pos1"))
    {
    return new BlockPosition(stack.getTagCompound().getCompoundTag("AWGateInfo").getCompoundTag("pos1"));
    }
  return null;
  }

@Override
public BlockPosition getScanPos2(ItemStack stack)
  {
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWGateInfo") && stack.getTagCompound().getCompoundTag("AWGateInfo").hasKey("pos2"))
    {
    return new BlockPosition(stack.getTagCompound().getCompoundTag("AWGateInfo").getCompoundTag("pos2"));
    }
  return null;
  }

}
