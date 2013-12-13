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
package shadowmage.ancient_framework;

import java.io.File;
import java.util.logging.Logger;

import shadowmage.ancient_framework.common.config.AWConfig;
import shadowmage.ancient_framework.common.registry.ObjectRegistry;
import shadowmage.ancient_warfare.common.network.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;



@Mod( modid = "AncientWarfare", name="Ancient Warfare", version=AWConfig.VERSION)
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = true,
packetHandler = PacketHandler.class,
channels = {"AW_mod"},
versionBounds="["+AWConfig.VERSION+",)"
)
public class AWFramework extends AWMod
{

@Instance("AncientWarfare")
public static AWFramework instance;

public ObjectRegistry objectRegistry;
/**
 * 
 */
public AWFramework()
  {
  // TODO Auto-generated constructor stub
  }

@Override
public void loadConfiguration(File config, Logger log)
  {
  this.config = new AWConfig(config, log, AWConfig.VERSION);
  objectRegistry = new ObjectRegistry(this.config);
  }

@Override
@EventHandler
public void preInit(FMLPreInitializationEvent evt)
  {
  // TODO Auto-generated method stub

  }

@Override
@EventHandler
public void init(FMLInitializationEvent evt)
  {
  // TODO Auto-generated method stub

  }

@Override
@EventHandler
public void postInit(FMLPostInitializationEvent evt)
  {
  // TODO Auto-generated method stub

  }

@Override
@EventHandler
public void serverPreStart(FMLServerAboutToStartEvent evt)
  {
  // TODO Auto-generated method stub

  }

@Override
@EventHandler
public void serverStarting(FMLServerStartingEvent evt)
  {
  // TODO Auto-generated method stub

  }

@Override
@EventHandler
public void serverStarted(FMLServerStartedEvent evt)
  {
  // TODO Auto-generated method stub

  }

@Override
@EventHandler
public void serverStopping(FMLServerStoppingEvent evt)
  {
  // TODO Auto-generated method stub

  }

@Override
@EventHandler
public void serverStopped(FMLServerStoppedEvent evt)
  {
  // TODO Auto-generated method stub

  }

}
