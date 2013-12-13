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
package shadowmage.ancient_warfare.common.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.common.inventory.AWInventoryBasic;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_framework.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.config.AWCoreStatics;
import shadowmage.ancient_warfare.common.container.ContainerDummy;

public class TEAWAutoCrafting extends TEAWCraftingWorkSite
{

public ItemStack result = null;
//protected ItemStack[] layoutMatrix = new ItemStack[9];

InventoryCrafting layoutInventory;

/**
 * 
 */
public TEAWAutoCrafting()
  {
  this.modelID = 7;
  this.workType = CivicWorkType.CRAFT;  
  this.craftMatrix = new int[]{0,1,2,3,4,5,6,7,8};
  this.resultSlot = new int[]{9};
  this.inventory = new AWInventoryBasic(10);
  this.workType = CivicWorkType.CRAFT;
  this.shouldBroadcast = true;
  this.layoutInventory = new InventoryCrafting(new ContainerDummy(), 3, 3);
  }

@Override
protected void updateCrafting()
  {
  if(this.result!=null)
    {
    if(this.isWorking)
      {
      if(this.workProgress<this.workProgressMax)
        {
        if(this.canUpdate || !AWCoreStatics.useNpcWorkForCrafting)
          {
          this.workProgress++;
          this.canUpdate = false;
          }
        }
      if(this.workProgress>=this.workProgressMax)
        {
        if(tryFinishCrafting())
          {   
          tryStartCrafting();
          }
        }
      }
    else
      {
      if(this.recipeStartCheckDelayTicks>0)
        {
        this.recipeStartCheckDelayTicks--;
        }
      else
        {
        this.recipeStartCheckDelayTicks = AWCoreStatics.npcAITicks*10;
        this.tryStartCrafting();        
        }
      }
    }
  else
    {
    this.workProgress = 0;
    this.workProgressMax = 0;
    this.isWorking = false;
    }
  }

@Override
protected boolean tryStartCrafting()
  {  
  if(this.canStartRecipe())
    {
    this.isWorking = true;
    this.workProgress = 0;
    this.workProgressMax = 40;
    ItemStack stack;
    for(int i = 0; i < this.layoutInventory.getSizeInventory(); i++)
      {
      stack = layoutInventory.getStackInSlot(i);
      if(stack==null){continue;}
      InventoryTools.tryRemoveItems(inventory, stack, 1, craftMatrix);
      }
    return true;
    }
  this.isWorking = false;
  return false;
  }

@Override
protected boolean tryFinishCrafting()
  {
  if(InventoryTools.canHoldItem(inventory, result, result.stackSize, resultSlot[0], resultSlot[0]))
    {
    InventoryTools.tryMergeStack(inventory, result.copy(), resultSlot);
    this.workProgress = 0;
    this.recipeStartCheckDelayTicks = 0;
    return true;
    }
  return false;
  }

@Override
public void setRecipe(ResourceListRecipe recipe)
  {
  // noop for vanilla table, recipe is set by craft matrix/result
  }

@Override
public void stopAndClear()
  {
  this.result = null;
  this.workProgress = 0;
  this.workProgressMax = 0;
  this.isWorking = false;
  }

@Override
public ResourceListRecipe getRecipe()
  {
  return null;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.orientation = tag.getByte("face");
  this.workProgress = tag.getInteger("time");
  this.workProgressMax = tag.getInteger("timeMax");
  this.isWorking = tag.getBoolean("work");
  this.teamNum = tag.getInteger("team");  
  if(tag.hasKey("inv") && this.inventory!=null)
    {
    this.inventory.readFromNBT(tag.getCompoundTag("inv"));
    }
  if(tag.hasKey("result"))
    {
    this.result = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("result"));
    }
  this.readExtraNBT(tag);
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setByte("face", (byte)this.orientation);
  tag.setInteger("time", this.workProgress);
  tag.setInteger("timeMax", this.workProgressMax);
  tag.setBoolean("work", this.isWorking);
  tag.setInteger("team", this.teamNum);
  if(this.inventory!=null)
    {
    tag.setCompoundTag("inv", this.inventory.getNBTTag());
    }
  if(this.result!=null)
    {
    tag.setCompoundTag("result", this.result.writeToNBT(new NBTTagCompound()));
    }
  this.writeExtraNBT(tag);
  }

@Override
public void readDescriptionPacket(NBTTagCompound tag)
  {
  
  }

@Override
public void writeDescriptionData(NBTTagCompound tag)
  {

  }

@Override
public void writeExtraNBT(NBTTagCompound tag)
  {
  tag.setCompoundTag("inv2", InventoryTools.getTagForInventory(layoutInventory));
  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  {
  InventoryTools.readInventoryFromTag(layoutInventory, tag.getCompoundTag("inv2"));
  }

@Override
public void onBlockClicked(EntityPlayer player)
  {
  if(player.worldObj.isRemote){return;}
  GUIHandler.instance().openGUI(AWCoreStatics.guiCraftAuto, player, xCoord, yCoord, zCoord);
  }

public ItemStack getLayoutMatrixSlot(int num)
  {
  if(num>=0 && num<9)
    {
    return this.layoutInventory.getStackInSlot(num);
    }
  return null;
  }

public void setLayoutMatrixSlot(int num, ItemStack stack)
  {
  if(num>=0 && num<9)
    {
    this.layoutInventory.setInventorySlotContents(num, stack);
    this.onLayoutMatrixChanged();
    }
  }

private void onLayoutMatrixChanged()
  {  
  this.result = null;
  this.workProgress = 0;
  this.workProgressMax = 0;
  this.isWorking = false;
  if(this.checkMatchingRecipes())
    {
    this.tryStartCrafting();
    }
  }

private boolean checkMatchingRecipes()
  {
  this.result = null;
  this.result = CraftingManager.getInstance().findMatchingRecipe(layoutInventory, worldObj);
  return this.result!=null;
  }


private boolean canStartRecipe()
  {
  ArrayList<ItemStack> items = new ArrayList<ItemStack>();
  ItemStack item; 
  for(int i = 0; i < 9; i++)
    {
    item = this.layoutInventory.getStackInSlot(i);
    if(item==null){continue;}
    items.add(item);
    }
  return this.containsResources(InventoryTools.getCompactedItemList(items));
  }

private boolean containsResources(List<ItemStack> items)
  {
  for(ItemStack item : items)
    {
    if(item==null){continue;}
    if(InventoryTools.getCountOf(this, item, craftMatrix)<item.stackSize)
      {
      return false;
      }
    }
  return true;
  }

}
