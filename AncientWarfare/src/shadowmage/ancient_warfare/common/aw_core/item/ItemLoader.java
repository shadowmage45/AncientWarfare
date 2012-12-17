package shadowmage.ancient_warfare.common.aw_core.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.registry.DescriptionRegistry;
import shadowmage.ancient_warfare.common.aw_core.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.aw_vehicles.upgrades.VehicleUpgrade;

public class ItemLoader
{

public static AWItemBase vehicleUpgrade = new AWItemBase(Config.getItemID("itemMulti.vehicleUpgrade", 13001, "Base item for all vehicle upgrades"),true);

private static ItemLoader INSTANCE;
private ItemLoader(){}
public static ItemLoader instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new ItemLoader();
    }
  return INSTANCE;
  }


/**
 * initial load, called during pre-init from mod core file
 */
public void load()
  {
  
  }

/**
 * special registerUpgrade method, directly registers a new upgrade using the vehicleUpgrade item.  calls all necessary calls
 * for languageRegistry, descriptionRegistry, etc----null upgrades are not registered
 * @param dmg
 * @param type
 * @param upgrade
 */
public void registerVehicleUpgradeItem(int dmg, int type, VehicleUpgrade upgrade)
  {
  if(upgrade!=null)
    {
    this.registerItem(vehicleUpgrade, dmg, upgrade.getUpgradeDisplayName());
    VehicleUpgradeRegistry.instance().registerUpgrade(dmg, type, upgrade);
    }
  }

public void registerItem(Item item, int dmg, String name)
  {
  DescriptionRegistry.instance().registerItem(item.shiftedIndex, dmg, name);
  }


}
