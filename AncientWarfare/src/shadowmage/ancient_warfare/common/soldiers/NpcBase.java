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
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IEntityContainerSynch;
import shadowmage.ancient_warfare.common.pathfinding.queuing.NpcNavigatorScheduled;
import shadowmage.ancient_warfare.common.pathfinding.threading.EntityNavigatorThreaded;
import shadowmage.ancient_warfare.common.registry.NpcRegistry;
import shadowmage.ancient_warfare.common.soldiers.INpcType.NpcVarsHelper;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

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

//public EntityNavigator nav;
//public EntityNavigatorThreaded nav;
public NpcNavigatorScheduled nav;

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
//  this.nav = new EntityNavigator(this);
//  this.nav = new EntityNavigatorThreaded(this);
  this.nav = new NpcNavigatorScheduled(this);
  this.stepHeight = 1.1f;
  }

public void setNpcType(INpcType type, int level)
  {
  //  Config.logDebug("npc type being assigned: "+type.getDisplayName());
  this.npcType = type;
  this.rank = level;
  this.npcAI.clear();
  this.executingTasks.clear();
  this.npcAI.addAll(type.getAI(this, level)); 
  this.npcType.addTargets(this, targetHelper);
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
  return this.target == null? NpcAI.TARGET_NONE : this.target.targetType.getTypeName();
  }

public void setTargetAW(AIAggroEntry entry)
  {
  this.target = entry;
  }

public boolean isRidingVehicle()
  {
  if(this.ridingEntity instanceof VehicleBase)
    {
    return true;
    }
  return false;
  }

@Override
protected void updateAITick() 
  {
  //  Config.logDebug("AI Tick. currently executing tasks: "+this.executingTasks.size());  
  int mutexStack = 0;
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
  for(INpcAI execTask : this.executingTasks)
    {  
    mutexStack += execTask.taskType();      
    }
  //  Config.logDebug("stack mutex: "+mutexStack);
  for(INpcAI possibleTask : this.npcAI)
    {    
    //    Config.logDebug("examining possible AI task: "+possibleTask.getTaskName());
    possibleTask.incrementTickCounts();
    if(this.executingTasks.contains(possibleTask))//if task is already present in executing list, do not add
      {
      continue;
      }
    //    Config.logDebug("exclusive task: "+possibleTask.exclusiveTasks());
    if((possibleTask.exclusiveTasks() & mutexStack) == 0)
      {
      if(possibleTask.shouldExecute(this))
        {
        this.executingTasks.add(possibleTask);
        mutexStack+=possibleTask.taskType();
        }
      }
    else
      {
      //      Config.logDebug("skipping task due to exlusion: "+possibleTask.getTaskName() +"::"+ mutexStack);
      }

    boolean found = false;
    int exclude = possibleTask.exclusiveTasks(); 
    }  
  }

@Override
public boolean attackEntityAsMob(Entity ent)
  {
  ent.attackEntityFrom(DamageSource.causeMobDamage(this), 4);
  return false;
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
  this.varsHelper.onTick();

  if(target!=null && !target.isValidEntry())
    {
    this.setTargetAW(null);
    }
  this.npcAITargetTick++;
  if(npcAITargetTick>=Config.npcAITicks && !worldObj.isRemote)
    {
    npcAITargetTick = 0;
    this.targetHelper.updateAggroEntries();
    this.targetHelper.checkForTargets();
    //this.pathTest.findPath(worldObj, posX, posY, posZ, posX+10, posY, posZ, 40);
    }
  if(!this.worldObj.isRemote)
    {
    this.nav.moveTowardsCurrentNode();
    }
  super.onUpdate();  
  }

public void handlePacketUpdate(NBTTagCompound tag)
  {
  if(tag.hasKey("path") && this.worldObj.isRemote)
    {
    tag = tag.getCompoundTag("path");
    this.nav.setMoveTo(tag.getInteger("tx"), tag.getInteger("ty"), tag.getInteger("tz"));
//    Config.log("setting move target client side");
    }
  }

@Override
protected void attackEntity(Entity par1Entity, float par2) 
  {

  }

@Override
public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
  {  
  super.attackEntityFrom(par1DamageSource, par2);
  if(par1DamageSource.getEntity() instanceof EntityLiving)
    {
    this.targetHelper.handleBeingAttacked((EntityLiving)par1DamageSource.getEntity());    
//    Config.logDebug("adding entity to soldier aggro list for revenge: "+par1DamageSource.getEntity());
    }
//  Config.logDebug("NPC hit by attack.  RawDamage: "+par2+" new health: "+getHealth());  
  return true;
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
