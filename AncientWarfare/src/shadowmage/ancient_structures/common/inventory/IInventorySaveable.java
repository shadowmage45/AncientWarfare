package shadowmage.ancient_structures.common.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public interface IInventorySaveable extends IInventory
{

public void readFromNBT(NBTTagCompound tag);
public void writeToNBT(NBTTagCompound tag);

public boolean isDirty();

}
