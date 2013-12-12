/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.npcs.INpcType;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.NpcTypeBase;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import shadowmage.ancient_warfare.common.tracker.GameDataTracker;


public class NpcRegistry
{



private NpcRegistry(){}
private static NpcRegistry INSTANCE;
static Random random = new Random();

public static NpcRegistry instance()
  {
  if(INSTANCE==null){INSTANCE = new NpcRegistry();}
  return INSTANCE;
  }

public void registerNPCs()
  {
  //DEBUG
  //ItemLoader.instance().addSubtypeToItem(ItemLoader.npcSpawner, npcDummy.getGlobalNpcType(), npcDummy.getDisplayName(), npcDummy.getDisplayTooltip());
  //END DEBUG...
  
  INpcType[] types = NpcTypeBase.getNpcTypes();
  for(INpcType type : types)
    {
    if(type==null || type.getGlobalNpcType()==0){continue;}//if null or dummy type, don't register....
    type.setEnabled(Config.getConfig().get("g_npc_config", type.getConfigName()+".enabled", true).getBoolean(true));
    if(!type.isEnabled()){continue;}    
    for(int i = 0; i < type.getNumOfLevels(); i++)
      {
      type.getLevelEntry(i).setAttackDamage(Config.getConfig().get("g_npc_config", type.getConfigName()+"."+i+".damage", type.getAttackDamage(i)).getInt(type.getAttackDamage(i)));
      if(type.getLevelEntry(i).getHealingDone()>0)
        {
        type.getLevelEntry(i).setHealingDone( Config.getConfig().get( "g_npc_config", type.getConfigName()+"."+i+".healing", type.getLevelEntry(i).getHealingDone()).getInt(type.getLevelEntry(i).getHealingDone() ) );        
        }
      type.getLevelEntry(i).setHealth(Config.getConfig().get("g_npc_config", type.getConfigName()+"."+i+".maxhealth", type.getMaxHealth(i)).getInt(type.getMaxHealth(i)));
      type.getLevelEntry(i).setActionTicks(Config.getConfig().get("g_npc_config", type.getConfigName()+"."+i+".actiontime", type.getActionTicks(i)).getInt(type.getActionTicks(i)));
      }
    Description d = ItemLoader.instance().addSubtypeInfoToItem(ItemLoader.npcSpawner, type.getGlobalNpcType(), type.getDisplayName(0), "", type.getDisplayTooltip(0));
    d.setIconTexture(type.getIconTexture(), type.getGlobalNpcType());
    } 
  }

private List<ItemStack> displayItemCache = null;

public List getCreativeDisplayItems()
  {
  if(displayItemCache!=null)
    {
    return displayItemCache;
    }
  List<ItemStack> stacks = new ArrayList<ItemStack>();
  ItemStack stack = null;  
  NBTTagCompound tag = null;
  INpcType[] types = NpcTypeBase.getNpcTypes();
  for(INpcType type : types)
    {
    //DEBUG//|| type.getGlobalNpcType()==0
    if(type==null || type.getGlobalNpcType()==0 || !type.isEnabled()){continue;}//if null or dummy type, don't register....
    for(int i = 0; i < type.getNumOfLevels(); i++)
      {
      stack = new ItemStack(ItemLoader.npcSpawner,1,type.getGlobalNpcType());
      tag = new NBTTagCompound();
      tag.setInteger("lev", i);
      stack.setTagInfo("AWNpcSpawner", tag);
      stacks.add(stack);
      }
    }  
  displayItemCache = stacks;
  return stacks;
  }

public static Entity getNpcForType(int num, World world, int level, int team)
  {
  INpcType type = NpcTypeBase.getNpcType(num);
  if(type==null || !type.isEnabled())
    {
    return null;
    }
  if(type.isVanillaVillager())
    {
    if(Config.randomizeVillagers)
      {
      level = random.nextInt(type.getNumOfLevels()+1);
      }
    EntityVillager villager = new EntityVillager(world, level);
    return villager;
    }
  else
    {
    NpcBase npc = new NpcBase(world);
    npc.setNpcType(type, level); 
    if(type.isBandit())
      {
      npc.teamNum = 17;
      }
    ItemStack tool = type.getTool(level);
    if(tool!=null)
      {
      npc.setCurrentItemOrArmor(0, tool.copy());
      }        
    ItemStack[] armorStacks = type.getArmor(level);
    for(int i = 0; i <armorStacks.length && i < 4; i++)
      {
      if(armorStacks[i]!=null)
        {
        npc.setCurrentItemOrArmor(1 + 3-i, armorStacks[i].copy());
        }
      }
    if(!type.isBandit())
      {
      npc.teamNum = team;
      Config.logDebug("handling npc update from NPC spawn (NpcRegistry.getNpcForType(...))");
      GameDataTracker.instance().handleNpcUpdate(npc);      
      }
    return npc;
    }
  }

public static ItemStack getStackFor(INpcType type, int level)
  {
  ItemStack stack = new ItemStack(ItemLoader.npcSpawner.itemID, 1, type.getGlobalNpcType());
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("lev", level);
  stack.setTagInfo("AWNpcSpawner", tag);
  stack.getTagCompound().setName("tag");
  return stack;
  }

}
