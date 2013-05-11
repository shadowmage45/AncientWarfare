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
package shadowmage.ancient_warfare.common.npcs.waypoints;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.item.ItemLoader;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * routing information for a single routing slip for a courier
 * maintains information on the waypoint target(s) for the route
 * as well as the filters to be applied at those stops
 * @author Shadowmage
 *
 */
public class CourierRoutingInfo implements INBTTaggable
{

List<WayPointItemRouting> routingPoints = new ArrayList<WayPointItemRouting>();

public CourierRoutingInfo(ItemStack stack)
  {
  if(stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey("route"))
    {
    this.readFromNBT(stack.getTagCompound().getCompoundTag("route"));
    }
  }

public CourierRoutingInfo(NBTTagCompound tag)
  {
  this.readFromNBT(tag);
  }

public void writeToItem(ItemStack stack)
  {
  if(stack!=null && stack.getItem()==ItemLoader.courierRouteSlip)
    {
    stack.setTagInfo("route", getNBTTag());
    }
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
  
  }







}
