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
package shadowmage.ancient_warfare.common.aw_core.utils;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IMultipleCrafterCallback
{

/**
 * get a list of players viewing this container
 * internally, this list should use weakReferences and cull null references before
 * adding to a normal reference list and returning to caller 
 * @return
 */
public List<EntityPlayer> getPlayersViewingContainer();


public void addPlayerToList(EntityPlayer player);
public void removePlayer(EntityPlayer player);

/**
 * return true to cancel further processing (i.e. data only needs go to TE or Entity),
 * return false to continue passing data onto underlying containing container(i.e. container needs data as well (local update))
 * @param tag
 * @return
 */
public boolean handleUpdate(NBTTagCompound tag);

}
