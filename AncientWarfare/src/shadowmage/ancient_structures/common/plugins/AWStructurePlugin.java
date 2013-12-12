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
package shadowmage.ancient_structures.common.plugins;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import shadowmage.ancient_structures.common.structures.data.rules.BlockRule;


/**
 * base class for structure module plugins.
 * plugins will be responsible for handling additional non-vanilla blocks during scanning and placement
 * plugins will also be responsible for handling additional non-vanilla entities during scanning and placement
 * plugins can be used to add additional world gen/village gen structures8
 * @author Shadowmage
 *
 */
public class AWStructurePlugin
{

String pluginName;

public AWStructurePlugin()
  {
  
  }

public void loadWorldGenStructures(){}
public void loadVillageGenStructures(){}
public void registerBlockHandlingCapabilities(){}
public void registerEntityHandlingCapabilities(){}
public void handleBlockScan(World world, int x, int y, int z, StructurePluginData data){}
public void handleEntityScan(Entity entity, StructurePluginData data){};
public void handleBlockPlacement(World world, int x, int y, int z, BlockRule rule, StructurePluginData data){}
public void handleEntityPlacement(World world, Object entityRule, StructurePluginData data){}

}
