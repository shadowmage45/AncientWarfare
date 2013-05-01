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
package shadowmage.ancient_warfare.common.civics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.network.Packet05TE;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public abstract class TECivic extends TileEntity implements IInventory
{

int ticksExisted = 0;
int teamNum = 0;
/**
 * work-site bounds, may be un-set for non work-site civics
 */
public int minX;
public int minY;
public int minZ;
public int maxX;
public int maxY;
public int maxZ;
public TargetType workType;
protected boolean isWorkSite = false;
protected boolean broadcastWork = true;//user toggle...spawned NPC buildings will auto-broadcast
protected AWInventoryBasic inventory = new AWInventoryBasic(0);
protected Civic civic = (Civic) Civic.wheatFarm;//dummy/placeholder...
protected List<WorkPoint> workPoints = new ArrayList<WorkPoint>();//points being worked currently
protected Set<NpcBase> workers = Collections.newSetFromMap(new WeakHashMap<NpcBase, Boolean>());
protected Random rng = new Random();

private boolean hasWork = false;

protected int teID = 0;

static int teInstanceIDNext = 0;

protected int tickDivider = Config.npcAITicks * 10;

protected int clientWorkStatus = 0;
protected int clientInventoryStatus = 0;
protected int clientWorkerStatus = 0;

public TECivic()
  {
  teID = teInstanceIDNext;
  this.teInstanceIDNext++;
  }

/***************************************************SETUP/INIT**************************************************************/
public void setCivic(Civic civ)
  {
  this.civic = civ;
  inventory = new AWInventoryBasic(civ.getInventorySize());
  }

public Civic getCivic()
  {
  return this.civic;
  }

public void setBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
  {
  this.minX = minX;
  this.minY = minY;
  this.minZ = minZ;
  this.maxX = maxX;
  this.maxY = maxY;
  this.maxZ = maxZ;
  }

@Override
public void updateEntity()
  {
  ticksExisted++;
  if(this.worldObj!=null && !this.worldObj.isRemote && (this.ticksExisted+this.teID)% tickDivider == 0 )
    {
    long t1 = System.nanoTime();
    this.updateHasWork();
    this.broadCastToSoldiers(Config.npcAISearchRange);
    this.updateWorkPoints();
    this.validateWorkers(); 
    this.updateInventoryStatus();
    Config.logDebug("TE tick time: "+(System.nanoTime()-t1) + " for: "+this.getCivic().getDisplayName());
    }   
  super.updateEntity();
  }

@Override
public boolean canUpdate()
  {
  return true;
  }

public int getTeamNum()
  {
  return teamNum;
  }

public boolean isHostile(int sourceTeam)
  {
  if(this.worldObj==null)
    {
    return false;
    }
  return TeamTracker.instance().isHostileTowards(worldObj, sourceTeam, teamNum);
  }

public boolean canTeamInteract(int sourceTeam)
  {
  if(this.worldObj==null)
    {
    return false;
    }
  return !TeamTracker.instance().isHostileTowards(worldObj, teamNum, sourceTeam);
  }

public void broadCastToSoldiers(int maxRange)
  {
  if(this.worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord).expand(maxRange, maxRange, maxRange);
  List<NpcBase> npcList = worldObj.getEntitiesWithinAABB(NpcBase.class, bb);
  for(NpcBase npc : npcList)
    {
    if(isHostile(npc.teamNum))      
      {
      if(npc.npcType.isCombatUnit())
        {
        npc.targetHelper.handleTileEntityTargetBroadcast(this, TargetType.ATTACK_TILE, Config.npcAITicks*11);
        }      
      }
    else
      {
      if(broadcastWork)
        {    
        if(hasWork() && canHaveMoreWorkers(npc) && npc.npcType.getWorkTypes(npc.rank).contains(civic.getWorkType()))
          {
          npc.targetHelper.handleTileEntityTargetBroadcast(this, TargetType.WORK, Config.npcAITicks*11);
          }
        }
      }
    }
  }

protected void updateInventoryStatus()
  {
  if(this.inventory.getSizeInventory()>0)
    {
    int empty = this.inventory.getEmptySlotCount();
    int prevStatus = this.clientInventoryStatus;
    if(empty<=0)
      {
      this.clientInventoryStatus = 0;
      }
    else
      {
      this.clientInventoryStatus = 1;
      }
    if(prevStatus != this.clientInventoryStatus)
      {
      this.sendInventoryStatusUpdate(clientInventoryStatus);
      }
    }
  }

public boolean onInteract(World world, EntityPlayer player)
  {
  if(!world.isRemote && inventory.getSizeInventory()>0)
    {
    GUIHandler.instance().openGUI(GUIHandler.CIVIC_BASE, player, world, xCoord, yCoord, zCoord);
    }
  return true;
  }

/******************************************************WORK-SITE*********************************************************/
public boolean canHaveMoreWorkers(NpcBase worker)
  {
  if(this.workers.contains(worker))
    {
    return true;
    }
  if(this.workers.size()+1 <= this.civic.getMaxWorkers())
    {
    return true;
    }
  return false;
  }

/**
 * 
 * @param x
 * @param y
 * @param z
 * @param border pos for 'near' bounds, neg for 'inside' bounds
 * @return
 */
public boolean isInsideOrNearWorkBounds(int x, int y, int z, int border)
  {
  return x >=minX-border && x <=maxX+border && y >=minY-border && y<=maxY+border && z>=minZ-border && z<= maxZ+border;
  }

/**
 * get a random position within the work bounds of this work-block
 * @param border
 * @return
 */
public BlockPosition getPositionInBounds(int border)
  {
  int sx = this.maxX-this.minX+border;
  int sy = this.maxY-this.minY+border;
  int sz = this.maxZ-this.minZ+border;
  int x = minX-border + rng.nextInt(sx);
  int y = minY-border + rng.nextInt(sy);
  int z = minZ-border + rng.nextInt(sz);
  return new BlockPosition(x,y,z);
  }

public void addWorker(NpcBase npc)
  {
  if(npc!=null)
    {
    if(this.workers.isEmpty())
      {
      this.sendWorkerStatusUpdate(1);
      }
    this.workers.add(npc);
    }  
  }

public WorkPoint getWorkPoint(NpcBase npc)
  {
  if(!this.workers.contains(npc))
    {
    return null;
    }
  Iterator<WorkPoint> it = this.workPoints.iterator();
  WorkPoint p = null;
  while(it.hasNext())
    {
    p = it.next();
    if(p.hasWork(worldObj))
      {
      p.resetHarvestTicks();
      return p;
      }    
    }   
  return null;
  }

public void removeWorker(NpcBase npc)
  {
  this.workers.remove(npc);
  if(this.workers.isEmpty())
    {
    this.sendWorkerStatusUpdate(0);
    }
  }

public void onWorkFinished(NpcBase npc, WorkPoint point)
  {
  if(point!=null)
    {       
    this.workPoints.remove(point);
    }
  }

public void onWorkFailed(NpcBase npc, WorkPoint point)
  {
  this.onWorkFinished(npc, point);
  }

public void onWorkNoPath(NpcBase npc, WorkPoint point)
  {
  this.onWorkFinished(npc, point);
  }

/**
 * overridable method to update work-points. called from main onUpdate every X ticks
 * X=Config.npcAITicks * 10
 */
public void updateWorkPoints()
  {
 
  }

protected void validateWorkers()
  {
  Iterator<NpcBase> workIt = this.workers.iterator();
  NpcBase npc = null;
  while(workIt.hasNext())
    {
    npc = workIt.next();
    if(npc==null || npc.isDead || npc.getDistance(xCoord, yCoord, zCoord)>Config.npcAISearchRange)
      {
      workIt.remove();
      continue;
      }
    WayPoint p = npc.wayNav.getWorkSite();
    if(p==null || p.floorX()!= xCoord || p.floorY()!=yCoord || p.floorZ()!=zCoord)
      {
      workIt.remove();
      continue;
      }
    }
  }

public WorkPoint doWork(NpcBase npc, WorkPoint p)
  {
  p.incrementHarvestHits();  
  if(!p.hasWork(worldObj))
    {
    p.setHarvestHitToMax();
    }
  if(p.shouldFinish())
    {
    this.onWorkFinished(npc, p);
    return null;
    }
  return p;
  }

/**
 * return cached hasWork value
 * @return
 */
public boolean hasWork()
  {
  return hasWork;
  }

/**
 * update cached hasWork value...
 * only called from main onUpdate every X ticks
 * X = Config.npcAITicks*10
 */
protected void updateHasWork()
  {
  boolean hasWork = false;
  for(WorkPoint p : this.workPoints)
    {
    if(p.hasWork(worldObj))
      {
      hasWork = true;
      break;
      }
    }  
  this.setHasWork(hasWork);
  }

/**
 * set hasWork value, and send event to client
 * if it is a changed value
 * @param newVal
 */
protected void setHasWork(boolean newVal)
  {
  if(newVal!=this.hasWork)
    {
    if(newVal==true)
      {
      this.sendWorkStatusUpdate(1);
      }
    else
      {
      this.sendWorkStatusUpdate(0);
      }    
    }
  this.hasWork = newVal;
  }

/**
 * return a WORLD COORD bb for render
 * @return
 */
public AxisAlignedBB getBoundsForRender()
  { 
  return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX+1, maxY+1, maxZ+1);
  }

