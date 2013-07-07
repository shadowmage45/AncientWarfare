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
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.ITEWorkSite;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBase;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.network.Packet05TE;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.ServerPerformanceMonitor;

public abstract class TECivic extends TileEntity implements IInventory, ITEWorkSite
{

protected static Random rng = new Random();
private static int teInstanceIDNext = 0;

protected int ticksExisted = 0;
protected int teID = 0;
protected int tickDivider = Config.npcAITicks * 10;

/**
 * work-site bounds, may be un-set for non work-site civics
 */
public int minX;
public int minY;
public int minZ;
public int maxX;
public int maxY;
public int maxZ;
protected int teamNum = 0;
protected boolean isWorkSite = false;
protected boolean renderBounds = false;
public boolean broadcastWork = true;//user toggle...spawned NPC buildings will auto-broadcast
public AWInventoryBase inventory = null;
public AWInventoryBase overflow = new AWInventoryBasic(4);
protected Civic civic = (Civic) Civic.wheatFarm;//dummy/placeholder...

protected int[] resourceSlotIndices;
protected int[] otherSlotIndices;
protected Set<IWorker> workers = Collections.newSetFromMap(new WeakHashMap<IWorker, Boolean>());
protected boolean hasWork = false;

public TECivic()
  {
  teID = teInstanceIDNext;
  this.teInstanceIDNext++;
  }

/***************************************************SETUP/INIT**************************************************************/
public void setCivic(Civic civ)
  {
  this.civic = civ;
  if(inventory==null)
    {
    inventory = new AWInventoryBasic(civ.getInventorySize());
    }
  this.setupSidedInventoryIndices(civ);
  }

public void setupSidedInventoryIndices(Civic civ)
  {
  resourceSlotIndices = new int[civ.getResourceSlotSize()];
  for(int i = 0; i< civ.getResourceSlotSize(); i++)
    {
    resourceSlotIndices[i]=i;
    }
  otherSlotIndices = new int[civ.getInventorySize()-civ.getResourceSlotSize()];
  for(int i = civ.getResourceSlotSize(); i < civ.getInventorySize(); i++)
    {
    otherSlotIndices[i-civ.getResourceSlotSize()]=i;
    }
  }

public boolean shouldRenderMainBounds()
  {
  return this.renderBounds;
  }

public IInventory[] getInventoryToDropOnBreak()
  {
  return new IInventory[]{this, overflow};
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
  this.onPlaced();
  }

public boolean isPointInBounds(int x, int y, int z)
  {
  return x>=this.minX && x<=this.maxX && y>=this.minY && y<=this.maxY && z>=this.minZ && z<=this.maxZ;  
  }

public boolean doesBBIntersect(AxisAlignedBB bb)
  {
  AxisAlignedBB work = this.getWorkBounds();
  AxisAlignedBB extra = this.getSecondaryRenderBounds();
  if(bb==null || work==null)
    {
    return false;
    }
  if(bb.intersectsWith(work) || work.intersectsWith(bb))
    {
    return true;
    }
  else if(extra!=null)
    {
    if(bb.intersectsWith(extra) || extra.intersectsWith(bb))
      {
      return true;
      }
    }
  return false;
  }

public boolean isWorker(IWorker worker)
  {
  return this.workers.contains(worker);
  }

protected void tryAddItemToInventory(ItemStack item, int[] ... slotIndices)
  {
  if(item==null || slotIndices==null){return;}
  for(int[] indices : slotIndices)
    {
    if(item==null){return;}
    item = inventory.tryMergeItem(item, indices);
    }
  item = overflow.tryMergeItem(item);
  InventoryTools.dropItemInWorld(worldObj, item, xCoord, yCoord, zCoord);
  }

@Override
public void updateEntity()
  {
  long t1 = System.nanoTime();
  ticksExisted++;
  if(this.worldObj!=null && !this.worldObj.isRemote && (this.ticksExisted+this.teID)% tickDivider == 0 )
    {
    this.onCivicUpdate();
    }   
  super.updateEntity();
  ServerPerformanceMonitor.addCivicTickTime(System.nanoTime() - t1);
  }

protected void onCivicUpdate()
  {
  this.validateWorkers();
  this.updateHasWork();
  this.broadcastWork(Config.civicBroadcastRange);
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

public void broadcastWork(int maxRange)
  {
  if(this.worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1).expand(maxRange, maxRange/2, maxRange);
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
        if(hasWork() && canHaveMoreWorkers(npc) && npc.npcType.getWorkTypes(npc.rank).contains(civic.getWorkType()) && npc.teamNum==this.teamNum)
          {
          npc.targetHelper.handleTileEntityTargetBroadcast(this, TargetType.WORK, Config.npcAITicks*11);
          }
        }
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

@Override
public CivicWorkType getWorkType()
  {
  return this.getCivic().getWorkType();
  }

@Override
public boolean isWorkSite()
  {
  return this.getCivic().isWorkSite();
  }

public TECivicWarehouse getWarehousePosition()
  {
  if(this instanceof TECivicWarehouse)
    {
    return null;
    }
  TileEntity te;
  te = worldObj.getBlockTileEntity(xCoord-1, yCoord, zCoord);  
  if(te instanceof TECivicWarehouse)
    {
    return (TECivicWarehouse)te;
    }
  te = worldObj.getBlockTileEntity(xCoord+1, yCoord, zCoord);
  if(te instanceof TECivicWarehouse)
    {
    return (TECivicWarehouse)te;
    }
  te = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord-1);
  if(te instanceof TECivicWarehouse)
    {
    return (TECivicWarehouse)te;
    }
  te = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord+1);
  if(te instanceof TECivicWarehouse)
    {
    return (TECivicWarehouse)te;
    }
  return null;
  }

