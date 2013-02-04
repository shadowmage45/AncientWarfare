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
package shadowmage.ancient_warfare.common.world_gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shadowmage.meim.common.config.Config;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LootGenerator
{

private static LootGenerator INSTANCE;
private LootGenerator(){}
public static LootGenerator instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new LootGenerator();
    }
  return INSTANCE;
  }

/**
 * loot table types
 */
public static final int GENERIC = 0;
public static final int VALUABLES = 1;
public static final int RESEARCH = 2;
public static final int COMPONENTS = 3;
public static final int FOOD = 4;
public static final int AMMO = 5;
public static final int WEAPONS = 6;
public static final int VEHICLES = 7;

/**
 * entry for a single item
 * @author Shadowmage
 *
 */
private class ItemEntry
{
int id;
int meta;
int count;
int weight;
int value;
public ItemEntry(int id, int meta, int count, int weight, int value)
  {
  this.id = id;
  this.meta = meta;
  this.count = count;
  this.weight = weight;
  this.value = value;
  }
}/////////////////////////////END ITEMENTRY

/**
 * represents an entire level of loot for one loot type
 * @author Shadowmage
 *
 */
private class WeightedLootLevel
{
int totalWeight;

private List<ItemEntry> itemList = new ArrayList<ItemEntry>();

public void addItem(int id, int meta, int count, int weight, int value)
  {
  this.itemList.add(new ItemEntry(id, meta, count, weight, value));
  this.totalWeight+=weight;
  }

public void addItem(Item item, int count, int weight, int value)
  {
  this.addItem(item.itemID, 0, count, weight, value);
  }

public ItemEntry getRandomWeightedEntry(Random random)
  {
  if(totalWeight ==0 || this.itemList.isEmpty())
    {
    return null;
    }
  int check = random.nextInt(totalWeight);
  for(ItemEntry ent : this.itemList)
    {
    if(check> ent.weight)
      {
      check -=ent.weight;
      }
    else
      {
      return ent;
      }
    }
  return null;
  }


}/////////////////////////////END WEIGHTEDLOOTLEVEL

/**
 * loot tables..
 */
private WeightedLootLevel [] genericLootTable = new WeightedLootLevel[10];
private WeightedLootLevel [] valuablesTable = new WeightedLootLevel[10];
private WeightedLootLevel [] researchTable = new WeightedLootLevel[10];
private WeightedLootLevel [] componentsTable = new WeightedLootLevel[10];
private WeightedLootLevel [] foodTable = new WeightedLootLevel[10];
private WeightedLootLevel [] ammoTable = new WeightedLootLevel[10];
private WeightedLootLevel [] weaponsTable = new WeightedLootLevel[10];
private WeightedLootLevel [] vehiclesTable = new WeightedLootLevel[10];


public List<ItemStack> getRandomLoot(int maxValue, int maxLvl, int numOfStacks, int[] tables, Random random)
  {
  List<ItemStack> loot = new ArrayList<ItemStack>();
  
  int foundVal = 0;
  int table = 0;
  int level = 0;
  int retryCount;
  for(int i = 0; i < numOfStacks; i++)
    {
    retryCount = maxLvl;
    table = selectTable(tables, random);
    level = selectLevel(maxLvl, random);
    ItemEntry entry = getEntryFromTable(table,level, random);
    if(entry!=null)
      {
      loot.add(new ItemStack(entry.id, entry.count, entry.meta));
      Config.logDebug("adding stack "+entry.id+","+entry.meta);
      }
    else//if nothing at that level, keep checking levels until 0...      
      {
      Config.logDebug("null stack, retrying");
      while(entry==null && retryCount >0)
        {
        retryCount--;
        entry = getEntryFromTable(table, retryCount, random);        
        }
      if(entry!=null)
        {
        loot.add(new ItemStack(entry.id, entry.count, entry.meta));
        Config.logDebug("adding stack "+entry.id+","+entry.meta+"   from retry");
        }
      }
    }    
  return loot;
  }

private ItemEntry getEntryFromTable(int table, int level, Random random)
  {
  if(level>9 || table > 7 || table < 0 || level < 0)
    {
    return null;
    }
  WeightedLootLevel lvl = getTable(table)[level];
  if(lvl!=null)
    {
    return lvl.getRandomWeightedEntry(random);
    }
  return null;
  }

private WeightedLootLevel[] getTable(int table)
  {
  if(table > 7 || table < 0)
    {
    return null;
    }
  switch(table)
  {
  case GENERIC:
  return genericLootTable;
  case VALUABLES:
  return valuablesTable;
  case RESEARCH:
  return researchTable;
  case COMPONENTS:
  return componentsTable;
  case FOOD:
  return foodTable;
  case AMMO:
  return ammoTable;
  case WEAPONS:
  return weaponsTable;
  case VEHICLES:  
  return vehiclesTable;
  default:
  return null;  
  }
  }

private int selectTable(int[] tables, Random random)
  {
  return tables[random.nextInt(tables.length)];
  }

