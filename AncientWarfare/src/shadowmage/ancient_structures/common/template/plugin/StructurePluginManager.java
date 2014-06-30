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
import net.minecraftforge.common.MinecraftForge;
import shadowmage.ancient_structures.AWStructures;
import shadowmage.ancient_structures.api.IStructurePluginLookup;
import shadowmage.ancient_structures.api.IStructurePluginManager;
import shadowmage.ancient_structures.api.StructureContentPlugin;
import shadowmage.ancient_structures.api.StructurePluginRegistrationEvent;
import shadowmage.ancient_structures.api.TemplateRule;
import shadowmage.ancient_structures.api.TemplateRuleBlock;
import shadowmage.ancient_structures.api.TemplateRuleEntity;
import shadowmage.ancient_structures.common.template.load.TemplateParser;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginGates;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginModDefault;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginNpcs;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginVanillaHandler;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginVehicles;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleModBlocks;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class StructurePluginManager implements IStructurePluginManager, IStructurePluginLookup
{

private List<StructureContentPlugin> loadedContentPlugins = new ArrayList<StructureContentPlugin>();

private HashMap<Class<?extends Entity>, Class<? extends TemplateRule>> entityRules = new HashMap<Class<?extends Entity>, Class<? extends TemplateRule>>();
private HashMap<Block, Class<?extends TemplateRule>> blockRules = new HashMap<Block, Class<?extends TemplateRule>>();
private HashMap<Class<?extends TemplateRule>, String> idByRuleClass = new HashMap<Class<? extends TemplateRule>, String>();
private HashMap<String, Class<?extends TemplateRule>> ruleByID = new HashMap<String, Class<?extends TemplateRule>>();
private HashMap<Block, String> pluginByBlock = new HashMap<Block, String>();
private HashMap<Class<? extends Entity>, String> pluginByEntity = new HashMap<Class<? extends Entity>, String>();

private StructurePluginVanillaHandler vanillaPlugin;

public void loadPlugins()
  {  
  vanillaPlugin = new StructurePluginVanillaHandler();  
  this.registerPlugin(vanillaPlugin);
  this.registerPlugin(new StructurePluginGates());
  StructurePluginNpcs.load();
  StructurePluginVehicles.load();
    
  MinecraftForge.EVENT_BUS.post(new StructurePluginRegistrationEvent(this));
  this.registerPlugin(new StructurePluginModDefault());
  
  for(StructureContentPlugin plugin : this.loadedContentPlugins)
    {
    plugin.addHandledBlocks(this);
    plugin.addHandledEntities(this);
    }
  }

public void registerPlugin(StructureContentPlugin plugin)
  {
  loadedContentPlugins.add(plugin);
  }

public String getPluginNameForEntity(Class<? extends Entity> entityClass)
  {
  return this.pluginByEntity.get(entityClass);
  }

public String getPluginNameFor(Block block)
  {  
  return pluginByBlock.get(block);
  }

@Override
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
  int meta = world.getBlockMetadata(x, y, z);  
  if(clz!=null)
    {
    try
      {
      rule = clz.getConstructor(World.class, int.class, int.class, int.class, Block.class, int.class, int.class).newInstance(world, x, y, z, block, meta, turns);
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

public TemplateRuleEntity getRuleForEntity(World world, Entity entity, int turns, int x, int y, int z)
  {
  Class<? extends Entity> entityClass = entity.getClass();
  if(this.entityRules.containsKey(entityClass))
    {
    Class<? extends TemplateRule> entityRuleClass = this.entityRules.get(entityClass);
    if(entityRuleClass!=null)
      {
      try
        {
        return (TemplateRuleEntity) entityRuleClass.getConstructor(World.class, Entity.class, int.class, int.class, int.class, int.class).newInstance(world, entity, turns, x, y, z);
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
    }
  return null;//TODO
  }

@Override
public void registerEntityHandler(String pluginName, Class<?extends Entity> entityClass, Class<? extends TemplateRuleEntity> ruleClass)
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

@Override
public void registerBlockHandler(String pluginName, Block block, Class<? extends TemplateRuleBlock> ruleClass)
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

public static final TemplateRule getRule(List<String> ruleData, String ruleType) throws TemplateRuleException
{
Iterator<String> it = ruleData.iterator();
String name = null;
int ruleNumber = -1;
String line;
List<String> ruleDataPackage = new ArrayList<String>();
while(it.hasNext())
  {
  TemplateParser.lineNumber++;
  line = it.next();
  if(line.startsWith(ruleType+":"))
    {
    continue;
    }
  if(line.startsWith(":end"+ruleType))
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
Class<?extends TemplateRule> clz = AWStructures.instance.pluginManager.getRuleByName(name);
if(clz==null)
  {
  throw new TemplateRuleException("Could not locate plugin for rule type: "+name);
  }
if(name==null || ruleNumber<0 || ruleDataPackage.size()==0 || clz==null)
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
  rule.parseRule(ruleNumber, ruleDataPackage);
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

public static class TemplateRuleException extends Exception
{

public TemplateRuleException(String message) 
{
super(message);
}
}

}
