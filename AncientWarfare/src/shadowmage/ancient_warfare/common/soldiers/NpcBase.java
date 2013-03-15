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
package shadowmage.ancient_warfare.common.soldiers;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.interfaces.IEntityContainerSynch;
import shadowmage.ancient_warfare.common.soldiers.ai.INpcAI;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class NpcBase extends EntityCreature implements IEntityAdditionalSpawnData, IEntityContainerSynch
{

public int teamNum = 0; 
public int rank = 0;

public ArrayList<INpcAI> npcAI = new ArrayList<INpcAI>();
public ArrayList<INpcAI> executingTasks = new ArrayList<INpcAI>();

/**
 * @param par1World
 */
public NpcBase(World par1World)
  {
  super(par1World);
  }

public void setRank(int num){}

@Override
protected void updateAITick() 
  {
  this.executingTasks.clear();
  for(INpcAI task : this.npcAI)
    {
    boolean found = false;
    int[] exclude = task.exclusiveTasks();
    for(INpcAI execTask : this.executingTasks)
      {      
      for(int i = 0; i < exclude.length; i++)
        {
        if(exclude[i]==execTask.getGlobalAIType())
          {
          found = true;
          break;
          }
        }
      if(found)
        {
        break;
        }
      }
    if(!found && task.shouldExecute(this))
      {
      this.executingTasks.add(task);
      task.onTick(this);
      }
    }
  }

@Override
protected boolean isAIEnabled()
  {
  return true;
  }

@Override
public int getMaxHealth()
  {
  return 20;
  }

@Override
public String getTexture()
  {
  return this.texture;
  }

@Override
public void onUpdate()
  {
  super.onUpdate();
  }

@Override
protected void attackEntity(Entity par1Entity, float par2) 
  {
  
  }

@Override
public void writeSpawnData(ByteArrayDataOutput data)
  {
  data.writeInt(teamNum);
  data.writeInt(rank);
  }

@Override
public void readSpawnData(ByteArrayDataInput data)
  {
  this.teamNum = data.readInt();
  this.rank = data.readInt();
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setInteger("team", this.teamNum);
  tag.setInteger("rank", this.rank);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.teamNum = tag.getInteger("team");
  this.rank = tag.getInteger("rank");
  }

@Override
public void handleClientInput(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void addPlayer(EntityPlayer player)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void removePlayer(EntityPlayer player)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public boolean canInteract(EntityPlayer player)
  {
  // TODO Auto-generated method stub
  return false;
  }

}
