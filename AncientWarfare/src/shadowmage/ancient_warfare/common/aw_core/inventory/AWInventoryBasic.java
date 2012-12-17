package shadowmage.ancient_warfare.common.aw_core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class AWInventoryBasic implements IInventory
{

ItemStack[] inventorySlots;
public AWInventoryBasic(int size)
  {
  this.inventorySlots = new ItemStack[size];
  }

@Override
public int getSizeInventory()
  {
  return this.inventorySlots.length;
  }

@Override
public ItemStack getStackInSlot(int var1)
  {
  if(var1 < this.inventorySlots.length && var1>=0)
    {
    return this.inventorySlots[var1];
    }
  return null;
  }

@Override
public ItemStack decrStackSize(int slotNum, int decreaseBy)
  {
  if (this.inventorySlots[slotNum] != null)
    {
    ItemStack tempStack;
    if (this.inventorySlots[slotNum].stackSize <= decreaseBy)
      {
      tempStack = this.inventorySlots[slotNum];
      this.inventorySlots[slotNum] = null;
      this.onInventoryChanged();
      return tempStack;
      }
    else
      {
      tempStack = this.inventorySlots[slotNum].splitStack(decreaseBy);
      if (this.inventorySlots[slotNum].stackSize == 0)
        {
        this.inventorySlots[slotNum] = null;
        }
      this.onInventoryChanged();
      return tempStack;
      }
    }
  else
    {
    return null;
    }
  }

@Override
public ItemStack getStackInSlotOnClosing(int var1)
  {
  if (this.inventorySlots[var1] != null)
    {
    ItemStack var2 = this.inventorySlots[var1];
    this.inventorySlots[var1] = null;
    return var2;
    }
  else
    {
    return null;
    }
  }

@Override
public void setInventorySlotContents(int stackIndex, ItemStack newContents)
  {
  this.inventorySlots[stackIndex] = newContents;
  if (newContents != null && newContents.stackSize > this.getInventoryStackLimit())
    {
    newContents.stackSize = this.getInventoryStackLimit();
    }
  this.onInventoryChanged();
  }

@Override
public String getInvName()
  {  
  return "DefaultInventory";
  }

@Override
public int getInventoryStackLimit()
  {  
  return 64;
  }

@Override
public void onInventoryChanged()
  {
  // TODO Auto-generated method stub
  }

@Override
public boolean isUseableByPlayer(EntityPlayer var1)
  {
  return true;
  }

@Override
public void openChest()
  {
  
  }

@Override
public void closeChest()
  {
  
  }

}
