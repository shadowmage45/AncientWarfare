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

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
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
  this.setItemName("npcCommandBaton");  
  this.maxStackSize = 1;
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  MovingObjectPosition hitPos = this.getMovingObjectPositionFromPlayer(world, player, true);
  if(hitPos!=null && hitPos.typeOfHit == EnumMovingObjectType.ENTITY)
    {
    Config.logDebug("onUsedFinal hit entity: "+hitPos.entityHit);
    }  
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
      list.add("Current command: "+Command.values()[tag.getInteger("com")]);
      }
    if(tag.hasKey("rng"))
      {
      list.add("Range: "+tag.getInteger("rng"));
      }
//    BatonSettings settings = getBatonSettings(stack);
//    if(settings.hasEntity())
//      {
//      
//      }
//    if(settings.hasBlock)
//      {
//      list.add("Has target position");
//      }    
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
 * left-click attack, when hit an entity-living
 */
@Override
public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving)
  {
  if(!par3EntityLiving.worldObj.isRemote)
    {
    Config.logDebug("player hit entityLiving with command baton : "+par2EntityLiving);
    }
  return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
  }

/**
 * left-click attack, prior to processing..
 */
@Override
public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player,   Entity entity)
  {
  if(!player.worldObj.isRemote)
    {
    Config.logDebug("player left click entity with command baton : "+entity);
    if(entity instanceof NpcBase)
      {
      BatonSettings settings = getBatonSettings(stack);
      if(!settings.hasEntity(entity))
        {
        Config.logDebug("setting baton entity");
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
    BatonSettings settings = getBatonSettings(stack);
    Command cmd = settings.command;
    if(cmd.isMassEffect())
      {
      
      }
    if(settings.hasEntity())
      {
      Entity entity = settings.getEntity(player.worldObj);
      if(entity instanceof NpcBase)
        {
        NpcBase npc = (NpcBase)entity;
        Config.logDebug("commanding entity from settings");
        } 
      else
        {
        settings.setEntity(null);
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

@Override
public String getItemNameIS(ItemStack par1ItemStack)
  {
  return "Baton" + String.valueOf(par1ItemStack.getItemDamage()); 
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

public static Command[] getApplicableCommands(ItemStack stack)
  {
  if(stack!=null && stack.itemID == ItemLoader.instance().npcCommandBaton.itemID)
    {
    switch(stack.getItemDamage())
    {
    case 0:
    return new Command[]{Command.WORK, Command.PATROL, Command.HOME, Command.DEPOSIT, Command.CLEAR_DEPOSIT, Command.CLEAR_HOME, Command.CLEAR_PATROL, Command.CLEAR_WORK};
    case 1:
    return new Command[]{Command.WORK, Command.PATROL, Command.HOME, Command.DEPOSIT, Command.CLEAR_DEPOSIT, Command.CLEAR_HOME, Command.CLEAR_PATROL, Command.CLEAR_WORK,
        Command.MASS_HOME, Command.MASS_CLEAR_HOME};
    case 2:
    return new Command[]{Command.WORK, Command.PATROL, Command.HOME, Command.DEPOSIT, Command.CLEAR_DEPOSIT, Command.CLEAR_HOME, Command.CLEAR_PATROL, Command.CLEAR_WORK,
        Command.MASS_HOME, Command.MASS_CLEAR_HOME, Command.MASS_CLEAR_WORK, Command.MASS_WORK};
    case 3:
    return new Command[]{Command.WORK, Command.PATROL, Command.HOME, Command.DEPOSIT, Command.CLEAR_DEPOSIT, Command.CLEAR_HOME, Command.CLEAR_PATROL, Command.CLEAR_WORK,
        Command.MASS_HOME, Command.MASS_CLEAR_HOME, Command.MASS_CLEAR_WORK, Command.MASS_WORK, Command.MASS_PATROL, Command.MASS_CLEAR_PATROL};    
    }
    }
  return null;
  }

public class BatonSettings implements INBTTaggable
{

/**
 * work block/patrol position
 */
boolean hasBlock = false;
int x;
int y;
int z;
public int range = 0;
public Command command;
UUID entID;

private BatonSettings()
  {
  command = Command.NONE;
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
  if(hasBlock)
    {
    tag.setIntArray("pos", new int[]{x,y,z});
    }
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
  if(tag.hasKey("pos"))
    {
    hasBlock = true;
    int[] pos = tag.getIntArray("pos");
    x = pos[0];
    y = pos[1];
    z = pos[2];
    }
  if(tag.hasKey("uidlsb") && tag.hasKey("uidmsb"))
    {
    entID = new UUID(tag.getLong("uidmsb"), tag.getLong("uidlsb"));
    }
  if(tag.hasKey("com"))
    {
    command = Command.values()[tag.getInteger("com")];
    }
  else 
    {
    command = Command.NONE;
    }
  if(tag.hasKey("rng"))
    {
    range = tag.getInteger("rng");
    }
  }
}

public enum Command
{
NONE ("None", false),
WORK ("Work At Site", false),
PATROL ("Patrol Point", false),
HOME ("Set Home Point", false),
DEPOSIT ("Set Depository", false),
CLEAR_PATROL ("Clear Patrol Path", false),
CLEAR_HOME ("Clear Home Point", false),
CLEAR_WORK ("Clear Work Site", false),
CLEAR_DEPOSIT ("Clear Depository", false),
MASS_PATROL ("Area Set Patrol Point", true),
MASS_HOME ("Area Set Home Point", true),
MASS_WORK ("Area Set Work Point", true),
MASS_DEPOSIT ("Area Set Depository", true),
MASS_CLEAR_PATROL ("Area Clear Patrol Path", true),
MASS_CLEAR_HOME ("Area Clear Home Point", true),
MASS_CLEAR_WORK ("Area Clear Work Site", true),
MASS_CLEAR_DEPOSIT ("Area Clear Depository", true);

String name;
boolean massEffect = false;
private Command(String name, boolean mass)
  {
  this.name = name;
  this.massEffect = mass;
  }

public String getCommandName()
  {
  return name;
  }

public boolean isMassEffect()
  {
  return this.massEffect;
  }
}

}
