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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IEntityContainerSynch;
import shadowmage.ancient_warfare.common.interfaces.IPathableEntity;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.network.Packet04Npc;
import shadowmage.ancient_warfare.common.npcs.INpcType.NpcVarsHelper;
import shadowmage.ancient_warfare.common.npcs.commands.NpcCommand;
import shadowmage.ancient_warfare.common.npcs.helpers.NpcTargetHelper;
import shadowmage.ancient_warfare.common.npcs.inventory.NpcInventory;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPointNavigator;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccess;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccessEntity;
import shadowmage.ancient_warfare.common.pathfinding.navigator.Navigator;
import shadowmage.ancient_warfare.common.registry.NpcRegistry;
import shadowmage.ancient_warfare.common.targeting.TargetPosition;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
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
 * cooldown for attacking/shooting/harvesting.  set by ai on actions dependant upon action type.
 * updated EVERY TICK from NpcBase.onUpdate()
 */
public int actionTick = 0;

public int villageUpdateTick = 0;

protected int idleLookTicks = 0;

protected int lootCheckTicks = 0;

public INpcType npcType = NpcRegistry.npcDummy;
public NpcVarsHelper varsHelper;// = npcType.getVarsHelper(this);
public NpcTargetHelper targetHelper;

private NpcAIObjectiveManager aiManager;
private PathWorldAccessEntity worldAccess;
//public EntityNavigator nav;
public Navigator nav;
public WayPointNavigator wayNav;
public NpcInventory inventory;

/**
 * @param par1World
 */
public NpcBase(World par1World)
  {
  super(par1World);
  this.height = 1.6f;
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
  this.nav = new Navigator(this);
  this.nav.setCanOpenDoors(true);
  this.nav.setCanSwim(true);
  this.wayNav = new WayPointNavigator(this);
  this.inventory = new NpcInventory(this, 0);
  this.tasks.addTask(1, new EntityAISwimming(this));
  this.stepHeight = 1.1f;
  for (int i = 0; i < this.equipmentDropChances.length; ++i)
    {
    this.equipmentDropChances[i] = 1.f;
    }
  this.experienceValue = 10;
  }

