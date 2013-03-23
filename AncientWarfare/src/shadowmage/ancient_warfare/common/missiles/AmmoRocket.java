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
package shadowmage.ancient_warfare.common.missiles;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class AmmoRocket extends Ammo
{

public static float burnTimeFactor = 1.f;
public static float accelerationFactor = 0.05f;

/**
 * @param ammoType
 */
public AmmoRocket(int ammoType)
  {
  super(ammoType);
  }

@Override
public String getEntityName()
  {
  return "AW.Rocket";
  }

@Override
public String getDisplayName()
  {
  return "Hwacha Rocket";
  }

@Override
public String getDisplayTooltip()
  {
  return "Variable burn-time self-propelled arrow.";
  }

@Override
public String getModelTexture()
  {
  return "foo.png";
  }

@Override
public boolean updateAsArrow()
  {
  return true;
  }

@Override
public boolean isRocket()
  {
  return true;
  }

@Override
public boolean isPersistent()
  {
  return true;
  }

@Override
public float getDragFactor()
  {
  return 0;
  }

@Override
public float getAmmoWeight()
  {
  return 10;
  }

@Override
public void onImpactWorld(World world, float x, float y, float z)
  {
  
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z)
  {
  
  }

}
