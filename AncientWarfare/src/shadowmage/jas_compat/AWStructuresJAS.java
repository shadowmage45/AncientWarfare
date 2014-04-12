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
package shadowmage.jas_compat;

import jas.api.CompatibilityRegistrationEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_warfare.common.config.Config;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod( modid = "AncientStructuresJAS", name="Ancient Structures JAS", version="1.0.001-alpha", dependencies="required-after:AncientStructures")
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = true,
versionBounds="["+"1.0.001-alpha"+",)"
)

//https://github.com/Crudedragos/JustAnotherSpawner/archive/5be05aeb67c90005640f3a8793b8b9d13384ccc9.zip -- 1.6.4 source zip
//https://github.com/Crudedragos/JustAnotherSpawner/archive/a64880eb1c68fd4246dd6fb8c8f00e57929149f4.zip -- later (latest) 1.6.4 source zip

public class AWStructuresJAS
{

@Instance("AncientStructuresJAS")
public static AWStructuresJAS instance;  

private AWStructureInterpreter interpreter;

@EventHandler
public void preInit(FMLPreInitializationEvent evt) 
  {
  AWLog.log("Loading Ancient Warfare Structures JAS Compatibility Plugin configuration.");
  MinecraftForge.EVENT_BUS.register(this);
  interpreter = new AWStructureInterpreter();
  interpreter.loadStructureGroups(new Configuration(evt.getSuggestedConfigurationFile()));
  AWLog.log("Ancient Warfare Structures JAS Compatibility Plugin configuration has been loaded.");
  }

@EventHandler
public void init(FMLInitializationEvent evt)
  {
  }

@EventHandler
public void postInit(FMLPostInitializationEvent evt)
  {
  }

@ForgeSubscribe
public void onCompatEvent(CompatibilityRegistrationEvent evt)
  {
  AWLog.log("Registering Ancient Warfare Structures JAS Compatibility Plugin with JAS.");
  evt.loader.registerObject(interpreter);
  }

}
