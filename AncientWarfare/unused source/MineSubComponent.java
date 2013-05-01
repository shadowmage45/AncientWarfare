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
package shadowmage.ancient_warfare.common.civics.worksite.te.mine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class MineSubComponent implements INBTTaggable
{

private PriorityQueue<MinePoint> pointQueue = new PriorityQueue<MinePoint>();
private ArrayList<MinePoint> finishedPoints = new ArrayList<MinePoint>();
private NpcBase worker;
protected MineComponent parent;

public MineSubComponent(MineComponent parent)
  {
  this.parent = parent;
  }

public void validatePoints(World world)
  {
//  Config.logDebug("validating points for: "+this);
//  Iterator<MinePoint> it = this.finishedPoints.iterator();
//  MinePoint p = null;
//  while(it.hasNext())
//    {    
//    p = it.next();
////    Config.logDebug("validating point: "+p);
//    if(p.hasWork(world))
//      {
//      Config.logDebug("MINE VALIDATION: ADDING POINT BACK TO POINT QUEUE:");
//      it.remove();
//      this.pointQueue.offer(p);
//      }
//    }
  }

public void addNewPoint(int x, int y, int z, int order, byte special, TargetType type, boolean singleAction)
  {
  this.pointQueue.offer(new MinePoint(x,y,z,order, special, type).setOwner(this).setSingleAction(singleAction));
  }

public void addNewPoint(int x, int y, int z, int order, TargetType type, boolean singleAction)
  {
  this.pointQueue.offer(new MinePoint(x,y,z,order, type).setOwner(this).setSingleAction(singleAction));
  }

public void addNewPoint(MinePoint p)
  {
  this.pointQueue.offer(p);
  } 

public void addNewPointFinished(MinePoint p)
  {
  this.finishedPoints.add(p);
  }

public void onPointFinished(NpcBase npc, MinePoint p)
  {   
  if(p.singleAction)
    {
//    Config.logDebug("adding single action point to finished list: "+p);
    this.finishedPoints.add(p);
    }
  else
    {
//    Config.logDebug("skipping adding point to finished list: ");
    }
  }

public void onPointFailed(NpcBase npc, MinePoint p)
  {
  this.pointQueue.offer(p);
  }

public MinePoint getNextPoint()
  {
  return this.pointQueue.poll();
  }

public void setWorker(NpcBase npc)
  {
  this.worker = npc;
  }

public NpcBase getWorker()
  {
  return this.worker;
  }

public boolean hasWork()
  {
  return !this.pointQueue.isEmpty();
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList list = new NBTTagList();
  for(MinePoint p : this.pointQueue)
    {
    list.appendTag(p.getNBTTag());
    }
  tag.setTag("queue", list);
  list = new NBTTagList();
  for(MinePoint p : this.finishedPoints)
    {
    list.appendTag(p.getNBTTag());
    }
  tag.setTag("finished", list);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  NBTTagList list = tag.getTagList("queue");
  for(int i = 0; i < list.tagCount(); i++)
    {
    this.pointQueue.offer(new MinePoint((NBTTagCompound) list.tagAt(i)).setOwner(this));
    }
  list = tag.getTagList("finished");
  for(int i = 0; i < list.tagCount(); i++)
    {
    this.finishedPoints.add(new MinePoint((NBTTagCompound) list.tagAt(i)).setOwner(this));
    }
  }


}
