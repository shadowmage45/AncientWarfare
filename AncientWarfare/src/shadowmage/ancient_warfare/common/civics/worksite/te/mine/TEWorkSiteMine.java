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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.NpcBase;

public abstract class TEWorkSiteMine extends TECivic
{

/**
 *  I'm thinking it should map by level, keep the current level map in memory
 *    as a basic 3D array of bytes/shorts denoting action to be taken on that block
 *    /whether an action needs to be taken
 *    --when scanning for work-points, instead of scanning the world, you scan the in-
 *    memory array, which has custom tailored information for the path to be taken and
 *    action taken (both smaller lookup space, and no need to go through 'world' to get there)
 * 
 *  Mines will need a certain amount of either:
 *    logs,
 *    planks,
 *    sticks, or
 *    ladders
 *    --for construction of their central shaft.  At least one slot in the inventory
 *        should be preserved for ladders
 *  
 *  Mines will maintain fill material in their inventory slots.  When a worker harvests a point,
 *    if it is fill material and the mine is low, it will go into mine inventory, otherwise it
 *    will go into worker inventory.
 *    
 *  Mine should layout itself such that there is a central 2*2 vertical shaft, with ladders
 *    on the N/S walls.
 *  From this shaft should be one main E/W 2*2 horizontal x-axis tunnel at every level
 *  From these E/W shafts will come basic 2h*1w branch mining shafts
 *  
 *    
 */

int currentLevelNum;
int currentLevelMinY;
MineLevel currentLevel;

/**
 * 
 */
public TEWorkSiteMine()
  {
  // TODO Auto-generated constructor stub
  }

@Override
public WorkPoint getWorkPoint(NpcBase npc)
  {
  return super.getWorkPoint(npc);
  }

@Override
public boolean canAssignWorkPoint(NpcBase npc, WorkPoint p)
  {
  // TODO Auto-generated method stub
  return super.canAssignWorkPoint(npc, p);
  }

@Override
public void onWorkFinished(NpcBase npc, WorkPoint point)
  {
  // TODO Auto-generated method stub
  super.onWorkFinished(npc, point);
  }

@Override
public void onWorkFailed(NpcBase npc, WorkPoint point)
  {
  if(point instanceof WorkPointMine)
    {
    
    }
  super.onWorkFailed(npc, point);
  }

@Override
public void updateWorkPoints()
  {
  super.updateWorkPoints();
  if(this.workPoints.size()<this.civic.getMaxWorkers(structureRank))
    {
    while(this.workPoints.size()<this.civic.getMaxWorkers(structureRank) && this.currentLevel!=null && this.currentLevel.hasWork())
      {
      MinePointEntry minePoint = this.currentLevel.getNextWorkPoint();
      }
    }
  }

@Override
public WorkPoint doWork(NpcBase npc, WorkPoint p)
  {
  // TODO Auto-generated method stub
  return super.doWork(npc, p);
  }

@Override
public boolean hasWork(NpcBase npc)
  {
  // TODO Auto-generated method stub
  return super.hasWork(npc);
  }

@Override
public boolean onInteract(World world, EntityPlayer player)
  {
  if(!world.isRemote)
    {
    GUIHandler.instance().openGUI(GUIHandler.CIVIC_BASE, player, world, xCoord, yCoord, zCoord);
    }
  return true;
  }


}
