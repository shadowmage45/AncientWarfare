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

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;

public class AmmoNapalmShot extends Ammo
{

/**
 * @param ammoType
 */
public AmmoNapalmShot(int ammoType, int weight)
  {
  super(ammoType);
  this.displayName = "Napalm Shot " + weight +"kg";
  this.addTooltip(weight+"kg of flammable semi-liquid gel.");
  this.ammoWeight = weight;
  this.entityDamage = weight;
  this.vehicleDamage = weight;
  float scaleFactor = weight + 45.f;
  this.renderScale = ( weight / scaleFactor ) * 2; 
  this.iconTexture = "ammoNapalm1";
  this.modelTexture = Config.texturePath+"models/ammo/ammoStoneShot.png";
  this.isFlaming = true;
  }

@Override
public void onImpactWorld(World world, float x, float y, float z, MissileBase missile, MovingObjectPosition hit)
  {
  int bx = MathHelper.floor_float(x);
  int by = MathHelper.floor_float(y);  
  int bz = MathHelper.floor_float(z);  
  setBlockToLava(world, bx, by, bz, 5);
  double dx = missile.motionX;
  double dz = missile.motionZ;  
  if(Math.abs(dx)>Math.abs(dz))
    {
    dz = 0;
    }
  else
    {
    dx = 0;
    }
  dx = dx<0 ? -1 : dx > 0 ? 1 : dx;
  dz = dz<0 ? -1 : dz > 0 ? 1 : dz;
  if(ammoWeight>=15)//set the 'forward' block to lava as well
    {
    setBlockToLava(world, bx+ (int)dx, by, bz+(int)dz, 5);
    }
  if(ammoWeight>=30)//set the 'rear' block to lava as well
    {
    setBlockToLava(world, bx-(int)dx, by, bz-(int)dz, 5);
    }
  if(ammoWeight>=45)
    {
    if(dx==0)//have already done Z's
      {
      setBlockToLava(world, bx+1, by, bz, 5);
      setBlockToLava(world, bx-1, by, bz, 5);
      }
    else
      {
      setBlockToLava(world, bx, by, bz+1, 5);
      setBlockToLava(world, bx, by, bz-1, 5);
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
    onImpactWorld(world, x, (float)ent.posY, z, missile, null);
    }
  }

}
