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
package shadowmage.ancient_warfare.common.civics.worksite.te.mine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class MineLevel implements INBTTaggable
{

private int minX;
private int minY;
private int minZ;
private int xSize;
private int ySize;
private int zSize;

private List<MineComponent> components = new ArrayList<MineComponent>();
private List<MineComponent> finishedComponents = new ArrayList<MineComponent>();
private MineComponent currentComponent = null;

private PriorityQueue<MinePoint> pointQueue = new PriorityQueue<MinePoint>();
private ArrayList<MinePoint> finishedPoints = new ArrayList<MinePoint>();

public int levelSize = 4;//the height of the level in blocks
/**
 * position is minX, minY, minZ of the structure boundinb box(world coords)
 * size is the absolute size in blocks of the structure
 * @param xPos 
 * @param yPos
 * @param zPos
 * @param xSize
 * @param ySize
 * @param zSize
 */
public MineLevel(int xPos, int yPos, int zPos, int xSize, int ySize, int zSize)
  {
  this.xSize = xSize;
  this.ySize = ySize;
  this.zSize = zSize;
//  this.mineArray = new MinePoint[xSize*ySize*zSize];
  this.minX = xPos;
  this.minY = yPos;
  this.minZ = zPos;
  Config.logDebug(String.format("creating mineLevel pos: %d,%d,%d  size:  %d, %d, %d", minX, minY, minZ, xSize, ySize, zSize));
  }

public MineLevel(NBTTagCompound tag)
  {
  this.readFromNBT(tag);
  }

public boolean hasWork()
  {
  return !this.pointQueue.isEmpty();
  }

public MinePoint getNextMinePoint()
  {
  return this.pointQueue.poll();
  }

/**
 * called by onFailed to return a node to work-queue
 * @param ent
 */
public void addMinePointEntry(MinePoint ent)
  {
  this.pointQueue.offer(ent);
  }

public void onPointFinished(MinePoint ent)
  {
  this.finishedPoints.add(ent);
  }

//public MinePoint getDataWorldIndex(int x, int y, int z)
//  {
//  return mineArray[getWorldAdjustedIndex(x,y,z)];
//  }
//
//protected MinePoint getDataLocalIndex(int x, int y, int z)
//  {
//  return this.mineArray[getIndex(x, y, z)];
//  }
//
//protected void setDataLocalIndex(int x, int y, int z, MinePoint data)
//  {
//  this.mineArray[getIndex(x, y, z)]= data;
//  }
//
//public void setDataWorldIndex(int x, int y, int z, MinePoint data)
//  {  
////	Config.logDebug(String.format("setting info: %d, %d, %d -- arraySize %d", x,y,z,this.mineArray.length));
//  this.mineArray[getWorldAdjustedIndex(x,y,z)]=data;
//  }

private int getWorldAdjustedIndex(int x, int y, int z)
  {
//	Config.logDebug(String.format("World coord: %d, %d, %d :: adj coord: %d, %d, %d", x,y,z, x-minX, y-minY, z-minZ));
  x-= minX;
  y-= minY;
  z-= minZ;  
  return (xSize*zSize*y)+(xSize*z)+x;
  }

private int getIndex(int x, int y, int z)
  {
  return (xSize*zSize*y)+(xSize*z)+x;
  }

/**
 * called to map out the nodes for this level
 * @param world
 */
public void initializeLevel(World world)
  { 
  long t1 = System.nanoTime();
  int shaftX = minX -1 + xSize/2;
  int shaftZ = minZ -1 + zSize/2;
  int order = 0;
  order = this.mapShaft(world, order, shaftX, shaftZ);
  order = this.mapTunnels(world, order, shaftX, shaftZ);
  order = this.mapBranches(world, order, shaftX, shaftZ);
  order = this.mapExtras(world, order, shaftX, shaftZ);
  long t2 = System.nanoTime();
  Config.logDebug("mine level init time nanos: "+(t2-t1));
  List<String> lines = getMineExportMap();
  for(String l : lines)
    {
    Config.logDebug(l);
    }
  //find center of work area
  //construct center shaft set to CLEAR_THEN_LADDER
  //construct main E/W tunnels set to CLEAR_TUNNEL
  //construct N/S branches set to CLEAR_BRANCH
  //scan the entire structure for resources or empty spots
  //  if resource next to an existing tunnel/shaft/brach position
  //    set to CLEAR_THEN_FILL
  //  if empty spot next to existing tunnel/shaft/branch
  //    set to FILL
  }

protected int mapShaft(World world, int order, int shaftX, int shaftZ)
  {  
  MinePoint entry;
  for(int y = minY + ySize-1; y >= minY; y--, order++)//start at the top...
    {
    for(int x = shaftX; x<=shaftX+1; x++)
      {
      for(int z = shaftZ; z<=shaftZ+1; z++)
        {
        if(world.getBlockId(x, y, z)==Block.ladder.blockID)
          {
          this.addPointToFinishedAndMap(x, y, z, order, TargetType.MINE_CLEAR_THEN_LADDER);
          }
        else
          {
          this.addPointToQueueAndMap(x, y, z, order, TargetType.MINE_CLEAR_THEN_LADDER);
          }
        }
      }    
    }
  return order;
  }

/**
 * BY WORLD COORDS
 * @param x
 * @param y
 * @param z
 * @param order
 * @param type
 */
protected void addPointToQueueAndMap(int x, int y, int z, int order, TargetType type)
  {
  MinePoint entry = new MinePoint(x,y,z, order, type);
//  this.setDataWorldIndex(x, y, z, entry);  
  this.pointQueue.offer(entry);
  }

protected void addPointToFinishedAndMap(int x, int y, int z, int order, TargetType type)
  {
  MinePoint entry = new MinePoint(x,y,z, order, type);
  entry.currentAction = TargetType.NONE;
//  this.setDataWorldIndex(x, y, z, entry);
  this.finishedPoints.add(entry);
  }

protected void addToMap(World world, int x, int y, int z, int order, TargetType type)
  {
  MinePoint entry = new MinePoint(x,y,z, order, type);
//  this.setDataWorldIndex(x, y, z, entry);
  if(!entry.hasWork(world))
    {
//    Config.logDebug("entry had no work: "+entry);
    entry.currentAction = TargetType.NONE;
    this.finishedPoints.add(entry);
    }
  else
    {
    this.pointQueue.offer(entry);
    }
  }

protected int mapTunnels(World world, int order, int shaftX, int shaftZ)
  {
//  int tunnelStartOrder = order;
  int id = 0;
  for(int x = shaftX-1; x>= minX ; x--, order++)//add west tunnel
    {
    for(int z = shaftZ; z<= shaftZ+1; z++)      
      {
      for(int y = minY+1; y>= minY; y--)
        {
        if(x%4==0 && y ==minY)
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_THEN_TORCH);
          }
        else
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_TUNNEL);
          }
        }
      }
    }
