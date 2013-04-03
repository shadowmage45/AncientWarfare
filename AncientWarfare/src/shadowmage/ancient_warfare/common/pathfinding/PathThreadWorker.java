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
package shadowmage.ancient_warfare.common.pathfinding;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;

public class PathThreadWorker implements Runnable
{

private PathFinderJPS pather = new PathFinderJPS();
private Thread th;
List<Node> path = new ArrayList<Node>();
IPathableCallback caller;
int x, y, z, x1, y1, z1, maxRange;
PathWorldAccess world;
PathManager parent;

public boolean working = false;
public boolean interruped = false;


public PathThreadWorker(PathManager parent)
  {
  this.parent = parent;
  this.th = new Thread(this, "AW.PathThread");  
  this.pather.threaded = true;
  }

public void findPath(IPathableCallback caller, PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int maxRange)
  {
  this.path.clear();
  this.caller = caller;
  this.world = world;
  this.x = x;
  this.y = y;
  this.z = z;
  this.x1 = x1;
  this.y1 = y1;
  this.z1 = z1;
  this.working = true;
  this.interruped = false;
//  Config.logDebug("starting thread");
  this.th.start();
//  Config.logDebug("thread started");
  }

public void interruptWorker()
  {
  this.pather.setInterrupted();
  }

@Override
public void run()
  { 
  path = this.pather.findPath(world, x, y, z, x1, y1, z1, maxRange);
//  Config.logDebug("path find finished");
  if(!interruped)
    {
    this.parent.onThreadFinished(this);
    }  
  }

public void clearRefs()
  {
  this.interruped = false;
  this.working = false;
  this.working = false;
  this.world = null;
  this.caller = null;
  }

}
