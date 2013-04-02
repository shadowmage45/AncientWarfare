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
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.Pos3f;
import shadowmage.ancient_warfare.common.utils.Trig;

public class PathUtils
{
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
    px = Math.abs(mx== 0 ? 1.f : dx / mx);
    py = Math.abs(my== 0 ? 1.f : dy / my);
    pz = Math.abs(mz== 0 ? 1.f : dz / mz);
    pUse = px < py ? px : py;
    pUse = pUse < pz ? pUse : pz;
    x += mx*pUse;
    y += my*pUse;
    z += mz*pUse;
    hits.add(new Pos3f(x,y,z));
    Config.logDebug("hit: "+hits.get(hits.size()-1).toString());
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
    Config.logDebug("block: "+x+","+y+","+z);
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

public static void mcRayTraceRehash()
  {
  /**
   * if (!Double.isNaN(par1Vec3.xCoord) && !Double.isNaN(par1Vec3.yCoord) && !Double.isNaN(par1Vec3.zCoord))
        {
            if (!Double.isNaN(par2Vec3.xCoord) && !Double.isNaN(par2Vec3.yCoord) && !Double.isNaN(par2Vec3.zCoord))
            {
                int var5 = MathHelper.floor_double(par2Vec3.xCoord);
                int var6 = MathHelper.floor_double(par2Vec3.yCoord);
                int var7 = MathHelper.floor_double(par2Vec3.zCoord);
                int var8 = MathHelper.floor_double(par1Vec3.xCoord);
                int var9 = MathHelper.floor_double(par1Vec3.yCoord);
                int var10 = MathHelper.floor_double(par1Vec3.zCoord);
                int var11 = this.getBlockId(var8, var9, var10);
                int var12 = this.getBlockMetadata(var8, var9, var10);
                Block var13 = Block.blocksList[var11];

                if (var13 != null && (!par4 || var13 == null || var13.getCollisionBoundingBoxFromPool(this, var8, var9, var10) != null) && var11 > 0 && var13.canCollideCheck(var12, par3))
                {
                    MovingObjectPosition var14 = var13.collisionRayTrace(this, var8, var9, var10, par1Vec3, par2Vec3);

                    if (var14 != null)
                    {
                        return var14;
                    }
                }

                var11 = 200;

                while (var11-- >= 0)
                {
                    if (Double.isNaN(par1Vec3.xCoord) || Double.isNaN(par1Vec3.yCoord) || Double.isNaN(par1Vec3.zCoord))
                    {
                        return null;
                    }

                    if (var8 == var5 && var9 == var6 && var10 == var7)
                    {
                        return null;
                    }

                    boolean var39 = true;
                    boolean var40 = true;
                    boolean var41 = true;
                    double var15 = 999.0D;
                    double var17 = 999.0D;
                    double var19 = 999.0D;

                    if (var5 > var8)
                    {
                        var15 = (double)var8 + 1.0D;
                    }
                    else if (var5 < var8)
                    {
                        var15 = (double)var8 + 0.0D;
                    }
                    else
                    {
                        var39 = false;
                    }

                    if (var6 > var9)
                    {
                        var17 = (double)var9 + 1.0D;
                    }
                    else if (var6 < var9)
                    {
                        var17 = (double)var9 + 0.0D;
                    }
                    else
                    {
                        var40 = false;
                    }

                    if (var7 > var10)
                    {
                        var19 = (double)var10 + 1.0D;
                    }
                    else if (var7 < var10)
                    {
                        var19 = (double)var10 + 0.0D;
                    }
                    else
                    {
                        var41 = false;
                    }

                    double var21 = 999.0D;
                    double var23 = 999.0D;
                    double var25 = 999.0D;
                    double var27 = par2Vec3.xCoord - par1Vec3.xCoord;
                    double var29 = par2Vec3.yCoord - par1Vec3.yCoord;
                    double var31 = par2Vec3.zCoord - par1Vec3.zCoord;

                    if (var39)
                    {
                        var21 = (var15 - par1Vec3.xCoord) / var27;
                    }

                    if (var40)
                    {
                        var23 = (var17 - par1Vec3.yCoord) / var29;
                    }

                    if (var41)
                    {
                        var25 = (var19 - par1Vec3.zCoord) / var31;
                    }

                    boolean var33 = false;
                    byte var42;

                    if (var21 < var23 && var21 < var25)
                    {
                        if (var5 > var8)
                        {
                            var42 = 4;
                        }
                        else
                        {
                            var42 = 5;
                        }

                        par1Vec3.xCoord = var15;
                        par1Vec3.yCoord += var29 * var21;
                        par1Vec3.zCoord += var31 * var21;
                    }
                    else if (var23 < var25)
                    {
                        if (var6 > var9)
                        {
                            var42 = 0;
                        }
                        else
                        {
                            var42 = 1;
                        }

                        par1Vec3.xCoord += var27 * var23;
                        par1Vec3.yCoord = var17;
                        par1Vec3.zCoord += var31 * var23;
                    }
                    else
                    {
                        if (var7 > var10)
                        {
                            var42 = 2;
                        }
                        else
                        {
                            var42 = 3;
                        }

                        par1Vec3.xCoord += var27 * var25;
                        par1Vec3.yCoord += var29 * var25;
                        par1Vec3.zCoord = var19;
                    }

                    Vec3 var34 = this.getWorldVec3Pool().getVecFromPool(par1Vec3.xCoord, par1Vec3.yCoord, par1Vec3.zCoord);
                    var8 = (int)(var34.xCoord = (double)MathHelper.floor_double(par1Vec3.xCoord));

                    if (var42 == 5)
                    {
                        --var8;
                        ++var34.xCoord;
                    }

                    var9 = (int)(var34.yCoord = (double)MathHelper.floor_double(par1Vec3.yCoord));

                    if (var42 == 1)
                    {
                        --var9;
                        ++var34.yCoord;
                    }

                    var10 = (int)(var34.zCoord = (double)MathHelper.floor_double(par1Vec3.zCoord));

                    if (var42 == 3)
                    {
                        --var10;
                        ++var34.zCoord;
                    }

                    int var35 = this.getBlockId(var8, var9, var10);
                    int var36 = this.getBlockMetadata(var8, var9, var10);
                    Block var37 = Block.blocksList[var35];

                    if ((!par4 || var37 == null || var37.getCollisionBoundingBoxFromPool(this, var8, var9, var10) != null) && var35 > 0 && var37.canCollideCheck(var36, par3))
                    {
                        MovingObjectPosition var38 = var37.collisionRayTrace(this, var8, var9, var10, par1Vec3, par2Vec3);

                        if (var38 != null)
                        {
                            return var38;
                        }
                    }
                }

                return null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
   */
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
    Config.logDebug("hit: "+x0+","+z0);
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
    Config.logDebug("hit: "+x0+","+y0+","+z0);
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
  Config.logDebug("hit: "+x0+","+y0+","+z0);
  return blocks;
  }


}
