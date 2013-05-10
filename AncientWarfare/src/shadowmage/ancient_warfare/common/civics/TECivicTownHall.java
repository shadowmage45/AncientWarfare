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
package shadowmage.ancient_warfare.common.civics;

import java.util.Iterator;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class TECivicTownHall extends TECivic
{

public TECivicTownHall()
  {
  this.hasWork = true;
  this.broadcastWork = true;  
  }

@Override
public boolean hasWork()
  {
  return true;
  }
@Override
protected void updateHasWork()
  {  
  //noop
  }

@Override
protected void setHasWork(boolean newVal)
  {
  //noop
  }

@Override
public boolean canHaveMoreWorkers(NpcBase npc)
  {  
  return true;
  }

@Override
protected void validateWorkers()
  {
  Iterator<NpcBase> workIt = this.workers.iterator();
  NpcBase npc = null;
  while(workIt.hasNext())
    {
    npc = workIt.next();
    if(npc==null || npc.isDead || npc.getDistance(xCoord, yCoord, zCoord)>Config.npcAISearchRange)
      {      
      workIt.remove();
      continue;
      }
    WayPoint p = npc.wayNav.getUpkeepSite();
    if(p==null || p.floorX()!= xCoord || p.floorY()!=yCoord || p.floorZ()!=zCoord || worldObj.getBlockTileEntity(p.floorX(), p.floorY(), p.floorZ())!=this)
      {
      workIt.remove();
      continue;
      }    
    }
  }

@Override
public void broadCastToSoldiers(int maxRange)
  {
  if(this.worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1).expand(maxRange, maxRange/2, maxRange);
  List<NpcBase> npcList = worldObj.getEntitiesWithinAABB(NpcBase.class, bb);
  for(NpcBase npc : npcList)
    {
    if(isHostile(npc.teamNum))      
      {
      if(npc.npcType.isCombatUnit())
        {
        npc.targetHelper.handleTileEntityTargetBroadcast(this, TargetType.ATTACK_TILE, Config.npcAITicks*11);
        }      
      }
    else
      {
      if(broadcastWork)
        {    
        if(npc.wayNav.getUpkeepSite() == null)
          {
          Config.logDebug("broadcasting upkeep site to nearby npcs");
          npc.targetHelper.handleTileEntityTargetBroadcast(this, TargetType.UPKEEP, Config.npcAITicks*11);
          }
        }
      }
    }
  }

}
