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

import shadowmage.ancient_warfare.common.AWCore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerSettings extends ContainerBase
{

public boolean isOp;


public ContainerSettings(EntityPlayer openingPlayer)
  {
  super(openingPlayer, null);  
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {

  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  if(tag.hasKey("op"))
    {
    this.isOp = tag.getBoolean("op");
    }
  this.refreshGui();
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  ArrayList<NBTTagCompound> initList = new ArrayList<NBTTagCompound>();
  NBTTagCompound initTag = new NBTTagCompound();
  this.isOp = AWCore.proxy.isPlayerOp(player);
  initTag.setBoolean("op", isOp);
  initList.add(initTag);
  return initList;
  }

}
