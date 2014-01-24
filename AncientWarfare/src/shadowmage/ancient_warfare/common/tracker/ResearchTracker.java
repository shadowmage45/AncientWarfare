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
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.Packet01ModData;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class ResearchTracker
{

private ResearchTracker(){}
private static ResearchTracker instance = new ResearchTracker(){};
public static ResearchTracker instance()
  {  
  return null;
  }

ResearchData data;

PlayerEntry clientEntry = new PlayerEntry();

public void onPlayerLogin(EntityPlayer player)
  {
  if(!this.data.playerEntries.containsKey(player.getEntityName()))
    {
    PlayerEntry entry = new PlayerEntry();
    entry.playerName = player.getEntityName();    
    this.data.playerEntries.put(player.getEntityName(), entry);
    this.data.markDirty();
    }
  }

public void onWorldLoad(World world)
  {
  data = (ResearchData) world.mapStorage.loadData(ResearchData.class, ResearchData.dataName);
  if(data==null)
    {
    Config.log("loaded new data set for research data");
    world.mapStorage.setData(ResearchData.dataName, new ResearchData(ResearchData.dataName));
    data = (ResearchData) world.mapStorage.loadData(ResearchData.class, ResearchData.dataName);
    }
  }

public void loadOldData(NBTTagCompound tag)
  {
  World world = MinecraftServer.getServer().worldServers[0];
  if(world==null){return;}//wtf..throw an exception or something
  data = new ResearchData(ResearchData.dataName);
  data.readFromNBT(tag);
  world.mapStorage.setData(ResearchData.dataName, data);
  data.markDirty();
  }

public PlayerEntry getClientEntry()
  {
  return clientEntry;
  }

public PlayerEntry getEntryFor(String name)
  {
  if(this.clientEntry!=null && name.equals(this.clientEntry.playerName))
    {
    return this.clientEntry;
    }
  if(!this.data.playerEntries.containsKey(name))
    {
    this.createEntryForNewPlayer(name);
    }
  return this.data.playerEntries.get(name);
  }

public PlayerEntry getEntryFor(EntityPlayer player)
  {
  if(player.worldObj.isRemote && player.getEntityName().equals(clientEntry.playerName))
    {
    return clientEntry;
    }
  else if(player.worldObj.isRemote)
    {
    return null;
    }
  return this.data.playerEntries.get(player.getEntityName());
  }

public void addResearchToPlayer(World world, String name, int goal)
  {
  if(world.isRemote)
    {
    if(this.clientEntry.playerName.equals(name))
      {
      this.clientEntry.addCompletedResearch(goal);
      }
    }
  else if(this.data.playerEntries.containsKey(name))
    {
    this.data.playerEntries.get(name).addCompletedResearch(goal);
    EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(name);
    if(player!=null)
      {
      Packet01ModData pkt = new Packet01ModData();
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("research", true);
      tag.setInteger("new", goal);
      pkt.packetData = tag;
      pkt.sendPacketToPlayer(player);
      }
    this.data.markDirty();
    }
  }

/**
 * create a new entry for a player, set team to 0,
 * and relay new player/team info to all logged in players
 */
private void createEntryForNewPlayer(String playerName)
  { 
  PlayerEntry entry = new PlayerEntry();
  entry.playerName = String.valueOf(playerName);  
  this.data.playerEntries.put(String.valueOf(playerName), entry);
  IResearchGoal[] knownResearch = ResearchGoal.getDefaultKnownResearch();
  for(IResearchGoal goal : knownResearch)
    {
    entry.addCompletedResearch(goal.getGlobalResearchNum());
    }
  GameDataTracker.instance().markGameDataDirty();
  }
}
