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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.container.ContainerTeamControl;
import shadowmage.ancient_warfare.common.network.Packet01ModData;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;

public class TeamTracker
{

static int NPC_FRIENDLY = 16;
static int NPC_HOSTILE = 17;

public static TeamTracker instance(){return instance;}
private static TeamTracker instance = new TeamTracker();

private TeamData teamData;
private TeamData clientData;

public void loadOldData(NBTTagCompound tag)
  {
  World world = MinecraftServer.getServer().worldServers[0];
  if(world==null){return;}//wtf..throw an exception or something
  TeamData data = new TeamData();
  data.readFromNBT(tag);
  world.mapStorage.setData(TeamData.dataName, data);
  this.teamData = data;  
  this.teamData.markDirty();
  }

public void onWorldLoad(World world)
  {
  teamData = (TeamData) world.mapStorage.loadData(TeamData.class, TeamData.dataName);
  if(teamData==null)
    {
    teamData=new TeamData();
    world.mapStorage.setData(TeamData.dataName, teamData);
    } 
  }

public void onPlayerLogin(EntityPlayer player)
  {
  boolean newPlayer = teamData.getEntryForPlayer(player.getEntityName())==null;
  if(newPlayer)
    {
    handleNewPlayerLogin(player.getEntityName());
    this.sendTeamDatas();
    }
  else
    {
    sendToPlayer(player);
    }
  }

private void handleNewPlayerLogin(String playerName)
  {
  this.teamData.teamEntries[0].addNewPlayer(playerName, (byte)0);
  this.teamData.markDirty();
  }

public int getTeamForPlayer(EntityPlayer player)
  {
  if("AncientWarfare".equals(player.getEntityName())){return -1;}
  return getTeamEntryFor(player).teamNum;
  }

public TeamEntry getTeamEntryFor(EntityPlayer player)
  {
  TeamData data = player.worldObj.isRemote? clientData : teamData;
  return data.getEntryForPlayer(player.getEntityName());  
  }

public TeamEntry getTeamEntryFor(World world, int teamNum)
  {
  TeamData data = world.isRemote? clientData : teamData;
  return data.teamEntries[teamNum];  
  }

public boolean areTeamsMutuallyFriendly(World world, int teamA, int teamB)
  {
  return !isHostileTowards(world, teamA, teamB) && !isHostileTowards(world, teamB, teamA);
  }

public boolean areTeamsMutuallyHosile(World world, int teamA, int teamB)
  {
  return isHostileTowards(world, teamA, teamB) && isHostileTowards(world, teamB, teamA);
  }

public boolean isHostileTowards(World world, int aggressor, int defender)
  {  
  if(aggressor == defender)
    {
    return false;
    }
  if(aggressor == NPC_HOSTILE || defender== NPC_HOSTILE)
    {
    return true;
    }
  if(aggressor==NPC_FRIENDLY)
    {
    return defender == NPC_HOSTILE;
    }
  else if(defender == NPC_FRIENDLY)
    {
    return aggressor==NPC_HOSTILE;
    }
  TeamEntry entry = this.getTeamEntry(world, aggressor);
  if(entry!=null)
    {
    return entry.isHostileTowards(defender);
    }
  return true;
  }

private TeamEntry getTeamEntry(World world, int teamNum)
  {  
  if(world.isRemote)
    {
    return this.clientData.teamEntries[teamNum];
    }
  else
    {
    return this.teamData.teamEntries[teamNum];
    }
  }

public void handlePacketData(NBTTagCompound tag)//client/server entry method
  {
  if(tag.hasKey("clientData"))
    {
    EntityPlayer player = AWCore.proxy.getClientPlayer();
    if(player!=null && player.openContainer instanceof ContainerTeamControl)
      {
      ((ContainerTeamControl)player.openContainer).refreshGui();
      }
    this.readClientData(tag.getCompoundTag("clientData"));
    }
  }

private void readClientData(NBTTagCompound tag)//client-side blind read-method
  {
  this.clientData = new TeamData();
  this.clientData.readFromNBT(tag);
  }

private void sendTeamDatas()
  {
  NBTTagCompound teamTag = new NBTTagCompound();
  this.teamData.writeToNBT(teamTag);
  Packet01ModData pkt = new Packet01ModData();
  NBTTagCompound outerTag = new NBTTagCompound();
  outerTag.setCompoundTag("clientData", teamTag);
  pkt.packetData.setCompoundTag("team", outerTag);
  pkt.sendPacketToAllPlayers();
  }

private void sendToPlayer(EntityPlayer player)
  {
  NBTTagCompound teamTag = new NBTTagCompound();
  this.teamData.writeToNBT(teamTag);
  Packet01ModData pkt = new Packet01ModData();
  NBTTagCompound outerTag = new NBTTagCompound();
  outerTag.setCompoundTag("clientData", teamTag);
  pkt.packetData.setCompoundTag("team", outerTag);
  pkt.sendPacketToPlayer(player);
  }

/**
 * server side only
 * @param player
 * @param team
 */
public void setPlayerTeam(String player, int team)
  {
  teamData.setPlayerTeam(player, team);  
  this.sendTeamDatas();
  }

/**
 * server side only
 * @param player
 * @param rank
 */
public void setPlayerRank(String player, int rank)
  {
  teamData.setPlayerRank(player, rank);  
  this.sendTeamDatas();
  }

/**
 * server side only
 * @param player
 * @param team
 */
public void handlePlayerApplication(String player, int teamNum)
  {
  teamData.handlePlayerApply(player, teamNum);  
  this.sendTeamDatas();
  }

/**
 * server side only
 * @param player
 */
public void handleKickAction(String player)
  {
  teamData.handlePlayerKick(player);  
  this.sendTeamDatas();
  }

/**
 * server side only
 * @param player
 * @param team
 * @param accept
 */
public void handleAccept(String player, int team, boolean accept)
  {
  if(accept)
    {
    teamData.handlePlayerAccept(player, team);    
    }
  else
    {
    teamData.handlePlayerDeny(player, team);
    }  
  EntityPlayer playerEntity = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(player);
  if(playerEntity!=null)
    {
    String accMsg = accept? "You have been accepted to team: "+team : "Your application to team: "+team +" has been denied";
    playerEntity.addChatMessage(accMsg);
    }
  this.sendTeamDatas();
  }

/**
 * server side only
 * @param team
 * @param hostTeam
 * @param add should add or remove from allied teams list
 */
public void handleHostileChange(int team, int hostTeam, boolean add)
  {
  teamData.handleHostileChange(team, hostTeam, add);  
  this.sendTeamDatas();
  }

}
