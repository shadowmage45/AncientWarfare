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

public class EntityNavigatorThreaded implements IPathableCallback
{

NpcBase entity;
EntityPath path = new EntityPath();
PathWorldAccessEntity worldAccess;

List<Node> foundPath = null;

double x;
double y;
double z;

float maxPathLength = 20;

Node targetNode = null;

boolean addedPreviously = false;
boolean continuePath = false;

public EntityNavigatorThreaded(NpcBase owner)
  {
  this.entity = owner;
  this.worldAccess = new PathWorldAccessEntity(entity.worldObj, entity);
  this.x = entity.posX;
  this.y = entity.posY;
  this.z = entity.posZ;
  }

/**
 * called frequently, every x ticks (10) from aiTasks
 * @param tx
 * @param ty
 * @param tz
 */
public void setMoveTo(int tx, int ty, int tz)
  { 
  if(this.targetNode==null && this.foundPath!=null)
    {
    this.path.setPath(foundPath);
    this.targetNode = path.claimNode();
    this.x = targetNode.x;
    this.y = targetNode.y;
    this.z = targetNode.z;
    }
  int ex = MathHelper.floor_double(entity.posX);
  int ey = MathHelper.floor_double(entity.posY);
  int ez = MathHelper.floor_double(entity.posZ);
  float dist = Math.abs(Trig.getVelocity(tx-x, ty-y, tz-z));
  Node n = path.getEndNode();
  Config.logDebug("E:T:G "+ex+","+ey+","+ez+" :: "+tx+","+ty+","+tz+" :: "+n);
  float targetDiff = dist;
  
  if(targetNode!=null)
    {
    targetDiff = Trig.getDistance(x, y, z, tx, ty, tz);
    }  
  boolean calcPath = false;
  if(tx==(int)x && ty==(int)y && tz==(int)z)//we're already pathing there, check to see how many nodes are left in the current path, recalc if getting low
    {
    Config.logDebug("already have same target");
    if(n==null || (n.x!= (int)tx || n.y != (int)ty || n.z != (int)tz))//close, but goal node is not the current target
      {
      if(path.getPathNodeLength()<3)
        {
        Config.logDebug("same target, but node length is low and target is not goal node, recalculating");
        calcPath = true;          
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
      calcPath = true; 
      }
    else//paths could vary wildy, probably best to recalculate....try and preserve some of old?
      {
      Config.logDebug("new target, recalculating");
      calcPath = true;
      }
    }
  if(targetNode!=null)
    {
    if(Trig.getDistance(ex, ey, ez, targetNode.x, targetNode.y, targetNode.z)>3)//has fallen, been pushed, some other crap..recalc;
      {
      Config.logDebug("recalc due to falling/pushed/other stuff...");
      calcPath = true;
      }
    }
  if(calcPath)
    {    
    this.foundPath = null;
    this.x = tx;
    this.y = ty;
    this.z = tz; 
    if(!PathThreadManager.instance(!this.entity.worldObj.isRemote).containsPather(this))
      {
      Config.logDebug("starting path request");
      PathThreadManager.instance(!this.entity.worldObj.isRemote).requestPath(this, worldAccess, ex, ey, ez, tx, ty, tz, (int)this.maxPathLength);      
      }
    else//path request already queued
      {
      Config.logDebug("attempting update of path");
      if(!PathThreadManager.instance(!this.entity.worldObj.isRemote).tryUpdatingPathEntry(this, worldAccess, ex, ey, ez, tx, ty, tz, (int)this.maxPathLength))
        {
        
        //check if target of path is same as current target (or close)
        //if not close, interrupt current and put a new one in the queue
        Config.logDebug("pathing already started");
        }   
      }     
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

public void moveTowardsCurrentNode()
  {
  if(this.foundPath!=null)
    {
    this.path.setPath(foundPath);
    this.targetNode = path.claimNode();
    this.foundPath = null;
    }
  if(this.targetNode!=null)
    {
    int ex = MathHelper.floor_double(entity.posX);
    int ey = MathHelper.floor_double(entity.posY);
    int ez = MathHelper.floor_double(entity.posZ);
    if(ex==targetNode.x && ey==targetNode.y && ez == targetNode.z || Trig.getDistance(ex, ey, ez, targetNode.x, targetNode.y, targetNode.z)<1.0f)
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
      }
    entity.getMoveHelper().setMoveTo(targetNode.x+0.5d, targetNode.y, targetNode.z+0.5d, entity.getAIMoveSpeed());
    } 
  }

public List<Node> getCurrentPath()
  {
  return path.getPath();
  }

@Override
public void onPathFound(List<Node> pathNodes)
  {
  Config.logDebug("thread sent complete path to navigator");
  this.foundPath = pathNodes;
  }


}
