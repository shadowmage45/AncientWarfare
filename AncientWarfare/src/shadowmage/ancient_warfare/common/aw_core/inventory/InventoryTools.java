package shadowmage.ancient_warfare.common.aw_core.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;


public class InventoryTools
{
private InventoryTools(){}
public static InventoryTools instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new InventoryTools();
    }
  return INSTANCE;
  }
private static InventoryTools INSTANCE;

public static int getEmptySlots(IInventory inventory)
  {
  return 0;
  }

public static ItemStack merge(ItemStack stack, IInventory inventory, boolean iterateBackwards)
  {
  return merge(stack, inventory, 0, inventory.getSizeInventory(), iterateBackwards);
  }

public static ItemStack merge(ItemStack stack, IInventory inventory, int start, int stopBefore, boolean iterateBackwards)
  {
  return null;
  }

public static boolean containsItem(int id, int dmg, boolean useDmg)
  {
  return false;
  }

public static void decreaseItem(int id, int dmg, boolean useDmg)
 {
 
 }





}
