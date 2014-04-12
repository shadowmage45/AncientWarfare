package shadowmage.ancient_structures.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_structures.common.tile.SpawnerSettings;

public abstract class ContainerSpawnerAdvancedBase extends shadowmage.ancient_warfare.common.container.ContainerBase
{

public SpawnerSettings settings;

public ContainerSpawnerAdvancedBase(EntityPlayer player, SpawnerSettings settings)
  {
  super(player, null);
  this.settings = settings;
  }

public void sendSettingsToServer()
  {
  NBTTagCompound tag = new NBTTagCompound();
  settings.writeToNBT(tag);
  this.sendDataToServer(tag);
  }

}
