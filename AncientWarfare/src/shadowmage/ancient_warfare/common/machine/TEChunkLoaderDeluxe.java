/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.machine;

import java.util.Collection;
import java.util.Collections;

import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.GUIHandler;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class TEChunkLoaderDeluxe extends TEChunkLoader
{

public TEChunkLoaderDeluxe()
  {
  this.guiNumber = GUIHandler.CHUNKLOADER_DEULXE;
  }

public Collection<ChunkCoordIntPair> getForcedChunks()
  {
  if(this.tk!=null)
    {
    return this.tk.getChunkList();
    }
  return Collections.emptyList();    
  }

/**
 * 
 * @param chunkX
 * @param chunkZ
 * @param force if true, release if false
 */
public void setChunk(int chunkX, int chunkZ, boolean force)
  {
  if(this.tk!=null)
    {
    ChunkCoordIntPair chunk = new ChunkCoordIntPair(chunkX, chunkZ);
    Config.logDebug("setting chunk: "+chunkX+","+chunkZ+" forced: "+force);
    if(force)
      {
      ForgeChunkManager.forceChunk(tk, chunk);
      }
    else
      {
      ForgeChunkManager.unforceChunk(tk, chunk);
      }
    }
  }

@Override
public void onBlockPlaced()
  {  
  if(this.worldObj==null || this.worldObj.isRemote){return;}
  this.setTicket(ForgeChunkManager.requestTicket(AWCore.instance, worldObj, Type.NORMAL));  
  Config.log("Forcing chunk for position: "+xCoord +"," + yCoord +"," + zCoord + " for AW Deluxe chunkloader.");
  }


}
