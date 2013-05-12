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

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
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
import shadowmage.ancient_warfare.common.npcs.helpers.targeting.AITargetEntry;
import shadowmage.ancient_warfare.common.npcs.inventory.NpcInventory;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPointNavigator;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccess;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccessEntity;
import shadowmage.ancient_warfare.common.pathfinding.navigator.Navigator;
import shadowmage.ancient_warfare.common.registry.NpcRegistry;
import shadowmage.ancient_warfare.common.targeting.TargetPosition;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
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

protected int aiTick = 0;

public int villageUpdateTick = 0;

protected int idleLookTicks = 0;

protected int lootCheckTicks = 0;

protected int npcTicksExisted = 0;

public int npcUpkeepTicks = Config.npcUpkeepTicks;//how many upkeep ticks worth of food are remaining?

protected int npcHealingTicks = Config.npcHealingTicks;//

public INpcType npcType = NpcTypeBase.npcDummy;
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
  this.health = 20;  
  }

/**
 * add extra forced targets from config file. happens at entity instantiation to prevent issues
 * of people not registering their entities until post-post init...etc....
 */
public void addConfigTargets()
  {
  String[] targets = null;//Config.getConfig().get("npc_aggro_settings", "footsoldier", new String[]{}, "Forced targets for footsoldiers").getStringList();
  String targetType = null;
  if(npcType.isCombatUnit() && !npcType.getConfigName().equals(""))
    {
//    Config.logDebug("adding targets to combat unit");
    targets = Config.getConfig().get("npc_aggro_settings", npcType.getConfigName(), npcType.getDefaultTargets()).getStringList();
//    Config.logDebug("targetsToString: "+targets);
    if(targets!=null && targets.length>0)
      {
//      Config.logDebug("found: "+targets.length+ " targets from config");
      Class clz;
      for(String name : targets)
        {
//        Config.logDebug("attempting to get class for name: "+name);
        clz = (Class) EntityList.stringToClassMapping.get(name);
        if(clz!=null)
          {
//          Config.logDebug("adding "+clz+" to targets");
          targetHelper.addTargetEntry(new AITargetEntry(this, TargetType.ATTACK, clz, 0, true, Config.npcAISearchRange));
          }
        }
      }
    }
  }

@Override
public ItemStack getPickedResult(MovingObjectPosition target)
  {
  
  return NpcRegistry.getStackFor(npcType, rank);
  }

@Override
public void updateRidden()
  {
  super.updateRidden();
  this.motionX = 0;
  this.motionY = 0;
  this.motionZ = 0;
  }

@Override
public void travelToDimension(int par1)
  {
  wayNav.handleDimensionChange(par1);
  targetHelper.handleDimensionChange(par1);
  super.travelToDimension(par1);
  }

/**
 * called from player-attack event, and commander broadcast
 * @param ent
 */
public void handleBroadcastAttackTarget(Entity ent)
  {
  targetHelper.handleBroadcastTarget(ent, TargetType.ATTACK);
  }

public void handleBatonCommand(NpcCommand cmd, WayPoint p)
  {
  switch(cmd)
  {
  case HOME:
  wayNav.setHomePoint(p);
  break;
  case WORK:
  wayNav.setWorkSite(p);
  break;
  case PATROL:
  wayNav.addPatrolPoint(p);
  break;
  case DEPOSIT:
  wayNav.setDepositSite(p);
  break;
  case CLEAR_HOME:
  wayNav.setHomePoint(null);
  break;
  case CLEAR_WORK:
  wayNav.setWorkSite(null);
  break;
  case CLEAR_PATROL:
  wayNav.clearPatrolPoints();
  break;
  case CLEAR_DEPOSIT:
  wayNav.setDepositSite(null);
  break;
  case UPKEEP:
  wayNav.setUpkeepSite(p);
  break;
  case CLEAR_UPKEEP:
  wayNav.setUpkeepSite(null);
  break;
  case MOUNT:
  if(p.getEntity(worldObj) instanceof VehicleBase)
    { 
    wayNav.setMountTarget((VehicleBase)p.getEntity(worldObj));
    }
  break;
  case CLEAR_MOUNT:
  wayNav.setMountTarget(null);
  break;
  }
  }

