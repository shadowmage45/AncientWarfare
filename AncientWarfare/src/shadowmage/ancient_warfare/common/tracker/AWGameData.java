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
package shadowmage.ancient_warfare.common.tracker;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import shadowmage.ancient_warfare.common.AWStructureModule;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.world_gen.WorldGenManager;


public class AWGameData extends WorldSavedData
{
/**
 * http://www.minecraftforge.net/forum/index.php?topic=8520.0
 * https://github.com/diesieben07/Questology/blob/e9f46d8b3aa41b82f9021c892bca7ee558e7899a/source/demonmodders/questology/QuestologyWorldData.java
 */
static final String name = "AW_GAME_DATA";

/**
 * @param par1Str
 */
public AWGameData()
  {
  super(name);
  }

public AWGameData(String name)
  {
  super(name);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  if(tag.hasKey("playerData"))
    {
    Config.logDebug("loading player data");
    PlayerTracker.instance().readFromNBT(tag.getCompoundTag("playerData"));
    }
  if(tag.hasKey("teamData"))
    {
    Config.logDebug("loading team data");
    TeamTracker.instance().readFromNBT(tag.getCompoundTag("teamData"));
    }
  if(tag.hasKey("builders"))
    {
    Config.logDebug("loading builder data");
    AWStructureModule.instance().readFromNBT(tag.getCompoundTag("builders"));
    }  
  if(tag.hasKey("structMap"))
    {
    Config.logDebug("loading spawned structure data");
    WorldGenManager.instance().readFromNBT(tag.getCompoundTag("structMap"));
    }
  if(tag.hasKey("npcMap"))
    {
    GameDataTracker.instance().loadNpcMap(tag.getCompoundTag("npcMap"));
    }
  if(tag.hasKey("mailboxData"))
    {
    MailboxData.instance().readFromNBT(tag.getCompoundTag("mailboxData"));
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  tag.setCompoundTag("playerData", PlayerTracker.instance().getNBTTag());  
  tag.setCompoundTag("teamData", TeamTracker.instance().getNBTTag());
  tag.setCompoundTag("builders", AWStructureModule.instance().getNBTTag());  
  tag.setCompoundTag("structMap", WorldGenManager.instance().getNBTTag());  
  tag.setCompoundTag("npcMap", GameDataTracker.instance().getNpcMapTag());
  tag.setCompoundTag("mailboxData", MailboxData.instance().getNBTTag());
  }

public static AWGameData get(World world) 
  {
  AWGameData data = (AWGameData)world.loadItemData(AWGameData.class, name);
  if(data==null) 
    {
    data = new AWGameData();
    world.mapStorage.setData(name, data);
    }
  return data;
  }

}
