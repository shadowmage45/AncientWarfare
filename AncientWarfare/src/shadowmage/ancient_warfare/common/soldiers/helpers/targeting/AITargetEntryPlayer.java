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
package shadowmage.ancient_warfare.common.soldiers.helpers.targeting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;

public class AITargetEntryPlayer extends AITargetEntry
{

boolean sameTeam;
boolean oppositeTeam;

/**
 * @param owner
 * @param typeName
 * @param clz
 * @param priority
 * @param isEntityTarget
 * @param maxTargetRange
 */
public AITargetEntryPlayer(NpcBase owner, int typeName, float maxTargetRange, boolean sameTeam, boolean oppositeTeam)
  {
  super(owner, typeName, EntityPlayer.class, 0, true, maxTargetRange);  
  this.sameTeam = sameTeam;
  this.oppositeTeam = oppositeTeam;
  }

@Override
public boolean isTarget(Entity ent)
  {   
  if(ent instanceof EntityPlayer)
    {
    EntityPlayer player = (EntityPlayer)ent;
    int thisTeam = this.npc.teamNum;
    int otherTeam = TeamTracker.instance().getTeamForPlayer(player);//.teamNum;
    boolean hostile = TeamTracker.instance().isHostileTowards(player.worldObj, thisTeam, otherTeam);
    if(hostile && oppositeTeam || !hostile && sameTeam)
      {
      return true;
      }    
    }
  return false;
  }
}
