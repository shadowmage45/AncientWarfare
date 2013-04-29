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

import shadowmage.ancient_warfare.common.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class AmmoFlameShot extends Ammo
{
/**
 * @param ammoType
 * @param weight
 */
public AmmoFlameShot(int ammoType, int weight)
  {
  super(ammoType);
  this.isPersistent = false;
  this.isArrow = false;
  this.isRocket = false;
  this.isFlaming = true;
  this.ammoWeight = weight;
  this.displayName = "Fire Shot "+weight+"kg";
  this.addTooltip(weight+"kg canister of flammable liquid.");  
  float scaleFactor = weight + 45.f;
  this.renderScale = ( weight / scaleFactor ) * 2; 
  this.iconTexture = "ammoFlame1";
  this.modelTexture = Config.texturePath+"models/ammo/ammoStoneShot.png";
  }

@Override
public void onImpactWorld(World world, float x, float y, float z, MissileBase missile, MovingObjectPosition hit)
  {
  if(!world.isRemote)
    {
    int bx = (int)x;
    int by = (int)y+2;
    int bz = (int)z;
    this.igniteBlock(world, bx, by, bz,5);
    if(this.ammoWeight>=15)
      {
      this.igniteBlock(world, bx-1, by, bz,5);
      this.igniteBlock(world, bx+1, by, bz,5);
      this.igniteBlock(world, bx, by, bz-1,5);
      this.igniteBlock(world, bx, by, bz+1,5);
      }
    if(ammoWeight>=30)
      {
      this.igniteBlock(world, bx-1, by, bz-1,5);
      this.igniteBlock(world, bx-1, by, bz+1,5);
      this.igniteBlock(world, bx+1, by, bz-1,5);
      this.igniteBlock(world, bx+1, by, bz+1,5);      
      this.igniteBlock(world, bx-2, by, bz,5);
      this.igniteBlock(world, bx+2, by, bz,5);
      this.igniteBlock(world, bx, by, bz-2,5);
      this.igniteBlock(world, bx, by, bz+2,5);
      }
    if(ammoWeight>=45)
      {      
      this.igniteBlock(world, bx-1, by, bz-2, 5);
      this.igniteBlock(world, bx+1, by, bz-2, 5);
      this.igniteBlock(world, bx-1, by, bz+2, 5);
      this.igniteBlock(world, bx+1, by, bz+2, 5);      
      this.igniteBlock(world, bx-2, by, bz-1, 5);
      this.igniteBlock(world, bx-2, by, bz+1, 5);
      this.igniteBlock(world, bx+2, by, bz-1, 5);
      this.igniteBlock(world, bx+2, by, bz+1, 5);
      }
    }
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z, MissileBase missile)
  {
  if(!world.isRemote)
    {
    ent.attackEntityFrom(DamageType.fireMissile, getEntityDamage());
    ent.setFire(3);
    onImpactWorld(world, x, y, z, missile, null);
    }
  }

}
