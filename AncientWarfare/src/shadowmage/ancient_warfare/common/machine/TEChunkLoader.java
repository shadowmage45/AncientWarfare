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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerChunkloaderDeluxe.ChunkMapEntry;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

import com.google.common.collect.ImmutableSet;

public class TEChunkLoader extends TEMachine
{

Ticket tk;

Set<ChunkCoordIntPair> forcedChunks = new HashSet<ChunkCoordIntPair>();

/**
 * 
 */
public TEChunkLoader()
  {
  this.guiNumber = -1;
  this.shouldWriteInventory = false;
  this.canUpdate = true;//DEBUG--CHANGE BACK TO FALSE WHEN DONE  
  }



@Override
public void updateEntity()
  {  
  super.updateEntity();
  Config.log("chunkloader...loaded...");
  }



public void setTicket(Ticket tk)
  {
  this.releaseTicket();
  this.tk = tk;
  if(this.tk!=null)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setCompoundTag("pos", new BlockPosition(xCoord, yCoord, zCoord).writeToNBT(new NBTTagCompound()));
    tk.getModData().setCompoundTag("chunkTE", tag);
    
    ForgeChunkManager.forceChunk(tk, new ChunkCoordIntPair(xCoord>>4, zCoord>>4));
    ImmutableSet tkCk = tk.getChunkList();
    Iterator<ChunkCoordIntPair> it = tkCk.iterator();
    while(it.hasNext())
      {
      ChunkCoordIntPair ccip = it.next();
      this.forceChunk(ccip);
      }
    for(ChunkCoordIntPair ccip : this.forcedChunks)
      {
      this.forceChunk(ccip);
      }
    }
  }

public Collection<ChunkCoordIntPair> getForcedChunks()
{
if(this.tk!=null)
  {
  return this.tk.getChunkList();
  }
return Collections.emptyList();    
}

protected void forceChunk(ChunkCoordIntPair chunk)
  {
  if(this.tk!=null)
    {
    Config.log("Forcing chunk: "+chunk.chunkXPos+","+chunk.chunkZPos);
    ForgeChunkManager.forceChunk(tk, chunk);
    this.forcedChunks.add(chunk);
    Config.logDebug("forced chunks size: "+this.forcedChunks.size());
    }
  }

protected void releaseChunk(ChunkCoordIntPair chunk)
  {
  if(this.tk!=null)
    {
    Config.log("Releasing forced chunk: "+chunk.chunkXPos+","+chunk.chunkZPos);
    ForgeChunkManager.unforceChunk(tk, chunk);
    this.forcedChunks.remove(chunk);
    }
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
    if(force)
      {
      this.forceChunk(chunk);
      }
    else
      {
      this.releaseChunk(chunk);
      }
    }
  }

@Override
public void onBlockBreak()
  {
  if(!this.worldObj.isRemote)
    {
    this.releaseTicket();
    }
  super.onBlockBreak();
  }

public void releaseTicket()
  {
  if(this.tk!=null)
    {
    Config.log("Releasing chunks from chunkloader at: "+xCoord+","+yCoord+","+zCoord);  
    ForgeChunkManager.releaseTicket(tk);
    this.tk = null;
    }
  }

@Override
public void onBlockPlaced()
  {  
  if(this.worldObj==null || this.worldObj.isRemote){return;}
  this.setTicket(ForgeChunkManager.requestTicket(AWCore.instance, worldObj, Type.NORMAL));  
  Config.log("Forcing chunk for position: "+xCoord +"," + yCoord +"," + zCoord + " for AW single chunkloader.");
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {  
  super.readFromNBT(tag);
  if(tag.hasKey("chunkList"))
    {
    NBTTagList chunkList = tag.getTagList("chunkList");
    NBTTagCompound chunkTag;
    ChunkCoordIntPair chunk;
    for(int i = 0; i < chunkList.tagCount(); i++)
      {
      chunkTag = (NBTTagCompound) chunkList.tagAt(i);
      chunk = new ChunkCoordIntPair(chunkTag.getInteger("x"), chunkTag.getInteger("z"));
      this.forcedChunks.add(chunk);
      }
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  NBTTagList chunkList = new NBTTagList();
  NBTTagCompound chunkTag;
  for(ChunkCoordIntPair ck : this.forcedChunks)
    {
    chunkTag = new NBTTagCompound();
    chunkTag.setInteger("x", ck.chunkXPos);
    chunkTag.setInteger("z", ck.chunkZPos);
    chunkList.appendTag(chunkTag);
    }
  tag.setTag("chunkList", chunkList);
  }


}
