package shadowmage.ancient_structures.common.container;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_structures.common.tile.SpawnerSettings;

public class ContainerSpawnerAdvanced extends ContainerSpawnerAdvancedBase
{

public ContainerSpawnerAdvanced(EntityPlayer player)
  {
  super(player, null);
  settings = new SpawnerSettings();
  ItemStack item = player.inventory.getCurrentItem();
  if(item==null || !item.hasTagCompound() || !item.getTagCompound().hasKey("spawnerSettings")){throw new IllegalArgumentException("stack cannot be null, and must have tag compounds!!");}
  settings.readFromNBT(item.getTagCompound().getCompoundTag("spawnerSettings"));
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

@Override
public void handleInitData(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

}
