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
package shadowmage.ancient_warfare.common.npcs.helpers.targeting;

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;

public class AITargetEntryNpc extends AITargetEntry
{

boolean sameTeam;
boolean oppositeTeam;
int targetType = -1;
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

public AITargetEntryNpc(NpcBase npc, TargetType typeName, int priority, float maxTargetRange, boolean sameTeam, boolean oppositeTeam, int targetType)
  {
  this(npc, typeName, priority, maxTargetRange, sameTeam, oppositeTeam);
  this.targetType = targetType;  
  }

@Override
public boolean isTarget(Entity ent)
  {   
  if(ent==null || ent.isDead)
    {
    return false;
    }
  if(ent instanceof NpcBase)
    {
    NpcBase npc = (NpcBase)ent;
    if(targetType>=0 && npc.npcType.getGlobalNpcType()!=targetType)
      {
      return false;
      }
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
