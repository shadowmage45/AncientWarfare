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
package shadowmage.ancient_strategy.common.structure;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.gamedata.GameData;

public class StrategyStructureData extends GameData
{

HashMap<Integer, StrategyStructureDimensionList> structuresByDimension = new HashMap<Integer, StrategyStructureDimensionList>();

public static String dataName = "AW_STRATEGY_DATA";

public StrategyStructureData()
  {
  this(dataName);
  }

public StrategyStructureData(String name)
  {
  super(name);
  }

@Override
public void handlePacketData(NBTTagCompound data)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void readFromNBT(NBTTagCompound nbttagcompound)
  {
  
  }

@Override
public void writeToNBT(NBTTagCompound nbttagcompound)
  {
  
  }

public StrategyStructure getStructureByID(World world, UUID id)
  {  
  if(!structuresByDimension.containsKey(world.provider.dimensionId))
    {
    return null;
    }
  return structuresByDimension.get(world.provider.dimensionId).structuresByID.get(id);
  }

public void addStructure(World world, StrategyStructure structure)
  {
  if(!this.structuresByDimension.containsKey(world.provider.dimensionId))
    {
    this.structuresByDimension.put(world.provider.dimensionId, new StrategyStructureDimensionList());
    }
  this.structuresByDimension.get(world.provider.dimensionId).structuresByID.put(structure.id, structure);
  }

public void removeStructure(World world, UUID id)
  {
  if(this.structuresByDimension.containsKey(world.provider.dimensionId))
    {
    this.structuresByDimension.get(world.provider.dimensionId).structuresByID.remove(id);
    }
  }

private class StrategyStructureDimensionList
{

private HashMap<UUID, StrategyStructure> structuresByID = new HashMap<UUID, StrategyStructure>();

}

}
