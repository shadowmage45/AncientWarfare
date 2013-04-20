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
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.Pos3f;
import shadowmage.ancient_warfare.common.utils.Trig;

public class PathUtils
{

public static List<Node> guidedCrawl(PathWorldAccess world, int ex, int ey, int ez, int tx, int ty, int tz, int numOfNodes, Random rng)
  {
  List<Node> nodes = new ArrayList<Node>();
  /**
   * get initial move direction towards target.
   * add start node to path
   * for i < nodes
   *   find next move direction from current move direction, based on prev. move and dir to target
   *   add move vector to current
   *   add new node
   */
  int cx = ex;
  int cy = ey;
  int cz = ez;
  int mx = tx - ex;
  int mz = tz - ez;
  mx = mx< 0 ? -1 : mx > 1? 1 : mx;
  mz = mz< 0 ? -1 : mz > 1? 1 : mz;
  int moveDir = 8;
  int my = 0;  
  int dx = mx;
  int dz = mz;
  int dy = my;
  nodes.add(new Node(cx,cy,cz));
  for(int i = 0; i < numOfNodes; i++)
    {
    if(tx<cy && world.isWalkable(cx, cy-1, cz))
      {
      cy--;
      nodes.add(new Node(cx,cy,cz));
      }
    else if( tx> cy && world.isWalkable(cx, cy+1, cz))
      {      
      cy++;
      nodes.add(new Node(cx,cy,cz));
      }
//    else if (mx!=0 && mz!=0 && world.isWalkable(cx+mx, cy+dy, cz+mz))
//      {
//      boolean add = false;
//      if(mx==1 && mz==1)
//        {
//        if(world.isWalkable(cx, cy, cz+1)&&world.isWalkable(cx+1, cy, cz))
//          {
//          add = true;
//          }
//        }
//      else if(mx==-1 && mz==1)
//        {
//        if(world.isWalkable(cx, cy, cz+1) && world.isWalkable(cx-1, cy, cz))
//          {
//          add = true;
//          }
//        }
//      else if(mx==1 && mz==-1)
//        {
//        if(world.isWalkable(cx+1, cy, cz) && world.isWalkable(cx, cy, cz-1))
//          {
//          add = true;
//          }
//        }
//      else if(mx==-1 && mz==-1)
//        {
//        if(world.isWalkable(cx-1, cy, cz) && world.isWalkable(cx, cy, cz-1))
//          {
//          add = true;
//          }
//        }
//      if(add)
//        {
//        dx = mx;
//        dz = mz;
//        cx+=dx;
//        cy+=dy;
//        cz+=dz;
//        nodes.add(new Node(cx, cy, cz));
//        }
//      }
    else if(world.isWalkable(cx+mx, cy+dy, cz+mz))
      {
      dx = mx;
      dz = mz;
      cx+=dx;
      cy+=dy;
      cz+=dz;
      nodes.add(new Node(cx, cy, cz));
//      Config.logDebug("adding forwards/target node"+" "+ nodes.get(nodes.size()-1).toString());      
      }
//    else if(dx!=0 && dz!=0 && world.isWalkable(cx+dx, cy+dy, cz+dz))
//      {
//      boolean add = false;
//      if(dx==1 && dz==1)
//        {
//        if(world.isWalkable(cx, cy, cz+1)&&world.isWalkable(cx+1, cy, cz))
//          {
//          add = true;
//          }
//        }
//      else if(dx==-1 && dz==1)
//        {
//        if(world.isWalkable(cx, cy, cz+1) && world.isWalkable(cx-1, cy, cz))
//          {
//          add = true;
//          }
//        }
//      else if(dx==1 && dz==-1)
//        {
//        if(world.isWalkable(cx+1, cy, cz) && world.isWalkable(cx, cy, cz-1))
//          {
//          add = true;
//          }
//        }
//      else if(dx==-1 && dz==-1)
//        {
//        if(world.isWalkable(cx-1, cy, cz) && world.isWalkable(cx, cy, cz-1))
//          {
//          add = true;
//          }
//        }
//      if(add)
//        {        
//        cx+=dx;
//        cy+=dy;
//        cz+=dz;
//        nodes.add(new Node(cx, cy, cz));
//        }
//      }
    else if(world.isWalkable(cx+dx, cy+dy, cz+dz))
      {
      cx+=dx;
      cy+=dy;
      cz+=dz;
      nodes.add(new Node(cx, cy, cz));
//      Config.logDebug("adding forwards/continue node"+" "+ nodes.get(nodes.size()-1).toString());      
      }
    else if(world.isWalkable(cx+dx, cy, cz+dz))
      {
      dy = 0;
      cx+=dx;
      cy+=dy;
      cz+=dz;
      nodes.add(new Node(cx, cy, cz));
//      Config.logDebug("adding forwards/level node"+" "+ nodes.get(nodes.size()-1).toString());      
      }
    else if(world.isWalkable(cx+dx, cy-1, cz+dz))
      {
      dy = -1;
      cx+=dx;
      cy+=dy;
      cz+=dz;
      nodes.add(new Node(cx, cy, cz));
//      Config.logDebug("adding forwards/down node"+" "+ nodes.get(nodes.size()-1).toString());      
      }
    else if(world.isWalkable(cx+dx, cy+1, cz+dz))
      {
      dy = 1;
      cx+=dx;
      cy+=dy;
      cz+=dz;
      nodes.add(new Node(cx, cy, cz));
//      Config.logDebug("adding forwards/up node"+" "+ nodes.get(nodes.size()-1).toString());      
      }
    else
      {
      int turn = getRotationTowardTarget(dx, dz, mx, mz);
      int offset[];
      if(turn<=0)
        {
        offset = getRotatedMoveDelta(dx, dz, -1);
        }
      else
        {
        offset = getRotatedMoveDelta(dx, dz, 1);
        }
      if(world.isWalkable(cx+offset[0], cy, cz+offset[1]))
        {
        dx = offset[0];
        dz = offset[1];
        dy = 0;
        cx+=dx;
        cz+=dz;
        cy+=dy;
        nodes.add(new Node(cx, cy, cz));
//        Config.logDebug("adding turn/level node. turn: "+turn +" "+ nodes.get(nodes.size()-1).toString());
        }
      else if(world.isWalkable(cx+offset[0], cy-1, cz+offset[1]))
        {
        dx = offset[0];
        dz = offset[1];
        dy = -1;
        cx+=dx;
        cz+=dz;
        cy+=dy;
        nodes.add(new Node(cx, cy, cz));
//        Config.logDebug("adding turn/down node. turn: "+turn+" "+ nodes.get(nodes.size()-1).toString());
        }
      else if(world.isWalkable(cx+offset[0], cy+1, cz+offset[1]))
        {
        dx = offset[0];
        dz = offset[1];
        dy = 1;
        cx+=dx;
        cz+=dz;
        cy+=dy;   
        nodes.add(new Node(cx, cy, cz));
//        Config.logDebug("adding turn/up node. turn: "+turn +" "+ nodes.get(nodes.size()-1).toString());
        }
      }    
    mx = tx - cx;
    mz = tz - cz;
    mx = mx< 0 ? -1 : mx > 1? 1 : mx;
    mz = mz< 0 ? -1 : mz > 1? 1 : mz;
    if(cx==tx && cy==ty && cz==tz)
      {
      Config.logDebug("crawl hit goal");
      break;
      }
    }  
  return nodes;
  }

private static int getRotationAmount(int amt, int base)
  {
  while(amt<0)
    {
    amt+=8;
    }
  return amt;
  }

private static int getRotationTowardTarget(int dx, int dz, int mx, int mz)
  {
  boolean foundCurrent = false;
  boolean foundBase = false;
  int currentOffset = 0;
  int baseOffset = 0;
  int offset[];
  for(int i = 0; i < offsets.length; i++)
    {
    offset = offsets[i];
    if(offset[0]==dx && offset[1]==dz)
      {
      currentOffset = i;
      foundCurrent = true;
      }
    if(offset[0]==mx && offset[1]==mz)
      {
      baseOffset = i;
      foundBase = true;
      }
    if(foundBase && foundCurrent)
      {
      break;
      }
    }
  return currentOffset-baseOffset;
  }

private static int[] getRotatedMoveDelta(int dx, int dz, int turnAmt)
  {
  while(turnAmt<0)
    {
    turnAmt+=8;
    }
  int rightTurns = turnAmt;
  int offsetNum = 0;
  int[] offset; 
  for(int i = 0; i < offsets.length; i++)
    {
    offset = offsets[i];
    if(offset[0]==dx && offset[1]==dz)
      {
      offsetNum = i;
      break;
      }
    }
  for(int i = 0; i < rightTurns; i++)
    {
    offsetNum++;
    if(offsetNum>=offsets.length)
      {
      offsetNum = 0;
      }
    }
  if(offsetNum<0)
    {
    offsetNum = 0;
    }
  else if(offsetNum>=offsets.length)
    {
    offsetNum = 0;
    }
  return offsets[offsetNum];
  }

private static int[][] offsets = new int[8][2];

static
{
offsets[0] = new int[]{0,1};
offsets[1] = new int[]{-1,1};
offsets[2] = new int[]{-1,0};
offsets[3] = new int[]{-1,-1};
offsets[4] = new int[]{0,-1};
offsets[5] = new int[]{1,-1};
offsets[6] = new int[]{1,0};
offsets[7] = new int[]{1,1};
}

public static List<Node> findRandomPath(PathWorldAccess world, int ex, int ey, int ez, int mx, int my, int maxNodes, Random rng)
  {
  List<Node> nodes = new ArrayList<Node>(); 
  int dx = rng.nextInt(3)-1;
  int dz = rng.nextInt(3)-1;
  int cx = ex;
  int cy = ey;
  int cz = ez;
  int tries = 0;
  while((dx==0 && dz==0) ||!world.isWalkable(cx+dx, cy, cz+dz) && tries<10)
    {
    dx = rng.nextInt(3)-1;
    dz = rng.nextInt(3)-1;
    tries++;
    }
  if(dx==0 && dz==0)
    {
    return nodes;
    }
  while(world.isWalkable(cx, cy, cz) && nodes.size()<maxNodes)
    { 
    nodes.add(new Node(cx, cy, cz));
    cx+=dx;
    cz+=dz;
    if(world.isWalkable(cx+dx, cy, cz+dz))
      {      
            
      }
    if(world.isWalkable(cx+dx, cy-1, cz+dz))
      {      
      cy--;      
      }
    else if(world.isWalkable(cx+dx, cy+1, cz+dz))
      {      
      cy++;
      }        
    }
  return nodes;
  } 

public static int[] findClosestValidBlockTo(PathWorldAccess world, int x, int y, int z, int sy)
  {
  if(world.isWalkable(x, y, z))
    {
    return new int[]{x,y,z};
    }
  int ty = findClosestYTo(world, x, y, z);
  if(ty>0)
    {
    return new int[]{x,ty,z};
    }
  ty = findClosestYTo(world, x-1, y, z);
  if(ty>0)
    {
    return new int[]{x-1,ty,z};
    }
  ty = findClosestYTo(world, x+1, y, z);
  if(ty>0)
    {
    return new int[]{x+1,ty,z};
    }
  ty = findClosestYTo(world, x, y, z-1);
  if(ty>0)
    {
    return new int[]{x,ty,z-1};
    }
  ty = findClosestYTo(world, x, y, z+1);
  if(ty>0)
    {
    return new int[]{x,ty,z+1};
    }
  return new int[]{x,y,z};
  }

public static int findClosestYTo(PathWorldAccess world, int x, int y, int z)
  {
  if(world.isWalkable(x, y, z))
    {
    return y;
    }
  else
    {
    for(int ty = y; ty < 255; ty++)
      {
      if(world.isWalkable(x, ty, z))
        {
        return ty;
        }
      }
    for(int ty = y; ty>0 ; ty--)
      {
      if(world.isWalkable(x, ty, z))
        {
        return ty;
        }
      }
    }
  return -1;
  }

//http://playtechs.blogspot.com/2007/03/raytracing-on-grid.html
//http://xnawiki.com/index.php/Voxel_traversal
//http://www.cse.yorku.ca/~amana/research/grid.pdf

/**
 * wewt...only took like....8 tries and a whole day of thinking...but custom written from the ground up.
 * returns all hits (sometimes+1) between vectors 0 and 1 (x0, x1, etc...).  finds exact position hit on the block side as it is crossing into that block (does not maintain side information)
 * @param x0
 * @param y0
 * @param z0
 * @param x1
 * @param y1
 * @param z1
 * @return
 */
public static List<Pos3f> traceRay2(float x0, float y0, float z0, float x1, float y1, float z1)
  {
  List<Pos3f> hits = new ArrayList<Pos3f>();
  
  float travel = 0;
  float distance = Trig.getDistance(x0, y0, z0, x1, y1, z1);  
  float mx = x1-x0;
  float my = y1-y0;
  float mz = z1-z0;  
  boolean invertX = x0<x1;
  boolean invertY = y0<y1;
  boolean invertZ = z0<z1;
  float dx;
  float dy;
  float dz;
  float px;
  float py;
  float pz;
  
  float pUse;
  
  float x = x0;
  float y = y0;
  float z = z0;
  hits.add(new Pos3f(x,y,z));
  while(travel<distance)
    {    
    dx = 1 - (x % 1.f);
    dy = 1 - (y % 1.f);
    dz = 1 - (z % 1.f);
//    if(invertX){dx = 1-dx;}
//    if(invertY){dy = 1-dy;}
//    if(invertZ){dz = 1-dz;}
    px = Math.abs(mx== 0 ? 1.f : dx / mx);
    py = Math.abs(my== 0 ? 1.f : dy / my);
    pz = Math.abs(mz== 0 ? 1.f : dz / mz);
    pUse = px < py ? px : py;
    pUse = pUse < pz ? pUse : pz;
    x += mx*pUse;
    y += my*pUse;
    z += mz*pUse;
    hits.add(new Pos3f(x,y,z));
//    Config.logDebug("hit: "+hits.get(hits.size()-1).toString());
    travel += distance * pUse;
    }  
  return hits;
  }

public static List<Pos3f> traceRay(float x0, float y0, float z0, float x1, float y1, float z1)
  {//http://xnawiki.com/index.php/Voxel_traversal
  
  /**
   * ??? on the next 5 lines..
   */
  int maxDepth = 60;
  float len = Trig.getDistance(x0, y0, z0, x1, y1, z1);
  Vec3 tmp = Vec3.createVectorHelper(x1, y1, z1);
  tmp =  tmp.normalize();
  x1 = (float) tmp.xCoord;
  y1 = (float) tmp.yCoord;
  z1 = (float) tmp.zCoord;
  
  List<Pos3f> hitPositions = new ArrayList<Pos3f>();
  int x = MathHelper.floor_float(x0);
  int y = MathHelper.floor_float(y0);
  int z = MathHelper.floor_float(z0);
  int stepX = x1 ==0 ? 0 : x1> 0 ? 1 : -1;
  int stepY = y1 ==0 ? 0 : y1> 0 ? 1 : -1;
  int stepZ = z1 ==0 ? 0 : z1> 0 ? 1 : -1;
  Node cellBoundary = new Node(stepX>0? 1 : 0, stepY>0 ? 1 : 0, stepZ>0? 1: 0);
  float tMaxX = (cellBoundary.x - x0) / x1;
  float tMaxY = (cellBoundary.y - y0) / y1;
  float tMaxZ = (cellBoundary.z - z0) / z1;
  if(Float.isNaN(tMaxX)){tMaxX = Float.POSITIVE_INFINITY;}
  if(Float.isNaN(tMaxY)){tMaxY = Float.POSITIVE_INFINITY;}
  if(Float.isNaN(tMaxZ)){tMaxZ = Float.POSITIVE_INFINITY;}
  
  float tDeltaX = stepX / x1;
  float tDeltaY = stepY / y1;
  float tDeltaZ = stepZ / z1;
  if(Float.isNaN(tDeltaX)){tDeltaX = Float.POSITIVE_INFINITY;}
  if(Float.isNaN(tDeltaY)){tDeltaY = Float.POSITIVE_INFINITY;}
  if(Float.isNaN(tDeltaZ)){tDeltaZ = Float.POSITIVE_INFINITY;}
  
  for(int i = 0; i < maxDepth; i++)
    {    
    hitPositions.add(new Pos3f(x,y,z));
//    Config.logDebug("block: "+x+","+y+","+z);
    if(Trig.getDistance(x0, y0, z0, x, y, z)>=len)
      {
      break;//
      }
    if(tMaxX < tMaxY && tMaxX <tMaxZ)
      {
      x+=stepX;
      tMaxX += tDeltaX;
      }
    else if(tMaxY < tMaxZ)
      {
      y += stepY;
      tMaxY += tDeltaY;
      }
    else
      {
      z += stepZ;
      tMaxZ += tDeltaZ;
      }
    }  
  return hitPositions;
  }


public static List<BlockPosition> getPositionsBetween2(float x0, float z0, float x1, float z1)
  {
  
  //http://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
  /**
   * function line(x0, y0, x1, y1)
   dx := abs(x1-x0)
   dy := abs(y1-y0) 
   if x0 < x1 then sx := 1 else sx := -1
   if y0 < y1 then sy := 1 else sy := -1
   err := dx-dy
 
   loop
     setPixel(x0,y0)
     if x0 = x1 and y0 = y1 exit loop
     e2 := 2*err
     if e2 > -dy then 
       err := err - dy
       x0 := x0 + sx
     end if
     if e2 <  dx then 
       err := err + dx
       y0 := y0 + sy 
     end if
   end loop
   */
  List<BlockPosition> blocks = new ArrayList<BlockPosition>();
  float dx = (int) Math.abs(x1-x0);
  float dz = (int) Math.abs(z1-z0);
  float sx = x0< x1 ? 1 : -1;
  float sz = z0< z1 ? 1 : -1;
  float err = dx-dz;
  float e2;
  for(int i = 0; i < dx+dz; i++)
    {
    blocks.add(new BlockPosition(x0,0,z0));
//    Config.logDebug("hit: "+x0+","+z0);
    if(x0==x1 && z0==z1)
      {
      break;//finished
      }
    e2 = 2*err;
    if(e2>-dz)
      {
      err = err-dz;
      x0 = x0 +sx;
      }
    if(e2<dx)
      {
      err = err +dx;
      z0 = z0+sz;
      }
    }  
  //Config.logDebug("hit: "+x0+","+z0);
  return blocks;
  }

public static List<BlockPosition> getPositionsBetween(float x0, float y0, float z0, float x1, float y1, float z1)
  {//4-connected line alg...from..somewhere online (stack overflow post)
/**
 * void drawLine(int x0, int y0, int x1, int y1) {
    int dx = abs(x1 - x0);
    int dy = abs(y1 - y0);
    int sgnX = x0 < x1 ? 1 : -1;
    int sgnY = y0 < y1 ? 1 : -1;
    int e = 0;
    for (int i=0; i < dx+dy; i++) {
        drawPixel(x0, y0);
        int e1 = e + dy;
        int e2 = e - dx;
        if (abs(e1) < abs(e2)) {
            x0 += sgnX;
            e = e1;
        } else {
            y0 += sgnY;
            e = e2;
        }
    }
}
 */
  List<BlockPosition> blocks = new ArrayList<BlockPosition>();
  int dx = (int) Math.abs(x1-x0);
  int dz = (int) Math.abs(z1-z0);
  int sx = x0< x1 ? 1 : -1;
  int sz = z0< z1 ? 1 : -1;
  int e = 0;
  int e1;
  int e2;
  for(int i = 0; i < dx+dz; i++)
    {
    blocks.add(new BlockPosition(x0,y0,z0));
//    Config.logDebug("hit: "+x0+","+y0+","+z0);
    e1 = e+dz;
    e2 = e-dx;
    if(Math.abs(e1)<Math.abs(e2))
      {
      x0 += sx;
      e = e1;
      }
    else
      {
      z0 += sz;
      e = e2;
      }
    }  
  blocks.add(new BlockPosition(x0,y0,z0));
//  Config.logDebug("hit: "+x0+","+y0+","+z0);
  return blocks;
  }


}
