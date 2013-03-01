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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;

public class AmmoRegistry
{
private AmmoRegistry(){}

private static AmmoRegistry INSTANCE;

private Map<Integer, IAmmoType> ammoInstances = new HashMap<Integer, IAmmoType>();
private Map<IAmmoType, Class<? extends Entity>> ammoEntities = new HashMap<IAmmoType, Class<? extends Entity>>();

public static AmmoRegistry instance()
  {
  if(INSTANCE==null){INSTANCE = new AmmoRegistry();}
  return INSTANCE;
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

public void registerAmmoTypeWithItem(IAmmoType ammo, Item item, int itemDamage, Class <? extends Entity> missileEntity)
  {
  if(!DescriptionRegistry.instance().contains(item))
    {
    DescriptionRegistry.instance().registerItemWithSubtypes(item.itemID);
    }  
  DescriptionRegistry.instance().addSubtypeToItem(item.itemID, itemDamage, ammo.getDisplayName());
  DescriptionRegistry.instance().setTooltip(item.itemID, itemDamage, ammo.getDisplayTooltip());
  this.registerAmmoType(ammo, missileEntity);
  }

public void registerAmmoType(IAmmoType ammo, Class <? extends Entity> clz)
  {
  if(ammo==null)
    {
    return;
    }
  int type = ammo.getAmmoType();
  if(!this.ammoInstances.containsKey(type))
    {
    this.ammoInstances.put(type, ammo);
    AWEntityRegistry.registerEntity(clz, ammo.getEntityName(), 170, 5, true);
    this.ammoEntities.put(ammo, clz);
    }
  else
    {
    Config.logError("Attempt to register a duplicate ammo type for number: "+type);
    Config.logError("Ammo attempting to being registered: "+ammo.getDisplayName());
    }  
  }

static int nextAmmoType = 0;
/**
 * get the next open global ammo number type (searches up to 400 indices)
 * @return available index, or -1 if none
 */
public int getAvailableAmmoType()
  {
  int id = nextAmmoType;
  nextAmmoType++;
  if(this.ammoInstances.containsKey(id))
    {
    Config.logError("Error while attempting to generate next valid ammo type ID: "+id+" Id already exists.");    
    }
  return id;
  }

public Entity getAmmoEntityFor(int type, World world)
  {
  if(this.ammoInstances.containsKey(type))
    {
    IAmmoType ammo = this.ammoInstances.get(type);
    if(this.ammoEntities.containsKey(ammo))
      {
      try
        {
        return this.ammoEntities.get(ammo).getDeclaredConstructor(World.class).newInstance(world);
        } 
      catch (InstantiationException e)
        {
        e.printStackTrace();
        } 
      catch (IllegalAccessException e)
        {
        e.printStackTrace();
        } 
      catch (IllegalArgumentException e)
        {
        e.printStackTrace();
        } 
      catch (InvocationTargetException e)
        {
        e.printStackTrace();
        } 
      catch (NoSuchMethodException e)
        {
        e.printStackTrace();
        } 
      catch (SecurityException e)
        {
        e.printStackTrace();
        }
      }
    }
  return null;
  }

}
