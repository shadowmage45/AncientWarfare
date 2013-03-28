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

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.NpcTypeBase;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;

public class NpcSoldierTest extends NpcTypeBase
{

/**
 * @param type
 */
public NpcSoldierTest(int type)
  {
  super(type);
  this.displayName = "Soldier Test";
  this.tooltip = "Test Soldier for Attack and Vehicle Interaction";
  this.addLevel("Soldier Rank 0", "foo");
  this.addLevel("Soldier Rank 1", "foo");
  this.addLevel("Soldier Rank 2", "foo");
  }

@Override
public void addTargets(NpcTargetHelper helper)
  {
  helper.addTargetEntry("attack", EntityPlayer.class, 0, false, true);
  helper.addTargetEntry("attack", NpcBase.class, 1, false, true);
  helper.addTargetEntry("attack", EntityMob.class, 2, false, true);
  }

}
