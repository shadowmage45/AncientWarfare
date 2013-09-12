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
package shadowmage.ancient_warfare.common.npcs.waypoints;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.interfaces.IPathableEntity;
import shadowmage.ancient_warfare.common.interfaces.ITEWorkSite;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;


/**
 * maintains data on player set targets, ai targets, and work targets
 * both static and dynamic.
 * features load/save for static points
 * does not validate points, that is up to the AI/user
 * @author Shadowmage
 *
 */
public class WayPointNavigator implements INBTTaggable
{

IPathableEntity owner;
ITargetEntry currentTarget = null;
ITargetEntry playerTarget = null;
WayPoint homePoint = null;
WayPoint work = null;
WayPoint upkeep = null;
ITEWorkSite workSite = null;
WayPoint guard = null;
VehicleBase vehicle = null;
NpcBase commander = null;
List<WayPoint> patrolPoints = new ArrayList<WayPoint>();
List<WayPointItemRouting> courierPoints = new ArrayList<WayPointItemRouting>();
WayPointItemRouting point = null;//current point
int currentPatrolPoint = 0;
int courierPoint = -1;

public WayPointNavigator(IPathableEntity owner)
  {
  this.owner = owner;
  }

public void handleDimensionChange(int dim)
  {
  this.setHomePoint(null);
  this.setUpkeepSite(null);
  this.setWorkSite(null);
  this.clearPatrolPoints();
  this.currentTarget = null;
  this.playerTarget = null;
  }

/************************************************GUARD TARGET*************************************************/
public WayPoint getGuardTarget()
  {
  return guard;
  }

public void setGuardTarget(WayPoint guard)
  {
  this.guard = guard;
  }


/************************************************COURIER TARGET*************************************************/
public int getCourierNum()
  {
  return this.courierPoint;
  }

public int getCourierSize()
  {
  return this.courierPoints.size();
  }

public void setCourierNum(int num)
  {
  this.courierPoint = num;
  }

public void addCourierPoint(WayPointItemRouting point)
  {
  this.courierPoints.add(point);
  }

public WayPointItemRouting getCourierPointAt(int index)
  {
  if(index>=0 && index < this.courierPoints.size())
    {
    return this.courierPoints.get(index);
    }
  return null;
  }

public WayPointItemRouting getCurrentCourierPoint()
  {
  if(this.courierPoint>=0 && this.courierPoint<this.courierPoints.size())
    {
    this.point = this.courierPoints.get(courierPoint);
    return point;
    }
  this.courierPoint = -1;
  return null;
  }

public WayPointItemRouting getNextCourierPoint()
  {
  this.courierPoint++;
  if(this.courierPoint>=this.courierPoints.size())
    {
    this.courierPoint = 0;
    }
  return this.getCurrentCourierPoint();
  }

public void clearCourierPoints()
  {
  this.courierPoints.clear();
  this.courierPoint = -1;
  this.point = null;
  }

public WayPointItemRouting getActiveCourierPoint()
  {
  return this.point;
  }

public void setActiveCourierPoint(WayPointItemRouting point)
  {
  this.point = point;
  }

/************************************************COMMANDER TARGET*************************************************/
public NpcBase getCommander()
  {
  return this.commander;
  }

public void setCommander(NpcBase com)
  {
  this.commander = com;
  }

/************************************************MOUNT TARGET*************************************************/
public VehicleBase getMountTarget()
  {
  return this.vehicle;
  }

public void setMountTarget(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

/************************************************CURRENT TARGET*************************************************/
public ITargetEntry getTarget()
  {
  return this.currentTarget;
  }

public void setTarget(ITargetEntry target)
  {
  this.currentTarget = target;
  }

/************************************************PLAYER TARGET*************************************************/
public ITargetEntry getPlayerTarget()
  {
  return this.playerTarget;
  }

public void setPlayerTarget(ITargetEntry target)
  {
  this.playerTarget = target;
  }

/************************************************PATROL POINTS*************************************************/
public void addPatrolPoint(WayPoint p)
  {
  this.patrolPoints.add(p);
  }

public WayPoint getNextPatrolPoint()
  {
  this.currentPatrolPoint++;
  if(this.currentPatrolPoint>=this.patrolPoints.size())
    {
    this.currentPatrolPoint = 0;
    }
  if(this.patrolPoints.size()>0)
    {
    return this.patrolPoints.get(this.currentPatrolPoint);
    }
  return null;
  }

public int getPatrolSize()
  {
  return this.patrolPoints.size();
  }

public void clearPatrolPoints()
  {
  this.patrolPoints.clear();
  this.currentPatrolPoint = 0;
  }

/************************************************WORK SITE*************************************************/
public WayPoint getWorkSite()
  {
  return work;
  }

public void setWorkSite(WayPoint p)
  {
  this.work = p;  
  this.workSite = null;
  }

public ITEWorkSite getWorkSiteTile()
  {
  return this.workSite;
  }

public void setWorkSiteTile(ITEWorkSite te)
  {
  this.workSite = te;
  }

/************************************************UPKEEP SITE*************************************************/
public WayPoint getUpkeepSite()
  {
  return upkeep;
  }

public void setUpkeepSite(WayPoint p)
  {
  this.upkeep = p;
  }

/************************************************HOME POINT*************************************************/
public void setHomePoint(WayPoint p)
  {
  this.homePoint = p;
  }

public WayPoint getHomePoint()
  {
  return this.homePoint;
  }

/************************************************UTILITY STUFF*************************************************/
@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList list = new NBTTagList();
  for(WayPoint p : this.patrolPoints)
    {
    list.appendTag(p.getNBTTag());
    }
  tag.setTag("patrol", list);  
  list = new NBTTagList(); 
  for(WayPointItemRouting p : this.courierPoints)
    {
    list.appendTag(p.getNBTTag());
    }
  tag.setTag("courier", list);
  if(this.homePoint!=null)
    {
    tag.setCompoundTag("home", this.homePoint.getNBTTag());
    } 
  if(work!=null)
    {
    tag.setCompoundTag("work", this.work.getNBTTag());
    }
  if(upkeep!=null)
    {
    tag.setCompoundTag("upkeep", this.upkeep.getNBTTag());
    } 
  if(guard!=null)
    {
    tag.setCompoundTag("guard", this.guard.getNBTTag());
    }
  tag.setInteger("pNum", this.currentPatrolPoint);
  tag.setInteger("cNum", courierPoint);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.currentPatrolPoint = tag.getInteger("pNum");
  this.courierPoint = tag.getInteger("cNum");
  this.patrolPoints.clear();
  this.courierPoints.clear();
  NBTTagList patrol = tag.getTagList("patrol");
  for(int i = 0; i < patrol.tagCount(); i++)
    {
    this.patrolPoints.add(new WayPoint((NBTTagCompound) patrol.tagAt(i)));
    } 
  patrol = tag.getTagList("courier");
  for(int i = 0; i < patrol.tagCount(); i++)
    {
    this.courierPoints.add(new WayPointItemRouting((NBTTagCompound)patrol.tagAt(i)));
    }
  if(tag.hasKey("home"))
    {
    this.homePoint = new WayPoint(tag.getCompoundTag("home"));
    }  
  if(tag.hasKey("work"))
    {
    this.work = new WayPoint(tag.getCompoundTag("work"));
    } 
  if(tag.hasKey("upkeep"))
    {
    this.upkeep = new WayPoint(tag.getCompoundTag("upkeep"));
    } 
  if(tag.hasKey("guard"))
    {
    this.guard = new WayPoint(tag.getCompoundTag("guard"));
    }
  }

}
