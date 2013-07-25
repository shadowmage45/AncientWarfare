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

import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.config.Config;

public class TEEngine extends TEMachine
{

protected byte pistonDirection = 0;
public boolean isWorking = false;
protected float pistonProgress = 0.f;

public TEEngine()
  {
  this.canPointVertical = true;
  this.canUpdate = true;
  }

public String getTexture()
  {
  return EngineData.getEngineTexture(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
  }

@Override
public void updateEntity()
  {
  super.updateEntity();
  this.updatePistonState();
  }

protected void updatePistonState()
  {
  if(this.isWorking && this.pistonProgress <=0)
    {
    this.isWorking = false;
    this.pistonDirection = 1;
    }
  if(this.pistonDirection!=0)
    {
    this.pistonProgress+=this.pistonDirection*0.8f;
    }
//  else if(this.pistonProgress==8.f)
//    {
//    this.pistonDirection = -1;
//    }
  if(this.pistonProgress>=8)
    {
    this.pistonDirection = -1;
    }
  else if(this.pistonProgress<=0)
    {
    this.pistonProgress = 0.f;
    }
  
  }

public byte getPistonDirection()
  {
  return this.pistonDirection;
  }

public float getPistonProgress()
  {
  return this.pistonProgress;      
  }

protected void setIsWorking(boolean work)
  {
  if(this.worldObj!=null && work!=this.isWorking)
    {
    if(!isWorking && pistonProgress==0)
      {
      this.pistonDirection = 1;
      this.worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockLoader.engineBlock.blockID, 0, work? 1 : 0);
      this.isWorking = work;
      }
    }
  }

@Override
public boolean receiveClientEvent(int par1, int par2)
  {
  if(par1==0)
    {    
    this.isWorking = par2==1;
    }
  return true;
  }

}
