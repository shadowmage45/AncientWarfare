/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
package shadowmage.ancient_warfare.common.item;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.InventoryTools;
import shadowmage.ancient_framework.common.utils.RayTraceUtils;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.commands.NpcCommand;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class ItemNpcCommandBaton extends AWItemClickable
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemNpcCommandBaton(int itemID)
  {
  super(itemID, true);
  this.maxStackSize = 1;
  this.hasLeftClick = true;
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  { 
  if(world.isRemote){return false;}
  if(player.isSneaking())
    {
    GUIHandler.instance().openGUI(GUIHandler.instance().NPC_COMMAND_BATON, player, world, 0, 0, 0);
    }
  else
    {
    if(true)
      {
      HashSet<Entity> excluded = new HashSet<Entity>();
      excluded.add(player);
      if(player.ridingEntity!=null)
        {
        excluded.add(player.ridingEntity);
        }
      MovingObjectPosition pos = RayTraceUtils.getPlayerTarget(player, 140.f, 1.f);
      if(pos!=null)
        {
        if(pos.entityHit!=null)
          {
          handleNpcAttackCommand(player, stack, getBatonSettings(stack), pos);
          }
        else
          {
          handleNpcCommand(player, stack, getBatonSettings(stack), pos, NpcCommand.CLEAR_PATROL);
          handleNpcCommand(player, stack, getBatonSettings(stack), pos, NpcCommand.PATROL);
          }
        Config.logDebug("hit pos: "+pos.hitVec + " entity: "+pos.entityHit);
        }
      else
        {
        Config.logDebug(" NULL HIT ");
        }
      }
    }
  return false;
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  super.addInformation(stack, player, list, par4);   
  list.add("Left Click: Execute Baton Command");
  list.add("Right Click: Command attack/move to");
  list.add("(Sneak)Right Click: Open GUI");
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("batonSettings"))
    {
    NBTTagCompound tag = stack.getTagCompound().getCompoundTag("batonSettings");
    if(tag.hasKey("com"))
      {
      list.add("Current command: "+NpcCommand.values()[tag.getInteger("com")]);
      }
    if(tag.hasKey("rng"))
      {
      list.add("Range: "+tag.getInteger("rng"));
      }
    }
  else
    {
    list.add("No command");
    }    
  }

/**
 * left-click attack, prior to processing..
 */
@Override
public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
  {  
  if(!player.worldObj.isRemote)
    {
    MovingObjectPosition hit = new MovingObjectPosition(entity);
    BatonSettings settings = getBatonSettings(stack);
    if(entity instanceof NpcBase)
      {
      NpcBase npc = (NpcBase)entity;
      if(npc.teamNum==16)
        {
        ItemStack ration = new ItemStack(ItemLoader.rations,1);
        int count = InventoryTools.getCountOf(player.inventory, ration, 0, player.inventory.getSizeInventory()-5);
        if(count >= npc.npcType.getUpkeepCost(npc.rank)/2)
          {
          npc.teamNum = TeamTracker.instance().getTeamForPlayer(player);
          InventoryTools.tryRemoveItems(player.inventory, ration, npc.npcType.getUpkeepCost(npc.rank)/2, 0, player.inventory.getSizeInventory()-5);
          player.addChatMessage("Converting NPC to your team!");
          return true;
          }
        }
      }
    if(settings.command == NpcCommand.MOUNT)
      {
      if(entity instanceof VehicleBase && entity.riddenByEntity==null)
        {
        this.handleNpcCommand(player, stack, settings, hit, settings.command);
        }
      }
    else if(settings.command == NpcCommand.GUARD)
      {
      this.handleNpcCommand(player, stack, settings, hit, settings.command);
      }
    return true;
    }  
  return super.onLeftClickEntity(stack, player, entity);
  }

protected void handleNpcAttackCommand(EntityPlayer player, ItemStack stack, BatonSettings settings, MovingObjectPosition hit)
  {
  NpcBase npc = null;
  
  if(hit==null || hit.entityHit==null)
    {
    return;
    }    
  int range = settings.range < 20 ? 20 : settings.range;
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().getAABB(player.posX-range, player.posY-range, player.posZ-range, player.posX+range, player.posY+range, player.posZ+range);
  List<NpcBase> npcs = player.worldObj.getEntitiesWithinAABB(NpcBase.class, bb);
  
  Iterator<NpcBase> it = npcs.iterator();
  int commanded = 0;
  int followCommanded = 0;
  while(it.hasNext())
    {
    npc = it.next();  
    if(!npc.npcType.isCombatUnit())
      {
      continue;
      }
    if(npc.getPlayerTarget()!=null && npc.getPlayerTarget().getEntity(npc.worldObj)==player)
      {
      followCommanded++;
      npc.setTargetAW(null);
      npc.handleBroadcastAttackTarget(hit.entityHit, 1000);
      }    
    else if(player.getDistanceToEntity(npc)<settings.range && !npc.isAggroTowards(player) && npc.teamNum<15)
      {
      commanded++;
      npc.setTargetAW(null);
      npc.handleBroadcastAttackTarget(hit.entityHit, 1000);
      }   
    }  
  player.addChatMessage("Commanding "+(commanded + followCommanded)+ " Npcs to attack " + hit.entityHit.getTranslatedEntityName());
  }

