package shadowmage.ancient_structures.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_structures.common.tile.SpawnerSettings;

public class ContainerSpawnerAdvancedInventoryItem extends ContainerSpawnerAdvancedInventoryBase
{

public ContainerSpawnerAdvancedInventoryItem(EntityPlayer player)
  {
  super(player);
  
  settings = new SpawnerSettings();
  ItemStack item = player.inventory.getCurrentItem();
  if(item==null || !item.hasTagCompound() || !item.getTagCompound().hasKey("spawnerSettings")){throw new IllegalArgumentException("stack cannot be null, and must have tag compounds!!");}
  settings.readFromNBT(item.getTagCompound().getCompoundTag("spawnerSettings"));
  inventory = settings.getInventory();
  
  this.addSettingsInventorySlots();
  this.addPlayerSlots(player, 8, 70, 8);
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("spawnerSettings"))
    {
    ItemStack item = player.inventory.getCurrentItem();
    item.setTagInfo("spawnerSettings", tag.getCompoundTag("spawnerSettings"));
    }
  }

}
