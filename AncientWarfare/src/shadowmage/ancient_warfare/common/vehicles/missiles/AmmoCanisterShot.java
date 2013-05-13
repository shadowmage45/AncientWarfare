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

public class AmmoCanisterShot extends Ammo
{

/**
 * @param ammoType
 */
public AmmoCanisterShot(int ammoType, int weight)
  {
  super(ammoType);
  this.ammoWeight = weight;
  this.displayName = "Canister Shot "+weight+"kg";
  this.addTooltip("Spawns "+weight+" submunitions on impact");
  float scaleFactor = weight + 45.f;
  this.renderScale = ( weight / scaleFactor ) * 2; 
  this.iconTexture = "ammoCluster1";
  this.modelTexture = Config.texturePath+"models/ammo/ammoStoneShot.png"; 
  this.entityDamage = 8;
  this.vehicleDamage = 8;
  }

@Override
public void onImpactWorld(World world, float x, float y, float z, MissileBase missile, MovingObjectPosition hit)
  {   
  if(!world.isRemote)
    {
    double px = hit.hitVec.xCoord - missile.motionX;
    double py = hit.hitVec.yCoord - missile.motionY;
    double pz = hit.hitVec.zCoord - missile.motionZ;
    spawnGroundBurst(world, (float)px, (float)py, (float)pz, 10, Ammo.ammoBallIronShot, (int)ammoWeight, 35, hit.sideHit, missile.shooterLiving);
    }
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z, MissileBase missile)
  {
  if(!world.isRemote)
    {
    spawnAirBurst(world, (float)ent.posX, (float)ent.posY+ent.height, (float)ent.posZ, 10, Ammo.ammoBallIronShot, (int)ammoWeight, missile.shooterLiving);
    }
  }

}
