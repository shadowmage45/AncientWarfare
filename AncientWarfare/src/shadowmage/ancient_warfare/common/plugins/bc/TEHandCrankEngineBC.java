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
package shadowmage.ancient_warfare.common.plugins.bc;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.machine.TEHandCrankEngine;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

public class TEHandCrankEngineBC extends TEHandCrankEngine implements IPowerReceptor, IPowerEmitter
{


protected PowerHandler powerHandler;
protected PowerReceiver outputTarget;
int broadcastDelayTicks = 0;
protected boolean broadcastWork = true;
/**
 * 
 */
public TEHandCrankEngineBC()
  {
  this.canUpdate = true;
  powerHandler = new PowerHandler(this, Type.ENGINE);
  powerHandler.configurePowerPerdition(1, 100);
  powerHandler.configure(0, 75, Config.npcWorkMJ, 1000);
  }

@Override
public void updateEntity()
  {
  super.updateEntity(); 
  if(worldObj==null || worldObj.isRemote){return;} 
  int x = xCoord + facingDirection.offsetX;
  int y = yCoord + facingDirection.offsetY;
  int z = zCoord + facingDirection.offsetZ;
  this.broadcastWork(Config.npcAISearchRange);
  TileEntity tile = worldObj.getBlockTileEntity(x, y, z);  
  if(isPoweredTile(tile))
    {    
    this.outputTarget = ((IPowerReceptor)tile).getPowerReceiver(this.facingDirection.getOpposite()); 
    if(this.outputTarget!=null)
      {  
      float toMove = this.powerHandler.getEnergyStored();
      if(toMove>=this.powerHandler.getActivationEnergy())
        {
        toMove = outputTarget.getMaxEnergyReceived() < toMove ? outputTarget.getMaxEnergyReceived() : toMove;
        this.setIsWorking(true);
        toMove = this.powerHandler.useEnergy(toMove, toMove, true);
        toMove = this.outputTarget.receiveEnergy(Type.ENGINE, toMove, facingDirection.getOpposite());   
        }
      else
        {
        this.setIsWorking(false);
        }
      }
    else
      {
      this.setIsWorking(false);
      }
    }
  else
    {
    this.outputTarget = null;
    this.setIsWorking(false);
    }  
  }

public boolean isPoweredTile(TileEntity tile) 
  {
  if (tile instanceof IPowerReceptor) 
    {
    PowerReceiver receptor = ((IPowerReceptor) tile).getPowerReceiver(facingDirection.getOpposite());
    return receptor != null;
    }
  return false;
  }
  
@Override
public void broadcastWork(int maxRange)
  {
  if(!this.broadcastWork)
    {
    return;
    }
  if(this.broadcastDelayTicks>0)
    {
    this.broadcastDelayTicks--;
    return;
    }
  this.broadcastDelayTicks = Config.npcAITicks * 10;
  if(!this.hasWork())
    {
    return;
    }
  if(this.worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1).expand(maxRange, maxRange/2, maxRange);
  List<NpcBase> npcList = worldObj.getEntitiesWithinAABB(NpcBase.class, bb);
  for(NpcBase npc : npcList)
    {
    if(isHostile(npc.teamNum))      
      {
      if(npc.npcType.isCombatUnit())
        {
        npc.handleTileEntityTargetBroadcast(this, TargetType.ATTACK_TILE, Config.npcAITicks*11);
        }      
      }
    else
      {
      if(canHaveMoreWorkers(npc) && npc.npcType.getWorkTypes(npc.rank).contains(this.getWorkType()) && npc.teamNum==this.teamNumber)
        {
        npc.handleTileEntityTargetBroadcast(this, TargetType.WORK, Config.npcAITicks*11);
        }
      }
    }
  }

@Override
public boolean hasWork()
  {
  return this.powerHandler!=null && this.powerHandler.getEnergyStored() < this.powerHandler.getMaxEnergyStored();
  }

@Override
public void doWork(IWorker worker)
  {
  if(this.powerHandler!=null && this.powerHandler.getEnergyStored() < this.powerHandler.getMaxEnergyStored())
    {
    this.powerHandler.addEnergy(Config.npcWorkMJ);
    }
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  NBTTagCompound powerTag = new NBTTagCompound();
  this.powerHandler.writeToNBT(powerTag);
  tag.setCompoundTag("power", powerTag);
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  if(tag.hasKey("power"))
    {
    NBTTagCompound powerTag = tag.getCompoundTag("power");
    this.powerHandler.readFromNBT(powerTag);
    }
  }

@Override
public boolean canEmitPowerFrom(ForgeDirection side)
  {
  return side==this.facingDirection;
  }

@Override
public PowerReceiver getPowerReceiver(ForgeDirection side)
  {
  return side==this.facingDirection ? null : this.powerHandler.getPowerReceiver();
  }

@Override
public void doWork(PowerHandler workProvider)
  {
  
  }

@Override
public World getWorld()
  {
  return worldObj;
  }
}
