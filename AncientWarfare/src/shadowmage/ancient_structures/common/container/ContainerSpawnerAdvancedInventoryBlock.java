package shadowmage.ancient_structures.common.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_structures.common.tile.TileAdvancedSpawner;

public class ContainerSpawnerAdvancedInventoryBlock extends ContainerSpawnerAdvancedInventoryBase
{

TileAdvancedSpawner spawner;
public ContainerSpawnerAdvancedInventoryBlock(EntityPlayer player, TileAdvancedSpawner te)
  {
  super(player);  
  spawner = te;
  settings = spawner.getSettings();
  inventory = settings.getInventory();  
  this.addSettingsInventorySlots();
  this.addPlayerSlots(player, 8, 70, 8);
  }

private void sendSettingsToClient()
  {
  NBTTagCompound tag = new NBTTagCompound();
  settings.writeToNBT(tag);
  sendDataToPlayer(tag);
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(player.worldObj.isRemote)
    {
    settings.readFromNBT(tag);
    this.refreshGui();
    }
  else
    {
    spawner.readFromNBT(tag);
    player.worldObj.markBlockForUpdate(spawner.xCoord, spawner.yCoord, spawner.zCoord);
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  handlePacketData(tag);
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  List<NBTTagCompound> list = new ArrayList<NBTTagCompound>();
  NBTTagCompound tag = new NBTTagCompound();
  settings.writeToNBT(tag);
  list.add(tag);
  return list;
  }

}
