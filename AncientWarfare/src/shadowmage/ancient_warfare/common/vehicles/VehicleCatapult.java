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
package shadowmage.ancient_warfare.common.vehicles;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleRegistry;
import shadowmage.ancient_warfare.common.utils.Trig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class VehicleCatapult extends VehicleBase
{

public float armAngle = -7.f;
public float armSpeed = 0.f;
public float crankAngle = 0.f;
public float crankSpeed = 0.f;

/**
 * @param par1World
 */
public VehicleCatapult(World par1World)
  {
  super(par1World);  
  }

@Override
public void onFiringUpdate()
  {  
  if(!worldObj.isRemote)
    {
    Config.logDebug("onFiringUpdate");
    }
  float prevAngle = this.armAngle;
  this.armAngle += 87.f/20;
  if(this.armAngle>=70)
    {
    this.firingHelper.startLaunching();
    this.armAngle = 70.f;
    }
  this.armSpeed = this.armAngle - prevAngle;
  }

@Override
public void onLaunchingUpdate()
  {  
  if(!worldObj.isRemote)
    {
    Config.logDebug("onLaunchingUpdate");
    }
  for(int i = 0; i <10; i++)
    {
    this.firingHelper.spawnMissile(0, 0, 0);
    }
  
  this.firingHelper.setFinishedLaunching();
  }

@Override
public void onReloadUpdate()
  {
  if(!worldObj.isRemote)
    {
    Config.logDebug("onReloadUpdate");
    }
  float prevAngle = this.armAngle;
  this.armAngle -= 87 / (float)this.reloadTimeCurrent;
  if(this.armAngle <= -7)
    {
    this.armAngle = -7;
    }
  this.armSpeed =this.armAngle - prevAngle;
  }

@Override
public void onUpdate()
  {
  super.onUpdate();
  //this.armAngle = 90- this.turretPitch -7;
  }


}
