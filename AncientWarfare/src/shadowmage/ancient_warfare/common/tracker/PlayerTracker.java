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

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.network.Packet01ModData;
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

private HashMap<String, Boolean> playerControlKeys = new HashMap<String, Boolean>();

public boolean isControlPressed(EntityPlayer player)
  {
  return playerControlKeys.containsKey(player.getEntityName()) ? playerControlKeys.get(player.getEntityName()) : false;
  }

public void handleControlPacket(World world,  NBTTagCompound tag)
  {  
  this.handleControlInput(tag.getString("id"), tag.getBoolean("down"));
  }

public void handleControlInput(String name, boolean down)
  {
  this.playerControlKeys.put(name, down);
  }

@Override
public void onPlayerLogin(EntityPlayer player)
  {
  if(player.worldObj.isRemote)
    {
    return;
    }
  TeamTracker.instance().onPlayerLogin(player);
  ResearchTracker.instance().onPlayerLogin(player);
  NBTTagCompound initTag = new NBTTagCompound();
  NBTTagCompound tag = this.getClientInitData(player);
  if(tag!=null)
    {
    initTag.setCompoundTag("playerData", tag);
    }
  tag = Config.instance().getClientInitData();
  if(tag!=null)
    {
    initTag.setCompoundTag("configData", tag);
    }  
  Packet01ModData init = new Packet01ModData();
  init.setInitData(initTag);
  AWCore.proxy.sendPacketToPlayer(player, init);  
  }

public void handleClientInit(NBTTagCompound tag)
  {
  ResearchTracker.instance().clientEntry = new PlayerEntry();
  ResearchTracker.instance().clientEntry.readFromNBT(tag);
  }

private NBTTagCompound getClientInitData(EntityPlayer player)
  {
  PlayerEntry ent = ResearchTracker.instance().getEntryFor(player);
  if(ent!=null)
    {
    return ent.getNBTTag();
    }
  return null;
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
 
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
 
  }


}
