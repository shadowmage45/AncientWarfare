/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.jas_compat;

import jas.api.StructureInterpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.passive.EntityPig;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import shadowmage.ancient_structures.common.utils.AWGameData;
import shadowmage.ancient_structures.common.world_gen.StructureMap;

public class AWStuctureInterpreter implements StructureInterpreter
{

private List<String> keys = new ArrayList<String>();

/**
 * 
 */
public AWStuctureInterpreter()
  {
  keys.add("AWStructures");
  }

@Override
public Collection<String> getStructureKeys()
  {
  return keys;
  }

@Override
public Collection<SpawnListEntry> getStructureSpawnList(String structureKey)
  {
  List<SpawnListEntry> list = new ArrayList<SpawnListEntry>();
  SpawnListEntry entry = new SpawnListEntry(EntityPig.class, 1, 1, 1);
  list.add(entry);
  return list;
  }

@Override
public String areCoordsStructure(World world, int xCoord, int yCoord, int zCoord)
  {
  StructureMap map = AWGameData.get(world, StructureMap.name, StructureMap.class);
  
  return "AWStructures";
  }

@Override
public boolean shouldUseHandler(World world, BiomeGenBase biomeGenBase)
  {
  return true;
  }

}
