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
package shadowmage.ancient_warfare.common.tracker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.interfaces.INBTTaggable;
import shadowmage.ancient_framework.common.network.Packet01ModData;
import shadowmage.ancient_warfare.AWCore;
import shadowmage.ancient_warfare.common.container.ContainerTeamControl;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry.TeamMemberEntry;

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

static int NPC_FRIENDLY = 16;
static int NPC_HOSTILE = 17;
private TeamEntry[] serverTeamEntries = new TeamEntry[16];
private TeamEntry[] clientTeamEntries = new TeamEntry[16];

private TeamTracker()
  {
  for(int i= 0; i < 16; i++)
    {
    serverTeamEntries[i] = new TeamEntry();
    clientTeamEntries[i] = new TeamEntry();
    serverTeamEntries[i].teamNum = i;
    clientTeamEntries[i].teamNum = i;
    }
  }
public static TeamTracker instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new TeamTracker();
    }
  return INSTANCE;
  }
private static TeamTracker INSTANCE;

public void handleClientHostileTeamChange(String name, byte team, byte oTeam, boolean newStatus)
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("pName", name);
  tag.setByte("num", team);  
  tag.setByte("oTeam", oTeam);
  tag.setBoolean("status", newStatus);
  tag.setBoolean("hostChange", true);
  Packet01ModData pkt = new Packet01ModData();
  pkt.setTeamUpdate(tag);
  pkt.sendPacketToServer();
  }

public void handleClientRankChange(String name, byte num, byte newRank)
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("pName", name);
  tag.setByte("num", num);  
  tag.setByte("rank", newRank);
  tag.setBoolean("rankChange", true);
  Packet01ModData pkt = new Packet01ModData();
  pkt.setTeamUpdate(tag);
  pkt.sendPacketToServer();
  }

public void handleClientApplyToTeam(String name, byte num)
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("pName", name);
  tag.setByte("num", num);  
  tag.setBoolean("apply", true);
  Packet01ModData pkt = new Packet01ModData();
  pkt.setTeamUpdate(tag);
  pkt.sendPacketToServer();
  }

public void handleClientAppAction(byte num, String name, boolean accept)
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("pName", name);
  tag.setByte("num", num);  
  if(accept)
    {
    tag.setBoolean("accept", true);
    }
  else    
    {
    tag.setBoolean("deny", true);
    }
  Packet01ModData pkt = new Packet01ModData();
  pkt.setTeamUpdate(tag);
  pkt.sendPacketToServer();
  }

public boolean areTeamsMutuallyFriendly(World world, int teamA, int teamB)
  {
  return !isHostileTowards(world, teamA, teamB) && !isHostileTowards(world, teamB, teamA);
  }

public boolean areTeamsMutuallyHosile(World world, int teamA, int teamB)
  {
  return isHostileTowards(world, teamA, teamB) && isHostileTowards(world, teamB, teamA);
  }

