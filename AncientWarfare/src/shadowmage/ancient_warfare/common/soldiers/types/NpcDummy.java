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

import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.NpcTypeBase;
import shadowmage.ancient_warfare.common.soldiers.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;

public class NpcDummy extends NpcTypeBase
{

/**
 * @param type
 */
public NpcDummy(int type)
  {
  super(type);
  this.displayName = "Dummy Test";
  this.tooltip = "Dummy Test -- PlaceHolder tooltip";
  this.addLevel("Dummy Test Level Name 1", "foo", null, null);
  this.addLevel("Dummy Test Level Name 2", "foo", null, null);
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
