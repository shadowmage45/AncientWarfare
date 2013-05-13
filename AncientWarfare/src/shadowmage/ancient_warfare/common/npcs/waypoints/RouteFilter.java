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

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

/**
 * maintains list of accessibility methods for a routing waypoint
 * to easier query what should be moved/etc
 * filter is reconstructed each time an NPC moves to a waypoint
 * 
 * @author Shadowmage
 *
 */
public class RouteFilter
{

/**
 * the point this filter is for
 */
WayPointItemRouting point;

ArrayList<Filter> filters = new ArrayList<Filter>();

protected boolean finished = false;

NpcBase npc;

private class Filter
  {
  ItemStack filterStack;
  int neededQuantities;
  }

public RouteFilter(WayPointItemRouting point, NpcBase npc)
  {
  this.point = point;
  this.npc = npc;
  ItemStack filterStack;  
  for(int i = 0; i < point.getFilterLength(); i++)
    {
    filterStack = point.getFilterStack(i);
    if(filterStack!=null)
      {
      boolean found = false;
      for(Filter f : this.filters)
        {        
        if(InventoryTools.doItemsMatch(filterStack, f.filterStack))
          {
          found = true;
          f.neededQuantities +=filterStack.stackSize;
          break;
          }        
        }
      if(!found)
        {
        Filter f = new Filter();
        f.filterStack = filterStack.copy();
        f.neededQuantities = filterStack.stackSize;
        filters.add(f);
        }
      }
    }
  }

public RouteInfo getNextTransaction()
  {
  TileEntity te = point.getTileEntity(npc.worldObj);
  if(te instanceof IInventory)
    {
    IInventory inventory = (IInventory)te;
    ItemStack fromSlot = null;
    for(int i = 0; i < inventory.getSizeInventory(); i++)
      {
      fromSlot = inventory.getStackInSlot(i);
      
      }        
    /**
     * scan through inventory
     *  for each item
     *    if matches a filter && !filter.isDone
     *      decrease filter needed count by match size
     *      return routeInfo for that item
     */    
    }
  this.finished = true;
  return null;
  }

public boolean isFinished()
  {
  return finished;
  }


}
