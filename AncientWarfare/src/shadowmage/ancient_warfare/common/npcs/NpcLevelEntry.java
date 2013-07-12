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
package shadowmage.ancient_warfare.common.npcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;

public class NpcLevelEntry
{

protected ItemStack upkeepAdditionalItem;
protected ItemStack toolStack;
private ItemStack[] armorStacks = new ItemStack[4];
protected String name;
protected String texture;
protected String tooltip;
protected int attackDamage = 4;
protected int rangedAttackDistance = 0;
protected int health = 20;
protected int inventorySize = 0;
protected int specInventorySize = 0;
protected int actionTicks = 35;
protected int upkeepCost = 8;//default one pork chop/ two apples
protected float accuracy = 1.f;
protected IAmmoType ammo;//used for archers
protected List<CivicWorkType> workTypes = new ArrayList<CivicWorkType>();
protected List<ItemStackWrapperCrafting> recipeResources = new ArrayList<ItemStackWrapperCrafting>();
protected Set<Integer> neededResearch = new HashSet<Integer>();

public NpcLevelEntry(int type, int level, String tex)
  {
  this.name = "npc."+type+"."+level;
  this.tooltip = "npc."+type+"."+level+".tooltip";
  this.texture = tex;
  }

public NpcLevelEntry(int type, int level, String tex, ItemStack tool, ItemStack[] armor)
  {
  this(type, level, tex);  
  this.setTool(tool);  
  this.setArmor(armor);    
  }

public NpcLevelEntry(int type, int level, String tex, int damage, int health, float accuracy)
  {
  this(type, level, tex);
  this.setAttackDamage(damage);
  this.setHealth(health);
  this.setAccuracy(accuracy);
  }

public NpcLevelEntry setUpkeepAdditionalItem(ItemStack item)
  {
  this.upkeepAdditionalItem = item;
  return this;
  }

public NpcLevelEntry setUpkeep(int upkeep)
  {
  this.upkeepCost = upkeep;
  return this;
  }

public NpcLevelEntry setTool(ItemStack tool)
  {
  this.toolStack = tool;
  return this;
  }

public NpcLevelEntry setArmor(ItemStack[] armor)
  {
  if(armor!=null)
    {
    this.armorStacks = armor;
    }
  return this;
  }

public NpcLevelEntry setAttackDamage(int damage)
  {
  this.attackDamage = damage;
  return this;
  }

public NpcLevelEntry setActionTicks(int ticks)
  {
  this.actionTicks = ticks;
  return this;
  }

public NpcLevelEntry setRange(int range)
  {
  this.rangedAttackDistance = range;
  return this;
  }

public NpcLevelEntry setHealth(int health)
  {
  this.health = health;
  return this;
  }

public NpcLevelEntry setInventorySize(int size)
  {
  this.inventorySize = size;
  return this;
  }

public NpcLevelEntry setSpecInventorySize(int size)
  {
  this.specInventorySize = size;
  return this;
  }

public NpcLevelEntry setAccuracy(float acc)
  {
  this.accuracy = acc;
  return this;
  }

public NpcLevelEntry setAmmoType(IAmmoType ammo)
  {
  this.ammo = ammo;
  return this;
  }

public NpcLevelEntry addTargetType(CivicWorkType... l)
  {
  for(CivicWorkType t : l)
    {
    if(t!=null && !this.workTypes.contains(t))
      {
      this.workTypes.add(t);
      }
    }
  return this;
  }

public NpcLevelEntry addNeededResearch(Integer... nums)
  {
  for(Integer i : nums)
    {
    this.neededResearch.add(i);
    }
  return this;
  }

public NpcLevelEntry addRecipeResource(ItemStack... items)
  {
  for(ItemStack item : items)
    {
    this.recipeResources.add(new ItemStackWrapperCrafting(item, false, false));
    }
  return this;
  }

public Collection<ItemStackWrapperCrafting> getNeededResources()
  {
  return this.recipeResources;
  }

public Collection<Integer> getNeededResearch()
  {
  return this.neededResearch;
  }

public String getTooltip()
  {
  return this.tooltip;
  }

/**
 * @return the armorStacks
 */
public ItemStack[] getArmorStacks()
  {
  return armorStacks;
  }

}
