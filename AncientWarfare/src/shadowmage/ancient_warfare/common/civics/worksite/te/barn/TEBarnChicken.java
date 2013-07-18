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
package shadowmage.ancient_warfare.common.civics.worksite.te.barn;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class TEBarnChicken extends TEWorkSiteAnimalFarm
{

/**
 * 
 */
public TEBarnChicken()
  {
  this.entityClass = EntityChicken.class;
  this.breedingItem = new ItemStack(Item.seeds);
  }

@Override
protected void onCivicUpdate()
  {
  super.onCivicUpdate();
  List<EntityItem> entities = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(minX-1, minY-1, minZ-1, maxX+2, maxY+2, maxZ+2));
  ItemStack stack;
  if(entities!=null)
    {
    for(EntityItem ent : entities)
      {
      if(ent!=null && ent.getEntityItem()!=null)
        {
        stack = ent.getEntityItem();
        if(stack.itemID==Item.egg.itemID )
          {
          if(inventory.canHoldItem(stack, stack.stackSize))
            {
            stack = this.tryAddItemToInventory(stack, this.regularIndices);
            if(stack!=null)
              {
              ent.setEntityItemStack(stack);
              }
            else//stack is null/merged sucessfully
              {
              ent.setDead();
              }
            }
          }
        }
      }
    }
  }

}
