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
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginVanillaBlocks;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class StructurePluginManager
{

private List<StructureContentPlugin> loadedPlugins = new ArrayList<StructureContentPlugin>();
private HashMap<Block, StructureContentPlugin> blockHandlers = new HashMap<Block, StructureContentPlugin>();
private HashMap<Class, StructureContentPlugin> entityHandlers = new HashMap<Class, StructureContentPlugin>();

private List<Class> entityInputCache = new ArrayList<Class>();
private List<Block> blockInputCache = new ArrayList<Block>();

/**
 * should be called during pre-init to load default included block and entity handlers
 * needs to be called prior to loading templates, as the plugin-provided rules are needed by the
 * structure templates
 */
public void loadDefaultPlugins()
  {
  this.addPlugin(new StructurePluginVanillaBlocks());
  }

public void addPlugin(StructureContentPlugin plugin)
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
          "Attempt to overwrite existing block handler");
      }
    }
  blockInputCache.clear();
  
  plugin.addHandledEntities(entityInputCache);
  for(Class clz : entityInputCache)
    {
    if(Entity.class.isAssignableFrom(clz))
      {
      if(!entityHandlers.containsKey(clz))
        {
        entityHandlers.put(clz, plugin);
        }
      else
        {
        AWLog.logError("Could not register entity for plugin:\n" +
            "entity:"+ clz +"\n"+
            "plugin:"+ plugin.getClass() +"\n"+
            "Attempt to overwrite existing entity handler");
        }
      }
    else
      {
      AWLog.logError("Could not register entity for plugin:\n" +
          "entity:"+ clz +"\n"+
          "plugin:"+ plugin.getClass() +"\n"+
          "Class to register was not an Entity subclass");
      }    
    }
  }

public TemplateRule getRuleForBlock(World world, Block block, int face, int x, int y, int z, List<TemplateRule> priorRules)
  {
  StructureContentPlugin plugin = blockHandlers.get(block);  
  return plugin != null ? plugin.getRuleForBlock(world, block, face, x, y, z, priorRules) : null;
  }

public TemplateRule getRuleForEntity(World world, Entity entity, int face, int x, int y, int z, List<TemplateRule> priorRules)
  {
  StructureContentPlugin plugin = entityHandlers.get(entity);
  return plugin != null ? plugin.getRuleForEntity(world, entity, face, x, y, z, priorRules) : null;
  }

}
