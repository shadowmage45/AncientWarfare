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
package shadowmage.ancient_warfare.common.aw_core.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.aw_core.registry.entry.ItemDescription;
import shadowmage.ancient_warfare.common.aw_core.registry.entry.ItemIDPair;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * @author Shadowmage
 *
 */
public class DescriptionRegistry
{


private static DescriptionRegistry INSTANCE;
private DescriptionRegistry(){}

public static DescriptionRegistry instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new DescriptionRegistry();
    }
  return INSTANCE;
  }

private static Map<ItemIDPair,ItemDescription> descriptions = new HashMap<ItemIDPair,ItemDescription>();

public void registerItem(Item item, String displayName)
  {
  this.registerItem(item.shiftedIndex, 1, displayName);
  }

public void registerItem(ItemStack stack, String displayName)
  {
  if(stack!=null)
    {
    this.registerItem(stack.itemID, stack.getItemDamage(), displayName);
    }
  }

public void registerItem(int id, int dmg, String displayName)
  {
  this.registerItem(new ItemIDPair(id,dmg), displayName);
  }

/**
 * final ID register call, all others are funneled through here
 * @param id
 * @param displayName
 */
public void registerItem(ItemIDPair id, String displayName)
  {
  if(!this.contains(id.itemID, id.dmg))
    {
    if(displayName==null)
      {
      displayName = "";
      }
    this.descriptions.put(id, new ItemDescription(displayName, id));
    LanguageRegistry.addName(new ItemStack(id.itemID,1,id.dmg), displayName);
    }
  }

/**
 * stack version of addDescription
 * @param stack
 * @param description
 */
public void addDescription(ItemStack stack, String description)
  {
  if(this.contains(stack))
    {   
    this.descriptions.get(new ItemIDPair(stack.itemID, stack.getItemDamage())).setDescription(description);
    }    
  }

/**
 * item version of addDescription
 * @param item
 * @param description
 */
public void addDescription(Item item, String description)
  {
  this.addDescription(item.shiftedIndex, 1, description);
  }

/**
 * final addDescription call, all others end up here
 * @param id
 * @param dmg
 * @param desc
 */
public void addDescription(int id, int dmg, String desc)
  {
  if(this.contains(id, dmg))
    {
    this.getEntryFor(id, dmg).setDescription(desc);
    }
  }

/**
 * return an entry for an id/dmg pair
 * @param id
 * @param dmg
 * @return
 */
public ItemDescription getEntryFor(int id, int dmg)
  {
  for(ItemIDPair desc : this.descriptions.keySet())
    {
    if(desc.equals(id, dmg))
      {
      return this.descriptions.get(desc);
      }
    }
  return null;
  }

/**
 * return description for an itemStack, dmg sensitive
 * @param stack
 * @return
 */
public String getDescriptionFor(ItemStack stack)
  {  
  return stack==null? "" : this.getDescriptionFor(stack.itemID, stack.getItemDamage());
  }

/**
 * return description for an item, using dmg1
 * @param item
 * @return
 */
public String getDescriptionFor(Item item)
  {
  return this.getDescriptionFor(item.shiftedIndex, 1);
  }

/**
 * return description for item id/dmg pair
 * @param id
 * @param dmg
 * @return
 */
public String getDescriptionFor(int id, int dmg)
  {
  if(this.contains(id, dmg))
    {
    return this.getEntryFor(id, dmg).description;        
    }
  return "";
  }

/**
 * get tooltip for an item, in list format
 * @param id
 * @param dmg
 * @return
 */
public List getTooltipFor(int id, int dmg)
  {
  ItemDescription entry = this.getEntryFor(id, dmg);  
  if(entry!=null)
    {
    return entry.getTooltip();
    }
  return null;
  }

public boolean contains(ItemStack stack)
  {
  if(stack==null)
    {
    return false;
    }
  return this.contains(stack.itemID, stack.getItemDamage());
  }

public boolean contains(Item item)
  {
  return this.contains(item.shiftedIndex, 1);
  }

public boolean contains(int id, int dmg)
  {
  if(this.getEntryFor(id, dmg)!=null)
    {
    return true;
    }
  return false;
  }

public boolean contains(ItemIDPair pair)
  {
  if(this.getEntryFor(pair.itemID, pair.dmg)!=null)
    {
    return true;
    }
  return false;
  }
}
