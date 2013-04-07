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

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.pathfinding.EntityPath;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccessEntity;
import shadowmage.ancient_warfare.common.pathfinding.threading.IPathableCallback;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.utils.Trig;

/**
 * entity navigator that relies on PathScheduler to receive full paths.
 * 
 * Navigator attempts to re-use current path whenever possible, to reduce path requests, and the lengths of requested paths
 * 
 * 
 * path selection when 'set move to' is called
 *  if has no path
 *    reset path request count
 *    request starter path (short cutoff)
 *    if starter path end node!= target
 *      request full path, starting at starter path end node
 *  else if has path
 *    if path end node != target  
 *      if current target == new target && path request count < max request count (else mark it as 'unpathable')(perhaps try a quick backwards path search to see if it is even possible)
 *        request another path 'addition' from scheduler, from current end node to target
 *      else check distance between current path end node and new target
 *        if distance between new targets ratio to total target distance < X, keep current path (it will recalc when it gets closer due to the ratio)
 *        else try and see if can keep old path (similar direction of travel), full or partial, request 'addition' from a position in old path to new target
 *            if new target is past the end of the current path or new target is within X blocks of current end node, just request an 'addition'
 *        else drop path completely request new starter and full path (if necessary)
 *    else end node is correct, do nothing, target/path are (supposedly) correct
 *      
 * @author Shadowmage
 *
 */
