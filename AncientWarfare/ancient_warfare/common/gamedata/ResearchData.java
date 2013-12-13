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
package shadowmage.ancient_warfare.common.gamedata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

public class ResearchData extends WorldSavedData
{

public static final String name = "AWResearchData";

private HashMap<String, Set<Integer>> completedResearch = new HashMap<String, Set<Integer>>();

public ResearchData()
  {
  super(name);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  NBTTagList baseList = tag.getTagList("baseList");
  NBTTagCompound entryTag;
  NBTTagList goalList;
  String name;
  Set<Integer> goalSet;
  for(int i = 0; i < baseList.tagCount(); i++)
    {
    entryTag = (NBTTagCompound)baseList.tagAt(i);
    name = entryTag.getString("playerName");
    goalSet = new HashSet<Integer>();
    goalList = entryTag.getTagList("goals");
    for(int k = 0; k < goalList.tagCount(); k++)
      {
      goalSet.add(((NBTTagInt)goalList.tagAt(i)).data);
      }
    completedResearch.put(name, goalSet);
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {

  }

}
