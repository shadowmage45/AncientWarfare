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

import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public abstract class MineLevel
{

protected int minX;
protected int minY;
protected int minZ;
protected int xSize;
protected int ySize;
protected int zSize;
protected int shaftX;
protected int shaftZ;
public int levelSize = 4;//the height of the level in blocks

public LinkedList<WorkPoint> workList = new LinkedList<WorkPoint>();

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
  this.minX = xPos;
  this.minY = yPos;
  this.minZ = zPos;
  }

public boolean hasWork()
  {
  return !workList.isEmpty();
  }

/**
 * called to map out the nodes for this level
 * @param world
 */
public void initializeLevel(TEMine mine, World world)
  { 
  long t1 = System.nanoTime();
  shaftX = minX -1 + xSize/2;
  shaftZ = minZ -1 + zSize/2;
  scanLevel(mine, world);  
  long t2 = System.nanoTime();
  Config.logDebug("mine level init time nanos: "+(t2-t1)); 
  }

protected abstract void scanLevel(TEMine mine, World world);

protected boolean needsFilled(int id)
  {
  return id==Block.lavaMoving.blockID || id==Block.lavaStill.blockID || id==Block.waterMoving.blockID || id==Block.waterStill.blockID;
  }

protected boolean needsFilledFloor(int id)
  {
  return id== 0 || id==Block.gravel.blockID || needsFilled(id);
  }

protected boolean isValidResource(int id)
  {
  if(id==0 || id==Block.stone.blockID || id==Block.cobblestone.blockID || id== Block.bedrock.blockID || id== Block.dirt.blockID || id==Block.grass.blockID || id==Block.ladder.blockID || id==Block.torchWood.blockID || id==Block.gravel.blockID)
    {
    return false;
    }
  return true;
  }

protected boolean shouldClear(int id)
  {
  if(id==0 || id==Block.waterMoving.blockID || id==Block.waterStill.blockID || id==Block.lavaMoving.blockID || id==Block.lavaStill.blockID)
    {
    return false;
    }
  return true;
  }

protected void addNewPoint(int x, int y, int z, byte meta, TargetType type)
  {
  this.workList.add(new WorkPoint(x,y,z, meta,type));
  }

protected void addNewPoint(int x, int y, int z, TargetType type)
  {
  this.addNewPoint(x, y, z, (byte)0, type);
  }



}
