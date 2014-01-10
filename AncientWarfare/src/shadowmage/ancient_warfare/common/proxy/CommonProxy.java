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
package shadowmage.ancient_warfare.common.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.FakePlayerFactory;
import shadowmage.ancient_warfare.common.network.PacketBase;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class CommonProxy
{

/**
 * client-side vars used by debug TPS counter
 */
public long serverTickTime;
public int serverTPS;

public int sentPacketAvg;
public int recPacketAvg;

public boolean isPlayerOp(EntityPlayer player)
  {
  if(player.worldObj.isRemote){return false;}
  return isPlayerOp(player.getEntityName());
  }

private boolean isPlayerOp(String name)
  {
  return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(name);
  }

public EntityPlayer getClientPlayer()
  {
  return null;
  }

public void registerClientData()
  {
  //NOOP server side
  }

/**
 * NOOP server side
 */
public void sendPacketToServer(PacketBase pkt)
  {
  
  }

/**
 * Server Only...NOOP client side
 * @param ent
 * @param pkt
 */

public void sendPacketToAllClientsTracking(Entity ent, PacketBase pkt)
  {
  WorldServer world = (WorldServer)ent.worldObj;
  if(world!=null)
    {
    world.getEntityTracker().sendPacketToAllPlayersTrackingEntity(ent, pkt.get250Packet());
    }
  }

public void sendPacketToPlayer(EntityPlayer player, PacketBase packet)
  {
  PacketDispatcher.sendPacketToPlayer(packet.get250Packet(), (Player)player);
  }

public void sendPacketToAllPlayers(PacketBase packet)
  {
  PacketDispatcher.sendPacketToAllPlayers(packet.get250Packet());
  }

public EntityPlayer getFakePlayer(World world)
  {
  return FakePlayerFactory.get(world, "AncientWarfare");
  }


}
