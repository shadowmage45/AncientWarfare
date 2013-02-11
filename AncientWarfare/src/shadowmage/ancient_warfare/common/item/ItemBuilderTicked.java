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
package shadowmage.ancient_warfare.common.item;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.block.TEBuilder;
import shadowmage.ancient_warfare.common.structures.build.BuilderTicked;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.utils.BlockLoader;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class ItemBuilderTicked extends ItemBuilderInstant
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBuilderTicked(int itemID)
  {
  super(itemID);
  }

protected void attemptConstruction(World world, ProcessedStructure struct, int face, BlockPosition hit)
  {
  BlockPosition offsetHit = hit.copy();
  offsetHit.moveForward(face, -struct.zOffset + 1 + struct.clearingBuffer);
  world.setBlockAndMetadata(hit.x, hit.y, hit.z, BlockLoader.instance().builder.blockID, BlockTools.getBlockMetaFromPlayerFace(face));    
  TEBuilder te = (TEBuilder) world.getBlockTileEntity(hit.x, hit.y, hit.z);
  if(te!=null)
    { 
    BuilderTicked builder = new BuilderTicked(world, struct, face, offsetHit);
    builder.startConstruction();
    te.setBuilder(builder);
    Ticket tk = ForgeChunkManager.requestTicket(AWCore.instance, world, Type.NORMAL);
    if(tk!=null)
      {
      ForgeChunkManager.forceChunk(tk, new ChunkCoordIntPair(hit.x/16, hit.z/16));
      te.setTicket(tk);
      }
    struct.addBuilder(builder);
    }  
  } 

@Override
public boolean renderBuilderBlockBB()
  {
  return true;
  }
}
