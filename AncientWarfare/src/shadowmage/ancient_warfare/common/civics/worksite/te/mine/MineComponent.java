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
package shadowmage.ancient_warfare.common.civics.worksite.te.mine;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.npcs.NpcBase;

public abstract class MineComponent implements INBTTaggable
{
int maxWorkers;//how many concurrent workers in the same component

public abstract MinePoint getWorkFor(NpcBase worker);

public abstract boolean hasWork();

public abstract void onWorkFinished(NpcBase npc, MinePoint p);//return the point to its 'finished' list

public abstract void onWorkFailed(NpcBase npc, MinePoint p);
  
public abstract int scanComponent(World world, int minX, int minY, int minZ, int xSize, int ySize, int zSize, int order, int shaftX, int shaftZ);

public abstract void verifyCompletedNodes(World world);

protected boolean needsFilled(int id)
  {
  return id==0 || id==Block.lavaMoving.blockID || id==Block.lavaStill.blockID || id==Block.waterMoving.blockID || id==Block.waterStill.blockID;
  }

protected boolean isValidResource(int id)
  {
  if(id==0 || id==Block.stone.blockID || id==Block.cobblestone.blockID || id== Block.bedrock.blockID || id== Block.dirt.blockID || id==Block.grass.blockID || id==Block.ladder.blockID || id==Block.torchWood.blockID)
    {
    return false;
    }
  return true;
  }
}
