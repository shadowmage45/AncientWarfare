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
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.soldiers.INpcType;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.NpcTypeBase;
import shadowmage.ancient_warfare.common.soldiers.types.NpcDummy;
import shadowmage.ancient_warfare.common.soldiers.types.NpcVillager;


public class NpcRegistry
{


public static INpcType npcDummy = new NpcDummy(0);
public static INpcType npcVillager = new NpcVillager(1);

private NpcRegistry(){}
private static NpcRegistry INSTANCE;

public static NpcRegistry instance()
  {
  if(INSTANCE==null){INSTANCE = new NpcRegistry();}
  return INSTANCE;
  }

public void registerNPCs()
  {
  //DEBUG
  ItemLoader.instance().addSubtypeToItem(ItemLoader.npcSpawner, npcDummy.getGlobalNpcType(), npcDummy.getDisplayName(), npcDummy.getDisplayTooltip());
  //END DEBUG...
  
  INpcType[] types = NpcTypeBase.getNpcTypes();
  for(INpcType type : types)
    {
    if(type==null || type.getGlobalNpcType()==0){continue;}//if null or dummy type, don't register....
    ItemLoader.instance().addSubtypeToItem(ItemLoader.npcSpawner, type.getGlobalNpcType(), type.getDisplayName(), type.getDisplayTooltip());
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
    if(type==null ){continue;}//if null or dummy type, don't register....
    for(int i = 0; i < type.getNumOfLevels(); i++)
      {
      stack = new ItemStack(ItemLoader.npcSpawner,1,type.getGlobalNpcType());
      tag = new NBTTagCompound();
      tag.setInteger("lev", i);
      tag.setString("name", type.getLevelName(i));
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
  if(type==null)
    {
    return null;
    }
  if(type.isVanillaVillager())
    {
    EntityVillager villager = new EntityVillager(world, level);
    return villager;
    }
  else
    {
    NpcBase npc = new NpcBase(world);
    npc.setNpcType(type, level);  
    npc.teamNum = team;
    return npc;
    }
  }

public static ItemStack getStackFor(INpcType type, int level)
  {
  ItemStack stack = new ItemStack(ItemLoader.npcSpawner.itemID, 1, type.getGlobalNpcType());
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("lev", level);
  tag.setString("name", type.getLevelName(level));
  stack.setTagInfo("AWNpcSpawner", tag);
  return null;
  }
}
