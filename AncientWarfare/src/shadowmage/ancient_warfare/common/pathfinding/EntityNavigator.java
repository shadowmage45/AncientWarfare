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

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
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
    if(worldAccess.isDoor(hit.x, hit.y, hit.z))
      {
      return false;
      }
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
//      calcPath = true;
      stuckTicks = 0;
      List<Node> fullPath = this.path.getActivePath();
      float dist = Float.MAX_VALUE;
      int closestNodeIndex = 0;
      Node n = null;
      for(int i = 0; i < fullPath.size(); i++)
        {
        n = fullPath.get(i); 
        if(getDistanceFromNode(n)<dist)
          {
          closestNodeIndex = i;
          dist = getDistanceFromNode(n);
          }
        else
          {
          break;
          }        
        }
      if(n!=null && dist<2 && ey==n.y)
        {
        List<Node> newPath = new ArrayList<Node>();
        for(int i = 0; i < fullPath.size(); i++)
          {
          if(i<closestNodeIndex)
            {
            continue;
            }
          newPath.add(fullPath.get(i));
          }
        this.path.setPath(newPath);      
//        this.clearPath();
        Config.logDebug("detecting stuck, reseating path");
        this.claimNode();
        }
      else
        {
        Config.logDebug("detecting stuck, recalculating path");
        calcPath = true;
        }      
      }
    }
  else
    {
    stuckTicks = 0;
    }
  prevEx = ex;
  prevEy = ey;
  prevEz = ez; 
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
  else//target is not null, but end node is
    {
    if(targetNode.x != tx || targetNode.y != ty || targetNode.z != tz)
      {
      if(canPathStraightToTarget(ex, ey, ez, tx, ty, tz))
        {
        this.targetNode = new Node(tx,ty,tz);
        this.targetX = tx;
        this.targetY = ty;
        this.targetZ = tz;
        return;
        }
      else
        {
        PathManager.instance().requestPath(this, worldAccess, targetNode.x, targetNode.y, targetNode.z, tx, ty, tz, 60);
        }
      }
    }
  if(calcPath)
    { 
    ey = PathUtils.findClosestYTo(worldAccess, ex, ey, ez);//set entity start-block to a valid block..hopefully
    this.calcPath(ex, ey, ez, tx, ty, tz);
    }
  }

public void clearPath()
  {
  this.targetNode = null;
  this.targetX = MathHelper.floor_double(entity.posX);
  this.targetY = MathHelper.floor_double(entity.posY);
  this.targetZ = MathHelper.floor_double(entity.posZ);
  this.path.clearPath();
  }

