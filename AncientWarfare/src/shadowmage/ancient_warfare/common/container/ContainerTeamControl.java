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
package shadowmage.ancient_warfare.common.container;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;

public class ContainerTeamControl extends ContainerBase
{

public ContainerTeamControl(EntityPlayer openingPlayer)
  {
  super(openingPlayer, null); 
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("apply"))
    {    
    String name = tag.getString("name");
    int team = tag.getInteger("team");
    TeamTracker.instance().handlePlayerApplication(name, team);
    }
  if(tag.hasKey("kick"))
    {
    String name = tag.getString("name");
    TeamTracker.instance().handleKickAction(name);
    }
  if(tag.hasKey("accept"))
    {
    boolean accept = tag.getBoolean("accept");
    String name = tag.getString("name");
    int team = tag.getInteger("team");
    TeamTracker.instance().handleAccept(name, team, accept);
    }
  if(tag.hasKey("hostile"))
    {
    boolean add = tag.getBoolean("hostile");
    int team = tag.getInteger("team");
    int hostTeam = tag.getInteger("hostTeam");
    TeamTracker.instance().handleHostileChange(team, hostTeam, add);
    }
  if(tag.hasKey("rank"))
    {
    String name = tag.getString("name");
    int rank = tag.getInteger("rank");
    TeamTracker.instance().setPlayerRank(name, rank);
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

}
