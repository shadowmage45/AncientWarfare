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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.build.StructureValidationSettings;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginVanillaHandler;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;
import shadowmage.ancient_structures.common.template.rule.TemplateRuleBlock;

public class StructurePluginManager
{

private List<StructureValidationPlugin> loadedValidationPlugins = new ArrayList<StructureValidationPlugin>();
private HashMap<String, Class<? extends StructureValidationSettings>> validationClassesByName = new HashMap<String, Class<? extends StructureValidationSettings>>();
private HashMap<Class<? extends StructureValidationSettings>, String> validationClassesByClass = new HashMap<Class<? extends StructureValidationSettings>, String>();

private List<StructureContentPlugin> loadedContentPlugins = new ArrayList<StructureContentPlugin>();

private HashMap<Class<?extends Entity>, Class<? extends TemplateRule>> entityRules = new HashMap<Class<?extends Entity>, Class<? extends TemplateRule>>();
private HashMap<Block, Class<?extends TemplateRule>> blockRules = new HashMap<Block, Class<?extends TemplateRule>>();
private HashMap<Class<?extends TemplateRule>, String> idByRuleClass = new HashMap<Class<? extends TemplateRule>, String>();
private HashMap<String, Class<?extends TemplateRule>> ruleByID = new HashMap<String, Class<?extends TemplateRule>>();
private HashMap<Block, String> pluginByBlock = new HashMap<Block, String>();
private HashMap<Class<? extends Entity>, String> pluginByEntity = new HashMap<Class<? extends Entity>, String>();

private StructurePluginVanillaHandler vanillaPlugin;

/**
 * should be called during pre-init to load default included block and entity handlers
 * needs to be called prior to loading templates, as the plugin-provided rules are needed by the
 * structure templates
 */
public void loadPlugins()
  {
  vanillaPlugin = new StructurePluginVanillaHandler();
  this.addPlugin(vanillaPlugin);
  
  for(StructureContentPlugin plugin : this.loadedContentPlugins)
    {
    plugin.addHandledBlocks(this);
    plugin.addHandledEntities(this);
    }
  
  for(StructureValidationPlugin plugin : this.loadedValidationPlugins)
    {
    plugin.registerValidationClasses(this);
    }
  }

public void addPlugin(StructureContentPlugin plugin)
  {
  loadedContentPlugins.add(plugin);
  }

public void addPlugin(StructureValidationPlugin plugin)
  {
  loadedValidationPlugins.add(plugin);
  }

public String getPluginNameForEntity(Class<? extends Entity> entityClass)
  {
  return this.pluginByEntity.get(entityClass);
  }

public String getPluginNameFor(Block block)
  {  
  return pluginByBlock.get(block);
  }

public String getPluginNameFor(Class<?extends TemplateRule> ruleClass)
  {
  return this.idByRuleClass.get(ruleClass);
  }

public Class<?extends TemplateRule> getRuleByName(String name)
  {
  return this.ruleByID.get(name);
  }

public TemplateRuleBlock getRuleForBlock(World world, Block block, int turns, int x, int y, int z)
  {
  TemplateRule rule;    
  Class<?extends TemplateRule> clz = blockRules.get(block);
  if(clz!=null)
    {
    int meta = world.getBlockMetadata(x, y, z);  
    try
      {
      rule = clz.getConstructor(World.class, int.class, int.class, int.class, Block.class, int.class, int.class).newInstance(world, x, y, z, block, meta, turns);
      AWLog.logDebug("getting block rule for block during scan: "+rule);
      return (TemplateRuleBlock) rule;
      } 
    catch (InstantiationException e)
      {
      e.printStackTrace();
      } 
    catch (IllegalAccessException e)
      {
      e.printStackTrace();
      } 
    catch (IllegalArgumentException e)
      {
      e.printStackTrace();
      } 
    catch (InvocationTargetException e)
      {
      e.printStackTrace();
      } 
    catch (NoSuchMethodException e)
      {
      e.printStackTrace();
      } 
    catch (SecurityException e)
      {
      e.printStackTrace();
      }      
    }  
  return null;
  }

public TemplateRule getRuleForEntity(World world, Entity entity, int turns, int x, int y, int z)
  {
  return null;//TODO
  }

public void registerEntityHandler(String pluginName, Class<?extends Entity> entityClass, Class<? extends TemplateRule> ruleClass)
  {
  if(ruleByID.containsKey(pluginName))
    {
    if(!ruleByID.get(pluginName).equals(ruleClass))
      {
      Class clz = ruleByID.get(pluginName);
      throw new IllegalArgumentException("Attempt to overwrite "+clz+" with "+ruleClass+" by "+pluginName + " for entityClass: "+entityClass);
      }
    }
  entityRules.put(entityClass, ruleClass);
  ruleByID.put(pluginName, ruleClass);
  idByRuleClass.put(ruleClass, pluginName);
  pluginByEntity.put(entityClass, pluginName);
  }

public void registerBlockHandler(String pluginName, Block block, Class<? extends TemplateRule> ruleClass)
  {  
  if(ruleByID.containsKey(pluginName))
    {
    if(!ruleByID.get(pluginName).equals(ruleClass))
      {
      Class clz = ruleByID.get(pluginName);
      throw new IllegalArgumentException("Attempt to overwrite "+clz+" with "+ruleClass+" by "+pluginName + " for block: "+block);
      }
    }  
  blockRules.put(block, ruleClass);
  ruleByID.put(pluginName, ruleClass);
  idByRuleClass.put(ruleClass, pluginName);
  pluginByBlock.put(block, pluginName);  
  }

public void registerValidationClass(String pluginName, Class<? extends StructureValidationSettings> validationClass)
  {
  validationClassesByClass.put(validationClass, pluginName);
  validationClassesByName.put(pluginName, validationClass);
  }

public TemplateRule getRule(List<String> ruleData)
  {
  Iterator<String> it = ruleData.iterator();
  String name = null;
  int ruleNumber = -1;
  String line;
  List<String> ruleDataPackage = new ArrayList<String>();
  while(it.hasNext())
    {
    line = it.next();
    if(line.startsWith("rule:"))
      {
      continue;
      }
    if(line.startsWith(":endrule"))
      {
      break;
      }
    if(line.startsWith("plugin="))
      {
      name = StringTools.safeParseString("=", line);
      }
    if(line.startsWith("number="))
      {
      ruleNumber = StringTools.safeParseInt("=", line);
      }
    if(line.startsWith("data:"))
      {
      while(it.hasNext())
        {
        line = it.next();
        if(line.startsWith(":enddata"))
          {
          break;
          }
        ruleDataPackage.add(line);
        }
      }
    }
  AWLog.logDebug("parsed rule: "+ruleNumber);
  Class<?extends TemplateRule> clz = getRuleByName(name);
  if(name==null || ruleNumber<=0 || ruleDataPackage.size()==0 || clz==null)
    {
    throw new IllegalArgumentException("Not enough data to create template rule.\n"+
    		"name: "+name+"\n"+
    		"number:"+ruleNumber+"\n"+
    		"ruleDataPackage.size:"+ruleDataPackage.size()+"\n"+
    		"ruleClass: "+clz);
    }
  
  try
    {    
    TemplateRule rule = clz.getConstructor().newInstance();
    AWLog.logDebug("parsed rule of type: "+clz + "  for name: "+name);
    rule.ruleNumber = ruleNumber;
    rule.parseRuleData(ruleDataPackage);
    return rule;
    } 
  catch (InstantiationException e)
    {
    e.printStackTrace();
    } 
  catch (IllegalAccessException e)
    {
    e.printStackTrace();
    } 
  catch (IllegalArgumentException e)
    {
    e.printStackTrace();
    } 
  catch (InvocationTargetException e)
    {
    e.printStackTrace();
    } 
  catch (NoSuchMethodException e)
    {
    e.printStackTrace();
    } 
  catch (SecurityException e)
    {
    e.printStackTrace();
    }
  return null;
  }


}
