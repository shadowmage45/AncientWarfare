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
package shadowmage.ancient_warfare.common.vehicles.helpers;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.registry.entry.VehicleAmmoEntry;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AmmoHelper
{

private VehicleBase vehicle;

private List<VehicleAmmoEntry> ammoTypes = new ArrayList<VehicleAmmoEntry>();

public AmmoHelper(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

public void addUseableAmmo(IAmmoType ammo)
  {
  this.ammoTypes.add(new VehicleAmmoEntry(ammo));
  }

public void updateAmmoCounts()
  {
  //TODO
  }


}
