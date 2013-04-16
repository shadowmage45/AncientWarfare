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
package shadowmage.ancient_warfare.common.pathfinding.queuing;

import java.util.LinkedList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathFinderThetaStar;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccess;
import shadowmage.ancient_warfare.common.pathfinding.threading.IPathableCallback;
import shadowmage.ancient_warfare.common.utils.Trig;

/**
 * attempted alternative to
 * @author Shadowmage
 *
 */
public class PathScheduler
{

/**
 * for immediately processed paths, what is the 'maxRange' that should be passed as the cutoff parameter?
 */
public static final int IMMEDIATE_PATH_CUTOFF = 6;

public static final int PATH_CUTOFF_LENGTH = 60;

public static final long PATH_CUTOFF_TIME = 2000000;//2ms


private PathFinderThetaStar pather = new PathFinderThetaStar();
private LinkedList<PathRequest> pathRequests = new LinkedList<PathRequest>();

long tickStartTime;
long tickEndTime;
long pathingMaxTime;

private static PathScheduler serverScheduler = new PathScheduler();
private static PathScheduler clientScheduler = new PathScheduler();
public static PathScheduler serverInstance(){return serverScheduler;}
public static PathScheduler clientInstance(){return clientScheduler;}
private PathScheduler(){}

/**
 * called when an entity firsts requests a path. uses special params on the pathfinder to ensure a quick/short path is found, just to get the entity going somewhere
 * @param world
 * @param x
 * @param y
 * @param z
 * @param tx
 * @param ty
 * @param tz
 */
public List<Node> requestStartPath(PathWorldAccess world, int x, int y, int z, int tx, int ty, int tz)
  {
  return this.pather.findPath(world, x, y, z, tx, ty, tz, 4);
  }

public void requestPath(IPathableCallback caller, PathWorldAccess world, int x, int y, int z, int tx, int ty, int tz)
  {
  this.pathRequests.add(new PathRequest(caller, world, x, y, z, tx, ty, tz, PATH_CUTOFF_LENGTH));
  }

public List<Node> getQuickPath(PathWorldAccess world, int x, int y, int z, int tx, int ty, int tz, int maxRange)
  {
  this.pather.quickStop = true;
  List<Node> nodes = this.pather.findPath(world, x, y, z, tx, ty, tz, maxRange);      
  this.pather.quickStop = false;
  return nodes;
  } 

public void onTickStart()
  {
  tickStartTime = System.nanoTime();
  }

public void onTickEnd()
  {
  tickEndTime = System.nanoTime();
  pathingMaxTime = (50000000l - (tickEndTime - tickStartTime));//
  pathingMaxTime = (long)((double)pathingMaxTime * 0.75d);
  this.startProcessingPaths();
  }

private void startProcessingPaths()
  {
  int totalProcessed = 0;
  long processingTime = 0;
  long jobStart;
  PathRequest req = null;
  while(processingTime<this.pathingMaxTime && !this.pathRequests.isEmpty())
    {    
    jobStart = System.nanoTime();
    totalProcessed++;
    req = this.pathRequests.pop();
    if(this.pathingMaxTime - processingTime < PATH_CUTOFF_TIME)
      {
      if(this.pathingMaxTime - processingTime < PATH_CUTOFF_TIME / 2)
        {
        break;//probably not enough time to do anything usefull
        }
      pather.maxRunTime = this.pathingMaxTime - processingTime;
      }
    else
      {
      pather.maxRunTime = PATH_CUTOFF_TIME;
      }
    req.caller.onPathFound(pather.findPath(req.world, req.x, req.y, req.z, req.tx, req.ty, req.tz, PATH_CUTOFF_LENGTH));
    processingTime += System.nanoTime()-jobStart;
    }
  if(totalProcessed>0 && this == serverScheduler)
    {
    Config.logDebug("processed: "+totalProcessed+" paths this tick. left in q: "+this.pathRequests.size()+".  Processing time: "+processingTime+" had: "+pathingMaxTime);
    }
  }

private class PathRequest
{
IPathableCallback caller;
PathWorldAccess world;
int x;
int y;
int z;
int tx;
int ty;
int tz;
int maxRange;
private PathRequest(IPathableCallback caller, PathWorldAccess world, int x, int y, int z, int tx, int ty, int tz, int maxRange)
  {
  this.caller = caller;
  this.world = world;
  this.x = x;
  this.y = y;
  this.z = z;
  this.tx = tx;
  this.tz = tz;
  this.ty = ty;
  this.maxRange =maxRange;
  }
}


}
