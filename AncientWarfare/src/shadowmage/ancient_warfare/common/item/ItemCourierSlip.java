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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.waypoints.CourierRoutingInfo;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPointItemRouting;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class ItemCourierSlip extends AWItemClickable
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemCourierSlip(int itemID)
  {
  super(itemID, true);
  this.hasLeftClick = true;
  this.setMaxStackSize(1);
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {  
  super.addInformation(stack, player, list, par4);
  list.add("Left Click: Add Routing Point");
  list.add("   for block and side clicked");
  list.add("Right Click: Open GUI");
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote)
    {
    return true;
    }
  GUIHandler.instance().openGUI(GUIHandler.COURIER_SLIP, player, world, 0, 0, 0);
  return true;
  }

@Override
public boolean getShareTag()
  {
  return false;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player,  ItemStack stack, BlockPosition hit, int side)
  {
  TileEntity te = player.worldObj.getBlockTileEntity(hit.x, hit.y, hit.z);
  if(te instanceof IInventory)
    {
    CourierRoutingInfo info = new CourierRoutingInfo(stack);
    if(info.getRouteSize() < 4 + (2*stack.getItemDamage()) || info.getSwapPoint()>-1)
      {
      info.addRoutePoint(new WayPointItemRouting(hit.x, hit.y, hit.z, side));
      info.writeToItem(stack);    
      GUIHandler.instance().openGUI(GUIHandler.COURIER_SLIP, player, player.worldObj, 0, 0, 0);
      }
    else
      {
      player.addChatMessage("Routing Slip has full route!");
      }
    }
  return false;
  }




}
