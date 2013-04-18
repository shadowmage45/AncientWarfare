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
package shadowmage.ancient_warfare.common.civics.worksite.te;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.npcs.NpcBase;

public class TEWorkSiteFarm extends TEWorkSite
{

@Override
public boolean onInteract(World world, EntityPlayer player)
  {
  return false;
  }

@Override
protected void updateWorkPoints()
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void doWork(NpcBase npc, WorkPoint workEntry)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void onWorkFinished(WorkPoint entry)
  {
  // TODO Auto-generated method stub
  
  }

}
