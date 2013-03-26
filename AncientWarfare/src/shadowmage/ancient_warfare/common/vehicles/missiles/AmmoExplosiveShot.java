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
import net.minecraft.world.World;

public class AmmoExplosiveShot extends Ammo
{

boolean bigExplosion;
/**
 * @param ammoType
 */
public AmmoExplosiveShot(int ammoType, int weight, boolean bigExplosion)
  {
  super(ammoType);
  String prefix = bigExplosion ? "High Explosive" : "Explosive";
  this.displayName = prefix + " shot " + weight +"kg";
  this.displayTooltip = weight+"kg of "+prefix+" powder charge in a launchable case.";
  this.ammoWeight = weight;
  this.bigExplosion = bigExplosion;
  this.entityDamage = weight;
  this.vehicleDamage = weight;
  }

@Override
public void onImpactWorld(World world, float x, float y, float z, MissileBase missile)
  {
  
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z, MissileBase missile)
  {
  
  }

}
