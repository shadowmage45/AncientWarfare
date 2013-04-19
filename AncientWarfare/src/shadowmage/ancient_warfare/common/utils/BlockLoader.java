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
package shadowmage.ancient_warfare.common.utils;

import net.minecraft.block.Block;
import shadowmage.ancient_warfare.common.block.BlockBuilder;
import shadowmage.ancient_warfare.common.block.TEBuilder;
import shadowmage.ancient_warfare.common.civics.BlockCivic;
import shadowmage.ancient_warfare.common.civics.worksite.te.TEWorkSiteFarm;
import shadowmage.ancient_warfare.common.config.Config;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockLoader
{

public static final Block builder = new BlockBuilder(Config.getBlockID("blockSingle.builder", 3700, "Placeholder block for ticked-structure builders."));
public static final Block civicBlock1 = new BlockCivic(Config.getBlockID("blockMulti.civic1", 3701, "Civic Block 1"), "CivicBlock1", 0);
public static final Block civicBlock2 = new BlockCivic(Config.getBlockID("blockMulti.civic2", 3702, "Civic Block 2"), "CivicBlock2", 1);
public static final Block civicBlock3 = new BlockCivic(Config.getBlockID("blockMulti.civic3", 3703, "Civic Block 3"), "CivicBlock3", 2);
public static final Block civicBlock4 = new BlockCivic(Config.getBlockID("blockMulti.civic4", 3704, "Civic Block 4"), "CivicBlock4", 3);

private static BlockLoader INSTANCE;
private BlockLoader(){}
public static BlockLoader instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new BlockLoader();
    }
  return INSTANCE;
  }

public void load()
  {
  GameRegistry.registerBlock(builder, "Builder");
  GameRegistry.registerTileEntity(TEBuilder.class, "AWBuilderTE");
  
  GameRegistry.registerTileEntity(TEWorkSiteFarm.class, "AWFarmSiteTE");  
  }

}
