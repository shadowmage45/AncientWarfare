package shadowmage.ancient_warfare.common.aw_vehicles.stats;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.aw_core.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.aw_core.registry.entry.VehicleUpgrade;
import shadowmage.ancient_warfare.common.aw_vehicles.VehicleBase;

public class UpgradeStats
{

/**
 * currently installed upgrades, will be iterated through linearly to call upgrade.applyEffects, multiple upgrades may have cumulative effects
 */
private List upgrades = new ArrayList<VehicleUpgrade>();

/**
 * list of all upgrades that are valid for this vehicle, used by inventoryChecking to see whether it can be installed or not
 */
private List validUpgrades = new ArrayList<VehicleUpgrade>();
private VehicleBase vehicle;

public UpgradeStats(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

public void updateUpgrades()
  {
  //set all alterable values back to base values
  //apply effects from upgrades linearly
  }

public void addValidUpgrade(int type)
  {
  VehicleUpgrade upgrade = VehicleUpgradeRegistry.instance().getUpgrade(type);
  if(upgrade!=null && !this.validUpgrades.contains(upgrade))
    {
    this.validUpgrades.add(upgrade);
    }
  }

public void addValidUpgrade(String name)
  {
  VehicleUpgrade upgrade = VehicleUpgradeRegistry.instance().getUpgrade(name);
  if(upgrade!=null && !this.validUpgrades.contains(upgrade))
    {
    this.validUpgrades.add(upgrade);
    }
  }

}
