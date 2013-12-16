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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.interfaces.ITEWorkSite;
import shadowmage.ancient_warfare.common.machine.TEMechanicalWorker;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

public class TEMechanicalWorkerBC extends TEMechanicalWorker implements IPowerReceptor
{

PowerHandler provider;
/**
 * 
 */
public TEMechanicalWorkerBC()
  {
  provider = new PowerHandler(this, Type.MACHINE);
  provider.configurePowerPerdition(1, 100);
  provider.configure(2, 25, 70, 210);
  this.canUpdate = true;
  }

//@Override
//public int powerRequest(ForgeDirection from)
//  {
//  IPowerProvider p = getPowerProvider();
//  float needed = p.getMaxEnergyStored() - p.getEnergyStored();
//  return (int) Math.ceil(Math.min(p.getMaxEnergyReceived(), needed));  
//  }

@Override
public void updateEntity()
  {
  super.updateEntity();
  if(this.worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  if(this.provider.getEnergyStored()>=this.provider.getActivationEnergy())
    {    
    if(this.workSite!=null && this.workSite.hasWork())
      {
//      Config.logDebug("using energy: "+this.provider.getActivationEnergy());
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
//    Config.logDebug(String.format("checking block %s, %s, %s for work site", x,y,z));
    TileEntity e = worldObj.getBlockTileEntity(x, y, z);
    if(e instanceof ITEWorkSite)
      {
      ITEWorkSite workSite = (ITEWorkSite)e;
      if(workSite.canHaveMoreWorkers(this))
        {
        this.workSite = (ITEWorkSite)e;
//        Config.logDebug("found work site!!");        
        }
      }
    }
  }


@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  NBTTagCompound powerTag = new NBTTagCompound();
  this.provider.writeToNBT(powerTag);
  tag.setCompoundTag("power", powerTag);
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  if(tag.hasKey("power"))
    {
    NBTTagCompound powerTag = tag.getCompoundTag("power");
    this.provider.readFromNBT(powerTag);
    }
  }

@Override
public PowerReceiver getPowerReceiver(ForgeDirection side)
  {
  return provider.getPowerReceiver();
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
