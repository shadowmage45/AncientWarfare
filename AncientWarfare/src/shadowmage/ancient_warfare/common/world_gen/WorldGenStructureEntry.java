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
public final int maxClearing;
public final int clearingBuffer;
public final int maxLeveling;
public final int levelingBuffer;
public final String[] biomesOnly;
public final String[] biomesNot;


private final boolean clearingOverride;
private final boolean levelingOverride;
private final boolean biomeOverride;

public WorldGenStructureEntry(String name, boolean unique, int weight, int value, int mC, int cB, int mL, int lB, String[] bO, String[] bN)
  {
  this.name = name;
  this.unique = unique;
  this.weight = weight;
  this.value = value;
  this.maxClearing = mC;
  this.clearingBuffer = cB;
  this.maxLeveling = mL;
  this.levelingBuffer = lB;
  this.biomesOnly = bO;
  this.biomesNot = bN;
  if(this.maxClearing>=0)
    {
    this.clearingOverride = true;
    }
  else
    {
    this.clearingOverride = false;
    }
  if(this.maxLeveling>=0)
    {
    this.levelingOverride = true;
    }
  else
    {
    this.levelingOverride = false;
    }
  if(this.biomesNot !=null || this.biomesOnly!=null)
    {
    this.biomeOverride = true;
    }
  else
    {
    this.biomeOverride = false;
    }
  }

public boolean hasClearingOverride()
  {
  return this.clearingOverride;
  }

public boolean hasLevlingOverride()
  {
  return this.levelingOverride;
  }

public boolean hasBiomeOverride()
  {
  return this.biomeOverride;
  }



}
