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
package shadowmage.ancient_warfare.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public abstract class TEStructureControl extends TileEntity
{

int updateTicks = 0;
int teamNum = 0;
int minX;
int minY;
int minZ;
int maxX;
int maxY;
int maxZ;
List<BlockPosition> waypoints = new ArrayList<BlockPosition>();

@Override
public void updateEntity()
  {
  if(updateTicks<=0)
    {
    this.updateWaypoints();
    this.broadCastToSoldiers(Config.npcAISearchRange);
    this.updateTicks = Config.npcAITicks;
    }
  else
    {
    updateTicks--;
    }
  super.updateEntity();
  }

@Override
public boolean canUpdate()
  {
  return true;
  }

public int getTeamNum()
  {
  return teamNum;
  }

public abstract void updateWaypoints();

public boolean isHostile(int sourceTeam)
  {
  if(this.worldObj==null)
    {
    return false;
    }
  return TeamTracker.instance().isHostileTowards(worldObj, sourceTeam, teamNum);
  }

public void broadCastToSoldiers(int maxRange)
  {
  if(this.worldObj==null)
    {
    return;
    }
  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord).expand(maxRange, maxRange, maxRange);
  List<NpcBase> npcList = worldObj.getEntitiesWithinAABB(NpcBase.class, bb);
  for(NpcBase npc : npcList)
    {
    if(isHostile(npc.teamNum))      
      {
      
      }
    else
      {
      
      }
    //transmit to NPC, he should add to list (defend/targets)
    }
  }

public abstract boolean onInteract(World world, EntityPlayer player);

}
