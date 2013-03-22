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
package shadowmage.ancient_warfare.common.tracker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.network.Packet01ModData;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;

/**
 * client/server team data tracker
 * maintains client and server lists seperately, so one class
 * may be used as _the_ team tracker.
 * only one or the other will be populated on client/server
 * unless ran on integrated server, where both will be populated (SSP)
 * @author Shadowmage
 *
 */
public class TeamTracker implements INBTTaggable
{

private TeamEntry[] serverTeamEntries = new TeamEntry[16];
private TeamEntry[] clientTeamEntries = new TeamEntry[16];

private TeamTracker(){}
public static TeamTracker instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new TeamTracker();
    }
  return INSTANCE;
  }
private static TeamTracker INSTANCE;

public void handleNewPlayerLogin(EntityPlayer player)
  {
  if(this.serverTeamEntries[0]==null)
    {
    this.serverTeamEntries[0] = new TeamEntry();
    this.serverTeamEntries[0].teamNum = 0;    
    }
  this.serverTeamEntries[0].memberNames.add(player.getEntityName());
      
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("num", 0);
  tag.setString("pName", player.getEntityName());
  tag.setBoolean("new", true);
  Packet01ModData pkt = new Packet01ModData();
  pkt.setTeamUpdate(tag);
  EntityPlayer otherPlayer;
  for(Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
    {
    otherPlayer = (EntityPlayer)obj;
    if(otherPlayer!=null && otherPlayer != player)
      {
      pkt.sendPacketToPlayer(otherPlayer);
      }
    }    
  }

public void handleClientUpdate(NBTTagCompound tag)
  {
  if(tag.hasKey("new"))
    {
    int num = tag.getInteger("num");
    this.clientTeamEntries[num].memberNames.add(tag.getString("pName"));
    }
  else if(tag.hasKey("change"))
    {
    int num = tag.getInteger("num");
    String name = tag.getString("pName");
    int oldTeam = this.getTeamForPlayerClient(name);
    this.clientTeamEntries[oldTeam].memberNames.remove(name);
    this.clientTeamEntries[num].memberNames.add(name);
    }
  }

public void handleClientInit(NBTTagCompound tag)
  {
  this.clientTeamEntries = new TeamEntry[16];
  NBTTagList teamList = tag.getTagList("tL");
  NBTTagCompound teamTag = null;
  TeamEntry entry = null;
  for(int i = 0; i < teamList.tagCount(); i++)
    {
    teamTag = (NBTTagCompound) teamList.tagAt(i);
    entry = new TeamEntry();
    entry.readFromNBT(teamTag);
    if(this.clientTeamEntries[entry.teamNum]==null)
      {
      this.clientTeamEntries[entry.teamNum]=entry;
      }
    else
      {
      Config.logError("Error reading Team Data from NBT, duplicate team detected");
      }
    }
  }

public NBTTagCompound getClientInitData()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList teamList = new NBTTagList();  
  NBTTagCompound teamTag = null;
  for(TeamEntry entry : this.serverTeamEntries)
    {
    if(entry!=null)
      {
      teamList.appendTag(entry.getNBTTag());
      }    
    }
  tag.setTag("tL", teamList);
  return tag;
  }

public void handleServerUpdate(NBTTagCompound tag)
  { 
  if(tag.hasKey("change"))
    {
    int num = tag.getInteger("num");
    String name = tag.getString("pName");
    int oldTeam = this.getTeamForPlayerClient(name);
    this.serverTeamEntries[oldTeam].memberNames.remove(name);
    this.serverTeamEntries[num].memberNames.add(name);
    Packet01ModData pkt = new Packet01ModData();    
    pkt.setTeamUpdate(tag);
    pkt.sendPacketToAllPlayers();
    }
  }

public TeamEntry getTeamEntryFor(EntityPlayer player)
  {
  int teamNum = getTeamForPlayer(player);
  return getTeamEntry(player.worldObj, teamNum);
  }

public int getTeamForPlayerServer(String name)
  {
  for(TeamEntry ent : this.serverTeamEntries)
    {
    if(ent.memberNames.contains(name))
      {
      return ent.teamNum;
      }
    }
  return 0;
  }

public int getTeamForPlayer(EntityPlayer player)
  {
  if(player.worldObj.isRemote)    
    {
    return getTeamForPlayerClient(player.getEntityName());
    }
  else
    {
    return getTeamForPlayerServer(player.getEntityName());
    }
  }

public int getTeamForPlayerClient(String name)
  {
  for(TeamEntry ent : this.clientTeamEntries)
    {
    if(ent.memberNames.contains(name))
      {
      return ent.teamNum;
      }
    }
  return 0;
  }

public boolean isHostileTowards(World world, int aggressor, int defender)
  {
  return this.getTeamEntry(world, aggressor).isHostileTowards(defender);
  }

/**
 * client/server sensitive version of getEntry automatically pulls correct
 * entry depending upon if world.isRemote
 * @param world
 * @param teamNum
 * @return
 */
public TeamEntry getTeamEntry(World world, int teamNum)
  {  
  if(world.isRemote)
    {
    return this.getTeamEntryClient(teamNum);
    }
  else
    {
    return this.getTeamEntryServer(teamNum);
    }
  }

private TeamEntry getTeamEntryClient(int teamNum)
  {
  if(teamNum<0 || teamNum>=this.clientTeamEntries.length)
    {
    return null;
    }
  return this.clientTeamEntries[teamNum];
  }

private TeamEntry getTeamEntryServer(int teamNum)
  {
  if(teamNum<0 || teamNum>=this.serverTeamEntries.length)
    {
    return null;
    }
  return this.serverTeamEntries[teamNum];
  }

/**
 * return the entire tag to be saved to disc in AWCore.dat file
 */
@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList teamList = new NBTTagList();  
  NBTTagCompound teamTag = null;
  for(TeamEntry entry : this.serverTeamEntries)
    {
    if(entry!=null)
      {
      teamList.appendTag(entry.getNBTTag());
      }    
    }
  tag.setTag("tL", teamList);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.serverTeamEntries = new TeamEntry[16];
  NBTTagList teamList = tag.getTagList("tL");
  NBTTagCompound teamTag = null;
  TeamEntry entry = null;
  for(int i = 0; i < teamList.tagCount(); i++)
    {
    teamTag = (NBTTagCompound) teamList.tagAt(i);
    entry = new TeamEntry();
    entry.readFromNBT(teamTag);
    if(this.serverTeamEntries[entry.teamNum]==null)
      {
      this.serverTeamEntries[entry.teamNum]=entry;
      }
    else
      {
      Config.logError("Error reading Team Data from NBT, duplicate team detected");
      }
    }
  }

public void clearAllData()
  {
  this.serverTeamEntries = new TeamEntry[16];
  this.clientTeamEntries = new TeamEntry[16];
  }

}
