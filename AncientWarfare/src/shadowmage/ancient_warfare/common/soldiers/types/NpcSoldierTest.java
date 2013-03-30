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
package shadowmage.ancient_warfare.common.soldiers.types;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import shadowmage.ancient_warfare.common.soldiers.INpcAI;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.NpcTypeBase;
import shadowmage.ancient_warfare.common.soldiers.ai.AIAttackTarget;
import shadowmage.ancient_warfare.common.soldiers.ai.AIChooseAttackTarget;
import shadowmage.ancient_warfare.common.soldiers.ai.AIChooseMountTarget;
import shadowmage.ancient_warfare.common.soldiers.ai.AIMountVehicle;
import shadowmage.ancient_warfare.common.soldiers.ai.AIMoveToTarget;
import shadowmage.ancient_warfare.common.soldiers.ai.AIWanderTest;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AITargetEntry;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AITargetEntryMountableVehicle;

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
  this.addLevel("Soldier Rank 0", "foo");
  this.addLevel("Soldier Rank 1", "foo");
  this.addLevel("Soldier Rank 2", "foo");
  }

@Override
public void addTargets(NpcBase npc, NpcTargetHelper helper)
  {
//  helper.addTargetEntry(new AITargetEntry(npc, "attack", EntityPlayer.class, 0, true, 40));
  helper.addTargetEntry(new AITargetEntry(npc, "attack", EntityMob.class, 0, true, 40));
  helper.addTargetEntry(new AITargetEntry(npc, "attack", EntitySlime.class, 0, true, 40));
  helper.addTargetEntry(new AITargetEntryMountableVehicle(npc, -1, 20));
  }

@Override
public List<INpcAI> getAI(NpcBase npc, int level)
  {
  ArrayList<INpcAI> aiEntries = new ArrayList<INpcAI>();
  aiEntries.add(new AIMoveToTarget(npc));
  aiEntries.add(new AIChooseAttackTarget(npc));  
  aiEntries.add(new AIAttackTarget(npc));
//  aiEntries.add(new AIChooseMountTarget(npc));
//  aiEntries.add(new AIMountVehicle(npc));
  aiEntries.add(new AIWanderTest(npc,20));
  return aiEntries;
  }
}
