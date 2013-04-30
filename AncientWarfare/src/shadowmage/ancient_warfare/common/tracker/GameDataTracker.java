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
import shadowmage.ancient_warfare.common.world_gen.WorldGenManager;

/**
 * handles saving and loading of game data to world directory
 * @author Shadowmage
 *
 */
public class GameDataTracker
{

private static GameDataTracker INSTANCE;
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

public void resetAllTrackedData()
  {
  PlayerTracker.instance().clearAllData();
  TeamTracker.instance().clearAllData();
  AWStructureModule.instance().clearAllData();
  WorldGenManager.resetMap();
  this.lastLoadedTimeStamp = -1L;
  }

public void handleWorldLoad(World world)
  {
  if(world.isRemote)
    {
    return;
    }
  /**
   * load stats file, populate globalTag
   */  
  File part1 = MinecraftServer.getServer().getFile("");
  String part2 = MinecraftServer.getServer().getFolderName();      
  String filePart2 = part1+"/saves/"+part2;
  if((MinecraftServer.getServer() instanceof DedicatedServer))
    {
    filePart2 = part1+"/"+part2;
    }

  if(!filePart2.equals(lastSavePath))
    {
    this.lastSavePath = filePart2;
    this.resetAllTrackedData();
    }

  File rawFile = new File(filePart2, "AWWorldData.dat");
 
  NBTTagCompound tag = null;
  try
    {
    tag = CompressedStreamTools.read(rawFile);
    }
  catch (IOException e)
    {
    Config.logError("could not load AWWorldData, no such file, or improper format");
    return;
    }
  if(tag==null)
    {
    //Config.logError("Null data tag loaded from file, aborting loading of world stats");
    return;
    }
  long time = tag.getLong("tS");
  
  if(this.lastLoadedTimeStamp>0)
    {
    /**
     * compare timestamps...if the just read one is OLDER/THE SAME as the existing copy....do nothing
     */    
    if(time <= this.lastLoadedTimeStamp)
      {        
      return;
      }       
    }   
  
  this.lastLoadedTimeStamp = time;
  
  
  
  if(tag.hasKey("playerData"))
    {
    PlayerTracker.instance().readFromNBT(tag.getCompoundTag("playerData"));
    }
  if(tag.hasKey("teamData"))
    {
    TeamTracker.instance().readFromNBT(tag.getCompoundTag("teamData"));
    }
  if(tag.hasKey("builders"))
    {
    AWStructureModule.instance().readFromNBT(tag.getCompoundTag("builders"));
    }  
  if(tag.hasKey("structMap"))
    {
    WorldGenManager.instance().readFromNBT(tag.getCompoundTag("structMap"));
    }
  }

public void handleWorldSave(World world)
  {
  if(world.isRemote)
    {
    return;
    }
  NBTTagCompound tag = new NBTTagCompound();
  /**
   * save system time into tag
   */
  tag.setLong("tS", System.currentTimeMillis());
  File part1 = MinecraftServer.getServer().getFile("");
  String part2 = MinecraftServer.getServer().getFolderName();      
  String filePart2 = part1+"/saves/"+part2;
  if((MinecraftServer.getServer() instanceof DedicatedServer))
    {
    filePart2 = part1+"/"+part2;
    }
  File rawFile = new File(filePart2,"AWWorldData.dat");  
  try
    {
    this.lastSavePath = filePart2;
    NBTTagCompound setTag = PlayerTracker.instance().getNBTTag();
    
    if(setTag!=null)
      {
      tag.setCompoundTag("playerData", setTag);
      }
    setTag = TeamTracker.instance().getNBTTag();
    if(setTag!=null)
      {
      tag.setCompoundTag("teamData", setTag);
      }
    setTag = AWStructureModule.instance().getNBTTag();
    if(setTag!=null)
      {
      tag.setCompoundTag("builders", setTag);
      } 
    setTag = WorldGenManager.instance().getNBTTag();
    if(setTag!=null)
      {
      tag.setCompoundTag("structMap", setTag);
      }
    if(tag==null || rawFile==null)
      {
//      Config.logDebug("null tag or rawFile detected on WorldSave");
      return;
      }
    CompressedStreamTools.write(tag, rawFile);
    
//    /**
//     * this ensures that the savedData is the most-recent data...
//     */
//    WorldGenStructureMap map = WorldGenManager.dimensionStructures.get(world.getWorldInfo().getDimension());
//    if(map!=null)
//      {
//      Config.logDebug("setting structure map to save for dimension: "+world.getWorldInfo().getDimension());
//      world.perWorldStorage.setData("AWstructMap"+world.getWorldInfo().getDimension(), map);      
//      }
    
    }
  catch (IOException e)
    {
    e.printStackTrace();
    Config.logError("SEVERE ERROR SAVING WORLD STATS FILE TO WORLD FOLDER");
    return;
    }  
  }


}
