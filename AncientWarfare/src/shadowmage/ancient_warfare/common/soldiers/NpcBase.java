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
import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IEntityContainerSynch;
import shadowmage.ancient_warfare.common.registry.NpcRegistry;
import shadowmage.ancient_warfare.common.soldiers.INpcType.NpcVarsHelper;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper.AIAggroEntry;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class NpcBase extends EntityCreature implements IEntityAdditionalSpawnData, IEntityContainerSynch
{

public int teamNum = 0; 
public int rank = 0;

/**
 * used to check for targets/update target entries
 */
int npcAITargetTick = 0;

public ArrayList<INpcAI> npcAI = new ArrayList<INpcAI>();
public ArrayList<INpcAI> executingTasks = new ArrayList<INpcAI>();

public INpcType npcType = NpcRegistry.npcDummy;
public NpcVarsHelper varsHelper;// = npcType.getVarsHelper(this);
public NpcTargetHelper targetHelper;

private AIAggroEntry target = null;

/**
 * @param par1World
 */
public NpcBase(World par1World)
  {
  super(par1World);
  this.varsHelper = new NpcDummyVarHelper(this);  
  this.targetHelper = new NpcTargetHelper(this);
  this.moveSpeed = 0.325f;
  this.setAIMoveSpeed(0.325f); 
  }

public void setNpcType(INpcType type, int level)
  {
//  Config.logDebug("npc type being assigned: "+type.getDisplayName());
  this.npcType = type;
  this.rank = level;
  this.npcAI.clear();
  this.executingTasks.clear();
  this.npcAI.addAll(type.getAI(this, level)); 
  this.npcType.addTargets(targetHelper);
  }

public boolean isAggroTowards(NpcBase npc)
  {
  return npc.npcType.isCombatUnit() && isAggroTowards(npc.teamNum);
  }

public boolean isAggroTowards(EntityPlayer player)
  {
  return isAggroTowards(TeamTracker.instance().getTeamForPlayer(player));
  }

public boolean isAggroTowards(int otherTeam)
  {
  return TeamTracker.instance().isHostileTowards(worldObj, teamNum, otherTeam);
  }

public Entity getTargetEntity()
  {
  return this.target!=null ? this.target.getEntity() : null;
  }

public AIAggroEntry getTarget()
  {
  return this.target;
  }

public String getTargetType()
  {
  return this.target == null? "No Target" : this.target.targetType;
  }

public void setTarget(AIAggroEntry entry)
  {
  this.target = entry;
  }

@Override
protected void updateAITick() 
  {
//  Config.logDebug("AI Tick. currently executing tasks: "+this.executingTasks.size());  
  Iterator<INpcAI> it = this.executingTasks.iterator();
  INpcAI task;
  while(it.hasNext())
    {    
    task = it.next();
    if(task.shouldExecute(this))
      {
      if(!task.hasStarted())
        {
        task.startAI();
        }
      task.onTick();
      if(task.isFinished())
        {
        it.remove();
        }
      }
    else
      {
      it.remove();
      }    
    }
  
  for(INpcAI possibleTask : this.npcAI)
    {    
//    Config.logDebug("examining possible AI task: "+possibleTask.getTaskName());
    possibleTask.incrementTickCounts();
    if(this.executingTasks.contains(possibleTask))//if task is already present in executing list, do not add
      {
      continue;
      }
    boolean found = false;
    int exclude = possibleTask.exclusiveTasks();   
    for(INpcAI execTask : this.executingTasks)
      {  
      if((execTask.exclusiveTasks() & exclude )!= 0)
        {
        found = true;
//        Config.logDebug("mutex exception!! will not add to executing tasks");
        break;
        }      
      }
    if(!found && possibleTask.shouldExecute(this))
      {
      this.executingTasks.add(possibleTask);
      }
    else if(!possibleTask.shouldExecute(this))
      {
//      Config.logDebug("task self-denied execution");
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
  return this.npcType.getDisplayTexture(rank);
  }


@Override
public void onUpdate()
  {
  super.onUpdate();
  this.varsHelper.onTick();
  
  this.npcAITargetTick++;
  if(npcAITargetTick>=Config.npcAITicks)
    {
    npcAITargetTick = 0;
    this.targetHelper.updateAggroEntries();
    this.targetHelper.checkForTargets();
    }  
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
  tag.setInteger("type", this.npcType.getGlobalNpcType());
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.teamNum = tag.getInteger("team");
  this.rank = tag.getInteger("rank");
  int type = tag.getInteger("type");
  this.setNpcType(NpcTypeBase.getNpcType(type), this.rank);
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
