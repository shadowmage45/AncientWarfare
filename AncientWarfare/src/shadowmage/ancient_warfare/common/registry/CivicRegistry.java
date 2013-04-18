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
package shadowmage.ancient_warfare.common.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

/**
 * map civic Blocks and TEs to the item damage/rank for the spawner item
 * @author Shadowmage
 *
 */
public class CivicRegistry
{

private HashMap<Integer, CivicEntry> civicMap = new HashMap<Integer, CivicEntry>();

public Block getCivicBlockFor(int type, int rank)
  {
  return null;
  }

public TileEntity getTEFor(int type, int rank)
  {
  return null;
  }

public void registerBlock(Block block, int globalID)
  {
  
  }

public void registerTE(int globalID, Class <? extends TileEntity> clz)
  {
  
  }

private class CivicEntry
{
int globalID;
Map<Integer, Class<? extends TileEntity>> workSiteMap = new HashMap<Integer, Class<? extends TileEntity>>();
}

}
