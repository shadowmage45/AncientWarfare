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

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.gates.EntityGate;

public class ContainerGateControl extends ContainerBase
{

EntityGate gate;

public ContainerGateControl(EntityPlayer openingPlayer, int id)
  {
  super(openingPlayer, null);
  gate = (EntityGate) openingPlayer.worldObj.getEntityByID(id);
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("repack"))
    {
    gate.repackEntity();
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {

  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

}
