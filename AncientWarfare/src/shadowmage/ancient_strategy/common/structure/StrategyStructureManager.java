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

import shadowmage.ancient_framework.common.gamedata.AWGameData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class StrategyStructureManager
{

public static StrategyStructureManager instance(){return null;}

public StrategyStructureManager()
  {
  
  }

public StrategyStructure getStructureByID(World world, UUID id)
  {
  StrategyStructureData data = AWGameData.get(world, StrategyStructureData.dataName, StrategyStructureData.class);
  return data==null ? null : data.getStructureByID(world, id);
  }

public void addStructure(World world, StrategyStructure structure)
  {
  StrategyStructureData data = AWGameData.get(world, StrategyStructureData.dataName, StrategyStructureData.class);
  data.addStructure(world, structure);
  }

public void removeStructure(World world, UUID id)
  {
  StrategyStructureData data = AWGameData.get(world, StrategyStructureData.dataName, StrategyStructureData.class);
  data.removeStructure(world, id);
  }

public void removeStructureClient(World world, UUID id)
  {
  StrategyStructureData data = AWGameData.get(world, StrategyStructureData.dataName, StrategyStructureData.class);
  data.removeStructureClient(world, id);
  }

public void handlePacketData(World world, NBTTagCompound tag)
  {
  /**
   * server->client comms regarding new structures/removals/etc
   */
  }

}
