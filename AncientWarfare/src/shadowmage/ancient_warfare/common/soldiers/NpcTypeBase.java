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
package shadowmage.ancient_warfare.common.soldiers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.soldiers.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;

public abstract class NpcTypeBase implements INpcType
{

public static NpcTypeBase [] npcTypes = new NpcTypeBase[256];

protected int npcType;
private int numOfLevels = 0;
protected String displayName = "AW.Npc";
private List<String> levelNames = new ArrayList<String>();
protected String tooltip = "AW.Npc.Tooltip";
private List<String> displayTexture = new ArrayList<String>();
protected int maxHealth = 20;
protected int inventorySize = 0;
protected boolean isCombatUnit = false;
protected boolean isVanillaVillager = false;
protected float rangedAttackDistance = 0.f;

protected List<ItemStack> toolStacks = new ArrayList<ItemStack>();
protected List<ItemStack[]> armorStacks = new ArrayList<ItemStack[]>();

public NpcTypeBase(int type)
  {
  this.npcType = type;
  if(type>=0 && type<npcTypes.length)
    {
    npcTypes[type] = this;
    }
  }

@Override
public int getGlobalNpcType()
  {
  return npcType;
  }

@Override
public int getNumOfLevels()
  {
  return numOfLevels;
  }

@Override
public String getDisplayName()
  {
  return displayName;
  }

@Override
public float getRangedAttackDistance(int level)
  {
  return rangedAttackDistance;
  }

@Override
public String getLevelName(int level)
  {
  if(level<0 && level >=this.levelNames.size())
    {
    return "no level name";
    }
  return this.levelNames.get(level);
  }

@Override
public String getDisplayTooltip()
  {
  return tooltip;
  }

@Override
public String getDisplayTexture(int level)
  {
  if(level>=0 && level< this.displayTexture.size())
    {
    return displayTexture.get(level);
    }
  return "foo.png";
  }

public void addLevel(String name, String tex, ItemStack toolStack, ItemStack[] armorStacks)
  {
  this.levelNames.add(name);
  this.displayTexture.add(tex);
  this.toolStacks.add(toolStack);
  if(armorStacks==null)
    {
    armorStacks = new ItemStack[4];
    }
  this.armorStacks.add(armorStacks);
  this.numOfLevels++;  
  }

@Override
public int getMaxHealth(int level)
  {
  return maxHealth;
  }

@Override
public boolean isCombatUnit()
  {
  return isCombatUnit;
  }

@Override
public int getInventorySize(int level)
  {
  return inventorySize;
  }

@Override
public ItemStack getTool(int level)
  {
  if(level>=0 && level<this.toolStacks.size())
    {
    return this.toolStacks.get(level);
    }
  return null;
  }

@Override
public ItemStack[] getArmor(int level)
  {
  if(level>=0 && level<this.toolStacks.size())
    {    
    return this.armorStacks.get(level);
    }
  return new ItemStack[4];
  }

/**
 * overridable helper method to be used during setting of npc constructor info
 * @param level
 * @return
 */
protected ItemStack getToolStack(int level)
  {
  return null;
  }

/**
 * overridable helper method to be used during setting of npc constructor info
 * @param level
 * @return
 */
protected ItemStack[] getArmorStack(int level)
  {
  ItemStack[] stacks = new ItemStack[4];  
  return stacks;
  }

@Override
public IAmmoType getAmmoType(int level)
  {
  return null;
  }

@Override
public float getAccuracy(int level)
  {
  return 1.f;
  }

@Override
public NpcVarsHelper getVarsHelper(NpcBase npc)
  {
  return new NpcVarHelperDummy(npc);
  }

@Override
public boolean isVanillaVillager()
  {
  return isVanillaVillager;
  }

public static INpcType getNpcType(int num)
  {
  if(num>=0 && num < npcTypes.length)
    {
    return npcTypes[num];
    }
  return null;
  }

public static INpcType[] getNpcTypes()
  {
  return npcTypes;
  }

@Override
public List<NpcAIObjective> getAI(NpcBase npc, int level)
  {
  ArrayList<NpcAIObjective> aiEntries = new ArrayList<NpcAIObjective>(); 
  return aiEntries;
  }

public class NpcVarHelperDummy extends NpcVarsHelper
{

/**
 * @param npc
 */
public NpcVarHelperDummy(NpcBase npc)
  {
  super(npc);
  }

@Override
public void onTick()
  {

  }

@Override
public float getVar1()
  {
  return 0;
  }

@Override
public float getVar2()
  {
  return 0;
  }

@Override
public float getVar3()
  {
  return 0;
  }

@Override
public float getVar4()
  {
  return 0;
  }

@Override
public float getVar5()
  {
  return 0;
  }

@Override
public float getVar6()
  {
  return 0;
  }

@Override
public float getVar7()
  {
  return 0;
  }

@Override
public float getVar8()
  {
  return 0;
  }
}

}