public AxisAlignedBB getSecondaryRenderBounds()
  {
  return null;
  }

/*****************************************************NBT/PACKETS*********************************************************/

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.readCivicDataFromNBT(tag);
  }

protected void readCivicDataFromNBT(NBTTagCompound tag)
  {
  if(tag.hasKey("civType"))
    {
    Civic civ = CivicRegistry.instance().getCivicFor(tag.getInteger("civType"));
    this.setCivic(civ);
    }
  else
    {
    this.setCivic(CivicRegistry.instance().getCivicFor(0));
    this.invalidate();
    }  
  if(tag.hasKey("bounds"))
    {
    int[] bounds = tag.getIntArray("bounds");
    this.minX = bounds[0];
    this.minY = bounds[1];
    this.minZ = bounds[2];
    this.maxX = bounds[3];
    this.maxY = bounds[4];
    this.maxZ = bounds[5];
    }
  else
    {
    this.minX = this.xCoord;
    this.minY = this.yCoord;
    this.minZ = this.zCoord;
    this.maxX = this.xCoord;
    this.maxY = this.yCoord;
    this.maxZ = this.zCoord;
    }
  if(tag.hasKey("team"))
    {
    this.teamNum = tag.getInteger("team");
    }
  if(tag.hasKey("inventory"))
    {
    this.inventory.readFromNBT(tag.getCompoundTag("inventory"));
    }  
  if(tag.hasKey("broad"))
    {
    this.broadcastWork = tag.getBoolean("broad");
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setInteger("civType", civic.getGlobalID());
  tag.setIntArray("bounds", new int[]{minX, minY, minZ, maxX, maxY, maxZ});
  tag.setCompoundTag("inventory", this.inventory.getNBTTag());
  tag.setInteger("team", this.teamNum);
  tag.setBoolean("broad", broadcastWork);
  }

@Override
public Packet getDescriptionPacket()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setIntArray("bounds", new int[]{minX, minY, minZ, maxX, maxY, maxZ});
  tag.setInteger("civType", this.civic.getGlobalID());
  tag.setInteger("team", this.teamNum); 
  tag.setBoolean("broad", broadcastWork);
  return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
  }


