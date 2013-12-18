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
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginVanillaHandler;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;
import shadowmage.ancient_structures.common.template.rule.TemplateRuleBlock;

public class StructurePluginManager
{

private List<StructureContentPlugin> loadedPlugins = new ArrayList<StructureContentPlugin>();

private HashMap<Block, StructureRuleRegistration> blockRules = new HashMap<Block, StructureRuleRegistration>();
private HashMap<Class<?extends Entity>, StructureRuleRegistration> entityRules = new HashMap<Class<?extends Entity>, StructureRuleRegistration>();
private HashMap<String, StructureRuleRegistration> registrationByName = new HashMap<String, StructureRuleRegistration>();
private HashMap<Class<?extends TemplateRule>, String> idByRuleClass = new HashMap<Class<? extends TemplateRule>, String>();

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
  
  for(StructureContentPlugin plugin : this.loadedPlugins)
    {
    plugin.addHandledBlocks(this);
    plugin.addHandledEntities(this);
    }
  }

public void addPlugin(StructureContentPlugin plugin)
  {
  loadedPlugins.add(plugin);
  }

public TemplateRuleBlock getRuleForBlock(World world, Block block, int turns, int x, int y, int z)
  {
  TemplateRule rule;    
  StructureRuleRegistration reg = blockRules.get(block);
  if(reg!=null)
    {
    int meta = world.getBlockMetadata(x, y, z);  
    try
      {
      rule = reg.ruleClass.getConstructor(World.class, int.class, int.class, int.class, Block.class, int.class, int.class).newInstance(world, x, y, z, block, meta, turns);
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
  StructureRuleRegistration reg = new StructureRuleRegistration(pluginName, ruleClass); 
  entityRules.put(entityClass, reg);
  registrationByName.put(pluginName, reg); 
  idByRuleClass.put(ruleClass, pluginName);
  }

public void registerBlockHandler(String pluginName, Block block, Class<? extends TemplateRule> ruleClass)
  {
  StructureRuleRegistration reg = new StructureRuleRegistration(pluginName, ruleClass); 
  blockRules.put(block, reg);
  registrationByName.put(pluginName, reg);
  idByRuleClass.put(ruleClass, pluginName);
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
    if(line.startsWith("rule: "))
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
  StructureRuleRegistration reg = registrationByName.get(name);
  if(name==null || ruleNumber<=0 || ruleDataPackage.size()==0 || reg==null)
    {
    throw new IllegalArgumentException("not enough data to create template rule");
    }
  Class<?extends TemplateRule> clz = reg.ruleClass;
  try
    {
    TemplateRule rule = clz.getConstructor().newInstance();
    rule.parseRuleData(ruleDataPackage);
    } catch (InstantiationException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } catch (IllegalAccessException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } catch (IllegalArgumentException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } catch (InvocationTargetException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } catch (NoSuchMethodException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } catch (SecurityException e)
    {
    // TODO Auto-generated catch block
    e.printStackTrace();
    }
  return null;
  }

public String getPluginNameFor(Block block)
  {
  StructureRuleRegistration reg = blockRules.get(block);  
  return reg!=null ? reg.pluginName : null;
  }

public String getPluginNameFor(Class<?extends TemplateRule> ruleClass)
  {
  return this.idByRuleClass.get(ruleClass);
  }

public class StructureRuleRegistration
{
String pluginName;
Class<?extends TemplateRule> ruleClass;

public StructureRuleRegistration(String name, Class<?extends TemplateRule> ruleClass)
  {
  this.pluginName = name;
  this.ruleClass = ruleClass;
  }
}

}
