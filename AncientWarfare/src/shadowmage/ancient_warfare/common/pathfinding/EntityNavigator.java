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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.Packet04Npc;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.utils.Trig;

public class EntityNavigator
{

NpcBase entity;
EntityPath path = new EntityPath();
PathWorldAccessEntity worldAccess;
PathFinderThetaStar pather;

double x;
double y;
double z;

float maxPathLength = 60;

Node targetNode = null;

boolean addedPreviously = false;
boolean continuePath = false;

public EntityNavigator(NpcBase owner)
  {
  this.entity = owner;
  this.worldAccess = new PathWorldAccessEntity(entity.worldObj, entity);
  pather = new PathFinderThetaStar();  
  this.x = entity.posX;
  this.y = entity.posY;
  this.z = entity.posZ;
  }

/**
 * called frequently, every x ticks (5) from aiTasks
 * @param tx
 * @param ty
 * @param tz
 */
public void setMoveTo(int tx, int ty, int tz)
  {  
  //if can see target on eye X and foot X (no obstacles in straight path)
  //  {
  //  check all blocks in a line beneath that path to see if they are walkable
  //    if true, set targetNode to target, move in a straight line towards that path
  //  }
  int ex = MathHelper.floor_double(entity.posX);
  int ey = MathHelper.floor_double(entity.posY);
  int ez = MathHelper.floor_double(entity.posZ);
  float dist = Math.abs(Trig.getVelocity(tx-x, ty-y, tz-z));
  float targetDiff = dist;
  Node n = path.getEndNode();
  if(targetNode!=null)
    {
    targetDiff = Trig.getDistance(x, y, z, tx, ty, tz);
    }  
  boolean calcPath = false;
  if(tx==x && ty==y && tz==z)//we're already pathing there, check to see how many nodes are left in the current path, recalc if getting low
    {    
    if(n==null || (n.x!= (int)tx || n.y != (int)ty || n.z != (int)tz))//close, but goal node is not the current target
      {
      if(path.getPathNodeLength()<3)
        {
        Config.logDebug("same target, but node length is low, recalculating");
        Config.logDebug("adding to path");
        if(n!=null)
          {
//          path.addPath(pather.findPath(worldAccess, n.x, n.y, n.z, tx, ty, tz, (int)maxPathLength));
          x = tx;
          y = ty;
          z = tz;
          }
        else
          {
          calcPath = true;
          }
        }
      }
    }
  else if(n!=null && n.x==tx &&n.y==ty &&n.z==tz)
    {
    Config.logDebug("already had perfect path, not recalcing");
    }
  else
    {    
    if(dist > maxPathLength && targetDiff<dist*0.3f)//its so close to our current target, don't bother changing path
      {
      Config.logDebug("far away target with little difference, skipping recalc");
      }
    else if(dist<maxPathLength && targetDiff<dist*0.1f && targetDiff>1.f)//else add new nodes from current goal to new goal, reseat current target 
      {
      if(!addedPreviously)
        {
        addedPreviously = true;
        Config.logDebug("adding to path");
        if(n!=null)
          {
//          path.addPath(pather.findPath(worldAccess, n.x, n.y, n.z, tx, ty, tz, (int)maxPathLength));
          x = tx;
          y = ty;
          z = tz;
          }
        else
          {
          calcPath = true;
          }
        }
      else
        {
        Config.log("already added, recalc");
        calcPath = true;
        addedPreviously = false;
        }      
      
      }
    else//paths could vary wildy, probably best to recalculate....try and preserve some of old?
      {
      Config.logDebug("new target, recalculating");
      calcPath = true;
      }
    }
  if(false)//can see target node check...not sure
    {
    
    }
//  if(targetNode!=null)
//    {
//    if(Math.abs(entity.posY - (double)this.targetNode.y)>2.5d)//has fallen, been pushed, some other crap..recalc;
//      {
//      Config.logDebug("recalc due to falling/pushed/other stuff...");
//      calcPath = true;
//      }
//    }
  if(calcPath)
    {
    if(targetDiff<3 && this.targetNode!=null)
      {
      Config.logDebug("setting previous target node for trimming of new path");
      //pather.setPreviousPathEndNode(targetNode);
      }
    path.setPath(pather.findPath(worldAccess, ex, ey, ez, tx, ty, tz, (int) maxPathLength));    
    this.x = tx;
    this.y = ty;
    this.z = tz;
    this.targetNode = null;
    this.targetNode = path.claimNode();
    } 
  if(!entity.worldObj.isRemote)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("tx", tx);
    tag.setInteger("ty", ty);
    tag.setInteger("tz", tz);
    Packet04Npc pkt = new Packet04Npc();
    pkt.setParams(entity);
    pkt.setPathTarget(tag);
    pkt.sendPacketToAllTrackingClients(entity);
    }
  }

boolean wasOnLadder = false;

public void moveTowardsCurrentNode()
  {
  if(this.targetNode!=null)
    {
    //entity.getNavigator().tryMoveToXYZ(targetNode.x+0.5d, targetNode.y, targetNode.z+0.5d, entity.getAIMoveSpeed());
    
    int ex = MathHelper.floor_double(entity.posX);
    int ey = MathHelper.floor_double(entity.posY);
    int ez = MathHelper.floor_double(entity.posZ);
    if(ex==targetNode.x && ey==targetNode.y && ez == targetNode.z || (Trig.getDistance(ex, ey, ez, targetNode.x, targetNode.y, targetNode.z)<1.00f && ey==targetNode.y))
      {
//      Config.logDebug("claiming node from completion LATE "+this.targetNode+"::"+entity);
      this.targetNode = path.claimNode();
//      Config.logDebug("new :: "+this.targetNode);
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
      else
        {
        
        }
      wasOnLadder = true;
      }
    else if(wasOnLadder)
      {
      wasOnLadder = false;
      }   
    
    entity.getMoveHelper().setMoveTo(targetNode.x+0.5d, targetNode.y, targetNode.z+0.5d, entity.getAIMoveSpeed());
    } 
  }

public List<Node> getCurrentPath()
  {
  return path.getPath();
  }

}
