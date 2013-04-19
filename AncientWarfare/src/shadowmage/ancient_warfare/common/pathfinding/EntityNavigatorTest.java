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

import java.util.Random;

import net.minecraft.util.MathHelper;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IPathableEntity;

public class EntityNavigatorTest extends EntityNavigator
{
protected Random rng = new Random();

/**
 * @param owner
 */
public EntityNavigatorTest(IPathableEntity owner)
  {
  super(owner);
  }

@Override
protected boolean canPathStraightToTarget(int ex, int ey, int ez, int tx, int ty, int tz)
  {
  return super.canPathStraightToTarget(ex, ey, ez, tx, ty, tz);
  }

@Override
protected void calcPath(int ex, int ey, int ez, int tx, int ty, int tz)
  {  
  this.path.setPath(PathUtils.guidedCrawl(worldAccess, ex, ey, ez, tx, ty, tz, 60, rng ));  
  Node endNode = this.path.getEndNode();
  if(endNode!=null)
    {
    if(endNode.x!=tx || endNode.y!=ty || endNode.z != tz)//if we didn't find the target, request a full pathfind from end of starter path to the target.
      {
      Config.logDebug("crawl did not find target, requesting full path");
      PathManager.instance().requestPath(this, worldAccess, endNode.x, endNode.y, endNode.z, tx, ty, tz, 60);
      }      
    }
  this.targetX = tx;
  this.targetY = ty;
  this.targetZ = tz;
  this.claimNode();
  }
}
