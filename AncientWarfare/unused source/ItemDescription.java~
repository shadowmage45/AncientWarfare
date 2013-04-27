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
package shadowmage.ancient_warfare.common.registry.entry;

import java.util.HashMap;

public class ItemDescription
{

public final int itemID;
private final boolean hasSubtypes;
private final HashMap<Integer, String> names = new HashMap<Integer, String>();
private final HashMap<Integer, String> descriptions = new HashMap<Integer, String>();
private final HashMap<Integer, String> tooltips = new HashMap<Integer, String>();

public ItemDescription(int id)
  {
  this.itemID = id;
  this.hasSubtypes = true;
  }

public ItemDescription(int id, String name)
  {
  this.itemID = id;
  this.hasSubtypes = false;
  this.names.put(0, name);
  this.descriptions.put(0, "");
  }

public ItemDescription(int id, String name, String description)
  {
  this.itemID = id;
  this.hasSubtypes = false;
  this.names.put(0, name);
  this.descriptions.put(0, description);
  }

public void addSubtype(int damage, String name)
  {
  this.addSubtype(damage, name, "");
  }

public void addSubtype(int damage, String name, String description)
  {
  if(!this.hasSubtypes)
    {
    return;
    }
  this.names.put(damage, name);
  this.descriptions.put(damage, description);
  }

public void setDescription(String description)
  {
  this.descriptions.put(0, description);
  }

public void setDescription(int dmg, String description)
  {
  if(this.names.containsKey(dmg))//keep names and descriptions lined up
    {
    this.descriptions.put(dmg, description);
    }  
  }

public void setTooltip(int dmg, String tooltip)
  {
  if(this.names.containsKey(dmg))
    {
    this.tooltips.put(dmg, tooltip);
    }
  }

public String getTooltip(int dmg)
  {
  if(this.tooltips.containsKey(dmg))
    {
    return this.tooltips.get(dmg);
    }
  return "";
  }

public boolean contains(int dmg)
  {
  return dmg ==0 || this.hasSubtypes && this.names.containsKey(dmg);
  }
}
