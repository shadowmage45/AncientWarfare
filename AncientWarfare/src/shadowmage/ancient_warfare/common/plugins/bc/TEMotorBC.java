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
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.ITEWorkSite;
import shadowmage.ancient_warfare.common.machine.TEWorkMotor;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;

public class TEMotorBC extends TEWorkMotor implements IPowerReceptor
{

IPowerProvider provider;
/**
 * 
 */
public TEMotorBC()
  {
  provider = PowerFramework.currentFramework.createPowerProvider();
  this.canUpdate = true;  
  this.initPowerProvider();
  }

private void initPowerProvider() 
  {
  provider.configure(20, 1, 8, 10, 100);
  provider.configurePowerPerdition(1, 100);
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

@Override
public void updateEntity()
  {
  super.updateEntity();
  if(this.worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  Config.logDebug("energy stored: " + this.provider.getEnergyStored());
  if(this.provider.getEnergyStored()>=this.provider.getActivationEnergy())
    {    
    if(this.workSite!=null && this.workSite.hasWork())
      {
      Config.logDebug("using energey: "+this.provider.getActivationEnergy());
      this.provider.useEnergy(this.provider.getActivationEnergy(), this.provider.getActivationEnergy(), true);
      this.workSite.doWork(this);
      }
    }
  if(this.workSite==null)
    {
    int x = xCoord;
    int y = yCoord;
    int z = zCoord;
    ForgeDirection d = this.facingDirection;
    x += d.offsetX;
    z += d.offsetZ;   
    Config.logDebug(String.format("checking block %s, %s, %s for work site", x,y,z));
    TileEntity e = worldObj.getBlockTileEntity(x, y, z);
    if(e instanceof ITEWorkSite)
      {
      ITEWorkSite workSite = (ITEWorkSite)e;
      if(workSite.canHaveMoreWorkers(this))
        {
        this.workSite = (ITEWorkSite)e;
        Config.logDebug("found work site!!");        
        }
      }
    }
  }



}
