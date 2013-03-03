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
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public abstract class AmmoBase implements IAmmoType
{

private ItemStack ammoStack;
int ammoType;

public AmmoBase(int ammoType)
  {
  this.ammoStack = new ItemStack(this.getItemID(), 1, this.getItemMeta());
  this.ammoType = ammoType;
  }

@Override
public int getAmmoType()
  {
  return this.ammoType;
  }

@Override
public ItemStack getDisplayStack()
  {
  return this.ammoStack;
  }

@Override
public ItemStack getAmmoStack(int qty)
  {
  return new ItemStack(this.getItemID(), qty, this.getItemMeta());
  }

/**
 * override to implement upgrade-specific ammo use.  this method will be checked before a vehicle will accept ammo into its bay, or fire ammo from its bay
 */
@Override
public boolean isAmmoValidFor(VehicleBase vehicle)
  {
  return true;
  }

}
