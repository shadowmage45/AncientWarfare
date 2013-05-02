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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TEWorkSiteAnimalFarm extends TEWorkSite
{

protected Class<? extends EntityAnimal> entityClass = EntityPig.class;
protected int maxAnimalCount = 6;
protected ItemStack breedingItem = new ItemStack(Block.carrot);

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
  List<WorkPoint> potentialPoints = new ArrayList<WorkPoint>();
  List<EntityAnimal> entities = worldObj.getEntitiesWithinAABB(entityClass, getWorkBounds());
  List<EntityAnimal> breedable = new ArrayList<EntityAnimal>();
  List<EntityAnimal> cullable = new ArrayList<EntityAnimal>();
  if(entities!=null && !entities.isEmpty())
    {
    for(EntityAnimal ent : entities)
      {
      if(ent!=null && ent.getClass()==entityClass)
        {
        if(ent.getGrowingAge()==0)
          {
          breedable.add(ent);
          }
        if(ent.getGrowingAge()>=0)
          {
          cullable.add(ent);
          }        
        }
      }
    }  
  EntityAnimal first;
  EntityAnimal second;
  while(breedable.size()>=2 && inventory.containsAtLeast(breedingItem, 2))
    {
    //do two animals at once...
    first = breedable.remove(0);
    second = breedable.remove(0);
    this.addWorkPoint(first, TargetType.BARN_BREED);
    this.addWorkPoint(second, TargetType.BARN_BREED);
    }
  int cullCount = cullable.size() - this.maxAnimalCount;
  for(int i = 0; i < cullCount && cullable.size()>0 ; i++)
    {
    first = cullable.remove(0);
    this.addWorkPoint(first, TargetType.BARN_CULL);
    }
  }

@Override
protected void doWork(NpcBase npc, WorkPoint p)
  {
  if(p.work==TargetType.BARN_BREED && p.target!=null && inventory.containsAtLeast(breedingItem, 2))
    {
    WorkPoint otherP = workPoints.poll();
    if(otherP!=null && otherP.target!=null)
      {
      ((EntityAnimal)p.target).inLove = 600;//.setTarget(otherP.target);
      ((EntityAnimal)otherP.target).inLove = 600;//setTarget(p.target);
      inventory.tryRemoveItems(breedingItem, 2);
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
        stack = npc.inventory.tryMergeItem(stack);
        if(stack!=null)
          {
          stack = inventory.tryMergeItem(stack);
          if(stack!=null)
            {
            InventoryTools.dropItemInWorld(worldObj, stack, xCoord+0.5d, yCoord, zCoord+0.5d);
            }
          }
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
  if(p.work==TargetType.BARN_BREED && ent.getGrowingAge()>0)
    {
    return TargetType.NONE;
    }    
  if(p.work==TargetType.BARN_CULL && ent.getGrowingAge()<0)
    {
    return TargetType.NONE;
    }
  return p.work;
  }

}
