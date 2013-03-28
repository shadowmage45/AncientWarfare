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
package shadowmage.ancient_warfare.common.soldiers.types;

import java.lang.reflect.Field;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcTypeBase;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class NpcVillager extends NpcTypeBase
{

/**
 * @param type
 */
public NpcVillager(int type)
  {
  super(type);
  this.displayName = "Villager";
  this.tooltip = "Spawns a Villager when used.";
  this.isVanillaVillager = true;
  this.addLevel("Farmer", "");
  this.addLevel("Librarian", "");
  this.addLevel("Priest", "");
  this.addLevel("Smith", "");
  this.addLevel("Butcher", "");
  
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
      this.addLevel("ModVillager:"+entry, "");
      }
    }
  catch(Exception e)
    {
    Config.logError("Could not access VillagerRegistry.instance()");
    }
  }

@Override
public void addTargets(NpcTargetHelper helper)
  {
  // TODO Auto-generated method stub
  
  }


}
