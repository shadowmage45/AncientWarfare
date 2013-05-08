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
package shadowmage.ancient_warfare.common.civics.worksite.te.courier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.TECivicTownHall;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class TECivicCourier extends TECivic
{

WayPoint controllerPoint;
TECivicTownHall controller;

/**
 * 
 */
public TECivicCourier()
  {
  // TODO Auto-generated constructor stub
  }

@Override
public boolean onInteract(World world, EntityPlayer player)
  {
  if(!world.isRemote && inventory.getSizeInventory()>0)
    {
    GUIHandler.instance().openGUI(GUIHandler.CIVIC_BASE, player, world, xCoord, yCoord, zCoord);
    }
  return true;
  }

@Override
public void updateEntity()
  {
  super.updateEntity();
  if(worldObj!=null && !worldObj.isRemote)
    {
    if(controller!=null)
      {
      if(worldObj.checkChunksExist(controller.xCoord, controller.yCoord, controller.zCoord, controller.xCoord, controller.yCoord, controller.zCoord))
        {
        if(worldObj.getBlockTileEntity(controller.xCoord, controller.yCoord, controller.zCoord)!=controller)
          {
          controller = null;
          controllerPoint = null;
          }
        }
      }
    if(controller!=null && controllerPoint==null)
      {
      controllerPoint = new WayPoint(controller.xCoord, controller.yCoord, controller.zCoord, TargetType.WORK);
      } 
    if(controller==null && controllerPoint!=null)
      {
      if(worldObj.checkChunksExist(controllerPoint.floorX(), controllerPoint.floorY(), controllerPoint.floorZ(),controllerPoint.floorX(), controllerPoint.floorY(), controllerPoint.floorZ()))
        {
        TileEntity te = worldObj.getBlockTileEntity(controllerPoint.floorX(),controllerPoint.floorY(), controllerPoint.floorZ());
        if(te instanceof TECivicTownHall)
          {
          controller = (TECivicTownHall)te;
          }
        else
          {
          controller = null;
          controllerPoint = null;
          }
        }      
      }
    }
  }

public void setController(TECivicTownHall controller)
  {
  this.controller = controller;
  this.controllerPoint = new WayPoint(controller.xCoord, controller.yCoord, controller.zCoord, TargetType.WORK);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  if(tag.hasKey("controlPoint"))
    {
    this.controllerPoint = new WayPoint(tag.getCompoundTag("controlPoint"));
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  if(this.controllerPoint!=null)
    {
    tag.setCompoundTag("controlPoint", this.controllerPoint.getNBTTag());
    }
  }

}
