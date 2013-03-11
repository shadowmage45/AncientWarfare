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

import shadowmage.ancient_warfare.common.soldiers.NpcBase;

public interface INpcAI
{

public abstract int getGlobalAIType();//global reference number for AI, used for reconstructing trees from data
public abstract int getSuccessTicks();//how many ticks to check before executing again, if previous execution succeeded
public abstract int getFailureTicks();//how many ticks to check before executing again, if previous execution failed
public abstract boolean shouldExecute();//a quick pre-check to see if the task should execute this tick
public abstract int[] exclusiveTasks();//tasks that cannot execute the same time as this

public abstract void onTick(NpcBase npc);//apply your AI actions to the NPC here.  Called during the AI update loop, only if shouldExecute()==true && ticksTilTry<=0


}
