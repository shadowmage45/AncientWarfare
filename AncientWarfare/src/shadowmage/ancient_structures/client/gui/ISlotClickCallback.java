package shadowmage.ancient_structures.client.gui;

import net.minecraft.inventory.IInventory;

public interface ISlotClickCallback
{

public void onSlotClicked(IInventory inventory, int slotIndex, int button);

}
