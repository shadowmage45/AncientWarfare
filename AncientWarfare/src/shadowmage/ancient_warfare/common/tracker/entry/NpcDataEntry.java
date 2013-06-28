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
package shadowmage.ancient_warfare.common.tracker.entry;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class NpcDataEntry implements INBTTaggable
{

/**
 * entity unique id bits
 */
UUID entityID;
int npcType;
int npcRank;
BlockPosition lastKnownPosition = new BlockPosition(0,0,0);
int lastKnownHealth = 20;

/**
 * 
 */
public NpcDataEntry()
  {
  
  }

public NpcDataEntry(NpcBase npc)
  {
  this.entityID = npc.getPersistentID();
  this.npcType = npc.npcType.getGlobalNpcType();
  this.npcRank = npc.rank;
  this.lastKnownHealth = npc.getHealth();
  this.lastKnownPosition.updateFromEntityPosition(npc);
  }

public void updateEntry(NpcBase npc)
  {
  this.lastKnownHealth = npc.getHealth();
  this.lastKnownPosition.updateFromEntityPosition(npc);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setLong("idmsb", entityID.getMostSignificantBits());
  tag.setLong("idlsb", entityID.getLeastSignificantBits());
  tag.setCompoundTag("pos", lastKnownPosition.writeToNBT(new NBTTagCompound()));
  tag.setInteger("health", lastKnownHealth);
  tag.setInteger("type", npcType);
  tag.setInteger("rank", npcRank);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.lastKnownPosition = new BlockPosition(tag.getCompoundTag("pos"));
  this.lastKnownHealth = tag.getInteger("health");
  this.npcRank = tag.getInteger("rank");
  this.npcType = tag.getInteger("type");
  this.entityID = new UUID(tag.getLong("idmsb"), tag.getLong("idlsb"));
  }

}
