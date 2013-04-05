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
package shadowmage.ancient_warfare.common.pathfinding.threading;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathFinderThetaStar;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccess;

public class PathThreadWorker implements Runnable
{

private PathFinderThetaStar pather = new PathFinderThetaStar();
List<Node> path = new ArrayList<Node>();
IPathableCallback caller;
int x, y, z, x1, y1, z1, maxRange;
PathWorldAccess world;

private boolean interruped = false;
private int num;
private static int threadNum = 0;

public PathThreadWorker()
  {
  this.num = threadNum;  
  //this.pather.threaded = true;
  threadNum++;
  }

public void setupPathParams(IPathableCallback caller, PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int maxRange)
  {
  this.path.clear();
  this.caller = caller;
  this.world = world;
  this.maxRange = maxRange;
  this.x = x;
  this.y = y;
  this.z = z;
  this.x1 = x1;
  this.y1 = y1;
  this.z1 = z1;  
//  Config.logDebug("setting worker hasWork:"+this.toString());
  this.interruped = false;
  }

public void interruptWorker()
  {
  this.interruped = true;
  //this.pather.setInterrupted();
  }

@Override
public void run()
  { 
  path = this.pather.findPath(world, x, y, z, x1, y1, z1, maxRange);
//  Config.logDebug("thread finishing");
  }

public PathResult getPathResult()
  {
  PathResult p = new PathResult();
  p.caller = this.caller;
  p.path.addAll(this.path);
  this.path.clear();
  return p;
  }

@Override
public String toString()
  {
  return "AWPathThread:"+num;
  }

public void clearRefs()
  {
  this.interruped = false;
  this.world = null;
  this.caller = null;
  }

}
