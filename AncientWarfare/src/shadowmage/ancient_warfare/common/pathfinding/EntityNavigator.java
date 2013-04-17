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

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IPathableEntity;
import shadowmage.ancient_warfare.common.pathfinding.threading.IPathableCallback;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.Trig;

public class EntityNavigator implements IPathableCallback
{
IPathableEntity owner;
Entity entity;
EntityPath path = new EntityPath();
public PathWorldAccess worldAccess;
//public PathScheduler scheduler;
int targetX;
int targetY;
int targetZ;
int prevEx;
int prevEy;
int prevEz;
int stuckTicks = 0;
Node targetNode = null;

public boolean canOpenDoors = false;

public EntityNavigator(IPathableEntity owner)
  {
	this.owner = owner;
  this.entity = owner.getEntity();
  this.worldAccess = owner.getWorldAccess();
  this.targetX = MathHelper.floor_double(entity.posX);
  this.targetY = MathHelper.floor_double(entity.posY);
  this.targetZ = MathHelper.floor_double(entity.posZ);
  this.prevEx = targetX;
  this.prevEy = targetY;
  this.prevEz = targetZ;
//  if(entity.worldObj.isRemote)
//    {
//    this.scheduler = PathScheduler.clientInstance();
//    }
//  else
//    {
//    this.scheduler = PathScheduler.serverInstance();
//    }
  }

protected boolean canPathStraightToTarget(int ex, int ey, int ez, int tx, int ty, int tz)
  {
  int yOffset = ty-ey;
  int currentY = ey;
  if(Math.abs(yOffset)>1)
    {
    return false;
    }
  List<BlockPosition> blockHits = PathUtils.getPositionsBetween2(ex, ez, tx, tz);
  for(BlockPosition hit : blockHits)
    {
    if(worldAccess.isWalkable(hit.x, currentY-1, hit.z))
      {
      currentY--;
      }
    else if(worldAccess.isWalkable(hit.x, currentY, hit.z))
      {
      
      }
    else if(worldAccess.isWalkable(hit.x, currentY+1, hit.z))
      {
      currentY++;
      }
    else
      {
      return false;
      }
    if(Math.abs(currentY-ty)>1)
      {
      return false;
      }
    }
  return true;
  }

/**
 * called frequently, every x ticks (5) from aiTasks
 * @param tx
 * @param ty
 * @param tz
 */
public void setMoveTo(int tx, int ty, int tz)
  {  
  int ex = MathHelper.floor_double(entity.posX);
  int ey = MathHelper.floor_double(entity.posY);
  int ez = MathHelper.floor_double(entity.posZ);
  boolean calcPath = false;    
  if(ex==prevEx && ez==prevEz && !owner.isPathableEntityOnLadder())
    {
    stuckTicks++;
    if(stuckTicks > 20/Config.npcAITicks)
      {
      calcPath = true;
      stuckTicks = 0;
      this.clearPath();
      Config.logDebug("detecting stuck, recalcing path");
      }
    }
  else
    {
    stuckTicks = 0;
    }
  prevEx = ex;
  prevEy = ey;
  prevEz = ez;
  if(canPathStraightToTarget(ex, ey, ez, tx, ty, tz))
    {
    this.targetNode = new Node(tx,ty,tz);
    this.targetX = tx;
    this.targetY = ty;
    this.targetZ = tz;
    return;
    }
 
  Node endNode = this.path.getEndNode();    
  if(endNode==null && targetNode==null)//we have no path, request a starter, and full if necessary
    {
//  Config.logDebug("new target and had no path, recalculating path");
    calcPath = true;    
    }
  else if(endNode!=null)//we have a path already...
    {
    float dist = Trig.getVelocity(tx-this.targetX, ty-this.targetY, tz-this.targetZ);
    float length = Trig.getVelocity(ex-tx, ey-ty, ez-tz);
    if(dist > 4 && dist > length * 0.2f)//quite a bit of difference between current target/path and new target path
      {
      float endDist = Trig.getVelocity(tx-endNode.x, ty-endNode.y, tz-endNode.z);
      if((endDist < 3 && endNode.y==ty) || endDist < length * 0.2f)//try and re-use entire old path
        {
        targetX = tx;
        targetY = ty;
        targetZ = tz;
        PathManager.instance().requestPath(this, worldAccess, endNode.x, endNode.y, endNode.z, tx, ty, tz, 60);
        }
      else
        {
        //try and see if target is anywhere near a point on old path, and recalc/reqeuest addition from that point
        //else request full new path
        calcPath = true;
        }      
      }    
    }
  if(calcPath)
    { 
    ey = PathUtils.findClosestYTo(worldAccess, ex, ey, ez);
    this.calcPath(ex, ey, ez, tx, ty, tz);
    }
  }

public void clearPath()
  {
  this.targetNode = null;
  this.targetX = MathHelper.floor_double(entity.posX);
  this.targetY = MathHelper.floor_double(entity.posY);
  this.targetZ = MathHelper.floor_double(entity.posZ);
  this.path.setPath(new ArrayList<Node>());
  }

protected void calcPath(int ex, int ey, int ez, int tx, int ty, int tz)
  {
  this.path.setPath(PathManager.instance().findStartPath(worldAccess, ex, ey, ez, tx, ty, tz, tz));//grab a quick path if just to start moving
  Node endNode = this.path.getEndNode();
  if(endNode!=null)
    {
    if(endNode.x!=tx || endNode.y!=ty || endNode.z != tz)//if we didn't find the target, request a full pathfind from end of starter path to the target.
      {
      PathManager.instance().requestPath(this, worldAccess, endNode.x, endNode.y, endNode.z, targetX, targetY, targetZ, 60);
      }      
    }
  this.targetX = tx;
  this.targetY = ty;
  this.targetZ = tz;
  this.claimNode();
  }

protected void claimNode()
  {
  this.targetNode = this.path.claimNode();  
  }

/**
 * used to force-set a path..via wander, pre-calculated, etc..
 * @param pathNodes
 */
public void setPath(List<Node> pathNodes)
  {      
  if(pathNodes!=null && pathNodes.size()>=1)
    {
    Node end = pathNodes.get(pathNodes.size()-1);
    this.targetX = end.x;
    this.targetY = end.y;
    this.targetZ = end.z; 
    this.path.setPath(pathNodes);
    this.claimNode();
    }
  }

public void moveTowardsCurrentNode()
  {
  if(this.targetNode==null && this.path.getPathNodeLength()>0)
    {
    this.claimNode();
    }
  if(this.targetNode!=null)
    {     
    int ex = MathHelper.floor_double(entity.posX);
    int ey = MathHelper.floor_double(entity.posY);
    int ez = MathHelper.floor_double(entity.posZ);
    if(ex==targetNode.x && ey==targetNode.y && ez == targetNode.z || Trig.getDistance(ex, ey, ez, targetNode.x, targetNode.y, targetNode.z)<0.8f)
      {
      this.claimNode();
      if(targetNode==null)
        {   
        return;
        }
      }    
    if(owner.isPathableEntityOnLadder())
      {
      if(targetNode.y<ey)
        {
        entity.motionY = -0.125f;
        }
      else if(targetNode.y>ey)
        {
        entity.motionY = 0.125f;
        }    
      }
    owner.setMoveTo(targetNode.x+0.5f, targetNode.y, targetNode.z+0.5f);
    }  
  }

public List<Node> getCurrentPath()
  {
  return path.getPath();
  }

@Override
public void onPathFound(List<Node> pathNodes)
  {  
  pathNodes.remove(0);
  this.path.addPath(pathNodes);
  }
@Override
public void onPathFailed(List<Node> partialPathNodes)
  {

  }

}
