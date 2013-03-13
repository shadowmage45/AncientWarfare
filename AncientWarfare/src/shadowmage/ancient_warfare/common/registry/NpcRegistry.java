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
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.soldiers.INpcType;


public class NpcRegistry
{

private Map<Integer, INpcType> npcInstances = new HashMap<Integer, INpcType>();

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

public void registerNPC(INpcType type)
  {
  
  }

public INpcType getNpcType(int type)
  {
  return this.npcInstances.get(type);
  }

public List getCreativeDisplayItems()
  {
  List<ItemStack> stacks = new ArrayList<ItemStack>();
  Iterator<Entry<Integer, INpcType>> it = this.npcInstances.entrySet().iterator();
  Entry<Integer, INpcType> ent = null;
  ItemStack stack = null;
  INpcType type = null;
  while(it.hasNext())
    {
    ent = it.next();
    type = ent.getValue();
    if(type==null )
      {
      continue;
      }
    for(int i = 0; i < type.getRanks(); i++)
      {
      stack = new ItemStack(ItemLoader.npcSpawner,1,type.getGlobalNpcType());
      NBTTagCompound tag = new NBTTagCompound();
      tag.setInteger("lev", i);
      stack.setTagInfo("AWNpcSpawner", tag);
      stacks.add(stack);
      }
    }
  return stacks;
  }

}
