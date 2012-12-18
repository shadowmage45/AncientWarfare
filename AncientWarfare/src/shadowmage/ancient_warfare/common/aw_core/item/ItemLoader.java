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
package shadowmage.ancient_warfare.common.aw_core.item;

import net.minecraft.item.Item;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.registry.DescriptionRegistry;
import shadowmage.ancient_warfare.common.aw_core.registry.VehicleAmmoRegistry;
import shadowmage.ancient_warfare.common.aw_core.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.aw_core.registry.entry.ItemIDPair;
import shadowmage.ancient_warfare.common.aw_core.registry.entry.VehicleAmmo;
import shadowmage.ancient_warfare.common.aw_core.registry.entry.VehicleUpgrade;

public class ItemLoader
{

public static AWItemBase vehicleUpgrade = new AWItemBase(Config.getItemID("itemMulti.vehicleUpgrade", 13001, "Base item for all vehicle upgrades"),true);
public static AWItemBase vehicleAmmo = new AWItemBase(Config.getItemID("itemMulti.vehicleAmmo", 13002, "Base item for all vehicle ammunition types"),true);

private static ItemLoader INSTANCE;
private ItemLoader(){}
public static ItemLoader instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new ItemLoader();
    }
  return INSTANCE;
  }


/**
 * initial load, called during pre-init from mod core file
 */
public void load()
  {
  
  }

/**
 * special registerUpgrade method, directly registers a new upgrade using the vehicleUpgrade item.  calls all necessary calls
 * for languageRegistry, descriptionRegistry, etc----null upgrades are not registered
 * @param dmg
 * @param type
 * @param upgrade
 */
public void registerVehicleUpgradeItem(int dmg, int type, VehicleUpgrade upgrade)
  {
  if(upgrade!=null)
    {
    this.registerItem(vehicleUpgrade, dmg, upgrade.getUpgradeDisplayName());
    VehicleUpgradeRegistry.instance().registerUpgrade(dmg, type, upgrade);
    }
  }

/**
 * special itemRegister function that will create the necessary items and add them to all registers
 * @param dmg
 * @param type
 * @param name
 * @param displayName
 */
public void registerVehicleAmmoItem(int dmg, int type, String name, String displayName)
  {
  ItemIDPair pair = new ItemIDPair(vehicleAmmo.shiftedIndex, dmg);
  VehicleAmmo entry = new VehicleAmmo(pair, name, displayName,type);
  this.registerItem(pair, displayName);
  VehicleAmmoRegistry.instance().registerAmmoType(entry);
  }

public void registerItem(ItemIDPair id, String name)
  {
  DescriptionRegistry.instance().registerItem(id, name);
  }

public void registerItem(int id, int dmg, String name)
  {
  DescriptionRegistry.instance().registerItem(id, dmg, name);
  }

public void registerItem(Item item, int dmg, String name)
  {
  this.registerItem(item.shiftedIndex, dmg, name);
  }


}
