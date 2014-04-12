package shadowmage.ancient_structures.common.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_structures.common.tile.SpawnerSettings;
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
  if(tag.hasKey("spawnerSettings"))
    {
    if(player.worldObj.isRemote)
      {
      settings.readFromNBT(tag.getCompoundTag("spawnerSettings"));
      this.refreshGui();
      }
    else
      {
      spawner.readFromNBT(tag);
      player.worldObj.markBlockForUpdate(spawner.xCoord, spawner.yCoord, spawner.zCoord);
      }
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
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
