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
package shadowmage.ancient_warfare.common.soldiers;

import java.util.List;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.soldiers.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;

public interface INpcType
{

public abstract int getGlobalNpcType();
public abstract String getDisplayName();
public abstract String getDisplayTooltip();
public abstract String getDisplayTexture(int level);
public abstract String getLevelName(int level);
public abstract int getNumOfLevels();
public abstract int getMaxHealth(int level);
public abstract int getInventorySize(int level);
public abstract float getRangedAttackDistance(int level);

public abstract List<NpcAIObjective> getAI(NpcBase npc, int level);

public abstract boolean isCombatUnit();
public abstract boolean isVanillaVillager();
public abstract ItemStack getTool(int level);
public abstract ItemStack[] getArmor(int level);

public abstract NpcVarsHelper getVarsHelper(NpcBase npc);
public abstract void addTargets(NpcBase npc, NpcTargetHelper helper);


public abstract class NpcVarsHelper
{

private NpcBase npc;

public NpcVarsHelper(NpcBase npc)
  {
  this.npc = npc;
  }
public abstract void onTick();
public abstract float getVar1();
public abstract float getVar2();
public abstract float getVar3();
public abstract float getVar4();
public abstract float getVar5();
public abstract float getVar6();
public abstract float getVar7();
public abstract float getVar8();
}

}
