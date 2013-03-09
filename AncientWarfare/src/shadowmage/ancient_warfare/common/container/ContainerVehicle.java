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
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.interfaces.IEntityContainerSynch;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class ContainerVehicle extends ContainerBase
{

public VehicleBase vehicle;
public Slot[] playerSlots = new Slot[27];


/**
 * @param openingPlayer
 * @param synch
 */
public ContainerVehicle(EntityPlayer openingPlayer,  IEntityContainerSynch synch, VehicleBase vehicle)
  {
  super(openingPlayer, synch);
  this.vehicle = vehicle;

  int y;
  int x;
  int slotNum;
  int xPos; 
  int yPos;
  for (x = 0; x < 9; ++x)//add player hotbar slots
    {
    slotNum = x;
    xPos = 8 + x * 18;
    yPos = 142;
    this.addSlotToContainer(new Slot(openingPlayer.inventory, x, 8 + x * 18, 142));
    }
  for (y = 0; y < 3; ++y)
    {
    for (x = 0; x < 9; ++x)
      {
      slotNum = y*9 + x + 9;// +9 is to increment past hotbar slots
      xPos = 8 + x * 18;
      yPos = 84 + y * 18;
      this.addSlotToContainer(new Slot(openingPlayer.inventory, slotNum, xPos, yPos));
      }
    }
  for (y = 0; y < 2; y++)
    {
    for(x = 0; x <3 ;x++)
      {
      slotNum = y*3 + x;
      xPos = 8 + x * 18;
      yPos = 84 + y * 18 - 2*18 - 8;
      this.addSlotToContainer(new Slot(vehicle.inventory.ammoInventory, slotNum, xPos, yPos));
      }
    }  
  for (y = 0; y < 2; y++)
    {
    for(x = 0; x <3 ;x++)
      {
      slotNum = y*3 + x;
      xPos = 8 + x * 18 + 3*18 + 8;
      yPos = 84 + y * 18 - 2*18 - 8;
      this.addSlotToContainer(new Slot(vehicle.inventory.upgradeInventory, slotNum, xPos, yPos));
      }
    }  
  
 
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
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