protected void calcPath(int ex, int ey, int ez, int tx, int ty, int tz)
  {
  this.path.setPath(PathManager.instance().findStartPath(worldAccess, ex, ey, ez, tx, ty, tz, tz));//grab a quick path if just to start moving
  Node endNode = this.path.getEndNode();
  if(endNode!=null)
    {
    if(endNode.x!=tx || endNode.y!=ty || endNode.z != tz)//if we didn't find the target, request a full pathfind from end of starter path to the target.
      {
      Config.logDebug("requesting full path");
      PathManager.instance().requestPath(this, worldAccess, endNode.x, endNode.y, endNode.z, tx, ty, tz, 60);
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
    }
  this.claimNode();
  }

protected boolean shouldClaimNextNode()
  {  
  return this.path.getActivePathSize()>0 && this.path.getFirstNode()!=null && (this.targetNode==null || getDistanceFromNode(targetNode)<entity.width);
  }

public void moveTowardsCurrentNode()
  {
  this.updateMoveHelper();
  while(this.shouldClaimNextNode())
    {    
    this.claimNode();
    Config.logDebug("claimiing node " + this.targetNode);
    }  
  if(this.targetNode!=null)
    {     
    int ex = MathHelper.floor_double(entity.posX);
    int ey = MathHelper.floor_double(entity.posY);
    int ez = MathHelper.floor_double(entity.posZ);      
    if(owner.isPathableEntityOnLadder())
      {
      if(targetNode.y<ey)
        {
        entity.motionY = -0.125f;
        entity.motionX *= 0.5f;
        entity.motionZ *= 0.5f;
        }
      else if(targetNode.y>ey)
        {
        entity.motionY = 0.125f;
        entity.motionX *= 0.5f;
        entity.motionZ *= 0.5f;
        }    
      }
    if(this.canOpenDoors)
      {
      this.doorInteraction(ex, ey, ez, targetNode.x, targetNode.y, targetNode.z); 
      }
    owner.setMoveTo(targetNode.x+0.5f, targetNode.y, targetNode.z+0.5f, owner.getDefaultMoveSpeed());
    }  
  }

protected float getDistanceFromNode(Node n)
  {
  return n==null? 0.f : (float) entity.getDistance(n.x+0.5d, n.y, n.z+0.5d);
  }

protected boolean isAtNode(int ex, int ey, int ez, Node n)
  {
  return ex==n.x && ez==n.z && ey==n.y;
  }

protected boolean isCurrentNodeADoor()
  {
  return this.targetNode!=null && this.worldAccess.isDoor(this.targetNode.x, this.targetNode.y, this.targetNode.z);
  }

protected boolean isNextNodeADoor()
  {
  Node n = path.getFirstNode();
  return n != null && this.worldAccess.isDoor(n.x, n.y, n.z);
  }

protected void updateMoveHelper()
  {
  if(this.doorOpenTicks>0)
    {
    this.doorOpenTicks--;        
    }      
  if(this.hasDoor)
    {
    if(this.doorOpenTicks<=0)
      {
      this.hasDoor = false;
      this.interactWithDoor(doorPos, false);
      } 
    }
  }

protected void doorInteraction(int ex, int ey, int ez, int tx, int ty, int tz)
  {
  if(this.doorCheckTicks<=0)
    {
    this.doorCheckTicks = this.doorCheckTicksMax;
    if(this.entity.isCollidedHorizontally && checkForDoors(ex, ey, ez,tx, ty, tz))
      {
      if(doorPos!=null)
        {
        this.interactWithDoor(doorPos, true);
        this.doorOpenTicks = this.doorOpenMax;
        }
      }        
    }
  else
    {
    this.doorCheckTicks--;
    }  
  }

protected boolean hasDoor = false;
protected BlockPosition doorPos = new BlockPosition(0,0,0);
protected int doorOpenTicks = 0;
protected int doorCheckTicks = 0;
protected int doorOpenMax = 15;
protected int doorCheckTicksMax = 5;

protected boolean checkForDoors(int ex, int ey, int ez, int tx, int ty, int tz)
  {
  int doorId = Block.doorWood.blockID;
  int id;
  id = entity.worldObj.getBlockId(ex, ey, ez);
  if(id==doorId)
    {
    doorPos.x = ex;
    doorPos.y = ey;
    doorPos.z = ez;
    hasDoor = true;
//    Config.logDebug("found door at: "+doorPos + " entPos: "+entity);
    return true;
    }
  float yaw = entity.rotationYaw;
  while(yaw<0)
    {
    yaw+=360.f;
    }
  while(yaw>=360.f)
    {
    yaw-=360.f;
    }
  int x = ex;
  int y = ey;
  int z = ez;
  if(yaw>=360-45 || yaw<45)//south, check z+
    {
    z++;
    }
  else if(yaw>=45&& yaw<45+90)//west, check x+
    {
    x--;
    }
  else if(yaw>=180-45 && yaw<180+45)//north
    {
    z--;
    }
  else//east
    {
    x++;
    }
  id = entity.worldObj.getBlockId(x, y, z);
  if(id==doorId)
    {
    doorPos.x = x;
    doorPos.y = y;
    doorPos.z = z;
    hasDoor = true;
//    Config.logDebug("found door at: "+doorPos);
    return true;
    }  
  return false;
  }

protected void interactWithDoor(BlockPosition doorPos, boolean open)
  {
  Block block = Block.blocksList[entity.worldObj.getBlockId(doorPos.x, doorPos.y, doorPos.z)];
  if(block.blockID==Block.doorWood.blockID)
    {
    ((BlockDoor)block).onPoweredBlockChange(entity.worldObj, doorPos.x, doorPos.y, doorPos.z, open);
    }
  }

public List<Node> getCurrentPath()
  {
  return path.getActivePath();
  }

@Override
public void onPathFound(List<Node> pathNodes)
  {  
  pathNodes.remove(0);
  this.path.addPath(worldAccess, pathNodes);
  }


}
