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

public class AmmoBallistaBoltExplosive extends Ammo
{

/**
 * @param ammoType
 */
public AmmoBallistaBoltExplosive(int ammoType)
  {
  super(ammoType);
  this.ammoWeight = 2.6f;
  this.renderScale = 0.3f;
  this.vehicleDamage = 15;
  this.entityDamage = 15;
  this.isArrow = true;
  this.isRocket = false;
  this.isPersistent = false;
  this.displayName = "Explosive Ballista Bolt";  
  this.displayTooltip = "A large wooden bolt fixed with an explosive tip.";  
  this.iconTexture = "ammoArrowFlame1";
  this.modelTexture = Config.texturePath+"models/ammo/arrowWood.png";
  }

@Override
public void onImpactWorld(World world, float x, float y, float z, MissileBase missile, MovingObjectPosition hit)
  {
  if(!world.isRemote)
    {
    createExplosion(world, missile, x, y, z, 0.8f);
    }
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z, MissileBase missile)
  {
  if(!world.isRemote)
    {
    ent.attackEntityFrom(DamageType.explosiveMissile, this.getEntityDamage());    
    ent.setFire(3);
    createExplosion(world, missile, x, y, z, 1.2f);
    }
  }
}
