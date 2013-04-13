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
package shadowmage.ancient_warfare.common.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;

public class ContainerTeamControl extends ContainerBase
{

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerTeamControl(EntityPlayer openingPlayer)
  {
  super(openingPlayer, null); 
  }

public void rebuildTeamList()
  {
  Config.logDebug("sending rebuild to gui");
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("rebuild", true);
  this.gui.handleDataFromContainer(tag);
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
 
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return null;
  }

}
