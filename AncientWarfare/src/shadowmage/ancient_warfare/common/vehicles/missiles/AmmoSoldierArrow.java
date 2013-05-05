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

public class AmmoSoldierArrow extends Ammo
{

public AmmoSoldierArrow(int ammoType, int damage, boolean flaming)
  {
  super(ammoType);
  this.ammoWeight = 1.f;
  this.renderScale = 0.125f;
  this.vehicleDamage = damage;
  this.entityDamage = damage;
  this.isArrow = true;
  this.isRocket = false;
  this.isPersistent = true;
  this.isFlaming = flaming;  
  String prefix = flaming? "Small Flaming " : "Small ";
  String suffix = damage <= 5? "Wooden Arrow" : "Iron Arrow";
  this.displayName = prefix+suffix;  
  this.addTooltip("A smaller arrow designed for use in bows.");  
  
  if(flaming)
    {
    this.iconTexture = "ammoArrowFlame1";
    }
  else
    {
    this.iconTexture = "ammoArrow1";
    }  
  if(damage<=5)
    {
    this.modelTexture = Config.texturePath+"models/ammo/arrowWood.png";
    }
  else
    {
    this.modelTexture = Config.texturePath+"models/ammo/arrowIron.png";    
    }
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
    ent.attackEntityFrom(DamageType.genericMissile, this.getEntityDamage());  
    }
  }
}
