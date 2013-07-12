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
package shadowmage.ancient_warfare.common.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import shadowmage.ancient_warfare.common.machine.TEChunkLoaderDeluxe;

public class ContainerChunkloaderDeluxe extends ContainerBase
{
public TEChunkLoaderDeluxe te;
List<ChunkCoordIntPair> chunkList = new ArrayList<ChunkCoordIntPair>();

public ChunkCoordIntPair[][] chunkMap = new ChunkCoordIntPair[11][11];

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerChunkloaderDeluxe(EntityPlayer openingPlayer, TEChunkLoaderDeluxe te)
  {
  super(openingPlayer, null);
  this.te = te;  
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("setList"))
    {
    NBTTagList list = tag.getTagList("setList");
    this.chunkList.clear();
    for(int i = 0;i < list.tagCount(); i++)
      {      
      NBTTagCompound chunkTag = (NBTTagCompound) list.tagAt(i);
      int x = chunkTag.getInteger("x");
      int z = chunkTag.getInteger("z");
      
      /**
       * send to te to force new chunk on ticket, or release old chunk
       */
      this.chunkList.add(new ChunkCoordIntPair(x, z));
      }
    this.refreshGui();
    }
  if(tag.hasKey("chunkSelect"))
    {
    boolean force = tag.getBoolean("chunkSelect");
    int x = tag.getInteger("x");
    int z = tag.getInteger("z");
    this.te.setChunk(te.xCoord/16 + x - 5, te.zCoord/16 + z - 5, force);
    }
  }

public void handleChunkSelection(int x, int z)
  {
  boolean force = false;
  ChunkCoordIntPair c = this.chunkMap[x][z];
  if(c==null)
    {
    force = true;
    int tx = te.xCoord/16;
    int tz = te.zCoord/16;
    this.chunkMap[x][z]=new ChunkCoordIntPair(tx + x - 5, tz + z - 5);
    }
  else
    {
    force = false;
    this.chunkMap[x][z]=null;
    }
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("chunkSelect", force);
  tag.setInteger("x", x);
  tag.setInteger("z", z);
  this.sendDataToServer(tag);
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  if(tag.hasKey("chunkList"))
    {
    int tx = te.xCoord/16;
    int tz = te.zCoord/16;
    int x;
    int z;
    ChunkCoordIntPair chunk;
    NBTTagList list = tag.getTagList("chunkList");
    for(int i = 0;i < list.tagCount(); i++)
      {
      NBTTagCompound chunkTag = (NBTTagCompound) list.tagAt(i);
      x = chunkTag.getInteger("x");
      z = chunkTag.getInteger("z");
      chunk = new ChunkCoordIntPair(x, z);
      if(tx - x +5 >= 0 && tx - x + 5 <11 && tz - z + 5 >=0 && tz - z + 5 < 11)
        {
        this.chunkMap[tx - x+5][tz - z+5] =chunk;
        }      
      this.chunkList.add(chunk);
      }
    }
  this.refreshGui();
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  List<NBTTagCompound> tagList = new ArrayList<NBTTagCompound>();
  NBTTagCompound tag = new NBTTagCompound();  
  tagList.add(tag);
  NBTTagList list = new NBTTagList();
  NBTTagCompound chunkTag;
  for(ChunkCoordIntPair c : te.getForcedChunks())
    {
    chunkTag = new NBTTagCompound();
    chunkTag.setInteger("x", c.chunkXPos);
    chunkTag.setInteger("z", c.chunkZPos);
    list.appendTag(chunkTag);
    }  
  tag.setTag("chunkList", list);
  return tagList;
  }

}
