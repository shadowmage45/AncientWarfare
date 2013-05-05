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
package shadowmage.ancient_warfare.common.npcs.waypoints;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.interfaces.IPathableEntity;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;


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
WayPoint deposit = null;
WayPoint upkeep = null;
TECivic workSite = null;
List<WayPoint> patrolPoints = new ArrayList<WayPoint>();
int currentPatrolPoint = 0;


//TODO add special item routing waypoints for couriers

public WayPointNavigator(IPathableEntity owner)
  {
  this.owner = owner;
  }

public void handleDimensionChange(int dim)
  {
  this.setDepositSite(null);
  this.setHomePoint(null);
  this.setUpkeepSite(null);
  this.setWorkSite(null);
  this.clearPatrolPoints();
  this.currentTarget = null;
  this.playerTarget = null;
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
  }

public TECivic getWorkSiteTile()
  {
  return this.workSite;
  }

public void setWorkSiteTile(TECivic te)
  {
  this.workSite = te;
  }

/************************************************DEPOSIT SITE*************************************************/
public void setDepositSite(WayPoint p)
  {
  this.deposit = p;
  }

public WayPoint getDepositSite()
  {
  return deposit;
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
  if(this.homePoint!=null)
    {
    tag.setCompoundTag("home", this.homePoint.getNBTTag());
    } 
  if(work!=null)
    {
    tag.setCompoundTag("work", this.work.getNBTTag());
    }
  if(deposit!=null)
    {
    tag.setCompoundTag("deposit", this.deposit.getNBTTag());
    }
  if(upkeep!=null)
    {
    tag.setCompoundTag("upkeep", this.upkeep.getNBTTag());
    }
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.patrolPoints.clear();
  NBTTagList patrol = tag.getTagList("patrol");
  for(int i = 0; i < patrol.tagCount(); i++)
    {
    this.patrolPoints.add(new WayPoint((NBTTagCompound) patrol.tagAt(i)));
    } 
  if(tag.hasKey("home"))
    {
    this.homePoint = new WayPoint(tag.getCompoundTag("home"));
    }  
  if(tag.hasKey("work"))
    {
    this.work = new WayPoint(tag.getCompoundTag("work"));
    }
  if(tag.hasKey("deposit"))
    {    
    this.deposit = new WayPoint(tag.getCompoundTag("deposit"));
    }
  if(tag.hasKey("upkeep"))
    {
    this.upkeep = new WayPoint(tag.getCompoundTag("upkeep"));
    }
  }

}
