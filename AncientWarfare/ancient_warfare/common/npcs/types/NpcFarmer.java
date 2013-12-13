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
package shadowmage.ancient_warfare.common.npcs.types;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.config.AWCoreConfig;
import shadowmage.ancient_warfare.common.interfaces.ITEWorkSite;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.NpcTypeBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.objectives.AIFollowPlayer;
import shadowmage.ancient_warfare.common.npcs.ai.objectives.AIGoToWork;
import shadowmage.ancient_warfare.common.npcs.ai.objectives.AINpcUpkeepObjective;
import shadowmage.ancient_warfare.common.npcs.ai.objectives.AISeekShelter;
import shadowmage.ancient_warfare.common.npcs.ai.objectives.AIStayNearHome;
import shadowmage.ancient_warfare.common.npcs.ai.objectives.AIWander;
import shadowmage.ancient_warfare.common.npcs.helpers.NpcTargetHelper;
import shadowmage.ancient_warfare.common.npcs.helpers.targeting.AITargetEntry;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class NpcFarmer extends NpcTypeBase
{

/**
 * @param type
 */
public NpcFarmer(int type)
  {
  super(type);
  this.iconTexture = "npcFarmer";  
  this.addLevel(type, 0, Statics.TEXTURE_PATH + "models/npc/npcFarmer.png", getToolStack(0), null).addTargetType(CivicWorkType.FARM, CivicWorkType.ENGINE).setActionTicks(40).setUpkeep(4).addNeededResearch(ResearchGoalNumbers.logistics1);
  this.addLevel(type, 1, Statics.TEXTURE_PATH + "models/npc/npcFarmer.png", getToolStack(1), null).addTargetType(CivicWorkType.FARM, CivicWorkType.ENGINE).setActionTicks(30).setUpkeep(6).addNeededResearch(ResearchGoalNumbers.logistics3);
  this.addLevel(type, 2, Statics.TEXTURE_PATH + "models/npc/npcFarmer.png", getToolStack(2), null).addTargetType(CivicWorkType.FARM, CivicWorkType.ENGINE).setActionTicks(20).setUpkeep(8).addNeededResearch(ResearchGoalNumbers.logistics5);
  this.isCombatUnit = false;
  this.defaultTargets = defaultTargetList;
  this.configName = "farmer";
  }

@Override
public void addTargets(NpcBase npc, NpcTargetHelper helper)
  {
  helper.addTargetEntry(new AITargetEntry(npc, TargetType.WORK, ITEWorkSite.class, 0, false, AWCoreConfig.npcAISearchRange));
  }

@Override
protected ItemStack getToolStack(int level)
  {
  ItemStack sword1 = null;// = new ItemStack(Item.swordSteel,1);
  switch(level)
  {
  case 0:
  sword1 = new ItemStack(Item.hoeStone,1);
  return sword1;
  
  case 1:
  sword1 = new ItemStack(Item.hoeIron,1);
  return sword1;
  
  case 2:  
  sword1 = new ItemStack(Item.hoeDiamond,1);
  return sword1;
  }
  return null;
  }

@Override
public List<NpcAIObjective> getAI(NpcBase npc, int level)
  {
  ArrayList<NpcAIObjective> aiEntries = new ArrayList<NpcAIObjective>();  
  aiEntries.add(new AIFollowPlayer(npc, 90));
  aiEntries.add(new AISeekShelter(npc, 85));
  aiEntries.add(new AINpcUpkeepObjective(npc, 82));
  aiEntries.add(new AIGoToWork(npc, 80));  
  aiEntries.add(new AIStayNearHome(npc, 70, 40, 15));
  aiEntries.add(new AIWander(npc, 10));
  return aiEntries;
  }
}
