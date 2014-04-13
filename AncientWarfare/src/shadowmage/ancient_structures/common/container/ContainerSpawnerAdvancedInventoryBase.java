package shadowmage.ancient_structures.common.container;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_structures.common.inventory.InventoryBasic;
import shadowmage.ancient_structures.common.inventory.SlotFiltered;
import shadowmage.ancient_structures.common.inventory.SlotItemFilter;
import shadowmage.ancient_structures.common.item.AWStructuresItemLoader;
import shadowmage.ancient_structures.common.tile.SpawnerSettings;
import shadowmage.ancient_warfare.common.container.ContainerBase;

public class ContainerSpawnerAdvancedInventoryBase extends ContainerBase
{

SpawnerSettings settings;
InventoryBasic inventory;

public ContainerSpawnerAdvancedInventoryBase(EntityPlayer player)
  {
  super(player, null);  
  }

protected void addSettingsInventorySlots()
  {
  int xPos;
  int yPos;
  int slotNum;
  
  SlotItemFilter filter = new SlotItemFilter()
    {
    @Override
    public boolean isItemValid(ItemStack stack)
      {      
      if(stack!=null && stack.getItem() instanceof ItemBlock)
        {
        ItemBlock block = (ItemBlock)stack.getItem();
        if(block.getBlockID()==AWStructuresItemLoader.spawnerBlock.blockID)
          {
          return false;
          }
        }
      return true;
      }
    };
    
  for(int y = 0; y<3; y++)
    {
    yPos = y*18 + 8;
    for(int x = 0; x<3; x++)
      {
      xPos = x*18 + 8;//TODO find offset
      slotNum = y*3 + x;
      addSlotToContainer(new SlotFiltered(inventory, slotNum, xPos, yPos, filter));
      }
    }
  }

@Override
public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotClickedIndex)
  {
  ItemStack slotStackCopy = null;
  Slot theSlot = (Slot)this.inventorySlots.get(slotClickedIndex);
  if (theSlot != null && theSlot.getHasStack())
    {
    ItemStack slotStack = theSlot.getStack();
    slotStackCopy = slotStack.copy();
    int storageSlots = 9;  
    if (slotClickedIndex < 36)//player slots...
      {      
      if (!this.mergeItemStack(slotStack, 36, 36+storageSlots, false))//merge into storage inventory
        {
        return null;
        }
      }
    else if(slotClickedIndex >=36 &&slotClickedIndex < 36+storageSlots)//storage slots, merge to player inventory
      {
      if (!this.mergeItemStack(slotStack, 0, 36, true))//merge into player inventory
        {
        return null;
        }
      }
    if (slotStack.stackSize == 0)
      {
      theSlot.putStack((ItemStack)null);
      }
    else
      {
      theSlot.onSlotChanged();
      }
    if (slotStack.stackSize == slotStackCopy.stackSize)
      {
      return null;
      }
    theSlot.onPickupFromSlot(par1EntityPlayer, slotStack);
    }
  return slotStackCopy;
  }

public void sendSettingsToServer()
  {
  NBTTagCompound tag = new NBTTagCompound();
  settings.writeToNBT(tag);
  sendDataToServer(tag);
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

}
