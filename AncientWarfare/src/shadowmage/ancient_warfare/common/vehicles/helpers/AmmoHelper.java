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
import java.util.HashMap;
import java.util.List;

import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.missiles.MissileBase;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.registry.entry.VehicleAmmoEntry;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AmmoHelper
{

private VehicleBase vehicle;

public int currentAmmoType = 0;

private List<VehicleAmmoEntry> ammoEntries = new ArrayList<VehicleAmmoEntry>();
private HashMap<Integer, IAmmoType> ammoTypes = new HashMap<Integer, IAmmoType>();//local ammo type to global entry

public AmmoHelper(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

public void addUseableAmmo(IAmmoType ammo)
  {
  this.ammoEntries.add(new VehicleAmmoEntry(ammo));
  this.ammoTypes.put(this.ammoEntries.size()-1, ammo);
  }

public void updateAmmoCounts()
  {
  
  }

public IAmmoType getCurrentAmmoType()
  {
  return this.ammoTypes.get(currentAmmoType);
  }

public MissileBase getMissile(float x, float y, float z, float mx, float my, float mz)
  {
  IAmmoType ammo = this.getCurrentAmmoType();
  if(ammo!=null)
    {
    MissileBase missile = new MissileBase(vehicle.worldObj);   
    missile.setMissileParams(ammo, x, y, z, mx, my, mz);
    return missile;
    }
  return null;  
  }

}
