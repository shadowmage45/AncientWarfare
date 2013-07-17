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
package shadowmage.ancient_warfare.common.machine;

import java.util.Iterator;

import com.google.common.collect.ImmutableSet;

import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class TEChunkLoader extends TEMachine
{

Ticket tk;

/**
 * 
 */
public TEChunkLoader()
  {
  this.guiNumber = -1;
  this.shouldWriteInventory = false;
  this.canUpdate = false;  
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
      ForgeChunkManager.forceChunk(tk, ccip);
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
}
