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
package shadowmage.ancient_warfare.common.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import shadowmage.ancient_warfare.common.crafting.TEAWCrafting;
import shadowmage.ancient_warfare.common.crafting.TEAWResearch;

public class ContainerResearch extends ContainerAWCrafting
{

TEAWResearch resTE;
public List<Integer> researchQueueCache = new ArrayList<Integer>();

/**
 * @param openingPlayer
 * @param te
 */
public ContainerResearch(EntityPlayer openingPlayer, TEAWCrafting te)
  {
  super(openingPlayer, te);
  this.resTE = (TEAWResearch)te;
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  super.handlePacketData(tag);
  if(tag.hasKey("addQ"))
    {
    this.resTE.addResearchToQueue(tag.getInteger("addQ"));
    }
  if(tag.hasKey("remQ"))
    {    
    this.resTE.removeResearchFromQueue(tag.getInteger("remQ"));
    }
  if(tag.hasKey("queue"))
    {
    this.researchQueueCache.clear();
    int[] datas = tag.getIntArray("queue");
    for(int i =0; i < datas.length; i++)
      {
      this.researchQueueCache.add(datas[i]);
      }
    this.refreshGui();
    }
  }

@Override
public void detectAndSendChanges()
  {
  super.detectAndSendChanges();
  List<Integer> queue = resTE.getResearchQueue();
  if(!this.researchQueueCache.equals(queue))
    {
    this.researchQueueCache.clear();
    this.researchQueueCache.addAll(queue);    
    NBTTagIntArray ints = new NBTTagIntArray("queue");
    int[] array = new int[researchQueueCache.size()];    
    for(int i = 0; i < queue.size(); i++)
      {
      array[i] = queue.get(i);
      }
    ints.intArray = array;            
    NBTTagCompound tag = new NBTTagCompound();
    tag.setTag("queue", ints);
    this.sendDataToPlayer(tag);
    }
  }



}
