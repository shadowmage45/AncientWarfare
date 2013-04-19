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
package shadowmage.ancient_warfare.common.pathfinding.waypoints;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.interfaces.IPathableEntity;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathManager;
import shadowmage.ancient_warfare.common.pathfinding.threading.IPathableCallback;
import shadowmage.ancient_warfare.common.utils.TargetType;
import shadowmage.ancient_warfare.common.utils.Trig;

public class WayPointNavigator implements IPathableCallback, INBTTaggable
{

IPathableEntity owner;
WayPoint homePoint = null;
WayPoint workPoint = null;
WayPoint depositPoint = null;
List<WayPointPath> wayPaths = new ArrayList<WayPointPath>();
List<WayPoint> wayPoints = new ArrayList<WayPoint>();
List<WayPoint> patrolPoints = new ArrayList<WayPoint>();
int currentPatrolPoint = 0;

WayPoint searchPointA;
WayPoint searchPointB;

public WayPointNavigator(IPathableEntity owner)
  {
  this.owner = owner;
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
  Config.logDebug("clearing patrol points");
  this.patrolPoints.clear();
  this.currentPatrolPoint = 0;
  }

public void addWayPoint(WayPoint p)
  {
  this.wayPoints.add(p);
  }

public WayPoint getClosestWayPointOfType(TargetType type)
  {
  WayPoint bestFound = null;
  float bestDist = Float.POSITIVE_INFINITY;
  float dist = Float.POSITIVE_INFINITY;
  Entity ent = owner.getEntity();
  for(WayPoint p : this.wayPoints)
    {
    if(p!=null && p.type==type &&p.isValidWayPoint(ent.worldObj))
      {
      dist =Trig.getDistance(ent.posX, ent.posY, ent.posZ, p.x, p.y, p.z); 
      if(dist<bestDist)
        {
        bestFound = p;
        bestDist = dist;        
        }
      }
    }
  return bestFound;
  }

public void clearWayPoints()
  {
  this.wayPoints.clear();
  this.wayPaths.clear();
  this.searchPointA = null;
  this.searchPointB = null;
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

public void setWorkPoint(int x, int y, int z)
  {
  TileEntity te = this.owner.getEntity().worldObj.getBlockTileEntity(x, y, z);
  if(te instanceof TECivic)
    {
    TECivic tec = (TECivic)te;
    if(tec.getCivic()!=null && tec.getCivic().isWorkSite())
      {
      this.workPoint = new WayPoint(x,y,z, TargetType.WORK);
      }
    else
      {
      this.workPoint = null;
      }
    }
  else
    {
    this.workPoint = null;
    }  
  }

public void clearWorkPoint()
  {
  this.workPoint = null;
  }

public boolean hasWorkPoint()
  {
  return this.workPoint!=null;
  }

public WayPoint getWorkPoint()
  {
  return this.workPoint;
  }

public void setDepositPoint(int x, int y, int z)
  {
  TileEntity te = this.owner.getEntity().worldObj.getBlockTileEntity(x, y, z);
  if(te instanceof TECivic)
    {
    TECivic tec = (TECivic)te;
    if(tec.getCivic()!=null && tec.getCivic().isDepository())
      {
      this.depositPoint = new WayPoint(x,y,z, TargetType.DEPOSIT);
      }
    else
      {
      this.depositPoint = null;
      }
    }
  else
    {
    this.depositPoint = null;
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

public WayPoint getDepositPoint()
  {
  return this.depositPoint;
  }

private void findPathFor(WayPoint a, WayPoint b)
  {
  this.searchPointA = a;
  this.searchPointB = b;
  PathManager.instance().requestPath(this, owner.getWorldAccess(), a.x, a.y, a.z, b.x, b.y, b.z, 60);
  }

private WayPointPath getPathFor(WayPoint a, WayPoint b)
  {
  for(WayPointPath path : wayPaths)    
    {
    if(path.isPathFor(a, b))
      {
      return path;
      }
    }
  return null;
  }

private void setPathFor(WayPoint a, WayPoint b, List<Node> path)
  {
  this.wayPaths.add(new WayPointPath(a, b, path));
  }

@Override
public void onPathFound(List<Node> pathNodes)
  {  
  if(this.searchPointA!=null && this.searchPointB!=null)
    {
    this.setPathFor(searchPointA, searchPointB, pathNodes);
    this.searchPointA = null;
    this.searchPointB = null;
    }
  }

@Override
public void onPathFailed(List<Node> partialPathNodes)
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
  if(this.workPoint!=null)
    {
    tag.setCompoundTag("work", this.workPoint.getNBTTag());    
    }
  if(this.depositPoint!=null)
    {
    tag.setCompoundTag("deposit", this.depositPoint.getNBTTag());
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
    this.workPoint = new WayPoint(tag.getCompoundTag("work"));
    }
  if(tag.hasKey("deposit"))
    {
    this.depositPoint = new WayPoint(tag.getCompoundTag("deposit"));
    }
  }

private class WayPointPath
{
WayPoint pointA;
WayPoint pointB;
List<Node> pathNodes;
private WayPointPath(WayPoint a, WayPoint b, List<Node> path)
  {
  this.pointA = a;
  this.pointB = b;
  this.pathNodes = path;
  }

private boolean isPathFor(WayPoint a, WayPoint b)
  {
  return (pointA.areWayPointsEqual(a) && pointB.areWayPointsEqual(b)) || (pointA.areWayPointsEqual(b) && pointB.areWayPointsEqual(a));
  }

private List<Node> getPath()
  {
  List<Node> pathNodes = new ArrayList<Node>();
  if(this.pathNodes!=null)
    {
    for(Node n : this.pathNodes)
      {
      pathNodes.add(new Node(n.x, n.y, n.z));
      }
    }
  return pathNodes;
  }

}


}
