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
package shadowmage.ancient_warfare.common.pathfinding.threading;

import java.util.LinkedList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathFinderThetaStar;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccess;

public class PathThreadPool
{

static int threadNum = 0;
private static int MAX_THREADS = Config.npcPathfinderThreads;
private static PathThreadPool INSTANCE = new PathThreadPool();
public static PathThreadPool instance(){return INSTANCE;}
private LinkedList<PathThreadWorker> workQueue = new LinkedList<PathThreadWorker>();
private PoolWorker[] workerThreads = new PoolWorker[MAX_THREADS];
private LinkedList<PathResult> results = new LinkedList<PathResult>();
private LinkedList<PathThreadWorker> idleWorkers = new LinkedList<PathThreadWorker>();
private PathFinderThetaStar quickPather = new PathFinderThetaStar();

private PathThreadPool()
  {
  for(int i = 0; i < MAX_THREADS; i++)
    {
    this.workerThreads[i] = new PoolWorker();
    this.workerThreads[i].start();
    }
  }

//public List<Node> findStarterPath(PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int maxRange)
//  {
//  return quickPather.findPath(world, x, y, z, x1, y1, z1, maxRange);
//  }

public void requestPath(IPathableCallback caller, PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int maxRange)
  {
  synchronized(idleWorkers)
  {
  PathThreadWorker worker;
  if(this.idleWorkers.isEmpty())
    {
    worker = new PathThreadWorker();
    }
  else
    {
    worker = this.idleWorkers.removeLast();
    }
  worker.setupPathParams(caller, world, x, y, z, x1, y1, z1, maxRange);
  addTaskToQueue(worker);
//  Config.logDebug("work queue size: "+this.workQueue.size());
  while(this.workQueue.size()>300)
    {
    this.workQueue.pop();
    }
  }
  }

public void tryDispatchResults()
  {
  synchronized(results)
  {
  for(PathResult r : this.results)
    {
    r.caller.onPathFound(r.path);
    }
  this.results.clear();
  }
  }

private void onTaskCompleted(PathThreadWorker worker)
  {
  synchronized(results)
    {
    this.results.addLast(worker.getPathResult());
//    Config.logDebug("results size:"+this.results.size());
    }
  synchronized(idleWorkers)
    {
    this.idleWorkers.add(worker);
    }
//  tryDispatchResults();
  }

private void addTaskToQueue(PathThreadWorker worker) 
  {
  synchronized(workQueue) 
    {
    workQueue.addLast(worker);
    workQueue.notify();
    }
  }

private class PoolWorker extends Thread
{

private int num;
private PoolWorker()
  {
  this.num = threadNum;  
  }

PathThreadWorker task;

@Override
public String toString()
  {
  return "AW.Path.Pool.Thread:"+num;
  }

@Override
public void run()
  {
  while(true)
    {
    synchronized(workQueue)
      {
      while(workQueue.isEmpty())
        {
        try
          {
          workQueue.wait();
          } 
        catch(Exception e){}
        }      
      task = workQueue.removeFirst();
      }
    try
      {
      task.run();
      onTaskCompleted(task);      
      task = null;
      }
    catch(RuntimeException e)
      {
//      Config.logDebug("caught runtime exception from pool thread");
      }    
    }
  }
}

}
