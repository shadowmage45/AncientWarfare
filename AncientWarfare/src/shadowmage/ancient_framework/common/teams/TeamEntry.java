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

import java.util.HashSet;
import java.util.Set;

public class TeamEntry
{

public final String teamName;
String leaderName;
int teamColor;//RGBA hex color, e.g. 0xff00ffff==purple
Set<String> playerNames = new HashSet<String>();
Set<String> warringTeams = new HashSet<String>();
Set<String> alliedTeams = new HashSet<String>();

boolean autoAcceptApplications = false;

public TeamEntry(String teamName, String leaderName, int teamColor)
  {
  this.teamName = teamName;
  this.leaderName = leaderName;
  this.teamColor = teamColor;
  }

public void addPlayer(String player)
  {
  playerNames.add(player);
  }

public void removePlayer(String player)
  {
  playerNames.remove(player);
  }

public void addWarringTeam(String teamName)
  {
  this.warringTeams.add(teamName);
  if(this.alliedTeams.contains(teamName))
    {
    this.alliedTeams.remove(teamName);
    }
  }

public void removeWarringTeam(String teamName)
  {
  this.warringTeams.remove(teamName);
  }

public boolean isHostileTowardsTeam(String teamName)
  {
  return this.warringTeams.contains(teamName);
  }

public void setLeaderName(String playerName)
  {
  if(this.playerNames.contains(playerName))
    {
    this.leaderName = playerName;
    }
  }

public int getTeamColor()
  {
  return teamColor;
  }

}
