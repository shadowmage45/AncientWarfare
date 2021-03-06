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
package shadowmage.ancient_warfare.common.vehicles.upgrades;

import java.util.Collection;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public interface IVehicleUpgradeType
{

public abstract int getUpgradeGlobalTypeNum();
public abstract String getDisplayName();
public abstract String getDisplayTooltip();
public abstract String getIconTexture();
public abstract void applyVehicleEffects(VehicleBase vehicle);

public abstract ItemStack getUpgradeStack(int qty);

public abstract ResourceListRecipe constructRecipe();
public abstract Collection<Integer> getNeededResearch();

}
