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
package shadowmage.ancient_warfare.common.civics.worksite;

import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public abstract class TEWorkSite extends TECivic
{

protected LinkedList<WorkPoint> workPoints = new LinkedList<WorkPoint>();

public TEWorkSite()
  {
  this.isWorkSite = true;    
  }

protected abstract void scan();

protected abstract void doWork(NpcBase npc, WorkPoint p);

protected abstract TargetType validateWorkPoint(WorkPoint p);

@Override
protected void onCivicUpdate()
  {
  long t1;
  long t2;  
  long s1;
  long s2;
  t1 = System.nanoTime();
  validateWorkPoints();  
  t2 = System.nanoTime();
  s1 = t2-t1;
  t1=t2;
  if(!hasWork())
    {
    scan();
    }
  t2 = System.nanoTime();
  s2=t2-t1;
//  Config.logDebug("work site point validation time: "+s1);
//  Config.logDebug("work site scan time: "+s2);
//  Config.logDebug("work site total update time: "+(s2+s1)+" for type "+this.getCivic().getDisplayName());
  super.onCivicUpdate();
  }

@Override
protected void updateHasWork()
  {  
  this.setHasWork(!this.workPoints.isEmpty());
  }

@Override
public void doWork(NpcBase npc)
  {
  Iterator<WorkPoint> it = this.workPoints.iterator();
  WorkPoint p;
  while(it.hasNext())
    {
    p = it.next();
    if(validateWorkPoint(p)!=p.work)
      {
      it.remove();
      }
    else
      {
      this.doWork(npc, p);
      break;
      }
    }  
  this.updateHasWork();
  }

protected void validateWorkPoints()
  {
  Iterator<WorkPoint> it = this.workPoints.iterator();
  WorkPoint p;
  while(it.hasNext())
    {
    p = it.next();
    if(validateWorkPoint(p)!=p.work)
      {
      it.remove();
      }
    }
  setHasWork(!workPoints.isEmpty());
  }

protected void addWorkPoint(int x, int y, int z, TargetType work)
  {
  this.workPoints.add(new WorkPoint(x, y, z, work));
  }

protected void addWorkPoint(Entity ent, TargetType work)
  {
  this.workPoints.add(new WorkPoint(ent, work));
  }

}
