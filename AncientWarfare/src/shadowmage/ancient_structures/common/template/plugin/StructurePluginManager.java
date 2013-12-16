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
package shadowmage.ancient_structures.common.template.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class StructurePluginManager
{

private List<StructurePlugin> loadedPlugins = new ArrayList<StructurePlugin>();
private HashMap<Block, StructurePlugin> blockHandlers = new HashMap<Block, StructurePlugin>();
private HashMap<Class, StructurePlugin> entityHandlers = new HashMap<Class, StructurePlugin>();

private List<Class> entityInputCache = new ArrayList<Class>();
private List<Block> blockInputCache = new ArrayList<Block>();

public void addPlugin(StructurePlugin plugin)
  {
  this.loadedPlugins.add(plugin);  
  
  plugin.addHandledBlocks(blockInputCache);
  for(Block b : blockInputCache)
    {
    if(!blockHandlers.containsKey(b))
      {
      blockHandlers.put(b, plugin);      
      }
    else
      {
      AWLog.logError("Could not register block for plugin:\n" +
      		"block:"+ b +"\n"+
      		"plugin:"+ plugin.getClass() +"\n"+
          "Attempt to overwrite block handler for : "+b);
      }
    }
  blockInputCache.clear();
  
  plugin.addHandledEntities(entityInputCache);
  for(Class clz : entityInputCache)
    {
    if(!entityHandlers.containsKey(clz) && Entity.class.isAssignableFrom(clz))
      {
      entityHandlers.put(clz, plugin);
      }
    else
      {
      AWLog.logError("Could not register entity for plugin:\n" +
          "entity:"+ clz +"\n"+
          "plugin:"+ plugin.getClass() +"\n"+
          "Improper entity class or attempt to overwrite existing handler");
      }
    }
  }

public TemplateRule getRuleForBlock(World world, Block block, int face, int x, int y, int z, List<TemplateRule> priorRules)
  {
  StructurePlugin plugin = blockHandlers.get(block);  
  return plugin != null ? plugin.getRuleForBlock(world, block, face, x, y, z, priorRules) : null;
  }

public TemplateRule getRuleForEntity(World world, Entity entity, int face, int x, int y, int z, List<TemplateRule> priorRules)
  {
  StructurePlugin plugin = entityHandlers.get(entity);
  return plugin != null ? plugin.getRuleForEntity(world, entity, face, x, y, z, priorRules) : null;
  }

}
