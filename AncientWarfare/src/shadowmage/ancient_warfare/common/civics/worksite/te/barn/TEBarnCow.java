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
package shadowmage.ancient_warfare.common.civics.worksite.te.barn;

import java.util.List;

import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;


public class TEBarnCow extends TEWorkSiteAnimalFarm
{

protected ItemStack bucketFilter = new ItemStack(Item.bucketEmpty);
/**
 * 
 */
public TEBarnCow()
  {
  this.entityClass = EntityCow.class;
  this.breedingItem = new ItemStack(Item.wheat);
  }


@Override
protected void scan()
  {  
  List<EntityAnimal> entities = worldObj.getEntitiesWithinAABB(entityClass, getWorkBounds());
  breedingList.clear();
  cullableList.clear();
  if(entities!=null && !entities.isEmpty())
    {
    int age;
    for(EntityAnimal ent : entities)
      {
      age = ent.getGrowingAge();
      if(age==0)
        {
        breedingList.add(ent);
        }
      if(age>=0)
        {
        cullableList.add(ent);
        }   
      }
    }
  EntityAnimal first;
  EntityAnimal second;
  boolean hasFood = inventory.containsAtLeast(breedingItem, 2);
  while(breedingList.size()>=2 && hasFood)
    {
    //do two animals at once...
    first = breedingList.poll();//.remove(0);
    second = breedingList.poll();//.remove(0);
    this.addWorkPoint(first, TargetType.BARN_BREED);
    this.addWorkPoint(second, TargetType.BARN_BREED);
    }
  int cullCount = cullableList.size() - this.maxAnimalCount;
  int bucketCount = inventory.getCountOf(bucketFilter);
  int emptyCount = inventory.getEmptySlotCount();
  for(int i = 0; i < cullableList.size(); i++)
    {
    first = cullableList.poll();//.remove(0);
    if(i>=bucketCount && i>=cullCount)
      {
      break;
      }
    if(i<bucketCount && emptyCount>=1)
      {
      emptyCount--;
      this.addWorkPoint(first, TargetType.BARN_MILK);
      }
    if(i<cullCount && emptyCount>=1)
      {      
      this.addWorkPoint(first, TargetType.BARN_CULL);
      }    
    }  
  }

@Override
protected void doWork(NpcBase npc, WorkPoint p)
  {
  super.doWork(npc, p);
  if(p.work==TargetType.BARN_MILK && p.target!=null && inventory.containsAtLeast(bucketFilter, 1))
    {
    inventory.tryRemoveItems(bucketFilter, 1);
    ItemStack stack = inventory.tryMergeItem(new ItemStack(Item.bucketMilk));
    stack = overflow.tryMergeItem(stack);
    InventoryTools.dropItemInWorld(worldObj, stack, xCoord+0.5d, yCoord+1.d, zCoord+0.5d);
    }
  }

@Override
protected TargetType validateWorkPoint(WorkPoint p)
  {
  if(!(p.target instanceof EntityCow)){return TargetType.NONE;}
  EntityCow ent = (EntityCow) p.target;
  if(ent.isDead)
    {
    return TargetType.NONE;
    }
  if(p.work==TargetType.BARN_BREED && (ent.getGrowingAge()>0 || !inventory.containsAtLeast(breedingItem, 2)))
    {
    return TargetType.NONE;
    }    
  if(p.work==TargetType.BARN_CULL && (ent.getGrowingAge()<0 || inventory.getEmptySlotCount()<1))
    {
    return TargetType.NONE;
    }
  if(p.work==TargetType.BARN_MILK && (ent.getGrowingAge()<0 || inventory.getEmptySlotCount()<1))
    {
    return TargetType.NONE;
    }
  return p.work;
  }

}
