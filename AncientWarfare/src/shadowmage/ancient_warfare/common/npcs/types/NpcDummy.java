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

import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.NpcTypeBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.helpers.NpcTargetHelper;

public class NpcDummy extends NpcTypeBase
{

/**
 * @param type
 */
public NpcDummy(int type)
  {
  super(type);
  this.isAvailableInSurvival = false;
  this.addLevel(type, 0, Config.texturePath + "models/npcDefault.png", null, null);
  this.addLevel(type, 1, Config.texturePath + "models/npcDefault.png", null, null);
  this.isCombatUnit = true;
  }


@Override
public List<NpcAIObjective> getAI(NpcBase npc, int level)
  {
  ArrayList<NpcAIObjective> aiEntries = new ArrayList<NpcAIObjective>(); 
  return aiEntries;
  }

@Override
public void addTargets(NpcBase npc, NpcTargetHelper helper)
  {
  // TODO Auto-generated method stub
  
  }

}