private int selectLevel(int maxLevel, Random random)
  {
  int totalLevelWeight = 0;
  for(int i = 0; i <= maxLevel; i++)
    {
    totalLevelWeight += (i+1)^4;
    }  
  int check = random.nextInt(totalLevelWeight);
  for(int i = 0; i <= maxLevel; i++)
    {
    int pow = (maxLevel-i+1)^4;
    if(check>=pow)
      {
      check-=pow;
      }
    else
      {
      return i;
      }
    }
  return 0;
  }

private void addLootEntry(int table, int level, int id, int meta, int count, int weight, int value)
  {
  if(table > 7 || table < 0|| level < 0 || level >9)
    {
    return;
    }
  WeightedLootLevel[] tbl = getTable(table);
  if(tbl[level]==null)
    {
    tbl[level] = new WeightedLootLevel();
    }
  tbl[level].addItem(id, meta, count, weight, value);
  }

private void addLootEntry(int table, int level, Item item, int count, int weight, int value)
  {
  this.addLootEntry(table, level, item.itemID, 0, count, weight, value);
  }

public void loadStaticLootTables()
  {
//  this.addLootEntry(GENERIC, 0, Block.torchWood.blockID, 0, 1, 10, 1);
//  this.addLootEntry(GENERIC, 1, Item.silk, 2, 10, 2);
//  this.addLootEntry(GENERIC, 2, Item.gunpowder, 1, 10, 4);
  this.addLootEntry(VALUABLES, 0, Item.coal, 1, 10, 1);
  this.addLootEntry(VALUABLES, 1, Item.ingotIron, 1, 10, 3);
  this.addLootEntry(VALUABLES, 2, Item.swordSteel, 1, 10, 5);
  this.addLootEntry(VALUABLES, 3, Item.goldNugget, 1, 10, 1);
  this.addLootEntry(VALUABLES, 4, Item.ingotIron, 5, 10, 15);
  this.addLootEntry(VALUABLES, 5, Item.ingotGold, 1, 10, 10);
  this.addLootEntry(VALUABLES, 6, Item.ingotGold, 2, 10, 20);
  this.addLootEntry(VALUABLES, 7, Item.ingotIron, 10, 10, 30);
  this.addLootEntry(VALUABLES, 8, Item.diamond, 1, 10, 50);
  this.addLootEntry(VALUABLES, 9, Item.swordDiamond, 1, 10, 100);
  
  this.addLootEntry(GENERIC, 0, Item.coal, 1, 10, 1);
  this.addLootEntry(GENERIC, 1, Item.ingotIron, 1, 10, 3);
  this.addLootEntry(GENERIC, 2, Item.swordSteel, 1, 10, 5);
  this.addLootEntry(GENERIC, 3, Item.goldNugget, 1, 10, 1);
  this.addLootEntry(GENERIC, 4, Item.ingotIron, 5, 10, 15);
  this.addLootEntry(GENERIC, 5, Item.ingotGold, 1, 10, 10);
  this.addLootEntry(GENERIC, 6, Item.ingotGold, 2, 10, 20);
  this.addLootEntry(GENERIC, 7, Item.ingotIron, 10, 10, 30);
  this.addLootEntry(GENERIC, 8, Item.diamond, 1, 10, 50);
  this.addLootEntry(GENERIC, 9, Item.swordDiamond, 1, 10, 100);
  
  this.addLootEntry(RESEARCH, 0, Item.coal, 1, 10, 1);
  this.addLootEntry(RESEARCH, 1, Item.ingotIron, 1, 10, 3);
  this.addLootEntry(RESEARCH, 2, Item.swordSteel, 1, 10, 5);
  this.addLootEntry(RESEARCH, 3, Item.goldNugget, 1, 10, 1);
  this.addLootEntry(RESEARCH, 4, Item.ingotIron, 5, 10, 15);
  this.addLootEntry(RESEARCH, 5, Item.ingotGold, 1, 10, 10);
  this.addLootEntry(RESEARCH, 6, Item.ingotGold, 2, 10, 20);
  this.addLootEntry(RESEARCH, 7, Item.ingotIron, 10, 10, 30);
  this.addLootEntry(RESEARCH, 8, Item.diamond, 1, 10, 50);
  this.addLootEntry(RESEARCH, 9, Item.swordDiamond, 1, 10, 100);
  
  this.addLootEntry(COMPONENTS, 0, Item.coal, 1, 10, 1);
  this.addLootEntry(COMPONENTS, 1, Item.ingotIron, 1, 10, 3);
  this.addLootEntry(COMPONENTS, 2, Item.swordSteel, 1, 10, 5);
  this.addLootEntry(COMPONENTS, 3, Item.goldNugget, 1, 10, 1);
  this.addLootEntry(COMPONENTS, 4, Item.ingotIron, 5, 10, 15);
  this.addLootEntry(COMPONENTS, 5, Item.ingotGold, 1, 10, 10);
  this.addLootEntry(COMPONENTS, 6, Item.ingotGold, 2, 10, 20);
  this.addLootEntry(COMPONENTS, 7, Item.ingotIron, 10, 10, 30);
  this.addLootEntry(COMPONENTS, 8, Item.diamond, 1, 10, 50);
  this.addLootEntry(COMPONENTS, 9, Item.swordDiamond, 1, 10, 100);
  
  }

}
