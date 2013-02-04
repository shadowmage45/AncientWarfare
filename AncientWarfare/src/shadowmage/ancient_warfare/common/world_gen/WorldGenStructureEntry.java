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
package shadowmage.ancient_warfare.common.world_gen;

public class WorldGenStructureEntry
{
public final String name;
public final boolean unique;
public final int weight;
public final int value;

public WorldGenStructureEntry(String csv)
  {
  String[] split = csv.trim().split(",");
  name = split[0];
  unique = Boolean.parseBoolean(split[1].trim());
  weight = Integer.parseInt(split[2].trim());
  value = Integer.parseInt(split[3].trim());
  }

public WorldGenStructureEntry(String name, boolean unique, int weight, int value)
  {
  this.name = name;
  this.unique = unique;
  this.weight = weight;
  this.value = value;
  }

}
