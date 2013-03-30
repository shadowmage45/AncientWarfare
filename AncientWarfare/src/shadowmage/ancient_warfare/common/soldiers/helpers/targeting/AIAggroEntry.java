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
package shadowmage.ancient_warfare.common.soldiers.helpers.targeting;

import java.lang.ref.WeakReference;

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;

public class AIAggroEntry
{
private WeakReference<Entity> ent = new WeakReference<Entity>(null);

public AITargetEntry targetType;
public int targetPriority;
protected float posX;
protected float posY;
protected float posZ;
public int aggroLevel;
public boolean isEntityEntry = false;
NpcBase npc;

public AIAggroEntry(NpcBase npc, AITargetEntry targetType, Entity ent)
  {
  this.ent = new WeakReference<Entity>(ent);
  this.targetType = targetType;
  this.isEntityEntry = true;
  this.targetPriority = targetType.priority;
  this.npc = npc;
  } 

public AIAggroEntry(NpcBase npc, AITargetEntry targetType, int x, int y, int z)
  {
  this.posX = x;
  this.posY = y;
  this.posZ = z;
  this.targetPriority = targetType.priority;
  this.targetType = targetType;
  this.npc = npc;
  }

public float getDistanceFrom()
  {
  if(!this.isEntityEntry)
    {
    return (float) npc.getDistance(posX+0.5d, posY+0.5d, posZ+0.5d);
    }
  if(this.ent.get()!=null)
    {
    return npc.getDistanceToEntity(this.ent.get());
    }
  return 0;
  }

public AIAggroEntry setAggro(int aggro)
  {
  this.aggroLevel = aggro;
  return this;
  }

public Entity getEntity()
  {
  return ent.get();
  }

public float posX()
  {
  if(this.isEntityEntry)
    {
    if(this.getEntity()!=null)
      {
      this.posX = (float) this.getEntity().posX;
      }
    }
  return posX;
  }

public float posY()
  {
  if(this.isEntityEntry)
    {
    if(this.getEntity()!=null)
      {
      this.posY = (float) this.getEntity().posY;
      }
    }
  return posY;
  }

public float posZ()
  {
  if(this.isEntityEntry)
    {
    if(this.getEntity()!=null)
      {
      this.posZ = (float) this.getEntity().posZ;
      }
    }
  return posZ;
  }

public boolean isValidEntry()
  {
  if(!this.isEntityEntry)
    {
    if(npc.worldObj.getChunkProvider().chunkExists(((int)posX/16), ((int)posZ/16)))
      {
      
      //TODO check and see if the block at x,y,z is still a valid target block (enemy priority block)
      }
    else
      {
      return false;
      }
    }
  else
    {
    if(this.ent.get()!=null && !this.ent.get().isDead)
      {
      return this.targetType.isTarget(this.ent.get());
      }
    else
      {
      return false;        
      }
    }
  return true;
  }

public boolean matches(Entity ent)
  {
  return this.ent.get() != null && this.ent.get()==ent;
  }

public boolean matches(int x, int y, int z)
  {
  return this.posX == x && this.posY == y && this.posZ == z;
  }
}
