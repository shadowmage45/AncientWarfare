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

import shadowmage.ancient_warfare.common.config.Config;

public class PathBenchmarking
{


private static PathBenchmarking INSTANCE = new PathBenchmarking();
public static PathBenchmarking instance(){return INSTANCE;} 


private PathWorldAccessTest world = new PathWorldAccessTest();
private PathFinderJPS patherJPS = new PathFinderJPS();
private PathFinder pather = new PathFinder();

long t;
long total;

public void doTestNormal(float maxLength)
  {
  for(int i = 0; i <100; i++)
    {
    t = System.nanoTime();
    pather.findPath(world, 1, 1, 1, 40, 1, 40, (int)maxLength);
    total += System.nanoTime()-t;
    }
  Config.logDebug("100 x A* pathfinding runs: "+total/1000000+"ms   "+ total);
  total = 0;
  }

public void doTestJPS(float maxLength)
  {
  for(int i = 0; i <100; i++)
    {
    t = System.nanoTime();
    patherJPS.findPath(world, 1, 1, 1, 40, 1, 40, (int)maxLength);
    total += System.nanoTime()-t;
    }
  Config.logDebug("100 x JPS pathfinding runs: "+total/1000000+"ms   "+ total);
  total = 0;
  }


}
