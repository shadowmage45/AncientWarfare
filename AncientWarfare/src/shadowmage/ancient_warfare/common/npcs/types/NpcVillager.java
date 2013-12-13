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
package shadowmage.ancient_warfare.common.npcs.types;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.NpcTypeBase;
import shadowmage.ancient_warfare.common.npcs.helpers.NpcTargetHelper;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class NpcVillager extends NpcTypeBase
{

/**
 * @param type
 */
public NpcVillager(int type)
  {
  super(type);
  this.isVanillaVillager = true;
  this.iconTexture = "npcVillager";
  this.addLevel(type, 0, "", null, null);
  this.addLevel(type, 1, "", null, null);
  this.addLevel(type, 2, "", null, null);
  this.addLevel(type, 3, "", null, null);
  this.addLevel(type, 4, "", null, null);
  
  int level = 5;
  /**
   * reaaaaaaaaallllyyyy ugly reflection to get at cpw's villager registry to pull extra villager types..
   */
  try
    {
    Field privateList = VillagerRegistry.class.getDeclaredField("newVillagerIds");
    privateList.setAccessible(true);
    List theList = (List) privateList.get(VillagerRegistry.instance());
    for(Object ob : theList)
      {
      Integer entry = (Integer)ob;
      this.addLevel(type, level, "", null, null);
      level++;
      }
    }
  catch(Exception e)
    {
    Exception e1 = new Exception("Could not access VillagerRegistry.instance() to add mod villager recruit recipes");
    e1.setStackTrace(e.getStackTrace());
    e1.printStackTrace();
    }
  for(int i = 0; i < level; i++)
    {
    this.getLevelEntry(i).addNeededResearch(ResearchGoalNumbers.command1).addRecipeResource(new ItemStack(Item.emerald,1,0), new ItemStack(Item.porkCooked, 20, 0));    
    }
  }

@Override
public void addTargets(NpcBase npc, NpcTargetHelper helper)
  {
  // TODO Auto-generated method stub
  
  }


}
