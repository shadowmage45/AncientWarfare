/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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

import java.util.PriorityQueue;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class MineLevelTemplated extends MineLevel
{

protected PriorityQueue<MinePointPlaced> queue = new PriorityQueue<MinePointPlaced>();
protected MineTemplate template;
protected boolean hasTorches = false;
protected boolean hasFiller = false;
protected boolean hasLadders = false;

/**
 * @param xPos
 * @param yPos
 * @param zPos
 * @param xSize
 * @param ySize
 * @param zSize
 */
public MineLevelTemplated(int xPos, int yPos, int zPos, int xSize, int ySize, int zSize)
  {
  super(xPos, yPos, zPos, xSize, ySize, zSize);
  }

@Override
protected void scanLevel(TEMine mine, World world)
  {
  this.hasFiller = mine.hasFiller();
  this.hasTorches = mine.hasTorch();
  this.hasLadders = mine.inventory.containsAtLeast(mine.ladderFilter, 1);
  /**
   * scan area, x,y,z.
   * translate world x,y,z into template x,y,z
   *  templateX = 
   */
  int xDiff = template.xSize-this.xSize;
  int zDiff = template.zSize-this.zSize;  
  MineActionPoint a;
  TargetType actualAction;
  int wx;//world coordinates
  int wy;
  int wz;
  int tx;
  int ty;
  int tz;
  for(int x = 0; x< this.xSize; x++)
    {
    wx = this.minX + x;
    tx = x + (xDiff/2);
    for(int y = 0; y < this.ySize; y++)
      {
      wy = this.minY + y;
      ty = y;
      for(int z = 0; z< this.zSize; z++)
        {
        wz = this.minZ + z;
        tz = z +(zDiff/2);
        a = template.getAction(tx, ty, tz);
        actualAction = getAdjustedAction(a, mine, world);
        if(actualAction!=TargetType.NONE)
          {
          this.queue.offer(new MinePointPlaced(wx, wy, wz, (byte)a.meta, actualAction, a.order));
          }
        }
      }
    }
  while(!queue.isEmpty())
    {
    this.workList.add(queue.poll());    
    }
  }

protected TargetType getAdjustedAction(MineActionPoint a, TEMine m, World world)
  {
  return TargetType.NONE;
  }

protected class MinePointPlaced extends WorkPoint implements Comparable<MinePointPlaced>
{
int order;
/**
 * @param x
 * @param y
 * @param z
 * @param special
 * @param t
 */
public MinePointPlaced(int x, int y, int z, byte special, TargetType t, int order)
  {
  super(x, y, z, special, t);
  this.order = order;
  }

@Override
public int compareTo(MinePointPlaced arg0)
  {
  if(arg0!=null)
    {
    if(this.order<arg0.order)
      {
      return -1;
      }
    else if(this.order>arg0.order)
      {
      return 1;
      }
    }
  return 0;
  }

}
}
