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

import shadowmage.ancient_warfare.common.tracker.AWGameData;
import shadowmage.ancient_warfare.common.tracker.MailboxData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class MailboxSaveData extends WorldSavedData
{
static final String name = "AW_MAIL_DATA";
/**
 * @param par1Str
 */
public MailboxSaveData()
  {
  super(name);
  }

public MailboxSaveData(String name)
  {
  super(name);
  }

@Override
public void readFromNBT(NBTTagCompound nbttagcompound)
  {
  MailboxData.instance().readFromNBT(nbttagcompound.getCompoundTag("mailboxData"));
  }

@Override
public void writeToNBT(NBTTagCompound nbttagcompound)
  {
  nbttagcompound.setCompoundTag("mailboxData", MailboxData.instance().getNBTTag());
  }

public static MailboxSaveData get(World world) 
  {
  MailboxSaveData data = (MailboxSaveData)world.loadItemData(MailboxSaveData.class, name);
  if(data==null) 
    {
    data = new MailboxSaveData();
    world.mapStorage.setData(name, data);
    }
  return data;
  }
}
