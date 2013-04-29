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
import shadowmage.ancient_warfare.common.network.Packet05TE;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public abstract class TECivic extends TileEntity implements IInventory
{

int updateTicks = 0;
int teamNum = 0;
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
protected Civic civic;
//protected int structureRank = 0;
protected List<WorkPoint> workPoints = new ArrayList<WorkPoint>();//points being worked currently
protected Set<NpcBase> workers = Collections.newSetFromMap(new WeakHashMap<NpcBase, Boolean>());
protected Random rng = new Random();
AxisAlignedBB primaryBounds;

protected boolean hasWork = false;


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
  primaryBounds = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX+1, maxY+1, maxZ+1);
  }

@Override
public void updateEntity()
  {
  if(updateTicks<=0 && this.worldObj!=null && !this.worldObj.isRemote)
    {
    this.updateHasWork();
    this.broadCastToSoldiers(Config.npcAISearchRange);
    this.updateWorkPoints();
    this.validateWorkers();    
    this.updateTicks = Config.npcAITicks;
    }
  else
    {
    updateTicks--;
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
      Config.log("found hostile npc!");
      if(npc.npcType.isCombatUnit())
        {
        //add attack entry
        //TODO
        }      
      }
    else
      {
      if(broadcastWork)
        {        
//        Config.logDebug("broadcasting to npcs!!");
        if(npc.wayNav.getWorkSite()==null && hasWork() && canHaveMoreWorkers(npc))
          {
//          Config.logDebug("Entity had no work site, checking if valid!");
          if(npc.npcType.getWorkTypes(npc.rank).contains(civic.getWorkType()))
            {
//            Config.logDebug("SETTING NPC WORK SITE THROUGH BROADCAST THROUGH TE");
            npc.wayNav.setWorkSite(xCoord, yCoord, zCoord);
            }
          }
        }
      }
    }
  }

public abstract boolean onInteract(World world, EntityPlayer player);

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
  this.workers.add(npc);
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
    if(!p.hasWorker() && p.hasWork(worldObj))
      {
      p.setWorked(npc);
      p.resetHarvestTicks();
      return p;
      }    
    }   
  return null;
  }

public void removeWorker(NpcBase npc)
  {
  this.workers.remove(npc);
  }

public void onWorkFinished(NpcBase npc, WorkPoint point)
  {
  if(point!=null)
    {    
    point.setFinished();
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

public void updateWorkPoints()
  {
  /**
   * check through current active queue, remove any entries that are invalid
   */
  Iterator<WorkPoint> it = this.workPoints.iterator();
  WorkPoint p;
  while(it.hasNext())
    {
    p = it.next();
    if(!p.isValidEntry(worldObj))
      {
      it.remove();
      continue;
      }
    if(p.hasWorker())
      {
      if(p.getWorker().wayNav.getWorkPoint()!=p)
        {
        p.setWorked(null);
        }
      }
    }
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

public boolean hasWork()
  {
  return hasWork;
  }

protected void updateHasWork()
  {
  hasWork = false;
  for(WorkPoint p : this.workPoints)
    {
    if(p.getWorker()==null && p.hasWork(worldObj))
      {
      hasWork = true;
      break;
      }
    }  
  }

/**
 * return a WORLD COORD bb for render
 * @return
 */
public AxisAlignedBB getBoundsForRender()
  { 
  primaryBounds = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX+1, maxY+1, maxZ+1);
  return primaryBounds;
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
