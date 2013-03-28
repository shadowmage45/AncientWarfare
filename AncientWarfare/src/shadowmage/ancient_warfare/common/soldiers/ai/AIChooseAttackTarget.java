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
package shadowmage.ancient_warfare.common.soldiers.ai;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.soldiers.NpcAI;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;

public class AIChooseAttackTarget extends NpcAI
{

/**
 * @param npc
 */
public AIChooseAttackTarget(NpcBase npc)
  {
  super(npc);
  this.failureTicks = 20;
  this.successTicks = 100;
  this.taskName = "ChooseAttackTarget";
  }

@Override
public int exclusiveTasks()
  {
  return NONE;
  }

@Override
public void onAiStarted()
  {
  // TODO Auto-generated method stub
  }

@Override
public void onTick()
  {  
  this.finished = true;
  this.success = true;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  
  }

@Override
public NBTTagCompound getNBTTag()
  {
  return new NBTTagCompound();
  }

}
