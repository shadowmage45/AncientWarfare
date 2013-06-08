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

import java.util.ArrayList;
import java.util.Arrays;
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
import shadowmage.ancient_warfare.common.crafting.TEAWCrafting;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class ContainerAWCrafting extends ContainerBase
{

TEAWCrafting te;
public int displayProgress;
public int displayProgressMax;
/**
 * is te locked for auto-work e.g. is TE recipe!=null?
 */
boolean isLocked = false;
/**
 * is TE currently processing a recipe?
 */
boolean isWorking = false;
/**
 * cached copy of te current recipe
 */
ResourceListRecipe recipeCache = null;

/**
 * current client-recipe
 */
ResourceListRecipe clientRecipe = null;

public PlayerEntry entry;
int researchLength;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerAWCrafting(EntityPlayer openingPlayer, TEAWCrafting te)
  {
  super(openingPlayer, null);
  this.te = te;
  this.addPlayerSlots(player, 8, 156, 4);

  int posX = 8;
  int posY = 8+24;
  int slotNum = 0;
  Slot slot;
  int totalSlots = te.craftMatrix.length;
  int columns = totalSlots / 3;
  if(totalSlots%3!=0){columns++;}  
  for(int y = 0; y < 3; y++)
    {
    for(int x = 0; x < columns; x++)
      {
      posX = 8 + x * 18;
      posY = 8 + 24 + 18 + 4 + 18 + 4;
      posY += y * 18;
      slotNum = y * columns + x;
      slot = new Slot(te, slotNum, posX, posY);
      this.addSlotToContainer(slot);
      }
    }
  if(te.resultSlot!=null && te.resultSlot.length>0)
    {
    slot = new SlotPullOnly(te, te.resultSlot[0], 8 + 3* 18 + 18 , 8 + 24 + 18 + 4 + 18 + 4 + 18);
    this.addSlotToContainer(slot);    
    }
  if(te.bookSlot!=null && te.bookSlot.length>0)
    {
    slot = new SlotResourceOnly(te, te.bookSlot[0], 8 , 8 + 24, Arrays.asList(new ItemStack(ItemLoader.researchBook))).setIgnoreType(3).setMaxStackSize(1);
    this.addSlotToContainer(slot);    
    }
  this.isWorking = te.isWorking();
  this.recipeCache = te.getRecipe();
  this.isLocked = this.recipeCache!=null;
  this.displayProgress = te.getWorkProgress();
  this.displayProgressMax = te.getWorkProgressMax();
  this.entry = te.getWorkingPlayerEntry();
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
  if(tag.hasKey("time"))
    {
    this.displayProgress = tag.getInteger("time");
    }  
  if(tag.hasKey("timeMax"))
    {
    this.displayProgressMax = tag.getInteger("timeMax");
    }
  if(tag.hasKey("recipe") && player.worldObj.isRemote)
    {
    ItemStack stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("recipe"));
    this.recipeCache = AWCraftingManager.instance().getRecipeByResult(stack);
    this.clientRecipe = this.recipeCache;
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("remove"))
    {    
    this.recipeCache = null;
    this.clientRecipe = null;
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("stop") && !player.worldObj.isRemote)
    {
    Config.logDebug("receiving server stop work command");
    te.stopAndClear();
    this.recipeCache = null;
    this.clientRecipe = null;
    this.isWorking = false;
    this.isLocked = false;
    this.displayProgress = 0;
    this.displayProgressMax = 0;
    }
  if(tag.hasKey("set") && !player.worldObj.isRemote)
    {
    ItemStack result = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("result"));
    int id = result.itemID;
    int dmg = result.getItemDamage();
    ResourceListRecipe recipe = AWCraftingManager.instance().getRecipeByResult(result);
    te.setRecipe(recipe);
    }
  if(tag.hasKey("work"))
    {
    Config.logDebug("receiving work update");
    this.isWorking = tag.getBoolean("work");
    }
  if(tag.hasKey("lock"))
    {
    this.isLocked = tag.getBoolean("lock");
    }
  if(tag.hasKey("entry"))
    {    
    if(tag.getBoolean("entry"))
      {
      this.entry = new PlayerEntry();
      this.entry.readFromNBT(tag.getCompoundTag("entryData"));
      }
    else
      {
      this.entry = null;
      }
    if(this.gui!=null)
      {
      this.gui.refreshGui();      
      }    
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  this.displayProgress = tag.getInteger("time");
  this.displayProgressMax = tag.getInteger("timeMax");
  this.isWorking = tag.getBoolean("work");
  this.isLocked = tag.getBoolean("lock");
  if(tag.hasKey("entry"))    
    {
    this.entry = new PlayerEntry();
    entry.readFromNBT(tag.getCompoundTag("entry"));
    }
  if(this.gui!=null)
    {
    this.gui.refreshGui();
    }
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  ArrayList<NBTTagCompound> list = new ArrayList<NBTTagCompound>();
  NBTTagCompound tag = new NBTTagCompound();
  if(this.entry!=null)
    {
    tag.setCompoundTag("entry", this.entry.getNBTTag());
    }
  tag.setInteger("time", this.displayProgress);
  tag.setInteger("timeMax", this.displayProgressMax);
  tag.setBoolean("work", this.isWorking);
  tag.setBoolean("lock", this.isLocked);
  return list;
  }

