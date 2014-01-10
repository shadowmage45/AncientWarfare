/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.warzone;

import java.util.Collection;
import java.util.Collections;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class WarzoneManager
{

WarzoneSaveData warData;

public static WarzoneManager instance(){return instance;}
private static WarzoneManager instance = new WarzoneManager();
private WarzoneManager(){}

public void onWorldLoad(World world)
  {
  warData = (WarzoneSaveData) world.mapStorage.loadData(WarzoneSaveData.class, WarzoneSaveData.dataName);
  if(warData==null)
    {
    warData = new WarzoneSaveData();
    world.mapStorage.setData(WarzoneSaveData.dataName, warData);
    }  
  }

public void addWarzone(World world, BlockPosition p1, BlockPosition p2)
  {
  if(warData==null){return;}
  warData.addNewZone(world, new Warzone(p1, p2));  
  warData.markDirty();
  }

public boolean isPositionInZone(World world, int x, int y, int z)
  {
  if(warData==null){return false;}
  return warData.isPositionInZone(world, x, y, z);
  }

public boolean shouldBreakBlock(World world, int x, int y, int z)
  {
  boolean useWarzones = !Config.globalWarfare;
  if(!useWarzones){return true;}
  boolean invertZones = Config.warzonesArePeaceZones;
  boolean insideZone = isPositionInZone(world, x, y, z);
  return (!invertZones && insideZone) || (invertZones && !insideZone);
  }

public Collection<Warzone> getCurrentWarzones(World world)
  {
  if(warData==null){return Collections.emptyList();}
  return warData.getWarzones(world);
  }

public void removeWarzone(World world, BlockPosition min, BlockPosition max)
  {
  this.warData.removeWarzone(world, min, max);
  this.warData.markDirty();
  }

}
