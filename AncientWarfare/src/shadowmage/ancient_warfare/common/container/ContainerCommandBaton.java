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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IEntityContainerSynch;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.item.ItemNpcCommandBaton;
import shadowmage.ancient_warfare.common.item.ItemNpcCommandBaton.BatonSettings;

public class ContainerCommandBaton extends ContainerBase
{

public BatonSettings settings;
public int batonRank = 0;
/**
 * @param openingPlayer
 * @param synch
 */
public ContainerCommandBaton(EntityPlayer openingPlayer)
  {
  super(openingPlayer, null);
  ItemStack stack = openingPlayer.getCurrentEquippedItem();
  if(stack!=null)
    {
    settings = ItemNpcCommandBaton.getBatonSettingsStatic(stack);
    batonRank = stack.getItemDamage();
    }  
  }

/**
 * client side-method to send changes to server
 */
public void saveSettings()
  {
  ItemStack stack = player.getCurrentEquippedItem();
  if(stack!=null && stack.itemID == ItemLoader.instance().npcCommandBaton.itemID)
    {
    ItemNpcCommandBaton.setBatonSettings(stack, settings);
    player.openContainer.detectAndSendChanges();
    Config.logDebug("sending changes to server!!");
    }
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return null;
  }

}
