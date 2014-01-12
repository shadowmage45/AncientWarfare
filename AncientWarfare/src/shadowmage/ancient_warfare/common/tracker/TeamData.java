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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;

public class TeamData extends WorldSavedData
{

public static String dataName = "AW_TEAM_DATA";

public TeamEntry[] teamEntries = new TeamEntry[16];
public TeamData(){this(dataName);}
public TeamData(String par1Str)
  {
  super(par1Str);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  NBTTagList teamList = tag.getTagList("tL");
  NBTTagCompound teamTag = null;
  TeamEntry entry = null;
  for(int i = 0; i < teamList.tagCount(); i++)
    {
    teamTag = (NBTTagCompound) teamList.tagAt(i);
    entry = new TeamEntry();
    entry.readFromNBT(teamTag);
    teamEntries[entry.teamNum]=entry;
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  NBTTagList teamList = new NBTTagList();  
  NBTTagCompound teamTag = null;
  for(TeamEntry entry : this.teamEntries)
    {
    if(entry!=null)
      {
      teamList.appendTag(entry.getNBTTag());
      }    
    }
  tag.setTag("tL", teamList);
  }

public void changePlayerTeam(String playerName, int newTeam){}

public int getTeamForPlayer(String name){return 0;}

public TeamEntry getEntryForPlayer(String name){return null;}

public void handlePlayerKick(String name){}

public void handlePlayerAccept(String name, int team){}

public void handlePlayerDeny(String name, int team){}

public void changePlayerRank(String name, int newRank){}
}
