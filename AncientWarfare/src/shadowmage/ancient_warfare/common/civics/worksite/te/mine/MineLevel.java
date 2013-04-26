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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
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

protected MineComponent shaft = new MineComponentShaft();
protected MineComponent tunnels = new MineComponentTunnels();
protected MineComponent branches = new MineComponentBranches();

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
  return this.shaft.hasWork() || this.tunnels.hasWork() || this.branches.hasWork();
  }

public MinePoint getNextMinePoint(NpcBase npc)
  {
  if(this.shaft.hasWork())
    {
    return this.shaft.getWorkFor(npc);
    }
  else if(this.tunnels.hasWork())
    {
    return this.tunnels.getWorkFor(npc);
    }
  else if(this.branches.hasWork())
    {
    return this.branches.getWorkFor(npc);
    }
  return null;
  }

public void onPointFailed(NpcBase npc, MinePoint ent)
  {
  if(ent.owner.parent==shaft)
    {
    shaft.onWorkFailed(npc, ent);
    }
  else if(ent.owner.parent==tunnels)
    {
    tunnels.onWorkFailed(npc, ent);
    }
  else if(ent.owner.parent==branches)
    {
    branches.onWorkFailed(npc, ent);
    }  
  }

public void onPointFinished(NpcBase npc, MinePoint ent)
  {
  Config.logDebug("point finished!! "+ent.owner + ent.owner.parent);
  if(ent.owner.parent==shaft)
    {
    Config.logDebug("shaft piece finished");
    shaft.onWorkFinished(npc, ent);
    }
  else if(ent.owner.parent==tunnels)
    {
    Config.logDebug("tunnel piece finished");
    tunnels.onWorkFinished(npc, ent);
    }
  else if(ent.owner.parent==branches)
    {
    Config.logDebug("branch piece finished");
    branches.onWorkFinished(npc, ent);
    }  
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
  long t2 = System.nanoTime();
  Config.logDebug("mine level init time nanos: "+(t2-t1)); 
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
  order = shaft.scanComponent(world, minX, minY, minZ, xSize, ySize, zSize, order, shaftX, shaftZ);
  return order;
  }

protected int mapTunnels(World world, int order, int shaftX, int shaftZ)
  {  
  order = tunnels.scanComponent(world, minX, minY, minZ, xSize, ySize, zSize, order, shaftX, shaftZ);
  return order;
  }

protected int mapBranches(World world, int order, int shaftX, int shaftZ)
  {
  order = branches.scanComponent(world, minX, minY, minZ, xSize, ySize, zSize, order, shaftX, shaftZ);
  return order;
  }

public List<String> getMapExport()
  {
  ArrayList<String> lines = new ArrayList<String>(); 
  return lines;
  }

/**
 * called from TE to validate 'cleared' points list
 */
public void validatePoints(World world)
  {
  this.shaft.verifyCompletedNodes(world);
  this.tunnels.verifyCompletedNodes(world);
  this.branches.verifyCompletedNodes(world);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound outerTag = new NBTTagCompound();
  outerTag.setIntArray("bounds", new int[]{minX, minY, minZ, xSize, ySize, zSize});
  outerTag.setTag("shaft", shaft.getNBTTag());
  outerTag.setTag("tunnels", tunnels.getNBTTag());
  outerTag.setTag("branches", branches.getNBTTag());  
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
  this.shaft.readFromNBT(tag.getCompoundTag("shaft"));
  this.tunnels.readFromNBT(tag.getCompoundTag("tunnels"));
  this.branches.readFromNBT(tag.getCompoundTag("branches"));
  }


}
