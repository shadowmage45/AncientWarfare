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
package shadowmage.ancient_warfare.common.civics.types;

import java.util.List;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.civics.TECivic;

public interface ICivicType
{

/**
 * global registry number, for ease of load/reload/etc--easy access to the backing data-class through reference
 * @return
 */
int getGlobalID();

/**
 * get the size of the inventory, may be 0
 * @param level
 * @return
 */
int getInventorySize();

/**
 * get item/gui display name for this civic
 * @param level
 * @return
 */
String getDisplayName();

/**
 * get the default itemTooltip to be used with this civic * 
 * @return
 */
String getDisplayTooltip();

String getIconTexture();

String[] getIconNames();

/**
 * return a NEW itemstack for this civic
 * @return
 */
ItemStack getItemToConstruct();

/**
 * return a new/cached itemStack for display purposes
 * @return
 */
ItemStack getDisplayItem();

CivicWorkType getWorkType();

int getMaxWorkSizeWidth();
int getMaxWorkSizeHeight();
int getMaxWorkAreaCube();

int getResourceSlotSize();
List<ItemStack> getResourceItemFilters();

boolean isWorkSite();
boolean isDepository();
boolean isDwelling();

boolean addToCreativeMenu();

/**
 * if work site, get the maximum number of workers for this structure
 * if dwelling, get the max number of residents inside structure bounds
 * @param rank
 * @return
 */
int getMaxWorkers();

/**
 * return a refernece to the TE class this civic will use, to aid in dynamic construction/placement of tile entity
 * (Tile entities still need to be registered to gameRegistry, same with blocks)
 * @return
 */
Class<? extends TECivic> getTileEntityClass();

}
