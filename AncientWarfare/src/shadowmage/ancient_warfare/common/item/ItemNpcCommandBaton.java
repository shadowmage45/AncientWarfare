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

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.commands.NpcCommand;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.EntityTools;

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
    if(tag.hasKey("uidlsb") && tag.hasKey("uidmsb"))
      {
      list.add("Has Npc to command, left click a block to execute current command");
      }
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
    list.add("Un-initialized Baton, right click to open GUI");
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
public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player,   Entity entity)
  {
  if(!player.worldObj.isRemote)
    {
    if(entity instanceof NpcBase)
      {
      BatonSettings settings = getBatonSettings(stack);
      if(!settings.hasEntity(entity))
        {
        player.addChatMessage("Setting Baton Main Target");
        settings.setEntity(entity);
        setBatonSettings(stack, settings);
        player.openContainer.detectAndSendChanges();
        }
      return true;
      }
    }  
  return super.onLeftClickEntity(stack, player, entity);
  }

@Override
public boolean onBlockStartBreak(ItemStack stack, int X, int Y, int Z, EntityPlayer player)
  { 
  if(!player.worldObj.isRemote)
    {
    MovingObjectPosition hit = getMovingObjectPositionFromPlayer(player.worldObj, player, true);
    if(hit==null)
      {
      return true;
      }
    BatonSettings settings = getBatonSettings(stack);
    NpcCommand cmd = settings.command;
    if(cmd==NpcCommand.NONE)
      {
      return true;
      }
    NpcBase npc = null;
    if(settings.hasEntity())
      {
      Entity entity = settings.getEntity(player.worldObj);
      if(entity instanceof NpcBase)
        {
        npc = (NpcBase)entity;
        } 
      else
        {
//        Config.logDebug("Baton has invalid entity!!");
        settings.setEntity(null);
        setBatonSettings(stack, settings);
        player.openContainer.detectAndSendChanges();
        }
      }    
    if(settings.range>0)
      {   
      player.addChatMessage("Executing Baton Command on Area");
      AxisAlignedBB bb = AxisAlignedBB.getAABBPool().getAABB(player.posX-settings.range, player.posY-settings.range, player.posZ-settings.range, player.posX+settings.range, player.posY+settings.range, player.posZ+settings.range);
      List<NpcBase> npcs = player.worldObj.getEntitiesWithinAABB(NpcBase.class, bb);
      int npcType = -1;
      if(npc!=null)
        {
        npc.handleBatonCommand(settings.command, X, Y, Z, hit.sideHit);
        npcType = npc.npcType.getGlobalNpcType();
        }
      for(NpcBase testNpc : npcs)
        {
        if(testNpc==null || (npc!=null && testNpc == npc) || player.getDistanceToEntity(testNpc)>settings.range || testNpc.isAggroTowards(player))
          {
          continue;
          }
        if(npcType==-1 || testNpc.npcType.getGlobalNpcType()==npcType)
          {
          testNpc.handleBatonCommand(settings.command, X, Y, Z, hit.sideHit);
          }        
        }
      }
    else
      {
      if(npc!=null)
        {
        player.addChatMessage("Executing Baton Command on Single Target");
        npc.handleBatonCommand(settings.command, X, Y, Z, hit.sideHit);
        }      
      }
    }   
  return true;
//  return super.onBlockStartBreak(stack, X, Y, Z, player);
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

public static NpcCommand[] getApplicableCommands(ItemStack stack)
  {
  if(stack!=null && stack.itemID == ItemLoader.instance().npcCommandBaton.itemID)
    {
    switch(stack.getItemDamage())
    {
    case 0:
    return new NpcCommand[]{NpcCommand.WORK, NpcCommand.PATROL, NpcCommand.HOME, NpcCommand.DEPOSIT, NpcCommand.CLEAR_DEPOSIT, NpcCommand.CLEAR_HOME, NpcCommand.CLEAR_PATROL, NpcCommand.CLEAR_WORK};
    case 1:
    return new NpcCommand[]{NpcCommand.WORK, NpcCommand.PATROL, NpcCommand.HOME, NpcCommand.DEPOSIT, NpcCommand.CLEAR_DEPOSIT, NpcCommand.CLEAR_HOME, NpcCommand.CLEAR_PATROL, NpcCommand.CLEAR_WORK,
        NpcCommand.MASS_HOME, NpcCommand.MASS_CLEAR_HOME};
    case 2:
    return new NpcCommand[]{NpcCommand.WORK, NpcCommand.PATROL, NpcCommand.HOME, NpcCommand.DEPOSIT, NpcCommand.CLEAR_DEPOSIT, NpcCommand.CLEAR_HOME, NpcCommand.CLEAR_PATROL, NpcCommand.CLEAR_WORK,
        NpcCommand.MASS_HOME, NpcCommand.MASS_CLEAR_HOME, NpcCommand.MASS_CLEAR_WORK, NpcCommand.MASS_WORK};
    case 3:
    return new NpcCommand[]{NpcCommand.WORK, NpcCommand.PATROL, NpcCommand.HOME, NpcCommand.DEPOSIT, NpcCommand.CLEAR_DEPOSIT, NpcCommand.CLEAR_HOME, NpcCommand.CLEAR_PATROL, NpcCommand.CLEAR_WORK,
        NpcCommand.MASS_HOME, NpcCommand.MASS_CLEAR_HOME, NpcCommand.MASS_DEPOSIT, NpcCommand.MASS_CLEAR_DEPOSIT, NpcCommand.MASS_CLEAR_WORK, NpcCommand.MASS_WORK, NpcCommand.MASS_PATROL, NpcCommand.MASS_CLEAR_PATROL};    
    }
    }
  return null;
  }

public class BatonSettings implements INBTTaggable
{

public int range = 0;
public NpcCommand command;
UUID entID;

private BatonSettings()
  {
  command = NpcCommand.NONE;
  }

private BatonSettings(NBTTagCompound tag)
  {
  this.readFromNBT(tag);
  }

public boolean hasEntity()
  {
  return this.entID!=null;
  }

public Entity getEntity(World world)
  {
  return entID==null ? null : EntityTools.getEntityByUUID(world, entID.getMostSignificantBits(), entID.getLeastSignificantBits());
  }

public boolean hasEntity(Entity entity)
  {
  UUID id = entity.getPersistentID();
  return entID!=null && id!=null && entID.getMostSignificantBits() == id.getMostSignificantBits() && entID.getLeastSignificantBits() == id.getLeastSignificantBits();
  }

public UUID getEntityID()
  {
  return this.entID;
  }

public void setEntity(Entity entity)
  {
  if(entity!=null)
    {
    UUID id = entity.getPersistentID();
    if(id!=null)
      {
      this.entID = new UUID(id.getMostSignificantBits(), id.getLeastSignificantBits());
      }
    }
  else
    {
    this.entID = null;
    }
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();   
  if(entID!=null)
    {
    tag.setLong("uidlsb", entID.getLeastSignificantBits());
    tag.setLong("uidmsb", entID.getMostSignificantBits());
    }
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
  if(tag.hasKey("uidlsb") && tag.hasKey("uidmsb"))
    {
    entID = new UUID(tag.getLong("uidmsb"), tag.getLong("uidlsb"));
    }
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
