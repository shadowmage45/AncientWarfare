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
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.utils.IDPairCount;

public class ContainerSurvivalBuilder extends ContainerBase
{

/**
 * client side blockList data
 */
public List<IDPairCount> idCounts = new ArrayList<IDPairCount>();

/**
 * @param openingPlayer
 */
public ContainerSurvivalBuilder(EntityPlayer openingPlayer)
  {
  super(openingPlayer, null);
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {  
  if(tag.hasKey("clear"))
    {
    ItemStack stack = player.getCurrentEquippedItem();
    if(stack==null || stack.getItem() == null || stack.getItem().itemID!= ItemLoader.structureBuilderDirect.itemID)
      {
      return;
      }
    stack.setTagInfo("structData", new NBTTagCompound());
    }  
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  this.idCounts.clear();
  if(tag.hasKey("blockList"))
    {
    NBTTagList blockListTag = tag.getTagList("blockList");
    for(int i = 0; i < blockListTag.tagCount(); i++)
      {
      NBTTagCompound ct = (NBTTagCompound) blockListTag.tagAt(i);
      this.idCounts.add(new IDPairCount(ct));      
      }
    }  
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  ArrayList<NBTTagCompound> packetTags = new ArrayList<NBTTagCompound>();
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList blockListTag = new NBTTagList();
  ProcessedStructure struct = StructureManager.instance().getTempStructure(player.getEntityName());//ItemBuilderDirect.getStructureFor(player.getEntityName());
  if(struct!=null)
    {
    List<IDPairCount> counts = struct.getResourceList();
    for(IDPairCount count : counts)
      {      
      blockListTag.appendTag(count.getTag());
      }
    }
  tag.setTag("blockList", blockListTag);
  packetTags.add(tag);
  return packetTags;
  }

}
