package shadowmage.ancient_structures.client.gui.elements;

import net.minecraft.inventory.IInventory;
import shadowmage.ancient_structures.client.gui.GuiContainerBase.ActivationEvent;
import shadowmage.ancient_structures.client.gui.ISlotClickCallback;
import shadowmage.ancient_structures.client.gui.ITooltipRenderer;
import shadowmage.ancient_structures.client.gui.Listener;

public class ItemSlotInventoried extends ItemSlot
{

int slotIndex;
IInventory inventory;
ISlotClickCallback containerCallback;

public ItemSlotInventoried(int topLeftX, int topLeftY, IInventory inv, int slot, ISlotClickCallback container, ITooltipRenderer render)
  {
  super(topLeftX, topLeftY, inv.getStackInSlot(slot), render);
  this.inventory = inv;
  this.slotIndex = slot;
  this.containerCallback = container;
  this.addNewListener(new Listener(Listener.MOUSE_UP)
    {
    @Override
    public boolean onEvent(GuiElement widget, ActivationEvent evt)
      {
      if(visible && enabled && containerCallback!=null && isMouseOverElement(evt.mx, evt.my) )
        {        
        containerCallback.onSlotClicked(inventory, slotIndex, evt.mButton);  
        item = inventory.getStackInSlot(slotIndex);      
        }
      return true;
      }
    });
  }

@Override
public void render(int mouseX, int mouseY, float partialTick)
  {  
  this.item = inventory.getStackInSlot(slotIndex);
  super.render(mouseX, mouseY, partialTick);
  }

}