public class NpcNavigatorScheduled implements IPathableCallback
{

private static float boidSearchRange = 4.f;
NpcBase entity;
EntityPath path = new EntityPath();
PathWorldAccessEntity worldAccess;
PathScheduler scheduler;
int targetX;
int targetY;
int targetZ;

Node targetNode = null;

public NpcNavigatorScheduled(NpcBase owner)
  {
  this.entity = owner;
  this.worldAccess = new PathWorldAccessEntity(entity.worldObj, entity);
  this.targetX = MathHelper.floor_double(entity.posX);
  this.targetY = MathHelper.floor_double(entity.posY);
  this.targetZ = MathHelper.floor_double(entity.posZ);
  if(owner.worldObj.isRemote)
    {
    this.scheduler = PathScheduler.clientInstance();
    }
  else
    {
    this.scheduler = PathScheduler.serverInstance();
    }
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
  Node endNode = this.path.getEndNode();    
  if(endNode==null && ex!=tx || ez!=tz || ey != ty)//we have no path, request a starter, and full if necessary
    {
    calcPath = true;    
    //    Config.logDebug("new target and had no path, recalculating path");
    }
  else//we have a path already...
    {
    float dist = Trig.getVelocity(tx-this.targetX, ty-this.targetY, tz-this.targetZ);
    float length = Trig.getVelocity(ex-tx, ey-ty, ez-tz);
    if(dist > 4 && dist > length * 0.2f)//quite a bit of difference between current target/path and new target path
      {
      float endDist = Trig.getVelocity(tx-endNode.x, ty-endNode.y, tz-endNode.z);
      if(endDist < 4 || endDist < length * 0.2f)//try and re-use entire old path
        {
        targetX = tx;
        targetY = ty;
        targetZ = tz;
        this.scheduler.requestPath(this, worldAccess, endNode.x, endNode.y, endNode.z, tx, ty, tz);
        //        Config.logDebug("attempting re-use of entire old path with addition");
        }
      else
        {
        //try and see if target is anywhere near a point on old path, and recalc/reqeuest addition from that point
        //else request full new path
        //        Config.logDebug("new target and had path, large diff from old end-node, recalculating path");
        calcPath = true;
        }      
      }
    else
      {
      //      Config.logDebug("skipping recalc due to little difference");
      }
    }
  if(calcPath)
    {   
    this.path.setPath(this.scheduler.requestStartPath(worldAccess, ex, ey, ez, tx, ty, tz));//grab a quick path if just to start moving
    endNode = this.path.getEndNode();
    if(endNode!=null)
      {
      if(endNode.x!=tx || endNode.y!=ty || endNode.z != tz)//if we didn't find the target, request a full pathfind from end of starter path to the target.
        {
        //        Config.logDebug("starter path did not complete, requesting full");
        this.scheduler.requestPath(this, worldAccess, endNode.x, endNode.y, endNode.z, targetX, targetY, targetZ);
        }      
      }
    this.targetX = tx;
    this.targetY = ty;
    this.targetZ = tz;
    this.targetNode = this.path.claimNode();
    //    if(!entity.worldObj.isRemote)
    //      {
    //      NBTTagCompound tag = new NBTTagCompound();
    //      tag.setInteger("tx", tx);
    //      tag.setInteger("ty", ty);
    //      tag.setInteger("tz", tz);
    //      Packet04Npc pkt = new Packet04Npc();
    //      pkt.setParams(entity);
    //      pkt.setPathTarget(tag);
    //      pkt.sendPacketToAllTrackingClients(entity);
    //      }
    }
  }

public void moveTowardsCurrentNode()
  {
  if(this.targetNode==null && this.path.getPathNodeLength()>0)
    {
    this.targetNode = this.path.claimNode();
    }
  if(this.targetNode!=null)
    {   
    int ex = MathHelper.floor_double(entity.posX);
    int ey = MathHelper.floor_double(entity.posY);
    int ez = MathHelper.floor_double(entity.posZ);
    if(ex==targetNode.x && ey==targetNode.y && ez == targetNode.z || Trig.getDistance(ex, ey, ez, targetNode.x, targetNode.y, targetNode.z)<1.0f)
      {
      //  Config.logDebug("claiming node from completion LATE "+this.targetNode+"::"+entity);
      this.targetNode = path.claimNode();
      //  Config.logDebug("new :: "+this.targetNode);
      if(targetNode==null)
        {        
        entity.getMoveHelper().setMoveTo(entity.posX, entity.posY, entity.posZ, entity.getAIMoveSpeed());
        return;
        }
      }    
    if(entity.isOnLadder())
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
    float[] movePos = this.getAdjustedMovePosition(targetNode.x+0.5f, targetNode.y, targetNode.z+0.5f);
    entity.getMoveHelper().setMoveTo(movePos[0], movePos[1], movePos[2], entity.getAIMoveSpeed());
    } 
  }

private float[] getAdjustedMovePosition(float tx, float ty, float tz)
  {
  return new float[]{tx,ty,tz};
//  float len = Trig.getVelocity(tx-entity.posX, ty-entity.posY, tz-entity.posZ);
//  List<NpcBase> neighbors = this.getNearbyNPCs();
//  //just try and stay X distance from nearby soldiers...
//  float ax = 0;
//  float ay = 0;
//  float az = 0;
//  Entity target = entity.getTargetEntity();
//  for(Entity n : neighbors)
//    {
//    if(n!=target && n!=entity && (int)n.posY==(int)entity.posY && n.getDistanceToEntity(entity)<((n.width+entity.width)*1.41f)+entity.width*2)
//      {
//      ax = ax - (float)(entity.posX - n.posX);
//      //ay = ay - (float)(entity.posY - n.posY);
//      az = az - (float)(entity.posZ - n.posZ);
//      }
//    }
//  ax*=0.2f;
//  ay*=0.2f;
//  az*=0.2f;
////  Config.logDebug("A's:: "+ax+","+ay+","+az);
//  //
//  //if one is in front of you, hold a certain distance from it
//  //    repeat until clear
//  //check new target block after avoidance check to make sure it is a valid destination block
//  
//  return new float[]{tx-ax, ty-ay, tz-az};
  }

private List<NpcBase> getNearbyNPCs()
  {
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(entity.posX-boidSearchRange, entity.posY-boidSearchRange, entity.posZ-boidSearchRange, entity.posX+boidSearchRange, entity.posY+boidSearchRange, entity.posZ+boidSearchRange);
  List<NpcBase> entityList = entity.worldObj.getEntitiesWithinAABB(NpcBase.class, bb);  
  return entityList;
  }

public List<Node> getCurrentPath()
{
return path.getPath();
}

@Override
public void onPathFound(List<Node> pathNodes)
  {  
  this.path.addPath(pathNodes);
  }

@Override
public void onPathFailed(List<Node> partialPathNodes)
  {

  }
}
