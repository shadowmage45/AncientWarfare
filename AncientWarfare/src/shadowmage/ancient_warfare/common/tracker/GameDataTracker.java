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

import java.io.File;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.AWStructureModule;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.world_gen.WorldGenManager;

/**
 * handles saving and loading of game data to world directory
 * @author Shadowmage
 *
 */
public class GameDataTracker
{

private static GameDataTracker INSTANCE;
AWGameData gameData = null;

private GameDataTracker()
  {  
  }
public static GameDataTracker instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new GameDataTracker();
    }
  return INSTANCE;
  }

private String lastSavePath = "";
private long lastLoadedTimeStamp = -1L;

public void markGameDataDirty()
  {
  if(this.gameData!=null)
    {
    this.gameData.markDirty();
    }
  }

public void resetAllTrackedData()
  {
  PlayerTracker.instance().clearAllData();
  TeamTracker.instance().clearAllData();
  AWStructureModule.instance().clearAllData();
  WorldGenManager.resetMap();
  AWCraftingManager.instance().resetClientData();
  this.markGameDataDirty();
  this.lastLoadedTimeStamp = -1L;
  }

public void handleWorldLoad(World world)
  {
  if(world.isRemote)
    {
    return;
    }
  Config.logDebug("loading game data from world");
  this.gameData = AWGameData.get(world);
  }

public void handleWorldSave(World world)
  {
  if(world.isRemote)
    {
    return;
    }
  }


}