@Override
public void detectAndSendChanges()
  {
  super.detectAndSendChanges();  
  if(this.player.worldObj.isRemote){return;}
  
  NBTTagCompound tag = null;
  
  if(this.displayProgress!=te.getWorkProgress())
    {
    if(tag==null){tag = new NBTTagCompound();}
    this.displayProgress = te.getWorkProgress();
    tag.setInteger("time", displayProgress);
    }
  
  if(this.displayProgressMax!=te.getWorkProgressMax())
    {
    if(tag==null){tag = new NBTTagCompound();}
    this.displayProgressMax = te.getWorkProgressMax();
    tag.setInteger("timeMax", displayProgressMax);
    }

  ResourceListRecipe ter = te.getRecipe();
  if(ter==null)
    {
    if(this.recipeCache!=null)
      {
      if(tag==null){tag = new NBTTagCompound();}
      this.recipeCache = null;
      tag.setBoolean("remove", true);   
      }   
    }
  else
    {
    if(this.recipeCache!=ter)
      {
      if(tag==null){tag = new NBTTagCompound();}
      this.recipeCache = ter;
      tag.setCompoundTag("recipe", this.recipeCache.getResult().writeToNBT(new NBTTagCompound()));
      }
    }

  if(this.isWorking!= te.isWorking())
    {
    if(tag==null){tag = new NBTTagCompound();}
    this.isWorking = te.isWorking();
    tag.setBoolean("work", this.isWorking);
    }
  
  if(this.isLocked != (te.getRecipe()!=null))
    {
    if(tag==null){tag = new NBTTagCompound();}
    this.isLocked = te.getRecipe()!=null;
    tag.setBoolean("lock", this.isLocked);
    }
  
  if(this.entry != te.getWorkingPlayerEntry())
    {
    if(tag==null){tag = new NBTTagCompound();}
    this.entry = te.getWorkingPlayerEntry();
    tag.setBoolean("entry", this.entry!=null);
    if(this.entry!=null)
      {
      tag.setCompoundTag("entryData", entry.getNBTTag());
      }
    }
  else if(this.entry!=null && this.entry.getKnownResearch().size() != this.researchLength)
    {
    if(tag==null){tag = new NBTTagCompound();}
    tag.setBoolean("entry", true);
    tag.setCompoundTag("entryData", entry.getNBTTag());
    }
      
  te.setCanUpdate();
  if(tag!=null)
    {
    this.sendDataToPlayer(tag);
    }
  }

}
