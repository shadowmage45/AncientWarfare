package shadowmage.ancient_warfare.common.aw_vehicles.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.aw_core.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.aw_vehicles.VehicleBase;

public class VehicleInventory extends AWInventoryBasic
{
private VehicleBase vehicle;

public int upgradeSlots;
public int ammoSlots;
public int armorSlots;
public int engineSlots;
public int fuelSlots;

public VehicleInventory(VehicleBase vehicle, int upgradeSlots, int ammoSlots, int armorSlots, int engineSlots, int fuelSlots)
  {
  super(upgradeSlots+ammoSlots+armorSlots+engineSlots+fuelSlots);
  this.vehicle = vehicle;
  this.upgradeSlots = upgradeSlots;
  this.ammoSlots = ammoSlots;
  this.armorSlots = armorSlots;
  this.engineSlots = engineSlots;
  this.fuelSlots = fuelSlots;
  }

public List getUpgradeSlots()
  {
  List items = new ArrayList<ItemStack>();
  for(int i = 0; i <this.upgradeSlots; i++)
    {
    items.add(this.getUpgradeInSlot(i));
    }
  return items;
  }

public ItemStack getUpgradeInSlot(int slot)  
  {
  if(slot<0 ||slot>=this.upgradeSlots)
    {
    return null;
    }
  return this.getStackInSlot(slot);
  }

public List getAmmoSlots()
  {
  List items = new ArrayList<ItemStack>();
  for(int i = 0; i <this.ammoSlots; i++)
    {
    items.add(this.getAmmoInSlot(i));
    }
  return items;
  }

public ItemStack getAmmoInSlot(int slot)
  {
  if(slot<0 || slot>=this.ammoSlots)
    {
    return null;
    }
  return this.getStackInSlot(upgradeSlots+slot);
  }

public List getArmorSlots()
  {
  List items = new ArrayList<ItemStack>();
  for(int i = 0; i <this.armorSlots; i++)
    {
    items.add(this.getArmorInSlot(i));
    }
  return items;
  }

public ItemStack getArmorInSlot(int slot)
  {
  if(slot<0 || slot>= this.armorSlots)
    {
    return null;
    }
  return this.getStackInSlot(upgradeSlots+ammoSlots+slot);
  }

public ItemStack getEngineInSlot(int slot)
  {
  if(slot<0 || slot>= this.engineSlots)
    {
    return null;
    }
  return this.getStackInSlot(upgradeSlots+ammoSlots+armorSlots+slot);
  }

public ItemStack getFuelInSlot(int slot)
  {
  if(slot<0 || slot>=this.fuelSlots)
    {
    return null;
    }
  return this.getStackInSlot(upgradeSlots+ammoSlots+armorSlots+engineSlots+slot);
  }

public boolean isValidForSlot(ItemStack stack, int slot)
  {
  if(slot<0 || slot>=this.getSizeInventory())
    {
    return false;
    }
  else if(slot<this.upgradeSlots)
    {
    //TODO return if is valid item from vehicle upgrade valid upgrades list
    }
  else if(slot<upgradeSlots+ammoSlots)
    {
    //TODO return if is valid item from vehicle valid ammo list
    }
  else if(slot<upgradeSlots+ammoSlots+armorSlots)
    {
    //TODO return if is valid armor
    }
  else if(slot<upgradeSlots+ammoSlots+armorSlots+engineSlots)
    {
    //TODO return if is valid engine
    }  
  return false;
  }

}
