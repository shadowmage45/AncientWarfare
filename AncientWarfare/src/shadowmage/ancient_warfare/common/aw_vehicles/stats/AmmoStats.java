package shadowmage.ancient_warfare.common.aw_vehicles.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shadowmage.ancient_warfare.common.aw_core.registry.VehicleAmmoRegistry;
import shadowmage.ancient_warfare.common.aw_core.registry.entry.VehicleAmmo;
import shadowmage.ancient_warfare.common.aw_core.registry.entry.VehicleAmmoEntry;
import shadowmage.ancient_warfare.common.aw_vehicles.VehicleBase;

public class AmmoStats
{

private VehicleBase vehicle;

/**
 * list of ammo types that have to be enabled by upgrades
 */
private ArrayList<VehicleAmmoEntry> validAmmoTypes = new ArrayList<VehicleAmmoEntry>();

public Map<Integer, VehicleAmmoEntry> localAmmoTypeMap = new HashMap<Integer, VehicleAmmoEntry>();

public AmmoStats(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

/**
 * initial registerAmmoType, called during instance construction
 * @param globalType
 */
public void addAmmoType(int globalType)
  {
  VehicleAmmo ammo = VehicleAmmoRegistry.instance().getEntryFor(globalType);
  if(ammo==null)
    {
    return;
    }
  VehicleAmmoEntry entry = new VehicleAmmoEntry(ammo);
  this.validAmmoTypes.add(entry);
  this.localAmmoTypeMap.put(this.localAmmoTypeMap.size(), entry);
  }

public VehicleAmmoEntry getAmmoGlobal(int globalType)
  {
  for(VehicleAmmoEntry entry : this.validAmmoTypes)
    {
    if(entry.getInfo().type==globalType)
      {
      return entry;
      }
    }
  return null;
  }

/**
 * return ammoType by localType
 * @param localType
 * @return
 */
public VehicleAmmoEntry getAmmo(int localType)
  {
  return this.localAmmoTypeMap.get(localType);
  }

/**
 * used by vehicle.inventory.isItemValidForSlot
 * @param id
 * @param dmg
 * @return
 */
public boolean isValidAmmo(int id, int dmg)
  {
  for(VehicleAmmoEntry entry : this.validAmmoTypes)
    {
    if(entry.getItemID().equals(id, dmg))
      {
      return true;
      }
    }
  return false;
  }

}
