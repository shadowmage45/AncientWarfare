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

import net.minecraft.item.ItemBlock;
import shadowmage.ancient_framework.common.block.AWBlockBase;
import shadowmage.ancient_framework.common.config.ModConfiguration;
import shadowmage.ancient_framework.common.item.AWItemBase;
import shadowmage.ancient_framework.common.registry.entry.Description;
import cpw.mods.fml.common.registry.GameRegistry;

public class ObjectRegistry
{

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

public void createItem(String name, Class<? extends AWItemBase> itemClz, int defaultID){}
public void createBlock(String name, Class<? extends AWBlockBase> blockClz, int defaultID){}
public void createBlock(String name, Class<? extends AWBlockBase> blockClz, Class<? extends ItemBlock> itemClz, int defaultID){}

public void registerBlock(String name, AWBlockBase block, Class<? extends ItemBlock> itemClz)
  {
  ObjectRegistration reg = this.registrationByName.get(name);
  if(reg==null)
    {
    reg = new ObjectRegistration(name, block);    
    }
  block.description = reg.description;
  GameRegistry.registerBlock(block, itemClz, name);
  registerObject(name, reg, block.blockID);
  }

public void registerItem(String name, AWItemBase item)
  {
  ObjectRegistration reg = this.registrationByName.get(name);
  if(reg==null)
    {
    reg = new ObjectRegistration(name, item);    
    }
  item.description = reg.description;
  GameRegistry.registerItem(item, name);
  registerObject(name, reg, item.itemID);
  }

public void registerBlock(String name, AWBlockBase block)
  {
  ObjectRegistration reg = this.registrationByName.get(name);
  if(reg==null)
    {
    reg = new ObjectRegistration(name, block);    
    }
  block.description = reg.description;
  GameRegistry.registerBlock(block, name);
  registerObject(name, reg, block.blockID);
  }

protected void registerObject(String name, ObjectRegistration reg, int id)
  {
  registrationByNumber.put(id, reg);
  registrationByName.put(name, reg);
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
