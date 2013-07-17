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
package shadowmage.ancient_warfare.common.plugins.bc;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.machine.TEWorkerMotor;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerProvider;
import buildcraft.api.transport.IPipeConnection;

public class TEWorkerMotorBC extends TEWorkerMotor
{

IPowerReceptor receptor = null;

/**
 * 
 */
public TEWorkerMotorBC()
  {
  this.canUpdate = true;
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
    /**
     * TODO
     */
  }

@Override
public boolean hasWork()
  {
  return this.receptor!=null && this.receptor.getPowerProvider()!=null && receptor.getPowerProvider().getEnergyStored() < receptor.getPowerProvider().getMaxEnergyStored() && this.receptor.getPowerProvider().isPowerSource(facingDirection.getOpposite());
  }

@Override
public void doWork(IWorker worker)
  {
  if(worker==this.worker && worker!=null && this.receptor!=null && this.receptor.getPowerProvider()!=null && this.receptor.getPowerProvider().isPowerSource(facingDirection.getOpposite()))
    {
    this.receptor.getPowerProvider().receiveEnergy(40, facingDirection.getOpposite());
    Config.logDebug("doing worker motor-work!!");
    }
  }

}
