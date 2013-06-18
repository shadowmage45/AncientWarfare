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
package shadowmage.ancient_warfare.common.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TEAWCraftingVanilla extends TEAWCraftingWorkSite
{

public ItemStack result = null;
protected ItemStack[] layoutMatrix = new ItemStack[9];

/**
 * 
 */
public TEAWCraftingVanilla()
  {
  this.modelID = 7;
  this.workType = CivicWorkType.CRAFT;  
  this.craftMatrix = new int[]{0,1,2,3,4,5,6,7,8};
  this.resultSlot = new int[]{9};
  this.inventory = new AWInventoryBasic(11);
  this.workType = CivicWorkType.RESEARCH;
  this.shouldBroadcast = true;
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
        if(this.canUpdate || !Config.useNpcWorkForCrafting)
          {
          this.workProgress++;
          this.canUpdate = false;
          }
        }
      if(this.workProgress>=this.workProgressMax)
        {
        if(tryFinish())
          {   
          tryStart();
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
        this.recipeStartCheckDelayTicks = Config.npcAITicks*10;
        this.tryStart();        
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
protected boolean tryStart()
  {  
  if(this.canStartRecipe())
    {
    this.isWorking = true;
    this.workProgress = 0;
    this.workProgressMax = 40;
    for(ItemStack stack : this.layoutMatrix)
      {
      if(stack==null){continue;}
      InventoryTools.tryRemoveItems(inventory, stack, 1, craftMatrix);
      }
    }
  this.isWorking = false;
  return false;
  }

@Override
protected boolean tryFinish()
  {
  if(InventoryTools.canHoldItem(inventory, result, result.stackSize, resultSlot[0], resultSlot[0]))
    {
    InventoryTools.tryMergeStack(inventory, result, resultSlot);
    this.workProgress = 0;
    this.recipeStartCheckDelayTicks = 0;
    this.result = null;
    return true;
    }
  return false;
  }

@Override
public void setRecipe(ResourceListRecipe recipe)
  {
  // noop for vanilla table, recipe is set by craft matrix
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

  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  {

  }

@Override
public void onBlockClicked(EntityPlayer player)
  {
  if(player.worldObj.isRemote){return;}
  GUIHandler.instance().openGUI(GUIHandler.AUTO_CRAFT, player, player.worldObj, xCoord, yCoord, zCoord);
  }

public ItemStack getLayoutMatrixSlot(int num)
  {
  if(num>=0 && num<9)
    {
    return this.layoutMatrix[num];
    }
  return null;
  }

public void setLayoutMatrixSlot(int num, ItemStack stack)
  {
  if(num>=0 && num<9)
    {
    this.layoutMatrix[num] = stack;
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
    this.tryStart();
    }
  }

private boolean checkMatchingRecipes()
  {
  this.result = null;
  List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
  for(IRecipe rec : recipes)
    {
    if(rec instanceof ShapedRecipes)
      {
      ShapedRecipes recipe = (ShapedRecipes)rec;
      if(this.matches(recipe))
        {
        this.result = recipe.getRecipeOutput().copy();        
        break;     
        }
      }
    else if(rec instanceof ShapelessRecipes)
      {
      ShapelessRecipes recipe = (ShapelessRecipes)rec;
      if(this.matchesShapeless(recipe))
        {
        this.result = recipe.getRecipeOutput().copy();
        break;
        }
      }    
    }
  return this.result!=null;
  }

/**
 * Used to check if a recipe matches current crafting inventory
 */
private boolean matches(ShapedRecipes recipe)
  {
  for (int i = 0; i <= 3 - recipe.recipeWidth; ++i)
    {
    for (int j = 0; j <= 3 - recipe.recipeHeight; ++j)
      {
      if (this.checkMatch(recipe, i, j, true))
        {
        return true;
        }
      if (this.checkMatch(recipe, i, j, false))
        {
        return true;
        }
      }
    }
  return false;
  }

/**
 * Checks if the region of a crafting inventory is match for the recipe.
 */
private boolean checkMatch(ShapedRecipes recipe, int par2, int par3, boolean par4)
  {
  for (int k = 0; k < 3; ++k)
    {
    for (int l = 0; l < 3; ++l)
      {
      int i1 = k - par2;
      int j1 = l - par3;
      ItemStack itemstack = null;

      if (i1 >= 0 && j1 >= 0 && i1 < recipe.recipeWidth && j1 < recipe.recipeHeight)
        {
        if (par4)
          {
          itemstack = recipe.recipeItems[recipe.recipeWidth - i1 - 1 + j1 * recipe.recipeWidth];
          }
        else
          {
          itemstack = recipe.recipeItems[i1 + j1 * recipe.recipeWidth];
          }
        }

//      ItemStack itemstack1 = par1InventoryCrafting.getStackInRowAndColumn(k, l);
      ItemStack itemstack1 = this.layoutMatrix[l*3+k];//this.getStackInSlot(l*3+k);
      if (itemstack1 != null || itemstack != null)
        {
        if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null)
          {
          return false;
          }

        if (itemstack.itemID != itemstack1.itemID)
          {
          return false;
          }

        if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage())
          {
          return false;
          }
        }
      }
    }
  return true;
  }

private boolean matchesShapeless(ShapelessRecipes recipe)
  {
  ArrayList arraylist = new ArrayList(recipe.recipeItems);
  for (int i = 0; i < 3; ++i)
    {
    for (int j = 0; j < 3; ++j)
      {
      //              ItemStack itemstack = par1InventoryCrafting.getStackInRowAndColumn(j, i);
      ItemStack itemstack = this.layoutMatrix[i*3+j];
      if (itemstack != null)
        {
        boolean flag = false;
        Iterator iterator = arraylist.iterator();
        while (iterator.hasNext())
          {
          ItemStack itemstack1 = (ItemStack)iterator.next();
          if (itemstack.itemID == itemstack1.itemID && (itemstack1.getItemDamage() == 32767 || itemstack.getItemDamage() == itemstack1.getItemDamage()))
            {
            flag = true;
            arraylist.remove(itemstack1);
            break;
            }
          }
        if (!flag)
          {
          return false;
          }
        }
      }
    }
  return arraylist.isEmpty();
  }

private boolean canStartRecipe()
  {
  return this.containsResources(InventoryTools.getCompactedItemList(Arrays.asList(this.layoutMatrix)));
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
