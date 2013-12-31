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
package shadowmage.ancient_vehicles.common.vehicles.helpers;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_vehicles.common.vehicles.VehicleBase;

public class VehiclePacketDataManager
{

public VehiclePacketDataManager(VehicleBase vehicleBase) 
  {
  // TODO Auto-generated constructor stub
  }

public void addData(String string, NBTTagCompound tag)
  {
  // TODO Auto-generated method stub

  }

public void setPackCommand(){}
public void setHealthUpdate(int val){}
public void setAmmoSelect(int type){}
public void setUpgradeUpdate(int[] upgrades, int[] armors){}
public void setAmmoCount(int type, int count){}
public void setMoveData(VehicleBase vehicle){}
public void onPacketReceived(NBTTagCompound tag){}

}
