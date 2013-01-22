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
package shadowmage.ancient_warfare.common.aw_core.tracker;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.aw_core.AWCore;
import shadowmage.ancient_warfare.common.aw_core.network.Packet01ModData;
import shadowmage.ancient_warfare.common.aw_core.tracker.entry.PlayerEntry;
import shadowmage.ancient_warfare.common.aw_core.utils.INBTTaggable;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;
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
  NBTTagCompound tag = this.getClientInitData();
  if(tag!=null)
    {
    initTag.setCompoundTag("playerData", tag);
    }
  tag = TeamTracker.instance().getClientInitData();
  if(tag!=null)
    {
    initTag.setCompoundTag("teamData", tag);
    }
  
  Packet01ModData init = new Packet01ModData();
  init.setInitData(initTag);
  AWCore.proxy.sendPacketToPlayer(player, init);  
  }

public void handleClientInit(NBTTagCompound tag)
  {
  //TODO
  }

private NBTTagCompound getClientInitData()
  {
  //TODO
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
  entry.playerName = player.getEntityName();
  this.playerEntries.put(player.getEntityName(), entry);
  TeamTracker.instance().handleNewPlayerLogin(player);
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
  // TODO return NBTTag of persistent data to save to world directory, SERVER SIDE ONLY...but that should be handled @ the caller
  return null;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  // TODO load data from persistent file from world directory....  
  }

public void clearAllData()
  {
  this.clientEntry = new PlayerEntry();
  this.playerEntries.clear();
  }

}
