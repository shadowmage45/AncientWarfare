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
package shadowmage.ancient_warfare.common.npcs;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IEntityContainerSynch;
import shadowmage.ancient_warfare.common.interfaces.IPathableEntity;
import shadowmage.ancient_warfare.common.npcs.INpcType.NpcVarsHelper;
import shadowmage.ancient_warfare.common.npcs.commands.NpcCommand;
import shadowmage.ancient_warfare.common.npcs.helpers.NpcTargetHelper;
import shadowmage.ancient_warfare.common.npcs.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.npcs.inventory.NpcInventory;
import shadowmage.ancient_warfare.common.pathfinding.EntityNavigator;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccess;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccessEntity;
import shadowmage.ancient_warfare.common.pathfinding.waypoints.WayPointNavigator;
import shadowmage.ancient_warfare.common.registry.NpcRegistry;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.TargetType;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class NpcBase extends EntityCreature implements IEntityAdditionalSpawnData, IEntityContainerSynch, IPathableEntity
{



public int teamNum = 0; 
public int rank = 0;

/**
 * used to check for targets/update target entries
 */
protected int npcAITargetTick = 0;
/**
 * cooldown for attacking/shooting/harvesting.  set by ai on actions dependant upon action type.
 * updated EVERY TICK from NpcBase.onUpdate()
 */
public int actionTick = 0;

public int villageUpdateTick = 0;

protected int idleLookTicks = 0;

public INpcType npcType = NpcRegistry.npcDummy;
public NpcVarsHelper varsHelper;// = npcType.getVarsHelper(this);
public NpcTargetHelper targetHelper;

public AIAggroEntry playerTarget = null;
private AIAggroEntry target = null;
private NpcAIObjectiveManager aiManager;
private PathWorldAccessEntity worldAccess;
public EntityNavigator nav;
public WayPointNavigator wayNav;
public NpcInventory inventory;

/**
 * @param par1World
 */
public NpcBase(World par1World)
  {
  super(par1World);
  this.varsHelper = new NpcDummyVarHelper(this);  
  this.targetHelper = new NpcTargetHelper(this);
  this.aiManager = new NpcAIObjectiveManager(this);
  this.moveSpeed = 0.325f;
  this.setAIMoveSpeed(0.325f);
  this.setMoveForward(0);
  this.worldAccess = new PathWorldAccessEntity(par1World, this);  
  this.worldAccess.canOpenDoors = true;
  this.worldAccess.canUseLaders = true;
  this.worldAccess.canSwim = true;
  this.nav = new EntityNavigator(this);
  this.nav.canOpenDoors = true;
  this.wayNav = new WayPointNavigator(this);
  this.inventory = new NpcInventory(this, 0);
  this.tasks.addTask(1, new EntityAISwimming(this));
  this.stepHeight = 1.1f;
  }

public void handleBatonCommand(NpcCommand cmd, int x, int y, int z)
  {
  Config.logDebug("receiving baton command");
  switch(cmd)
  {
  case HOME:
  wayNav.setHomePoint(x, y, z);
  break;
  case WORK:
  wayNav.setWorkPoint(x, y, z);
  break;
  case PATROL:
  wayNav.addPatrolPoint(x, y, z);
  break;
  case DEPOSIT:
  wayNav.setDepositPoint(x, y, z);
  break;
  case CLEAR_HOME:
  wayNav.clearHomePoint();
  break;
  case CLEAR_WORK:
  wayNav.clearWorkPoint();
  break;
  case CLEAR_PATROL:
  wayNav.clearPatrolPoints();
  break;
  case CLEAR_DEPOSIT:
  wayNav.clearDepositPoint();
  break;
  }
  }

public void setNpcType(INpcType type, int level)
  {
  //  Config.logDebug("npc type being assigned: "+type.getDisplayName());  
  this.npcType = type;
  this.rank = level;
  this.aiManager.addObjectives(type.getAI(this, level));
  this.npcType.addTargets(this, targetHelper);
  this.inventory = new NpcInventory(this, type.getInventorySize(level));
  }

public boolean isAggroTowards(NpcBase npc)
  {
  return this.npcType.isCombatUnit() && npc.npcType.isCombatUnit() && isAggroTowards(npc.teamNum);
  }

public boolean isAggroTowards(EntityPlayer player)
  {
  return this.npcType.isCombatUnit() && isAggroTowards(TeamTracker.instance().getTeamForPlayer(player));
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

/**
 * @return the playerTarget
 */
public AIAggroEntry getPlayerTarget()
  {
  return playerTarget;
  }

/**
 * @param playerTarget the playerTarget to set
 */
public void setPlayerTarget(AIAggroEntry playerTarget)
  {
  this.playerTarget = playerTarget;
  }

public TargetType getTargetType()
  {
  return this.target == null? TargetType.NONE : this.target.targetType.getTypeName();
  }

public void setTargetAW(AIAggroEntry entry)
  {
  this.target = entry;
  }

public boolean isRidingVehicle()
  {
  return this.ridingEntity!=null && this.ridingEntity instanceof VehicleBase;
  }

public float getDistanceFromTarget(AIAggroEntry target)
  {
  if(target!=null)
    {
    return Trig.getDistance(posX, posY, posZ, target.posX(), target.posY(), target.posZ());
    }
  return 0;
  }

int aiTick = 0;
@Override
protected void updateAITick() 
  {
  if(aiTick<Config.npcAITicks)
    {
    aiTick++;
    return;
    }
  aiTick = 0;
  this.aiManager.updateObjectives(); 
  }

@Override
public boolean interact(EntityPlayer player)
  {
  if(!this.isAggroTowards(player))
    {
    AIAggroEntry target = this.playerTarget;
    if(target==null || target.getEntity()!=player)
      {
      this.wayNav.clearHomePoint();
      Config.logDebug("setting player to follow to: "+player.getEntityName());
      this.playerTarget = new AIAggroEntry(this, this.targetHelper.playerTargetEntry, player);
      }
    else if(target!=null && target.getEntity()==player)
      {      
      if(player.isSneaking())
        {
        Config.logDebug("adding patrol point");
        this.wayNav.addPatrolPoint(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
        }
      else
        {
        this.playerTarget = null;
        this.wayNav.setHomePoint(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
        Config.log("clearing player to follow and setting home");
        }      
      }
    }
  return super.interact(player);
  }

@Override
public boolean attackEntityAsMob(Entity ent)
  {
  ent.attackEntityFrom(DamageSource.causeMobDamage(this), this.npcType.getAttackDamage(rank));
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
  return this.npcType== null? 20 : this.npcType.getMaxHealth(rank);
  }

@Override
public String getTexture()
  {
  return this.npcType.getDisplayTexture(rank);
  }

@Override
protected boolean canDespawn()
  {
  return false;
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
    }
  if(actionTick>0)
    {
    actionTick--;
    }
  this.updateArmSwingProgress();
  if(!this.worldObj.isRemote)
    {
    this.nav.moveTowardsCurrentNode();    
    }
  if(target!=null)
    {
    if(target.isEntityEntry)
      {
      this.getLookHelper().setLookPosition(target.posX(), target.posY(), target.posZ(), 10.0F, (float)this.getVerticalFaceSpeed());
      }
    else
      {
      this.getLookHelper().setLookPosition(target.posX(), posY+getEyeHeight(), target.posZ(), 10.0F, (float)this.getVerticalFaceSpeed());
      }
    }
  else
    {
    if(idleLookTicks <= 0)
      {
      idleLookTicks = this.getRNG().nextInt(20) + 20;
      double var1 = (Math.PI * 2D) * this.getRNG().nextDouble();
      double lookX = Math.cos(var1);
      double lookZ = Math.sin(var1);
      this.getLookHelper().setLookPosition(posX+lookX, posY+getEyeHeight(), posZ+lookZ, 10.f, (float)this.getVerticalFaceSpeed());
      }
    else if(idleLookTicks>0)
      {
      idleLookTicks--;
      }
    } 
  if(!this.npcType.isCombatUnit())
    {
//    Config.logDebug("non-combat NPC detected, doing villager stuffs");
    if(this.villageUpdateTick>0)
      {
      villageUpdateTick--;
      }
    else if(this.villageUpdateTick<=0)
      {
      Config.logDebug("adding npc to village collection queue");
      this.villageUpdateTick = 100+this.getRNG().nextInt(100);
      this.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
      }
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
    }
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

public int floorX()
  {
  return MathHelper.floor_double(posX);
  }

public int floorY()
  {
  return MathHelper.floor_double(posY);
  }

public int floorZ()
  {
  return MathHelper.floor_double(posZ);
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setInteger("team", this.teamNum);
  tag.setInteger("rank", this.rank);
  tag.setInteger("type", this.npcType.getGlobalNpcType());
  tag.setCompoundTag("waypoints", wayNav.getNBTTag());
  tag.setInteger("health", this.getHealth());
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.teamNum = tag.getInteger("team");
  this.rank = tag.getInteger("rank");
  int type = tag.getInteger("type");
  this.setNpcType(NpcTypeBase.getNpcType(type), this.rank);
  this.wayNav.readFromNBT(tag.getCompoundTag("waypoints"));
  this.health = tag.getInteger("health");
  }

@Override
public void handleClientInput(NBTTagCompound tag)
  {
 
  }

@Override
public void addPlayer(EntityPlayer player)
  {
  
  }

@Override
public void removePlayer(EntityPlayer player)
  {
  
  }

@Override
public boolean canInteract(EntityPlayer player)
  {
  return false;
  }

@Override
public void setMoveTo(double x, double y, double z, float moveSpeed)
  {
  this.getMoveHelper().setMoveTo(x, y, z, moveSpeed);
  }

@Override
public Entity getEntity()
  {
  return this;
  }

@Override
public boolean isPathableEntityOnLadder()
  {
  return this.isOnLadder();
  }

@Override
public void setPath(List<Node> path)
  {
  this.nav.setPath(path);
  }

@Override
public PathWorldAccess getWorldAccess()
  {
  return worldAccess;
  }

public void clearPath()
  {
  this.nav.clearPath();
  }

@Override
public float getDefaultMoveSpeed()
  {
  return 0.325f;
  }

}
