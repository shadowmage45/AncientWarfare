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
package shadowmage.ancient_warfare.common.aw_structure.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.aw_core.container.ContainerBase;
import shadowmage.ancient_warfare.common.aw_core.utils.IDPairCount;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.item.ItemBuilderDirect;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;

public class ContainerSurvivalBuilder extends ContainerBase
{

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
  // TODO Auto-generated method stub  
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
public NBTTagCompound getInitData()
  {
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
  return tag;
  }

}
