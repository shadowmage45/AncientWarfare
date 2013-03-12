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
package shadowmage.ancient_warfare.common.tracker.entry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;

/**
 * dataStruct representing all data for a single team
 * @author Shadowmage
 *
 */
public class TeamEntry implements INBTTaggable
{

public int teamNum;
public List<String> memberNames = new ArrayList<String>();
public List<Integer> nonHostileTeams = new ArrayList<Integer>();

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("num", this.teamNum);
  NBTTagList namesList = new NBTTagList();
  for(int i = 0; i < this.memberNames.size(); i++)
    {    
    NBTTagCompound memberTag = new NBTTagCompound();
    memberTag.setString("name", this.memberNames.get(i));
    namesList.appendTag(memberTag);
    }  
  tag.setTag("teamMembers", namesList);
  
  int[] nonHost = new int[this.nonHostileTeams.size()];
  for(int i = 0; i < this.nonHostileTeams.size(); i++)
    {
    nonHost[i] = this.nonHostileTeams.get(i);
    }
  tag.setIntArray("nonHost", nonHost);  
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.memberNames.clear();
  this.teamNum = tag.getInteger("num");
  NBTTagList namesList = tag.getTagList("teamMembers");
  for(int i = 0; i < namesList.tagCount(); i++)
    {
    NBTTagCompound memberTag = (NBTTagCompound) namesList.tagAt(i);
    String name = memberTag.getString("name");
    this.memberNames.add(name);
    }  
  int[] nonHost = tag.getIntArray("nonHost");
  this.nonHostileTeams.clear();
  for(int i = 0; i < nonHost.length; i++)
    {
    this.nonHostileTeams.add(nonHost[i]);
    }  
  }

public boolean isHostileTowards(int num)
  {
  return num !=this.teamNum && !this.nonHostileTeams.contains(num);
  }


}
