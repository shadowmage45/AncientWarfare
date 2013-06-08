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

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TEWorkSiteAnimalFarm extends TEWorkSite
{

protected Class<? extends EntityAnimal> entityClass = EntityPig.class;
protected int maxAnimalCount = 6;
protected ItemStack breedingItem = new ItemStack(Item.carrot);
LinkedList<EntityAnimal>breedingList = new LinkedList<EntityAnimal>();
LinkedList<EntityAnimal>cullableList = new LinkedList<EntityAnimal>();

/**
 * 
 */
public TEWorkSiteAnimalFarm()
  {
  this.isWorkSite = true;
  }

@Override
protected void scan()
  { 
  long t1 = System.nanoTime();
  long t2;  
  long t3;
  long s1;
  long s2;
  long s3;
  long s4;
  List<EntityAnimal> entities = worldObj.getEntitiesWithinAABB(entityClass, getWorkBounds());
  t2 = System.nanoTime();
  s1 = t2-t1;
  breedingList.clear();
  cullableList.clear();
  t3 = System.nanoTime();
  s2 = t3-t2; 
  t2=t3;
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
  t3 = System.nanoTime();
  s3 = t3-t2;
  t2=t3;
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
  if(this.inventory.getEmptySlotCount()>=1)
    {
    int cullCount = cullableList.size() - this.maxAnimalCount;
    for(int i = 0; i < cullCount && cullableList.size()>0 ; i++)
      {
      first = cullableList.poll();//.remove(0);
      this.addWorkPoint(first, TargetType.BARN_CULL);
      }  
    }
  t3 = System.nanoTime();
  s4 = t3-t2;
//  Config.logDebug("world entity seek time: "+s1);
//  Config.logDebug("list clearing time: "+s2);
//  Config.logDebug("first pass time: "+s3);
//  Config.logDebug("second pass time: "+s4);
//  Config.logDebug("total entity seek time: "+(s1+s2+s3+s4));
  }

@Override
protected void doWork(IWorker npc, WorkPoint p)
  {
  if(p.work==TargetType.BARN_BREED && p.target!=null && inventory.containsAtLeast(breedingItem, 2))
    {
    WorkPoint otherP = null;
    for(int i = 0; i < this.workPoints.size(); i++)
      {
      /**
       * really, the NEXT point should be a breeding point, as they are put in in pairs
       */
      otherP = workPoints.get(i);
      if(otherP!=null && otherP.target!=null && otherP.work==TargetType.BARN_BREED)
        {
        ((EntityAnimal)p.target).inLove = 600;//.setTarget(otherP.target);
        ((EntityAnimal)otherP.target).inLove = 600;//setTarget(p.target);
        inventory.tryRemoveItems(breedingItem, 2);
        workPoints.remove(otherP);
        break;
        }
      }   
    }
  else if(p.work==TargetType.BARN_CULL && p.target!=null)
    {
    EntityAnimal ent = (EntityAnimal)p.target;
    if(ent!=null)
      {
      ent.captureDrops = true;
      ent.arrowHitTimer =10;
      ent.attackEntityFrom(DamageSource.generic, ent.getHealth()+1);
      ItemStack stack;
      for(EntityItem item : ent.capturedDrops)
        {
        stack = item.getEntityItem();
        stack = inventory.tryMergeItem(stack);
        stack = overflow.tryMergeItem(stack);
        InventoryTools.dropItemInWorld(worldObj, stack, xCoord+0.5d, yCoord, zCoord+0.5d);       
        item.setDead();
        }
      }
    }
  }

@Override
protected TargetType validateWorkPoint(WorkPoint p)
  {
  EntityAnimal ent = (EntityAnimal) p.target;
  if(ent==null || ent.isDead)
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
  return p.work;
  }

}
