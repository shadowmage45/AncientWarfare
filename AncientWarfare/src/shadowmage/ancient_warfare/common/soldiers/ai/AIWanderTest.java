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

public class AIWanderTest extends NpcAI
{

/**
 * @param typeNum
 * @param npc
 */
public AIWanderTest(NpcBase npc)
  {
  super(npc);
  this.successTicks = 80;
  this.failureTicks = 10;
  }

@Override
public int exclusiveTasks()
  {
  return ATTACK+MOVE_TO+MOUNT_VEHICLE+FOLLOW+REPAIR+HEAL+HARVEST;
  }

@Override
public void onTick()
  {
  double bX = npc.posX + rng.nextInt(32)-16;
  double bY = npc.posY;
  double bZ = npc.posZ + rng.nextInt(32)-16;
  npc.getMoveHelper().setMoveTo(bX, bY, bZ, npc.getAIMoveSpeed());
  this.success = true;
  this.finished = true; 
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

@Override
public void onAiStarted()
  {

  }

}