public void setNpcType(INpcType type, int level)
  {
  this.npcType = type;
  this.rank = level;
  this.aiManager.addObjectives(type.getAI(this, level));
  this.npcType.addTargets(this, targetHelper);
  this.inventory = new NpcInventory(this, type.getInventorySize(level));
  this.experienceValue = 10 + 10*level;
  this.addConfigTargets();
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
  return this.npcType.isCombatUnit() && TeamTracker.instance().isHostileTowards(worldObj, teamNum, otherTeam);
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

@Override
protected void updateAITick() 
  {
  if(aiTick < Config.npcAITicks)
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
      }
    else
      {
      ITargetEntry target = this.wayNav.getPlayerTarget();
      if(target==null || target.getEntity(worldObj)!=player)
        {
        player.addChatMessage("Commanding Npc to follow!");
        this.wayNav.setPlayerTarget(TargetPosition.getNewTarget(player, TargetType.FOLLOW));
        }
      else if(target!=null && target.getEntity(worldObj)==player)
        {
        player.addChatMessage("Commanding Npc to stop following!");
        this.wayNav.setPlayerTarget(null);
        }
      }
    }
  return true;
  }

public void dismountVehicle()
  {
  if(this.isRidingVehicle())
    {
    Entity et = ridingEntity;
    this.ridingEntity = null;
    et.riddenByEntity = null;
    this.unmountEntity(et);
    VehicleBase vehicle = (VehicleBase)et;
    vehicle.moveHelper.clearInputFromDismount();    
    }
  }

@Override
public boolean attackEntityAsMob(Entity ent)
  {
  int commanderBonus = 0;
  if(wayNav.getCommander()!=null)
    {
    commanderBonus = 1 + wayNav.getCommander().rank;
    }
  ent.attackEntityFrom(DamageSource.causeMobDamage(this), this.npcType.getAttackDamage(rank) + commanderBonus);
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
  if(!this.worldObj.isRemote && this.isRidingVehicle())
    {
    this.getRidingVehicle().moveHelper.clearInputFromDismount();
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
  npcTicksExisted++;
  if(!worldObj.isRemote && (npcTicksExisted + this.entityId) % Config.npcAITicks == 0)
    {
    this.targetHelper.updateAggroEntries();
    this.targetHelper.checkForTargets();
    if(this.wayNav.getCommander()!=null && this.wayNav.getCommander().getTargetType()==TargetType.ATTACK)
      {
      this.handleBroadcastAttackTarget(this.wayNav.getCommander().getTarget().getEntity(worldObj));
      }
    }  
  if(this.npcUpkeepTicks>0)
    {
    this.npcUpkeepTicks--;
    }  
  if(this.npcUpkeepTicks!=0)
    {
    if(npcHealingTicks==0)
      {
      npcHealingTicks = Config.npcHealingTicks;
      this.handleHealingUpdate();
      }
    else if(npcHealingTicks>0)
      {
      npcHealingTicks--;
      }
    }
  if(actionTick>0)
    {
    actionTick--;
    }
  if(this.lootCheckTicks<=0)
    {
    this.lootCheckTicks = Config.npcAITicks;
    this.handleLootPickup();
    }
  else
    {
    this.lootCheckTicks--;
    }   
  this.updateArmSwingProgress();
  if(!this.worldObj.isRemote)
    {
    this.nav.onMovementUpdate();
    }
  ITargetEntry target = this.wayNav.getTarget();
  if(target!=null && target.isEntityEntry())
    {
    Entity ent = target.getEntity(worldObj);
    if(ent==null || ent.isDead)
      {
      this.setTargetAW(null);
      }
    }
  if(target!=null)
    {
    if(target.isEntityEntry() && target.getEntity(worldObj)!=null)
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
  int floorX = MathHelper.floor_double(posX);
  int floorY = MathHelper.floor_double(posY);
  int floorZ = MathHelper.floor_double(posZ);
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
      this.worldObj.villageCollectionObj.addVillagerPosition(floorX, floorY, floorZ);
      }
    }
  int id = worldObj.getBlockId(floorX, floorY, floorZ);
  if(!this.worldObj.isRemote && (id==Block.fence.blockID || id==Block.thinGlass.blockID))
    {
    this.pushOutOfBlocks();
    }
  this.handleHealthUpdate();
  boolean riding = false;
  if(this.isRidingVehicle())
    {
    riding = true;   
    }
  if(!this.worldObj.isRemote && this.getTarget()==null && riding)
    {
    VehicleBase vehicle = (VehicleBase)this.ridingEntity;
    vehicle.moveHelper.clearInputFromDismount();    
    }  
  super.onUpdate();     
  }

