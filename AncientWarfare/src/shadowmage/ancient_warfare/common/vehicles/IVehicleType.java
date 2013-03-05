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
package shadowmage.ancient_warfare.common.vehicles;

public interface IVehicleType
{

public abstract boolean isMountable();
public abstract boolean isDrivable();
public abstract boolean isCombatEngine();
public abstract boolean canAdjustYaw();
public abstract boolean canAdjustPitch();
public abstract boolean canAdjustPower();

public abstract float getMissileForwardsOffset();
public abstract float getMissileHorizontalOffset();
public abstract float getMissileVerticalOffset();

public abstract float getRiderForwardsOffset();
public abstract float getRiderHorizontalOffset();
public abstract float getRiderVerticalOffest();



}
