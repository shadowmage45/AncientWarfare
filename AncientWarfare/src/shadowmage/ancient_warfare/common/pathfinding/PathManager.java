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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;


/**
 * thread-pool management for pathfinders.
 * @author Shadowmage
 *
 */
public class PathManager
{

private static PathManager INSTANCE = new PathManager();
public static PathManager instance(){return INSTANCE;}

/**
 * open/unused thread instances, ready to be pulled out and put to work
 */
private volatile LinkedList<PathThreadWorker> idleThreads = new LinkedList<PathThreadWorker>();
/**
 * threads currently/actively working
 */
private volatile List<PathThreadWorker> workingThreads = new ArrayList<PathThreadWorker>();
/**
 * threads which are cancelled
 */
private volatile List<PathThreadWorker> cancelledThreads = new ArrayList<PathThreadWorker>();
/**
 * threads which are finished.  the caclulated paths from these will be dispatched to their entities on every server tick
 */
private volatile List<PathThreadWorker> finishedThreads = new ArrayList<PathThreadWorker>();

private LinkedList<PathRequestEntry> qRequests = new LinkedList<PathRequestEntry>();

private static final int MAX_THREADS = 7;

synchronized public void requestPath(IPathableCallback caller, PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int maxRange)
  {
  boolean found = false;
  for(PathRequestEntry entry : this.qRequests)
    {
    if(entry.caller == caller)
      {
      entry.world = world;
      entry.x = x;
      entry.y = y;
      entry.z = z;
      entry.x1 = x1;
      entry.y1 = y1;
      entry.z1 = z1;
      entry.maxRange = maxRange;
      found = true;
      }
    }
  if(!found)
    {
    for(PathThreadWorker worker : this.workingThreads)
      {
      if(worker.caller==caller)
        {
        worker.interruptWorker();
        break;
        }
      }
    for(PathThreadWorker worker : this.finishedThreads)//don't restart a path that is sitting in the finished queue...
      {
      if(worker.caller==caller)
        {
        found = true;
        break;
        }
      }
    if(!found)
      {
      this.qRequests.add(new PathRequestEntry(caller, world, x, y, z, x1, y1, z1, maxRange));
      }
    }
  this.tryStartThreads();
  }

synchronized public void onThreadFinished(PathThreadWorker worker)
  {
  if(this.workingThreads.contains(worker))
    {
    this.workingThreads.remove(worker);
    }
  this.finishedThreads.add(worker);
  //DEBUG
  worker.caller.onPathFound(worker.path);
  worker.clearRefs();  
  this.tryStartThreads();
  Config.logDebug("thread finished. threads still working: "+this.workingThreads.size());
  }

private synchronized void tryStartThreads()
  {
  if(this.workingThreads.size()< MAX_THREADS)
    {
    while(this.workingThreads.size()<=MAX_THREADS && !this.qRequests.isEmpty())
      {
      PathThreadWorker worker;
      if(!this.idleThreads.isEmpty())
        {
        worker = this.idleThreads.pop();
        }
      else
        {
        worker = new PathThreadWorker(this);
        }  
      PathRequestEntry req = this.qRequests.pop();
      worker.findPath(req.caller, req.world, req.x, req.y, req.z, req.x1, req.y1, req.z1, req.maxRange);
      this.workingThreads.add(worker);
      }
    }
  }

/**
 * called from server tick, used to synch finished paths into their owner from an in-cycle method
 */
synchronized public void onTickServer()
  { 
  Iterator<PathThreadWorker> it = this.finishedThreads.iterator();
  PathThreadWorker worker;
  while(it.hasNext())
    {
    worker = it.next();
    if(!worker.path.isEmpty())
      {      
      worker.caller.onPathFound(worker.path);
      }
    worker.clearRefs();
    it.remove();
    this.idleThreads.add(worker);
    }
  }

private class PathRequestEntry
{
IPathableCallback caller;
int x, y, z, x1, y1, z1, maxRange;
PathWorldAccess world;
List<Node> foundPath;
public PathRequestEntry(IPathableCallback caller, PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int maxRange)
  {
  this.caller = caller;
  this.world = world;
  this.x = x;
  this.y = y;
  this.z = z;
  this.x1 = x1;
  this.y1 = y1;
  this.z1 = z1;
  this.maxRange = maxRange;
  }
}

}
