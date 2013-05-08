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
package shadowmage.ancient_warfare.common.item;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.commands.NpcCommand;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.EntityTools;
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
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  { 
  if(!world.isRemote)
    {
    GUIHandler.instance().openGUI(GUIHandler.instance().NPC_COMMAND_BATON, player, world, 0, 0, 0);
    }
  return false;
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  super.addInformation(stack, player, list, par4);
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("batonSettings"))
    {
    NBTTagCompound tag = stack.getTagCompound().getCompoundTag("batonSettings");   
    list.add("Left-Click a block or entity");
    list.add("to execute current command.");
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
    list.add("No command-right click to set");
    }    
  }

/**
 * right click on an entity
 */
@Override
public boolean itemInteractionForEntity(ItemStack par1ItemStack,   EntityLiving par2EntityLiving)
  {  
  return super.itemInteractionForEntity(par1ItemStack, par2EntityLiving);
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
    if(settings.command == NpcCommand.DEPOSIT || settings.command==NpcCommand.UPKEEP)
      {
      boolean transmit = false;
      if(entity instanceof IInventory && ((IInventory)entity).getSizeInventory()>0)
        {
        this.handleNpcCommand(player, stack, settings, hit);
        }  
      } 
    else if(settings.command == NpcCommand.MOUNT)
      {
      if(entity instanceof VehicleBase && entity.riddenByEntity==null)
        {
        this.handleNpcCommand(player, stack, settings, hit);
        }
      }
    return true;
    }  
  return super.onLeftClickEntity(stack, player, entity);
  }

protected void handleNpcCommand(EntityPlayer player, ItemStack stack, BatonSettings settings, MovingObjectPosition hit)
  {
  NpcBase npc = null;
  NpcCommand cmd = settings.command;
  
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
  while(it.hasNext())
    {
    npc = it.next();
    if(npc.getPlayerTarget()!=null && npc.getPlayerTarget().getEntity(npc.worldObj)==player)
      {
      followCommanded++;
      pt = new WayPoint(p);
      npc.handleBatonCommand(cmd, p);
      }    
    else if(player.getDistanceToEntity(npc)<settings.range)
      {
      commanded++;
      pt = new WayPoint(p);
      npc.handleBatonCommand(cmd, p);
      }
    if(cmd.isSingleTargetOnly(cmd) && commanded+followCommanded>=1)
      {
      break;
      }
    }  
  player.addChatMessage("Commanding "+(commanded + followCommanded)+ " Npcs!");
  }

@Override
public boolean onBlockStartBreak(ItemStack stack, int X, int Y, int Z, EntityPlayer player)
  { 
  if(!player.worldObj.isRemote)
    {
    MovingObjectPosition hit = getMovingObjectPositionFromPlayer(player.worldObj, player, true);
    BatonSettings settings = getBatonSettings(stack);
    this.handleNpcCommand(player, stack, settings, hit);
    }   
  return true;
  }

@Override
public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
  {
  return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
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
