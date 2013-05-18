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
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote)
    {
    return true;
    }
  if(isShiftClick(player))
    {
    GUIHandler.instance().openGUI(GUIHandler.COURIER_SLIP, player, world, 0, 0, 0);
    }
//  else if(hit!=null)
//    {
//    CourierRoutingInfo info = new CourierRoutingInfo(stack);
//    info.addRoutePoint(new WayPointItemRouting(hit.x, hit.y, hit.z, side, TargetType.DELIVER));
//    info.writeToItem(stack);    
//    GUIHandler.instance().openGUI(GUIHandler.COURIER_SLIP, player, world, 0, 0, 0);
//    }
  return true;
  }

@Override
public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player)
  { 
  boolean flag = player.capabilities.isCreativeMode;
  if(!player.worldObj.isRemote)
    {
    TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
    if(te instanceof IInventory)
      {
      MovingObjectPosition hit = getMovingObjectPositionFromPlayer(player.worldObj, player, true);
      CourierRoutingInfo info = new CourierRoutingInfo(stack);
      if(info.getRouteSize() < 4 + (2*stack.getItemDamage()))
        {
        info.addRoutePoint(new WayPointItemRouting(x, y, z, hit.sideHit));
        info.writeToItem(stack);    
        GUIHandler.instance().openGUI(GUIHandler.COURIER_SLIP, player, player.worldObj, 0, 0, 0);
        }
      else
        {
        player.addChatMessage("Routing Slip has full route!");
        }
      }    
    }   
  return flag;
  }

@Override
public boolean getShareTag()
  {
  return false;
  }




}
