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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.AWItemBase;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import shadowmage.ancient_warfare.common.vehicles.missiles.Ammo;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;
import shadowmage.ancient_warfare.common.vehicles.missiles.MissileBase;

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
  for(Ammo ammo : Ammo.ammoTypes)
    {
    if(ammo!=null)
      {
      this.registerAmmoTypeWithItem(ammo);
      }
    }
//  this.registerAmmoTypeWithItem(Ammo.ammoStoneShot10);
//  this.registerAmmoTypeWithItem(Ammo.ammoStoneShot25);
//  this.registerAmmoTypeWithItem(Ammo.ammoStoneShot50);
//  this.registerAmmoTypeWithItem(Ammo.ammoArrow);
//  this.registerAmmoTypeWithItem(Ammo.ammoRocket);
  }

public List<IAmmoType> getAmmoTypes()
  {
  List<IAmmoType> ammosList = new ArrayList<IAmmoType>();
  for(Integer key : this.ammoInstances.keySet())
    {
    IAmmoType t = this.ammoInstances.get(key);
    if(t!=null)
      {
      ammosList.add(t);
      }
    }
  return ammosList;
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
  List<String> tips = ammo.getDisplayTooltip();  
  Description d = ItemLoader.instance().addSubtypeInfoToItem(item, ammo.getAmmoType(), ammo.getDisplayName());
  for(String tip : tips)
    {
    d.addTooltip(tip, ammo.getAmmoType());
    }
  d.addTooltip("Weight: "+ammo.getAmmoWeight(), ammo.getAmmoType());
  d.addTooltip("Entity Damage: "+ammo.getEntityDamage(), ammo.getAmmoType());
  d.addTooltip("Vehicle Damage: "+ammo.getVehicleDamage(), ammo.getAmmoType());
  if(ammo.isFlaming())
    {
    d.addTooltip("Flaming ammunition, ignites targets when hit", ammo.getAmmoType());
    }
  if(ammo.isProximityAmmo())
    {
    d.addTooltip("Proximity ammunition, detonates in proximity to targets", ammo.getAmmoType());
    }
  if(ammo.isPenetrating())
    {
    d.addTooltip("Penetrating ammunition, does not stop on impact", ammo.getAmmoType());
    }
  if(ammo.getSecondaryAmmoType() != null && ammo.getSecondaryAmmoTypeCount()>0)
    {
    d.addTooltip("Cluster ammunition, spawns "+ammo.getSecondaryAmmoTypeCount()+" submunitions", ammo.getAmmoType());
    IAmmoType t = ammo.getSecondaryAmmoType();
    d.addTooltip("Submunition Entity Damage: "+t.getEntityDamage(), ammo.getAmmoType());
    d.addTooltip("Submunition Vehicle Damage: "+t.getVehicleDamage(), ammo.getAmmoType());
    }
  d.addDisplayStack(ammo.getDisplayStack());
  d.setIconTexture(ammo.getIconTexture(), ammo.getAmmoType());
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
