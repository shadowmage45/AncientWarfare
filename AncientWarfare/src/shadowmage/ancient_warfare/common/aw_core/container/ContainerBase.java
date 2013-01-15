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
package shadowmage.ancient_warfare.common.aw_core.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.aw_core.AWCore;
import shadowmage.ancient_warfare.common.aw_core.network.IHandlePacketData;
import shadowmage.ancient_warfare.common.aw_core.network.Packet03GuiInput;
import shadowmage.ancient_warfare.common.aw_core.utils.IContainerGUICallback;
import shadowmage.ancient_warfare.common.aw_core.utils.IMultipleCrafterCallback;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ContainerBase extends Container implements IHandlePacketData
{

protected final EntityPlayer player;

/**
 * if opened client-side, will contain an instance of the GUI
 */
protected final IContainerGUICallback gui;

/**
 * if opened server side, might contain an instance of the base tile/entity
 * this entity should maintain a list of interacting players, so as to only
 * need to update client-side info for players actively viewing the container
 */
protected final IMultipleCrafterCallback multiCrafters;

/**
 * 
 * @param player thePlayer opening the GUI that uses this container
 * @param gui an instance of a GUI implementing the callback, NULL on server side
 * @param crafter a reference to the base entity--NULL on client side.  MAY be NULL on server side.
 */
public ContainerBase(EntityPlayer player, IContainerGUICallback gui, IMultipleCrafterCallback crafters)
  {
  this.player = player;  
  this.gui = gui;
  this.multiCrafters = crafters;
  
  if(!player.worldObj.isRemote)
    {
    if(this.multiCrafters!=null)
      {
      this.multiCrafters.addPlayerToList(player);
      }    
    NBTTagCompound tag = this.getInitData();
    if(tag!=null)
      {
      sendPacketToViewingPlayer(tag);
      }
    }  
  }

public final EntityPlayer getPlayer()
  {
  return this.player;
  }


@Override
public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
  {
  super.onCraftGuiClosed(par1EntityPlayer);
  /**
   * if on server side, and has multi-crafter container, remove player from
   * tracking list
   */
  if(!par1EntityPlayer.worldObj.isRemote && this.multiCrafters!=null)
    {
    this.multiCrafters.removePlayer(player);
    }
  }

@Override
public boolean canInteractWith(EntityPlayer var1)
  {
  return true;
  }

/**
 * params are tunc. to shorts before sent...max value ~16k
 * @param a
 * @param b
 */
public void sendUpdateToClient(int a, int b)
  {
  if(!this.player.worldObj.isRemote && this.player instanceof EntityPlayerMP )
    {
    ((EntityPlayerMP)player).sendProgressBarUpdate(this, a, b);
    }
  }

@SideOnly(Side.CLIENT)
public void updateProgressBar(int par1, int par2)
  {
  super.updateProgressBar(par1, par2);
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(this.player.worldObj.isRemote)
    {
    this.handleUpdateClient(tag);
    }
  else
    {    
    if(this.multiCrafters!=null)
      {
      /**
       * if handled by the entity completely, exit, else pass same data onto
       * container update method
       */
      if(this.multiCrafters.handleUpdate(tag))
        {
        return;          
        }
      }
    this.handleUpdateServer(tag);    
    }
  }

/**
 * individual container classes should implement this to handle information sent from the server to
 * this container client-side (complex information updates)
 * @param tag
 */
public abstract void handleUpdateClient(NBTTagCompound tag);

/**
 * individual container classes should implement this to handle information sent from the client to
 * this container on the server
 * @param tag
 */
public abstract void handleUpdateServer(NBTTagCompound tag);

/**
 * when the container is first opened server side, this data will be sent to the client to populate the
 * open containers fields
 * @return
 */
public abstract NBTTagCompound getInitData();

public void sendPacketToViewingPlayer(NBTTagCompound tag)
  {
  Packet03GuiInput pkt = new Packet03GuiInput();
  pkt.setData(tag);
  AWCore.proxy.sendPacketToPlayer(player, pkt);
  }

public void sendDataToServer(NBTTagCompound tag)
  {
  Packet03GuiInput pkt = new Packet03GuiInput();
  pkt.setData(tag);
  AWCore.proxy.sendPacketToServer(pkt);
  }

}
