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

import java.util.ArrayList;
import java.util.List;

public class ItemDescription
{

public ItemIDPair itemID;
public String displayName;
public String description = "";
public List usedToCraft = new ArrayList<String>();
public List usedIn = new ArrayList<String>();//what this item is used IN, if it is a module type item (armor, upgrades, ammo)

public ItemDescription(String name, ItemIDPair itemID)
  {
  this.displayName = name;
  this.itemID = itemID;
  }

public ItemDescription setDescription(String desc)
  {
  this.description = desc;
  return this;
  }

public void addCraft(String in)
  {
  this.usedToCraft.add(in);
  }

public void addUsedIn(String in)
  {
  this.usedIn.add(in);
  }

public List getTooltip()
  {
  ArrayList<String> tooltip = new ArrayList<String>();
  //TODO
  return tooltip;
  }



/**
 * return the description for this entry as a series of strings formatted for length
 * @param len 
 * @return
 */
public List<String> getFormattedDescription(int charLen)
  {
  //TODO
  return null;
  }

}