//  order = tunnelStartOrder;
  for(int x = shaftX+2; x <= minX + xSize-1; x++, order++)//add west tunnel
    {
    for(int z = shaftZ; z<= shaftZ+1; z++)      
      {
      for(int y = minY+1; y>= minY; y--)
        {
        if(x%4==0 && y ==minY)
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_THEN_TORCH);
          }
        else
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_TUNNEL);
          }
        }
      }
    }
  return order;
  }

protected int mapBranches(World world, int startOrder, int shaftX, int shaftZ)
  {
  int branchStartOrder = startOrder;
  int order = branchStartOrder;
  int id = 0;
  for(int x = shaftX-1; x>=minX; x-=3)
    {
    for(int z = shaftZ+2; z <=minZ+zSize-1; z++, order++)//do n/w side branches
      {
      for(int y = minY+1; y>= minY; y--)
        {
        if(z%4==0 && y ==minY)
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_THEN_TORCH);
          }
        else
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_BRANCH);
          }
        }
      }
    for(int z = shaftZ-1; z >=minZ; z--, order++)//do s/w side branches
      {
      for(int y = minY+1; y>= minY; y--)
        {
        if(z%4==0 && y ==minY)
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_THEN_TORCH);
          }
        else
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_BRANCH);
          }
        }
      }
    }  
  for(int x = shaftX+2; x <= minX+xSize-1; x+=3)
    {
    for(int z = shaftZ+2; z <=minZ+zSize-1; z++, order++)//do n/e side branches
      {
      for(int y = minY+1; y>= minY; y--)
        {
        if(z%4==0 && y ==minY)
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_THEN_TORCH);
          }
        else
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_BRANCH);
          }
        }
      }
    for(int z = shaftZ-1; z >=minZ; z--, order++)//do s/e side branches
      {
      for(int y = minY+1; y>= minY; y--)
        {
        if(z%4==0 && y == minY)
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_THEN_TORCH);
          }
        else
          {
          this.addToMap(world, x, y, z, order, TargetType.MINE_CLEAR_BRANCH);
          }
        }
      }
    }
  return order;
  }



