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
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.interfaces.IPathableEntity;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathManager;
import shadowmage.ancient_warfare.common.pathfinding.threading.IPathableCallback;
import shadowmage.ancient_warfare.common.utils.Trig;

public class WayPointNavigator implements IPathableCallback, INBTTaggable
{

IPathableEntity owner;
WayPoint homePoint = null;
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
  this.patrolPoints.add(new WayPoint(x,y,z,0));
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
  }

public void addWayPoint(WayPoint p)
  {
  this.wayPoints.add(p);
  }

public WayPoint getClosestWayPointOfType(int type)
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
  this.homePoint = new WayPoint(x,y,z,0);
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
  // TODO Auto-generated method stub
  return null;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub  
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
