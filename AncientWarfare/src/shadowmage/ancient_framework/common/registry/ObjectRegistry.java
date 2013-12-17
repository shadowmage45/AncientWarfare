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
package shadowmage.ancient_framework.common.registry;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import shadowmage.ancient_framework.common.block.AWBlockBase;
import shadowmage.ancient_framework.common.config.ModConfiguration;
import shadowmage.ancient_framework.common.item.AWItemBase;
import shadowmage.ancient_framework.common.item.AWItemBlockBase;
import shadowmage.ancient_framework.common.registry.entry.Description;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ObjectRegistry
{

private int nextItemID = 24000;
private int nextBlockID = 3700;
private ModConfiguration config;
HashMap<String, ObjectRegistration> registrationByName = new HashMap<String, ObjectRegistration>();
HashMap<Integer, ObjectRegistration> registrationByNumber = new HashMap<Integer, ObjectRegistration>();
/**
 * 
 */
public ObjectRegistry(ModConfiguration config)
  {
  this.config = config;
  }

public Description getDescriptionFor(String name)
  {
  ObjectRegistration reg = this.registrationByName.get(name);
  if(reg!=null)
    {
    return reg.description;
    }
  return null;
  }

public Description getDescriptionFor(int id)
  {
  ObjectRegistration reg = this.registrationByNumber.get(id);
  if(reg!=null)
    {
    return reg.description;
    }
  return null;
  }

public AWItemBase createItem(String name, Class<? extends AWItemBase> itemClz)
  {
  try
    {
    AWItemBase item = itemClz.getDeclaredConstructor(int.class).newInstance(config.getItemID(name, nextItemID++));
    registerItem(name, item);
    return item;
    } 
  catch (Exception e)
    {   
    e.printStackTrace();
    } 
  return null;
  }

public AWBlockBase createBlock(String name, Class<? extends AWBlockBase> blockClz)
  {
  try
    {
    AWBlockBase block = blockClz.getDeclaredConstructor(int.class).newInstance(config.getBlockID(name, nextBlockID++));
    registerBlock(name, block);
    return block;
    } 
  catch (Exception e)
    {   
    e.printStackTrace();
    } 
  return null;
  }

public AWBlockBase createBlock(String name, Class<? extends AWBlockBase> blockClz, Class<? extends ItemBlock> itemClz)
  {
  try
    {
    AWBlockBase item = blockClz.getDeclaredConstructor(int.class).newInstance(config.getBlockID(name, nextBlockID++));
    registerBlock(name, item, itemClz);
    return item;
    } 
  catch (Exception e)
    {   
    e.printStackTrace();
    } 
  return null;
  }

public <T>T createItemBasic(String name, Class<T> itemClz)
  {
  Item item;
  try
    {
    item = (Item) itemClz.getDeclaredConstructor(int.class).newInstance(config.getItemID(name, nextItemID++));
    return (T) item;
    } 
  catch (Exception e)
    {
    e.printStackTrace();
    }   
  return null;
  }

public Description registerBlock(String name, AWBlockBase block, Class<? extends ItemBlock> itemClz)
  {
  ObjectRegistration reg = this.registrationByName.get(name);
  if(reg==null)
    {
    reg = new ObjectRegistration(name, block);    
    }
  block.description = reg.description;
  GameRegistry.registerBlock(block, itemClz, name);
  registerObject(name, reg, block.blockID);
  AWItemBlockBase item = (AWItemBlockBase) Item.itemsList[block.blockID];
  item.description = block.description;
  return block.description;
  }

public Description registerItem(String name, AWItemBase item)
  {
  ObjectRegistration reg = this.registrationByName.get(name);
  if(reg==null)
    {
    reg = new ObjectRegistration(name, item);    
    }
  item.description = reg.description;
  GameRegistry.registerItem(item, name);
  registerObject(name, reg, item.itemID);
  return item.description;
  }

public Description registerBlock(String name, AWBlockBase block)
  {
  ObjectRegistration reg = this.registrationByName.get(name);
  if(reg==null)
    {
    reg = new ObjectRegistration(name, block);    
    }
  block.description = reg.description;
  GameRegistry.registerBlock(block, name);
  registerObject(name, reg, block.blockID);
  return block.description;
  }

public Description registerObject(String name, Object obj, int id)
  {
  ObjectRegistration reg = this.registrationByName.get(name);
  if(reg==null)
    {
    reg = new ObjectRegistration(name, obj);
    }
  registerObject(name, reg, id);
  return reg.description;
  }

protected void registerObject(String name, ObjectRegistration reg, int id)
  {
  registrationByNumber.put(id, reg);
  registrationByName.put(name, reg);
  LanguageRegistry.instance().addName(reg.obj, name);
  }

private class ObjectRegistration
{
private Object obj;
private String name;
private Class clz;
private Description description = new Description();
private ObjectRegistration(String name, Object obj)
  {
  this.obj = obj;
  this.name = name;
  this.clz = obj.getClass();
  }
}

}
