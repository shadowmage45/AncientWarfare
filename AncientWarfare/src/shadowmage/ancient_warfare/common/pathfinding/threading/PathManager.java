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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathFinder;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccess;


/**
 * thread-pool management for pathfinders.
 * @author Shadowmage
 *
 */
public class PathManager
{

private static PathManager INSTANCE = new PathManager();
public static PathManager instance(){return INSTANCE;}

private PathFinder quickPather = new PathFinder();



/**
 * open/unused thread instances, ready to be pulled out and put to work
 */
private volatile LinkedList<PathThreadWorker> idleThreads = new LinkedList<PathThreadWorker>();
private Lock idleLock = new ReentrantLock();

/**
 * threads currently/actively working
 */
private volatile List<PathThreadWorker> workingThreads = new ArrayList<PathThreadWorker>();
private Lock workersLock = new ReentrantLock();

/**
 * threads which are finished.  the caclulated paths from these will be dispatched to their entities on every server tick
 */
private volatile List<PathResult> finishedThreads = new ArrayList<PathResult>();
private Lock finishedLock = new ReentrantLock();

private LinkedList<PathRequestEntry> qRequests = new LinkedList<PathRequestEntry>();


private static final int MAX_THREADS = 8;
private Executor threadPool = Executors.newFixedThreadPool(MAX_THREADS);

private PathManager()
  {
  for(int i = 0; i < MAX_THREADS; i++)
    {
    PathThreadWorker worker = new PathThreadWorker(this);
    worker.startThread();
    this.idleThreads.add(worker);
    }
  }

public List<Node> findStarterPath(PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int maxRange)
  {
  return quickPather.findPath(world, x, y, z, x1, y1, z1, maxRange);
  }

public synchronized void requestPath(IPathableCallback caller, PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int maxRange)
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
    workersLock.lock();
    for(PathThreadWorker worker : this.workingThreads)
      {
      if(worker.caller==caller)
        {
        Config.logDebug("Calling to interrupt worker!!");
        worker.interruptWorker();
        break;
        }
      }
    workersLock.unlock();
    PathRequestEntry entry = new PathRequestEntry(caller, world, x, y, z, x1, y1, z1, maxRange);
//    Config.logDebug("creating new entry: "+entry.toString());
    this.qRequests.add(entry);      
    }
  this.tryStartThreads();
  }

synchronized public void onThreadFinished(PathThreadWorker worker)
  {
  //threadPool.ex
  this.workingThreads.remove(worker);
  finishedLock.lock();
  this.finishedThreads.add(worker.getPathResult());
//  Config.logDebug("finished results waiting for owners: "+this.finishedThreads.size());
  finishedLock.unlock();
  this.idleLock.lock();
  worker.hasWork(true, false);
  this.idleThreads.add(worker);  
  this.idleLock.unlock();
  Config.logDebug("thread finished: "+worker.toString()+". threads still working: "+this.workingThreads.size()+" still in q: "+this.qRequests.size()+ " idle pool: "+this.idleThreads.size());
  this.tryStartThreads();
  }

private synchronized void tryStartThreads()
  {  
  idleLock.lock();
  while(!this.idleThreads.isEmpty() && !this.qRequests.isEmpty())
    {    
    PathThreadWorker worker = this.idleThreads.pop();
    Config.logDebug("pulling thread from pool :"+worker+ " new idle pool size: "+this.idleThreads.size());
    PathRequestEntry req = this.qRequests.pop();
    worker.setupPathParams(req.caller, req.world, req.x, req.y, req.z, req.x1, req.y1, req.z1, req.maxRange);
    Config.logDebug("new req caller: "+worker.caller);
    this.workingThreads.add(worker);
    }
  idleLock.unlock();  
  }

/**
 * called from server tick, used to synch finished paths into their owner
 */
synchronized public void onTickServer()
  {
  this.tryStartThreads();
  finishedLock.lock();
//  Config.logDebug("ticking server side");
  Iterator<PathResult> it = this.finishedThreads.iterator();
  PathResult worker;
  while(it.hasNext())
    {
    worker = it.next();
    Config.logDebug("attempting send of path to caller: "+worker.caller);
    if(!worker.path.isEmpty() && worker.caller!=null)
      {      
      worker.caller.onPathFound(worker.path);
      }
    it.remove();
    }
  finishedLock.unlock();
  }

private class PathRequestEntry
{
IPathableCallback caller;
int x;
int y;
int z;
int x1;
int y1;
int z1;
int maxRange;
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

@Override
public String toString()
  {
  return "PathRequestEntry: "+x+","+y+","+z+" ::TO:: "+x1+","+y1+","+z1+" ML"+maxRange;
  }
}

}