/**
 * Checks if this entity is inside of an opaque block
 */
@Override
public boolean isEntityInsideOpaqueBlock()
  {
  if(this.isRidingVehicle())
    {
    return false;
    }
  for (int i = 0; i < 8; ++i)
    {
    float f = ((float)((i >> 0) % 2) - 0.5F) * this.width * 0.8F;
    float f1 = ((float)((i >> 1) % 2) - 0.5F) * 0.1F;
    float f2 = ((float)((i >> 2) % 2) - 0.5F) * this.width * 0.8F;
    int j = MathHelper.floor_double(this.posX + (double)f);
    int k = MathHelper.floor_double(this.posY + (double)this.getEyeHeight() + (double)f1);
    int l = MathHelper.floor_double(this.posZ + (double)f2);

    if (this.worldObj.isBlockNormalCube(j, k, l))
      {
      return true;
      }
    }

  return false;
  }

@Override
protected void entityInit()
  {
  super.entityInit();  
  this.dataWatcher.addObject(28, Byte.valueOf((byte) -1));//objective
  this.dataWatcher.addObject(29, Byte.valueOf((byte) -1));//task
  this.dataWatcher.addObject(30, Byte.valueOf((byte) -1));//other/error (no food/no deposit)
  this.dataWatcher.addObject(31, new Integer(20));//health  
  }

public byte getAIObjectiveID()
  {
  return this.dataWatcher.getWatchableObjectByte(28);
  }

public byte getAITaskID()
  {
  return this.dataWatcher.getWatchableObjectByte(29);
  }
  
public byte getAIErrorID()
  {
  return this.dataWatcher.getWatchableObjectByte(30);
  }

public void setTaskID(byte b)
  {
  this.dataWatcher.updateObject(29, Byte.valueOf(b));
  }

public void setObjectiveID(byte b)
  {
  this.dataWatcher.updateObject(28, Byte.valueOf(b));
  }

public void setErrorID(byte b)
  {
  this.dataWatcher.updateObject(30, Byte.valueOf(b));
  }

protected void handleHealthUpdate()
  {
  if(this.worldObj.isRemote)
    {
    int newHealth = this.dataWatcher.getWatchableObjectInt(31);
    if(newHealth!=this.health)
      {
//      Config.logDebug("setting client health from watched data");
      this.setEntityHealth(newHealth);
      }    
    }
  else
    {
    int watchedHealth = this.dataWatcher.getWatchableObjectInt(31);
    if(watchedHealth!=this.health)
      {
//      Config.logDebug("updating watched health");
      this.dataWatcher.updateObject(31, Integer.valueOf(health));
      }
    }
  }

