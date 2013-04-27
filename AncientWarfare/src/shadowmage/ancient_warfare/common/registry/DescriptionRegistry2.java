/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
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
package shadowmage.ancient_warfare.common.registry;

import java.util.HashMap;

import net.minecraft.block.Block;

import shadowmage.ancient_warfare.common.item.AWItemBase;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class DescriptionRegistry2
{


private static DescriptionRegistry2 INSTANCE;
private DescriptionRegistry2(){}

public static DescriptionRegistry2 instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new DescriptionRegistry2();
    }
  return INSTANCE;
  }

private HashMap<Integer, Description> descriptionsByID = new HashMap<Integer, Description>();

public Description registerItem(AWItemBase item, boolean single)
  {
  Description d = new Description(item, single);
  this.descriptionsByID.put(item.itemID, d);
  return d;
  }

public Description registerBlock(Block block, boolean single)
  {
  Description d = new Description(block, single);  
  this.descriptionsByID.put(block.blockID, d);
  return d;
  }

public Description getDescriptionFor(int id)
  {
  return this.descriptionsByID.get(id);
  }


}
