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
package shadowmage.ancient_warfare.common.pathfinding.navigator;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IEntityNavigator;
import shadowmage.ancient_warfare.common.interfaces.IPathableEntity;
import shadowmage.ancient_warfare.common.network.Packet04Npc;
import shadowmage.ancient_warfare.common.pathfinding.EntityPath;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathManager;
import shadowmage.ancient_warfare.common.pathfinding.PathUtils;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccess;
import shadowmage.ancient_warfare.common.pathfinding.threading.IPathableCallback;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.Pos3f;

public class Navigator implements IEntityNavigator, IPathableCallback
{

protected IPathableEntity owner;
protected Entity entity;
protected PathWorldAccess world;
protected EntityPath path;
protected final Node finalTarget = new Node(0,0,0);
protected Node currentTarget;
protected Random rng = new Random();
protected boolean hasDoor = false;
protected BlockPosition doorPos = new BlockPosition(0,0,0);
protected int doorOpenTicks = 0;
protected int doorCheckTicks = 0;
protected int doorOpenMax = 15;
protected int doorCheckTicksMax = 5;
protected final Pos3f stuckCheckPosition = new Pos3f(0,0,0);
protected int stuckCheckTicks = 40;
protected final int stuckCheckTicksMax = 40;

/**
 * TODO stuck timing/detection
 * TODO fallen detection
 * TODO fix when path is set, goes backwards
 */
public Navigator(IPathableEntity owner)
  {
  this.owner = owner;
  this.entity = owner.getEntity();
  this.world = owner.getWorldAccess();
  this.path = new EntityPath();
  finalTarget.reassign(MathHelper.floor_double(entity.posX),MathHelper.floor_double(entity.posY),MathHelper.floor_double(entity.posZ));
  this.stuckCheckPosition.setup(entity.posX, entity.posY, entity.posZ);
  }

@Override
public void setMoveToTarget(int x, int y, int z)
  {
  this.sendToClients(x, y, z);    
  int ex = MathHelper.floor_double(entity.posX);
  int ey = MathHelper.floor_double(entity.posY);
  int ez = MathHelper.floor_double(entity.posZ);
  ey = PathUtils.findClosestYTo(world, ex, ey, ez);
  y = PathUtils.findClosestYTo(world, x, y, z);
  if(this.shouldCalculatePath(ex, ey, ez, x, y, z))
    {
    this.finalTarget.reassign(x, y, z);
    this.calculatePath(ex, ey, ez, x, y, z);
    }
  }

@Override
public void onMovementUpdate()
  {  
//  Config.logDebug("onMovementUpdate");
  this.updateMoveHelper();
  this.detectStuck();
  this.claimNode();
  if(this.currentTarget!=null)
    {
    if(owner.isPathableEntityOnLadder())
      {
      if(currentTarget.y<(int)entity.posY)
        {
        entity.motionY = -0.125f;
        entity.motionX *= 0.5f;
        entity.motionZ *= 0.5f;
        }
      else if(currentTarget.y>(int)entity.posY)
        {
        entity.motionY = 0.125f;
        entity.motionX *= 0.5f;
        entity.motionZ *= 0.5f;
        }    
      }
    if(this.world.canOpenDoors)
      {
      this.doorInteraction(); 
      }
    float speed = getEntityDistance(currentTarget)<0.5f? owner.getDefaultMoveSpeed() *0.5f : owner.getDefaultMoveSpeed();
    owner.setMoveTo(currentTarget.x+0.5d, currentTarget.y, currentTarget.z+0.5d, speed);
    }
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

protected void detectStuck()
  {
  if(this.stuckCheckTicks<=0)
    {
    this.stuckCheckTicks = this.stuckCheckTicksMax;
    if(entity.getDistance(stuckCheckPosition.x, stuckCheckPosition.y, stuckCheckPosition.z)<1.5d)
      {
      Config.logDebug("detecting stuck, clearing path");
      this.clearPath();
      this.currentTarget = null;      
      }
    this.stuckCheckPosition.setup(entity.posX, entity.posY, entity.posZ);
    }
  else
    {
    this.stuckCheckTicks--;
    }
  }

protected boolean isNewTarget(int tx, int ty, int tz)
  {
  return !this.finalTarget.equals(tx, ty, tz);
  }

protected boolean isAtTarget(int x, int y, int z)
  {
  return entity.getDistance(x+0.5d, y, z+0.5d) < entity.width;
  }

protected boolean isPathEmpty()
  {
  return this.path.getActivePathSize()<=0;
  }

protected boolean shouldCalculatePath(int ex, int ey, int ez, int tx, int ty, int tz)
  {
  return isNewTarget(tx, ty, tz) || (isPathEmpty() && !isAtTarget(tx, ty, tz) && currentTarget==null);
  }

protected void calculatePath(int ex, int ey, int ez, int tx, int ty, int tz)
  {
  Config.logDebug("requesting starter path");
  this.path.setPath(PathUtils.guidedCrawl(world, ex, ey, ez, tx, ty, tz, 60, rng));    
  Node end = this.path.getEndNode();
  if(end!=null && (end.x!=tx || end.y!=ty || end.z!=tz))
    {
    Config.logDebug("requesting full path");
    this.path.addPath(PathManager.instance().findImmediatePath(world, end.x, end.y, end.z, tx, ty, tz));
    }  
  this.stuckCheckTicks = this.stuckCheckTicksMax;
  this.stuckCheckPosition.setup(entity.posX, entity.posY, entity.posZ);
  Node start = this.path.getFirstNode();
  if(start!=null && getEntityDistance(start)<0.8f && start.y==ey)
    {
    this.path.claimNode();
    //skip start node, go directly to second
    }
  this.claimNode();
  }

protected void doorInteraction()
  {
  int ex = MathHelper.floor_double(entity.posX);
  int ey = MathHelper.floor_double(entity.posY);
  int ez = MathHelper.floor_double(entity.posZ);  
  if(this.doorCheckTicks<=0)
    {
    this.doorCheckTicks = this.doorCheckTicksMax;
    if(this.entity.isCollidedHorizontally && checkForDoors(ex, ey, ez))
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

protected boolean checkForDoors(int ex, int ey, int ez)
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

protected void claimNode()
  {
  while((this.currentTarget==null && !isPathEmpty()) || getEntityDistance(currentTarget)<entity.width)
    {
    this.currentTarget = this.path.claimNode();
//    Config.logDebug("claimed node "+this.currentTarget+ " left in path: "+path.getActivePathSize());
    if(this.currentTarget==null)
      {
      break;
      }
    } 
  }

protected float getEntityDistance(Node n)
  {
  return entity == null? 0.f : n==null? 0.f : (float) entity.getDistance(n.x+0.5d, n.y, n.z+0.5d);
  }

protected void sendToClients(int x, int y, int z)  
  {
  if(!world.isRemote())//relay to client, force client-side to find path as well (debug rendering of path)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("tx", x);
    tag.setInteger("ty", y);
    tag.setInteger("tz", z);
    Packet04Npc pkt = new Packet04Npc();
    pkt.setParams(entity);
    pkt.setPathTarget(tag);
    pkt.sendPacketToAllTrackingClients(entity);
    }
  }

@Override
public void setCanSwim(boolean swim)
  {
  if(this.world!=null)
    {
    this.world.canSwim = swim;
    }
  }

@Override
public void setCanOpenDoors(boolean doors)
  {
  if(this.world!=null)
    {
    this.world.canOpenDoors = true;
    }
  }

@Override
public void setCanUseLadders(boolean ladders)
  {
  if(this.world!=null)
    {
    this.world.canUseLaders = ladders;
    }
  }

@Override
public void onPathFound(List<Node> pathNodes)
  {
  this.path.addPath(pathNodes);
  }

@Override
public void clearPath()
  {
  this.path.clearPath();
  this.currentTarget = null;
  }

@Override
public void forcePath(List<Node> n)
  {
  this.path.setPath(n);
  this.claimNode();
  }

@Override
public List<Node> getCurrentPath()
  {
  return path.getFullPath();
  }

}
