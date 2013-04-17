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
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.TargetType;

public class AITargetEntryNpc extends AITargetEntry
{

boolean sameTeam;
boolean oppositeTeam;

/**
 * 
 * @param npc
 * @param typeName
 * @param priority
 * @param maxTargetRange
 * @param sameTeam
 * @param oppositeTeam
 */
public AITargetEntryNpc(NpcBase npc, TargetType typeName, int priority, float maxTargetRange, boolean sameTeam, boolean oppositeTeam)
  {
  super(npc, typeName, NpcBase.class, priority, true, maxTargetRange);
  this.sameTeam = sameTeam;
  this.oppositeTeam = oppositeTeam;
  }

@Override
public boolean isTarget(Entity ent)
  {   
  if(ent instanceof NpcBase)
    {
    NpcBase npc = (NpcBase)ent;
    int thisTeam = this.npc.teamNum;
    int otherTeam = npc.teamNum;
    boolean hostile = TeamTracker.instance().isHostileTowards(npc.worldObj, thisTeam, otherTeam);
    if(hostile && oppositeTeam || !hostile && sameTeam)
      {
      return true;
      }    
    }
  return false;
  }

}