public void handleNewPlayerLogin(String playerName)
  {
  if(this.serverTeamEntries[0]==null)
    {
    this.serverTeamEntries[0] = new TeamEntry();
    this.serverTeamEntries[0].teamNum = 0;    
    }
  this.serverTeamEntries[0].addNewPlayer(playerName, (byte)0);//.memberNames.add(player.getEntityName());
      
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("num", (byte) 0);
  tag.setString("pName", playerName);
  tag.setBoolean("new", true);
  Packet01ModData pkt = new Packet01ModData();
  pkt.setTeamUpdate(tag);
  EntityPlayer otherPlayer;
  for(Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
    {
    otherPlayer = (EntityPlayer)obj;
    if(otherPlayer!=null && !otherPlayer.getEntityName().equals(playerName))
      {
      pkt.sendPacketToPlayer(otherPlayer);
      }
    } 
  GameDataTracker.instance().markGameDataDirty();
  }

public void handleClientUpdate(NBTTagCompound tag, EntityPlayer player)
  {
  byte num = tag.getByte("num");
  String name = tag.getString("pName");
  if(clientTeamEntries[num]==null)
    {
    clientTeamEntries[num]= new TeamEntry();
    clientTeamEntries[num].teamNum = num;
    }
  TeamEntry tagTeam = clientTeamEntries[num];
  if(tag.hasKey("new"))
    {    
    byte rank = tag.getByte("rank");    
    tagTeam.addNewPlayer(name, rank);
    }
  else if(tag.hasKey("change"))
    {
    byte rank = tag.getByte("rank");
    int oldTeam = this.getTeamForPlayerClient(name);
    this.clientTeamEntries[oldTeam].removePlayer(name);
    tagTeam.addNewPlayer(name, rank);
    }
  else if(tag.hasKey("apply"))
    {
    if(tagTeam.memberNames.size()==0)
      {
      int oldTeam = this.getTeamForPlayerClient(name);
      this.clientTeamEntries[oldTeam].removePlayer(name);
      if(tagTeam.teamNum==0)
        {
        tagTeam.addNewPlayer(name, (byte) 0);
        }
      else
        {
        tagTeam.addNewPlayer(name, (byte) 10);
        }
      }
    else
      {
      if(tagTeam.teamNum==0)
        {
        int oldTeam = this.getTeamForPlayerClient(name);
        this.clientTeamEntries[oldTeam].removePlayer(name);
        tagTeam.addNewPlayer(name, (byte) 0);
        }
      else
        {
        tagTeam.addApplicant(name);
        }
      }
    }
  else if(tag.hasKey("accept"))
    {
    int oldTeam = this.getTeamForPlayerServer(name);
    this.clientTeamEntries[oldTeam].removePlayer(name);
    tagTeam.addNewPlayer(name, (byte) 0);
    tagTeam.removeApplicant(name);
    }
  else if(tag.hasKey("deny"))
    {
    tagTeam.removeApplicant(name);
    }
  else if(tag.hasKey("rankChange"))
    {
    TeamMemberEntry entry = tagTeam.getEntryFor(name);
    entry.setMemberRank(tag.getByte("rank"));
    }
  else if(tag.hasKey("hostChange"))
    {
    tagTeam.handleHostileStatusChange(tag.getByte("oTeam"), tag.getBoolean("status"));
//    Config.logDebug("updating team non-hostile status and relaying!!");
    }
  if(player.openContainer instanceof ContainerTeamControl)
    {
    tag.setBoolean("rebuild", true);
    ((ContainerTeamControl) player.openContainer).rebuildTeamList();
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
    	AWCore.instance.logError("Error reading Team Data from NBT, duplicate team detected");
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

public void handleServerUpdate(NBTTagCompound tag, EntityPlayer player)
  { 
  byte num = tag.getByte("num");
  String name = tag.getString("pName");
  if(serverTeamEntries[num]==null)
    {
    serverTeamEntries[num] = new TeamEntry();
    serverTeamEntries[num].teamNum = num;
    }
  TeamEntry tagTeam = serverTeamEntries[num];
  Packet01ModData pkt = new Packet01ModData(); 
  if(tag.hasKey("change"))
    { 
    byte rank = tag.getByte("rank");
    int oldTeam = this.getTeamForPlayerServer(name);
    this.serverTeamEntries[oldTeam].removePlayer(name);
    tagTeam.addNewPlayer(name, rank);  
    pkt.setTeamUpdate(tag);
    pkt.sendPacketToAllPlayers();
    }
  else if(tag.hasKey("apply"))
    {    
    if(tagTeam.memberNames.size()==0)//new team is empty, create new team
      {
      int oldTeam = this.getTeamForPlayerServer(name);
      this.serverTeamEntries[oldTeam].removePlayer(name);
      if(tagTeam.teamNum==0)
        {
        tagTeam.addNewPlayer(name, (byte) 0);
        }
      else
        {
        tagTeam.addNewPlayer(name, (byte) 10);
        }
      }
    else
      {
      if(tagTeam.teamNum==0)
        {
        int oldTeam = this.getTeamForPlayerServer(name);
        tagTeam.addNewPlayer(name, (byte) 0);
        this.serverTeamEntries[oldTeam].removePlayer(name);
        }
      else
        {
        tagTeam.addApplicant(name);
        }      
      }
    pkt.setTeamUpdate(tag);
    pkt.sendPacketToAllPlayers();
    }
  else if(tag.hasKey("accept"))
    {
    int oldTeam = this.getTeamForPlayerServer(name);
    this.serverTeamEntries[oldTeam].removePlayer(name);
    tagTeam.addNewPlayer(name, (byte) 0);
    tagTeam.removeApplicant(name);
    pkt.setTeamUpdate(tag);
    pkt.sendPacketToAllPlayers();
    }
  else if(tag.hasKey("deny"))
    {
    tagTeam.removeApplicant(name);
    pkt.setTeamUpdate(tag);
    pkt.sendPacketToAllPlayers();
    }
  else if(tag.hasKey("rankChange"))
    {
    TeamMemberEntry entry = tagTeam.getEntryFor(name);
    entry.setMemberRank(tag.getByte("rank"));
    pkt.setTeamUpdate(tag);
    pkt.sendPacketToAllPlayers();
    }
  else if(tag.hasKey("hostChange"))
    {
    tagTeam.handleHostileStatusChange(tag.getByte("oTeam"), tag.getBoolean("status"));
    pkt.setTeamUpdate(tag);
    pkt.sendPacketToAllPlayers();
    }
  GameDataTracker.instance().markGameDataDirty();
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
    if(ent!=null && ent.containsPlayer(name))
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
    if(ent!=null && ent.containsPlayer(name))
      {
      return ent.teamNum;
      }
    }
  return 0;
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
      Exception e = new Exception("Error reading Team Data from NBT, duplicate team detected");
      e.printStackTrace();
      }
    }
  }

public void clearAllData()
  {
  this.serverTeamEntries = new TeamEntry[16];  
  this.clientTeamEntries = new TeamEntry[16];
  for(int i= 0; i < 16; i++)
    {
    serverTeamEntries[i] = new TeamEntry();
    clientTeamEntries[i] = new TeamEntry();
    serverTeamEntries[i].teamNum = i;
    clientTeamEntries[i].teamNum = i;
    }
  }

}
