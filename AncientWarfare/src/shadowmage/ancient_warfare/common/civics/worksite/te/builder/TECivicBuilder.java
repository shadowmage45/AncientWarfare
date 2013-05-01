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
package shadowmage.ancient_warfare.common.civics.worksite.te.builder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.structures.build.Builder;
import shadowmage.ancient_warfare.common.structures.build.BuilderTicked;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class TECivicBuilder extends TECivic
{
private BuilderTicked builder;
private boolean shouldRemove = false;

@Override
public boolean onInteract(World world, EntityPlayer player)
  {
  return false;
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
    this.worldObj.setBlock(xCoord, yCoord, zCoord, 0);
    return;
    }
  if(builder.world==null)
    {
    builder.world=this.worldObj;
    }
  
  if(builder.isFinished())
    {
    this.removeBuilder();
    this.worldObj.setBlock(xCoord, yCoord, zCoord, 0);
    }
  }

/************************************************WORK SITE*************************************************/

@Override
public WorkPoint getWorkPoint(NpcBase npc)
  {
  if(builder!=null && !builder.isFinished())
    {
    return new WorkPoint(this, xCoord, yCoord, zCoord, 1, TargetType.BUILD_PLACE);
    }
  return null;
  }

@Override
public void updateWorkPoints()
  {
  //NOOP--no 'work points'
  }

@Override
protected void updateHasWork()
  {
  this.hasWork = false;
  if(this.builder!=null && !this.builder.isFinished())
    {
    this.hasWork =true;
    }
  }

@Override
public void onWorkFinished(NpcBase npc, WorkPoint point)
  {
  super.onWorkFinished(npc, point);
  this.tickBuilder();
  
  }

/************************************************BUILDER*************************************************/

public void setBuilder(BuilderTicked builder)
  {
  if(this.builder==null)
    {
    this.builder = builder;
    if(builder!=null)
      {
      builder.tickTimer = 1;
      }
    }
  }

public void removeBuilder()
  {
  this.invalidate();  
  }

protected void tickBuilder()
  {
  if(builder!=null && !builder.isFinished())
    {
    builder.onTick();    
    }
  }

@Override
public void readFromNBT(NBTTagCompound par1nbtTagCompound)
  {
  super.readFromNBT(par1nbtTagCompound);
  if(par1nbtTagCompound.hasKey("builder"))
    {
//    Config.logDebug("reading builder data");
    NBTTagCompound builder = par1nbtTagCompound.getCompoundTag("builder");
    this.builder = Builder.readTickedBuilderFromNBT(builder);
    if(this.builder==null)
      {
      this.shouldRemove = true;
      }
    else
      {
      this.builder.tickTimer = 1;
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
}
