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
package shadowmage.ancient_warfare.common.vehicles.types;

public class VehicleTypeTrebuchetLarge extends VehicleTypeTrebuchet
{

  /**
   * @param typeNum
   */
  public VehicleTypeTrebuchetLarge(int typeNum)
    {
    super(typeNum);
    this.displayName = "Trebuchet Large";
    this.displayTooltip = "A large and powerful fixed emplacement trebuchet.";
    this.width = 2 * 2.5f;
    this.height = 2 * 2.5f;
    this.riderForwardsOffset = 1.425f * 2.5f;
    this.riderVerticalOffset = 0.5f;    
    this.baseMissileVelocityMax = 50.f;
    this.turretVerticalOffset = (34.f + 67.5f + 24.0f)*0.0625f*2.5f;
    }

}
