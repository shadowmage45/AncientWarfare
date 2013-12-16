/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_structures.common.structures.data.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.common.inventory.AWInventoryBasic;
import shadowmage.ancient_framework.common.utils.NBTReader;
import shadowmage.ancient_framework.common.utils.NBTWriter;
import shadowmage.ancient_framework.common.utils.StringTools;

public class InventoryRule
{

public AWInventoryBasic items;
public int ruleNumber;
public int inventorySize;

/**
 * 
 */
private InventoryRule()
  {
  // TODO Auto-generated constructor stub
  }

public static InventoryRule parseLines(List<String> ruleLines)
  {
  InventoryRule rule = new InventoryRule();
  Iterator<String> it = ruleLines.iterator();
  String line;
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("number"))
      {
      rule.ruleNumber = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("size"))
      {
      rule.inventorySize = StringTools.safeParseInt("=", line);
      rule.items = new AWInventoryBasic(rule.inventorySize);
      }
    else if(line.toLowerCase().startsWith("data:"))
      {
      if(rule.items==null)
        {
        rule.ruleNumber = -1;
        return rule;
        }
      ArrayList<String>dataLines = new ArrayList<String>();
      while(it.hasNext())
        {
        line = it.next();   
        if(line.toLowerCase().startsWith(":enddata"))
          {
          NBTTagCompound inventoryData = NBTReader.readTagFromLines(dataLines);
          if(inventoryData!=null)
            {
            rule.items.readFromNBT(inventoryData);
            }
          break;
          }
        dataLines.add(line);
        }
      }
    }
  return rule;
  }

/**
 * will return null if found no items in inventory (empty rule)
 * @param inv
 * @param num
 * @return
 */
public static InventoryRule populateRule(IInventory inv, int num)
  {
  InventoryRule rule = new InventoryRule();
  rule.ruleNumber = num;
  boolean foundItem = false;
  rule.items = new AWInventoryBasic(inv.getSizeInventory());
  rule.inventorySize = rule.items.getSizeInventory();
  ItemStack stack;
  for(int i = 0; i < rule.inventorySize; i++)
    {
    stack = inv.getStackInSlot(i);
    if(stack!=null)
      {
      foundItem = true;
      rule.items.setInventorySlotContents(i, stack.copy());
      }
    }   
  if(!foundItem)
    {
    return null;
    }
  return rule;
  }

public List<String> getRuleLines()
  {
  ArrayList<String> lines = new ArrayList<String>();
  lines.add("inventory:");
  lines.add("number="+this.ruleNumber);
  lines.add("size="+this.inventorySize);
  List<String> tagLines = NBTWriter.writeNBTToStrings(this.items.getNBTTag());  
  lines.add("data:");
  lines.addAll(tagLines);
  lines.add(":enddata");
  lines.add(":endinventory");
  return lines;
  }


}
