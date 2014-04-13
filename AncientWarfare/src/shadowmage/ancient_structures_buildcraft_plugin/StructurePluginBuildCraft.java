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

import net.minecraftforge.event.ForgeSubscribe;
import shadowmage.ancient_structures.api.IStructurePluginManager;
import shadowmage.ancient_structures.api.StructureContentPlugin;
import shadowmage.ancient_structures.api.StructurePluginRegistrationEvent;
import shadowmage.ancient_structures.common.template.plugin.StructurePluginManager;
import buildcraft.BuildCraftEnergy;

public class StructurePluginBuildCraft extends StructureContentPlugin
{

public StructurePluginBuildCraft()
  {
  
  }

@ForgeSubscribe
public void onRegisterEvent(StructurePluginRegistrationEvent evt)
  {
  evt.register.registerPlugin(this);
  }

@Override
public void addHandledBlocks(IStructurePluginManager manager)
  {
  manager.registerBlockHandler("buildcraftEngine", BuildCraftEnergy.engineBlock, TemplateRuleBuildCraftEngine.class);  
  }

@Override
public void addHandledEntities(IStructurePluginManager manager)
  {
  
  }

}