public void handleBatonCommand(NpcCommand cmd, int x, int y, int z, int side)
  {
  switch(cmd)
  {
  case HOME:
  wayNav.setHomePoint(x, y, z);
  break;
  case WORK:
  wayNav.setWorkSite(x, y, z);
  break;
  case PATROL:
  wayNav.addPatrolPoint(x, y, z);
  break;
  case DEPOSIT:
  wayNav.setDepositSite(x, y, z, side);
  break;
  case CLEAR_HOME:
  wayNav.clearHomePoint();
  break;
  case CLEAR_WORK:
  wayNav.clearWorkSite();
  break;
  case CLEAR_PATROL:
  wayNav.clearPatrolPoints();
  break;
  case CLEAR_DEPOSIT:
  wayNav.clearDepositSite();
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
  this.experienceValue = 10 + 10*level;
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

public ITargetEntry getTarget()
  {
  return this.wayNav.getTarget();
  }

/**
 * @return the playerTarget
 */
public ITargetEntry getPlayerTarget()
  {
  return this.wayNav.getPlayerTarget();
  }

/**
 * @param playerTarget the playerTarget to set
 */
public void setPlayerTarget(ITargetEntry playerTarget)
  {
  this.wayNav.setPlayerTarget(playerTarget);
  }

public TargetType getTargetType()
  {
  return this.wayNav.getTarget()==null ? TargetType.NONE : this.wayNav.getTarget().getTargetType();
  }

public void setTargetAW(ITargetEntry entry)
  {
  this.wayNav.setTarget(entry);
  }

public boolean isRidingVehicle()
  {
  return this.ridingEntity!=null && this.ridingEntity instanceof VehicleBase;
  }

public float getDistanceFromTarget(ITargetEntry target)
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
  if(player.worldObj.isRemote)
    {
    return true;
    }
  if(!this.isAggroTowards(player))
    {
    if(player.isSneaking())
      {
      GUIHandler.instance().openGUI(GUIHandler.NPC_BASE, player, worldObj, entityId, 0, 0);
      //opengui
      }
    else
      {
      ITargetEntry target = this.wayNav.getPlayerTarget();
      if(target==null || target.getEntity()!=player)
        {
        player.addChatMessage("Commanding Npc to follow!");
        this.wayNav.setPlayerTarget(TargetPosition.getNewTarget(player, TargetType.FOLLOW));
        }
      else if(target!=null && target.getEntity()==player)
        {
        player.addChatMessage("Commanding Npc to stop following!");
        this.wayNav.setPlayerTarget(null);
        }
      }
    }
  return true;
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
public void setDead()
  {
  super.setDead();
  if(this.worldObj!=null && !this.worldObj.isRemote && this.inventory.getSizeInventory()>0)
    {
    InventoryTools.dropInventoryInWorld(worldObj, inventory, posX, posY, posZ);
    }
  }

public void setActionTicksToMax()
  {
  this.actionTick = this.npcType.getActionTicks(rank);
  }

@Override
public void onUpdate()
  {
  this.varsHelper.onTick(); 
  if(!worldObj.isRemote && (ticksExisted + this.entityId) % Config.npcAITicks == 0)
    {
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
    this.nav.onMovementUpdate();
    }
  ITargetEntry target = this.wayNav.getTarget();
  if(target!=null)
    {
    if(target.getEntity()!=null)
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
      this.villageUpdateTick = 100+this.getRNG().nextInt(100);
      this.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
      }
    }
  if(this.lootCheckTicks<=0)
    {
    this.lootCheckTicks = Config.npcAITicks;
    List<EntityItem> worldItems = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(posX-2, posY-1, posZ-2, posY+1, posX+2, posZ+2));
    if(worldItems!=null)
      {
      ItemStack item;
      for(EntityItem ent : worldItems)
        {
        item = ent.getEntityItem();
        item = inventory.tryMergeItem(item);
        if(item!=null)
          {
          ent.setEntityItemStack(item);
          }
        else
          {
          ent.setDead();
          }
        }
      }
    }
  else
    {
    this.lootCheckTicks--;
    }
  super.onUpdate();    
  }

public void handleLootPickup()
  {
  
  }

public void handlePacketUpdate(NBTTagCompound tag)
  {
  if(tag.hasKey("path") && this.worldObj.isRemote)
    {
    tag = tag.getCompoundTag("path");
    this.nav.setMoveToTarget(tag.getInteger("tx"), tag.getInteger("ty"), tag.getInteger("tz"));
    }
  if(tag.hasKey("health") && worldObj.isRemote)
    {
    this.health = (int)tag.getByte("health");
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
    if(!worldObj.isRemote)
      {
      Packet04Npc pkt = new Packet04Npc();
      pkt.setParams(this);
      pkt.setHealthUpdate((byte) this.getHealth());
      pkt.sendPacketToAllTrackingClients(this);
      }
    }
  return true;
  }

@Override
public void writeSpawnData(ByteArrayDataOutput data)
  {
  data.writeInt(teamNum);
  data.writeInt(rank);
  data.writeInt(this.npcType.getGlobalNpcType());
  data.writeByte((byte)health);
  }

@Override
public void readSpawnData(ByteArrayDataInput data)
  {
  this.teamNum = data.readInt();
  this.rank = data.readInt();
  INpcType t = NpcTypeBase.getNpcType(data.readInt());
  this.setNpcType(t, rank);
  this.health = (int)data.readByte();
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
  tag.setCompoundTag("inv", this.inventory.getNBTTag());
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
  if(tag.hasKey("inv"))
    {
    this.inventory.readFromNBT(tag.getCompoundTag("inv"));
    }
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
  //TODO ??? WTF is this even called?
  return true;
  }

/**
 * IPathableEntity method...
 */
@Override
public void setMoveTo(double x, double y, double z, float moveSpeed)
  {
  this.getMoveHelper().setMoveTo(x, y, z, moveSpeed);
  }

/**
 * IPathableEntity method...
 */
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
  this.nav.forcePath(path);
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
