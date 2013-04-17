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
package shadowmage.ancient_warfare.common.npcs.types;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.NpcTypeBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.objectives.AIFollowPlayer;
import shadowmage.ancient_warfare.common.npcs.ai.objectives.AIPatrolPoints;
import shadowmage.ancient_warfare.common.npcs.helpers.NpcTargetHelper;

public class NpcSoldierTest extends NpcTypeBase
{

/**
 * @param type
 */
public NpcSoldierTest(int type)
  {
  super(type);
  this.displayName = "Soldier Test";
  this.tooltip = "Test Soldier for Attack and Vehicle Interaction";
  this.addLevel("Soldier Rank 0", "foo", null, null);
  this.addLevel("Soldier Rank 1", "foo", null, null);
  this.addLevel("Soldier Rank 2", "foo", null, null);
  }

@Override
public void addTargets(NpcBase npc, NpcTargetHelper helper)
  {
//  helper.addTargetEntry(new AITargetEntry(npc, NpcTargetHelper.TARGET_ATTACK, EntityPlayer.class, 0, true, 40));
//  helper.addTargetEntry(new AITargetEntry(npc, NpcTargetHelper.TARGET_ATTACK, EntityMob.class, 0, true, 40));
//  helper.addTargetEntry(new AITargetEntry(npc, NpcTargetHelper.TARGET_ATTACK, EntitySlime.class, 0, true, 40));
//  helper.addTargetEntry(new AITargetEntryMountableVehicle(npc, -1, 20));
  }

@Override
public List<NpcAIObjective> getAI(NpcBase npc, int level)
  {
  ArrayList<NpcAIObjective> aiEntries = new ArrayList<NpcAIObjective>();  
//  aiEntries.add(new AIDismountVehicles(npc, 10));
//  aiEntries.add(new AIAttackTargets(npc, 9, 20, 10));  
  aiEntries.add(new AIFollowPlayer(npc, 8));
  aiEntries.add(new AIPatrolPoints(npc, 7, 20));
//  aiEntries.add(new AIMountVehicles(npc, 7, 20));  
//  aiEntries.add(new AIAttackTargets(npc, 6, 40, 40));  
  return aiEntries;
  }
}
