/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.item.ItemLoaderCore;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;

public class AmmoHwachaRocketExplosive extends Ammo
{

/**
 * @param ammoType
 */
public AmmoHwachaRocketExplosive(int ammoType)
  {
  super(ammoType);
  this.entityDamage = 4;
  this.vehicleDamage = 4;
  this.isArrow = true;
  this.isPersistent = false;
  this.isRocket = true;
  this.ammoWeight = 1.3f;
  this.renderScale = 0.2f;
  this.configName = "hwacha_rocket_explosive";
  this.iconTexture = "ammoRocketExplosive1";
  this.modelTexture = Statics.TEXTURE_PATH+"models/ammo/arrowWood.png";
  
  this.numCrafted = 8;
  this.neededResearch.add(ResearchGoalNumbers.rockets2);
  this.neededResearch.add(ResearchGoalNumbers.ballistics2);  
  this.neededResearch.add(ResearchGoalNumbers.explosives2);
  this.resources.add(new ItemStackWrapperCrafting(ItemLoaderCore.rocketCharge, 1, false, false));
  this.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.stick), 8, false, false));
  this.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.feather), 2, false, false));
  this.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.ingotIron), 1, false, false));
  this.resources.add(new ItemStackWrapperCrafting(ItemLoaderCore.explosiveCharge, 2, false, false));
  }

@Override
public void onImpactWorld(World world, float x, float y, float z, MissileBase missile, MovingObjectPosition hit)
  {
  if(!world.isRemote)
    {
    createExplosion(world, missile, x, y, z, 0.6f);
    }
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z, MissileBase missile)
  {
  if(!world.isRemote)
    {
    ent.attackEntityFrom(DamageType.causeEntityMissileDamage(missile.shooterLiving, false, true), this.getEntityDamage()); 
    ent.setFire(3);
    createExplosion(world, missile, x, y, z, 0.8f);
    }
  }

}
