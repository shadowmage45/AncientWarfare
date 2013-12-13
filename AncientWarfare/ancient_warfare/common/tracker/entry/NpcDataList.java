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
package shadowmage.ancient_warfare.common.tracker.entry;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.npcs.NpcBase;

public class NpcDataList implements INBTTaggable
{

HashMap<UUID, NpcDataEntry> npcDatas = new HashMap<UUID, NpcDataEntry>();
public int teamNum = 0;

public NpcDataList(){}

public NpcDataList(int team)
  {
  this.teamNum = team;
  }

public int getDataLength()
  {
  return this.npcDatas.size();
  }

public void addEntry(NpcDataEntry entry)
  {
  this.npcDatas.put(entry.entityID, entry);
  }

public void handleNpcUpdate(NpcBase npc)
  {
  if(npc.isDead){return;}
  if(!this.npcDatas.containsKey(npc.getPersistentID()))
    {
    this.npcDatas.put(npc.getPersistentID(), new NpcDataEntry(npc));
    return;
    }
  this.npcDatas.get(npc.getPersistentID()).updateEntry(npc);
  }

public void removeNpcEntry(NpcBase npc)
  {
  npcDatas.remove(npc.entityId);
  }

public Collection<NpcDataEntry> getDataList()
  {
  return this.npcDatas.values();
  }

public NpcDataEntry getEntryFor(NpcBase npc)
  {
  if(!this.npcDatas.containsKey(npc.getPersistentID()))
    {
    this.npcDatas.put(npc.getPersistentID(), new NpcDataEntry(npc));
    }
  return this.npcDatas.get(npc.getPersistentID());
  }

public void handleNpcDeath(NpcBase npc)
  {
  this.npcDatas.remove(npc.getPersistentID());
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList list = new NBTTagList();
  for(NpcDataEntry data : this.npcDatas.values())
    {
    list.appendTag(data.getNBTTag());
    }
  tag.setTag("list", list);
  tag.setInteger("team", teamNum);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {  
  NBTTagList list = tag.getTagList("list");
  for(int i = 0; i < list.tagCount(); i++)
    {
    NpcDataEntry entry = new NpcDataEntry();
    entry.readFromNBT((NBTTagCompound) list.tagAt(i));
    if(entry.lastKnownPosition.y>=0 && (entry.dead || entry.lastKnownHealth>0))
      {
      this.npcDatas.put(entry.entityID, entry);      
      }
    }
  this.teamNum = tag.getInteger("team");
  }

}
