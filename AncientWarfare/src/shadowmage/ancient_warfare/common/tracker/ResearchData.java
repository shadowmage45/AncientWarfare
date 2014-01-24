/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.tracker;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class ResearchData extends WorldSavedData
{
/**
 * server-side list of all player entries
 */
Map<String, PlayerEntry> playerEntries = new HashMap<String, PlayerEntry>();

public static final String dataName = "AW_RESEARCH_DATA";

public ResearchData(String par1Str)
  {
  super(par1Str);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {  
  NBTTagList list = tag.getTagList("list");
  for(int i = 0; i < list.tagCount(); i++)
    {
    NBTTagCompound entTag = (NBTTagCompound) list.tagAt(i);
    PlayerEntry ent = new PlayerEntry();
    ent.readFromNBT(entTag);
    this.playerEntries.put(ent.playerName, ent);
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  NBTTagCompound entryTag;
  NBTTagList list = new NBTTagList();  
  for(String name : this.playerEntries.keySet())
    {
    PlayerEntry ent = this.playerEntries.get(name);
    list.appendTag(ent.getNBTTag());
    }
  tag.setTag("list", list);
  }

}
