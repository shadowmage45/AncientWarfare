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
package shadowmage.ancient_warfare.common.registry.entry;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.soldiers.NpcBase;

public class NpcEntry
{
public final String displayName;
public final String displayTooltip;
public final int numOfRanks;
public final List<String> rankNames = new ArrayList<String>();
public final Class <? extends NpcBase> entityClass;

public NpcEntry (Class <? extends NpcBase> clz, String name, String tooltip, int ranks, List<String> rankNames)
  {
  this.entityClass = clz;
  this.displayName = name;
  this.displayTooltip = tooltip;
  this.numOfRanks = ranks;
  this.rankNames.addAll(rankNames);      
  }

}
