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

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.interfaces.IPathableEntity;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.threading.IPathableCallback;
import shadowmage.ancient_warfare.common.targeting.TargetType;

/**
 * far more closely tied to Npcs than I would have liked...., more of a waypoint helper. needs moved into npcs/helper folder and renamed
 * need to create a separate class to manage player designated waypoints on vehicles/etc. (self driving vehicles could be fun)
 * @author Shadowmage
 *
 */
public class WayPointNavigator implements IPathableCallback, INBTTaggable
{

IPathableEntity owner;
ITargetEntry currentTarget = null;
ITargetEntry playerTarget = null;
WayPoint homePoint = null;
WayPoint workSitePoint = null;
WorkPoint depositPoint = null;
WorkPoint workPoint = null;
private TECivic workSite = null;
TileEntity depositSite = null;
List<WayPoint> wayPoints = new ArrayList<WayPoint>();
List<WayPoint> patrolPoints = new ArrayList<WayPoint>();
int currentPatrolPoint = 0;


public WayPointNavigator(IPathableEntity owner)
  {
  this.owner = owner;
  }

public ITargetEntry getTarget()
  {
  return this.currentTarget;
  }

public void setTarget(ITargetEntry target)
  {
  this.currentTarget = target;
  }

public ITargetEntry getPlayerTarget()
  {
  return this.playerTarget;
  }

public void setPlayerTarget(ITargetEntry target)
  {
  this.playerTarget = target;
  }

public void addPatrolPoint(int x, int y, int z)
  {
  this.patrolPoints.add(new WayPoint(x,y,z, TargetType.PATROL));
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

public void addWayPoint(WayPoint p)
  {
  this.wayPoints.add(p);
  }

public void validateSites()
  {
  this.validateWorkSite();
  this.validateDepositSite();
  }

protected void validateWorkSite()
  {
  if(this.workSitePoint!=null)
    {
    TECivic workSite = this.getWorkSite();
    if(workSite==null || workSite.xCoord != workSitePoint.floorX() || workSite.yCoord != workSitePoint.floorY() || workSite.zCoord!=workSitePoint.floorZ())
      {
      Config.logDebug("work site is null or not equal to work point "+this.getWorkSite());      
      //different sites, try and re-acquire      
      if(isPointLoaded(workSitePoint.floorX(), workSitePoint.floorY(), workSitePoint.floorZ()))
        {
        //if can acqurire, set, else clear
        TileEntity te = owner.getEntity().worldObj.getBlockTileEntity(workSitePoint.floorX(), workSitePoint.floorY(), workSitePoint.floorZ());
        if(te instanceof TECivic)
          {
          this.setWorkSite((TECivic) te);
          }
        else
          {this.setWorkSite(null);
          this.workSitePoint = null;
          }
        }
      //else chunk not loaded, check again later....
      }
    //else was valid site
    }
  else
    {
    this.setWorkSite(null);
    }
  }

protected void validateDepositSite()
  {
  if(this.depositPoint!=null)
    {
    if(this.depositSite==null || depositSite.xCoord != depositPoint.floorX() || depositSite.yCoord != depositPoint.floorY() || depositSite.zCoord!=depositPoint.floorZ())
      {
      //different sites, try and re-acquire      
      if(isPointLoaded(depositPoint.floorX(), depositPoint.floorY(), depositPoint.floorZ()))
        {
        //if can acqurire, set, else clear
        TileEntity te = owner.getEntity().worldObj.getBlockTileEntity(depositPoint.floorX(), depositPoint.floorY(), depositPoint.floorZ());
        if(te instanceof IInventory)
          {
          this.depositSite = te;
          }
        else
          {
          this.depositSite = null;
          this.depositPoint = null;
          }
        }
      //else chunk not loaded, check again later....
      }
    //else was valid site
    }
  else//was had no point, clear site just in case
    {
    this.clearDepositPoint();
    }
  }

protected boolean isPointLoaded(int x, int y, int z)
  {
  return true;
  }

public void clearWayPoints()
  {
  this.wayPoints.clear();
  }

public WorkPoint getWorkPoint()
  {
  return this.workPoint;
  }

public void setWorkPoint(WorkPoint point)
  {
  Config.logDebug("settign work point");
  this.workPoint = point;
  }

public void setHomePoint(int x, int y, int z)
  {
  this.homePoint = new WayPoint(x,y,z, TargetType.SHELTER);
  }

public boolean hasHomePoint()
  {
  return this.homePoint!=null;
  }

public WayPoint getHomePoint()
  {
  return this.homePoint;
  }

public void clearHomePoint()
  {
  this.homePoint = null;
  }

public void setWorkSitePoint(int x, int y, int z, int side)
  {
  Config.logDebug(String.format("setting  work site point to: %s, %s, %s : %s", x,y,z,side));
  TileEntity te = this.owner.getEntity().worldObj.getBlockTileEntity(x, y, z);
  if(te instanceof TECivic)
    {
    TECivic tec = (TECivic)te;
    if(tec.getCivic()!=null && tec.getCivic().isWorkSite())
      {
      Config.logDebug("setting work-site : " + tec);
      this.workSitePoint = new WayPoint(x,y,z, TargetType.WORK);
      this.setWorkSite(tec);    
      }
    else
      {
      Config.logDebug("civic present but not a work site");
      this.setWorkSite(null);
      this.workSitePoint = null;
      }
    }
  else
    {
    Config.logDebug("no civic present, cannot designate work site");
    this.setWorkSite(null);
    this.workSitePoint = null;
    }  
  }

public void clearWorkInfo()
  {  
  Config.logDebug("clearing work info");
  this.setWorkSite(null);
  this.workPoint = null;
  this.workSitePoint = null;
  }

public boolean hasWorkSitePoint()
  {
  return this.workSitePoint!=null;
  }

public WayPoint getWorkSitePoint()
  {
  return this.workSitePoint;
  }

public void setWorkSite(TECivic work)
  {
  Config.logDebug("setting work site : "+work);
  this.workSite = work;
  }

public TECivic getWorkSite()
  {
  return this.workSite;
  }

public boolean hasWorkSite()
  {
  return this.getWorkSite()!=null;
  }


public void setDepositPoint(int x, int y, int z, int side)
  {
  Config.logDebug(String.format("setting deposit point to: %s, %s, %s : %s", x,y,z,side));
  TileEntity te = this.owner.getEntity().worldObj.getBlockTileEntity(x, y, z);
  if(te instanceof TECivic)
    {
    TECivic tec = (TECivic)te;
    if(tec.getCivic()!=null && tec.getCivic().isDepository())
      {
      this.depositPoint = new WorkPoint(x,y,z, TargetType.DELIVER);
      this.depositSite = tec;
      }
    else
      {
      this.depositPoint = null;
      this.depositSite = null;
      }
    }
  else if(te instanceof IInventory)
    {
    this.depositPoint = new WorkPoint(x,y,z, side, TargetType.DELIVER);
    this.depositSite = te;
    }
  else
    {
    this.depositPoint = null;
    this.depositSite = null;
    }  
  }

public void clearDepositPoint()
  {
  this.depositPoint = null;
  }

public boolean hasDepositPoint()
  {
  return this.depositPoint!=null;
  }

public WorkPoint getDepositPoint()
  {
  return this.depositPoint;
  }


@Override
public void onPathFound(List<Node> pathNodes)
  {  
 
  }

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
  for(WayPoint p : this.wayPoints)
    {
    list.appendTag(p.getNBTTag());
    }
  tag.setTag("points", list);
  if(this.homePoint!=null)
    {
    tag.setCompoundTag("home", this.homePoint.getNBTTag());
    }  
  if(this.workSitePoint!=null)
    {
    tag.setCompoundTag("work", this.workSitePoint.getNBTTag());    
    }
  if(this.depositPoint!=null)
    {
    tag.setCompoundTag("deposit", this.depositPoint.getNBTTag());
    }
  if(this.workPoint!=null)
    {
    tag.setCompoundTag("work2", this.workPoint.getNBTTag());    
    }
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.patrolPoints.clear();
  this.wayPoints.clear();  
  NBTTagList patrol = tag.getTagList("patrol");
  for(int i = 0; i < patrol.tagCount(); i++)
    {
    this.patrolPoints.add(new WayPoint((NBTTagCompound) patrol.tagAt(i)));
    }
  NBTTagList points = tag.getTagList("points");
  for(int i = 0; i < points.tagCount(); i++)
    {
    this.wayPoints.add(new WayPoint((NBTTagCompound) points.tagAt(i)));
    }
  if(tag.hasKey("home"))
    {
    this.homePoint = new WayPoint(tag.getCompoundTag("home"));
    }
  if(tag.hasKey("work"))
    {
    this.workSitePoint = new WayPoint(tag.getCompoundTag("work"));
    }
  if(tag.hasKey("deposit"))
    {
    this.depositPoint = new WorkPoint(tag.getCompoundTag("deposit"));
    }
  if(tag.hasKey("work2"))
    {
    this.workPoint = new WorkPoint(tag.getCompoundTag("work2"));
    }
  }



}
