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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapper;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public class ResourceListRecipe implements INBTTaggable
{

protected HashSet<Integer> neededResearch = new HashSet<Integer>();
protected HashSet<IResearchGoal> neededResearchCache = new HashSet<IResearchGoal>();

ItemStack result;
List<ItemStackWrapperCrafting> resources = new ArrayList<ItemStackWrapperCrafting>();
String displayName;
RecipeType type = RecipeType.NONE;


public ResourceListRecipe(NBTTagCompound tag)
  {
	this.readFromNBT(tag);
  }

public ResourceListRecipe(ItemStack result, List<ItemStackWrapperCrafting> resources, RecipeType type)
  {
  this.result = result;
  this.resources = resources;
  displayName = result.getDisplayName();
  this.type = type;
  }

public ResourceListRecipe(ItemStack result, RecipeType type)
  {
  this.result = result;
  displayName = result.getDisplayName();
  this.resources = new ArrayList<ItemStackWrapperCrafting>();
  this.type = type;
  Config.logDebug("constructed recipe for : "+this.result + "  ::::  "+this.result.getTagCompound());
  }

public ResourceListRecipe copy()
  {
  ResourceListRecipe recipe = new ResourceListRecipe(result.copy(), type);
  recipe.displayName = this.displayName;
  for(ItemStackWrapperCrafting item : this.resources)
    {
    ItemStackWrapperCrafting newItem = new ItemStackWrapperCrafting(item.getFilter(), item.getQuantity(), item.ignoreDamage, item.ignoreTag);
    newItem.setRemainingNeeded(item.getRemainingNeeded());
    recipe.addResource(newItem);
    }
  return recipe;
  }

public ResourceListRecipe addResource(ItemStackWrapperCrafting item)
  {
  if(this.resources==null)
    {
    this.resources = new ArrayList<ItemStackWrapperCrafting>();
    }
  this.resources.add(item);
  return this;
  }

public ResourceListRecipe addResource(ItemStack stack, int qty, boolean dmg, boolean tag)
  {
  if(this.resources==null)
    {
    this.resources = new ArrayList<ItemStackWrapperCrafting>();
    }
  this.resources.add(new ItemStackWrapperCrafting(stack, qty, dmg, tag));
  return this;
  }

public ResourceListRecipe addResources(Collection<ItemStack> items, boolean damage, boolean tag)
  {
  if(this.resources==null)
    {
    this.resources = new ArrayList<ItemStackWrapperCrafting>();
    }
  for(ItemStack stack : items)
    {
    this.resources.add(new ItemStackWrapperCrafting(stack, stack.stackSize, damage, tag));
    }
  return this;
  }

public ResourceListRecipe addResources(Collection<ItemStackWrapperCrafting> items)
  {
  this.resources.addAll(items);
  return this;
  }

public List<ItemStackWrapperCrafting> getResourceList()
  {
  return resources;
  }

public ItemStack getResult()
  {
  return result;
  }

public String getDisplayName()
  {
  return this.displayName;
  }

public ResourceListRecipe setDisplayName(String name)
  {
  this.displayName = name;
  return this;
  }

/**
 * called on an active crafting recipe to see if any of its resources still need items 
 * @return
 */
public boolean isFinished()
  {
  return false;
  }

public boolean isResource(ItemStack filter)
  {
  for(ItemStackWrapper resource : this.resources)
    {
    if(resource.matches(filter))
      {
      return true;
      }
    }
  return false;
  }

public boolean canBeCraftedBy(PlayerEntry entry)
  {
  return Config.disableResearch? true : (this.neededResearch==null || this.neededResearch.isEmpty()) ? true : entry==null ? false : entry.hasDoneResearchByNumbers(neededResearch);
  }

public boolean doesInventoryContainResources(IInventory inventory, int[] slotNums)
  {
  boolean start = true;
  int count = 0;
  if(this.resources.isEmpty()){return false;}
  ItemStack fromInv = null;
  for(ItemStackWrapperCrafting stack : this.resources)
    {
    count = stack.getRemainingNeeded();
    Config.logDebug("examinig stack "+stack.getFilter() + " needed: " +  stack.getRemainingNeeded());
    for(int i = 0; i < slotNums.length; i++)
      {
      fromInv = inventory.getStackInSlot(slotNums[i]);
      if(fromInv==null){continue;}
      if(stack.matches(fromInv))
        {
        count -= fromInv.stackSize;
        Config.logDebug("found matching item ..count: "+fromInv.stackSize);
        }
      if(count<=0)
        {
        break;
        }
      }    
    if(count>0)
      {
      Config.logDebug("failed count check");
      start = false;
      break;
      }
    }
  return start;
  }

public int getResourceItemCount()
  {
  int count = 0;
  for(ItemStackWrapperCrafting craft : this.resources)
    {
    count += craft.getQuantity();
    }
  return count;
  }

public void removeResourcesFrom(IInventory inventory, int[] slotNums)
  {
  boolean start = true;
  int count = 0;
  int toRemove = 0;
  if(this.resources.isEmpty()){return;}
  ItemStack fromInv = null;
  for(ItemStackWrapperCrafting stack : this.resources)
    {
    count = stack.getQuantity();
    for(int i = 0; i < slotNums.length; i++)
      {
      fromInv = inventory.getStackInSlot(slotNums[i]);
      if(fromInv==null){continue;}
      if(stack.matches(fromInv))
        {
        toRemove = fromInv.stackSize;
        if(toRemove>count)
          {
          toRemove = count;
          }
        count -= toRemove;
        fromInv.stackSize-=toRemove;
        if(fromInv.stackSize<=0)
          {
          inventory.setInventorySlotContents(slotNums[i], null);
          }
        inventory.onInventoryChanged();
        }
      if(count<=0)
        {
        break;
        }
      }   
    }
  }

public HashSet<IResearchGoal> getNeededResearch()
  {
  if(this.neededResearchCache.size()==this.neededResearch.size())
    {
    return this.neededResearchCache;
    }
  this.neededResearchCache.clear();
  for(Integer i : neededResearch)
    {
    neededResearchCache.add(ResearchGoal.getGoalByID(i));
    }
  return neededResearchCache;
  }

public void addNeededResearch(int res)
  {
  this.neededResearch.add(res);
  }

public void addNeededResearch(Collection<Integer> nums)
  {
  this.neededResearch.addAll(nums);
  }

public void addNeededResearch(IResearchGoal goal)
  {
  this.neededResearch.add(goal.getGlobalResearchNum());
  }

@Override
public String toString()
  {
  return "AWResourceListRecipe: "+this.displayName + "  Required research size: "+this.neededResearch.size() + "  Resource list size: "+this.resources.size();
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.displayName = tag.getString("name");
  this.result = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("result"));
  NBTTagList list = tag.getTagList("res");
  for(int i = 0; i < list.tagCount(); i ++)
    {
    this.resources.add(new ItemStackWrapperCrafting((NBTTagCompound)list.tagAt(i)));
    }
  this.type = RecipeType.values()[tag.getInteger("type")];
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", this.displayName);
  tag.setCompoundTag("result", this.result.writeToNBT(new NBTTagCompound()));
  tag.setInteger("type", this.type.ordinal());
  NBTTagList list = new NBTTagList();
  for(ItemStackWrapperCrafting wrap : this.resources)
    {
    list.appendTag(wrap.writeToNBT(new NBTTagCompound()));
    }
  tag.setTag("res", list);
  return tag;
  }

/**
 * 
 */
public void reset()
  {
  for(ItemStackWrapperCrafting item : this.resources)
    {
    item.setRemainingNeeded(item.getQuantity());
    }
  }

}
