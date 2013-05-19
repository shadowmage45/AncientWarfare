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

import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.pathfinding.queuing.PathScheduler;
import shadowmage.ancient_warfare.common.pathfinding.threading.IPathableCallback;
import shadowmage.ancient_warfare.common.pathfinding.threading.PathThreadPool;

public class PathManager
{

PathFinderThetaStar staticPather = new PathFinderThetaStar();
PathFinderThetaStar staticPatherClient = new PathFinderThetaStar();

private PathManager(){}
private static PathManager instance = new PathManager();
public static PathManager instance()
  {
  return instance;
  }

//public void requestPath(IPathableCallback caller, PathWorldAccess world, int x, int y, int z, int tx, int ty, int tz, int maxRange)
//  {
//  switch(Config.npcPathfinderType)
//  {
//  case 0://in-line, do now
//  caller.onPathFound(staticPather.findPath(world, x, y, z, tx, ty, tz, maxRange));
//  break;
//  
//  case 1://scheduled, call scheduler for appropriate world-side
//  if(world.isRemote())
//    {
//    PathScheduler.clientInstance().requestPath(caller, world, x, y, z, tx, ty, tz);
//    }
//  else
//    {
//    PathScheduler.serverInstance().requestPath(caller, world, x, y, z, tx, ty, tz);
//    }
//  break;
//  
//  case 2:
//  PathThreadPool.instance().requestPath(caller, world, x, y, z, tx, ty, tz, maxRange);    
//  break;
//  
//  default:
//  caller.onPathFound(staticPather.findPath(world, x, y, z, tx, ty, tz, maxRange));
//  break;
//  }
//  }

//public List<Node> findImmediatePath(PathWorldAccess world, int x, int y, int z, int tx, int ty, int tz)
//  {
//  if(world.isRemote())
//    {
//    return staticPatherClient.findPath(world, x, y, z, tx, ty, tz, 60);
//    }
//  else
//    {
//    return staticPather.findPath(world, x, y, z, tx, ty, tz, 60);
//    }
//  }
//
//public List<Node> findStartPath(PathWorldAccess world, int x, int y, int z, int tx, int ty, int tz, int maxRange)
//  {
//  return staticPather.findPath(world, x, y, z, tx, ty, tz, 6);
//  }

}
