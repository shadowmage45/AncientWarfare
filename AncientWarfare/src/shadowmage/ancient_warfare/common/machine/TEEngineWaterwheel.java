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
import net.minecraft.block.Block;

public class TEEngineWaterwheel extends TEEngine
{

public boolean displayWheel;
protected int wheelUpdateTicks = 0;
protected int waterBlocks = 0;
protected float wheelRotation = 0;
protected float wheelSpeed = 0;

public TEEngineWaterwheel()
  {
  this.canPointVertical = false;
  }

public float getWheelRotation()
  {
  return this.wheelRotation;
  }

public float getWheelSpeed()
  {
  return this.wheelSpeed;
  }

@Override
public void updateEntity()
  {
  super.updateEntity();
  if(this.displayWheel)
    {
    this.wheelRotation++;
    this.wheelSpeed = 1;
    }
  else
    {
    this.wheelSpeed = 0;
    }
  if(this.worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  if(this.wheelUpdateTicks>0)
    {
    this.wheelUpdateTicks--;
    }
  else
    {    
    this.wheelUpdateTicks = 5;
    int x = this.xCoord - this.facingDirection.offsetX;
    int y = this.yCoord - this.facingDirection.offsetY;
    int z = this.zCoord - this.facingDirection.offsetZ;
    int wheelX = 0;
    int wheelZ = 0;
    if(this.facingDirection.offsetX==0)
      {
      wheelX = 1;
      }
    else
      {
      wheelZ = 1;
      }
    int id;
    boolean displayWheel = true;
    this.waterBlocks = 0;
    outerLoop://finally..a legitimate use of a label statment...
    for(int by = y-1; by <= y+1; by++)
      {
      for(int bz = z - wheelZ; bz <= z+wheelZ; bz++)
        {
        for(int bx = x - wheelX; bx <= x+wheelX; bx++)
          {
          id = this.worldObj.getBlockId(bx, by, bz);
          if(id!=Block.waterMoving.blockID && id!=Block.waterStill.blockID && !this.worldObj.isAirBlock(bx, by, bz))
            {
            displayWheel = false;
            break outerLoop;//break completely out of the loop, no need to continue
            }
          else if(id!=Block.waterMoving.blockID && id!=Block.waterStill.blockID)
            {
            waterBlocks++;
            }
          }        
        }      
      }
    this.setDisplayWheel(displayWheel);
    }  
  }

protected void setDisplayWheel(boolean present)
  {
  if(!this.worldObj.isRemote && present!=this.displayWheel)
    {
    this.worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockLoader.engineBlock.blockID, 1, present? 1 : 0);
    this.displayWheel = present;
    }
  else if(this.worldObj.isRemote)
    {
    this.displayWheel = present;
    }
  }

@Override
public boolean receiveClientEvent(int par1, int par2)
  {
  boolean flag = super.receiveClientEvent(par1, par2);
  if(par1==1)
    {
    this.setDisplayWheel(par2==1);
    }
  return flag || true;  
  }



}
