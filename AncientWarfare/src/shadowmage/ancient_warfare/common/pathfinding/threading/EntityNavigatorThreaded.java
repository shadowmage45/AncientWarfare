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
package shadowmage.ancient_warfare.common.pathfinding.threading;

import java.util.List;

import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.pathfinding.EntityPath;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccessEntity;
import shadowmage.ancient_warfare.common.utils.Trig;

public class EntityNavigatorThreaded implements IPathableCallback
{

NpcBase entity;
EntityPath path = new EntityPath();
PathWorldAccessEntity worldAccess;

int targetX;
int targetY;
int targetZ;

int maxPathLength = 60;
int maxFastPathLength = 8;

Node targetNode = null;

public EntityNavigatorThreaded(NpcBase owner)
  {
  this.entity = owner;
  this.worldAccess = new PathWorldAccessEntity(entity.worldObj, entity);
  this.targetX = MathHelper.floor_double(entity.posX);
  this.targetY = MathHelper.floor_double(entity.posY);
  this.targetZ = MathHelper.floor_double(entity.posZ);
  }

/**
 * called frequently, every x ticks (5) from aiTasks
 * @param tx
 * @param ty
 * @param tz
 */
public void setMoveTo(int tx, int ty, int tz)
  {  
  if(false)
    {
    return;
    }  
  int ex = MathHelper.floor_double(entity.posX);
  int ey = MathHelper.floor_double(entity.posY);
  int ez = MathHelper.floor_double(entity.posZ);
  float dist = Math.abs(Trig.getVelocity(tx-this.targetX, ty-this.targetY, tz-this.targetZ));
  float targetDiff = dist;
  Node n = path.getEndNode();
  if(n!=null)
    {
    targetDiff = Trig.getDistance(n.x, n.y, n.z, tx, ty, tz);
    }  
  boolean calcPath = false;
  if(targetDiff < dist * 0.1f || (n!=null && n.x==tx && n.y==ty && n.z==tz))
    {
//    Config.logDebug("diff/dist ratio below threshold, skipping recalc");
    }
  else
    {
    calcPath = true;
    }
  if(calcPath)
    {   
    this.targetX = tx;
    this.targetY = ty;
    this.targetZ = tz;
    this.targetNode = null;
//    Config.logDebug("getting starter path");
    this.path.setPath(PathThreadPool.instance().findStarterPath(worldAccess, ex, ey, ez, tx, ty, tz, maxFastPathLength));
    this.targetNode = this.path.claimNode();
    Node b = this.path.getEndNode();
    if(b!=null && (b.x!=tx || b.y!=ty || b.z!=tz))
      {     
//      Config.logDebug("starter path was valid, requesting full path find");
      PathThreadPool.instance().requestPath(this, worldAccess, b.x, b.y, b.z, tx, ty, tz, maxPathLength);
      }
    } 
//  if(!entity.worldObj.isRemote)
//    {
//    NBTTagCompound tag = new NBTTagCompound();
//    tag.setInteger("tx", tx);
//    tag.setInteger("ty", ty);
//    tag.setInteger("tz", tz);
//    Packet04Npc pkt = new Packet04Npc();
//    pkt.setParams(entity);
//    pkt.setPathTarget(tag);
//    pkt.sendPacketToAllTrackingClients(entity);
//    }
  }

public void moveTowardsCurrentNode()
  {
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
    if(entity.isPathableEntityOnLadder())
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
  return path.getActivePath();
  }

@Override
public void onPathFound(List<Node> pathNodes)
  {
//  Config.logDebug("thread returned path");
  this.path.addPath(pathNodes);
//  Config.logDebug("added: "+pathNodes.size()+ " nodes from thread pathfind");
  }

@Override
public void onPathFailed(List<Node> partialPathNodes)
  {
  // TODO Auto-generated method stub
  
  }

}
