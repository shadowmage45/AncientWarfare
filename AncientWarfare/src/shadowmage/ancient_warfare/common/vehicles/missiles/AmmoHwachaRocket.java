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

public class AmmoHwachaRocket extends Ammo
{

public static float burnTimeFactor = 3.f;
public static float accelerationFactor = 0.01f;

/**
 * @param ammoType
 */
public AmmoHwachaRocket(int ammoType)
  {
  super(ammoType);
  this.displayName = "Hwacha Rocket";
  this.displayTooltip = "A small self-propelled arrow with variable burn-time.";
  this.entityDamage = 6;
  this.vehicleDamage = 6;
  this.isArrow = true;
  this.isPersistent = true;
  this.isRocket = true;
  this.ammoWeight = 1.f;
  this.renderScale = 0.2f;
  this.iconTexture = "ammoRocket1";
  }

@Override
public void onImpactWorld(World world, float x, float y, float z, MissileBase missile, MovingObjectPosition hit)
  {
  
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z, MissileBase missile)
  {
  if(!world.isRemote)
    {
    ent.attackEntityFrom(DamageType.genericMissile, getEntityDamage());
    }
  }

}
