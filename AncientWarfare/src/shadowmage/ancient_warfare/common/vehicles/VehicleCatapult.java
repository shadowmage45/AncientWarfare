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
import shadowmage.ancient_warfare.common.utils.Trig;
import net.minecraft.world.World;

public class VehicleCatapult extends VehicleBase
{

public float armAngle = 0.f;
public float armSpeed = 0.f;
public float crankAngle = 0.f;
public float crankSpeed = 0.f;

/**
 * @param par1World
 */
public VehicleCatapult(World par1World)
  {
  super(par1World);
  this.yOffset = 0;
  this.ammoHelper.addUseableAmmo(AmmoRegistry.ammoArrow);
  }

@Override
public boolean isMountable()
  {
  return true;
  }

@Override
public void onFiringUpdate()
  {
  this.firingHelper.launchMissile();
  }

@Override
public void onReloadUpdate()
  {
  
  }

@Override
public void onUpdate()
  {
  super.onUpdate();
  this.armAngle = 90- this.firingHelper.turretPitch -7;
  }

@Override
public float getHorizontalMissileOffset(float pitch, float yaw)
  {
  return 0;
  }

@Override
public float getVerticalMissileOffset(float pitch, float yaw)
  {
  return 3* Trig.sinDegrees(90-pitch) + 0.4f;
  }

@Override
public float getForwardsMissileOffset(float pitch, float yaw)
  {
  return -3* Trig.cosDegrees(90-pitch);
  }

}
