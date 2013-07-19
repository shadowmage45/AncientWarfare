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

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.power.PowerProvider;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.ISolidSideTile;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.machine.TEEngineWaterwheel;

public class TEEngineWaterwheelBC extends TEEngineWaterwheel  implements IPipeConnection, ISolidSideTile, IPowerReceptor
{

IPowerProvider outputTarget = null;
IPowerProvider internalBuffer = null;
/**
 * 
 */
public TEEngineWaterwheelBC()
  {
  internalBuffer = PowerFramework.currentFramework.createPowerProvider();
  this.canUpdate = true;
  this.initPowerProvider();
  }

private void initPowerProvider() 
  {
  internalBuffer.configure(20, 1, 200, Config.npcWorkMJ, Config.npcWorkMJ*3);
  internalBuffer.configurePowerPerdition(1, 100);
  }

@Override
public void updateEntity()
  {
  super.updateEntity();
  if(this.worldObj==null || this.worldObj.isRemote || this.internalBuffer==null)
    {
    return;
    }  
  if(this.displayWheel)
    {
    int blocks = this.waterBlocks > 3 ? 3 : this.waterBlocks;
    this.internalBuffer.receiveEnergy(blocks, facingDirection.getOpposite());
    }
  int x = xCoord + facingDirection.offsetX;
  int y = yCoord + facingDirection.offsetY;
  int z = zCoord + facingDirection.offsetZ;
  TileEntity tile = worldObj.getBlockTileEntity(x, y, z);  
  if(isPoweredTile(tile))
    {    
    this.outputTarget = ((IPowerReceptor)tile).getPowerProvider(); 
    if(this.outputTarget!=null)
      {  
      int toMove = (int) (this.internalBuffer!=null ? this.internalBuffer.getEnergyStored() : 0);
      if(toMove>this.internalBuffer.getActivationEnergy())
        {
        toMove = outputTarget.getMaxEnergyReceived() < toMove ? outputTarget.getMaxEnergyReceived() : toMove;
        this.setIsWorking(true);
        this.outputTarget.receiveEnergy(this.internalBuffer.useEnergy(0, toMove, true), facingDirection.getOpposite());   
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
    IPowerProvider receptor = ((IPowerReceptor) tile).getPowerProvider();
    return receptor != null && receptor.getClass().getSuperclass().equals(PowerProvider.class);
    }
  return false;
  }

@Override
public boolean isSolidOnSide(ForgeDirection side)
  {
  return true;
  }

@Override
public boolean isPipeConnected(ForgeDirection with)
  {
  return with != facingDirection.getOpposite();
  }

@Override
public void setPowerProvider(IPowerProvider provider)
  {
  this.internalBuffer = provider;
  }

@Override
public IPowerProvider getPowerProvider()
  {
  return this.internalBuffer;
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
