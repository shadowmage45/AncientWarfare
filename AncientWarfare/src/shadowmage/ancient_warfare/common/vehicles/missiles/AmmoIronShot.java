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

public class AmmoIronShot extends Ammo
{

/**
 * @param ammoType
 */
public AmmoIronShot(int ammoType, int weight, int damage)
  {
  super(ammoType);
  this.ammoWeight = weight;
  this.displayName = "Iron Shot "+weight+"kg";
  this.displayTooltip = weight+"kg rough iron shot.";
  this.entityDamage = damage;
  this.vehicleDamage = damage;
  float scaleFactor = weight + 45.f;
  this.renderScale = ( weight / scaleFactor ) * 2; 
  this.iconTexture = "ammoStone1";
  }

@Override
public void onImpactWorld(World world, float x, float y, float z, MissileBase missile, MovingObjectPosition hit)
  {
  if(ammoWeight>=10 && !world.isRemote)
    {
    int bx = (int)x;
    int by = (int)y;
    int bz = (int)z;
    this.breakBlockAndDrop(world, bx, by, bz);    
    if(ammoWeight>=15)
      {
      this.breakBlockAndDrop(world, bx, by-1, bz);
      this.breakBlockAndDrop(world, bx-1, by, bz);
      this.breakBlockAndDrop(world, bx+1, by, bz);
      this.breakBlockAndDrop(world, bx, by, bz-1);
      this.breakBlockAndDrop(world, bx, by, bz+1);
      }
    if(ammoWeight>=25)
      {
      this.breakBlockAndDrop(world, bx-1, by, bz-1);
      this.breakBlockAndDrop(world, bx+1, by, bz-1);
      this.breakBlockAndDrop(world, bx-1, by, bz+1);
      this.breakBlockAndDrop(world, bx+1, by, bz+1);
      }
    }
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z, MissileBase missile)
  {
  if(!world.isRemote)
    {
    ent.attackEntityFrom(DamageType.genericMissile, this.getEntityDamage());
    }
  }

}
