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
package shadowmage.ancient_warfare.common.aw_core.tracker;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.aw_core.tracker.entry.PlayerEntry;
import shadowmage.ancient_warfare.common.aw_core.utils.INBTTaggable;
import cpw.mods.fml.common.IPlayerTracker;

/**
 * server side player tracker
 * @author Shadowmage
 *
 */
public class PlayerTracker implements IPlayerTracker, INBTTaggable
{
private PlayerTracker(){}
public static PlayerTracker instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new PlayerTracker();
    }
  return INSTANCE;
  }
private static PlayerTracker INSTANCE;

/**
 * server-side list of all player entries
 */
private Map<String, PlayerEntry> playerEntries = new HashMap<String, PlayerEntry>();

/**
 * player entry used by thePlayer client-side
 */
private PlayerEntry clientEntry = new PlayerEntry();

@Override
public void onPlayerLogin(EntityPlayer player)
  {
  if(!playerEntries.containsKey(player.getEntityName()))
    {
    
    }  
  }

@Override
public void onPlayerLogout(EntityPlayer player)
  {
  // TODO Auto-generated method stub

  }

@Override
public void onPlayerChangedDimension(EntityPlayer player)
  {
  // TODO Auto-generated method stub

  }

@Override
public void onPlayerRespawn(EntityPlayer player)
  {
  // TODO Auto-generated method stub

  }
@Override
public NBTTagCompound getNBTTag()
  {
  // TODO Auto-generated method stub
  return null;
  }
@Override
public void readFromNBT(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
  
  }

}
