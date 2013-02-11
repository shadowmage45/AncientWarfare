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
package shadowmage.ancient_warfare.common.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.build.Builder;
import shadowmage.ancient_warfare.common.structures.build.BuilderTicked;

public class TEBuilder extends TileEntity
{

private BuilderTicked builder;

private boolean shouldRemove = false;

private Ticket tk = null;

public void setTicket(Ticket tk)
  {
  
  }

public void removeBuilder()
  {
  this.invalidate();
  this.worldObj.setBlock(xCoord, yCoord, zCoord, 0);
  if(this.tk!=null)
    {
    ForgeChunkManager.releaseTicket(tk);
    this.tk = null;
    }
  }

@Override
public void readFromNBT(NBTTagCompound par1nbtTagCompound)
  {
  super.readFromNBT(par1nbtTagCompound);
  if(par1nbtTagCompound.hasKey("builder"))
    {
    System.out.println("reading builder data");
    NBTTagCompound builder = par1nbtTagCompound.getCompoundTag("builder");
    this.builder = Builder.readTickedBuilderFromNBT(builder);
    if(this.builder==null)
      {
      this.shouldRemove = true;
      }
    }  
  }

@Override
public void writeToNBT(NBTTagCompound par1nbtTagCompound)
  {
  super.writeToNBT(par1nbtTagCompound);
  if(this.builder!=null)
    {
    par1nbtTagCompound.setCompoundTag("builder", this.builder.getNBTTag());
    }
  }

@Override
public void updateEntity()
  {
  super.updateEntity();
  if(worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  if(builder==null)
    {
    Config.logError("Invalid builder in TE detected in builder block");
    return;
    }
  if(this.shouldRemove)
    {    
    this.removeBuilder();
    return;
    }  
  if(builder.world==null)
    {
    builder.world=this.worldObj;
    }
  builder.onTick();
  
  if(builder.isFinished())
    {
    this.removeBuilder();
    }
  }

@Override
public boolean canUpdate()
  {
  return true;
  }

public void setBuilder(BuilderTicked builder)
  {
  if(this.builder==null)
    {
    this.builder = builder;
    }
  }


}
