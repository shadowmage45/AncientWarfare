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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.Packet04Npc;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.utils.Trig;

public class EntityNavigator
{

NpcBase entity;
EntityPath path = new EntityPath();
PathWorldAccessEntity worldAccess;
PathFinder pather;

double x;
double y;
double z;

float maxPathLength = 16;

Node targetNode = null;

boolean addedPreviously = false;

public EntityNavigator(NpcBase owner)
  {
  this.entity = owner;
  this.worldAccess = new PathWorldAccessEntity(entity.worldObj, entity);
  pather = new PathFinder();  
  this.x = entity.posX;
  this.y = entity.posY;
  this.z = entity.posZ;
  }

private boolean checkBlocks2(int x0, int y0, int z0, int x1, int y1, int z1)
  {
  Config.logDebug("checking hits");
  boolean steepXY = Math.abs(y1-y0) > Math.abs(x1-x0);
  if(steepXY)
    {
    int t = x0;
    x0 = y0;
    y0 = t;
    t = x1;
    x1 = y1;
    y1 = t;
    //swap x0, y0
    //swap x1, y1
    }
  boolean steepXZ = Math.abs(z1 - z0)> Math.abs(x1-x0);
  if(steepXZ)
    {
    int t = x0;
    x0 = z0;
    z0 = t;
    t = x1;
    x1 = z1;
    z1 = t;
    //swap x0, z0
    //swap x1, z1
    }  
  Config.log("sXY: "+steepXY+ "  sXZ: "+steepXZ);
  int dX = Math.abs(x1 - x0);
  int dY = Math.abs(y1 - y0);
  int dZ = Math.abs(z1 - z0);
  int eXY = dX/2;
  int eXZ = dX/2;
  int sX = (x0>x1) ? -1 : 1;
  int sY = (y0>y1) ? -1 : 1;
  int sZ = (z0>z1) ? -1 : 1;
  int x;
  int y = y0;
  int z = z0;
  for(x = x0; x<=x1; x+=sX)
    {
    int x2 = x;
    int y2 = y;
    int z2 = z;
    if(steepXZ)
      {
      //swap x2, z2
      int t = x2;
      x2 = z2;
      z2 = t;
      }
    if(steepXY)
      {
      int t = x2;
      x2 = y2;
      y2 = t;
      //swap x2, y2
      }
    Config.logDebug("hit: "+x2+","+y2+","+z2);
    eXY -= dY;
    eXZ -= dZ;
    if(eXY<0)
      {
      y+=sY;
      eXY+=dX;
      }
    if(eXZ<0)
      {
      z+=sZ;
      eXZ+=dX;
      }
    }
  return false;
  }

private boolean checkBlocks(int x0, int y0, int z0, int x1, int y1, int z1)
  {
  Config.logDebug("checking hits");
  if(x0>x1)
    {
    int t = x0;
    x0 = x1;
    x1 = t;
    }
  if(z0>z1)
    {
    int t = z0;
    z0 = z1;
    z1 = t;
    }
  boolean swap = false;
  if(x0==x1)
    {
    swap = true;
    }
  if(z0==z1)
    {
    
    }
  int dx = x1-x0;  
  int dz = z1-z0;
  
  int D = 2*dz - dx;
  
  int z = z0;
  int x;
  Config.logDebug("checking hits");
  for(x = x0 ; x<= x1; x++ )
    {
    if(D>0)
      {
      z = z+1;
      Config.logDebug("blockHit :"+x+","+y0+","+z);
      D = D + (2*dz-2*dx);
      }
    else
      {
      Config.logDebug("blockHit :"+x+","+y0+","+z);
      D = D + (2*dz);
      }
    }
  
  return false;
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
  boolean calcPath = false;
  if(tx==x && ty==y && tz==z)//we're already pathing there, check to see how many nodes are left in the current path, recalc if getting low
    {
    Node n = path.getEndNode();
    if(n==null || (n.x!= (int)tx || n.y != (int)ty || n.z != (int)tz))//close, but goal node is not the current target
      {
      if(path.getPathNodeLength()<maxPathLength/4)
        {
//        Config.logDebug("same target, but node length is low, recalculating");
        calcPath = true;
        }
      }
    }
  else
    {
    float dist = Math.abs(Trig.getVelocity(tx-x, ty-y, tz-z));
    float targetDiff = Trig.getDistance(x, y, z, tx, ty, tz);
    if(dist > maxPathLength && targetDiff<dist*0.1f)//its so close to our current target, don't bother changing path
      {
//      Config.logDebug("far away target with little difference, skipping recalc");
      }
    else if(dist<maxPathLength && targetDiff<dist*0.1f && targetDiff>1.f)//else add new nodes from current goal to new goal, reseat current target 
      {
      if(!addedPreviously)
        {
        addedPreviously = true;
        Config.logDebug("adding to path");
        Node n = path.getEndNode();
        path.addPath(pather.findPath(worldAccess, n.x, n.y, n.z, tx, ty, tz, (int)maxPathLength));
        x = tx;
        y = ty;
        z = tz;
        }
      else
        {
//        Config.log("already added, recalc");
        calcPath = true;
        addedPreviously = false;
        }      
      
      }
    else//paths could vary wildy, probably best to recalculate....try and preserve some of old?
      {
//      Config.logDebug("new target, recalculating");
      calcPath = true;
      }
    }
  if(targetNode!=null)
    {
    if(Trig.getDistance(ex, ey, ez, targetNode.x, targetNode.y, targetNode.z)>3)//has fallen, been pushed, some other crap..recalc;
      {
      calcPath = true;
      }
    }
  if(calcPath)
    {
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
