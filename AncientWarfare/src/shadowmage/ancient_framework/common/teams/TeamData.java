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
package shadowmage.ancient_framework.common.teams;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.common.gamedata.GameData;

public class TeamData extends GameData
{
public static String defaultTeamName = "defaultTeam";
HashMap<String, TeamEntry> entriesByTeamName = new HashMap<String, TeamEntry>();
HashMap<String, TeamEntry> entriesByPlayerName = new HashMap<String, TeamEntry>();

public TeamData()
  {
  super("AWTeamData");
  }

public TeamData(String par1Str)
  {
  super(par1Str);
  }

@Override
public void readFromNBT(NBTTagCompound nbttagcompound)
  {
  //TODO
  }

@Override
public void writeToNBT(NBTTagCompound nbttagcompound)
  {
  //TODO
  }

public boolean isHostileTowards(String offenseTeam, String defenseTeam)
  {
  TeamEntry teamA = entriesByTeamName.get(offenseTeam);
  if(teamA!=null){return teamA.isHostileTowardsTeam(defenseTeam);}
  return false;
  }

public void handlePlayerLogin(String playerName)  
  {
  if(!this.entriesByPlayerName.containsKey(playerName))
    {
    if(!this.entriesByTeamName.containsKey(defaultTeamName))
      {
      this.entriesByTeamName.put(defaultTeamName, new TeamEntry(defaultTeamName, null, 0xffffffff));            
      }
    TeamEntry defaultTeam = entriesByTeamName.get(defaultTeamName);
    defaultTeam.addPlayer(playerName);
    this.entriesByPlayerName.put(playerName, defaultTeam);
    }    
  }

@Override
public void handlePacketData(NBTTagCompound data)
  {
  
  }
}
