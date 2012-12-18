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
package shadowmage.ancient_warfare.common.aw_core.registry.entry;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.aw_vehicles.VehicleBase;

public abstract class VehicleUpgrade
{

boolean directlyEffectsVehicle = false;

public VehicleUpgrade()
  {
  
  }

/**
 * apply the effects of this upgrade to the passed-in vehicle
 * @param vehicle
 */
public abstract void applyUpgradeEffects(VehicleBase vehicle);

/**
 * get the full display name for this item, this name will be registered
 * with languageRegistry
 * @return
 */
public abstract String getUpgradeDisplayName();

/**
 * get the internal (short) name for this upgrade, must be unique or will
 * cause lookup conflicts
 * @return
 */
public abstract String getUpgradeName();

}
