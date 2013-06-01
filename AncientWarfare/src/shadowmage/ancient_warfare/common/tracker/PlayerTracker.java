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

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.network.Packet01ModData;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;
import cpw.mods.fml.common.IPlayerTracker;

/**
 * server side player tracker
 * @author Shadowmage
 *
 */
public class PlayerTracker implements IPlayerTracker, INBTTaggable
{
private PlayerTracker(){}
public static PlayerTracker instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new PlayerTracker();
    }
  return INSTANCE;
  }
private static PlayerTracker INSTANCE;

/**
 * server-side list of all player entries
 */
private Map<String, PlayerEntry> playerEntries = new HashMap<String, PlayerEntry>();

/**
 * player entry used by thePlayer client-side
 */
private PlayerEntry clientEntry = new PlayerEntry();

public PlayerEntry getClientEntry()
  {
  return clientEntry;
  }

public PlayerEntry getEntryFor(String name)
  {
  return this.playerEntries.get(name);
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
  return this.playerEntries.get(player.getEntityName());
  }

@Override
public void onPlayerLogin(EntityPlayer player)
  {
  if(player.worldObj.isRemote)
    {
    return;
    }
  StructureManager.instance().handlePlayerLogin(player);  
  if(!playerEntries.containsKey(player.getEntityName()))
    {
    this.createEntryForNewPlayer(player);
    }  
  
  NBTTagCompound initTag = new NBTTagCompound();
  NBTTagCompound tag = this.getClientInitData(player);
  if(tag!=null)
    {
//    Config.logDebug("getting playerData");
    initTag.setCompoundTag("playerData", tag);
    }
  tag = TeamTracker.instance().getClientInitData();
  if(tag!=null)
    {
//    Config.logDebug("getting teamData");
    initTag.setCompoundTag("teamData", tag);
    }
  tag = Config.instance().getClientInitData();
  if(tag!=null)
    {
//    Config.logDebug("getting configData");
    initTag.setCompoundTag("configData", tag);
    }  
  Packet01ModData init = new Packet01ModData();
  init.setInitData(initTag);
  AWCore.proxy.sendPacketToPlayer(player, init);  
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
  else
    {
    this.playerEntries.get(name).addCompletedResearch(goal);
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
    }
  }

public void handleClientInit(NBTTagCompound tag)
  {
  this.clientEntry = new PlayerEntry();
  this.clientEntry.readFromNBT(tag);
  }

private NBTTagCompound getClientInitData(EntityPlayer player)
  {
  PlayerEntry ent = this.playerEntries.get(player.getEntityName());
  if(ent!=null)
    {
    return ent.getNBTTag();
    }
  return null;
  }

/**
 * create a new entry for a player, set team to 0,
 * and relay new player/team info to all logged in players
 */
private void createEntryForNewPlayer(EntityPlayer player)
  {
  if(player.worldObj.isRemote)
    {
    return;
    }
  PlayerEntry entry = new PlayerEntry();
  entry.playerName = String.valueOf(player.getEntityName());  
  this.playerEntries.put(String.valueOf(player.getEntityName()), entry);
  IResearchGoal[] knownResearch = ResearchGoal.getDefaultKnownResearch();
  for(IResearchGoal goal : knownResearch)
    {
    entry.addCompletedResearch(goal.getGlobalResearchNum());
    }
  TeamTracker.instance().handleNewPlayerLogin(player);  
  GameDataTracker.instance().markGameDataDirty();
  }

@Override
public void onPlayerLogout(EntityPlayer player)
  {  
  }

@Override
public void onPlayerChangedDimension(EntityPlayer player)
  {
  }

@Override
public void onPlayerRespawn(EntityPlayer player)
  {
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList list = new NBTTagList();  
  for(String name : this.playerEntries.keySet())
    {
    PlayerEntry ent = this.playerEntries.get(name);
    list.appendTag(ent.getNBTTag());
    }
  tag.setTag("list", list);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.playerEntries.clear();
  NBTTagList list = tag.getTagList("list");
  for(int i = 0; i < list.tagCount(); i++)
    {
    NBTTagCompound entTag = (NBTTagCompound) list.tagAt(i);
    PlayerEntry ent = new PlayerEntry();
    ent.readFromNBT(entTag);
    this.playerEntries.put(ent.playerName, ent);
    }
  }

public void clearAllData()
  {
  this.clientEntry = new PlayerEntry();
  this.playerEntries.clear();
  }

}
