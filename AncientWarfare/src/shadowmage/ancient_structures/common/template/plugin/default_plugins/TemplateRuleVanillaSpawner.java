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
package shadowmage.ancient_structures.common.template.plugin.default_plugins;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

public class TemplateRuleVanillaSpawner extends TemplateRuleVanillaBlocks
{

String mobID;

/**
 * @param world
 * @param x
 * @param y
 * @param z
 * @param block
 * @param meta
 */
public TemplateRuleVanillaSpawner(World world, int x, int y, int z, Block block, int meta, int turns)
  {
  super(world, x, y, z, block, meta, turns);
  TileEntityMobSpawner te = (TileEntityMobSpawner) world.getBlockTileEntity(x, y, z);
  mobID = te.getSpawnerLogic().getEntityNameToSpawn();  
  }

public TemplateRuleVanillaSpawner(String[] ruleData)
  {
  super(ruleData);
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  world.setBlock(x, y, z, Block.mobSpawner.blockID, meta, 2);
  TileEntityMobSpawner te = (TileEntityMobSpawner) world.getBlockTileEntity(x, y, z);
  te.getSpawnerLogic().setMobID(mobID);
  }

@Override
public boolean shouldReuseRule(World world, Block block, int meta, int turns, TileEntity te, int x, int y, int z)
  {  
  return te instanceof TileEntityMobSpawner && mobID.equals(((TileEntityMobSpawner)te).getSpawnerLogic().getEntityNameToSpawn());
  }

}
