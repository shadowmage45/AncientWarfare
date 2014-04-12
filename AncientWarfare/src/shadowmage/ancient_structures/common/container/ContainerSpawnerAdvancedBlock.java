package shadowmage.ancient_structures.common.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_structures.common.tile.TileAdvancedSpawner;

public class ContainerSpawnerAdvancedBlock extends ContainerSpawnerAdvancedBase
{

TileAdvancedSpawner spawner;
public ContainerSpawnerAdvancedBlock(EntityPlayer player, TileAdvancedSpawner te)
  {
  super(player, te.getSettings());
  spawner = te;
  }

@Override
public void onContainerClosed(EntityPlayer par1EntityPlayer)
  {
  super.onContainerClosed(par1EntityPlayer);
  }

@Override
public void sendSettingsToServer()
  {
  super.sendSettingsToServer();
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
      spawner.getSettings().readFromNBT(tag.getCompoundTag("spawnerSettings"));
      player.worldObj.markBlockForUpdate(spawner.xCoord, spawner.yCoord, spawner.zCoord);
      }
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