protected void handleNpcCommand(EntityPlayer player, ItemStack stack, BatonSettings settings, MovingObjectPosition hit, NpcCommand cmd)
  {
  NpcBase npc = null;
  
  if(hit==null)
    {
    return;
    }
  if(cmd==NpcCommand.NONE)
    {
    return;
    }
  WayPoint p = null;
  if(hit.entityHit!=null)
    {
    p = new WayPoint(hit.entityHit, cmd.getTargetType());
    }
  else if(player.worldObj.getBlockTileEntity(hit.blockX, hit.blockY, hit.blockZ)!=null)
    {
    p = new WayPoint(player.worldObj.getBlockTileEntity(hit.blockX, hit.blockY, hit.blockZ), hit.sideHit, cmd.getTargetType());
    }
  else
    {
    p = new WayPoint(hit.blockX, hit.blockY, hit.blockZ, hit.sideHit, cmd.getTargetType());
    }
  int range = settings.range < 20 ? 20 : settings.range;
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().getAABB(player.posX-range, player.posY-range, player.posZ-range, player.posX+range, player.posY+range, player.posZ+range);
  List<NpcBase> npcs = player.worldObj.getEntitiesWithinAABB(NpcBase.class, bb);
  WayPoint pt;
  Iterator<NpcBase> it = npcs.iterator();
  int commanded = 0;
  int followCommanded = 0;
  
  Config.logDebug(" commanding for wp: "+p);
  while(it.hasNext())
    {
    npc = it.next();    
    if(npc.getPlayerTarget()!=null && npc.getPlayerTarget().getEntity(npc.worldObj)==player)
      {
      followCommanded++;
      pt = new WayPoint(p);
      npc.handleBatonCommand(cmd, pt);
      }    
    else if(player.getDistanceToEntity(npc) < settings.range && !npc.isAggroTowards(player) && npc.teamNum<=15)
      {
      commanded++;
      pt = new WayPoint(p);
      npc.handleBatonCommand(cmd, pt);
      }
    if(cmd.isSingleTargetOnly(cmd) && commanded+followCommanded>=1)
      {
      break;
      }
    }  
  player.addChatMessage("Commanding "+(commanded + followCommanded)+ " Npcs!");
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(!player.worldObj.isRemote)
    {
    BatonSettings settings = getBatonSettings(stack);
    this.handleNpcCommand(player, stack, settings, new MovingObjectPosition(hit.x, hit.y, hit.z, side, Vec3.fakePool.getVecFromPool(0, 0, 0)), settings.command);
    }   
  return false;
  }

public BatonSettings getBatonSettings(ItemStack stack)
  {
  if(stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey("batonSettings"))
    {
    return new BatonSettings(stack.getTagCompound().getCompoundTag("batonSettings"));
    }
  return new BatonSettings();
  }

public static BatonSettings getBatonSettingsStatic(ItemStack stack)
  {
  ItemNpcCommandBaton baton = (ItemNpcCommandBaton)ItemLoader.npcCommandBaton;
  return baton.getBatonSettings(stack);
  }

public static void setBatonSettings(ItemStack stack, BatonSettings settings)
  {
  if(stack!=null && settings!=null)
    {
    stack.setTagInfo("batonSettings", settings.getNBTTag());
    }
  }

public class BatonSettings implements INBTTaggable
{

public int range = 0;
public NpcCommand command = NpcCommand.NONE;

private BatonSettings()
  {
  command = NpcCommand.NONE;
  }

private BatonSettings(NBTTagCompound tag)
  {
  this.readFromNBT(tag);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound(); 
  if(command!=null)
    {
    tag.setInteger("com", command.ordinal());
    }
  tag.setInteger("rng", range);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {  
  if(tag.hasKey("com"))
    {
    command = NpcCommand.values()[tag.getInteger("com")];
    }
  else 
    {
    command = NpcCommand.NONE;
    }
  if(tag.hasKey("rng"))
    {
    range = tag.getInteger("rng");
    }
  }
}


}
