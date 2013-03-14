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
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleFiringVarsHelper;

public class CatapultVarHelper extends VehicleFiringVarsHelper
{

public float armAngle = -7.f;
public float armSpeed = 0.f;
public float crankAngle = 0.f;
public float crankSpeed = 0.f;


/**
 * @param vehicle
 */
public CatapultVarHelper(VehicleBase vehicle)
  {
  super(vehicle);
  }

@Override
public void onFiringUpdate()
  {    
  float prevAngle = this.armAngle;
  this.armAngle += 87.f/20;
  if(this.armAngle>=70)
    {
    vehicle.firingHelper.startLaunching();
    this.armAngle = 70.f;
    }
  this.armSpeed = this.armAngle - prevAngle;
  }

@Override
public void onLaunchingUpdate()
  { 
  for(int i = 0; i <10; i++)
    {
    vehicle.firingHelper.spawnMissile(0, 0, 0);
    }  
  vehicle.firingHelper.setFinishedLaunching();
  }

@Override
public void onReloadUpdate()
  {  
  float prevAngle = this.armAngle;
  this.armAngle -= 87 / (float)vehicle.currentReloadTicks;
  if(this.armAngle <= -7)
    {
    this.armAngle = -7;
    }
  this.armSpeed =this.armAngle - prevAngle;
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setFloat("cA", crankAngle);
  tag.setFloat("cS", crankSpeed);
  tag.setFloat("aA", armAngle);
  tag.setFloat("aS", armSpeed);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.crankAngle = tag.getFloat("cA");
  this.crankSpeed = tag.getFloat("cS");
  this.armAngle = tag.getFloat("aA");
  this.armSpeed = tag.getFloat("aS");
  }

@Override
public float getVar1()
  {  
  return armAngle;
  }

@Override
public float getVar2()
  {
  return armSpeed;
  }

@Override
public float getVar3()
  {
  return crankAngle;
  }

@Override
public float getVar4()
  {
  return crankSpeed;
  }

@Override
public float getVar5()
  {
  return 0;
  }

@Override
public float getVar6()
  {
  return 0;
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
