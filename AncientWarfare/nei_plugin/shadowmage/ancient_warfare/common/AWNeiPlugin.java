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
package shadowmage.ancient_warfare.common;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.nei.AWNeiRecipeHandler;
import shadowmage.ancient_warfare.common.nei.NEIConfig;
import shadowmage.ancient_warfare.common.proxy.CommonProxyNEI;
import codechicken.nei.api.API;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod( modid = "AncientWarfareNeiPlugin", name="Ancient WarfareNeiPlugin", version=NEIConfig.version, dependencies="required-after:NotEnoughItems; required-after:AncientWarfare")
@NetworkMod
(
clientSideRequired = false,
serverSideRequired = false,
versionBounds=NEIConfig.version
)

public class AWNeiPlugin
{

@SidedProxy(clientSide = "shadowmage.ancient_warfare.client.proxy.ClientProxyNEI", serverSide = "shadowmage.ancient_warfare.common.proxy.CommonProxyNEI")
public static CommonProxyNEI proxy;
@Instance("AncientWarfareNeiPlugin")
public static AWNeiPlugin instance;  

protected AWNeiRecipeHandler handler;

/**
 * load settings, config, items
 * @param evt
 */
@PreInit
public void preInit(FMLPreInitializationEvent evt) 
  {  
  /**
   * load config file and setup logger
   */ 
  Config.log("Starting Loading NEI Plugin.  Version: "+NEIConfig.version);
  
  Config.log("Ancient Warfare NEI Plugin Pre-Init finished.");
  }

/**
 * load registry stuff....
 * @param evt
 */
@Init
public void init(FMLInitializationEvent evt)
  {
  Config.log("Ancient Warfare NEI Plugin  Init started."); 
  Config.log("Ancient Warfare NEI Plugin  Init completed.");
  }

/**
 * finalize config settings, load NPCs (which rely on other crap from other mods..potentially)
 * @param evt
 */
@PostInit
public void load(FMLPostInitializationEvent evt)
  {  
  Config.log("Ancient Warfare NEI Plugin  Post-Init started");  
  handler = new AWNeiRecipeHandler();
  API.registerRecipeHandler((ICraftingHandler)handler);
  API.registerUsageHandler((IUsageHandler)handler);
  Config.log("Ancient Warfare NEI Plugin  Post-Init completed.  Successfully completed all loading stages."); 
  }

}
