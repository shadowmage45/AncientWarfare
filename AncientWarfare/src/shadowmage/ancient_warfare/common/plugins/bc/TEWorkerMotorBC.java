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

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.machine.TEWorkerMotor;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.power.PowerProvider;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.ISolidSideTile;

public class TEWorkerMotorBC extends TEWorkerMotor implements IPipeConnection, ISolidSideTile, IPowerReceptor
{

IPowerReceptor receptor = null;
IPowerProvider provider = null;
/**
 * 
 */
public TEWorkerMotorBC()
  {
  provider = PowerFramework.currentFramework.createPowerProvider();
  this.canUpdate = true;
  this.initPowerProvider();
  }

private void initPowerProvider() 
  {
  provider.configure(20, 1, 2, Config.npcWorkMJ, Config.npcWorkMJ);
  provider.configurePowerPerdition(1, 100);
  }

@Override
public void updateEntity()
  {
  super.updateEntity();
  if(worldObj==null || worldObj.isRemote){return;} 
  int x = xCoord + facingDirection.offsetX;
  int y = yCoord + facingDirection.offsetY;
  int z = zCoord + facingDirection.offsetZ;
  TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
  if(isPoweredTile(tile))
    {
    this.receptor = (IPowerReceptor)tile; 
    if(this.receptor!=null && this.receptor.getPowerProvider()!=null)// && this.receptor.getPowerProvider().isPowerSource(facingDirection)
      {      
      int toMove = (int) (this.provider!=null ? this.provider.getEnergyStored() : 0);
      toMove = receptor.getPowerProvider().getMaxEnergyReceived() > toMove ? receptor.getPowerProvider().getMaxEnergyReceived() : toMove;
      this.receptor.getPowerProvider().receiveEnergy(this.provider.useEnergy(0, toMove, true), facingDirection.getOpposite());      
      }
    }
  else
    {
    this.receptor = null;
    }
  }

public boolean isPoweredTile(TileEntity tile) 
  {
  if (tile instanceof IPowerReceptor) 
    {
    IPowerProvider receptor = ((IPowerReceptor) tile).getPowerProvider();
    return receptor != null && receptor.getClass().getSuperclass().equals(PowerProvider.class);
    }
  return false;
  }

@Override
public void broadcastWork(int maxRange)
  {
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
        npc.targetHelper.handleTileEntityTargetBroadcast(this, TargetType.ATTACK_TILE, Config.npcAITicks*11);
        }      
      }
    else
      {
      if(broadcastWork)
        {    
        if(hasWork() && canHaveMoreWorkers(npc) && npc.npcType.getWorkTypes(npc.rank).contains(this.getWorkType()) && npc.teamNum==this.teamNumber)
          {
          npc.targetHelper.handleTileEntityTargetBroadcast(this, TargetType.WORK, Config.npcAITicks*11);
          }
        }
      }
    }
  }

@Override
public boolean hasWork()
  {
  return this.provider!=null && this.provider.getEnergyStored() < this.provider.getMaxEnergyStored();
//  return this.receptor!=null && this.receptor.getPowerProvider()!=null && receptor.getPowerProvider().getEnergyStored() < receptor.getPowerProvider().getMaxEnergyStored() ;//&& this.receptor.getPowerProvider().isPowerSource(facingDirection)
  }

@Override
public void doWork(IWorker worker)
  {
  if(this.provider!=null && this.provider.getEnergyStored() < this.provider.getMaxEnergyStored())
    {
    this.provider.receiveEnergy(Config.npcWorkMJ, facingDirection);
    }
  }

@Override
public boolean isSolidOnSide(ForgeDirection side)
  {
  return true;
  }

@Override
public boolean isPipeConnected(ForgeDirection with)
  {
  return with == facingDirection.getOpposite();
  }

@Override
public void setPowerProvider(IPowerProvider provider)
  {
  this.provider = provider;
  }

@Override
public IPowerProvider getPowerProvider()
  {
  return this.provider;
  }

@Override
public void doWork()
  {
  // TODO Auto-generated method stub  
  }

@Override
public int powerRequest(ForgeDirection from)
  {
  IPowerProvider p = getPowerProvider();
  float needed = p.getMaxEnergyStored() - p.getEnergyStored();
  return (int) Math.ceil(Math.min(p.getMaxEnergyReceived(), needed));  
  }
}
