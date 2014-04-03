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
package shadowmage.ancient_structures_buildcraft_plugin;

import net.minecraftforge.common.MinecraftForge;
import shadowmage.ancient_structures.AWStructures;
import shadowmage.ancient_structures.client.proxy.CommonProxy;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.item.AWStructuresItemLoader;
import shadowmage.ancient_structures.common.manager.BlockDataManager;
import shadowmage.ancient_structures.common.manager.WorldGenStructureManager;
import shadowmage.ancient_structures.common.template.load.TemplateLoader;
import shadowmage.ancient_structures.common.template.plugin.StructurePluginManager;
import shadowmage.ancient_structures.common.utils.AWGameData;
import shadowmage.ancient_structures.common.world_gen.StructureMap;
import shadowmage.ancient_structures.common.world_gen.WorldStructureGenerator;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;


@Mod( modid = "AncientStructuresBCPlugin", name="Ancient Structures BuildCraft Plugin", version=Config.VERSION, dependencies="required-after:AncientStructures")
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = true,
versionBounds="["+Config.VERSION+",)"
)

public class AWStructuresBCPlugin
{
@Instance("AncientStructures")
public static AWStructuresBCPlugin instance;  

@EventHandler
public void preInit(FMLPreInitializationEvent evt) 
  {  
  AWLog.log("Ancient Warfare Structures BuildCraft Plugin Starting Loading.  Version: "+Config.VERSION);
  AWStructures.instance.pluginManager.addPlugin(new StructurePluginBuildCraft());
  AWLog.log("Ancient Warfare Structures BuildCraft Plugin Pre-Init finished.");
  }

@EventHandler
public void init(FMLInitializationEvent evt)
  {

  }

@EventHandler
public void postInit(FMLPostInitializationEvent evt)
  {

  }

}
