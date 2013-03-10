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
package shadowmage.ancient_warfare.common.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.item.AWItemBase;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.missiles.Ammo;
import shadowmage.ancient_warfare.common.missiles.MissileBase;

public class AmmoRegistry
{


private AmmoRegistry(){}
private static AmmoRegistry INSTANCE;

private Map<Integer, IAmmoType> ammoInstances = new HashMap<Integer, IAmmoType>();
//private Map<Integer, IAmmoType> itemDamageMap = new HashMap<Integer, IAmmoType>();

public static AmmoRegistry instance()
  {
  if(INSTANCE==null){INSTANCE = new AmmoRegistry();}
  return INSTANCE;
  }

public void registerAmmoTypes()
  {
  AWEntityRegistry.registerEntity(MissileBase.class, "AWMissileBase", 165, 5, true);  
  
  /**
   * debug..these will need to use the itemRegistry method..
   */
  this.registerAmmoTypeWithItem(Ammo.ammoArrow);
  this.registerAmmoTypeWithItem(Ammo.ammoStoneShot);
  }

/**
 * used by structure gen to fill get ammo types to fill vehicles with
 * @param type
 * @return
 */
public IAmmoType getAmmoEntry(int type)
  {
  return this.ammoInstances.get(type);
  }

public void registerAmmoTypeWithItem(IAmmoType ammo)
  {
  AWItemBase item = ItemLoader.ammoItem; 
  ItemLoader.instance().addSubtypeToItem(item, ammo.getAmmoType(), ammo.getDisplayName(), ammo.getDisplayTooltip()); 
  this.registerAmmoType(ammo);
  }

public void registerAmmoType(IAmmoType ammo)
  {
  if(ammo==null)
    {
    return;
    }
  int type = ammo.getAmmoType();
  if(!this.ammoInstances.containsKey(type))
    {
    this.ammoInstances.put(type, ammo);
    }
  else
    {
    Config.logError("Attempt to register a duplicate ammo type for number: "+type);
    Config.logError("Ammo attempting to being registered: "+ammo.getDisplayName());
    }  
  }

public IAmmoType getAmmoForStack(ItemStack stack)
  {
  if(stack==null || stack.itemID != ItemLoader.ammoItem.itemID)
    {
    return null;
    }
  return this.ammoInstances.get(stack.getItemDamage());
  }

}
