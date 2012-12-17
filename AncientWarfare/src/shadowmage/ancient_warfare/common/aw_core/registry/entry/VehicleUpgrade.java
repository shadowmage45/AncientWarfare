package shadowmage.ancient_warfare.common.aw_core.registry.entry;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.aw_vehicles.VehicleBase;

public abstract class VehicleUpgrade
{

boolean directlyEffectsVehicle = false;

public VehicleUpgrade()
  {
  
  }

/**
 * apply the effects of this upgrade to the passed-in vehicle
 * @param vehicle
 */
public abstract void applyUpgradeEffects(VehicleBase vehicle);

/**
 * get the full display name for this item, this name will be registered
 * with languageRegistry
 * @return
 */
public abstract String getUpgradeDisplayName();

/**
 * get the internal (short) name for this upgrade, must be unique or will
 * cause lookup conflicts
 * @return
 */
public abstract String getUpgradeName();

}
