/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IInteractable;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public abstract class TEMachine extends TileEntity implements IInteractable
{

protected int teamNumber = 0;
protected int guiNumber = -1;
protected boolean canUpdate = false;
protected boolean shouldWriteInventory = true;
protected boolean hasSpecialModel = false;
public boolean canPointVertical = false;
public boolean facesOpposite = false;
public boolean isActivated = false;
public boolean canActivate = false;

protected ForgeDirection facingDirection = ForgeDirection.SOUTH;

public void onBlockBreak()
  {
  
  }

public void setDirection(ForgeDirection direction)
  {
  this.facingDirection = direction;
  }

public ForgeDirection getFacing()
  {
  return this.facingDirection;
  }

public void rotate(ForgeDirection axis)
  {
  if(!this.canPointVertical)
    {
    this.setDirection(BlockTools.getRight(facingDirection));
    }
  else
    {
    this.setDirection(facingDirection.getRotation(axis));
    }
  this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
  }

protected void setActivated(boolean act)
  {
  if(!this.canActivate || this.worldObj==null || this.worldObj.isRemote){return;}
  if(act!=this.isActivated)
    {    
    this.worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockLoader.machineBlock.blockID, 100, act ? 1 : 0);
    }
  }

@Override
public boolean receiveClientEvent(int par1, int par2)
  {
  boolean flag = super.receiveClientEvent(par1, par2);  
  boolean flag2 = false;
  if(par1==100)
    {    
    this.isActivated = par2==1;
    Config.logDebug("set isWorking to: "+this.isActivated+ " on "+ (this.worldObj.isRemote? "Client" : "Server"));
    this.worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    flag2 = true;
    }
  return flag || flag2;
  }

public void onBlockPlaced()
  {
  
  }

public void onBlockNeighborChanged()
  {
  
  }

public void setTeamNum(int num)
  {
  this.teamNumber = num;
  }

public int getTeamNum()
  {
  return teamNumber;
  }

public boolean isHostile(int sourceTeam)
  {
  if(this.worldObj==null)
    {
    return false;
    }
  return TeamTracker.instance().isHostileTowards(worldObj, sourceTeam, teamNumber);
  }

public boolean canTeamInteract(int sourceTeam)
  {
  if(this.worldObj==null)
    {
    return false;
    }
  return !TeamTracker.instance().isHostileTowards(worldObj, teamNumber, sourceTeam);
  }

@Override
public boolean canUpdate()
  {
  return canUpdate;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.facingDirection = ForgeDirection.getOrientation(tag.getByte("face"));
  if(this instanceof IInventory)
    {
    InventoryTools.readInventoryFromTag((IInventory)this, tag.getCompoundTag("inventory"));
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setByte("face", (byte) this.facingDirection.ordinal());
  if(this instanceof IInventory && this.shouldWriteInventory)
    {
    tag.setCompoundTag("inventory",InventoryTools.getTagForInventory((IInventory)this));
    }
  }

@Override
public Packet getDescriptionPacket()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("face", (byte) this.facingDirection.ordinal());
  Packet132TileEntityData pkt = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
  return pkt;
  }

@Override
public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
  if(pkt.customParam1.hasKey("face"))
    {
    this.facingDirection = ForgeDirection.getOrientation(pkt.customParam1.getByte("face"));
    }
  super.onDataPacket(net, pkt);
  }

@Override
public void onPlayerInteract(EntityPlayer player)
  {
  if(!player.worldObj.isRemote && this.guiNumber>=0)
    {
    this.openGui(player);
    }
  }

public void openGui(EntityPlayer player)
  {
  if(this.guiNumber>=0)
    {
    GUIHandler.instance().openGUI(guiNumber, player, player.worldObj, xCoord, yCoord, zCoord);    
    }
  }
}
