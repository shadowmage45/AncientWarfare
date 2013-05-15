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
package shadowmage.ancient_warfare.common.civics;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;

public class TECivicWarehouse extends TECivic
{

int storageSize = 0;
List<WayPoint> wareHouseBlocks = new ArrayList<WayPoint>();

/**
 * 
 */
public TECivicWarehouse()
  {
  // TODO Auto-generated constructor stub
  }

public void addWareHouseBlock(int x, int y, int z)
  {
  this.storageSize += 27;
  Config.logDebug("adding warehouse block. new size: "+this.storageSize);
  }

public void removeWarehouseBlock(int x, int y, int z)
  {
  this.storageSize-= 27;
  Config.logDebug("removing warehouse block. new size: "+this.storageSize);
  }

}
