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
package shadowmage.ancient_warfare.common.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_warfare.common.machine.TEChunkLoaderDeluxe;

public class ContainerChunkloaderDeluxe extends ContainerBase
{
public TEChunkLoaderDeluxe te;

public ChunkMapEntry[][] chunkMap = new ChunkMapEntry[11][11];

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerChunkloaderDeluxe(EntityPlayer openingPlayer, TEChunkLoaderDeluxe te)
  {
  super(openingPlayer, null);
  this.te = te;
  int centerChunkX = te.xCoord>>4;
  int centerChunkZ = te.zCoord>>4;
  int cornerChunkX = centerChunkX - 5;
  int cornerChunkZ = centerChunkZ - 5;
  for(int z = 0; z< 11; z++)
    {
    for(int x=0; x < 11; x++)
      {
      chunkMap[x][z] = new ChunkMapEntry(x+cornerChunkX, z+cornerChunkZ);
      }
    }
  if(!openingPlayer.worldObj.isRemote)
    {
    Collection<ChunkCoordIntPair> chunks = this.te.getForcedChunks();
    int adjX;
    int adjZ;
    for(ChunkCoordIntPair chunk : chunks)
      {
      adjX = chunk.chunkXPos - cornerChunkX;
      adjZ = chunk.chunkZPos - cornerChunkZ;
      if(adjX>=0 && adjX<11 && adjZ >=0 && adjZ <11)
        {
        chunkMap[adjX][adjZ].forced = true;
        }
      }
    }
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("chunkSelect"))
    {
    boolean force = tag.getBoolean("chunkSelect");
    int x = tag.getInteger("x");
    int z = tag.getInteger("z");
    ChunkMapEntry chunk = this.chunkMap[x][z];
    this.te.setChunk(chunk.chunkX, chunk.chunkZ, force);
    }
  }

public boolean isChunkForced(int arrayX, int arrayZ)
  {
  return chunkMap[arrayX][arrayZ].forced;
  }

public void handleChunkSelect(int arrayX, int arrayZ)
  {
  boolean forced = this.chunkMap[arrayX][arrayZ].forced;
  this.chunkMap[arrayX][arrayZ].forced = !forced;
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("chunkSelect", !forced);
  tag.setInteger("x", arrayX);
  tag.setInteger("z", arrayZ);
  this.sendDataToServer(tag);
  }

public int[] getChunkCoords(int arrayX, int arrayZ)
  {
  return new int[]{chunkMap[arrayX][arrayZ].chunkX,chunkMap[arrayX][arrayZ].chunkZ};
  }

public void handleChunkSelection(int x, int z)
  {
  this.handleChunkSelect(x, z);
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  if(tag.hasKey("chunkList"))
    {
    int x;
    int z;
    NBTTagList list = tag.getTagList("chunkList");
    for(int i = 0;i < list.tagCount(); i++)
      {
      NBTTagCompound chunkTag = (NBTTagCompound) list.tagAt(i);
      x = chunkTag.getInteger("x");
      z = chunkTag.getInteger("z");        
      chunkMap[x][z].forced = true;
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
  for(int z = 0; z<11 ; z++)
    {
    for(int x = 0 ; x < 11; x++)
      {
      if(chunkMap[x][z].forced)
        {
        chunkTag = new NBTTagCompound();
        chunkTag.setInteger("x", x);
        chunkTag.setInteger("z", z);
        list.appendTag(chunkTag);
        }
      }
    }  
  tag.setTag("chunkList", list);
  return tagList;
  }

public class ChunkMapEntry
{
int chunkX;
int chunkZ;
boolean forced = false;;
public ChunkMapEntry(int x, int z)
  {
  this.chunkX = x;
  this.chunkZ = z;
  }

public ChunkMapEntry setForced(boolean forced)
  {
  this.forced = forced; 
  return this;
  }

public boolean isForced()
  {
  return forced;
  }

public int getChunkX()
  {
  return chunkX;
  }

public int getChunkZ()
  {
  return chunkZ;
  }

}


}
