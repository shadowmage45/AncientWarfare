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

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.item.AWItemClickable;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;

public class ItemResearchNote extends AWItemClickable
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemResearchNote(int itemID)
  {
  super(itemID);
  this.hasLeftClick = false;
  this.setCreativeTab(CreativeTabAW.researchTab);
  this.maxStackSize = 1;
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  super.addInformation(stack, player, list, par4);
  list.add("Right Click to learn research.");
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote){return false;}
  IResearchGoal goal = ResearchGoal.getGoalByID(stack.getItemDamage());
  if(goal!=null && !PlayerTracker.instance().getEntryFor(player).hasDoneResearch(goal))
    {
    player.addChatMessage("Learning research from notes: " + goal.getDisplayName());
    PlayerTracker.instance().addResearchToPlayer(world, player.getEntityName(), goal.getGlobalResearchNum());
    if(!player.capabilities.isCreativeMode)
      {
      stack.stackSize--;
      if(stack.stackSize<=0)
        {
        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        }
      return false;
      }    
    }
  return false;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  return false;
  }

}