@Override
public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
  super.onDataPacket(net, pkt);
  this.readCivicDataFromNBT(pkt.customParam1);
  }

/**
 * called from AW packets on client-interaction/events
 * @param tag
 */
public void handlePacketData(NBTTagCompound tag)
  {  
  if(worldObj!=null && !worldObj.isRemote)
    {
    Packet05TE pkt = new Packet05TE();
    pkt.packetData = tag;
    }
  }
/******************************************************************EVENT HANDLING***********************************************************************************/


protected void sendWorkStatusUpdate(int val)
  {
  sendClientEvent(0, val);
  }

protected void sendInventoryStatusUpdate(int val)
  {
  sendClientEvent(1, val);
  }

protected void sendWorkerStatusUpdate(int val)
  {
  sendClientEvent(2, val);
  }

protected void sendClientEvent(int id, int val)
  {
  if(!worldObj.isRemote)
    {
    worldObj.addBlockEvent(xCoord, yCoord, zCoord, blockType.blockID, id, val);
    }
  }

@Override
public boolean receiveClientEvent(int par1, int par2)
  {
  switch(par1)
  {
  case 0://work status
  handleWorkStatusUpdate(par2);
  break;  
  case 1://inventory status
  handleInventoryStatusUpdate(par2);
  break;
  case 2://worker status
  handleWorkerStatusUpdate(par2);
  break;
  }
  return super.receiveClientEvent(par1, par2);
  }

public void handleInventoryStatusUpdate(int val)
  {
  clientInventoryStatus = val;
  }

public void handleWorkStatusUpdate(int val)
  {
  clientWorkStatus = val;
  }

public void handleWorkerStatusUpdate(int val)
  {
  clientWorkerStatus = val;
  }

/******************************************************************INVENTORY METHODS***********************************************************************************/

@Override
public int getSizeInventory()
  {
  return this.inventory.getSizeInventory();
  }

@Override
public ItemStack getStackInSlot(int var1)
  {
  return this.inventory.getStackInSlot(var1);
  }

@Override
public ItemStack decrStackSize(int var1, int var2)
  {
  return this.inventory.decrStackSize(var1, var2);
  }

@Override
public ItemStack getStackInSlotOnClosing(int var1)
  {
  return this.inventory.getStackInSlotOnClosing(var1);
  }

@Override
public void setInventorySlotContents(int var1, ItemStack var2)
  {
  this.inventory.setInventorySlotContents(var1, var2);  
  }

@Override
public String getInvName()
  {
  return "AWCivicInventory";
  }

@Override
public int getInventoryStackLimit()
  {
  return this.inventory.getInventoryStackLimit();
  }

@Override
public boolean isUseableByPlayer(EntityPlayer var1)
  {
  return !TeamTracker.instance().isHostileTowards(var1.worldObj, this.teamNum, TeamTracker.instance().getTeamForPlayer(var1));
  }

@Override
public void openChest()
  {
  }

@Override
public void closeChest()
  {
  }

@Override
public boolean isInvNameLocalized()
  {
  return true;
  }

@Override
public boolean isStackValidForSlot(int i, ItemStack itemstack)
  {  
  return true;
  }


}
