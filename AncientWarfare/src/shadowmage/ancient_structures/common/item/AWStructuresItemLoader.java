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
package shadowmage.ancient_structures.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_framework.AWFramework;
import shadowmage.ancient_framework.common.item.AWItemBase;
import shadowmage.ancient_framework.common.registry.ObjectRegistry;
import shadowmage.ancient_framework.common.registry.entry.Description;

public class AWStructuresItemLoader
{

private AWStructuresItemLoader(){}
private static AWStructuresItemLoader instance = new AWStructuresItemLoader();
public static AWStructuresItemLoader instance(){return instance;}
private static ObjectRegistry registry = AWFramework.instance.objectRegistry;
public static CreativeTabs structureTab = new CreativeTabs("Ancient Structures")
{
@Override
public Item getTabIconItem()
  {  
  return structureScanner;
  }

@Override
public String getTranslatedTabLabel()
  {
  return super.getTabLabel();
  }  
};//need to declare this instance prior to any items that use it as their creative tab

public static AWItemBase structureScanner = registry.createItem("item.structurescanner", ItemStructureScanner.class);
public static AWItemBase structureBuilderCreative = registry.createItem("item.structurebuilder", ItemBuilderCreative.class);

public void registerItems()
  {
  addDescription(structureScanner, "item.structurescanner",  0, "item.structurescanner.tooltip", "ancientwarfare:builder/structureScanner1");
  addDescription(structureBuilderCreative, "item.structurebuilder", 0, "item.structurebuilder.tooltip", "ancientwarfare:builder/structureBuilder1");
  }

public Description registerItemSubtyped(AWItemBase item, String baseName)
  {  
  Description d = AWFramework.instance.objectRegistry.registerItem(baseName, item);
  return d;
  }

public Description addDescription(AWItemBase item, String regName, int itemDamage, String tooltipKey, String itemIcon)
  {
  Description d = registry.getDescriptionFor(item.itemID);
  if(d==null)
    {
    d=registry.registerItem(regName, item);
    }
  if(d!=null)
    {
    d.setName(regName, itemDamage);
    d.addTooltip(tooltipKey, itemDamage);
    d.addDisplayStack(new ItemStack(item, 1, itemDamage));
    d.setIconTexture(itemIcon, itemDamage);
    }
  return d;
  }

}