@Override
public boolean canHaveMoreWorkers(IWorker npc)
  {  
  if(this.workers.contains(npc) && this.workers.size() <= this.civic.getMaxWorkers())
    {
    return true;
    }
  else if(this.workers.size()+1 <= this.civic.getMaxWorkers())
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

@Override
public void addWorker(IWorker npc)
  {
  if(npc!=null && !this.workers.contains(npc))
    {
    if(this.workers.isEmpty())
      {
      this.sendWorkerStatusUpdate(1);
      }
    this.workers.add(npc);
    }  
  }

@Override
public void doWork(IWorker npc)
  {
  
  }

@Override
public void removeWorker(IWorker npc)
  {
  this.workers.remove(npc);
  if(this.workers.isEmpty())
    {
    this.sendWorkerStatusUpdate(0);
    }
  }

protected void validateWorkers()
  {
  Iterator<IWorker> workIt = this.workers.iterator();
  IWorker npc = null;
  while(workIt.hasNext())
    {
    npc = workIt.next();
    if(npc==null)
      {      
      workIt.remove();
      continue;
      }
    if(npc.isDead())
      {
      workIt.remove();
      continue;
      }
    if(npc.getDistanceFrom(xCoord, yCoord, zCoord)>Config.npcAISearchRange)
      {
      workIt.remove();
      continue;
      }
    WayPoint p = npc.getWorkPoint();
    if(p==null || p.floorX()!= xCoord || p.floorY()!=yCoord || p.floorZ()!=zCoord)
      {
      workIt.remove();
      continue;
      }
    ITEWorkSite  te = npc.getWorkSite();
    if(te!=this)
      {
      workIt.remove();
      continue;
      }
    }
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
  this.setHasWork(false);
  }

/**
 * set hasWork value, and send event to client
 * if it is a changed value
 * @param newVal
 */
protected void setHasWork(boolean newVal)
  { 
  this.hasWork = newVal;
  }

/**
 * return a WORLD COORD bb for render, or entity BB selection
 * @return
 */
public AxisAlignedBB getWorkBounds()
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
  if(tag.hasKey("overflow"))
    {
    this.overflow.readFromNBT(tag.getCompoundTag("overflow"));
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
  if(this.inventory!=null)
  {
	  tag.setCompoundTag("inventory", this.inventory.getNBTTag());	  
  }
  tag.setCompoundTag("overflow", this.overflow.getNBTTag());
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

public void sendBroadCastWork(boolean val)
  {
  if(val)
    {
    this.sendClientEvent(0, 1);
    }
  else
    {
    this.sendClientEvent(0, 0);
    }
  }

protected void sendClientEvent(int id, int val)
  {
  if(worldObj.isRemote)
    {
    worldObj.addBlockEvent(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord), id, val);
    }
  }

@Override
public boolean receiveClientEvent(int par1, int par2)
  {
  switch(par1)
  {
  case 0://
  if(par2==0)
    {
    this.broadcastWork = false;
    }
  else
    { 
    this.broadcastWork = true;
    }
  this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
  break;  
  case 1://
  break;
  case 2://
  break;
  }
  return super.receiveClientEvent(par1, par2);
  }

public void onPlaced()
  {
  
  }

/******************************************************************INVENTORY METHODS***********************************************************************************/
public boolean resourceFilterContains(ItemStack stack)
  {
  if(stack==null)
    {
    return false;
    }
  for(ItemStack filter : this.getCivic().getResourceItemFilters())
    {
    if(InventoryTools.doItemsMatch(stack, filter))
      {
      return true;
      }
    }
  return false;
  }

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
