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
package shadowmage.ancient_warfare.common.research.vehicle;

import shadowmage.ancient_warfare.common.research.ResearchGoal;

public class ResearchTurrets extends ResearchGoal
{

/**
 * @param num
 */
public ResearchTurrets(int num, int level)
  {
  super(num);
  this.displayName = "Turreted Vehicles " + (level+1);
  this.detailedDescription.add("Researching Turreted Vehicles advances knowledge regarding the" +
  		"construction and design of turret-based vehicles of various designs. Higher ranks" +
  		" unlock access to higher tiers of turreted vehicles (if all other prerequisites" +
  		" for the vehicle have been met).");
  		
  }

}
