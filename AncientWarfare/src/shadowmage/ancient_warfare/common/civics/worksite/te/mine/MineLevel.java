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
import java.util.List;
import java.util.PriorityQueue;

import net.minecraft.world.World;

public class MineLevel
{

private final int minX;
private final int minY;
private final int minZ;
private final int xSize;
private final int ySize;
private final int zSize;

private int shaftX;
private int shaftZ;

private final MinePointEntry[] mineArray;
private PriorityQueue<MinePointEntry> pointQueue = new PriorityQueue<MinePointEntry>();

/**
 * position is minX, minY, minZ of the structure boundinb box
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
  this.mineArray = new MinePointEntry[xSize*ySize*zSize];
  this.minX = xPos;
  this.minY = yPos;
  this.minZ = zPos;
  }

public MinePointEntry getDataWorldIndex(int x, int y, int z)
  {
//  int yIndex = xSize * zSize * y;
//  int zIndex = xSize * z;
//  int xIndex = x;
  return mineArray[getWorldAdjustedIndex(x,y,z)];
  }

protected MinePointEntry getDataLocalIndex(int x, int y, int z)
  {
  return this.mineArray[getIndex(x, y, z)];
  }

protected void setDataLocalIndex(int x, int y, int z, MinePointEntry data)
  {
  this.mineArray[getIndex(x, y, z)]= data;
  }

public void setDataWorldIndex(int x, int y, int z, MinePointEntry data)
  {  
  this.mineArray[getWorldAdjustedIndex(x,y,z)]=data;
  }

private int getWorldAdjustedIndex(int x, int y, int z)
  {
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
public void initializeLevel(World world, int shaftX, int shaftZ)
  { 
//  int shaftX = minX -1 + xSize/2;
//  int shaftZ = minZ -1 + zSize/2;
  int order = 0;
  order = this.mapShaft(world, order, shaftX, shaftZ);
  order = this.mapTunnels(world, order, shaftX, shaftZ);
  order = this.mapBranches(world, order, shaftX, shaftZ);
  order = this.mapExtras(world, order, shaftX, shaftZ);
  
  
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
  MinePointEntry entry;
  for(int y = minY + ySize-1; y >= minY; y--, order++)//start at the top...
    {
    for(int x = shaftX; x<=shaftX+1; x++)
      {
      for(int z = shaftZ; z<=shaftZ+1; z++)
        {
        this.addPointToQueueAndMap(x, y, z, order, MineActionType.CLEAR_THEN_LADDER);
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
protected void addPointToQueueAndMap(int x, int y, int z, int order, MineActionType type)
  {
  MinePointEntry entry = new MinePointEntry(x,y,z, order, type);
  this.setDataWorldIndex(x, y, z, entry);
  this.pointQueue.offer(entry);
  }

protected int mapTunnels(World world, int order, int shaftX, int shaftZ)
  {
  int tunnelStartOrder = order;
  for(int x = shaftX-1; x>= minX ; x--, order++)//add west tunnel
    {
    for(int z = shaftZ; z<= shaftZ+1; z++)      
      {
      for(int y = minY+2; y>= minY+1; y--)
        {
        this.addPointToQueueAndMap(x, y, z, order, MineActionType.CLEAR_TUNNEL);
        }
      }
    }
  order = tunnelStartOrder;
  for(int x = shaftX+2; x <= minX + xSize-1; x++, order++)//add west tunnel
    {
    for(int z = shaftZ; z<= shaftZ+1; z++)      
      {
      for(int y = minY+2; y>= minY+1; y--)
        {
        this.addPointToQueueAndMap(x, y, z, order, MineActionType.CLEAR_TUNNEL);
        }
      }
    }
  return order;
  }

protected int mapBranches(World world, int startOrder, int shaftX, int shaftZ)
  {
  int branchStartOrder = startOrder;
  int order = branchStartOrder;
  for(int x = shaftX-1; x>=minX; x-=3)
    {
    order = branchStartOrder;
    for(int z = shaftZ+2; z <=minZ+zSize-1; z++, order++)//do n/w side branches
      {
      for(int y = minY+2; y>= minY+1; y--)
        {
        this.addPointToQueueAndMap(x, y, z, order, MineActionType.CLEAR_BRANCH);
        }
      }
    order = branchStartOrder;
    for(int z = shaftZ-1; z >=minZ; z--, order++)//do s/w side branches
      {
      for(int y = minY+2; y>= minY+1; y--)
        {
        this.addPointToQueueAndMap(x, y, z, order, MineActionType.CLEAR_BRANCH);
        }
      }
    }
  for(int x = shaftX+2; x <= minX+xSize-1; x+=3)
    {
    order = branchStartOrder;
    for(int z = shaftZ+2; z <=minZ+zSize-1; z++, order++)//do n/e side branches
      {
      for(int y = minY+2; y>= minY+1; y--)
        {
        this.addPointToQueueAndMap(x, y, z, order, MineActionType.CLEAR_BRANCH);
        }
      }
    order = branchStartOrder;
    for(int z = shaftZ-1; z >=minZ; z--, order++)//do s/e side branches
      {
      for(int y = minY+2; y>= minY+1; y--)
        {
        this.addPointToQueueAndMap(x, y, z, order, MineActionType.CLEAR_BRANCH);
        }
      }
    }
  return order;
  }

protected int mapExtras(World world, int startOrder, int shaftX, int shaftZ)
  {
  return startOrder;
  }

public void getNextWorkPoint(World world)
  {
  MinePointEntry entry = this.pointQueue.poll();
  entry.worked = true;
  }

public List<String> getMineExportMap()
  {
  List<String> map = new ArrayList<String>();
  String currentLine;
  MinePointEntry entry;
  for(int y = this.ySize-1; y >=0; y--)
    {
    map.add("Level: "+y);    
    for(int z = 0; z < this.zSize; z++)
      {
      currentLine = "";
      for(int x = 0; x < this.xSize; x++)
        {        
        if(x>0)
          {
          currentLine = currentLine + ",";
          }
        entry = getDataLocalIndex(x, y, z);
        if(entry==null)
          {
          currentLine = currentLine + "XX";
          }
        else if(entry.order<10)
          {
          currentLine = currentLine + "0" + String.valueOf(entry.order);
          }
        else
          {
          currentLine = currentLine + String.valueOf(entry.order);
          }
        }
      map.add(currentLine);
      }    
    }
  return map;
  }

private class MinePointEntry implements Comparable<MinePointEntry>
{
int x;
int y;
int z;
int order = 0;
boolean worked = false;
MineActionType action = MineActionType.NONE;//original action as designated when scanned
MineActionType currentAction = MineActionType.NONE;//current action needed, as designated by worked on/rescanned

protected MinePointEntry(int x, int y, int z, int order, MineActionType type)
  {
  this.order = order;
  this.action = type;
  this.currentAction = type;
  }

@Override
public int compareTo(MinePointEntry o)
  {
  if(o==null)
    {
    return -1;
    }
  if(order<o.order)
    {
    return -1;
    }
  if(order>o.order)
    {
    return 1;
    }
  return 0;
  }

@Override
public int hashCode()
  {
  final int prime = 31;
  int result = 1;
  result = prime * result + getOuterType().hashCode();
  result = prime * result + order;
  result = prime * result + x;
  result = prime * result + y;
  result = prime * result + z;
  return result;
  }

@Override
public boolean equals(Object obj)
  {
  if (this == obj)
    return true;
  if (obj == null)
    return false;
  if (!(obj instanceof MinePointEntry))
    return false;
  MinePointEntry other = (MinePointEntry) obj;
  if (!getOuterType().equals(other.getOuterType()))
    return false;
  if (order != other.order)
    return false;
  if (x != other.x)
    return false;
  if (y != other.y)
    return false;
  if (z != other.z)
    return false;
  return true;
  }

private MineLevel getOuterType()
  {
  return MineLevel.this;
  }

}

public enum MineActionType
{
NONE,
CLEAR_TUNNEL,//for e/w tunnels
CLEAR_BRANCH,//for n/s branches from tunnels
CLEAR_THEN_LADDER,//for the central vertical shaft
CLEAR_THEN_FILL,//for resources in the wall/roof/floor
FILL;//for holes in the wall/roof/floor
}


}
