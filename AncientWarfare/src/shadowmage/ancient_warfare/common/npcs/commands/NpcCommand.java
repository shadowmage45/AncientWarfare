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

import shadowmage.ancient_warfare.common.targeting.TargetType;

public enum NpcCommand
{
NONE ("None", TargetType.NONE),
WORK ("Work At Site", TargetType.WORK),
PATROL ("Patrol Point", TargetType.PATROL),
HOME ("Set Home Point", TargetType.SHELTER),
DEPOSIT ("Set Depository", TargetType.DELIVER),
CLEAR_PATROL ("Clear Patrol Path", TargetType.NONE),
CLEAR_HOME ("Clear Home Point", TargetType.NONE),
CLEAR_WORK ("Clear Work Site", TargetType.NONE),
CLEAR_DEPOSIT ("Clear Depository", TargetType.NONE),
MASS_PATROL ("Area Set Patrol Point", TargetType.PATROL),
MASS_HOME ("Area Set Home Point", TargetType.SHELTER),
MASS_WORK ("Area Set Work Point", TargetType.WORK),
MASS_DEPOSIT ("Area Set Depository", TargetType.DELIVER),
MASS_CLEAR_PATROL ("Area Clear Patrol Path", TargetType.NONE),
MASS_CLEAR_HOME ("Area Clear Home Point", TargetType.NONE),
MASS_CLEAR_WORK ("Area Clear Work Site", TargetType.NONE),
MASS_CLEAR_DEPOSIT ("Area Clear Depository", TargetType.NONE);
//due to using ordinal to store command, any new commands must be added to the END of the list
//also, don't EVER change ordering, or it will have some undesired effects in-game

String name;
TargetType type;
private NpcCommand(String name, TargetType type)
  {
  this.name = name;
  this.type = type;
  }

public String getCommandName()
  {
  return name;
  }

public TargetType getTargetType()
  {
  return type;
  }

}
