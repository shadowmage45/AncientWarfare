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
package shadowmage.ancient_warfare.common.container;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.crafting.TEAWVehicleCraft;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class ContainerVehicleCrafting extends ContainerBase
{

TEAWVehicleCraft te;
public boolean isWorking;
public ResourceListRecipe clientRecipe;
public ResourceListRecipe currentRecipe;
public int displayProgress;
public int displayProgressMax;
public PlayerEntry entry = null;
int researchLength = 0;
/**
 * @param openingPlayer
 * @param synch
 */
public ContainerVehicleCrafting(EntityPlayer openingPlayer, TEAWVehicleCraft te)
  {
  super(openingPlayer, null);
  this.te = te;
  this.addPlayerSlots(player, 8, 156, 4);

  int posX = 8;
  int posY = 8+24;
  int slotNum = 0;
  Slot slot;
  for(int y = 0; y < 3; y++)
    {
    for(int x = 0; x <3; x++)
      {
      posX = 8 + x * 18;
      posY = 8 + 24 + 18 + 4 + 18 + 4;
      posY += y * 18;
      slotNum = y * 3 + x;
      slot = new Slot(te, slotNum, posX, posY);
      this.addSlotToContainer(slot);
      }
    }
  slot = new SlotPullOnly(te, 9, 8 + 3* 18 + 18 , 8 + 24 + 18 + 4 + 18 + 4 + 18);
  this.addSlotToContainer(slot);
 
  slot = new SlotResourceOnly(te, 10, 8 , 8 + 24, Arrays.asList(new ItemStack(ItemLoader.researchBook))).setIgnoreType(3).setMaxStackSize(1);
  this.addSlotToContainer(slot);
  }

public void addSlots()
  {
  Slot s;
  for(Object o : this.inventorySlots)
    {
    s = (Slot)o;
    if(s.yDisplayPosition<0)
      {
      s.yDisplayPosition+=1000;      
      }
    }
  }

public void removeSlots()
  {
  Slot s;
  for(Object o : this.inventorySlots)
    {
    s = (Slot)o;
    if(s.yDisplayPosition>=0)
      {
      s.yDisplayPosition-=1000;      
      }
    }
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("prog"))
    {
    Config.logDebug("receiving display progress update");
    this.displayProgress = tag.getInteger("prog");
    }
  if(tag.hasKey("progMax"))
    {
    Config.logDebug("receiving display MAX update");
    this.displayProgressMax = tag.getInteger("progMax");
    }
  if(tag.hasKey("rec"))
    {
    Config.logDebug("receiving recipe update");
    this.currentRecipe = new ResourceListRecipe(tag.getCompoundTag("rec")); 
    this.clientRecipe = this.currentRecipe;
    Config.logDebug("recieved recipe: "+this.currentRecipe);
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    } 
 
  if(tag.hasKey("rem"))
    {
    Config.logDebug("receiving clear recipe update");
    this.currentRecipe = null;
    this.clientRecipe = null;
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("stop") && !player.worldObj.isRemote)
    {
    Config.logDebug("receiving server stop work command");
    te.stopAndClearRecipe();
    this.currentRecipe = null;
    this.clientRecipe = null;
    }
  if(tag.hasKey("set") && !player.worldObj.isRemote)
    {
    ItemStack result = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("result"));
    int id = result.itemID;
    int dmg = result.getItemDamage(); 
    Config.logDebug("recipe id: "+id+" dmg: "+dmg);
    ResourceListRecipe recipe = AWCraftingManager.instance().getRecipeByResult(result);
    Config.logDebug("receiving server set recipe command :: "+recipe);  
    te.validateAndSetRecipe(recipe);
    }
  if(tag.hasKey("work"))
    {
    Config.logDebug("receiving work update");
    this.isWorking = tag.getBoolean("work");
    }
  if(tag.hasKey("entry"))
    {    
    if(this.gui!=null)
      {
      this.gui.refreshGui();      
      }
    te.stopAndClearRecipe();
    this.currentRecipe = null;
    this.clientRecipe = null;
    boolean name = tag.getBoolean("entry");
    if(name)
      {
      this.entry = new PlayerEntry();
      this.entry.readFromNBT(tag);
      }
    else
      {
      this.entry = null;
      }
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {

  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

@Override
public void detectAndSendChanges()
  {
  super.detectAndSendChanges();
  if(this.player.worldObj.isRemote){return;}
  if(this.displayProgress!=te.displayProgress)
    {
    this.displayProgress = te.displayProgress;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("prog", displayProgress);
    this.sendDataToPlayer(tag);
    Config.logDebug("sending progress update");
    }
  if(this.displayProgressMax!=te.displayProgressMax)
    {
    this.displayProgressMax = te.displayProgressMax;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("progMax", displayProgressMax);
    this.sendDataToPlayer(tag);
    Config.logDebug("sending progress MAX update");
    }


  /**
   * check if current recipe is the same as TE recipe
   * if not, set current recipe to TE recipe and send complete packet
   * else check resource lists for differences, send diff
   *  
   */
  ResourceListRecipe ter = te.recipe;
  if(ter==null)
    {
    if(this.currentRecipe!=null)
      {
      this.currentRecipe = null;
      NBTTagCompound tag = new NBTTagCompound();   
      tag.setBoolean("rem", true);      
      this.sendDataToPlayer(tag);
      Config.logDebug("sending remove recipe update to player");
      }   
    }
  else
    {
    if(this.currentRecipe!=ter)
      {
      this.currentRecipe = te.recipe;
      NBTTagCompound tag = new NBTTagCompound();
      tag.setCompoundTag("rec", this.currentRecipe.getNBTTag());      
      this.sendDataToPlayer(tag);
      Config.logDebug("sending recipe update to player");
      }
    }

  if(this.isWorking!= te.isWorking)
    {
    this.isWorking = te.isWorking;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("work", this.isWorking);
    this.sendDataToPlayer(tag);
    }
  
  if(this.entry==null || !this.entry.playerName.equals(te.workingPlayer))
    {
    if(te.workingPlayer!=null)
      {
      this.entry = PlayerTracker.instance().getEntryFor(te.workingPlayer);
      NBTTagCompound tag = this.entry.getNBTTag();
      tag.setBoolean("entry", true);   
      this.sendDataToPlayer(tag);
      this.researchLength = this.entry.getKnownResearch().size();
      this.currentRecipe = null;
      this.clientRecipe = null;
      te.stopAndClearRecipe();
      }
    else
      {
      if(this.entry==null && te.workingPlayer==null)
        {
        return;
        }
      this.entry = null;
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("entry", false);   
      this.sendDataToPlayer(tag);
      this.currentRecipe = null;
      this.clientRecipe = null;
      te.stopAndClearRecipe();
      }
    }
  else if(this.entry!=null && this.entry.playerName.equals(te.workingPlayer))
    {
    int len = this.entry.getKnownResearch().size();
    if(len!=this.researchLength)
      {
      NBTTagCompound tag = this.entry.getNBTTag();
      tag.setBoolean("entry", true);   
      this.sendDataToPlayer(tag);
      this.researchLength = len;      
      
      }    
    }
  te.shouldUpdate = true;
  }

}
