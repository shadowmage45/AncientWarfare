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
package shadowmage.ancient_warfare.common.vehicles.helpers;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;



public class VehicleFiringVarsHelper implements INBTTaggable
{

protected VehicleBase vehicle;

public VehicleFiringVarsHelper(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

/**
 * called on every tick that the vehicle is 'firing' to update the firing animation and to call
 * launchMissile when animation has reached launch point
 */
public void onFiringUpdate(){}

/**
 * called every tick after the vehicle has fired, until reload timer is complete, to update animations
 */
public void onReloadUpdate(){}

/**
 * called every tick after startLaunching() is called, until setFinishedLaunching() is called...
 */
public void onLaunchingUpdate(){}

@Override
public NBTTagCompound getNBTTag()
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
  
  }

}
