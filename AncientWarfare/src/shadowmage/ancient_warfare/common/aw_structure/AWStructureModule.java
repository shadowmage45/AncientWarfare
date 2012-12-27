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
package shadowmage.ancient_warfare.common.aw_structure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

/**
 * config/setup module for structures and structure related stuff....
 * @author Shadowmage
 *
 */
public class AWStructureModule
{

WorldServer world;
MinecraftServer server;
EntityPlayer player;

public AWStructureModule()
  {

  }

public void load()
  {
  this.server = MinecraftServer.getServer();
  long seed = 0;
  ISaveHandler saveHandler = server.getActiveAnvilConverter().getSaveLoader("dummyWorld", true);
  WorldInfo var9 = saveHandler.loadWorldInfo();
  WorldSettings worldSettings;
  WorldType type = WorldType.parseWorldType("DEFAULT");
  if (var9 == null)
    {
    worldSettings = new WorldSettings(seed, server.getGameType(), server.canStructuresSpawn(), server.isHardcore(), type);
    worldSettings.func_82750_a("dummyWorld");
    }
  else
    {
    worldSettings = new WorldSettings(var9);
    }

  this.world = new WorldServer(this.server, saveHandler, "dummyWorld", 101, worldSettings, server.theProfiler);  
  }
}
