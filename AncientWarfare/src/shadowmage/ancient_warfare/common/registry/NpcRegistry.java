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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.registry.entry.NpcEntry;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;


public class NpcRegistry
{

private static Map<Integer, NpcEntry> npcTypes = new HashMap<Integer, NpcEntry>();

private NpcRegistry(){}
private static NpcRegistry INSTANCE;
public static NpcRegistry instance()
  {
  if(INSTANCE==null){INSTANCE = new NpcRegistry();}
  return INSTANCE;
  }

public void registerNPCs()
  {
  
  }

public List getCreativeDisplayItems()
  {
  List<ItemStack> stacks = new ArrayList<ItemStack>();
  Iterator<Entry<Integer, NpcEntry>> it = this.npcTypes.entrySet().iterator();
  Entry<Integer, NpcEntry> ent = null;
  ItemStack stack = null;
  NpcEntry type = null;
  while(it.hasNext())
    {
    ent = it.next();
    type = ent.getValue();
    if(type==null )
      {
      continue;
      }
    for(int i = 0; i < type.numOfRanks; i++)
      {
      stack = new ItemStack(ItemLoader.npcSpawner,1, ent.getKey());
      NBTTagCompound tag = new NBTTagCompound();
      tag.setInteger("lev", i);
      if(i <type.rankNames.size())
        {
        tag.setString("name", type.rankNames.get(i));
        }
      stack.setTagInfo("AWNpcSpawner", tag);
      stacks.add(stack);
      }
    }
  return stacks;
  }

public static NpcBase getNpcForType(int num, World world)
  {
  if(npcTypes.containsKey(num))
    {
    try
      {
      return npcTypes.get(num).entityClass.getDeclaredConstructor(World.class).newInstance(world);
      }
    catch(Exception e){}    
    }
  return null;
  }

}
