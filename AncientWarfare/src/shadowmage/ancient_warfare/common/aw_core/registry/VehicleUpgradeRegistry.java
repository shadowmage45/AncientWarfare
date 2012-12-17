package shadowmage.ancient_warfare.common.aw_core.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.aw_core.item.ItemLoader;
import shadowmage.ancient_warfare.common.aw_vehicles.upgrades.VehicleUpgrade;

public class VehicleUpgradeRegistry
{
private VehicleUpgradeRegistry(){}
public static VehicleUpgradeRegistry instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new VehicleUpgradeRegistry();
    }
  return INSTANCE;
  }
private static VehicleUpgradeRegistry INSTANCE;

private Map upgradeTypeMap = new HashMap<Integer, VehicleUpgrade>();
private Map upgradeNameMap = new HashMap<String, VehicleUpgrade>();

/**
 * called from ItemLoader
 * @param dmg
 * @param type
 * @param upgrade
 */
public void registerUpgrade(int dmg, int type, VehicleUpgrade upgrade)
  {
  ItemStack upgradeStack = new ItemStack(ItemLoader.vehicleUpgrade,1,dmg); 
  ItemLoader.vehicleUpgrade.addSubType(upgradeStack); 
  this.upgradeTypeMap.put(type, upgrade);
  this.upgradeNameMap.put(upgrade.getUpgradeName(), upgrade);
  }

public VehicleUpgrade getUpgrade(String name)
  {
  return (VehicleUpgrade) this.upgradeNameMap.get(name);
  }

public VehicleUpgrade getUpgrade(int type)
  {
  return (VehicleUpgrade) this.upgradeTypeMap.get(type);
  }

}
