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
package shadowmage.ancient_warfare.common.vehicles.missiles;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class AmmoHwachaRocketAirburst extends Ammo
{

/**
 * @param ammoType
 */
public AmmoHwachaRocketAirburst(int ammoType)
  {
  super(ammoType);
  this.displayName = "Hwacha Airburst Rocket";
  this.displayTooltip = "Delivers a burst of Stone Ball Shot over the heads of the enemy.";
  this.entityDamage = 0;
  this.vehicleDamage = 0;
  this.isArrow = true;
  this.isPersistent = false;
  this.isRocket = true;
  this.isProximityAmmo = true;
  this.groundProximity = 12.f;
  this.entityProximity = 10f;
  this.ammoWeight = 1.4f;
  this.renderScale = 0.2f;
  }

@Override
public void onImpactWorld(World world, float x, float y, float z, MissileBase missile, MovingObjectPosition hit)
  {
  if(!world.isRemote)
    {
    this.spawnAirBurst(world, x, y, z, 10, Ammo.ammoBallShot, 4);
    missile.setDead();
    }
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z, MissileBase missile)
  {
  if(!world.isRemote)
    {
    this.spawnAirBurst(world, x, y, z, 10, Ammo.ammoBallShot, 4);
    missile.setDead();
    }
  }

}
