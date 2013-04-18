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

public enum NpcCommand
{
NONE ("None", false),
WORK ("Work At Site", false),
PATROL ("Patrol Point", false),
HOME ("Set Home Point", false),
DEPOSIT ("Set Depository", false),
CLEAR_PATROL ("Clear Patrol Path", false),
CLEAR_HOME ("Clear Home Point", false),
CLEAR_WORK ("Clear Work Site", false),
CLEAR_DEPOSIT ("Clear Depository", false),
MASS_PATROL ("Area Set Patrol Point", true),
MASS_HOME ("Area Set Home Point", true),
MASS_WORK ("Area Set Work Point", true),
MASS_DEPOSIT ("Area Set Depository", true),
MASS_CLEAR_PATROL ("Area Clear Patrol Path", true),
MASS_CLEAR_HOME ("Area Clear Home Point", true),
MASS_CLEAR_WORK ("Area Clear Work Site", true),
MASS_CLEAR_DEPOSIT ("Area Clear Depository", true);
//due to using ordinal to store command, any new commands must be added to the END of the list
//also, don't EVER change ordering, or it will have some undesired effects in-game

String name;
boolean massEffect = false;
private NpcCommand(String name, boolean mass)
  {
  this.name = name;
  this.massEffect = mass;
  }

public String getCommandName()
  {
  return name;
  }

public boolean isMassEffect()
  {
  return this.massEffect;
  }
}
