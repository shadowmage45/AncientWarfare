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
package shadowmage.ancient_warfare.common.vehicles.VehicleVarHelpers;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleFiringVarsHelper;

public class BallistaVarHelper extends VehicleFiringVarsHelper
{


public float crankAngle = 0.f;
public float crankSpeed = 0.f;

public float bowAngle = 0.f;
public float bowSpeed = 0.f;

public float stringAngle = 0.f;
public float stringSpeed = 0.f;

/**
 * @param vehicle
 */
public BallistaVarHelper(VehicleBase vehicle)
  {
  super(vehicle);
  }

@Override
public void onFiringUpdate()
  {
  vehicle.firingHelper.startLaunching();
  }

@Override
public void onReloadUpdate()
  {
  
  }

@Override
public void onLaunchingUpdate()
  {
  vehicle.firingHelper.spawnMissile(0, 0, 0);
  vehicle.firingHelper.setFinishedLaunching();
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setFloat("cA", crankAngle);
  tag.setFloat("cS", crankSpeed);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.crankAngle = tag.getFloat("cA");
  this.crankSpeed = tag.getFloat("cS"); 
  }

@Override
public float getVar1()
  {
  return crankAngle;
  }

@Override
public float getVar2()
  {
  return crankSpeed;
  }

@Override
public float getVar3()
  {
  return bowAngle;
  }

@Override
public float getVar4()
  {
  return bowSpeed;
  }

@Override
public float getVar5()
  {
  return stringAngle;
  }

@Override
public float getVar6()
  {
  return stringSpeed;
  }

@Override
public float getVar7()
  {
  return 0;
  }

@Override
public float getVar8()
  {
  return 0;
  }

}
