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

import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class TEBarnSheep extends TEWorkSiteAnimalFarm
{

ItemStack shearsStack = new ItemStack(Item.shears);

int maxSheep = 20;

/**
 * 
 */
public TEBarnSheep()
  {
  this.entityClass = EntitySheep.class;
  this.breedingItem = new ItemStack(Item.wheat);
  }

@Override
protected void scan()
  { 
  List<WorkPoint> potentialPoints = new ArrayList<WorkPoint>();
  List<EntitySheep> entities = worldObj.getEntitiesWithinAABB(entityClass, getWorkBounds());
  List<EntitySheep> breedable = new ArrayList<EntitySheep>();
  List<EntitySheep> cullable = new ArrayList<EntitySheep>();
  if(entities!=null && !entities.isEmpty())
    {
    for(EntitySheep ent : entities)
      {
      if(ent!=null && ent.getClass()==entityClass)
        {
        if(ent.getGrowingAge()==0)
          {
          breedable.add(ent);
          }
        if(!ent.getSheared())
          {
          cullable.add(ent);
          }        
        }
      }
    }  
  EntitySheep first;
  EntitySheep second;
  int breedableCount = this.maxSheep - entities.size();
  if(entities.size()< this.maxSheep && breedableCount>0)
    {
    while(breedable.size()>=2 && inventory.containsAtLeast(breedingItem, 2) && breedableCount > 0)
      {
      breedableCount--;
      //do two animals at once...
      first = breedable.remove(0);
      second = breedable.remove(0);
      this.addWorkPoint(first, TargetType.BARN_BREED);
      this.addWorkPoint(second, TargetType.BARN_BREED);
      }
    }  
  for(int i = 0; i < cullable.size(); i++)
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
    EntitySheep ent = (EntitySheep)p.target;
    if(ent!=null)
      {
      ArrayList<ItemStack> woolDrops = ent.onSheared(shearsStack, worldObj, MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ), 0);
      if(woolDrops!=null)
        {
        for(ItemStack stack : woolDrops)
          {
          stack = npc.inventory.tryMergeItem(stack);
          if(stack!=null)
            {
            stack = inventory.tryMergeItem(stack);
            if(stack!=null)
              {
              InventoryTools.dropItemInWorld(worldObj, stack, xCoord+0.5d, yCoord, zCoord+0.5d);
              }
            }          
          }
        }      
      }
    }
  }
}
