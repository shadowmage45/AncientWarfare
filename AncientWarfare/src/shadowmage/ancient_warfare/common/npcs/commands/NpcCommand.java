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
package shadowmage.ancient_warfare.common.npcs.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public enum NpcCommand
{
NONE ("None", TargetType.NONE, 0),
WORK ("Work At Site", TargetType.WORK, 0),
PATROL ("Patrol Point", TargetType.PATROL, 0),
HOME ("Set Home Point", TargetType.SHELTER, 0),
CLEAR_PATROL ("Clear Patrol Path", TargetType.NONE, 0),
CLEAR_HOME ("Clear Home Point", TargetType.NONE, 0),
CLEAR_WORK ("Clear Work Site", TargetType.NONE, 0),
UPKEEP("Set Upkeep Target", TargetType.UPKEEP, 0),
CLEAR_UPKEEP("Clear Upkeep Target", TargetType.NONE, 0),
MOUNT("Set Vehicle to Pilot", TargetType.MOUNT, 0),
CLEAR_MOUNT("Clear Vehicle Target", TargetType.NONE, 0),
GUARD("Set Guard Target", TargetType.PATROL,  0),
CLEAR_GUARD("Clear Guard Target", TargetType.NONE, 0);
//due to using ordinal to store command, any new commands must be added to the END of the list
//also, don't EVER change ordering, or it will have some undesired effects in an already started game

String name;
TargetType type;
int batonRank = 0;
private NpcCommand(String name, TargetType type, int minBatonRank)
  {
  this.name = name;
  this.type = type;
  this.batonRank = minBatonRank;
  }

public String getCommandName()
  {
  return name;
  }

public TargetType getTargetType()
  {
  return type;
  }

public static List<NpcCommand> getCommandsForItem(ItemStack item)
  {
  if(item==null)
    {
    return Collections.emptyList();
    }
  else
    {
    return getCommandsForRank(item.getItemDamage());
    }
  }

public static boolean isSingleTargetOnly(NpcCommand command)
  {
  return command==MOUNT;
  }

public static ArrayList<NpcCommand> getCommandsForRank(int rank)
  {
  ArrayList<NpcCommand> commands = new ArrayList<NpcCommand>();
  for(NpcCommand com : values())
    {
    if(com.batonRank<=rank)
      {
      commands.add(com);
      }
    }
  return commands;
  }

}
