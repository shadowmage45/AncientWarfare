package shadowmage.ancient_structures.common.tile;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_structures.common.inventory.InventoryBasic;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TileAdvancedSpawner extends TileEntity
{

private SpawnerSettings settings = new SpawnerSettings();

public TileAdvancedSpawner()
  {
  
  }

@Override
public boolean canUpdate()
  {
  return true;
  }

@Override
public void setWorldObj(World world)
  {
  super.setWorldObj(world);
  settings.setWorld(world, xCoord, yCoord, zCoord);
  }

@Override
public void updateEntity()
  {
  if(worldObj.isRemote){return;}
  if(settings.worldObj==null)
    {
    settings.setWorld(worldObj, xCoord, yCoord, zCoord);
    }
  settings.onUpdate();
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  NBTTagCompound ntag = new NBTTagCompound();  
  settings.writeToNBT(ntag);
  tag.setTag("spawnerSettings", ntag);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  settings.readFromNBT(tag.getCompoundTag("spawnerSettings"));
  }

@Override
public Packet getDescriptionPacket()
  {
  NBTTagCompound tag = new NBTTagCompound();
  settings.writeToNBT(tag);
  Packet132TileEntityData pkt = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);  
  return pkt;
  }

@Override
public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
  settings.readFromNBT(pkt.data);
  super.onDataPacket(net, pkt);
  worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
  worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
  super.onDataPacket(net, pkt);
  }

public SpawnerSettings getSettings()
  {
  return settings;
  }

public void setSettings(SpawnerSettings settings)
  {
  this.settings = settings;
  this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
  }

public float getBlockHardness()
  {
  return settings.blockHardness;
  }

public void onBlockBroken()
  {
  if(worldObj.isRemote){return;}
  int xp = settings.getXpToDrop();
  while (xp > 0)
    {
    int j = EntityXPOrb.getXPSplit(xp);
    xp -= j;
    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.xCoord+0.5d, this.yCoord, this.zCoord+0.5d, j));
    }
  InventoryBasic inv = settings.getInventory();
  ItemStack item;
  for(int i = 0; i < inv.getSizeInventory(); i++)
    {
    item = inv.getStackInSlot(i);
    if(item == null){continue;}
    InventoryTools.dropItemInWorld(worldObj, item, xCoord, yCoord, zCoord);
    }
  }

public void handleClientEvent(int a, int b)
  {
  
  }

}
