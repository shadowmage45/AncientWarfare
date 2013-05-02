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
package shadowmage.ancient_warfare.common.block;

import net.minecraft.block.Block;
import shadowmage.ancient_warfare.common.civics.BlockCivic;
import shadowmage.ancient_warfare.common.civics.worksite.te.builder.TECivicBuilder;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmCactus;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmCarrot;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmPotato;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmReed;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmWheat;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEWorkSiteFarm;
import shadowmage.ancient_warfare.common.civics.worksite.te.mine.TEWorkSiteMine;
import shadowmage.ancient_warfare.common.civics.worksite.te.tree.TETreeFarmOak;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

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
  registerBlock(builder, "Builder"); 
  registerBlock(civicBlock1, "CivicBlock1");
  registerBlock(civicBlock2, "CivicBlock2");
  registerBlock(civicBlock3, "CivicBlock3");
  registerBlock(civicBlock4, "CivicBlock4");
  GameRegistry.registerTileEntity(TEBuilder.class, "AWBuilder");
  GameRegistry.registerTileEntity(TEWorkSiteFarm.class, "AWFarmSiteTE");   
  GameRegistry.registerTileEntity(TEFarmWheat.class, "Wheat Farm");
  GameRegistry.registerTileEntity(TEFarmCarrot.class, "Carrot Farm");
  GameRegistry.registerTileEntity(TEFarmPotato.class, "Potato Farm");
  GameRegistry.registerTileEntity(TEFarmReed.class, "Reed Farm");
  GameRegistry.registerTileEntity(TEFarmCactus.class, "Cactus Farm");
  GameRegistry.registerTileEntity(TEWorkSiteMine.class, "Basic Mine");
  
  GameRegistry.registerTileEntity(TETreeFarmOak.class, "Tree Farm Oak");
  
  GameRegistry.registerTileEntity(TECivicBuilder.class, "Civic Builder");
  }

public void registerBlock(Block block, String name)
  {
  GameRegistry.registerBlock(block, name);
  LanguageRegistry.addName(block, name);
  DescriptionRegistry2.instance().registerBlock(block, false);
  }

}
