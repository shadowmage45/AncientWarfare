/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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

import shadowmage.ancient_warfare.common.config.Config;
import net.minecraft.nbt.NBTTagCompound;

public class TEGateLock extends TEMachine
{

boolean isLocked;

/**
 * 
 */
public TEGateLock()
  {
  this.canUpdate = true;
  this.facesOpposite = true;
  }

public boolean isLocked()
  {
  return this.isLocked;
  }

@Override
public void onBlockNeighborChanged()
  {
  this.isLocked = false;
  int bx,by,bz;
  bx = xCoord + facingDirection.offsetX;
  by = yCoord + facingDirection.offsetY;
  bz = zCoord + facingDirection.offsetZ;
  Config.logDebug("checking lock block status...facing direction: "+facingDirection + " @"+xCoord+","+yCoord+","+zCoord);
  Config.logDebug("checking block: "+bx+","+by+","+bz+" for power.");
  int p = this.worldObj.isBlockProvidingPowerTo(bx, by, bz, facingDirection.ordinal());
  int p1 = this.worldObj.getIndirectPowerLevelTo(bx, by, bz, facingDirection.ordinal());
  boolean p2 = this.worldObj.getIndirectPowerOutput(bx, by, bz, facingDirection.ordinal());
  
  Config.logDebug(String.format("status: %s, %s, %s", p, p1, p2));
    
  if(this.worldObj.getIndirectPowerOutput(bx, by, bz, facingDirection.ordinal()))
    {
    Config.logDebug("setting lock block to locked!!");
    this.isLocked = true;
    }
  }

/********************************DATA SYNCH METHODS*******************************************/
@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.isLocked = tag.getBoolean("locked");
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setBoolean("locked", this.isLocked);
  }

}