protected void pushOutOfBlocks()
  {
  Config.logDebug("setting push out of blocks");
  int x = MathHelper.floor_double(posX);
  int y = MathHelper.floor_double(posY);
  int z = MathHelper.floor_double(posZ);
  float dist = Float.POSITIVE_INFINITY;
  float testDist;
  BlockPosition closest = new BlockPosition(x,y,z);
  for(int x1 = x-1; x1 <=x+1; x1++)
    {
    for(int z1 = z-1; z1<=z+1; z1++)
      {
      if(x1==x &&z1==z){continue;}
      if(worldAccess.isWalkable(x1, y, z1) || worldAccess.isWalkable(x1, y-1, z1))
        {
        testDist = (float) getDistance(x1+0.5d, y, z1+0.5d);
        if(testDist<dist)
          {
          closest.x = x1;
          closest.y = y;
          closest.z = z1;
          dist = testDist;
          }
        }           
      }
    }
  if(closest.x!=x || closest.z!=z)
    {
    this.setMoveTo(closest.x+0.5d, closest.y, closest.z+0.5d, this.moveSpeed);
    }
  }

protected void handleHealingUpdate()
  {
  if(this.health<this.getMaxHealth())
    {
    this.health++;
    }
  }

public void handleLootPickup()
  {
  List<EntityItem> worldItems = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(posX-1.5, posY-1, posZ-1.5, posX+1.5, posY+1, posZ+1.5));
  if(worldItems!=null)
    {
    ItemStack item;
    for(EntityItem ent : worldItems)
      {
      item = ent.getEntityItem();
      if(item==null){continue;}
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

public void handlePacketUpdate(NBTTagCompound tag)
  {
  if(tag.hasKey("path") && this.worldObj.isRemote)
    {
    tag = tag.getCompoundTag("path");
    this.nav.setMoveToTarget(tag.getInteger("tx"), tag.getInteger("ty"), tag.getInteger("tz"));
    }
  }

@Override
public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
  {  
  if(wayNav.getCommander()!=null)
    {
    par2 -= (2 + wayNav.getCommander().rank)/2;
    if(par2<1)
      {
      par2 = 1;
      }
    }
  super.attackEntityFrom(par1DamageSource, par2);
  if(par1DamageSource.getEntity() instanceof EntityLiving)
    {
    this.targetHelper.handleBeingAttacked((EntityLiving)par1DamageSource.getEntity()); 
    }
  return true;
  }

@Override
protected boolean canTriggerWalking()
  {
  return false;
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
  tag.setInteger("upkeep", this.npcUpkeepTicks);
  tag.setInteger("healing", this.npcHealingTicks);
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
  this.npcUpkeepTicks = tag.getInteger("upkeep");
  this.npcHealingTicks = tag.getInteger("healing");
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
 * Set MOVEHELPER (current node) move-to-target
 */
@Override
public void setMoveTo(double x, double y, double z, float moveSpeed)
  {
  if(this.ridingEntity==null)
    {
    this.getMoveHelper().setMoveTo(x, y, z, moveSpeed);
    }
  }

/**
 * set NAVIGATOR moveto target
 * @param x
 * @param y
 * @param z
 */
public void setMoveToTarget(int x, int y, int z)
  {
  VehicleBase vehicle = this.getRidingVehicle();
  if(vehicle!=null)
    {
    vehicle.nav.setMoveToTarget(x, y, z);    
    }
  else
    {
    this.nav.setMoveToTarget(x, y, z);    
    }
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
  VehicleBase vehicle = this.getRidingVehicle();
  if(vehicle!=null)
    {
    vehicle.nav.forcePath(path);
    }
  else
    {
    this.nav.forcePath(path);
    }
  }

@Override
public PathWorldAccess getWorldAccess()
  {
  return worldAccess;
  }

public void clearPath()
  {    
  VehicleBase vehicle = this.getRidingVehicle();
  if(vehicle!=null)
    {
    vehicle.clearPath();
    vehicle.moveHelper.clearInputFromDismount();
    }  
  else
    {
    this.nav.clearPath();
    }
  }

public VehicleBase getRidingVehicle()
  {
  if(this.isRidingVehicle())
    {
    return (VehicleBase)this.ridingEntity;
    }
  return null;
  }

@Override
public float getDefaultMoveSpeed()
  {
  return 0.325f;
  }

}
