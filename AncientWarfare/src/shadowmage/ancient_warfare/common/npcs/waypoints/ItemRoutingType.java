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

public enum ItemRoutingType
  {
  NONE,//no action
  KEEP_STOCKED,//keep the specified item stocked in the specified amount
  KEEP_EMPTIED,//keep the specified item removed from the inventory
  EMTPY_MORE_THAN,//if more than the specified amount, remove extras
  ALLOW_WITHDRAWAL,//FOR KEEP STOCKED, ALLOW WITHDRAWAL OF THIS ITEM FROM THIS POINT
  WITHDRAW_ALL,//remove all items from the specified inventory
  DEFAULT_DEPOSIT,
  }
