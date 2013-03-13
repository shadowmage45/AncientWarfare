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

import net.minecraft.world.World;

public class VehicleBallista extends VehicleBase
{

public float crankAngle = 0.f;
public float crankSpeed = 0.f;
/**
 * @param par1World
 */
public VehicleBallista(World par1World)
  {
  super(par1World);
  // TODO Auto-generated constructor stub
  }

@Override
public void onFiringUpdate()
  {
  this.firingHelper.startLaunching();
  }

@Override
public void onReloadUpdate()
  {
  
  }

@Override
public void onLaunchingUpdate()
  {
  this.firingHelper.spawnMissile(0, 0, 0);
  this.firingHelper.setFinishedLaunching();
  }

}