protected int mapExtras(World world, int order, int shaftX, int shaftZ)
  {
//  int index;
//  int id;
//  boolean filled;
//  boolean resource;
//  MinePoint p;
//  for(int y = 0; y < this.ySize; y++ )
//    {
//    for(int z = 0; z < this.zSize; z++)
//      {
//      for(int x = 0; x < this.xSize; x++)
//        {
//        if(getDataLocalIndex(x, y, z)==null)
//          {
//          id = world.getBlockId(x+minX, y+minY, z+minZ);
//          filled = false;
//          resource = false;
//          if(needsFilled(id))
//            {
//            filled = true;
//            }
//          else if(isValidResource(id))
//            {
//            resource = true;
//            }
//          if(filled||resource)
//            {
//            order++;
//            for(BlockPosition o : blockOffsets)
//              {
//              index = getIndex(x+o.x, y+o.y, z+o.z);
//              if(index>=0 && index<this.mineArray.length && mineArray[index]!=null)
//                {
//                if(filled)
//                  {
//                  p = new MinePoint(x+minX,y+minY,z+minZ, order, TargetType.MINE_FILL);
//                  this.setDataLocalIndex(x, y, z, p);
//                  this.pointQueue.offer(p);
//                  //set to fill
//                  }
//                else if(resource)
//                  {
//                  p = new MinePoint(x+minX,y+minY,z+minZ, order, TargetType.MINE_CLEAR_THEN_FILL);
//                  this.setDataLocalIndex(x, y, z, p);
//                  this.pointQueue.offer(p);
//                  }
//                break;
//                }
//              } 
//            }          
//          }
//               
//        //if getdatalocal==null
//        //  if there is a non-null node in any of the 6 directions (and not out of mine bounds)
//        //    add as a clear_then_fill node
//        }
//      }
//    }
  return order;
  }

protected boolean needsFilled(int id)
  {
  return id==0 || id==Block.lavaMoving.blockID || id==Block.lavaStill.blockID || id==Block.waterMoving.blockID || id==Block.waterStill.blockID;
  }

protected boolean isValidResource(int id)
  {
  if(id==0 || id==Block.stone.blockID || id==Block.cobblestone.blockID || id== Block.bedrock.blockID || id== Block.dirt.blockID || id==Block.grass.blockID)
    {
    return false;
    }
  return true;
  }

protected static BlockPosition[] blockOffsets = new BlockPosition[6];

static
{
blockOffsets[0] = new BlockPosition(-1,0,0);
blockOffsets[1] = new BlockPosition(0,-1,0);
blockOffsets[2] = new BlockPosition(0,0,-1);
blockOffsets[3] = new BlockPosition(1,0,0);
blockOffsets[4] = new BlockPosition(0,1,0);
blockOffsets[5] = new BlockPosition(0,0,1);
}

public List<String> getMineExportMap()
  {
  List<String> map = new ArrayList<String>();
  return map;
  }

/**
 * called from TE to validate 'cleared' points list
 */
public void validatePoints(World world)
  {
  Iterator<MinePoint> it = this.finishedPoints.iterator();
  MinePoint p = null;
  while(it.hasNext())
    {
    p = it.next();
    if(p!=null && p.hasWork(world))
      {
      it.remove();
      this.pointQueue.offer(p);
      }
    }
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound outerTag = new NBTTagCompound();
  outerTag.setIntArray("bounds", new int[]{minX, minY, minZ, xSize, ySize, zSize});
  NBTTagList minePointList = new NBTTagList();
  MinePoint a;
//  for(int i = 0; i < this.mineArray.length; i++)
//    {
//    a = this.mineArray[i];
//    if(a!=null)
//      {
//      minePointList.appendTag(a.getNBTTag());
//      }
//    }
  outerTag.setTag("minePointList", minePointList);
  return outerTag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  int[] bounds = tag.getIntArray("bounds");
  this.minX = bounds[0];
  this.minY = bounds[1];
  this.minZ = bounds[2];
  this.xSize = bounds[3];
  this.ySize = bounds[4];
  this.zSize = bounds[5];
  NBTTagList minePointList = tag.getTagList("minePointList");
  NBTTagCompound pointTag = null;
  MinePoint point = null;
  for(int i = 0; i < minePointList.tagCount(); i++)
    {
    pointTag = (NBTTagCompound) minePointList.tagAt(i);
    point = new MinePoint(pointTag);
    if(point.currentAction==TargetType.NONE)
      {
      this.finishedPoints.add(point);
      }
    else
      {
      this.pointQueue.offer(point);
      }
    }
  }


}
