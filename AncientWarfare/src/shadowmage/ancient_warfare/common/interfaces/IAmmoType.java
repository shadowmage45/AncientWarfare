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
package shadowmage.ancient_warfare.common.interfaces;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

/**
 * interfaced used by ammo types, implemented for possible future API use and 
 * ease of future expansion without necessitating extension/inheritance
 * @author Shadowmage
 */
public interface IAmmoType
{

/**
 * get this ammo types global reference/ID number (used by vehicles to determine usability)
 * @return
 */
int getAmmoType();
String getDisplayName();
String getDisplayTooltip();
int getItemID();
int getItemMeta();
ItemStack getDisplayStack();

boolean isAmmoValidFor(VehicleBase vehicle);//can be used for per-upgrade compatibility.  vehicle will check this before firing or adding ammo to the vehicle
boolean isRocket();//determines flight characteristics
boolean isPersistent();//should die on impact, or stay on ground(arrows)
float getWeightFactor();// | 0 <-> 1.f |  factor applied to initial velocity

void onImpactWorld(World world, float x, float y, float z);//called when the entity impacts a world block
void onImpactEntity(World world, Entity ent, float x, float y, float z);//called when the entity impacts another entity

}
