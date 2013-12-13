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
package shadowmage.ancient_warfare.common.npcs.waypoints;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.item.ItemLoaderCore;

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
int swapPoint = -1;

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

public int getRouteSize()
  {
  return routingPoints.size();
  }

public WayPointItemRouting getPoint(int index)
  {
  if(index>=0 && index<this.routingPoints.size())
    {
    return this.routingPoints.get(index);
    }
  return null;
  }

public void addRoutePoint(WayPointItemRouting point)
  {
  if(point!=null)
    {
    if(this.swapPoint>=0 && this.swapPoint <this.routingPoints.size())
      {
      this.routingPoints.get(swapPoint).reassignPoint(point.floorX(), point.floorY(), point.floorZ(), point.getSide());
      this.swapPoint = -1;
      }
    else
      {
      this.routingPoints.add(point);
      this.swapPoint = -1;
      }
    }
  }

public void setSwapPoint(int index)
  {
  if(index>=0 && index<this.routingPoints.size())
    {
    this.swapPoint = index;    
    }
  else
    {
    this.swapPoint = -1;
    }
  }

public void removeRoutePoint(int index)
  {
  if(index>=0 && index<this.routingPoints.size())
    {
    this.routingPoints.remove(index);
    }
  }

public void setRoutingPoint(int index, WayPointItemRouting p)
  {
  if(index>=0 && index<this.routingPoints.size())
    {
    this.routingPoints.set(index, p);
    }
  else if(index==this.routingPoints.size())
    {
    this.routingPoints.add(p);
    }
  }

public void movePointUp(int index)
  {
  if(index>0 && index<this.routingPoints.size())
    {
    WayPointItemRouting p = this.getPoint(index);
    if(p!=null)
      {
      this.removeRoutePoint(index);
      this.routingPoints.add(index-1, p);      
      }
    }
  }

public int getSwapPoint()
  {
  return this.swapPoint;
  }

public void movePointDown(int index)
  {
  if(index>=0 && index<this.routingPoints.size())
    {
    WayPointItemRouting p = this.getPoint(index);
    if(p!=null)
      {
      this.removeRoutePoint(index);
      if(index>=this.routingPoints.size())
        {
        this.routingPoints.add(p);
        }
      else
        {
        this.routingPoints.add(index+1, p);
        }
      }
    }
  }

public void writeToItem(ItemStack stack)
  {
  if(stack!=null && stack.itemID==ItemLoaderCore.routingSlip.itemID)
    {
    stack.setTagInfo("route", getNBTTag());
    }
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList points = new NBTTagList();
  for(int i = 0; i < this.routingPoints.size(); i++)
    {
    points.appendTag(this.routingPoints.get(i).getNBTTag());
    }  
  tag.setTag("list", points);
  if(this.swapPoint>-1)
    {
    tag.setInteger("swap", this.swapPoint);
    }
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  NBTTagList points = tag.getTagList("list");
  for(int i = 0; i < points.tagCount(); i++)
    {
    this.routingPoints.add(new WayPointItemRouting((NBTTagCompound)points.tagAt(i)));
    }
  if(tag.hasKey("swap"))
    {
    this.swapPoint = tag.getInteger("swap");
    }
  }







}
