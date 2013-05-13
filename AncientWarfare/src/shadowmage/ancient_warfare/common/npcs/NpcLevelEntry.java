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
import java.util.List;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;

public class NpcLevelEntry
{

protected ItemStack upkeepAdditionalItem;
protected ItemStack toolStack;
private ItemStack[] armorStacks = new ItemStack[4];
protected String name;
protected String texture;
protected int attackDamage = 4;
protected int rangedAttackDistance = 0;
protected int health = 20;
protected int inventorySize = 9;
protected int specInventorySize = 1;
protected int actionTicks = 35;
protected int upkeepCost = 8;//default one pork chop/ two apples
protected float accuracy = 1.f;
protected IAmmoType ammo;//used for archers
protected List<CivicWorkType> workTypes = new ArrayList<CivicWorkType>();

public NpcLevelEntry(String name, String tex)
  {
  this.name = name;
  this.texture = tex;
  }

public NpcLevelEntry(String name, String tex, ItemStack tool, ItemStack[] armor)
  {
  this(name, tex);  
  this.setTool(tool);  
  this.setArmor(armor);    
  }

public NpcLevelEntry(String name, String tex, int damage, int health, float accuracy)
  {
  this(name, tex);
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

/**
 * @return the armorStacks
 */
public ItemStack[] getArmorStacks()
  {
  return armorStacks;
  }

}
