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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;

public class PlayerEntry implements INBTTaggable
{

public String playerName = "";
EntityPlayer player = null;
List<Integer> doneResearch = new ArrayList<Integer>();

public void addCompletedResearch(int num)
  {
  this.doneResearch.add(num);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", playerName);
  int[] research = new int[doneResearch.size()];
  for(int i = 0; i < doneResearch.size(); i++)
    {
    research[i] = doneResearch.get(i);
    }
  tag.setIntArray("res", research);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.playerName = tag.getString("name");
  this.doneResearch.clear();
  int[] research = tag.getIntArray("res");
  for(int i = 0; i< research.length; i++)
    {
    doneResearch.add(research[i]);
    }
  }

}
