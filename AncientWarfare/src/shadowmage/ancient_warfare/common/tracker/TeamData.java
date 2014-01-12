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
  for(int i = 0; i < teamEntries.length; i++)
    {
	teamEntries[i] = new TeamEntry();
	teamEntries[i].teamNum = i;
    }
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

public void setPlayerTeam(String playerName, int newTeam)
  {
  TeamEntry t = this.getEntryForPlayer(playerName);
  t.removePlayer(playerName);
  this.teamEntries[newTeam].addNewPlayer(playerName, this.teamEntries[newTeam].memberNames.size()==0 ? newTeam==0 ? (byte)0 : (byte)10 : (byte)0);
  }

public void setPlayerRank(String playerName, int newRank)
  {
  TeamEntry t = this.getEntryForPlayer(playerName);
  t.getEntryFor(playerName).setMemberRank((byte) newRank);
  }

public int getTeamForPlayer(String name)
  {
  for(int i = 0;i < this.teamEntries.length; i++)
    {
    if(this.teamEntries[i].containsPlayer(name))
      {
      return i;
      }
    }
  return 0;
  }

public TeamEntry getEntryForPlayer(String name)  
  {
  for(int i = 0;i < this.teamEntries.length; i++)
    {
    if(this.teamEntries[i].containsPlayer(name))
      {
      return teamEntries[i];
      }
    }
  return teamEntries[0];
  }

public void handlePlayerApply(String name, int team)
  {
  this.teamEntries[team].addApplicant(name);
  }

public void handlePlayerKick(String name)
  {
  this.getEntryForPlayer(name).removePlayer(name);
  this.setPlayerTeam(name, 0);
  }

public void handlePlayerAccept(String name, int team)
  {
  TeamEntry t = teamEntries[team];
  t.applicants.remove(name);
  t.addNewPlayer(name, (byte)0);
  }

public void handlePlayerDeny(String name, int team)
  {
  TeamEntry t = teamEntries[team];
  t.applicants.remove(name);
  }

public void handleHostileChange(int team, int hostTeam, boolean add)
  {
  TeamEntry t = teamEntries[team];
  if(add)
    {
    t.nonHostileTeams.add(hostTeam);
    }
  else
    {
    t.nonHostileTeams.remove(Integer.valueOf(hostTeam));
    }
  }
}
